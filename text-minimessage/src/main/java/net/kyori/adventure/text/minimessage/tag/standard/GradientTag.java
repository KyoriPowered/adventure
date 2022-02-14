/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import java.util.PrimitiveIterator;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.parser.node.ValueNode;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tree.Node;
import net.kyori.adventure.util.ShadyPines;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * A transformation that applies a colour gradient.
 *
 * @since 4.10.0
 */
public final class GradientTag implements Modifying, Examinable {
  public static final String GRADIENT = "gradient";

  private int size = 0;
  private int disableApplyingColorDepth = -1;

  private int index = 0;
  private int colorIndex = 0;

  private float factorStep = 0;
  private final TextColor[] colors;
  private float phase;
  private final boolean negativePhase;

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
        if (argValue.charAt(0) == '#') {
          parsedColor = TextColor.fromHexString(argValue);
        } else {
          parsedColor = NamedTextColor.NAMES.value(arg.lowerValue());
        }
        if (parsedColor == null) {
          throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colours or hex (#RRGGBB) colors.", argValue), args);
        }
        textColors.add(parsedColor);
      }

      if (textColors.size() < 2) {
        throw ctx.newException("Invalid gradient, not enough colors. Gradients must have at least two colors.", args);
      }
    } else {
      textColors = Collections.emptyList();
    }

    return new GradientTag(phase, textColors);
  }

  private GradientTag(final float phase, final List<TextColor> colors) {
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
  public void visit(final Node curr) {
    if (curr instanceof ValueNode) {
      final String value = ((ValueNode) curr).value();
      this.size += value.codePointCount(0, value.length());
    } else if (curr instanceof TagNode) {
      final TagNode tag = (TagNode) curr;
      if (tag.tag() instanceof Inserting) {
        // ComponentTransformation.apply() returns the value of the component placeholder
        ComponentFlattener.textOnly().flatten(((Inserting) tag.tag()).value(), s -> this.size += s.codePointCount(0, s.length()));
      }
    }
  }

  @Override
  public void postVisit() {
    // init
    int sectorLength = this.size / (this.colors.length - 1);
    if (sectorLength < 1) {
      sectorLength = 1;
    }
    this.factorStep = 1.0f / (sectorLength + this.index);
    this.phase = this.phase * sectorLength;
    this.index = 0;
  }

  @Override
  public Component apply(final Component current, final int depth) {
    if ((this.disableApplyingColorDepth != -1 && depth > this.disableApplyingColorDepth) || current.style().color() != null) {
      if (this.disableApplyingColorDepth == -1) {
        this.disableApplyingColorDepth = depth;
      }
      // This component has its own color applied, which overrides ours
      // We still want to keep track of where we are though if this is text
      if (current instanceof TextComponent) {
        final String content = ((TextComponent) current).content();
        final int len = content.codePointCount(0, content.length());
        for (int i = 0; i < len; i++) {
          // increment our color index
          this.color();
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
        final Component comp = Component.text(new String(holder, 0, 1), this.color());
        parent.append(comp);
      }

      return parent.build();
    }

    return Component.empty().mergeStyle(current);
  }

  private TextColor color() {
    // color switch needed?
    if (this.factorStep * this.index > 1) {
      this.colorIndex++;
      this.index = 0;
    }

    float factor = this.factorStep * (this.index++ + this.phase);
    // loop around if needed
    if (factor > 1) {
      factor = 1 - (factor - 1);
    }

    if (this.negativePhase && this.colors.length % 2 != 0) {
      // flip the gradient segment for to allow for looping phase -1 through 1
      return TextColor.lerp(factor, this.colors[this.colorIndex + 1], this.colors[this.colorIndex]);
    } else {
      return TextColor.lerp(factor, this.colors[this.colorIndex], this.colors[this.colorIndex + 1]);
    }
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
    final GradientTag that = (GradientTag) other;
    return this.index == that.index
      && this.colorIndex == that.colorIndex
      && ShadyPines.equals(that.factorStep, this.factorStep)
      && this.phase == that.phase && Arrays.equals(this.colors, that.colors);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(this.index, this.colorIndex, this.factorStep, this.phase);
    result = 31 * result + Arrays.hashCode(this.colors);
    return result;
  }
}
