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
import java.util.function.UnaryOperator;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HoverEventTest {
  @Test
  void testAsHoverEvent() {
    final HoverEvent<Component> event = HoverEvent.showText(TextComponent.of("kittens"));
    assertSame(event, event.asHoverEvent());
    assertSame(event, event.asHoverEvent(UnaryOperator.identity()));
    assertEquals(HoverEvent.showText(TextComponent.of("cats")), event.asHoverEvent(old -> TextComponent.of("cats")));
  }

  @Test
  void testShowItemItem() {
    final HoverEvent.ShowItem si0 = new HoverEvent.ShowItem(Key.of("stone"), 1);
    assertEquals(Key.of("stone"), si0.item());
    final HoverEvent.ShowItem si1 = si0.item(Key.of("dirt"));
    assertEquals(Key.of("stone"), si0.item()); // original should be unmodified
    assertEquals(Key.of("dirt"), si1.item());
  }

  @Test
  void testShowItemCount() {
    final HoverEvent.ShowItem si0 = new HoverEvent.ShowItem(Key.of("stone"), 1);
    assertEquals(1, si0.count());
    assertSame(si0, si0.count(1)); // unmodified
    final HoverEvent.ShowItem si1 = si0.count(2);
    assertEquals(1, si0.count()); // original should be unmodified
    assertEquals(2, si1.count());
  }

  @Test
  void testShowEntityType() {
    final HoverEvent.ShowEntity se0 = new HoverEvent.ShowEntity(Key.of("cow"), UUID.randomUUID());
    assertEquals(Key.of("cow"), se0.type());
    final HoverEvent.ShowEntity se1 = se0.type(Key.of("chicken"));
    assertEquals(Key.of("cow"), se0.type()); // original should be unmodified
    assertEquals(Key.of("chicken"), se1.type());
  }

  @Test
  void testShowEntityId() {
    final UUID id0 = UUID.randomUUID();
    final HoverEvent.ShowEntity se0 = new HoverEvent.ShowEntity(Key.of("cow"), id0);
    assertEquals(id0, se0.id());
    final UUID id1 = UUID.randomUUID();
    final HoverEvent.ShowEntity se1 = se0.id(id1);
    assertEquals(id0, se0.id()); // original should be unmodified
    assertEquals(id1, se1.id());
  }

  @Test
  void testShowEntityName() {
    final Component n0 = TextComponent.of("Cow");
    final HoverEvent.ShowEntity se0 = new HoverEvent.ShowEntity(Key.of("cow"), UUID.randomUUID(), n0);
    assertEquals(n0, se0.name());
    final Component n1 = TextComponent.of("Chicken");
    final HoverEvent.ShowEntity se1 = se0.name(n1);
    assertEquals(n0, se0.name()); // original should be unmodified
    assertEquals(n1, se1.name());
  }

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
        HoverEvent.showEntity(new HoverEvent.ShowEntity(Key.of("cat"), entity)),
        HoverEvent.showEntity(new HoverEvent.ShowEntity(Key.of("cat"), entity, null)),
        HoverEvent.of(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.ShowEntity(Key.of("cat"), entity)),
        HoverEvent.of(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.ShowEntity(Key.of("cat"), entity, null))
      )
      .addEqualityGroup(
        HoverEvent.showEntity(new HoverEvent.ShowEntity(Key.of("cat"), entity, TextComponent.empty())),
        HoverEvent.of(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.ShowEntity(Key.of("cat"), entity, TextComponent.empty()))
      )
      .testEquals();
  }

  @Test
  void assertReadable() {
    for(final HoverEvent.Action<?> action : ImmutableSet.of(
      HoverEvent.Action.SHOW_TEXT,
      HoverEvent.Action.SHOW_ITEM,
      HoverEvent.Action.SHOW_ENTITY
    )) {
      assertTrue(action.readable());
    }
  }
}
