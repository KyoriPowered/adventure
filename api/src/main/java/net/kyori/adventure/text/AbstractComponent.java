/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * An abstract implementation of a text component.
 *
 * @since 4.0.0
 */
public abstract class AbstractComponent implements Component {
  static List<Component> asComponents(final List<? extends ComponentLike> list) {
    return asComponents(list, false);
  }

  static List<Component> asComponents(final List<? extends ComponentLike> list, final boolean allowEmpty) {
    if(list.isEmpty()) {
      // We do not need to create a new list if the one we are copying is empty - we can
      // simply just return our known-empty list instead.
      return Collections.emptyList();
    }
    final List<Component> components = new ArrayList<>(list.size());
    for(int i = 0, size = list.size(); i < size; i++) {
      final ComponentLike like = list.get(i);
      final Component component = like.asComponent();
      if(allowEmpty || component != Component.empty()) {
        components.add(component);
      }
    }
    return Collections.unmodifiableList(components);
  }

  static <T> List<T> addOne(final List<T> oldList, final T newElement) {
    if(oldList.isEmpty()) return Collections.singletonList(newElement);
    final List<T> newList = new ArrayList<>(oldList.size() + 1);
    newList.addAll(oldList);
    newList.add(newElement);
    return newList;
  }

  /**
   * The list of children.
   */
  protected final List<Component> children;
  /**
   * The style of this component.
   */
  protected final Style style;

  protected AbstractComponent(final @NonNull List<? extends ComponentLike> children, final @NonNull Style style) {
    this.children = asComponents(children);
    this.style = style;
  }

  @Override
  public final @NonNull List<Component> children() {
    return this.children;
  }

  @Override
  public final @NonNull Style style() {
    return this.style;
  }

  @Override
  public @NonNull Component replaceText(final @NonNull Consumer<TextReplacementConfig.Builder> configurer) {
    requireNonNull(configurer, "configurer");
    return this.replaceText(Buildable.configureAndBuild(TextReplacementConfig.builder(), configurer));
  }

  @Override
  public @NonNull Component replaceText(final @NonNull TextReplacementConfig config) {
    requireNonNull(config, "replacement");
    if(!(config instanceof TextReplacementConfigImpl)) {
      throw new IllegalArgumentException("Provided replacement was a custom TextReplacementConfig implementation, which is not supported.");
    }
    return TextReplacementRenderer.INSTANCE.render(this, ((TextReplacementConfigImpl) config).createState());
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof AbstractComponent)) return false;
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

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("children", this.children),
      ExaminableProperty.of("style", this.style)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }
}
