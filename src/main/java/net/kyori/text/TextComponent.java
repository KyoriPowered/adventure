/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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

import com.google.common.base.Objects;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A plain text component.
 */
public class TextComponent extends AbstractComponent {

  /**
   * The plain text content.
   */
  @Nonnull private final String content;

  /**
   * Creates a text component builder.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a text component with context.
   *
   * @param content the plain text content
   * @return the text component
   */
  public static TextComponent of(@Nonnull final String content) {
    return builder().content(content).build();
  }

  protected TextComponent(@Nonnull final Builder builder) {
    super(builder);
    this.content = builder.content;
  }

  protected TextComponent(@Nonnull final List<Component> children, @Nullable final TextColor color, @Nonnull final TextDecoration.State obfuscated, @Nonnull final TextDecoration.State bold, @Nonnull final TextDecoration.State strikethrough, @Nonnull final TextDecoration.State underlined, @Nonnull final TextDecoration.State italic, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent hoverEvent, @Nullable final String insertion, @Nonnull final String content) {
    super(children, color, obfuscated, bold, strikethrough, underlined, italic, clickEvent, hoverEvent, insertion);
    this.content = content;
  }

  /**
   * Gets the plain text content.
   *
   * @return the plain text content
   */
  @Nonnull
  public String content() {
    return this.content;
  }

  /**
   * Sets the plain text content.
   *
   * @param content the plain text content
   * @return a copy of this component
   */
  @Nonnull
  public TextComponent content(@Nonnull final String content) {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, checkNotNull(content, "content"));
  }

  @Nonnull
  @Override
  public TextComponent append(@Nonnull Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> children = new ArrayList<>(this.children.size() + 1);
    children.addAll(this.children);
    children.add(component);
    return new TextComponent(children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Nonnull
  @Override
  public TextComponent color(@Nullable TextColor color) {
    return new TextComponent(this.children, color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Nonnull
  @Override
  public TextComponent decoration(@Nonnull final TextDecoration decoration, final boolean flag) {
    return (TextComponent) super.decoration(decoration, flag);
  }

  @Nonnull
  @Override
  public TextComponent decoration(@Nonnull TextDecoration decoration, @Nonnull TextDecoration.State state) {
    switch(decoration) {
      case BOLD: return new TextComponent(this.children, this.color, this.obfuscated, checkNotNull(state, "flag"), this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
      case ITALIC: return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, checkNotNull(state, "flag"), this.clickEvent, this.hoverEvent, this.insertion, this.content);
      case UNDERLINE: return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, checkNotNull(state, "flag"), this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
      case STRIKETHROUGH: return new TextComponent(this.children, this.color, this.obfuscated, this.bold, checkNotNull(state, "flag"), this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
      case OBFUSCATED: return new TextComponent(this.children, this.color, checkNotNull(state, "flag"), this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  @Nonnull
  @Override
  public TextComponent clickEvent(@Nullable ClickEvent event) {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion, this.content);
  }

  @Nonnull
  @Override
  public TextComponent hoverEvent(@Nullable HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, event, this.insertion, this.content);
  }

  @Nonnull
  @Override
  public TextComponent insertion(@Nullable String insertion) {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion, this.content);
  }

  @Nonnull
  @Override
  public TextComponent mergeStyle(@Nonnull final Component that) {
    return new TextComponent(this.children, that.color(), that.decoration(TextDecoration.OBFUSCATED), that.decoration(TextDecoration.BOLD), that.decoration(TextDecoration.STRIKETHROUGH), that.decoration(TextDecoration.UNDERLINE), that.decoration(TextDecoration.ITALIC), that.clickEvent(), that.hoverEvent(), that.insertion(), this.content);
  }

  @Nonnull
  @Override
  public TextComponent mergeColor(@Nonnull Component that) {
    return new TextComponent(this.children, that.color(), this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Nonnull
  @Override
  public TextComponent mergeDecorations(@Nonnull Component that) {
    final TextDecoration.State obfuscated = that.decoration(TextDecoration.OBFUSCATED) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.OBFUSCATED) : this.obfuscated;
    final TextDecoration.State bold = that.decoration(TextDecoration.BOLD) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.BOLD) : this.bold;
    final TextDecoration.State strikethrough = that.decoration(TextDecoration.STRIKETHROUGH) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.STRIKETHROUGH) : this.strikethrough;
    final TextDecoration.State underlined = that.decoration(TextDecoration.UNDERLINE) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.UNDERLINE) : this.underlined;
    final TextDecoration.State italic = that.decoration(TextDecoration.ITALIC) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.ITALIC) : this.italic;
    return new TextComponent(this.children, this.color, obfuscated, bold, strikethrough, underlined, italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Nonnull
  @Override
  public TextComponent mergeEvents(@Nonnull Component that) {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, that.clickEvent(), that.hoverEvent(), this.insertion, this.content);
  }

  @Nonnull
  @Override
  public TextComponent resetStyle() {
    return new TextComponent(this.children, null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null, this.content);
  }

  @Nonnull
  @Override
  public TextComponent copy() {
    return new TextComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.content);
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof TextComponent)) return false;
    if(!super.equals(other)) return false;
    final TextComponent component = (TextComponent) other;
    return Objects.equal(this.content, component.content);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), this.content);
  }

  @Override
  protected void populateToString(@Nonnull final Objects.ToStringHelper builder) {
    builder.add("content", this.content);
  }

  /**
   * A text component builder.
   */
  public static class Builder extends AbstractComponent.AbstractBuilder<Builder, TextComponent> {

    @Nullable private String content;

    /**
     * Sets the plain text content.
     *
     * @param content the plain text content
     * @return this builder
     */
    @Nonnull
    public Builder content(@Nonnull final String content) {
      this.content = content;
      return this;
    }

    @Override
    public TextComponent build() {
      checkState(this.content != null, "content must be set");
      return new TextComponent(this);
    }
  }
}
