/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage.transformation.inbuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.Element;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A transformation that applies a colour gradient.
 *
 * @since 4.1.0
 */
public final class GradientTransformation extends Transformation {
  private int index = 0;
  private int colorIndex = 0;

  private float factorStep = 0;
  private TextColor[] colors;
  private float phase = 0;
  private boolean negativePhase = false;

  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.1.0
   */
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.GRADIENT);
  }

  private GradientTransformation() {
  }

  @Override
  public void load(final String name, final List<Element.TagPart> args) {
    super.load(name, args);

    if(!args.isEmpty()) {
      final List<TextColor> textColors = new ArrayList<>();
      for(int i = 0; i < args.size(); i++) {
        final String arg = args.get(i).getValue();
        // last argument? maybe this is the phase?
        if(i == args.size() - 1) {
          try {
            this.phase = Float.parseFloat(arg);
            if(this.phase < -1f || this.phase > 1f) {
              throw new ParsingException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0f, 1.0f] (inclusive).", this.phase), -1);
            }
            if(this.phase < 0) {
              this.negativePhase = true;
              this.phase = 1 + this.phase;
            }
            break;
          } catch(final NumberFormatException ignored) {
          }
        }

        final TextColor parsedColor;
        if(arg.charAt(0) == '#') {
          parsedColor = TextColor.fromHexString(arg);
        } else {
          parsedColor = NamedTextColor.NAMES.value(arg.toLowerCase(Locale.ROOT));
        }
        if(parsedColor == null) {
          throw new ParsingException(String.format("Unable to parse a color from '%s'. Please use NamedTextColors or Hex colors.", arg), -1);
        }
        textColors.add(parsedColor);
      }
      if(textColors.size() < 2) {
        throw new ParsingException("Invalid gradient, not enough colors. Gradients must have at least two colors.", -1);
      }
      this.colors = textColors.toArray(new TextColor[0]);
      if(this.negativePhase) {
        Collections.reverse(Arrays.asList(this.colors));
      }
    } else {
      this.colors = new TextColor[] {TextColor.fromHexString("#ffffff"), TextColor.fromHexString("#000000")};
    }
  }

  @Override
  public Component apply() {
//    if(current instanceof TextComponent) {
//      final TextComponent textComponent = (TextComponent) current;
//      final String content = textComponent.content();
//
//      // init
//      final int size = content.length();
//      final int sectorLength = size / (this.colors.length - 1);
//      this.factorStep = 1.0f / (sectorLength + this.index);
//      this.phase = this.phase * sectorLength;
//      this.index = 0;
//
//      // apply
//      int charSize;
//      final char[] holder = new char[2];
//      for(final PrimitiveIterator.OfInt it = content.codePoints().iterator(); it.hasNext();) {
//        charSize = Character.toChars(it.nextInt(), holder, 0);
//        Component comp = Component.text(new String(holder, 0, charSize));
//        comp = this.merge(comp, current);
//        comp = comp.color(this.color());
//        parent.append(comp);
//      }
//
//      return null;
//    }
//
//    throw new ParsingException("Expected TextComponent, got: " + current.getClass().toString(), -1);
    return Component.empty(); // TODO gradient
  }

  private TextColor color() {
    // color switch needed?
    if(this.factorStep * this.index > 1) {
      this.colorIndex++;
      this.index = 0;
    }

    float factor = this.factorStep * (this.index++ + this.phase);
    // loop around if needed
    if(factor > 1) {
      factor = 1 - (factor - 1);
    }

    if(this.negativePhase && this.colors.length % 2 != 0) {
      // flip the gradient segment for to allow for looping phase -1 through 1
      return this.interpolate(this.colors[this.colorIndex + 1], this.colors[this.colorIndex], factor);
    } else {
      return this.interpolate(this.colors[this.colorIndex], this.colors[this.colorIndex + 1], factor);
    }
  }

  private TextColor interpolate(final TextColor color1, final TextColor color2, final float factor) {
    return TextColor.color(
            Math.round(color1.red() + factor * (color2.red() - color1.red())),
            Math.round(color1.green() + factor * (color2.green() - color1.green())),
            Math.round(color1.blue() + factor * (color2.blue() - color1.blue()))
    );
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
            ExaminableProperty.of("phase", this.phase),
            ExaminableProperty.of("colors", this.colors)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final GradientTransformation that = (GradientTransformation) other;
    return this.index == that.index
      && this.colorIndex == that.colorIndex
      && Float.compare(that.factorStep, this.factorStep) == 0
      && this.phase == that.phase && Arrays.equals(this.colors, that.colors);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(this.index, this.colorIndex, this.factorStep, this.phase);
    result = 31 * result + Arrays.hashCode(this.colors);
    return result;
  }

  /**
   * Factory for {@link GradientTransformation} instances.
   *
   * @since 4.1.0
   */
  public static class Parser implements TransformationParser<GradientTransformation> {
    @Override
    public GradientTransformation parse() {
      return new GradientTransformation();
    }
  }
}
