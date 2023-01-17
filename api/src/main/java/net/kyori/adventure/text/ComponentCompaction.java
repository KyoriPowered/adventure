/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

final class ComponentCompaction {
  @VisibleForTesting
  static final boolean SIMPLIFY_STYLE_FOR_BLANK_COMPONENTS = false;

  private ComponentCompaction() {
  }

  static Component compact(final @NotNull Component self, final @Nullable Style parentStyle) {
    final List<Component> children = self.children();
    Component optimized = self.children(Collections.emptyList());
    if (parentStyle != null) {
      optimized = optimized.style(self.style().unmerge(parentStyle));
    }

    final int childrenSize = children.size();

    if (childrenSize == 0) {
      // no children, style can be further simplified if self is blank
      if (isBlank(optimized)) {
        optimized = optimized.style(simplifyStyleForBlank(optimized.style(), parentStyle));
      }

      // leaf nodes do not need to be further optimized - there is no point
      return optimized;
    }

    // if there is only one child, check if self a useless empty component
    if (childrenSize == 1 && optimized instanceof TextComponent) {
      final TextComponent textComponent = (TextComponent) optimized;

      if (textComponent.content().isEmpty()) {
        final Component child = children.get(0);

        // merge the updated/parent style into the child before we return
        return child.style(child.style().merge(optimized.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET)).compact();
      }
    }

    // propagate the parent style context to children
    // by merging the parent's style into this component
    Style childParentStyle = optimized.style();
    if (parentStyle != null) {
      childParentStyle = childParentStyle.merge(parentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
    }

    // optimize all children
    final List<Component> childrenToAppend = new ArrayList<>(children.size());
    for (int i = 0; i < children.size(); ++i) {
      Component child = children.get(i);

      // compact child recursively
      child = compact(child, childParentStyle);

      // ignore useless empty children (regardless of its style)
      if (child.children().isEmpty() && child instanceof TextComponent) {
        final TextComponent textComponent = (TextComponent) child;

        if (textComponent.content().isEmpty()) {
          continue;
        }
      }

      childrenToAppend.add(child);
    }

    // try to merge children into this parent component
    if (optimized instanceof TextComponent) {
      while (!childrenToAppend.isEmpty()) {
        final Component child = childrenToAppend.get(0);
        final Style childStyle = child.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);

        if (child instanceof TextComponent && Objects.equals(childStyle, childParentStyle)) {
          // merge child components into the parent if they are a text component with the same effective style
          // in context of their parent style
          optimized = joinText((TextComponent) optimized, (TextComponent) child);
          childrenToAppend.remove(0);

          // if the merged child had any children, retain them
          childrenToAppend.addAll(0, child.children());
        } else {
          // this child can't be merged into the parent, so all children from now on must remain children
          break;
        }
      }
    }

    // try to concatenate any further children with their neighbor
    // until no further joining is possible
    for (int i = 0; i + 1 < childrenToAppend.size();) {
      final Component child = childrenToAppend.get(i);
      final Component neighbor = childrenToAppend.get(i + 1);

      if (child.children().isEmpty() && child instanceof TextComponent && neighbor instanceof TextComponent) {
        // calculate the children's styles in context of their parent style
        final Style childStyle = child.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
        final Style neighborStyle = neighbor.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);

        // check if styles are equivalent
        if (childStyle.equals(neighborStyle)) {
          final Component combined = joinText((TextComponent) child, (TextComponent) neighbor);

          // replace the child and its neighbor with the single, combined component
          childrenToAppend.set(i, combined);
          childrenToAppend.remove(i + 1);

          // don't increment the index -
          // we want to try and optimize this combined component even further
          continue;
        }
      }

      i++;
    }

    // no children, style can be further simplified if self is blank
    if (childrenToAppend.isEmpty() && isBlank(optimized)) {
      optimized = optimized.style(simplifyStyleForBlank(optimized.style(), parentStyle));
    }

    return optimized.children(childrenToAppend);
  }

  /**
  * Checks whether the Component is blank (a TextComponent containing only space characters).
  *
  * @param component the component to check
  * @return true if the provided component is blank, false otherwise
  */
  private static boolean isBlank(final Component component) {
    if (component instanceof TextComponent) {
      final TextComponent textComponent = (TextComponent) component;

      final String content = textComponent.content();

      for (int i = 0; i < content.length(); i++) {
        final char c = content.charAt(i);
        if (c != ' ') return false;
      }

      return true;
    }
    return false;
  }

  /**
  * Simplify the provided style to remove any information that is redundant,
  * given that the content is blank.
  *
  * @param style style to simplify
  * @param parentStyle style from component's parents, for context
  * @return a new, simplified style
  */
  private static @NotNull Style simplifyStyleForBlank(final @NotNull Style style, final @Nullable Style parentStyle) {
    if (!SIMPLIFY_STYLE_FOR_BLANK_COMPONENTS) {
      // todo: can this be fixed a better way?
      // https://github.com/KyoriPowered/adventure/issues/849
      return style;
    }

    final Style.Builder builder = style.toBuilder();

    // TextColor doesn't affect spaces, unless there is other decoration present
    if (!(style.hasDecoration(TextDecoration.UNDERLINED) || style.hasDecoration(TextDecoration.STRIKETHROUGH))
      && (parentStyle == null || !(parentStyle.hasDecoration(TextDecoration.UNDERLINED) || parentStyle.hasDecoration(TextDecoration.STRIKETHROUGH)))) {
      builder.color(null);
    }

    // ITALIC/OBFUSCATED don't affect spaces (in modern versions), as these styles only affect glyph rendering
    builder.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
    builder.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);

    // UNDERLINE/STRIKETHROUGH affects spaces because the line renders on top
    // BOLD affects spaces because it increments the character advance by 1

    // font affects spaces in 1.19+ (since 22w11a), due to the font glyph provider for spaces

    return builder.build();
  }

  private static TextComponent joinText(final TextComponent one, final TextComponent two) {
    return TextComponentImpl.create(two.children(), one.style(), one.content() + two.content());
  }
}
