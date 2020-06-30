/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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

public class Rainbow implements Fancy {

  private int colorIndex = 0;

  private float center = 128;
  private float width = 127;
  private double frequency = 1;

  private final int phase;

  public Rainbow() {
    this(0);
  }

  public Rainbow(int phase) {
    this.phase = phase;
  }

  @Override
  public void init(int size) {
    center = 128;
    width = 127;
    frequency = Math.PI * 2 / size;
  }

  @Override
  public Component apply(Component current) {
    return current.color(getColor(phase));
  }

  private TextColor getColor(float phase) {
    int index = colorIndex++;
    int red = (int) (Math.sin(frequency * index + 2 + phase) * width + center);
    int green = (int) (Math.sin(frequency * index + 0 + phase) * width + center);
    int blue = (int) (Math.sin(frequency * index + 4 + phase) * width + center);
    return TextColor.of(red, green, blue);
  }
}
