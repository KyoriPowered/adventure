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
package net.kyori.text.format;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.kyori.text.Component;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.util.ToStringer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

public final class Style {
  private static final Style EMPTY = new Style(null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null);
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  private final @Nullable TextColor color;
  private final TextDecoration.State obfuscated;
  private final TextDecoration.State bold;
  private final TextDecoration.State strikethrough;
  private final TextDecoration.State underlined;
  private final TextDecoration.State italic;
  private final @Nullable ClickEvent clickEvent;
  private final @Nullable HoverEvent hoverEvent;
  private final @Nullable String insertion;

  /**
   * Creates a builder.
   *
   * @return a builder
   */
  public static @NonNull Builder builder() {
    return new Builder();
  }

  /**
   * Gets an empty style.
   *
   * @return empty style
   */
  public static @NonNull Style empty() {
    return EMPTY;
  }

  /**
   * Creates a style with color.
   *
   * @param color the style
   * @return a style
   */
  public static @NonNull Style of(final @Nullable TextColor color) {
    return builder().color(color).build();
  }

  /**
   * Creates a style with decorations.
   *
   * @param decorations the decorations
   * @return a style
   */
  public static @NonNull Style of(final TextDecoration@NonNull... decorations) {
    final Builder builder = builder();
    for(final TextDecoration decoration : decorations) {
      builder.decoration(decoration, true);
    }
    return builder.build();
  }

  /**
   * Creates a style with color and decorations.
   *
   * @param color the style
   * @param decorations the decorations
   * @return a style
   */
  public static @NonNull Style of(final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    final Builder builder = builder();
    builder.color(color);
    for(final TextDecoration decoration : decorations) {
      builder.decoration(decoration, true);
    }
    return builder.build();
  }

