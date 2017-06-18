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
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract implementation of a text component.
 */
public abstract class BaseComponent implements Component {

  /**
   * The list of children.
   *
   * <p>This list is set to {@link #EMPTY_COMPONENT_LIST an empty list of components}
   * by default to prevent unnecessary list creation for components with no children.</p>
   */
  @Nonnull private List<Component> children = EMPTY_COMPONENT_LIST;
  /**
   * The color of this component.
   */
  @Nullable private TextColor color;
  /**
   * If this component should have the {@link TextDecoration#OBFUSCATED obfuscated} decoration.
   */
  @Nonnull private TextDecoration.State obfuscated = TextDecoration.State.NOT_SET;
  /**
   * If this component should have the {@link TextDecoration#BOLD bold} decoration.
   */
  @Nonnull private TextDecoration.State bold = TextDecoration.State.NOT_SET;
  /**
   * If this component should have the {@link TextDecoration#STRIKETHROUGH strikethrough} decoration.
   */
  @Nonnull private TextDecoration.State strikethrough = TextDecoration.State.NOT_SET;
  /**
   * If this component should have the {@link TextDecoration#UNDERLINE underlined} decoration.
   */
  @Nonnull private TextDecoration.State underlined = TextDecoration.State.NOT_SET;
  /**
   * If this component should have the {@link TextDecoration#ITALIC italic} decoration.
   */
  @Nonnull private TextDecoration.State italic = TextDecoration.State.NOT_SET;
  /**
   * The click event to apply to this component.
   */
  @Nullable private ClickEvent clickEvent;
  /**
   * The hover event to apply to this component.
   */
  @Nullable private HoverEvent hoverEvent;
  /**
   * The string to insert when this component is shift-clicked in chat.
   */
  @Nullable private String insertion;

  @Nonnull
  @Override
  public List<Component> children() {
    return Collections.unmodifiableList(this.children);
  }

  @Nonnull
  @Override
  public Component append(@Nonnull final Component component) {
    this.detectCycle(component); // detect cycle before modifying
    if(this.children == EMPTY_COMPONENT_LIST) this.children = new ArrayList<>();
    this.children.add(component);
    return this;
  }

  @Nullable
  @Override
  public TextColor color() {
    return this.color;
  }

  @Nonnull
  @Override
  public Component color(@Nullable final TextColor color) {
    this.color = color;
    return this;
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

  @Nonnull
  @Override
  public Component decoration(@Nonnull final TextDecoration decoration, @Nonnull final TextDecoration.State state) {
    switch(decoration) {
      case BOLD: this.bold = checkNotNull(state, "flag"); return this;
      case ITALIC: this.italic = checkNotNull(state, "flag"); return this;
      case UNDERLINE: this.underlined = checkNotNull(state, "flag"); return this;
      case STRIKETHROUGH: this.strikethrough = checkNotNull(state, "flag"); return this;
      case OBFUSCATED: this.obfuscated = checkNotNull(state, "flag"); return this;
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  @Nullable
  @Override
  public ClickEvent clickEvent() {
    return this.clickEvent;
  }

  @Nonnull
  @Override
  public Component clickEvent(@Nullable final ClickEvent event) {
    this.clickEvent = event;
    return this;
  }

  @Nullable
  @Override
  public HoverEvent hoverEvent() {
    return this.hoverEvent;
  }

  @Nonnull
  @Override
  public Component hoverEvent(@Nullable final HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    this.hoverEvent = event;
    return this;
  }

  @Nullable
  @Override
  public String insertion() {
    return this.insertion;
  }

  @Nonnull
  @Override
  public Component insertion(@Nullable final String insertion) {
    this.insertion = insertion;
    return this;
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
    if(other == null || !(other instanceof BaseComponent)) return false;
    return this.equals((BaseComponent) other);
  }

  protected boolean equals(@Nonnull final BaseComponent that) {
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
