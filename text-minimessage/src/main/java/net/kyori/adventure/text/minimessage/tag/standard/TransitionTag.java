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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;

/**
 * Changes the color based on a phase param.
 *
 * @param colors the colors to transition between
 * @param phase the phase for the transition
 * @param negativePhase whether the phase is negative
 * @since 4.10.0
 */
@ApiStatus.Internal
record TransitionTag(
  TextColor[] colors,
  float phase,
  boolean negativePhase
) implements Inserting {

  @ApiStatus.Internal
  public static final String TRANSITION = "transition";

  static final TagResolver RESOLVER = TagResolver.resolver(TransitionTag.TRANSITION, TransitionTag::create);

  static Tag create(final ArgumentQueue args, final Context ctx) {
    float phase = 0;
    final List<TextColor> textColors;
    if (args.hasNext()) {
      textColors = new ArrayList<>();
      while (args.hasNext()) {
        final Argument arg = args.pop();

        // Determine if this is a color first. Double#parseDouble is "slow" in cases where we hit a string.
        final String argValue = arg.value();
        final TextColor color = ColorTagResolver.resolveColorOrNull(argValue);

        if (color != null) {
          textColors.add(color);
        } else {
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

          // We hit an invalid argument here!
          throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colors or hex (#RRGGBB) colors.", argValue), args);
        }
      }

      if (textColors.size() < 2) {
        throw ctx.newException("Invalid transition, not enough colors. Transitions must have at least two colors.", args);
      }
    } else {
      textColors = Collections.emptyList();
    }

    final TextColor[] colorsArray;
    final boolean negativePhase;

    if (phase < 0) {
      negativePhase = true;
      phase = 1 + phase;
      Collections.reverse(textColors);
    } else {
      negativePhase = false;
    }

    if (textColors.isEmpty()) {
      colorsArray = new TextColor[]{TextColor.color(0xffffff), TextColor.color(0x000000)};
    } else {
      colorsArray = textColors.toArray(new TextColor[0]);
    }

    return new TransitionTag(colorsArray, phase, negativePhase);
  }

  @ApiStatus.Internal
  public TransitionTag {
  }

  @Override
  public Component value() {
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
}
