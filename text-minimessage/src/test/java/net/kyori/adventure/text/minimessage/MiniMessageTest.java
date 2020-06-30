package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiniMessageTest {

  @Test
  public void testMarkdownBuilder() {
    Component expected = TextComponent.of("BOLD").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED);
    Component result = MiniMessage.builder().markdown().build().deserialize("**<red>BOLD**");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testNormalBuilder() {
    Component expected = TextComponent.of("Test").color(NamedTextColor.RED);
    Component result = MiniMessage.builder().build().deserialize("<red>Test");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testNormal() {
    Component expected = TextComponent.of("Test").color(NamedTextColor.RED);
    Component result = MiniMessage.instance().deserialize("<red>Test");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testMarkdown() {
    Component expected = TextComponent.of("BOLD").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED);
    Component result = MiniMessage.withMarkDown().deserialize("**<red>BOLD**");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }
}
