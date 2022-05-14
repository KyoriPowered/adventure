/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

final class ComponentCompaction {
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();

  private ComponentCompaction() {
  }

  static Component compact(final @NotNull Component self, final @Nullable Style parentStyle) {
    final List<Component> children = self.children();
    Component optimized = self.children(Collections.emptyList());
    if (parentStyle != null) {
      optimized = optimized.style(simplifyStyle(self.style(), parentStyle));
    }

    final int childrenSize = children.size();

    if (childrenSize == 0) {
      // no children, style can be further simplified if self is blank
      if (isBlank(self)) {
        optimized = optimized.style(simplifyStyleForBlank(self.style()));
      }

      // leaf nodes do not need to be further optimized - there is no point
      return optimized;
    }

    // if there is only one child, check if self a useless empty component
    if (childrenSize == 1 && self instanceof TextComponent) {
      final TextComponent textComponent = (TextComponent) self;

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
    while (!childrenToAppend.isEmpty()) {
      final Component child = childrenToAppend.get(0);
      final Style childStyle = child.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);

      if (optimized instanceof TextComponent && child instanceof TextComponent && Objects.equals(childStyle, childParentStyle)) {
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

    // try to concatenate any further children with their neighbor
    // until no further joining is possible
    for (int i = 0; i + 1 < childrenToAppend.size();) {
      final Component child = childrenToAppend.get(i);
      final Component neighbor = childrenToAppend.get(i + 1);

      // calculate the children's styles in context of their parent style
      final Style childStyle = child.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
      final Style neighborStyle = neighbor.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);

      if (child.children().isEmpty() && child instanceof TextComponent && neighbor instanceof TextComponent && childStyle.equals(neighborStyle)) {
        final Component combined = joinText((TextComponent) child, (TextComponent) neighbor);

        // replace the child and its neighbor with the single, combined component
        childrenToAppend.set(i, combined);
        childrenToAppend.remove(i + 1);

        // don't increment the index -
        // we want to try and optimize this combined component even further
      } else {
        i++;
      }
    }

    // no children, style can be further simplified if self is blank
    if (childrenToAppend.isEmpty() && isBlank(self)) {
      optimized = optimized.style(simplifyStyleForBlank(self.style()));
    }

    return optimized.children(childrenToAppend);
  }

  /**
   * Simplify the provided style to remove any information that is redundant.
   *
   * @param style style to simplify
   * @param parentStyle parent to compare against
   * @return a new, simplified style
   */
  private static @NotNull Style simplifyStyle(final @NotNull Style style, final @NotNull Style parentStyle) {
    if (style.isEmpty()) {
      // the target style is empty, so there is nothing to simplify
      return style;
    }

    final Style.Builder builder = style.toBuilder();
    if (Objects.equals(style.font(), parentStyle.font())) {
      builder.font(null);
    }

    if (Objects.equals(style.color(), parentStyle.color())) {
      builder.color(null);
    }

    for (final TextDecoration decoration : DECORATIONS) {
      if (style.decoration(decoration) == parentStyle.decoration(decoration)) {
        builder.decoration(decoration, TextDecoration.State.NOT_SET);
      }
    }

    if (Objects.equals(style.clickEvent(), parentStyle.clickEvent())) {
      builder.clickEvent(null);
    }

    if (Objects.equals(style.hoverEvent(), parentStyle.hoverEvent())) {
      builder.hoverEvent(null);
    }

    if (Objects.equals(style.insertion(), parentStyle.insertion())) {
      builder.insertion(null);
    }

    return builder.build();
  }

  /**
  * Checks whether the Component is blank (a TextComponent containing only space characters)
  *
  * @param component the component to check
  * @return true if the provided component is blank, false otherwise
  */
  private static boolean isBlank(Component component) {
    if (component instanceof TextComponent) {
      final TextComponent textComponent = (TextComponent) component;
      
      for (char c : textComponent.content().toCharArray()) {
        if (c != ' ') return false;
      }
      
      return true;
    }
    return false;
  }
  
  /**
  * Simplify the provided style to remove any information that is redundant,
  * given that the content is blank
  *
  * @param style style to simplify
  * @return a new, simplified style
  */
  private static @NotNull Style simplifyStyleForBlank(final @NotNull Style style) {
    final Style.Builder builder = style.toBuilder();
    
    builder.color(null);
    builder.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
    builder.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
    
    return builder.build();
  }

  private static TextComponent joinText(final TextComponent one, final TextComponent two) {
    return TextComponentImpl.create(two.children(), one.style(), one.content() + two.content());
  }
}
