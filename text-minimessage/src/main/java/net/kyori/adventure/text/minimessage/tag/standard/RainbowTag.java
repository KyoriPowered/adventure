/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.HSVLike;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Applies rainbow color to a component.
 *
 * @since 4.10.0
 */
final class RainbowTag extends AbstractColorChangingTag {
  private static final String REVERSE = "!";
  private static final String RAINBOW = "rainbow";

  static final TagResolver RESOLVER = SerializableResolver.claimingComponent(RAINBOW, RainbowTag::create, AbstractColorChangingTag::claimComponent);

  private final boolean reversed;
  private final double dividedPhase;

  private int colorIndex = 0;

  static Tag create(final ArgumentQueue args, final Context ctx) {
    boolean reversed = false;
    int phase = 0;

    if (args.hasNext()) {
      String value = args.pop().value();
      if (value.startsWith(REVERSE)) {
        reversed = true;
        value = value.substring(REVERSE.length());
      }
      if (value.length() > 0) {
        try {
          phase = Integer.parseInt(value);
        } catch (final NumberFormatException ex) {
          throw ctx.newException("Expected phase, got " + value);
        }
      }
    }

    return new RainbowTag(reversed, phase, ctx);
  }

  private RainbowTag(final boolean reversed, final int phase, final Context ctx) {
    super(ctx);
    this.reversed = reversed;
    this.dividedPhase = ((double) phase) / 10d;
  }

  @Override
  protected void init() {
    if (this.reversed) {
      this.colorIndex = this.size() - 1;
    }
  }

  @Override
  protected void advanceColor() {
    if (this.reversed) {
      if (this.colorIndex == 0) {
        this.colorIndex = this.size() - 1;
      } else {
        this.colorIndex--;
      }
    } else {
      this.colorIndex++;
    }
  }

  @Override
  protected TextColor color() {
    final float index = this.colorIndex;
    final float hue = (float) ((index / this.size() + this.dividedPhase) % 1f);
    return TextColor.color(HSVLike.hsvLike(hue, 1f, 1f));
  }

  @Override
  protected @NotNull Consumer<TokenEmitter> preserveData() {
    final boolean reversed = this.reversed;
    final int phase = (int) Math.round(this.dividedPhase * 10);
    return emit -> {
      emit.tag(RAINBOW);
      if (reversed && phase != 0) {
        emit.argument(REVERSE + phase);
      } else if (reversed) {
        emit.argument(REVERSE);
      } else if (phase != 0) {
        emit.argument(Integer.toString(phase));
      }
    };
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("phase", this.dividedPhase));
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final RainbowTag that = (RainbowTag) other;
    return this.colorIndex == that.colorIndex && this.dividedPhase == that.dividedPhase;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.colorIndex, this.dividedPhase);
  }
}
