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
 * A scoreboard selector component.
 */
public class SelectorComponent extends AbstractComponent {

  /**
   * The selector pattern.
   */
  @Nonnull private final String pattern;

  /**
   * Creates a selector component builder.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a selector component with a pattern.
   *
   * @param pattern the selector pattern
   * @return the text component
   */
  public static SelectorComponent of(@Nonnull final String pattern) {
    return builder().pattern(pattern).build();
  }

  protected SelectorComponent(@Nonnull final Builder builder) {
    super(builder);
    this.pattern = builder.pattern;
  }

  protected SelectorComponent(@Nonnull final List<Component> children, @Nullable final TextColor color, @Nonnull final TextDecoration.State obfuscated, @Nonnull final TextDecoration.State bold, @Nonnull final TextDecoration.State strikethrough, @Nonnull final TextDecoration.State underlined, @Nonnull final TextDecoration.State italic, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent hoverEvent, @Nullable final String insertion, @Nonnull final String pattern) {
    super(children, color, obfuscated, bold, strikethrough, underlined, italic, clickEvent, hoverEvent, insertion);
    this.pattern = pattern;
  }

  /**
   * Gets the selector pattern.
   *
   * @return the selector pattern
   */
  @Nonnull
  public String pattern() {
    return this.pattern;
  }

  /**
   * Sets the selector pattern.
   *
   * @param pattern the selector pattern
   * @return a copy of this component
   */
  @Nonnull
  public SelectorComponent pattern(@Nonnull final String pattern) {
    return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, checkNotNull(pattern, "pattern"));
  }

  @Nonnull
  @Override
  public SelectorComponent append(@Nonnull Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> children = new ArrayList<>(this.children.size() + 1);
    children.addAll(this.children);
    children.add(component);
    return new SelectorComponent(children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent color(@Nullable TextColor color) {
    return new SelectorComponent(this.children, color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent decoration(@Nonnull final TextDecoration decoration, final boolean flag) {
    return (SelectorComponent) super.decoration(decoration, flag);
  }

  @Nonnull
  @Override
  public SelectorComponent decoration(@Nonnull TextDecoration decoration, @Nonnull TextDecoration.State state) {
    switch(decoration) {
      case BOLD: return new SelectorComponent(this.children, this.color, this.obfuscated, checkNotNull(state, "flag"), this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
      case ITALIC: return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, checkNotNull(state, "flag"), this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
      case UNDERLINE: return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, checkNotNull(state, "flag"), this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
      case STRIKETHROUGH: return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, checkNotNull(state, "flag"), this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
      case OBFUSCATED: return new SelectorComponent(this.children, this.color, checkNotNull(state, "flag"), this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  @Nonnull
  @Override
  public SelectorComponent clickEvent(@Nullable ClickEvent event) {
    return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent hoverEvent(@Nullable HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, event, this.insertion, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent insertion(@Nullable String insertion) {
    return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent mergeStyle(@Nonnull final Component that) {
    return new SelectorComponent(this.children, that.color(), that.decoration(TextDecoration.OBFUSCATED), that.decoration(TextDecoration.BOLD), that.decoration(TextDecoration.STRIKETHROUGH), that.decoration(TextDecoration.UNDERLINE), that.decoration(TextDecoration.ITALIC), that.clickEvent(), that.hoverEvent(), that.insertion(), this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent mergeColor(@Nonnull Component that) {
    return new SelectorComponent(this.children, that.color(), this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent mergeDecorations(@Nonnull Component that) {
    final TextDecoration.State obfuscated = that.decoration(TextDecoration.OBFUSCATED) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.OBFUSCATED) : this.obfuscated;
    final TextDecoration.State bold = that.decoration(TextDecoration.BOLD) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.BOLD) : this.bold;
    final TextDecoration.State strikethrough = that.decoration(TextDecoration.STRIKETHROUGH) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.STRIKETHROUGH) : this.strikethrough;
    final TextDecoration.State underlined = that.decoration(TextDecoration.UNDERLINE) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.UNDERLINE) : this.underlined;
    final TextDecoration.State italic = that.decoration(TextDecoration.ITALIC) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.ITALIC) : this.italic;
    return new SelectorComponent(this.children, this.color, obfuscated, bold, strikethrough, underlined, italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent mergeEvents(@Nonnull Component that) {
    return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, that.clickEvent(), that.hoverEvent(), this.insertion, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent resetStyle() {
    return new SelectorComponent(this.children, null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null, this.pattern);
  }

  @Nonnull
  @Override
  public SelectorComponent copy() {
    return new SelectorComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.pattern);
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof SelectorComponent)) return false;
    if(!super.equals(other)) return false;
    final SelectorComponent that = (SelectorComponent) other;
    return Objects.equal(this.pattern, that.pattern);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), this.pattern);
  }

  @Override
  protected void populateToString(@Nonnull final Objects.ToStringHelper builder) {
    builder.add("pattern", this.pattern);
  }

  /**
   * A selector component builder.
   */
  public static class Builder extends AbstractComponent.AbstractBuilder<Builder, SelectorComponent> {

    @Nullable private String pattern;

    /**
     * Sets the selector pattern.
     *
     * @param pattern the selector pattern
     * @return this builder
     */
    @Nonnull
    public Builder pattern(@Nonnull final String pattern) {
      this.pattern = pattern;
      return this;
    }

    @Override
    public SelectorComponent build() {
      checkState(this.pattern != null, "pattern must be set");
      return new SelectorComponent(this);
    }
  }
}
