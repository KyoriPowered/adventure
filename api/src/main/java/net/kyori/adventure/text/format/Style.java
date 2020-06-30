/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.format;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.util.Buildable;
import net.kyori.adventure.util.ShadyPines;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

public final class Style implements Buildable<Style, Style.Builder>, Examinable {
  public static final Key DEFAULT_FONT = Key.of("default");
  private static final Style EMPTY = new Style(null, null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null);
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  private final @Nullable Key font;
  private final @Nullable TextColor color;
  private final TextDecoration.State obfuscated;
  private final TextDecoration.State bold;
  private final TextDecoration.State strikethrough;
  private final TextDecoration.State underlined;
  private final TextDecoration.State italic;
  private final @Nullable ClickEvent clickEvent;
  private final @Nullable HoverEvent<?> hoverEvent;
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
    if(color == null) return empty();
    return new Style(null, color, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null);
  }

  /**
   * Creates a style with decoration.
   *
   * @param decoration the decoration
   * @return a style
   */
  public static @NonNull Style of(final @NonNull TextDecoration decoration) {
    return builder().decoration(decoration, true).build();
  }

  /**
   * Creates a style with decorations.
   *
   * @param decorations the decorations
   * @return a style
   */
  public static @NonNull Style of(final TextDecoration@NonNull... decorations) {
    if(decorations.length == 0) return empty();
    final Builder builder = builder();
    decorate(builder, decorations);
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
    decorate(builder, decorations);
    return builder.build();
  }

  private static void decorate(final Builder builder, final TextDecoration[] decorations) {
    for(int i = 0, length = decorations.length; i < length; i++) {
      final TextDecoration decoration = decorations[i];
      builder.decoration(decoration, true);
    }
  }

  /**
   * Creates a style with color and decorations.
   *
   * @param color the style
   * @param decorations the decorations
   * @return a style
   */
  public static @NonNull Style of(final @Nullable TextColor color, final Set<TextDecoration> decorations) {
    final Builder builder = builder();
    builder.color(color);
    if(!decorations.isEmpty()) {
      for(final TextDecoration decoration : decorations) {
        builder.decoration(decoration, true);
      }
    }
    return builder.build();
  }

  /**
   * Creates a style.
   *
   * @param consumer the builder consumer
   * @return a style
   */
  public static @NonNull Style make(final @NonNull Consumer<Builder> consumer) {
    return Buildable.configureAndBuild(builder(), consumer);
  }

  private Style(final @Nullable Key font, final @Nullable TextColor color, final TextDecoration.State obfuscated, final TextDecoration.State bold, final TextDecoration.State strikethrough, final TextDecoration.State underlined, final TextDecoration.State italic, final @Nullable ClickEvent clickEvent, final @Nullable HoverEvent<?> hoverEvent, final @Nullable String insertion) {
    this.font = font;
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
   * Edits this style.
   *
   * <p>The old style will be merge into the new style before {@code consumer} is called.</p>
   *
   * @param consumer the consumer
   * @return a new style
   */
  public @NonNull Style edit(final @NonNull Consumer<Builder> consumer) {
    return this.edit(consumer, Merge.Strategy.ALWAYS);
  }

  /**
   * Edits this style.
   *
   * @param consumer the consumer
   * @param strategy the merge strategy
   * @return a new style
   */
  public @NonNull Style edit(final @NonNull Consumer<Builder> consumer, final Style.Merge.@NonNull Strategy strategy) {
    return make(style -> {
      if(strategy == Style.Merge.Strategy.ALWAYS) {
        style.merge(this, strategy);
      }
      consumer.accept(style);
      if(strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET) {
        style.merge(this, strategy);
      }
    });
  }

  /**
   * Gets the font.
   *
   * @return the font
   */
  public @Nullable Key font() {
    return this.font;
  }

  /**
   * Sets the font.
   *
   * @param font the font
   * @return a style
   */
  public @NonNull Style font(final @Nullable Key font) {
    if(Objects.equals(this.font, font)) return this;
    return new Style(font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
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
    if(Objects.equals(this.color, color)) return this;
    return new Style(this.font, color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
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
    if(decoration == TextDecoration.BOLD) {
      return this.bold;
    } else if(decoration == TextDecoration.ITALIC) {
      return this.italic;
    } else if(decoration == TextDecoration.UNDERLINED) {
      return this.underlined;
    } else if(decoration == TextDecoration.STRIKETHROUGH) {
      return this.strikethrough;
    } else if(decoration == TextDecoration.OBFUSCATED) {
      return this.obfuscated;
    }
    throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
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
    requireNonNull(state, "state");
    if(decoration == TextDecoration.BOLD) {
      return new Style(this.font, this.color, this.obfuscated, state, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    } else if(decoration == TextDecoration.ITALIC) {
      return new Style(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, state, this.clickEvent, this.hoverEvent, this.insertion);
    } else if(decoration == TextDecoration.UNDERLINED) {
      return new Style(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, state, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    } else if(decoration == TextDecoration.STRIKETHROUGH) {
      return new Style(this.font, this.color, this.obfuscated, this.bold, state, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    } else if(decoration == TextDecoration.OBFUSCATED) {
      return new Style(this.font, this.color, state, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    }
    throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
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
    for(int i = 0, length = DECORATIONS.length; i < length; i++) {
      final TextDecoration decoration = DECORATIONS[i];
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
    return new Style(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion);
  }

  /**
   * Gets the hover event.
   *
   * @return the hover event
   */
  public @Nullable HoverEvent<?> hoverEvent() {
    return this.hoverEvent;
  }

  /**
   * Sets the hover event.
   *
   * @param event the hover event
   * @return a style
   */
  public @NonNull Style hoverEvent(final @Nullable HoverEvent<?> event) {
    return new Style(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, event, this.insertion);
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
    if(Objects.equals(this.insertion, insertion)) return this;
    return new Style(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion);
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
   * @param strategy the merge strategy
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy) {
    return this.merge(that, strategy, Merge.all());
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param merge the part to merge
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that, final @NonNull Merge merge) {
    return this.merge(that, Collections.singleton(merge));
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param strategy the merge strategy
   * @param merge the part to merge
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Merge merge) {
    return this.merge(that, strategy, Collections.singleton(merge));
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
   * @param strategy the merge strategy
   * @param merges the parts to merge
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Merge@NonNull... merges) {
    return this.merge(that, strategy, Merge.of(merges));
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param merges the parts to merge
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that, final @NonNull Set<Merge> merges) {
    return this.merge(that, Merge.Strategy.ALWAYS, merges);
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param strategy the merge strategy
   * @param merges the parts to merge
   * @return a style
   */
  public @NonNull Style merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Set<Merge> merges) {
    if(that.isEmpty() || strategy == Merge.Strategy.NEVER || merges.isEmpty()) {
      // nothing to merge
      return this;
    }

    if(this.isEmpty() && Merge.hasAll(merges)) {
      // if the current style is empty and all merge types have been requested
      // we can just return the other style instead of trying to merge
      return that;
    }

    final Builder builder = this.toBuilder();
    builder.merge(that, strategy, merges);
    return builder.build();
  }

  /**
   * Tests if this style is empty.
   *
   * @return {@code true} if this style is empty, {@code false} if this
   *     style is not empty
   */
  public boolean isEmpty() {
    return this == EMPTY;
  }

  /**
   * Create a builder from this style.
   *
   * @return a builder
   */
  @Override
  public @NonNull Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("color", this.color),
      ExaminableProperty.of("obfuscated", this.obfuscated),
      ExaminableProperty.of("bold", this.bold),
      ExaminableProperty.of("strikethrough", this.strikethrough),
      ExaminableProperty.of("underlined", this.underlined),
      ExaminableProperty.of("italic", this.italic),
      ExaminableProperty.of("clickEvent", this.clickEvent),
      ExaminableProperty.of("hoverEvent", this.hoverEvent),
      ExaminableProperty.of("insertion", this.insertion),
      ExaminableProperty.of("font", this.font)
    );
  }

  @Override
  public @NonNull String toString() {
    return StringExaminer.simpleEscaping().examine(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof Style)) return false;
    final Style that = (Style) other;
    return Objects.equals(this.color, that.color)
      && this.obfuscated == that.obfuscated
      && this.bold == that.bold
      && this.strikethrough == that.strikethrough
      && this.underlined == that.underlined
      && this.italic == that.italic
      && Objects.equals(this.clickEvent, that.clickEvent)
      && Objects.equals(this.hoverEvent, that.hoverEvent)
      && Objects.equals(this.insertion, that.insertion)
      && Objects.equals(this.font, that.font);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(this.color);
    result = (31 * result) + this.obfuscated.hashCode();
    result = (31 * result) + this.bold.hashCode();
    result = (31 * result) + this.strikethrough.hashCode();
    result = (31 * result) + this.underlined.hashCode();
    result = (31 * result) + this.italic.hashCode();
    result = (31 * result) + Objects.hashCode(this.clickEvent);
    result = (31 * result) + Objects.hashCode(this.hoverEvent);
    result = (31 * result) + Objects.hashCode(this.insertion);
    result = (31 * result) + Objects.hashCode(this.font);
    return result;
  }

  /**
   * A merge choice.
   */
  public enum Merge {
    COLOR,
    DECORATIONS,
    EVENTS,
    INSERTION,
    FONT;

    static final Set<Merge> ALL = of(values());
    static final Set<Merge> COLOR_AND_DECORATIONS = of(COLOR, DECORATIONS);

    /**
     * Gets a merge set of all merge types.
     *
     * @return a merge set
     */
    public static @NonNull Set<Merge> all() {
      return ALL;
    }

    /**
     * Gets a merge set containing {@link #COLOR} and {@link #DECORATIONS}.
     *
     * @return a merge set
     */
    public static @NonNull Set<Merge> colorAndDecorations() {
      return COLOR_AND_DECORATIONS;
    }

    /**
     * Creates a merge set.
     *
     * @param merges the merge parts
     * @return a merge set
     */
    public static @NonNull Set<Merge> of(final Merge@NonNull... merges) {
      return ShadyPines.enumSet(Merge.class, merges);
    }

    static boolean hasAll(final @NonNull Set<Merge> merges) {
      return merges.size() == ALL.size();
    }

    /**
     * A merge strategy.
     */
    public enum Strategy {
      // CHECKSTYLE:OFF
      /**
       * Always merge onto target.
       */
      ALWAYS {
        @Override boolean mergeColor(final @NonNull Builder target, final @Nullable TextColor color) { return true; }
        @Override boolean mergeDecoration(final @NonNull Builder target, final @NonNull TextDecoration decoration) { return true; }
        @Override boolean mergeClickEvent(final @NonNull Builder target, final @Nullable ClickEvent event) { return true; }
        @Override boolean mergeHoverEvent(final @NonNull Builder target, final @Nullable HoverEvent<?> event) { return true; }
        @Override boolean mergeInsertion(final @NonNull Builder target, final @Nullable String insertion) { return true; }
        @Override boolean mergeFont(final @NonNull Builder target, final @Nullable Key font) { return true; }
      },
      /**
       * Never merges onto target.
       */
      NEVER {
        @Override boolean mergeColor(final @NonNull Builder target, final @Nullable TextColor color) { return false; }
        @Override boolean mergeDecoration(final @NonNull Builder target, final @NonNull TextDecoration decoration) { return false; }
        @Override boolean mergeClickEvent(final @NonNull Builder target, final @Nullable ClickEvent event) { return false; }
        @Override boolean mergeHoverEvent(final @NonNull Builder target, final @Nullable HoverEvent<?> event) { return false; }
        @Override boolean mergeInsertion(final @NonNull Builder target, final @Nullable String insertion) { return false; }
        @Override boolean mergeFont(final @NonNull Builder target, final @Nullable Key font) { return false; }
      },
      // CHECKSTYLE:ON
      /**
       * Merge onto target when not already set on target.
       */
      IF_ABSENT_ON_TARGET {
        @Override
        boolean mergeColor(final @NonNull Builder target, final @Nullable TextColor color) {
          return target.color == null;
        }

        @Override
        boolean mergeDecoration(final @NonNull Builder target, final @NonNull TextDecoration decoration) {
          if(decoration == TextDecoration.OBFUSCATED) {
            return target.obfuscated == TextDecoration.State.NOT_SET;
          } else if(decoration == TextDecoration.BOLD) {
            return target.bold == TextDecoration.State.NOT_SET;
          } else if(decoration == TextDecoration.STRIKETHROUGH) {
            return target.strikethrough == TextDecoration.State.NOT_SET;
          } else if(decoration == TextDecoration.UNDERLINED) {
            return target.underlined == TextDecoration.State.NOT_SET;
          } else if(decoration == TextDecoration.ITALIC) {
            return target.italic == TextDecoration.State.NOT_SET;
          }
          throw new IllegalArgumentException();
        }

        @Override
        boolean mergeClickEvent(final @NonNull Builder target, final @Nullable ClickEvent event) {
          return target.clickEvent == null;
        }

        @Override
        boolean mergeHoverEvent(final @NonNull Builder target, final @Nullable HoverEvent<?> event) {
          return target.hoverEvent == null;
        }

        @Override
        boolean mergeInsertion(final @NonNull Builder target, final @Nullable String insertion) {
          return target.insertion == null;
        }

        @Override
        boolean mergeFont(final @NonNull Builder target, final @Nullable Key font) {
          return target.font == null;
        }
      };

      // CHECKSTYLE:OFF
      abstract boolean mergeColor(final @NonNull Builder target, final @Nullable TextColor color);
      abstract boolean mergeDecoration(final @NonNull Builder target, final @NonNull TextDecoration decoration);
      abstract boolean mergeClickEvent(final @NonNull Builder target, final @Nullable ClickEvent event);
      abstract boolean mergeHoverEvent(final @NonNull Builder target, final @Nullable HoverEvent<?> event);
      abstract boolean mergeInsertion(final @NonNull Builder target, final @Nullable String insertion);
      abstract boolean mergeFont(final @NonNull Builder target, final @Nullable Key font);
      // CHECKSTYLE:ON
    }
  }

  /**
   * A style builder.
   */
  public static final class Builder implements Buildable.AbstractBuilder<Style> {
    /**
     * The font.
     */
    private @Nullable Key font;
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
    private @Nullable HoverEvent<?> hoverEvent;
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
      this.color = style.color;
      this.obfuscated = style.obfuscated;
      this.bold = style.bold;
      this.strikethrough = style.strikethrough;
      this.underlined = style.underlined;
      this.italic = style.italic;
      this.clickEvent = style.clickEvent;
      this.hoverEvent = style.hoverEvent;
      this.insertion = style.insertion;
      this.font = style.font;
    }

    /**
     * Sets the font.
     *
     * @param font the font
     * @return this builder
     */
    public @NonNull Builder font(final @Nullable Key font) {
      this.font = font;
      return this;
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
      requireNonNull(state, "state");
      if(decoration == TextDecoration.BOLD) {
        this.bold = state;
        return this;
      } else if(decoration == TextDecoration.ITALIC) {
        this.italic = state;
        return this;
      } else if(decoration == TextDecoration.UNDERLINED) {
        this.underlined = state;
        return this;
      } else if(decoration == TextDecoration.STRIKETHROUGH) {
        this.strikethrough = state;
        return this;
      } else if(decoration == TextDecoration.OBFUSCATED) {
        this.obfuscated = state;
        return this;
      }
      throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
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
    public @NonNull Builder hoverEvent(final @Nullable HoverEvent<?> event) {
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
     * @param strategy the merge strategy
     * @return a style
     */
    public @NonNull Builder merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy) {
      return this.merge(that, strategy, Merge.all());
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param merges the parts to merge
     * @return a style
     */
    public @NonNull Builder merge(final @NonNull Style that, final @NonNull Merge@NonNull... merges) {
      if(merges.length == 0) return this;
      return this.merge(that, Merge.of(merges));
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param strategy the merge strategy
     * @param merges the parts to merge
     * @return a style
     */
    public @NonNull Builder merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Merge@NonNull... merges) {
      if(merges.length == 0) return this;
      return this.merge(that, strategy, Merge.of(merges));
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param merges the parts to merge
     * @return a style
     */
    public @NonNull Builder merge(final @NonNull Style that, final @NonNull Set<Merge> merges) {
      return this.merge(that, Merge.Strategy.ALWAYS, merges);
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param strategy the merge strategy
     * @param merges the parts to merge
     * @return a style
     */
    public @NonNull Builder merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Set<Merge> merges) {
      if(that.isEmpty() || strategy == Merge.Strategy.NEVER || merges.isEmpty()) {
        // nothing to merge
        return this;
      }

      if(merges.contains(Merge.COLOR)) {
        final TextColor color = that.color();
        if(color != null && strategy.mergeColor(this, color)) this.color(color);
      }

      if(merges.contains(Merge.DECORATIONS)) {
        for(int i = 0, length = DECORATIONS.length; i < length; i++) {
          final TextDecoration decoration = DECORATIONS[i];
          final TextDecoration.State state = that.decoration(decoration);
          if(state != TextDecoration.State.NOT_SET && strategy.mergeDecoration(this, decoration)) this.decoration(decoration, state);
        }
      }

      if(merges.contains(Merge.EVENTS)) {
        final ClickEvent clickEvent = that.clickEvent();
        if(clickEvent != null && strategy.mergeClickEvent(this, clickEvent)) this.clickEvent(clickEvent);

        final HoverEvent<?> hoverEvent = that.hoverEvent();
        if(hoverEvent != null && strategy.mergeHoverEvent(this, hoverEvent)) this.hoverEvent(hoverEvent);
      }

      if(merges.contains(Merge.INSERTION)) {
        final String insertion = that.insertion();
        if(insertion != null && strategy.mergeInsertion(this, insertion)) this.insertion(insertion);
      }

      if(merges.contains(Merge.FONT)) {
        final Key font = that.font();
        if(font != null && strategy.mergeFont(this, font)) this.font(font);
      }

      return this;
    }

    /**
     * Builds the style.
     *
     * @return the style
     */
    @Override
    public @NonNull Style build() {
      if(this.isEmpty()) {
        return EMPTY;
      }
      return new Style(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    }

    private boolean isEmpty() {
      return this.color == null
        && this.obfuscated == TextDecoration.State.NOT_SET
        && this.bold == TextDecoration.State.NOT_SET
        && this.strikethrough == TextDecoration.State.NOT_SET
        && this.underlined == TextDecoration.State.NOT_SET
        && this.italic == TextDecoration.State.NOT_SET
        && this.clickEvent == null
        && this.hoverEvent == null
        && this.insertion == null
        && this.font == null;
    }
  }
}
