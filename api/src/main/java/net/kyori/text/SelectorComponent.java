/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A scoreboard selector component.
 */
public class SelectorComponent extends AbstractBuildableComponent<SelectorComponent, SelectorComponent.Builder> implements ScopedComponent<SelectorComponent> {
  /**
   * The selector pattern.
   */
  private final String pattern;

  /**
   * Creates a selector component builder.
   *
   * @return a builder
   */
  public static @NonNull Builder builder() {
    return new Builder();
  }

  /**
   * Creates a selector component builder with a pattern.
   *
   * @param pattern the selector pattern
   * @return a builder
   */
  public static @NonNull Builder builder(final @NonNull String pattern) {
    return new Builder().pattern(pattern);
  }

  /**
   * Creates a selector component with a pattern.
   *
   * @param pattern the selector pattern
   * @return the selector component
   */
  public static @NonNull SelectorComponent of(final @NonNull String pattern) {
    return builder(pattern).build();
  }

  protected SelectorComponent(final @NonNull Builder builder) {
    super(builder);
    this.pattern = requireNonNull(builder.pattern, "pattern");
  }

  protected SelectorComponent(final @NonNull List<Component> children, final @NonNull Style style, final @NonNull String pattern) {
    super(children, style);
    this.pattern = pattern;
  }

  /**
   * Gets the selector pattern.
   *
   * @return the selector pattern
   */
  public @NonNull String pattern() {
    return this.pattern;
  }

  /**
   * Sets the selector pattern.
   *
   * @param pattern the selector pattern
   * @return a copy of this component
   */
  public @NonNull SelectorComponent pattern(final @NonNull String pattern) {
    return new SelectorComponent(this.children, this.style, requireNonNull(pattern, "pattern"));
  }

  @Override
  public @NonNull SelectorComponent style(final @NonNull Style style) {
    return new SelectorComponent(this.children, style, this.pattern);
  }

  @Override
  public @NonNull SelectorComponent append(final @NonNull Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> children = new ArrayList<>(this.children.size() + 1);
    children.addAll(this.children);
    children.add(component);
    return new SelectorComponent(children, this.style, this.pattern);
  }

  @Override
  public @NonNull SelectorComponent copy() {
    return new SelectorComponent(this.children, this.style, this.pattern);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof SelectorComponent)) return false;
    if(!super.equals(other)) return false;
    final SelectorComponent that = (SelectorComponent) other;
    return Objects.equals(this.pattern, that.pattern);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.pattern);
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("pattern", this.pattern);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * A selector component builder.
   */
  public static class Builder extends AbstractComponentBuilder<SelectorComponent, Builder> {
    private @Nullable String pattern;

    Builder() {
    }

    Builder(final @NonNull SelectorComponent component) {
      super(component);
      this.pattern = component.pattern();
    }

    /**
     * Sets the selector pattern.
     *
     * @param pattern the selector pattern
     * @return this builder
     */
    public @NonNull Builder pattern(final @NonNull String pattern) {
      this.pattern = pattern;
      return this;
    }

    @Override
    public @NonNull SelectorComponent build() {
      if(this.pattern == null) throw new IllegalStateException("pattern must be set");
      return new SelectorComponent(this);
    }
  }
}
