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
package net.kyori.adventure.text;

import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ComponentTest {
  @Test
  void testStyleReset() {
    Component component = Component.text("kittens");
    assertFalse(component.hasStyling());
    component = component.decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);
    assertTrue(component.hasStyling());
    component = component.style(Style.empty());
    assertFalse(component.hasStyling());
  }

  @Test
  void testCycleHoverRoot() {
    assertThrows(IllegalStateException.class, () -> {
      final Component hoverComponent = Component.text("hover");
      final Component component = Component.text()
        .content("cat")
        .hoverEvent(HoverEvent.showText(hoverComponent))
        .build();
      // component's hover event value is hoverComponent, we should not be able to add it
      hoverComponent.append(component);
      fail("A component was added to itself");
    });
  }

  @Test
  void testCycleHoverChild() {
    assertThrows(IllegalStateException.class, () -> {
      final Component hoverComponent = Component.text("hover child");
      final Component component = Component.text().content("cat")
        .hoverEvent(HoverEvent.showText(Component.text("hover").append(hoverComponent)))
        .build();
      // component's hover event value contains hoverComponent, we should not be able to add it
      hoverComponent.append(component);
      fail("A component was added to itself");
    });
  }
}
