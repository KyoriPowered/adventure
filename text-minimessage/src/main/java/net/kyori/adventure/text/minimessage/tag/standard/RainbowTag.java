/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import java.util.stream.Stream;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.HSVLike;
import net.kyori.examination.ExaminableProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Applies rainbow color to a component.
 *
 * @since 4.10.0
 */
@NullMarked
final class RainbowTag extends AbstractColorChangingTag {
  private static final String REVERSE = "!";
  private static final String RAINBOW = "rainbow";

  static final TagResolver RESOLVER = TagResolver.resolver(RAINBOW, RainbowTag::create);

  private final boolean reversed;
  private final int phase;

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

    return new RainbowTag(reversed, phase);
  }

  private RainbowTag(final boolean reversed, final int phase) {
    this.reversed = reversed;
    this.phase = phase;
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
    final float hue = (index / this.size() + this.phase / 10f) % 1;
    return TextColor.color(HSVLike.hsvLike(hue, 1f, 1f));
  }

  @Override
  public Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("phase", this.phase));
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final RainbowTag that = (RainbowTag) other;
    return this.colorIndex == that.colorIndex && this.phase == that.phase;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.colorIndex, this.phase);
  }
}
