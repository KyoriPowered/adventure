/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Changes the color based on a phase param.
 *
 * @since 4.10.0
 */
public final class TransitionTag implements Inserting, Examinable {
  public static final String TRANSITION = "transition";

  private final TextColor[] colors;
  private final float phase;
  private final boolean negativePhase;

  static final TagResolver RESOLVER = TagResolver.resolver(TransitionTag.TRANSITION, TransitionTag::create);

  static Tag create(final ArgumentQueue args, final Context ctx) {
    float phase = 0;
    final List<TextColor> textColors;
    if (args.hasNext()) {
      textColors = new ArrayList<>();
      while (args.hasNext()) {
        final Tag.Argument arg = args.pop();
        // last argument? maybe this is the phase?
        if (!args.hasNext()) {
          final OptionalDouble possiblePhase = arg.asDouble();
          if (possiblePhase.isPresent()) {
            phase = (float) possiblePhase.getAsDouble();
            if (phase < -1f || phase > 1f) {
              throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0f, 1.0f] (inclusive).", phase), args);
            }
            break;
          }
        }

        final String argValue = arg.value();
        final TextColor parsedColor;
        if (argValue.charAt(0) == TextColor.HEX_CHARACTER) {
          parsedColor = TextColor.fromHexString(argValue);
        } else {
          parsedColor = NamedTextColor.NAMES.value(arg.lowerValue());
        }
        if (parsedColor == null) {
          throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colors or hex (#RRGGBB) colors.", argValue), args);
        }
        textColors.add(parsedColor);
      }

      if (textColors.size() < 2) {
        throw ctx.newException("Invalid transition, not enough colors. Transitions must have at least two colors.", args);
      }
    } else {
      textColors = Collections.emptyList();
    }

    return new TransitionTag(phase, textColors);
  }

  private TransitionTag(final float phase, final List<TextColor> colors) {
    if (phase < 0) {
      this.negativePhase = true;
      this.phase = 1 + phase;
      Collections.reverse(colors);
    } else {
      this.negativePhase = false;
      this.phase = phase;
    }

    if (colors.isEmpty()) {
      this.colors = new TextColor[]{TextColor.color(0xffffff), TextColor.color(0x000000)};
    } else {
      this.colors = colors.toArray(new TextColor[0]);
    }
  }

  @Override
  public @NotNull Component value() {
    return Component.text("", this.color());
  }

  private TextColor color() {
    final float steps = 1f / (this.colors.length - 1);
    for (int colorIndex = 1; colorIndex < this.colors.length; colorIndex++) {
      final float val = colorIndex * steps;
      if (val >= this.phase) {
        final float factor = 1 + (this.phase - val) * (this.colors.length - 1);

        if (this.negativePhase) {
          // flip the gradient segment for to allow for looping phase -1 through 1
          return TextColor.lerp(1 - factor, this.colors[colorIndex], this.colors[colorIndex - 1]);
        } else {
          return TextColor.lerp(factor, this.colors[colorIndex - 1], this.colors[colorIndex]);
        }
      }
    }
    return this.colors[0];
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("phase", this.phase),
      ExaminableProperty.of("colors", this.colors)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final TransitionTag that = (TransitionTag) other;
    return this.phase == that.phase && Arrays.equals(this.colors, that.colors);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(this.phase);
    result = 31 * result + Arrays.hashCode(this.colors);
    return result;
  }
}
