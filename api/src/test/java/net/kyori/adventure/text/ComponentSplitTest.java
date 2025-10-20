/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text;

import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComponentSplitTest {
  @Test
  public void testSimple() {
    final Component example = Component.text("first", NamedTextColor.RED).appendNewline().append(Component.text("second"));
    final List<Component> expected = Arrays.asList(
      Component.empty().color(NamedTextColor.RED).append(Component.text("first")),
      Component.empty().color(NamedTextColor.RED).append(Component.text("second"))
    );

    assertEquals(expected, example.split(Component.newline()));
  }

  @Test
  public void testAdvanced() {
    final Component example = Component.text("first", NamedTextColor.RED).appendNewline().append(Component.text("second", NamedTextColor.BLUE));
    final List<Component> expected = Arrays.asList(
      Component.empty().color(NamedTextColor.RED).append(Component.text("first")),
      Component.empty().color(NamedTextColor.RED).append(Component.text("second", NamedTextColor.BLUE))
    );

    assertEquals(expected, example.split(Component.newline()));
  }
}
