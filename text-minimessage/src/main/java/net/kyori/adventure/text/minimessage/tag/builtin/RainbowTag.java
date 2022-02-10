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
package net.kyori.adventure.text.minimessage.tag.builtin;

import java.util.Collections;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
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
 * Applies rainbow color to a component.
 *
 * @since 4.10.0
 */
public final class RainbowTag implements Modifying, Examinable {
  private static final String REVERSE = "!";
  public static final String RAINBOW = "rainbow";

  private int size;
  private int disableApplyingColorDepth = -1;

  private int colorIndex = 0;

  private float center = 128;
  private float width = 127;
  private double frequency = 1;
  private final boolean reversed;

  private final int phase;

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
          throw ctx.newError("Expected phase, got " + value);
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
  public void visit(final Node curr) {
    if (curr instanceof ValueNode) {
      final String value = ((ValueNode) curr).value();
      this.size += value.codePointCount(0, value.length());
    } else if (curr instanceof TagNode) {
      final TagNode tag = (TagNode) curr;
      if (tag.tag() instanceof Inserting) {
        // Inserting.apply() returns the value of the component placeholder
        ComponentFlattener.textOnly().flatten(((Inserting) tag.tag()).value(), s -> this.size += s.codePointCount(0, s.length()));
      }
    }
  }

  @Override
  public void postVisit() {
    // init
    this.center = 128;
    this.width = 127;
    this.frequency = Math.PI * 2 / this.size;
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

      if (this.colorIndex == 0 && this.reversed) {
        this.colorIndex = this.size - 1;
      }

      // apply
      final int[] holder = new int[1];
      for (final PrimitiveIterator.OfInt it = content.codePoints().iterator(); it.hasNext();) {
        holder[0] = it.nextInt();
        final Component comp = Component.text(new String(holder, 0, 1), this.color(this.phase));
        parent.append(comp);
      }

      return parent.build();
    }

    return Component.text("", current.style());
  }

  private TextColor color(final float phase) {
    final int index = this.reversed ? this.colorIndex-- : this.colorIndex++;
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
    final RainbowTag that = (RainbowTag) other;
    return this.colorIndex == that.colorIndex
      && ShadyPines.equals(that.center, this.center)
      && ShadyPines.equals(that.width, this.width)
      && ShadyPines.equals(that.frequency, this.frequency)
      && this.phase == that.phase;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.colorIndex, this.center, this.width, this.frequency, this.phase);
  }
}
