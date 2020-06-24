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
package net.kyori.adventure.text.event;

import com.google.common.collect.ImmutableSet;
import com.google.common.testing.EqualsTester;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.TextComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HoverEventTest {
  @Test
  void testEquality() {
    final UUID entity = UUID.randomUUID();
    new EqualsTester()
      .addEqualityGroup(
        HoverEvent.showText(TextComponent.empty()),
        HoverEvent.of(HoverEvent.Action.SHOW_TEXT, TextComponent.empty())
      )
      .addEqualityGroup(
        HoverEvent.showItem(new HoverEvent.ShowItem(Key.of("air"), 1, null)),
        HoverEvent.of(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ShowItem(Key.of("air"), 1, null))
      )
      .addEqualityGroup(
        HoverEvent.showEntity(new HoverEvent.ShowEntity(Key.of("cat"), entity, TextComponent.empty())),
        HoverEvent.of(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.ShowEntity(Key.of("cat"), entity, TextComponent.empty()))
      )
      .testEquals();
  }

  @Test
  void assertReadable() {
    for(final HoverEvent.Action<?> action : ImmutableSet.of(HoverEvent.Action.SHOW_TEXT, HoverEvent.Action.SHOW_ITEM, HoverEvent.Action.SHOW_ENTITY)) {
      assertTrue(action.readable());
    }
  }
}
