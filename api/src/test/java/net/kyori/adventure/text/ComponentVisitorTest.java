/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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

import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComponentVisitorTest {

  @Test
  public void testOf() {
    final int[] hits = {0};

    Component.text()
      .content("Purity Ring ")
      .append(Component.text("are absolutely", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" the best "))
      .append(Component.translatable("some.adjective"))
      .append(Component.text(" band", NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("ever"))))
      .append(Component.text("."))
      .build().visit(component -> hits[0]++);

    assertEquals(6, hits[0]);
  }

  @Test
  public void testOfWhile() {
    final int[] hits = {0};

    Component.text()
      .content("Purity Ring ")
      .append(Component.text("are absolutely", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" the best "))
      .append(Component.translatable("some.adjective"))
      .append(Component.text(" band", NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("ever"))))
      .append(Component.text("."))
      .build().visitWhile(component -> {
        hits[0]++;
        return component instanceof TextComponent;
      });

    assertEquals(4, hits[0]);
  }

  @Test
  public void testOfUntil() {
    final int[] hits = {0};

    Component.text()
      .content("Purity Ring ")
      .append(Component.text("are absolutely", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" the best "))
      .append(Component.translatable("some.adjective"))
      .append(Component.text(" band", NamedTextColor.GREEN).hoverEvent(HoverEvent.showText(Component.text("ever"))))
      .append(Component.text("."))
      .build().visitUntil(component -> {
        hits[0]++;
        return component.hoverEvent() != null;
      });

    assertEquals(5, hits[0]);
  }
}