  private Style(final @Nullable TextColor color, final TextDecoration.State obfuscated, final TextDecoration.State bold, final TextDecoration.State strikethrough, final TextDecoration.State underlined, final TextDecoration.State italic, final @Nullable ClickEvent clickEvent, final @Nullable HoverEvent hoverEvent, final @Nullable String insertion) {
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

  /**
   * Gets the color.
   *
   * @return the color
   */
  public @Nullable TextColor color() {
    return this.color;
  }

  /**
   * Sets the color if there isn't one set already.
   *
   * @param color the color
   * @return this builder
   */
  public @NonNull Style colorIfAbsent(final @Nullable TextColor color) {
    if(this.color == null) {
      return this.color(color);
    }
    return this;
  }

  /**
   * Sets the color.
   *
   * @param color the color
   * @return a style
   */
  public @NonNull Style color(final @Nullable TextColor color) {
    return new Style(color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
  }

  /**
   * Tests if this style has a decoration.
   *
   * @param decoration the decoration
   * @return {@code true} if this style has the decoration, {@code false} if this
   *     style does not have the decoration
   */
  public boolean hasDecoration(final @NonNull TextDecoration decoration) {
    return this.decoration(decoration) == TextDecoration.State.TRUE;
  }

  /**
   * Gets the state of a decoration on this style.
   *
   * @param decoration the decoration
   * @return {@link TextDecoration.State#TRUE} if this style has the decoration,
   *     {@link TextDecoration.State#FALSE} if this style does not have the decoration,
   *     and {@link TextDecoration.State#NOT_SET} if not set
   */
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

  /**
   * Sets the state of a decoration on this style.
   *
   * @param decoration the decoration
   * @param flag {@code true} if this style should have the decoration, {@code false} if
   *     this style should not have the decoration
   * @return a style
   */
  public @NonNull Style decoration(final @NonNull TextDecoration decoration, final boolean flag) {
    return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
  }

  /**
   * Sets the value of a decoration on this style.
   *
   * @param decoration the decoration
   * @param state {@link TextDecoration.State#TRUE} if this style should have the
   *     decoration, {@link TextDecoration.State#FALSE} if this style should not
   *     have the decoration, and {@link TextDecoration.State#NOT_SET} if the decoration
   *     should not have a set value
   * @return a style
   */
  public @NonNull Style decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    switch(decoration) {
      case BOLD: return new Style(this.color, this.obfuscated, requireNonNull(state, "flag"), this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
      case ITALIC: return new Style(this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, requireNonNull(state, "flag"), this.clickEvent, this.hoverEvent, this.insertion);
      case UNDERLINED: return new Style(this.color, this.obfuscated, this.bold, this.strikethrough, requireNonNull(state, "flag"), this.italic, this.clickEvent, this.hoverEvent, this.insertion);
      case STRIKETHROUGH: return new Style(this.color, this.obfuscated, this.bold, requireNonNull(state, "flag"), this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
      case OBFUSCATED: return new Style(this.color, requireNonNull(state, "flag"), this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  /**
   * Gets a set of decorations this style has.
   *
   * @return a set of decorations this style has
   */
  public @NonNull Set<TextDecoration> decorations() {
    return this.decorations(Collections.emptySet());
  }

  /**
   * Gets a set of decorations this style has.
   *
   * @param defaultValues a set of default values
   * @return a set of decorations this style has
   */
  public @NonNull Set<TextDecoration> decorations(final @NonNull Set<TextDecoration> defaultValues) {
    final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);
    for(final TextDecoration decoration : DECORATIONS) {
      final TextDecoration.State value = this.decoration(decoration);
      if(value == TextDecoration.State.TRUE || (value == TextDecoration.State.NOT_SET && defaultValues.contains(decoration))) {
        decorations.add(decoration);
      }
    }
    return decorations;
  }

  /**
   * Gets the click event.
   *
   * @return the click event
   */
  public @Nullable ClickEvent clickEvent() {
    return this.clickEvent;
  }

  /**
   * Sets the click event.
   *
   * @param event the click event
   * @return a style
   */
  public @NonNull Style clickEvent(final @Nullable ClickEvent event) {
    return new Style(this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion);
  }

  /**
   * Gets the hover event.
   *
   * @return the hover event
   */
  public @Nullable HoverEvent hoverEvent() {
    return this.hoverEvent;
  }

  /**
   * Sets the hover event.
   *
   * @param event the hover event
   * @return a style
   */
  public @NonNull Style hoverEvent(final @Nullable HoverEvent event) {
    return new Style(this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, event, this.insertion);
  }

  /**
   * Gets the string to be inserted when this style is shift-clicked.
   *
   * @return the insertion string
   */
  public @Nullable String insertion() {
    return this.insertion;
  }

  /**
   * Sets the string to be inserted when this style is shift-clicked.
   *
   * @param insertion the insertion string
   * @return a style
   */
  public @NonNull Style insertion(final @Nullable String insertion) {
    return new Style(this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion);
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that) {
    return this.merge(that, Merge.all());
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param merges the parts to merge
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that, final @NonNull Merge@NonNull... merges) {
    return this.merge(that, Merge.of(merges));
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param merges the parts to merge
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that, final @NonNull Set<Merge> merges) {
    if(merges.isEmpty() || that.isEmpty()) {
      return this;
    }

    final Builder builder = this.toBuilder();
    builder.merge(that, merges);
    return builder.build();
  }

  /**
   * Merges the color from another style into this style.
   *
   * @param that the other style
   * @return a style
   * @deprecated use {@link #merge(Style, Set)} instead
   */
  @Deprecated
  public @NonNull Style mergeColor(final @NonNull Style that) {
    return this.merge(that, Collections.singleton(Merge.COLOR));
  }

  /**
   * Merges the decorations from another style into this style.
   *
   * @param that the other style
   * @return a style
   * @deprecated use {@link #merge(Style, Set)} instead
   */
  @Deprecated
  public @NonNull Style mergeDecorations(final @NonNull Style that) {
    return this.merge(that, Collections.singleton(Merge.DECORATIONS));
  }

  /**
   * Merges the events from another style into this style.
   *
   * @param that the other style
   * @return a style
   * @deprecated use {@link #merge(Style, Set)} instead
   */
  @Deprecated
  public @NonNull Style mergeEvents(final @NonNull Style that) {
    return this.merge(that, Collections.singleton(Merge.EVENTS));
  }

  /**
   * Tests if this style is empty.
   *
   * @return {@code true} if this style is empty, {@code false} if this
   *     style is not empty
   */
  public boolean isEmpty() {
    return this.color == null
      && this.obfuscated == TextDecoration.State.NOT_SET
      && this.bold == TextDecoration.State.NOT_SET
      && this.strikethrough == TextDecoration.State.NOT_SET
      && this.underlined == TextDecoration.State.NOT_SET
      && this.italic == TextDecoration.State.NOT_SET
      && this.clickEvent == null
      && this.hoverEvent == null
      && this.insertion == null;
  }

  /**
   * Create a builder from this style.
   *
   * @return a builder
   */
  public @NonNull Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public @NonNull String toString() {
    final Map<String, Object> builder = new LinkedHashMap<>();
    builder.put("color", this.color);
    builder.put("obfuscated", this.obfuscated);
    builder.put("bold", this.bold);
    builder.put("strikethrough", this.strikethrough);
    builder.put("underlined", this.underlined);
    builder.put("italic", this.italic);
    builder.put("clickEvent", this.clickEvent);
    builder.put("hoverEvent", this.hoverEvent);
    builder.put("insertion", this.insertion);
    return ToStringer.toString(this, builder);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof Style)) return false;
    final Style that = (Style) other;
    return this.color == that.color
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
    return Objects.hash(this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
  }

  /**
   * A merge choice.
   */
  public enum Merge {
    COLOR,
    DECORATIONS,
    EVENTS,
    INSERTION;

    static final Set<Merge> ALL = of(Merge.values());

    /**
     * Gets a merge set of all merge types.
     *
     * @return a merge set
     */
    public static @NonNull Set<Merge> all() {
      return ALL;
    }

    /**
     * Creates a merge set.
     *
     * @param merges the merge parts
     * @return a merge set
     */
    public static @NonNull Set<Merge> of(final Merge... merges) {
      final Set<Merge> set = EnumSet.noneOf(Merge.class);
      Collections.addAll(set, merges);
      return Collections.unmodifiableSet(set);
    }
  }

  /**
   * A style builder.
   */
  public static class Builder {
    /**
     * The color.
     */
    private @Nullable TextColor color;
    /**
     * If this component should have the {@link TextDecoration#OBFUSCATED obfuscated} decoration.
     */
    private TextDecoration.State obfuscated = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#BOLD bold} decoration.
     */
    private TextDecoration.State bold = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#STRIKETHROUGH strikethrough} decoration.
     */
    private TextDecoration.State strikethrough = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#UNDERLINED underlined} decoration.
     */
    private TextDecoration.State underlined = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#ITALIC italic} decoration.
     */
    private TextDecoration.State italic = TextDecoration.State.NOT_SET;
    /**
     * The click event to apply to this component.
     */
    private @Nullable ClickEvent clickEvent;
    /**
     * The hover event to apply to this component.
     */
    private @Nullable HoverEvent hoverEvent;
    /**
     * The string to insert when this component is shift-clicked in chat.
     */
    private @Nullable String insertion;

    protected Builder() {
    }

    protected Builder(final @NonNull Component component) {
      this(component.style());
    }

    protected Builder(final @NonNull Style style) {
      this.color = style.color();
      this.obfuscated = style.decoration(TextDecoration.OBFUSCATED);
      this.bold = style.decoration(TextDecoration.BOLD);
      this.strikethrough = style.decoration(TextDecoration.STRIKETHROUGH);
      this.underlined = style.decoration(TextDecoration.UNDERLINED);
      this.italic = style.decoration(TextDecoration.ITALIC);
      this.clickEvent = style.clickEvent();
      this.hoverEvent = style.hoverEvent();
      this.insertion = style.insertion();
    }

    /**
     * Sets the color.
     *
     * @param color the color
     * @return this builder
     */
    public @NonNull Builder color(final @Nullable TextColor color) {
      this.color = color;
      return this;
    }

    /**
     * Sets the color if there isn't one set already.
     *
     * @param color the color
     * @return this builder
     */
    public @NonNull Builder colorIfAbsent(final @Nullable TextColor color) {
      if(this.color == null) {
        this.color = color;
      }
      return this;
    }

    /**
     * Sets the state of a decoration on this style.
     *
     * @param decoration the decoration
     * @param flag {@code true} if this style should have the decoration, {@code false} if
     *     this style should not have the decoration
     * @return a style
     */
    public @NonNull Builder decoration(final @NonNull TextDecoration decoration, final boolean flag) {
      return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
    }

    /**
     * Sets the value of a decoration.
     *
     * @param decoration the decoration
     * @param state {@link TextDecoration.State#TRUE} if this component should have the
     *     decoration, {@link TextDecoration.State#FALSE} if this component should not
     *     have the decoration, and {@link TextDecoration.State#NOT_SET} if the decoration
     *     should not have a set value
     * @return this builder
     */
    public @NonNull Builder decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
      switch(decoration) {
        case BOLD: this.bold = requireNonNull(state, "flag"); return this;
        case ITALIC: this.italic = requireNonNull(state, "flag"); return this;
        case UNDERLINED: this.underlined = requireNonNull(state, "flag"); return this;
        case STRIKETHROUGH: this.strikethrough = requireNonNull(state, "flag"); return this;
        case OBFUSCATED: this.obfuscated = requireNonNull(state, "flag"); return this;
        default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
      }
    }

    /**
     * Sets the click event.
     *
     * @param event the click event
     * @return this builder
     */
    public @NonNull Builder clickEvent(final @Nullable ClickEvent event) {
      this.clickEvent = event;
      return this;
    }

    /**
     * Sets the hover event.
     *
     * @param event the hover event
     * @return this builder
     */
    public @NonNull Builder hoverEvent(final @Nullable HoverEvent event) {
      this.hoverEvent = event;
      return this;
    }

    /**
     * Sets the string to be inserted.
     *
     * @param insertion the insertion string
     * @return this builder
     */
    public @NonNull Builder insertion(final @Nullable String insertion) {
      this.insertion = insertion;
      return this;
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @return a style
     */
    public @NonNull Builder merge(final @NonNull Style that) {
      return this.merge(that, Merge.all());
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param merges the parts to merge
     * @return a style
     */
    public @NonNull Builder merge(final @NonNull Style that, final @NonNull Merge @NonNull ... merges) {
      return this.merge(that, Merge.of(merges));
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param merges the parts to merge
     * @return a style
     */
    public @NonNull Builder merge(final @NonNull Style that, final @NonNull Set<Merge> merges) {
      if(merges.contains(Merge.COLOR)) {
        final TextColor color = that.color();
        if(color != null) this.color(color);
      }

      if(merges.contains(Merge.DECORATIONS)) {
        for(final TextDecoration decoration : DECORATIONS) {
          final TextDecoration.State state = that.decoration(decoration);
          if(state != TextDecoration.State.NOT_SET) this.decoration(decoration, state);
        }
      }

      if(merges.contains(Merge.EVENTS)) {
        final ClickEvent clickEvent = that.clickEvent();
        if(clickEvent != null) this.clickEvent(clickEvent);

        final HoverEvent hoverEvent = that.hoverEvent();
        if(hoverEvent != null) this.hoverEvent(hoverEvent);
      }

      if(merges.contains(Merge.INSERTION)) {
        this.insertion(that.insertion());
      }

      return this;
    }

    /**
     * Builds the style.
     *
     * @return the style
     */
    public @NonNull Style build() {
      return new Style(this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    }
  }
}
