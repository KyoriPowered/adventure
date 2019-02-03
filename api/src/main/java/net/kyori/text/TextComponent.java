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
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * A plain text component.
 */
public class TextComponent extends AbstractBuildableComponent<TextComponent, TextComponent.Builder> {
  /**
   * The plain text content.
   */
  private final @NonNull String content;

  /**
   * Creates a text component builder.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a text component builder with content.
   *
   * @param content the plain text content
   * @return a builder
   */
  public static Builder builder(final @NonNull String content) {
    return new Builder().content(content);
  }

  /**
   * Creates a text component with content.
   *
   * @param content the plain text content
   * @return the text component
   */
  public static TextComponent of(final @NonNull String content) {
    return builder(content).build();
  }

  /**
   * Creates a text component with content, and optional color.
   *
   * @param content the plain text content
   * @param color the color
   * @return the text component
   */
  public static TextComponent of(final @NonNull String content, final @Nullable TextColor color) {
    return of(content, color, Collections.emptySet());
  }

  /**
   * Creates a text component with content, and optional color and decorations.
   *
   * @param content the plain text content
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  public static TextComponent of(final @NonNull String content, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    final Set<TextDecoration> activeDecorations = new HashSet<>(decorations.length);
    Collections.addAll(activeDecorations, decorations);
    return of(content, color, activeDecorations);
  }

  /**
   * Creates a text component with content, and optional color and decorations.
   *
   * @param content the plain text content
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  public static TextComponent of(final @NonNull String content, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(content).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a text component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the text component
   */
  public static TextComponent make(final @NonNull Consumer<Builder> consumer) {
    final Builder builder = builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a text component by applying configuration from {@code consumer}.
   *
   * @param content the plain text content
   * @param consumer the builder configurator
   * @return the text component
   */
  public static TextComponent make(final @NonNull String content, final @NonNull Consumer<Builder> consumer) {
    final Builder builder = builder(content);
    consumer.accept(builder);
    return builder.build();
  }

  protected TextComponent(final @NonNull Builder builder) {
    super(builder);
    this.content = builder.content;
  }

  protected TextComponent(final @NonNull List<Component> children, final @Nullable TextColor color, final TextDecoration.@NonNull State obfuscated, final TextDecoration.@NonNull State bold, final TextDecoration.@NonNull State strikethrough, final TextDecoration.@NonNull State underlined, final TextDecoration.@NonNull State italic, final @Nullable ClickEvent clickEvent, final @Nullable HoverEvent hoverEvent, final @Nullable String insertion, final @NonNull String content) {
    super(children, color, obfuscated, bold, strikethrough, underlined, italic, clickEvent, hoverEvent, insertion);
    this.content = content;
  }

  /**
   * Gets the plain text content.
   *
   * @return the plain text content
   */
  public @NonNull String content() {
    return this.content;
  }

  /**
   * Sets the plain text content.
   *
   * @param content the plain text content
   * @return a copy of this component
   */
  public @NonNull TextComponent content(final @NonNull String content) {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, requireNonNull(content, "content"));
  }

  @Override
  public @NonNull TextComponent append(final @NonNull Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> children = new ArrayList<>(this.children.size() + 1);
    children.addAll(this.children);
    children.add(component);
    return new TextComponent(children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Override
  public @NonNull TextComponent color(final @Nullable TextColor color) {
    return new TextComponent(this.children, color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Override
  public @NonNull TextComponent decoration(final @NonNull TextDecoration decoration, final boolean flag) {
    return (TextComponent) super.decoration(decoration, flag);
  }

  @Override
  public @NonNull TextComponent decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    switch(decoration) {
      case BOLD: return new TextComponent(this.children, this.color, this.obfuscated, requireNonNull(state, "flag"), this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
      case ITALIC: return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, requireNonNull(state, "flag"), this.clickEvent, this.hoverEvent, this.insertion, this.content);
      case UNDERLINED: return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, requireNonNull(state, "flag"), this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
      case STRIKETHROUGH: return new TextComponent(this.children, this.color, this.obfuscated, this.bold, requireNonNull(state, "flag"), this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
      case OBFUSCATED: return new TextComponent(this.children, this.color, requireNonNull(state, "flag"), this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  @Override
  public @NonNull TextComponent clickEvent(final @Nullable ClickEvent event) {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion, this.content);
  }

  @Override
  public @NonNull TextComponent hoverEvent(final @Nullable HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, event, this.insertion, this.content);
  }

  @Override
  public @NonNull TextComponent insertion(final @Nullable String insertion) {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion, this.content);
  }

  @Override
  public @NonNull TextComponent mergeStyle(final @NonNull Component that) {
    return new TextComponent(this.children, that.color(), that.decoration(TextDecoration.OBFUSCATED), that.decoration(TextDecoration.BOLD), that.decoration(TextDecoration.STRIKETHROUGH), that.decoration(TextDecoration.UNDERLINED), that.decoration(TextDecoration.ITALIC), that.clickEvent(), that.hoverEvent(), that.insertion(), this.content);
  }

  @Override
  public @NonNull TextComponent mergeColor(final @NonNull Component that) {
    return new TextComponent(this.children, that.color(), this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Override
  public @NonNull TextComponent mergeDecorations(final @NonNull Component that) {
    final TextDecoration.State obfuscated = that.decoration(TextDecoration.OBFUSCATED) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.OBFUSCATED) : this.obfuscated;
    final TextDecoration.State bold = that.decoration(TextDecoration.BOLD) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.BOLD) : this.bold;
    final TextDecoration.State strikethrough = that.decoration(TextDecoration.STRIKETHROUGH) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.STRIKETHROUGH) : this.strikethrough;
    final TextDecoration.State underlined = that.decoration(TextDecoration.UNDERLINED) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.UNDERLINED) : this.underlined;
    final TextDecoration.State italic = that.decoration(TextDecoration.ITALIC) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.ITALIC) : this.italic;
    return new TextComponent(this.children, this.color, obfuscated, bold, strikethrough, underlined, italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Override
  public @NonNull TextComponent mergeEvents(final @NonNull Component that) {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, that.clickEvent(), that.hoverEvent(), this.insertion, this.content);
  }

  @Override
  public @NonNull TextComponent resetStyle() {
    return new TextComponent(this.children, null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null, this.content);
  }

  @Override
  public @NonNull TextComponent copy() {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof TextComponent)) return false;
    if(!super.equals(other)) return false;
    final TextComponent component = (TextComponent) other;
    return Objects.equals(this.content, component.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.content);
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("content", this.content);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * A text component builder.
   */
  public static class Builder extends AbstractBuildableComponent.AbstractBuilder<TextComponent, Builder> {
    private @Nullable String content;

    Builder() {
    }

    Builder(final @NonNull TextComponent component) {
      super(component);
      this.content = component.content();
    }

    /**
     * Sets the plain text content.
     *
     * @param content the plain text content
     * @return this builder
     */
    public @NonNull Builder content(final @NonNull String content) {
      this.content = content;
      return this;
    }

    @Override
    public @NonNull TextComponent build() {
      if(this.content == null) throw new IllegalStateException("content must be set");
      return new TextComponent(this);
    }
  }
}
