/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.serializer.ComponentSerializer;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ComponentTest {

  @Test
  public void testCopy() {
    final TextComponent.Builder component = TextComponent.builder().content("").color(TextColor.GRAY);
    component.append(TextComponent.builder().content("This is a test").color(TextColor.DARK_PURPLE).build());
    component.append(TextComponent.builder().content(" ").build());
    component.append(TextComponent.builder().content("A what?").color(TextColor.DARK_AQUA).clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/what")).build());
    assertEquals(component.build(), component.build().copy());
  }

  @Test
  public void testDecorations() {
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
  public void testStyleReset() {
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
  public void testContains() {
    final Component child = TextComponent.builder().content("kittens").build();
    final Component component = TextComponent.builder()
      .content("cat")
      .append(child)
      .build();
    assertTrue(component.contains(child));
  }

  @Test(expected = IllegalStateException.class)
  public void testCycleSelf() {
    final Component component = TextComponent.builder().content("cat").build();
    component.append(component);
    fail("A component was added to itself");
  }

  @Test(expected = IllegalStateException.class)
  public void testCycleHoverRoot() {
    final Component hoverComponent = TextComponent.builder().content("hover").build();
    final Component component = TextComponent.builder()
      .content("cat")
      .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent))
      .build();
    // component's hover event value is hoverComponent, we should not be able to add it
    hoverComponent.append(component);
    fail("A component was added to itself");
  }

  @Test(expected = IllegalStateException.class)
  public void testCycleHoverChild() {
    final Component hoverComponent = TextComponent.builder().content("hover child").build();
    final Component component = TextComponent.builder().content("cat")
      .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.builder().content("hover").build().append(hoverComponent)))
      .build();
    // component's hover event value contains hoverComponent, we should not be able to add it
    hoverComponent.append(component);
    fail("A component was added to itself");
  }

  @Test
  public void testSerializeDeserialize() {
    final TextComponent expected = TextComponent.builder()
      .content("Hello!")
      .color(TextColor.DARK_PURPLE)
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://google.com/"))
      .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.builder().content(":o").color(TextColor.DARK_AQUA).build()))
      .build();
    final String json = ComponentSerializer.serialize(expected);
    assertEquals(expected, ComponentSerializer.deserialize(json));
  }

  @Test
  public void assertOpenFileNotReadable() {
    final ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_FILE, "fake");
    assertFalse(event.action().isReadable());
  }

  @Test
  public void testCopyHover() {
    final HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.builder().content("Kittens!").build());
    final HoverEvent copy = event.copy();
    assertEquals(event, copy);
  }
}
