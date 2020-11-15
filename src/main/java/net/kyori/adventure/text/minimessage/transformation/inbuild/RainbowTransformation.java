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
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.transformation.Inserting;
import net.kyori.adventure.text.minimessage.transformation.OneTimeTransformation;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

public class RainbowTransformation extends OneTimeTransformation implements Inserting {
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.RAINBOW);
  }

  private int colorIndex = 0;

  private float center = 128;
  private float width = 127;
  private double frequency = 1;

  private int phase;

  private RainbowTransformation() {
  }

  @Override
  public void load(String name, List<Token> args) {
    super.load(name, args);

    if (Token.oneString(args)) {
      try {
        phase = Integer.parseInt(args.get(0).value());
      } catch (NumberFormatException ex) {
        throw new ParsingException("Expected phase, got " + args.get(0).value(), -1);
      }
    }
  }

  @Override
  public Component applyOneTime(Component current, TextComponent.Builder parent, ArrayDeque<Transformation> transformations) {
    if (current instanceof TextComponent) {
      TextComponent textComponent = (TextComponent) current;
      String content = textComponent.content();

      // init
      center = 128;
      width = 127;
      frequency = Math.PI * 2 / content.length();

      // apply
      for (char c : content.toCharArray()) {
        TextComponent comp = Component.text(c, getColor(phase));
        parent.append(comp);
      }

      return null;
    }

    throw new ParsingException("Expected Text Comp", -1);
  }

  private TextColor getColor(float phase) {
    int index = colorIndex++;
    int red = (int) (Math.sin(frequency * index + 2 + phase) * width + center);
    int green = (int) (Math.sin(frequency * index + 0 + phase) * width + center);
    int blue = (int) (Math.sin(frequency * index + 4 + phase) * width + center);
    return TextColor.color(red, green, blue);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("phase", this.phase));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RainbowTransformation that = (RainbowTransformation) o;
    return colorIndex == that.colorIndex && Float.compare(that.center, center) == 0 && Float.compare(that.width, width) == 0 && Double.compare(that.frequency, frequency) == 0 && phase == that.phase;
  }

  @Override
  public int hashCode() {
    return Objects.hash(colorIndex, center, width, frequency, phase);
  }

  public static class Parser implements TransformationParser<RainbowTransformation> {
    @Override
    public RainbowTransformation parse() {
      return new RainbowTransformation();
    }
  }
}
