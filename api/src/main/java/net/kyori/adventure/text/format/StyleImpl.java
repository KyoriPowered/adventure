/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record StyleImpl(
  @Nullable Key font,
  @Nullable TextColor color,
  TextDecoration.State obfuscated,
  TextDecoration.State bold,
  TextDecoration.State strikethrough,
  TextDecoration.State underlined,
  TextDecoration.State italic,
  @Nullable ClickEvent clickEvent,
  @Nullable HoverEvent<?> hoverEvent,
  @Nullable String insertion
) implements Style {
  static final StyleImpl EMPTY = new StyleImpl(null, null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null);
  static final TextDecoration[] DECORATIONS = TextDecoration.values();

  @Override
  public @Nullable Key font() {
    return this.font;
  }

  @Override
  public @NotNull Style font(final @Nullable Key font) {
    if (Objects.equals(this.font, font)) return this;
    return new StyleImpl(font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
  }

  @Override
  public @Nullable TextColor color() {
    return this.color;
  }

  @Override
  public @NotNull Style color(final @Nullable TextColor color) {
    if (Objects.equals(this.color, color)) return this;
    return new StyleImpl(this.font, color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
  }

  @Override
  public @NotNull Style colorIfAbsent(final @Nullable TextColor color) {
    if (this.color == null) {
      return this.color(color);
    }
    return this;
  }

  @Override
  public TextDecoration.@NotNull State decoration(final @NotNull TextDecoration decoration) {
    return switch (decoration) {
      case BOLD -> this.bold;
      case ITALIC -> this.italic;
      case UNDERLINED -> this.underlined;
      case STRIKETHROUGH -> this.strikethrough;
      case OBFUSCATED -> this.obfuscated;
    };
  }

  @Override
  public @NotNull Style decoration(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
    requireNonNull(state, "state");
    return switch (decoration) {
      case BOLD -> new StyleImpl(this.font, this.color, this.obfuscated, state, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
      case ITALIC -> new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, state, this.clickEvent, this.hoverEvent, this.insertion);
      case UNDERLINED -> new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, state, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
      case STRIKETHROUGH -> new StyleImpl(this.font, this.color, this.obfuscated, this.bold, state, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
      case OBFUSCATED -> new StyleImpl(this.font, this.color, state, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    };
  }

  @Override
  public @NotNull Style decorations(final @NotNull Map<TextDecoration, TextDecoration.State> decorations) {
    final TextDecoration.State obfuscated = decorations.getOrDefault(TextDecoration.OBFUSCATED, this.obfuscated);
    final TextDecoration.State bold = decorations.getOrDefault(TextDecoration.BOLD, this.bold);
    final TextDecoration.State strikethrough = decorations.getOrDefault(TextDecoration.STRIKETHROUGH, this.strikethrough);
    final TextDecoration.State underlined = decorations.getOrDefault(TextDecoration.UNDERLINED, this.underlined);
    final TextDecoration.State italic = decorations.getOrDefault(TextDecoration.ITALIC, this.italic);
    return new StyleImpl(this.font, this.color, obfuscated, bold, strikethrough, underlined, italic, this.clickEvent, this.hoverEvent, this.insertion);
  }

  @Override
  public @Nullable ClickEvent clickEvent() {
    return this.clickEvent;
  }

  @Override
  public @NotNull Style clickEvent(final @Nullable ClickEvent event) {
    return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion);
  }

  @Override
  public @Nullable HoverEvent<?> hoverEvent() {
    return this.hoverEvent;
  }

  @Override
  public @NotNull Style hoverEvent(final @Nullable HoverEventSource<?> source) {
    return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, HoverEventSource.unbox(source), this.insertion);
  }

  @Override
  public @Nullable String insertion() {
    return this.insertion;
  }

  @Override
  public @NotNull Style insertion(final @Nullable String insertion) {
    if (Objects.equals(this.insertion, insertion)) return this;
    return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion);
  }

  @Override
  public @NotNull Style merge(final @NotNull Style that, final Merge.@NotNull Strategy strategy, final @NotNull Set<Merge> merges) {
    if (nothingToMerge(that, strategy, merges)) {
      return this;
    }

    if (this.isEmpty() && Merge.hasAll(merges)) {
      // if the current style is empty and all merge types have been requested
      // we can just return the other style instead of trying to merge
      return that;
    }

    final Builder builder = this.toBuilder();
    builder.merge(that, strategy, merges);
    return builder.build();
  }

  @SuppressWarnings("RedundantIfStatement")
  static boolean nothingToMerge(final @NotNull Style mergeFrom, final Merge.@NotNull Strategy strategy, final @NotNull Set<Merge> merges) {
    if (strategy == Merge.Strategy.NEVER) return true;
    if (mergeFrom.isEmpty()) return true;
    if (merges.isEmpty()) return true;
    return false;
  }

  @Override
  public boolean isEmpty() {
    return this == EMPTY;
  }

  /**
   * Create a builder from this style.
   *
   * @return a builder
   */
  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
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
  public @NotNull String toString() {
    return Internals.toString(this);
  }

  static final class BuilderImpl implements Builder {
    @Nullable Key font;
    @Nullable TextColor color;
    TextDecoration.State obfuscated = TextDecoration.State.NOT_SET;
    TextDecoration.State bold = TextDecoration.State.NOT_SET;
    TextDecoration.State strikethrough = TextDecoration.State.NOT_SET;
    TextDecoration.State underlined = TextDecoration.State.NOT_SET;
    TextDecoration.State italic = TextDecoration.State.NOT_SET;
    @Nullable ClickEvent clickEvent;
    @Nullable HoverEvent<?> hoverEvent;
    @Nullable String insertion;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull StyleImpl style) {
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

    @Override
    public @NotNull Builder font(final @Nullable Key font) {
      this.font = font;
      return this;
    }

    @Override
    public @NotNull Builder color(final @Nullable TextColor color) {
      this.color = color;
      return this;
    }

    @Override
    public @NotNull Builder colorIfAbsent(final @Nullable TextColor color) {
      if (this.color == null) {
        this.color = color;
      }
      return this;
    }

    @Override
    public @NotNull Builder decoration(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
      requireNonNull(state, "state");
      switch (decoration) {
        case BOLD -> this.bold = state;
        case ITALIC -> this.italic = state;
        case UNDERLINED -> this.underlined = state;
        case STRIKETHROUGH -> this.strikethrough = state;
        case OBFUSCATED -> this.obfuscated = state;
        default -> throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
      }
      return this;
    }

    // todo(kashike): promote to public api?
    @NotNull Builder decorationIfAbsent(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
      requireNonNull(state, "state");
      if (decoration == TextDecoration.BOLD) {
        if (this.bold == TextDecoration.State.NOT_SET) {
          this.bold = state;
        }
        return this;
      } else if (decoration == TextDecoration.ITALIC) {
        if (this.italic == TextDecoration.State.NOT_SET) {
          this.italic = state;
        }
        return this;
      } else if (decoration == TextDecoration.UNDERLINED) {
        if (this.underlined == TextDecoration.State.NOT_SET) {
          this.underlined = state;
        }
        return this;
      } else if (decoration == TextDecoration.STRIKETHROUGH) {
        if (this.strikethrough == TextDecoration.State.NOT_SET) {
          this.strikethrough = state;
        }
        return this;
      } else if (decoration == TextDecoration.OBFUSCATED) {
        if (this.obfuscated == TextDecoration.State.NOT_SET) {
          this.obfuscated = state;
        }
        return this;
      }
      throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }

    @Override
    public @NotNull Builder clickEvent(final @Nullable ClickEvent event) {
      this.clickEvent = event;
      return this;
    }

    @Override
    public @NotNull Builder hoverEvent(final @Nullable HoverEventSource<?> source) {
      this.hoverEvent = HoverEventSource.unbox(source);
      return this;
    }

    @Override
    public @NotNull Builder insertion(final @Nullable String insertion) {
      this.insertion = insertion;
      return this;
    }

    @Override
    public @NotNull Builder merge(final @NotNull Style that, final Merge.@NotNull Strategy strategy, final @NotNull Set<Merge> merges) {
      requireNonNull(that, "style");
      requireNonNull(strategy, "strategy");
      requireNonNull(merges, "merges");

      if (nothingToMerge(that, strategy, merges)) {
        return this;
      }

      final Merger merger = merger(strategy);

      if (merges.contains(Merge.COLOR)) {
        final TextColor color = that.color();
        if (color != null) merger.mergeColor(this, color);
      }

      if (merges.contains(Merge.DECORATIONS)) {
        for (int i = 0, length = DECORATIONS.length; i < length; i++) {
          final TextDecoration decoration = DECORATIONS[i];
          final TextDecoration.State state = that.decoration(decoration);
          if (state != TextDecoration.State.NOT_SET) merger.mergeDecoration(this, decoration, state);
        }
      }

      if (merges.contains(Merge.EVENTS)) {
        final ClickEvent clickEvent = that.clickEvent();
        if (clickEvent != null) merger.mergeClickEvent(this, clickEvent);

        final HoverEvent<?> hoverEvent = that.hoverEvent();
        if (hoverEvent != null) merger.mergeHoverEvent(this, hoverEvent);
      }

      if (merges.contains(Merge.INSERTION)) {
        final String insertion = that.insertion();
        if (insertion != null) merger.mergeInsertion(this, insertion);
      }

      if (merges.contains(Merge.FONT)) {
        final Key font = that.font();
        if (font != null) merger.mergeFont(this, font);
      }

      return this;
    }

    private static Merger merger(final Merge.Strategy strategy) {
      if (strategy == Merge.Strategy.ALWAYS) {
        return AlwaysMerger.INSTANCE;
      } else if (strategy == Merge.Strategy.NEVER) {
        throw new UnsupportedOperationException();
      } else if (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET) {
        return IfAbsentOnTargetMerger.INSTANCE;
      }
      throw new IllegalArgumentException(strategy.name());
    }

    @Override
    public @NotNull StyleImpl build() {
      if (this.isEmpty()) {
        return EMPTY;
      }
      return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
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
