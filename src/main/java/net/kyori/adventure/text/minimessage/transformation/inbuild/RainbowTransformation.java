/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.parser.node.ValueNode;
import net.kyori.adventure.text.minimessage.transformation.Modifying;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Applies rainbow color to a component.
 *
 * @since 4.1.0
 */
public final class RainbowTransformation extends Transformation implements Modifying {

  private int size;
  private int disableApplyingColorDepth = -1;

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
  public void load(final String name, final List<TagPart> args) {
    super.load(name, args);

    if (args.size() == 1) {
      try {
        this.phase = Integer.parseInt(args.get(0).value());
      } catch (final NumberFormatException ex) {
        throw new ParsingException("Expected phase, got " + args.get(0), this.argTokenArray());
      }
    }
  }

  @Override
  public void visit(final ElementNode curr) {
    if (curr instanceof ValueNode) {
      final String value = ((ValueNode) curr).value();
      this.size += value.codePointCount(0, value.length());
    }
  }

  @Override
  public Component apply() {
    // init
    this.center = 128;
    this.width = 127;
    this.frequency = Math.PI * 2 / this.size;

    return Component.empty();
  }

  @Override
  public Component apply(final Component current, final int depth) {
    if ((this.disableApplyingColorDepth != -1 && depth > this.disableApplyingColorDepth) || current.style().color() != null) {
      if (this.disableApplyingColorDepth == -1) {
        this.disableApplyingColorDepth = depth;
      }
      // This component has it's own color applied, which overrides ours
      // We still want to keep track of where we are though if this is text
      if (current instanceof TextComponent) {
        final String content = ((TextComponent) current).content();
        final int len = content.codePointCount(0, content.length());
        for (int i = 0; i < len; i++) {
          // increment our color index
          this.color(this.phase);
        }
      }
      return current.children(Collections.emptyList());
    }

    this.disableApplyingColorDepth = -1;
    if (current instanceof TextComponent && ((TextComponent) current).content().length() > 0) {
      final TextComponent textComponent = (TextComponent) current;
      final String content = textComponent.content();

      final TextComponent.Builder parent = Component.text();

      // apply
      final int[] holder = new int[1];
      for (final PrimitiveIterator.OfInt it = content.codePoints().iterator(); it.hasNext();) {
        holder[0] = it.nextInt();
        final Component comp = Component.text(new String(holder, 0, 1), this.color(this.phase));
        parent.append(comp);
      }

      return parent.build();
    }

    return Component.empty().mergeStyle(current);
  }

  private TextColor color(final float phase) {
    final int index = this.colorIndex++;
    final int red = (int) (Math.sin(this.frequency * index + 2 + phase) * this.width + this.center);
    final int green = (int) (Math.sin(this.frequency * index + 0 + phase) * this.width + this.center);
    final int blue = (int) (Math.sin(this.frequency * index + 4 + phase) * this.width + this.center);
    return TextColor.color(red, green, blue);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("phase", this.phase));
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
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
