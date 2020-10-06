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
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.util.Buildable;
import net.kyori.adventure.util.ShadyPines;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A style.
 *
 * @since 4.0.0
 */
public interface Style extends Buildable<Style, Style.Builder> {
  /**
   * The default font.
   *
   * @since 4.0.0
   */
  Key DEFAULT_FONT = Key.key("default");

  /**
   * Gets an empty style.
   *
   * @return empty style
   * @since 4.0.0
   */
  static @NonNull Style empty() {
    return StyleImpl.EMPTY;
  }

  /**
   * Creates a builder.
   *
   * @return a builder
   * @since 4.0.0
   */
  static @NonNull Builder style() {
    return new StyleImpl.BuilderImpl();
  }

  /**
   * Creates a style.
   *
   * @param consumer the builder consumer
   * @return a style
   * @since 4.0.0
   */
  static @NonNull Style style(final @NonNull Consumer<Builder> consumer) {
    return Buildable.configureAndBuild(style(), consumer);
  }

  /**
   * Creates a style with color.
   *
   * @param color the style
   * @return a style
   * @since 4.0.0
   */
  static @NonNull Style style(final @Nullable TextColor color) {
    if(color == null) return empty();
    return new StyleImpl(null, color, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null);
  }

  /**
   * Creates a style with decoration.
   *
   * @param decoration the decoration
   * @return a style
   * @since 4.0.0
   */
  static @NonNull Style style(final @NonNull TextDecoration decoration) {
    return style().decoration(decoration, true).build();
  }

  /**
   * Creates a style with color and decorations.
   *
   * @param color the style
   * @param decorations the decorations
   * @return a style
   * @since 4.0.0
   */
  static @NonNull Style style(final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    final Builder builder = style();
    builder.color(color);
    StyleImpl.decorate(builder, decorations);
    return builder.build();
  }

  /**
   * Creates a style with color and decorations.
   *
   * @param color the style
   * @param decorations the decorations
   * @return a style
   * @since 4.0.0
   */
  static @NonNull Style style(final @Nullable TextColor color, final Set<TextDecoration> decorations) {
    final Builder builder = style();
    builder.color(color);
    if(!decorations.isEmpty()) {
      for(final TextDecoration decoration : decorations) {
        builder.decoration(decoration, true);
      }
    }
    return builder.build();
  }

  /**
   * Creates a style with {@code applicables} applied.
   *
   * @param applicables the applicables
   * @return a style
   * @since 4.0.0
   */
  static @NonNull Style style(final StyleBuilderApplicable@NonNull... applicables) {
    if(applicables.length == 0) return empty();
    final Builder builder = style();
    for(int i = 0, length = applicables.length; i < length; i++) {
      applicables[i].styleApply(builder);
    }
    return builder.build();
  }

  /**
   * Creates a style with {@code applicables} applied.
   *
   * @param applicables the applicables
   * @return a style
   * @since 4.0.0
   */
  static @NonNull Style style(final @NonNull Iterable<? extends StyleBuilderApplicable> applicables) {
    final Builder builder = style();
    for(final StyleBuilderApplicable applicable : applicables) {
      applicable.styleApply(builder);
    }
    return builder.build();
  }

  /**
   * Creates a builder.
   *
   * @return a builder
   * @since 4.0.0
   * @deprecated use {@link #style()}
   */
  @Deprecated
  static @NonNull Builder builder() {
    return new StyleImpl.BuilderImpl();
  }

  /**
   * Creates a style with color.
   *
   * @param color the style
   * @return a style
   * @since 4.0.0
   * @deprecated use {@link #style(TextColor)}
   */
  @Deprecated
  static @NonNull Style of(final @Nullable TextColor color) {
    return style(color);
  }

  /**
   * Creates a style with decoration.
   *
   * @param decoration the decoration
   * @return a style
   * @since 4.0.0
   * @deprecated use {@link #style(TextDecoration)}
   */
  @Deprecated
  static @NonNull Style of(final @NonNull TextDecoration decoration) {
    return style(decoration);
  }

  /**
   * Creates a style with color and decorations.
   *
   * @param color the style
   * @param decorations the decorations
   * @return a style
   * @since 4.0.0
   * @deprecated use {@link #style(TextColor, TextDecoration...)}
   */
  @Deprecated
  static @NonNull Style of(final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return style(color, decorations);
  }

