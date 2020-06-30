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
package net.kyori.adventure.text.minimessage.fancy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Gradient implements Fancy {

  private int colorIndex = 0;

  private float factorStep = 0;
  private final TextColor color1;
  private final TextColor color2;

  public Gradient() {
    this(TextColor.fromHexString("#ffffff"), TextColor.fromHexString("#000000"));
  }

  public Gradient(TextColor c1, TextColor c2) {
    this.color1 = c1;
    this.color2 = c2;
  }

  @Override
  public void init(int size) {
    this.factorStep = (float) (1. / (size + this.colorIndex - 1));
    this.colorIndex = 0;
  }

  @Override
  public Component apply(Component current) {
    return current.color(getColor());
  }

  private TextColor getColor() {
    float factor = factorStep * colorIndex++;
    return interpolate(color1, color2, factor);
  }

  private TextColor interpolate(TextColor color1, TextColor color2, float factor) {
    return TextColor.of(
      Math.round(color1.red() + factor * (color2.red() - color1.red())),
      Math.round(color1.green() + factor * (color2.green() - color1.green())),
      Math.round(color1.blue() + factor * (color2.blue() - color1.blue()))
    );
  }
}
