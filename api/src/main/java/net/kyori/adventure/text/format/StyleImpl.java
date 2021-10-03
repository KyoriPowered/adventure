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

final class StyleImpl implements Style {
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  static final StyleImpl EMPTY = new StyleImpl(null, null, DecorationMap.EMPTY, null, null, null);
  // visible to avoid generating accessors when creating a builder
  final @Nullable Key font;
  final @Nullable TextColor color;
  final @NotNull DecorationMap decorations;
  final @Nullable ClickEvent clickEvent;
  final @Nullable HoverEvent<?> hoverEvent;
  final @Nullable String insertion;

  StyleImpl(
    final @Nullable Key font,
    final @Nullable TextColor color,
    final @NotNull Map<TextDecoration, TextDecoration.State> decorations,
    final @Nullable ClickEvent clickEvent,
    final @Nullable HoverEvent<?> hoverEvent,
    final @Nullable String insertion
  ) {
    this.font = font;
    this.color = color;
    this.decorations = DecorationMap.fromMap(decorations);
    this.clickEvent = clickEvent;
    this.hoverEvent = hoverEvent;
    this.insertion = insertion;
  }

  @Override
  public @Nullable Key font() {
    return this.font;
  }

  @Override
  public @NotNull Style font(final @Nullable Key font) {
    if (Objects.equals(this.font, font)) return this;
    return new StyleImpl(font, this.color, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
  }

  @Override
  public @Nullable TextColor color() {
    return this.color;
  }

  @Override
  public @NotNull Style color(final @Nullable TextColor color) {
    if (Objects.equals(this.color, color)) return this;
    return new StyleImpl(this.font, color, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
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
    final @Nullable TextDecoration.State state = this.decorations.get(decoration);
    if (state != null) {
      return state;
    }
    throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
  }

  @Override
  public @NotNull Style decoration(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
    requireNonNull(state, "state");
    final @NotNull TextDecoration.State thisState = this.decoration(decoration);
    if (thisState == state) return this;
    return new StyleImpl(this.font, this.color, this.decorations.with(decoration, state), this.clickEvent, this.hoverEvent, this.insertion);
  }

  @Override
  public @NotNull Map<TextDecoration, TextDecoration.State> decorations() {
    return this.decorations;
  }

  @Override
  public @NotNull Style decorations(final @NotNull Map<TextDecoration, TextDecoration.State> decorations) {
    return new StyleImpl(this.font, this.color, DecorationMap.merge(decorations, this.decorations), this.clickEvent, this.hoverEvent, this.insertion);
  }

  @Override
  public @Nullable ClickEvent clickEvent() {
    return this.clickEvent;
  }

  @Override
  public @NotNull Style clickEvent(final @Nullable ClickEvent event) {
    return new StyleImpl(this.font, this.color, this.decorations, event, this.hoverEvent, this.insertion);
  }

  @Override
  public @Nullable HoverEvent<?> hoverEvent() {
    return this.hoverEvent;
  }

  @Override
  public @NotNull Style hoverEvent(final @Nullable HoverEventSource<?> source) {
    return new StyleImpl(this.font, this.color, this.decorations, this.clickEvent, HoverEventSource.unbox(source), this.insertion);
  }

  @Override
  public @Nullable String insertion() {
    return this.insertion;
  }

  @Override
  public @NotNull Style insertion(final @Nullable String insertion) {
    if (Objects.equals(this.insertion, insertion)) return this;
    return new StyleImpl(this.font, this.color, this.decorations, this.clickEvent, this.hoverEvent, insertion);
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

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("color", this.color),
      ExaminableProperty.of("obfuscated", this.decorations.get(TextDecoration.OBFUSCATED)),
      ExaminableProperty.of("bold", this.decorations.get(TextDecoration.BOLD)),
      ExaminableProperty.of("strikethrough", this.decorations.get(TextDecoration.STRIKETHROUGH)),
      ExaminableProperty.of("underlined", this.decorations.get(TextDecoration.UNDERLINED)),
      ExaminableProperty.of("italic", this.decorations.get(TextDecoration.ITALIC)),
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

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof StyleImpl)) return false;
    final StyleImpl that = (StyleImpl) other;
    return Objects.equals(this.color, that.color)
      && this.decorations.equals(that.decorations)
      && Objects.equals(this.clickEvent, that.clickEvent)
      && Objects.equals(this.hoverEvent, that.hoverEvent)
      && Objects.equals(this.insertion, that.insertion)
      && Objects.equals(this.font, that.font);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(this.color);
    result = (31 * result) + this.decorations.hashCode();
    result = (31 * result) + Objects.hashCode(this.clickEvent);
    result = (31 * result) + Objects.hashCode(this.hoverEvent);
    result = (31 * result) + Objects.hashCode(this.insertion);
    result = (31 * result) + Objects.hashCode(this.font);
    return result;
  }

  static final class BuilderImpl implements Builder {
    @Nullable Key font;
    @Nullable TextColor color;
    final Map<TextDecoration, TextDecoration.State> decorations;
    @Nullable ClickEvent clickEvent;
    @Nullable HoverEvent<?> hoverEvent;
    @Nullable String insertion;

    BuilderImpl() {
      this.decorations = new EnumMap<>(DecorationMap.EMPTY);
    }

    BuilderImpl(final @NotNull StyleImpl style) {
      this.color = style.color;
      this.decorations = new EnumMap<>(style.decorations);
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
      this.decorations.replace(decoration, state);
      return this;
    }

    // todo(kashike): promote to public api?
    @NotNull Builder decorationIfAbsent(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
      requireNonNull(state, "state");
      final @Nullable TextDecoration.State thisState = this.decorations.get(decoration);
      if (thisState == TextDecoration.State.NOT_SET) {
        this.decorations.put(decoration, state);
      }
      if (thisState != null) {
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
      return new StyleImpl(this.font, this.color, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
    }

    private boolean isEmpty() {
      return this.color == null
        && this.decorations.values().stream().allMatch(state -> state == TextDecoration.State.NOT_SET)
        && this.clickEvent == null
        && this.hoverEvent == null
        && this.insertion == null
        && this.font == null;
    }
  }
}
