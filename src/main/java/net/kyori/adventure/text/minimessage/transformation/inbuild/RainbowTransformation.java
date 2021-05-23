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

import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.Element;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Applies rainbow color to a component.
 *
 * @since 4.1.0
 */
public final class RainbowTransformation extends Transformation {
  private int colorIndex = 0;

  private float center = 128;
  private float width = 127;
  private double frequency = 1;

  private int phase;

  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.1.0
   */
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.RAINBOW);
  }

  private RainbowTransformation() {
  }

  @Override
  public void load(final String name, final List<Element.TagPart> args) {
    super.load(name, args);

    if(args.size() == 1) {
      try {
        this.phase = Integer.parseInt(args.get(0).getValue());
      } catch(final NumberFormatException ex) {
        throw new ParsingException("Expected phase, got " + args.get(0), -1);
      }
    }
  }

  @Override
  public Component apply() {
//    if(current instanceof TextComponent) {
//      final TextComponent textComponent = (TextComponent) current;
//      final String content = textComponent.content();
//
//      // init
//      this.center = 128;
//      this.width = 127;
//      this.frequency = Math.PI * 2 / content.length();
//
//      // apply
//      int charSize;
//      final char[] holder = new char[2];
//      for(final PrimitiveIterator.OfInt it = content.codePoints().iterator(); it.hasNext();) {
//        charSize = Character.toChars(it.nextInt(), holder, 0);
//        Component comp = Component.text(new String(holder, 0, charSize));
//        comp = this.merge(comp, current);
//        comp = comp.color(this.color(this.phase));
//        parent.append(comp);
//      }
//
//      return null;
//    }
//
//    throw new ParsingException("Expected Text Comp", -1);
    return Component.empty(); // TODO rainbow
  }

  private TextColor color(final float phase) {
    final int index = this.colorIndex++;
    final int red = (int) (Math.sin(this.frequency * index + 2 + phase) * this.width + this.center);
    final int green = (int) (Math.sin(this.frequency * index + 0 + phase) * this.width + this.center);
    final int blue = (int) (Math.sin(this.frequency * index + 4 + phase) * this.width + this.center);
    return TextColor.color(red, green, blue);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("phase", this.phase));
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final RainbowTransformation that = (RainbowTransformation) other;
    return this.colorIndex == that.colorIndex
      && Float.compare(that.center, this.center) == 0
      && Float.compare(that.width, this.width) == 0
      && Double.compare(that.frequency, this.frequency) == 0
      && this.phase == that.phase;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.colorIndex, this.center, this.width, this.frequency, this.phase);
  }

  /**
   * Factory for {@link RainbowTransformation} instances.
   *
   * @since 4.1.0
   */
  public static class Parser implements TransformationParser<RainbowTransformation> {
    @Override
    public RainbowTransformation parse() {
      return new RainbowTransformation();
    }
  }
}
