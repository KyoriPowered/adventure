/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import com.google.common.collect.ImmutableSet;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.serializer.ComponentSerializers;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static net.kyori.text.Tests.with;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ComponentTest {
  @Test
  void testCreate() {
    with(TextComponent.of("foo"), component -> {
      assertEquals("foo", component.content());
      assertNull(component.color());
      assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.BOLD));
    });
    with(TextComponent.of("foo", TextColor.GREEN), component -> {
      assertEquals("foo", component.content());
      assertEquals(TextColor.GREEN, component.color());
      assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.BOLD));
    });
    with(TextComponent.of("foo", TextColor.GREEN, ImmutableSet.of(TextDecoration.BOLD)), component -> {
      assertEquals("foo", component.content());
      assertEquals(TextColor.GREEN, component.color());
      assertEquals(TextDecoration.State.TRUE, component.decoration(TextDecoration.BOLD));
      assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.ITALIC));
    });
  }

  @Test
  void testCopy() {
    final TextComponent.Builder component = TextComponent.builder().content("").color(TextColor.GRAY);
    component.append(TextComponent.builder().content("This is a test").color(TextColor.DARK_PURPLE).build());
    component.append(TextComponent.builder().content(" ").build());
    component.append(TextComponent.builder().content("A what?").color(TextColor.DARK_AQUA).clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/what")).build());
    assertEquals(component.build(), component.build().copy());
  }

  @Test
  void testDecorations() {
    TextComponent component = TextComponent.builder().content("Kittens!").build();

    // The bold decoration should not be set at this point.
    assertFalse(component.hasDecoration(TextDecoration.BOLD));
    assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.BOLD));

    component = component.decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);

    final Set<TextDecoration> decorations = component.decorations();

    // The bold decoration should be set and true at this point.
    assertTrue(component.hasDecoration(TextDecoration.BOLD));
    assertEquals(TextDecoration.State.TRUE, component.decoration(TextDecoration.BOLD));
    assertEquals(component.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE, decorations.contains(TextDecoration.BOLD));
    assertTrue(decorations.contains(TextDecoration.BOLD));
    assertFalse(decorations.contains(TextDecoration.OBFUSCATED));
  }

  @Test
  void testStyleReset() {
    Component component = TextComponent.builder()
      .content("kittens")
      .build();
    assertFalse(component.hasStyling());
    component = component.decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);
    assertTrue(component.hasStyling());
    component = component.resetStyle();
    assertFalse(component.hasStyling());
  }

  @Test
  void testContains() {
    final Component child = TextComponent.builder().content("kittens").build();
    final Component component = TextComponent.builder()
      .content("cat")
      .append(child)
      .build();
    assertTrue(component.contains(child));
  }

  @Test
  void testCycleSelf() {
    assertThrows(IllegalStateException.class, () -> {
      final Component component = TextComponent.builder().content("cat").build();
      component.append(component);
      fail("A component was added to itself");
    });
  }

  @Test
  void testCycleHoverRoot() {
    assertThrows(IllegalStateException.class, () -> {
      final Component hoverComponent = TextComponent.builder().content("hover").build();
      final Component component = TextComponent.builder()
        .content("cat")
        .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent))
        .build();
      // component's hover event value is hoverComponent, we should not be able to add it
      hoverComponent.append(component);
      fail("A component was added to itself");
    });
  }

  @Test
  void testCycleHoverChild() {
    assertThrows(IllegalStateException.class, () -> {
      final Component hoverComponent = TextComponent.builder().content("hover child").build();
      final Component component = TextComponent.builder().content("cat")
        .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.builder().content("hover").build().append(hoverComponent)))
        .build();
      // component's hover event value contains hoverComponent, we should not be able to add it
      hoverComponent.append(component);
      fail("A component was added to itself");
    });
  }

  @Test
  void testSerializeDeserialize() {
    final TextComponent expected = TextComponent.builder()
      .content("Hello!")
      .color(TextColor.DARK_PURPLE)
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://google.com/"))
      .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.builder().content(":o").color(TextColor.DARK_AQUA).build()))
      .build();
    final String json = ComponentSerializers.JSON.serialize(expected);
    assertEquals(expected, ComponentSerializers.JSON.deserialize(json));
  }

  @Test
  void assertOpenFileNotReadable() {
    final ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_FILE, "fake");
    assertFalse(event.action().readable());
  }

  @Test
  void testCopyHover() {
    final HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.builder().content("Kittens!").build());
    final HoverEvent copy = event.copy();
    assertEquals(event, copy);
  }

  @Test
  void testSerializeTranslatable() {
    final TranslatableComponent component = TranslatableComponent.of(
      "multiplayer.player.left",
      TextComponent.builder("kashike")
        .clickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg kashike "))
        .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, TextComponent.of("{\"name\":\"kashike\",\"id\":\"eb121687-8b1a-4944-bd4d-e0a818d9dfe2\"}")))
      .build()
    ).color(TextColor.YELLOW);
    assertEquals(component, ComponentSerializers.JSON.deserialize(ComponentSerializers.JSON.serialize(component)));
  }
}
