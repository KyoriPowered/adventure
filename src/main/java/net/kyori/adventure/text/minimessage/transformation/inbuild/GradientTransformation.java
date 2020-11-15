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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.ParseException;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;
import net.kyori.adventure.text.minimessage.transformation.Inserting;
import net.kyori.adventure.text.minimessage.transformation.OneTimeTransformation;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

// TODO gradient
public class GradientTransformation extends OneTimeTransformation implements Inserting {
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.GRADIENT);
  }

  private int index = 0;
  private int colorIndex = 0;

  private float factorStep = 0;
  private TextColor[] colors;
  private float phase = 0;
  private boolean negativePhase = false;

  private GradientTransformation() {
  }

  @Override
  public void load(String name, List<Token> args) {
    super.load(name, args);

    if (!args.isEmpty()) {
      List<TextColor> textColors = new ArrayList<>();
      for (int i = 0; i < args.size(); i++) {
        Token arg = args.get(i);
        if (arg.type() == TokenType.STRING) {
          // last argument? maybe this is the phase?
          if (i == args.size() - 1) {
            try {
              this.phase = Float.parseFloat(arg.value());
              if (phase < -1f || phase > 1f) {
                throw new ParseException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0f, 1.0f] (inclusive).", phase));
              }
              if (phase < 0) {
                this.negativePhase = true;
                this.phase = 1 + phase;
              }
              break;
            } catch (NumberFormatException ignored) {}
          }

          if(arg.value().charAt(0) == '#') {
            textColors.add(TextColor.fromHexString(arg.value()));
          } else {
            textColors.add(NamedTextColor.NAMES.value(arg.value().toLowerCase(Locale.ROOT)));
          }
        }
      }
      this.colors = textColors.toArray(new TextColor[0]);
      if (this.negativePhase) {
        Collections.reverse(Arrays.asList(this.colors));
      }
    } else {
      colors = new TextColor[] { TextColor.fromHexString("#ffffff"), TextColor.fromHexString("#000000")};
    }
  }

  @Override
  public Component applyOneTime(Component current, TextComponent.Builder parent, ArrayDeque<Transformation> transformations) {
    if (current instanceof TextComponent) {
      TextComponent textComponent = (TextComponent) current;
      String content = textComponent.content();

      // init
      int size = content.length();
      final int sectorLength = size / (this.colors.length - 1);
      this.factorStep = 1.0f / (sectorLength + this.index);
      this.phase = this.phase * (sectorLength);
      this.index = 0;

      // apply
      int charSize;
      final char[] holder = new char[2];
      for (PrimitiveIterator.OfInt it = content.codePoints().iterator(); it.hasNext();) {
        charSize = Character.toChars(it.nextInt(), holder, 0);
        Component comp = Component.text(new String(holder, 0, charSize));
        comp = merge(comp, current);
        comp = comp.color(getColor());
        parent.append(comp);
      }

      return null;
    }

    throw new ParsingException("Expected Text Comp", -1);
  }

  private TextColor getColor() {
    // color switch needed?
    if (factorStep * index > 1) {
      colorIndex++;
      index = 0;
    }

    float factor = factorStep * (index++ + phase);
    // loop around if needed
    if (factor > 1) {
      factor = 1 - (factor - 1);
    }

    if (negativePhase && colors.length % 2 != 0) {
      // flip the gradient segment for to allow for looping phase -1 through 1
      return interpolate(colors[colorIndex + 1], colors[colorIndex], factor);
    } else {
      return interpolate(colors[colorIndex], colors[colorIndex + 1], factor);
    }
  }

  private TextColor interpolate(TextColor color1, TextColor color2, float factor) {
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GradientTransformation that = (GradientTransformation) o;
    return index == that.index && colorIndex == that.colorIndex && Float.compare(that.factorStep, factorStep) == 0 && phase == that.phase && Arrays.equals(colors, that.colors);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(index, colorIndex, factorStep, phase);
    result = 31 * result + Arrays.hashCode(colors);
    return result;
  }

  public static class Parser implements TransformationParser<GradientTransformation> {
    @Override
    public GradientTransformation parse() {
      return new GradientTransformation();
    }
  }
}