  /**
   * Creates a style with color and decorations.
   *
   * @param color the style
   * @param decorations the decorations
   * @return a style
   * @since 4.0.0
   * @deprecated use {@link #style(TextColor, Set)}
   */
  @Deprecated
  static @NonNull Style of(final @Nullable TextColor color, final Set<TextDecoration> decorations) {
    return style(color, decorations);
  }

  /**
   * Creates a style with {@code applicables} applied.
   *
   * @param applicables the applicables
   * @return a style
   * @since 4.0.0
   * @deprecated use {@link #style(StyleBuilderApplicable...)}
   */
  @Deprecated
  static @NonNull Style of(final StyleBuilderApplicable@NonNull... applicables) {
    return style(applicables);
  }

  /**
   * Creates a style with {@code applicables} applied.
   *
   * @param applicables the applicables
   * @return a style
   * @since 4.0.0
   * @deprecated use {@link #style(Iterable)}
   */
  @Deprecated
  static @NonNull Style of(final @NonNull Iterable<? extends StyleBuilderApplicable> applicables) {
    return style(applicables);
  }

  /**
   * Creates a style.
   *
   * @param consumer the builder consumer
   * @return a style
   * @since 4.0.0
   * @deprecated use {@link #style(Consumer)}
   */
  @Deprecated
  static @NonNull Style make(final @NonNull Consumer<Builder> consumer) {
    return Buildable.configureAndBuild(style(), consumer);
  }

  /**
   * Edits this style.
   *
   * <p>The old style will be merge into the new style before {@code consumer} is called.</p>
   *
   * @param consumer the consumer
   * @return a new style
   * @since 4.0.0
   */
  default @NonNull Style edit(final @NonNull Consumer<Builder> consumer) {
    return this.edit(consumer, Merge.Strategy.ALWAYS);
  }

  /**
   * Edits this style.
   *
   * @param consumer the consumer
   * @param strategy the merge strategy
   * @return a new style
   * @since 4.0.0
   */
  default @NonNull Style edit(final @NonNull Consumer<Builder> consumer, final Merge.@NonNull Strategy strategy) {
    return style(style -> {
      if(strategy == Merge.Strategy.ALWAYS) {
        style.merge(this, strategy);
      }
      consumer.accept(style);
      if(strategy == Merge.Strategy.IF_ABSENT_ON_TARGET) {
        style.merge(this, strategy);
      }
    });
  }

  /**
   * Gets the font.
   *
   * @return the font
   * @since 4.0.0
   */
  @Nullable Key font();

  /**
   * Sets the font.
   *
   * @param font the font
   * @return a style
   * @since 4.0.0
   */
  @NonNull Style font(final @Nullable Key font);

  /**
   * Gets the color.
   *
   * @return the color
   * @since 4.0.0
   */
  @Nullable TextColor color();

  /**
   * Sets the color.
   *
   * @param color the color
   * @return a style
   * @since 4.0.0
   */
  @NonNull Style color(final @Nullable TextColor color);

  /**
   * Sets the color if there isn't one set already.
   *
   * @param color the color
   * @return this builder
   * @since 4.0.0
   */
  @NonNull Style colorIfAbsent(final @Nullable TextColor color);

  /**
   * Tests if this style has a decoration.
   *
   * @param decoration the decoration
   * @return {@code true} if this style has the decoration, {@code false} if this
   *     style does not have the decoration
   * @since 4.0.0
   */
  default boolean hasDecoration(final @NonNull TextDecoration decoration) {
    return this.decoration(decoration) == TextDecoration.State.TRUE;
  }

  /**
   * Gets the state of a decoration on this style.
   *
   * @param decoration the decoration
   * @return {@link TextDecoration.State#TRUE} if this style has the decoration,
   *     {@link TextDecoration.State#FALSE} if this style does not have the decoration,
   *     and {@link TextDecoration.State#NOT_SET} if not set
   * @since 4.0.0
   */
  TextDecoration.@NonNull State decoration(final @NonNull TextDecoration decoration);

