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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * An abstract implementation of a text component.
 */
public abstract class AbstractComponent implements Component {
  /**
   * The list of children.
   */
  protected final List<Component> children;
  /**
   * The color of this component.
   */
  protected final @Nullable TextColor color;
  /**
   * If this component should have the {@link TextDecoration#OBFUSCATED obfuscated} decoration.
   */
  protected final TextDecoration.State obfuscated;
  /**
   * If this component should have the {@link TextDecoration#BOLD bold} decoration.
   */
  protected final TextDecoration.State bold;
  /**
   * If this component should have the {@link TextDecoration#STRIKETHROUGH strikethrough} decoration.
   */
  protected final TextDecoration.State strikethrough;
  /**
   * If this component should have the {@link TextDecoration#UNDERLINED underlined} decoration.
   */
  protected final TextDecoration.State underlined;
  /**
   * If this component should have the {@link TextDecoration#ITALIC italic} decoration.
   */
  protected final TextDecoration.State italic;
  /**
   * The click event to apply to this component.
   */
  protected final @Nullable ClickEvent clickEvent;
  /**
   * The hover event to apply to this component.
   */
  protected final @Nullable HoverEvent hoverEvent;
  /**
   * The string to insert when this component is shift-clicked in chat.
   */
  protected final @Nullable String insertion;

  protected AbstractComponent(final @NonNull List<Component> children, final @Nullable TextColor color, final TextDecoration.@NonNull State obfuscated, final TextDecoration.@NonNull State bold, final TextDecoration.@NonNull State strikethrough, final TextDecoration.@NonNull State underlined, final TextDecoration.@NonNull State italic, final @Nullable ClickEvent clickEvent, final @Nullable HoverEvent hoverEvent, final @Nullable String insertion) {
    this.children = Collections.unmodifiableList(new ArrayList<>(children));
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

  @Override
  public @NonNull List<Component> children() {
    return this.children;
  }

  @Override
  public @Nullable TextColor color() {
    return this.color;
  }

  @Override
  public TextDecoration.@NonNull State decoration(final @NonNull TextDecoration decoration) {
    switch(decoration) {
      case BOLD: return this.bold;
      case ITALIC: return this.italic;
      case UNDERLINED: return this.underlined;
      case STRIKETHROUGH: return this.strikethrough;
      case OBFUSCATED: return this.obfuscated;
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  @Override
  public @Nullable ClickEvent clickEvent() {
    return this.clickEvent;
  }

  @Override
  public @Nullable HoverEvent hoverEvent() {
    return this.hoverEvent;
  }

  @Override
  public @Nullable String insertion() {
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
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof AbstractComponent)) return false;
    return this.equals((AbstractComponent) other);
  }

  protected boolean equals(final @NonNull AbstractComponent that) {
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

  @Override
  public @NonNull String toString() {
    final Map<String, Object> builder = new LinkedHashMap<>();
    this.populateToString(builder);
    builder.put("children", this.children);
    builder.put("color", this.color);
    builder.put("obfuscated", this.obfuscated);
    builder.put("bold", this.bold);
    builder.put("strikethrough", this.strikethrough);
    builder.put("underlined", this.underlined);
    builder.put("italic", this.italic);
    builder.put("clickEvent", this.clickEvent);
    builder.put("hoverEvent", this.hoverEvent);
    builder.put("insertion", this.insertion);
    final StringJoiner joiner = new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}");
    builder.forEach((key, value) -> joiner.add(key + "=" + value));
    return joiner.toString();
  }

  protected void populateToString(final @NonNull Map<String, Object> builder) {
  }
}
