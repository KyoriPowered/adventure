/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * An abstract implementation of a text component.
 *
 * @since 4.0.0
 */
@Debug.Renderer(text = "this.debuggerString()", childrenArray = "this.children().toArray()", hasChildren = "!this.children().isEmpty()")
public abstract class AbstractComponent implements Component {
  private static final Predicate<Component> NOT_EMPTY = component -> component != Component.empty();
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();

  /**
   * The list of children.
   */
  protected final List<Component> children;
  /**
   * The style of this component.
   */
  protected final Style style;

  protected AbstractComponent(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style) {
    this.children = ComponentLike.asComponents(children, NOT_EMPTY);
    this.style = style;
  }

  @Override
  public final @NotNull List<Component> children() {
    return this.children;
  }

  @Override
  public final @NotNull Style style() {
    return this.style;
  }

  @Override
  public @NotNull Component replaceText(final @NotNull Consumer<TextReplacementConfig.Builder> configurer) {
    requireNonNull(configurer, "configurer");
    return this.replaceText(Buildable.configureAndBuild(TextReplacementConfig.builder(), configurer));
  }

  @Override
  public @NotNull Component replaceText(final @NotNull TextReplacementConfig config) {
    requireNonNull(config, "replacement");
    if (!(config instanceof TextReplacementConfigImpl)) {
      throw new IllegalArgumentException("Provided replacement was a custom TextReplacementConfig implementation, which is not supported.");
    }
    return TextReplacementRenderer.INSTANCE.render(this, ((TextReplacementConfigImpl) config).createState());
  }

  @Override
  public @NotNull Component compact() {
    return this.optimize(null);
  }

  private Component optimize(final @Nullable Style parentStyle) {
    Component optimized = this.children(Collections.emptyList());
    if (parentStyle != null) {
      optimized = optimized.style(simplifyStyle(this.style(), parentStyle));
    }

    if (this.children.isEmpty()) {
      // leaf nodes do not need to be further optimized - there is no point
      return optimized;
    }

    // propagate the parent style context to children
    // by merging this component's style into the parent style
    Style childParentStyle = optimized.style();
    if (parentStyle != null) {
      childParentStyle = parentStyle.merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
    }

    // optimize all children
    final List<Component> childrenToAppend = new ArrayList<>(this.children.size());
    for (int i = 0; i < this.children.size(); ++i) {
      childrenToAppend.add(((AbstractComponent) this.children.get(i)).optimize(childParentStyle));
    }

    // try to merge children into this parent component
    for (final ListIterator<Component> it = childrenToAppend.listIterator(); it.hasNext();) {
      final Component child = it.next();
      final Style childStyle = child.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);

      if (optimized instanceof TextComponent && child instanceof TextComponent && Objects.equals(childStyle, childParentStyle)) {
        // merge child components into the parent if they are a text component with the same effective style
        // in context of their parent style
        optimized = joinText((TextComponent) optimized, (TextComponent) child);
        it.remove();

        // if the merged child had any children, retain them
        child.children().forEach(it::add);
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

      if (child instanceof TextComponent && neighbor instanceof TextComponent && childStyle.equals(neighborStyle)) {
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

  private static TextComponent joinText(final TextComponent one, final TextComponent two) {
    return Component.text(one.content() + two.content(), one.style());
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof AbstractComponent)) return false;
    final AbstractComponent that = (AbstractComponent) other;
    return Objects.equals(this.children, that.children)
      && Objects.equals(this.style, that.style);
  }

  @Override
  public int hashCode() {
    int result = this.children.hashCode();
    result = (31 * result) + this.style.hashCode();
    return result;
  }

  @SuppressWarnings("unused")
  private String debuggerString() {
    return StringExaminer.simpleEscaping().examine(this.examinableName(), this.examinablePropertiesWithoutChildren());
  }

  protected Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren() {
    return Stream.of(ExaminableProperty.of("style", this.style));
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      this.examinablePropertiesWithoutChildren(),
      Stream.of(
        ExaminableProperty.of("children", this.children)
      )
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }
}
