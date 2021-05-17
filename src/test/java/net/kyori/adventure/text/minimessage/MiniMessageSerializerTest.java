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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageSerializerTest {

  @Test
  void testColor() {
    final String expected = "<red>This is a test";

    final Builder builder = Component.text().content("This is a test").color(NamedTextColor.RED);

    this.test(builder, expected);
  }

  @Test
  void testColorClosing() {
    final String expected = "<red>This is a </red>test";

    final Builder builder = Component.text()
      .content("This is a ").color(NamedTextColor.RED)
      .append(Component.text("test"));

    this.test(builder, expected);
  }

  @Test
  void testNestedColor() {
    final String expected = "<red>This is a <blue>blue <red>test";

    final Builder builder = Component.text()
      .content("This is a ").color(NamedTextColor.RED)
      .append(Component.text("blue ", NamedTextColor.BLUE))
      .append(Component.text("test", NamedTextColor.RED));

    this.test(builder, expected);
  }

  @Test
  void testDecoration() {
    final String expected = "<underlined>This is <bold>underlined</underlined>, this</bold> isn't";

    final Builder builder = Component.text()
      .content("This is ").decoration(TextDecoration.UNDERLINED, true)
      .append(Component.text(b -> b.content("underlined").decoration(TextDecoration.UNDERLINED, true).decoration(TextDecoration.BOLD, true)))
      .append(Component.text(b -> b.content(", this").decoration(TextDecoration.BOLD, true)))
      .append(Component.text(" isn't"));

    this.test(builder, expected);
  }

  @Test
  void testHover() {
    final String expected = "<hover:show_text:\"---\">Some hover</hover> that ends here";

    final Builder builder = Component.text()
      .content("Some hover").hoverEvent(HoverEvent.showText(Component.text("---")))
      .append(Component.text(" that ends here"));

    this.test(builder, expected);
  }

  @Test
  @Disabled // TODO fix, see GH-92
  void testParentHover() {
    final String expected = "<hover:show_text:\"<red>---<blue><bold>-\">This is a child with hover</hover>";

    final Builder builder = Component.text().hoverEvent(HoverEvent.showText(Component.text()
        .content("---").color(NamedTextColor.RED)
        .append(Component.text("-", NamedTextColor.BLUE, TextDecoration.BOLD))
        .build()))
      .append(Component.text("This is a child with hover"));

    this.test(builder, expected);
  }

  @Test
  void testHoverWithNested() {
    final String expected = "<hover:show_text:\"<red>---<blue><bold>-\">Some hover</hover> that ends here";

    final Builder builder = Component.text()
            .append(Component.text("Some hover").hoverEvent(
                    HoverEvent.showText(Component.text("---").color(NamedTextColor.RED)
                            .append(Component.text("-", NamedTextColor.BLUE, TextDecoration.BOLD)))))
            .append(Component.text(" that ends here"));

    this.test(builder, expected);
  }

  @Test
  void testClick() {
    final String expected = "<click:run_command:\"test\">Some click</click> that ends here";

    final Builder builder = Component.text()
      .content("Some click").clickEvent(ClickEvent.runCommand("test"))
      .append(Component.text(" that ends here"));

    this.test(builder, expected);
  }

  @Test
  void testContinuedClick() {
    final String expected = "<click:run_command:\"test\">Some click<red> that doesn't end here";

    final Builder builder = Component.text()
      .content("Some click").clickEvent(ClickEvent.runCommand("test"))
      // TODO figure out how to avoid repeating the click event here
      .append(Component.text(b -> b.content(" that doesn't end here").color(NamedTextColor.RED).clickEvent(ClickEvent.runCommand("test"))));

    this.test(builder, expected);
  }

  @Test
  void testContinuedClick2() {
    final String expected = "<click:run_command:\"test\">Some click<red> that doesn't end here";

    final Builder builder = Component.text()
      .content("Some click").clickEvent(ClickEvent.runCommand("test"))
      .append(Component.text(b -> b.content(" that doesn't end here").color(NamedTextColor.RED).clickEvent(ClickEvent.runCommand("test"))));

    this.test(builder, expected);
  }

  @Test
  void testKeyBind() {
    final String expected = "Press <key:key.jump> to jump!";

    final Builder builder = Component.text()
      .content("Press ")
      .append(Component.keybind("key.jump"))
      .append(Component.text(" to jump!"));

    this.test(builder, expected);
  }

  @Test
  void testKeyBindWithColor() {
    final String expected = "Press <red><key:key.jump> to jump!";

    final Builder builder = Component.text()
      .content("Press ")
      .append(Component.keybind("key.jump").color(NamedTextColor.RED))
      .append(Component.text(" to jump!", NamedTextColor.RED));

    this.test(builder, expected);
  }

  @Test
  void testTranslatable() {
    final String expected = "You should get a <lang:block.minecraft.diamond_block>!";

    final Builder builder = Component.text()
      .content("You should get a ")
      .append(Component.translatable("block.minecraft.diamond_block"))
      .append(Component.text("!"));

    this.test(builder, expected);
  }

  @Test
  void testTranslatableWithArgs() {
    final String expected = "<lang:some_key:\"<red>:arg\\\" 1\":\"<blue>arg 2\">";

    final Component translatable = Component.translatable()
            .key("some_key")
            .args(text(":arg\" 1", NamedTextColor.RED), text("arg 2", NamedTextColor.BLUE))
            .build();

    this.test(translatable, expected);
  }

  @Test
  void testInsertion() {
    final String expected = "Click <insert:test>this</insert> to insert!";

    final Builder builder = Component.text()
      .content("Click ")
      .append(Component.text(b -> b.content("this").insertion("test")))
      .append(Component.text(" to insert!"));

    this.test(builder, expected);
  }

  @Test
  void testHexColor() {
    final String expected = "<color:#ff0000>This is a </color:#ff0000>test";

    final Builder builder = Component.text()
      .content("This is a ").color(TextColor.fromHexString("#ff0000"))
      .append(Component.text("test"));

    this.test(builder, expected);
  }

  @Test
  void testFont() {
    final String expected = "<font:minecraft:default>This is a </font>test";

    final Builder builder = Component.text()
            .content("This is a ").font(Key.key("minecraft", "default"))
            .append(Component.text("test"));

    this.test(builder, expected);
  }

  @Disabled // TODO make this test actually work
  @Test
  void testRainbow() {
    final String expected = "<rainbow>test</rainbow> >> reeeeeeeee";

    final Component parsed = MiniMessage.get().parse(expected);
    System.out.println(parsed);

    final String serialized = MiniMessage.get().serialize(parsed);
    System.out.println(serialized);
    final Component reparsed = MiniMessage.get().parse(serialized);
    System.out.println(reparsed);

    assertEquals(parsed, reparsed);
  }

  @Test
  void testShowItemHover() {
    final TextComponent.Builder input = text()
            .content("test")
            .hoverEvent(HoverEvent.showItem(Key.key("minecraft", "stone"), 5));
    final String expected = "<hover:show_item:'minecraft:stone':5>test";
    this.test(input, expected);
  }

  @Test
  void testShowEntityHover() {
    final UUID uuid = UUID.randomUUID();
    final String nameString = "<gold>Custom Name!";
    final Component name = MiniMessage.get().parse(nameString);
    final TextComponent.Builder input = text()
            .content("test")
            .hoverEvent(HoverEvent.showEntity(Key.key("minecraft", "zombie"), uuid, name));
    final String expected = String.format("<hover:show_entity:'minecraft:zombie':%s:\"%s\">test", uuid.toString(), nameString);
    this.test(input, expected);
  }

  private void test(final @NonNull ComponentLike builder, final @NonNull String expected) {
    final String string = MiniMessageSerializer.serialize(builder.asComponent());
    assertEquals(expected, string);
  }
}
