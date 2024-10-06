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
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * A transformation that applies a colour gradient.
 *
 * @since 4.10.0
 */
class GradientTag extends AbstractColorChangingTag {
  private static final String GRADIENT = "gradient";
  private static final TextColor DEFAULT_WHITE = TextColor.color(0xffffff);
  private static final TextColor DEFAULT_BLACK = TextColor.color(0x000000);

  static final TagResolver RESOLVER = SerializableResolver.claimingComponent(GRADIENT, GradientTag::create, AbstractColorChangingTag::claimComponent);

  private int index = 0;

  private double multiplier = 1;

  private final TextColor[] colors;
  @Range(from = -1, to = 1) double phase;

  private final boolean negativePhase;

  static Tag create(final ArgumentQueue args, final Context ctx) {
    double phase = 0;
    final List<TextColor> textColors;
    if (args.hasNext()) {
      textColors = new ArrayList<>();
      while (args.hasNext()) {
        final Tag.Argument arg = args.pop();
        // last argument? maybe this is the phase?
        if (!args.hasNext()) {
          final OptionalDouble possiblePhase = arg.asDouble();
          if (possiblePhase.isPresent()) {
            phase = possiblePhase.getAsDouble();
            if (phase < -1d || phase > 1d) {
              throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0, 1.0] (inclusive).", phase), args);
            }
            break;
          }
        }

        final TextColor parsedColor = ColorTagResolver.resolveColor(arg.value(), ctx);
        textColors.add(parsedColor);
      }

      if (textColors.size() == 1) {
        throw ctx.newException("Invalid gradient, not enough colors. Gradients must have at least two colors.", args);
      }
    } else {
      textColors = Collections.emptyList();
    }

    return new GradientTag(phase, textColors);
  }

  GradientTag(final double phase, final List<TextColor> colors) {
    if (colors.isEmpty()) {
      this.colors = new TextColor[]{DEFAULT_WHITE, DEFAULT_BLACK};
    } else {
      this.colors = colors.toArray(new TextColor[0]);
    }

    if (phase < 0) {
      this.negativePhase = true;
      this.phase = 1 + phase; // [-1, 0) -> [0, 1)
      Collections.reverse(Arrays.asList(this.colors));
    } else {
      this.negativePhase = false;
      this.phase = phase;
    }
  }

  @Override
  protected void init() {
    // Set a scaling factor for character indices, so that the colours in a gradient are evenly spread across the original text
    // make it so the max character index maps to the maximum colour
    this.multiplier = this.size() == 1 ? 0 : (double) (this.colors.length - 1) / (this.size() - 1);
    this.phase *= this.colors.length - 1;
    this.index = 0;
  }

  @Override
  protected void advanceColor() {
    this.index++;
  }

  @Override
  protected TextColor color() {
    // from [0, this.colors.length - 1], select the position in the gradient
    // we will wrap around in order to preserve an even cycle as would be seen with non-zero phases
    final double position = ((this.index * this.multiplier) + this.phase);
    final int lowUnclamped = (int) Math.floor(position);

    final int high = (int) Math.ceil(position) % this.colors.length;
    final int low = lowUnclamped % this.colors.length;

    return TextColor.lerp((float) position - lowUnclamped, this.colors[low], this.colors[high]);
  }

  @Override
  protected @NotNull Consumer<TokenEmitter> preserveData() {
    final TextColor[] colors;
    final double phase;

    if (this.negativePhase) {
      colors = Arrays.copyOf(this.colors, this.colors.length);
      Collections.reverse(Arrays.asList(colors));
      phase = this.phase - 1;
    } else {
      colors = this.colors;
      phase = this.phase;
    }

    return emit -> {
      emit.tag(GRADIENT);
      if (colors.length != 2 || !colors[0].equals(DEFAULT_WHITE) || !colors[1].equals(DEFAULT_BLACK)) { // non-default params
        for (final TextColor color : colors) {
          if (color instanceof NamedTextColor) {
            emit.argument(NamedTextColor.NAMES.keyOrThrow((NamedTextColor) color));
          } else {
            emit.argument(color.asHexString());
          }
        }
      }

      if (phase != 0) {
        emit.argument(Double.toString(phase));
      }
    };
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("phase", this.phase),
      ExaminableProperty.of("colors", this.colors)
    );
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final GradientTag that = (GradientTag) other;
    return this.index == that.index
      && this.phase == that.phase
      && Arrays.equals(this.colors, that.colors);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(this.index, this.phase);
    result = 31 * result + Arrays.hashCode(this.colors);
    return result;
  }
}
