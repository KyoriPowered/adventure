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
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class KeybindComponent extends AbstractBuildableComponent<KeybindComponent, KeybindComponent.Builder> {

  /**
   * The keybind.
   */
  @Nonnull private final String keybind;

  /**
   * Creates a keybind component builder.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a keybind component builder with a keybind.
   *
   * @param keybind the keybind
   * @return a builder
   */
  public static Builder builder(@Nonnull final String keybind) {
    return new Builder().keybind(keybind);
  }

  /**
   * Creates a keybind component with a keybind.
   *
   * @param keybind the keybind
   * @return the text component
   */
  public static KeybindComponent of(@Nonnull final String keybind) {
    return builder(keybind).build();
  }

  protected KeybindComponent(@Nonnull final Builder builder) {
    super(builder);
    this.keybind = builder.keybind;
  }

  protected KeybindComponent(@Nonnull final List<Component> children, @Nullable final TextColor color, @Nonnull final TextDecoration.State obfuscated, @Nonnull final TextDecoration.State bold, @Nonnull final TextDecoration.State strikethrough, @Nonnull final TextDecoration.State underlined, @Nonnull final TextDecoration.State italic, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent hoverEvent, @Nullable final String insertion, @Nonnull final String keybind) {
    super(children, color, obfuscated, bold, strikethrough, underlined, italic, clickEvent, hoverEvent, insertion);
    this.keybind = keybind;
  }

  /**
   * Gets the keybind.
   *
   * @return the keybind
   */
  @Nonnull
  public String keybind() {
    return this.keybind;
  }

  /**
   * Sets the keybind.
   *
   * @param keybind the keybind
   * @return a copy of this component
   */
  @Nonnull
  public KeybindComponent keybind(@Nonnull final String keybind) {
    return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, checkNotNull(keybind, "keybind"));
  }

  @Nonnull
  @Override
  public KeybindComponent append(@Nonnull final Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> children = new ArrayList<>(this.children.size() + 1);
    children.addAll(this.children);
    children.add(component);
    return new KeybindComponent(children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent color(@Nullable final TextColor color) {
    return new KeybindComponent(this.children, color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent decoration(@Nonnull final TextDecoration decoration, final boolean flag) {
    return (KeybindComponent) super.decoration(decoration, flag);
  }

  @Nonnull
  @Override
  public KeybindComponent decoration(@Nonnull final TextDecoration decoration, @Nonnull final TextDecoration.State state) {
    switch(decoration) {
      case BOLD: return new KeybindComponent(this.children, this.color, this.obfuscated, checkNotNull(state, "flag"), this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
      case ITALIC: return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, checkNotNull(state, "flag"), this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
      case UNDERLINE: return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, checkNotNull(state, "flag"), this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
      case STRIKETHROUGH: return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, checkNotNull(state, "flag"), this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
      case OBFUSCATED: return new KeybindComponent(this.children, this.color, checkNotNull(state, "flag"), this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  @Nonnull
  @Override
  public KeybindComponent clickEvent(@Nullable final ClickEvent event) {
    return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent hoverEvent(@Nullable final HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, event, this.insertion, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent insertion(@Nullable final String insertion) {
    return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent mergeStyle(@Nonnull final Component that) {
    return new KeybindComponent(this.children, that.color(), that.decoration(TextDecoration.OBFUSCATED), that.decoration(TextDecoration.BOLD), that.decoration(TextDecoration.STRIKETHROUGH), that.decoration(TextDecoration.UNDERLINE), that.decoration(TextDecoration.ITALIC), that.clickEvent(), that.hoverEvent(), that.insertion(), this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent mergeColor(@Nonnull final Component that) {
    return new KeybindComponent(this.children, that.color(), this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent mergeDecorations(@Nonnull final Component that) {
    final TextDecoration.State obfuscated = that.decoration(TextDecoration.OBFUSCATED) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.OBFUSCATED) : this.obfuscated;
    final TextDecoration.State bold = that.decoration(TextDecoration.BOLD) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.BOLD) : this.bold;
    final TextDecoration.State strikethrough = that.decoration(TextDecoration.STRIKETHROUGH) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.STRIKETHROUGH) : this.strikethrough;
    final TextDecoration.State underlined = that.decoration(TextDecoration.UNDERLINE) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.UNDERLINE) : this.underlined;
    final TextDecoration.State italic = that.decoration(TextDecoration.ITALIC) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.ITALIC) : this.italic;
    return new KeybindComponent(this.children, this.color, obfuscated, bold, strikethrough, underlined, italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent mergeEvents(@Nonnull final Component that) {
    return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, that.clickEvent(), that.hoverEvent(), this.insertion, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent resetStyle() {
    return new KeybindComponent(this.children, null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null, this.keybind);
  }

  @Nonnull
  @Override
  public KeybindComponent copy() {
    return new KeybindComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.keybind);
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof KeybindComponent)) return false;
    if(!super.equals(other)) return false;
    final KeybindComponent component = (KeybindComponent) other;
    return Objects.equals(this.keybind, component.keybind);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.keybind);
  }

  @Override
  protected void populateToString(@Nonnull final MoreObjects.ToStringHelper builder) {
    builder.add("keybind", this.keybind);
  }

  @Nonnull
  @Override
  public Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * A keybind component builder.
   */
  public static class Builder extends AbstractBuildableComponent.AbstractBuilder<KeybindComponent, Builder> {

    @Nullable private String keybind;

    Builder() {
    }

    Builder(@Nonnull final KeybindComponent component) {
      super(component);
      this.keybind = component.keybind();
    }

    /**
     * Sets the keybind.
     *
     * @param keybind the keybind
     * @return this builder
     */
    @Nonnull
    public Builder keybind(@Nonnull final String keybind) {
      this.keybind = keybind;
      return this;
    }

    @Nonnull
    @Override
    public KeybindComponent build() {
      checkState(this.keybind != null, "keybind must be set");
      return new KeybindComponent(this);
    }
  }
}