  /**
   * Sets the state of {@code decoration} to {@link TextDecoration.State#TRUE} on this style.
   *
   * @param decoration the decoration
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style decorate(final @NonNull TextDecoration decoration) {
    return this.decoration(decoration, TextDecoration.State.TRUE);
  }

  /**
   * Sets the state of a decoration on this style.
   *
   * @param decoration the decoration
   * @param flag {@code true} if this style should have the decoration, {@code false} if
   *     this style should not have the decoration
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style decoration(final @NonNull TextDecoration decoration, final boolean flag) {
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
   * @since 4.0.0
   */
  @NonNull Style decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state);

  /**
   * Gets a map of decorations this style has.
   *
   * @return a set of decorations this style has
   * @since 4.0.0
   */
  @NonNull Map<TextDecoration, TextDecoration.State> decorations();

  /**
   * Sets decorations for this style using the specified {@code decorations} map.
   *
   * <p>If a given decoration does not have a value explicitly set, the value of that particular decoration is not changed.</p>
   *
   * @param decorations the decorations
   * @return a style
   * @since 4.0.0
   */
  @NonNull Style decorations(final @NonNull Map<TextDecoration, TextDecoration.State> decorations);

  /**
   * Gets the click event.
   *
   * @return the click event
   * @since 4.0.0
   */
  @Nullable ClickEvent clickEvent();

  /**
   * Sets the click event.
   *
   * @param event the click event
   * @return a style
   * @since 4.0.0
   */
  @NonNull Style clickEvent(final @Nullable ClickEvent event);

  /**
   * Gets the hover event.
   *
   * @return the hover event
   * @since 4.0.0
   */
  @Nullable HoverEvent<?> hoverEvent();

  /**
   * Sets the hover event.
   *
   * @param source the hover event source
   * @return a style
   * @since 4.0.0
   */
  @NonNull Style hoverEvent(final @Nullable HoverEventSource<?> source);

  /**
   * Gets the string to be inserted when this style is shift-clicked.
   *
   * @return the insertion string
   * @since 4.0.0
   */
  @Nullable String insertion();

  /**
   * Sets the string to be inserted when this style is shift-clicked.
   *
   * @param insertion the insertion string
   * @return a style
   * @since 4.0.0
   */
  @NonNull Style insertion(final @Nullable String insertion);

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style merge(final @NonNull Style that) {
    return this.merge(that, Merge.all());
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param strategy the merge strategy
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy) {
    return this.merge(that, strategy, Merge.all());
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param merge the part to merge
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style merge(final @NonNull Style that, final @NonNull Merge merge) {
    return this.merge(that, Collections.singleton(merge));
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param strategy the merge strategy
   * @param merge the part to merge
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Merge merge) {
    return this.merge(that, strategy, Collections.singleton(merge));
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param merges the parts to merge
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style merge(final @NonNull Style that, final @NonNull Merge@NonNull... merges) {
    return this.merge(that, Merge.of(merges));
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param strategy the merge strategy
   * @param merges the parts to merge
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Merge@NonNull... merges) {
    return this.merge(that, strategy, Merge.of(merges));
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param merges the parts to merge
   * @return a style
   * @since 4.0.0
   */
  default @NonNull Style merge(final @NonNull Style that, final @NonNull Set<Merge> merges) {
    return this.merge(that, Merge.Strategy.ALWAYS, merges);
  }

  /**
   * Merges from another style into this style.
   *
   * @param that the other style
   * @param strategy the merge strategy
   * @param merges the parts to merge
   * @return a style
   * @since 4.0.0
   */
  @NonNull Style merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Set<Merge> merges);

  /**
   * Tests if this style is empty.
   *
   * @return {@code true} if this style is empty, {@code false} if this
   *     style is not empty
   * @since 4.0.0
   */
  boolean isEmpty();

  /**
   * Create a builder from this style.
   *
   * @return a builder
   */
  @Override
  @NonNull Builder toBuilder();

  /**
   * A merge choice.
   *
   * @since 4.0.0
   */
  enum Merge {
    /**
     * Merges {@link Style#color()}.
     *
     * @since 4.0.0
     */
    COLOR,
    /**
     * Merges {@link Style#decorations()}.
     *
     * @since 4.0.0
     */
    DECORATIONS,
    /**
     * Merges {@link Style#clickEvent()} and {@link Style#hoverEvent()}.
     *
     * @since 4.0.0
     */
    EVENTS,
    /**
     * Merges {@link Style#insertion()}.
     *
     * @since 4.0.0
     */
    INSERTION,
    /**
     * Merges {@link Style#font()}.
     *
     * @since 4.0.0
     */
    FONT;

    static final Set<Merge> ALL = of(values());
    static final Set<Merge> COLOR_AND_DECORATIONS = of(COLOR, DECORATIONS);

    /**
     * Gets a merge set of all merge types.
     *
     * @return a merge set
     * @since 4.0.0
     */
    public static @NonNull Set<Merge> all() {
      return ALL;
    }

    /**
     * Gets a merge set containing {@link #COLOR} and {@link #DECORATIONS}.
     *
     * @return a merge set
     * @since 4.0.0
     */
    public static @NonNull Set<Merge> colorAndDecorations() {
      return COLOR_AND_DECORATIONS;
    }

    /**
     * Creates a merge set.
     *
     * @param merges the merge parts
     * @return a merge set
     * @since 4.0.0
     */
    public static @NonNull Set<Merge> of(final Merge@NonNull... merges) {
      return ShadyPines.enumSet(Merge.class, merges);
    }

    static boolean hasAll(final @NonNull Set<Merge> merges) {
      return merges.size() == ALL.size();
    }

    /**
     * A merge strategy.
     *
     * @since 4.0.0
     */
    public enum Strategy {
      /**
       * Always merge onto target.
       *
       * @since 4.0.0
       */
      ALWAYS,
      /**
       * Never merges onto target.
       *
       * @since 4.0.0
       */
      NEVER,
      /**
       * Merge onto target when not already set on target.
       *
       * @since 4.0.0
       */
      IF_ABSENT_ON_TARGET;
    }
  }

  /**
   * A style builder.
   *
   * @since 4.0.0
   */
  interface Builder extends Buildable.Builder<Style> {
    /**
     * Sets the font.
     *
     * @param font the font
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder font(final @Nullable Key font);

    /**
     * Sets the color.
     *
     * @param color the color
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder color(final @Nullable TextColor color);

    /**
     * Sets the color if there isn't one set already.
     *
     * @param color the color
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder colorIfAbsent(final @Nullable TextColor color);

    /**
     * Sets {@code decoration} to {@link TextDecoration.State#TRUE}.
     *
     * @param decoration the decoration
     * @return a style
     * @since 4.0.0
     */
    default @NonNull Builder decorate(final @NonNull TextDecoration decoration) {
      return this.decoration(decoration, TextDecoration.State.TRUE);
    }

    /**
     * Sets {@code decorations} to {@link TextDecoration.State#TRUE}.
     *
     * @param decorations the decorations
     * @return a style
     * @since 4.0.0
     */
    default @NonNull Builder decorate(final @NonNull TextDecoration@NonNull... decorations) {
      for(int i = 0, length = decorations.length; i < length; i++) {
        this.decorate(decorations[i]);
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
     * @since 4.0.0
     */
    default @NonNull Builder decoration(final @NonNull TextDecoration decoration, final boolean flag) {
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
     * @since 4.0.0
     */
    @NonNull Builder decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state);

    /**
     * Sets the click event.
     *
     * @param event the click event
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder clickEvent(final @Nullable ClickEvent event);

    /**
     * Sets the hover event.
     *
     * @param source the hover event source
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder hoverEvent(final @Nullable HoverEventSource<?> source);

    /**
     * Sets the string to be inserted.
     *
     * @param insertion the insertion string
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder insertion(final @Nullable String insertion);

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @return a style
     * @since 4.0.0
     */
    default @NonNull Builder merge(final @NonNull Style that) {
      return this.merge(that, Merge.all());
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param strategy the merge strategy
     * @return a style
     * @since 4.0.0
     */
    default @NonNull Builder merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy) {
      return this.merge(that, strategy, Merge.all());
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param merges the parts to merge
     * @return a style
     * @since 4.0.0
     */
    default @NonNull Builder merge(final @NonNull Style that, final @NonNull Merge@NonNull... merges) {
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
     * @since 4.0.0
     */
    default @NonNull Builder merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Merge@NonNull... merges) {
      if(merges.length == 0) return this;
      return this.merge(that, strategy, Merge.of(merges));
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param merges the parts to merge
     * @return a style
     * @since 4.0.0
     */
    default @NonNull Builder merge(final @NonNull Style that, final @NonNull Set<Merge> merges) {
      return this.merge(that, Merge.Strategy.ALWAYS, merges);
    }

    /**
     * Merges from another style into this style.
     *
     * @param that the other style
     * @param strategy the merge strategy
     * @param merges the parts to merge
     * @return a style
     * @since 4.0.0
     */
    @NonNull Builder merge(final @NonNull Style that, final Merge.@NonNull Strategy strategy, final @NonNull Set<Merge> merges);

    /**
     * Applies {@code applicable} to this builder.
     *
     * @param applicable the applicable
     * @return this builder
     * @since 4.0.0
     */
    default @NonNull Builder apply(final @NonNull StyleBuilderApplicable applicable) {
      applicable.styleApply(this);
      return this;
    }

    /**
     * Builds the style.
     *
     * @return the style
     */
    @Override
    @NonNull Style build();
  }
}
