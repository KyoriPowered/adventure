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

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * A translatable text component.
 */
public class TranslatableComponent extends AbstractBuildableComponent<TranslatableComponent, TranslatableComponent.Builder> {
  /**
   * The translation key.
   */
  private final String key;
  /**
   * The list of translation arguments.
   */
  private final @NonNull List<Component> args;

  /**
   * Creates a translatable component builder.
   *
   * @return a builder
   */
  public static @NonNull Builder translatable() {
    return new Builder();
  }

  /**
   * Creates a translatable component builder.
   *
   * @return a builder
   */
  public static @NonNull Builder builder() {
    return new Builder();
  }

  /**
   * Creates a translatable component builder with a translation key.
   *
   * @param key the translation key
   * @return a builder
   */
  public static @NonNull Builder translatable(final @NonNull String key) {
    return new Builder().key(key);
  }

  /**
   * Creates a translatable component builder with a translation key.
   *
   * @param key the translation key
   * @return a builder
   */
  public static @NonNull Builder builder(final @NonNull String key) {
    return new Builder().key(key);
  }

  /**
   * Creates a translatable component with a translation key.
   *
   * @param key the translation key
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent of(final @NonNull String key) {
    return builder(key).build();
  }

  /**
   * Creates a translatable component with a translation key.
   *
   * @param key the translation key
   * @param color the color
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color) {
    return builder(key)
      .color(color)
      .build();
  }

  /**
   * Creates a translatable component with a translation key.
   *
   * @param key the translation key
   * @param color the color
   * @param decorations the decorations
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(key)
      .color(color)
      .decorations(decorations, true)
      .build();
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param args the translation arguments
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent of(final @NonNull String key, final @NonNull Component... args) {
    return of(key, null, args);
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param color the color
   * @param args the translation arguments
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull Component... args) {
    return of(key, color, Collections.emptySet(), args);
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param color the color
   * @param decorations the decorations
   * @param args the translation arguments
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations, final @NonNull Component... args) {
    return of(key, color, decorations, Arrays.asList(args));
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param args the translation arguments
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent of(final @NonNull String key, final @NonNull List<Component> args) {
    return of(key, null, args);
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param color the color
   * @param args the translation arguments
   * @return the translatable component
   */
  public static TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull List<Component> args) {
    return of(key, color, Collections.emptySet(), args);
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param color the color
   * @param decorations the decorations
   * @param args the translation arguments
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations, final @NonNull List<Component> args) {
    return builder()
      .color(color)
      .decorations(decorations, true)
      .key(key)
      .args(args)
      .build();
  }

  /**
   * Creates a translatable component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent make(final @NonNull Consumer<Builder> consumer) {
    final Builder builder = builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a translatable component by applying configuration from {@code consumer}.
   *
   * @param key the translation key
   * @param consumer the builder configurator
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent make(final @NonNull String key, final @NonNull Consumer<Builder> consumer) {
    final Builder builder = builder(key);
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a translatable component by applying configuration from {@code consumer}.
   *
   * @param key the translation key
   * @param args the translation arguments
   * @param consumer the builder configurator
   * @return the translatable component
   */
  public static @NonNull TranslatableComponent make(final @NonNull String key, final @NonNull List<Component> args, final @NonNull Consumer<Builder> consumer) {
    final Builder builder = builder(key).args(args);
    consumer.accept(builder);
    return builder.build();
  }

  protected TranslatableComponent(final @NonNull Builder builder) {
    super(builder);
    this.key = builder.key;
    this.args = Collections.unmodifiableList(new ArrayList<>(builder.args));
  }

  protected TranslatableComponent(final @NonNull List<Component> children, final @NonNull Style style, final @NonNull String key, final @NonNull List<Component> args) {
    super(children, style);
    this.key = key;
    this.args = Collections.unmodifiableList(new ArrayList<>(args));
  }

  /**
   * Gets the translation key.
   *
   * @return the translation key
   */
  public @NonNull String key() {
    return this.key;
  }

  /**
   * Sets the translation key.
   *
   * @param key the translation key
   * @return a copy of this component
   */
  public @NonNull TranslatableComponent key(final @NonNull String key) {
    return new TranslatableComponent(this.children, this.style, requireNonNull(key, "key"), this.args);
  }

  /**
   * Gets the unmodifiable list of translation arguments.
   *
   * @return the unmodifiable list of translation arguments
   */
  public @NonNull List<Component> args() {
    return Collections.unmodifiableList(this.args);
  }

  /**
   * Sets the translation arguments for this component.
   *
   * @param args the translation arguments
   * @return this component
   */
  public @NonNull TranslatableComponent args(final @NonNull List<Component> args) {
    return new TranslatableComponent(this.children, this.style, this.key, args);
  }

  @Override
  public @NonNull TranslatableComponent append(final @NonNull Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> children = new ArrayList<>(this.children.size() + 1);
    children.addAll(this.children);
    children.add(component);
    return new TranslatableComponent(children, this.style, this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent color(final @Nullable TextColor color) {
    return new TranslatableComponent(this.children, this.style.color(color), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent decoration(final @NonNull TextDecoration decoration, final boolean flag) {
    return (TranslatableComponent) super.decoration(decoration, flag);
  }

  @Override
  public @NonNull TranslatableComponent decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    return new TranslatableComponent(this.children, this.style.decoration(decoration, state), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent clickEvent(final @Nullable ClickEvent event) {
    return new TranslatableComponent(this.children, this.style.clickEvent(event), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent hoverEvent(final @Nullable HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    return new TranslatableComponent(this.children, this.style.hoverEvent(event), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent insertion(final @Nullable String insertion) {
    return new TranslatableComponent(this.children, this.style.insertion(insertion), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent mergeStyle(final @NonNull Component that) {
    return new TranslatableComponent(this.children, this.style.mergeStyle(that.style()), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent mergeColor(final @NonNull Component that) {
    return new TranslatableComponent(this.children, this.style.mergeColor(that.style()), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent mergeDecorations(final @NonNull Component that) {
    return new TranslatableComponent(this.children, this.style.mergeDecorations(that.style()), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent mergeEvents(final @NonNull Component that) {
    return new TranslatableComponent(this.children, this.style.mergeEvents(that.style()), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent resetStyle() {
    return new TranslatableComponent(this.children, this.style.resetStyle(), this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent copy() {
    return new TranslatableComponent(this.children, this.style, this.key, this.args);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof TranslatableComponent)) return false;
    if(!super.equals(other)) return false;
    final TranslatableComponent that = (TranslatableComponent) other;
    return Objects.equals(this.key, that.key) && Objects.equals(this.args, that.args);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.key, this.args);
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("key", this.key);
    builder.put("args", this.args);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * A text component builder.
   */
  public static class Builder extends AbstractComponentBuilder<TranslatableComponent, Builder> {
    private @Nullable String key;
    private @NonNull List<Component> args = EMPTY_COMPONENT_LIST;

    Builder() {
    }

    Builder(final @NonNull TranslatableComponent component) {
      super(component);
      this.key = component.key();
      this.args = component.args();
    }

    /**
     * Sets the translation key.
     *
     * @param key the translation key
     * @return this builder
     */
    public @NonNull Builder key(final @NonNull String key) {
      this.key = key;
      return this;
    }

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     */
    public @NonNull Builder args(final @NonNull Component... args) {
      return this.args(Arrays.asList(args));
    }

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     */
    public @NonNull Builder args(final @NonNull List<Component> args) {
      this.args = args;
      return this;
    }

    @Override
    public @NonNull TranslatableComponent build() {
      if(this.key == null) throw new IllegalStateException("key must be set");
      return new TranslatableComponent(this);
    }
  }
}
