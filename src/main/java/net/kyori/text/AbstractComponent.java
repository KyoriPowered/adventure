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

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An abstract implementation of a text component.
 */
public abstract class AbstractComponent implements Component {

  /**
   * The list of children.
   */
  @Nonnull protected final List<Component> children;
  /**
   * The color of this component.
   */
  @Nullable protected final TextColor color;
  /**
   * If this component should have the {@link TextDecoration#OBFUSCATED obfuscated} decoration.
   */
  @Nonnull protected final TextDecoration.State obfuscated;
  /**
   * If this component should have the {@link TextDecoration#BOLD bold} decoration.
   */
  @Nonnull protected final TextDecoration.State bold;
  /**
   * If this component should have the {@link TextDecoration#STRIKETHROUGH strikethrough} decoration.
   */
  @Nonnull protected final TextDecoration.State strikethrough;
  /**
   * If this component should have the {@link TextDecoration#UNDERLINE underlined} decoration.
   */
  @Nonnull protected final TextDecoration.State underlined;
  /**
   * If this component should have the {@link TextDecoration#ITALIC italic} decoration.
   */
  @Nonnull protected final TextDecoration.State italic;
  /**
   * The click event to apply to this component.
   */
  @Nullable protected final ClickEvent clickEvent;
  /**
   * The hover event to apply to this component.
   */
  @Nullable protected final HoverEvent hoverEvent;
  /**
   * The string to insert when this component is shift-clicked in chat.
   */
  @Nullable protected final String insertion;

  protected AbstractComponent(@Nonnull final List<Component> children, @Nullable final TextColor color, @Nonnull final TextDecoration.State obfuscated, @Nonnull final TextDecoration.State bold, @Nonnull final TextDecoration.State strikethrough, @Nonnull final TextDecoration.State underlined, @Nonnull final TextDecoration.State italic, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent hoverEvent, @Nullable final String insertion) {
    this.children = ImmutableList.copyOf(children);
    this.color = color;
    this.obfuscated = obfuscated;
    this.bold = bold;
    this.strikethrough = strikethrough;
    this.underlined = underlined;
    this.italic = italic;
    this.clickEvent = clickEvent;
    this.hoverEvent = hoverEvent;
    this.insertion = insertion;
  }

  @Nonnull
  @Override
  public List<Component> children() {
    return this.children;
  }

  @Nullable
  @Override
  public TextColor color() {
    return this.color;
  }

  @Nonnull
  @Override
  public TextDecoration.State decoration(@Nonnull final TextDecoration decoration) {
    switch(decoration) {
      case BOLD: return this.bold;
      case ITALIC: return this.italic;
      case UNDERLINE: return this.underlined;
      case STRIKETHROUGH: return this.strikethrough;
      case OBFUSCATED: return this.obfuscated;
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  @Nullable
  @Override
  public ClickEvent clickEvent() {
    return this.clickEvent;
  }

  @Nullable
  @Override
  public HoverEvent hoverEvent() {
    return this.hoverEvent;
  }

  @Nullable
  @Override
  public String insertion() {
    return this.insertion;
  }

  @Override
  public boolean hasStyling() {
    // A component has styling when any of these fields are set.
    return this.color != null
      || this.obfuscated != TextDecoration.State.NOT_SET
      || this.bold != TextDecoration.State.NOT_SET
      || this.strikethrough != TextDecoration.State.NOT_SET
      || this.underlined != TextDecoration.State.NOT_SET
      || this.italic != TextDecoration.State.NOT_SET
      || this.clickEvent != null
      || this.hoverEvent != null
      || this.insertion != null;
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof AbstractComponent)) return false;
    return this.equals((AbstractComponent) other);
  }

  protected boolean equals(@Nonnull final AbstractComponent that) {
    return Objects.equals(this.children, that.children)
      && this.color == that.color
      && Objects.equals(this.obfuscated, that.obfuscated)
      && Objects.equals(this.bold, that.bold)
      && Objects.equals(this.strikethrough, that.strikethrough)
      && Objects.equals(this.underlined, that.underlined)
      && Objects.equals(this.italic, that.italic)
      && Objects.equals(this.clickEvent, that.clickEvent)
      && Objects.equals(this.hoverEvent, that.hoverEvent)
      && Objects.equals(this.insertion, that.insertion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
  }

  @Nonnull
  @Override
  public String toString() {
    final MoreObjects.ToStringHelper builder = MoreObjects.toStringHelper(this);
    this.populateToString(builder);
    builder
      .add("children", this.children)
      .add("color", this.color)
      .add("obfuscated", this.obfuscated)
      .add("bold", this.bold)
      .add("strikethrough", this.strikethrough)
      .add("underlined", this.underlined)
      .add("italic", this.italic)
      .add("clickEvent", this.clickEvent)
      .add("hoverEvent", this.hoverEvent)
      .add("insertion", this.insertion);
    return builder.toString();
  }

  protected void populateToString(@Nonnull final MoreObjects.ToStringHelper builder) {
  }
}
