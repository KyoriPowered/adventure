/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageSerializerTest extends TestBase {

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
      .append(Component.text("This is a ", NamedTextColor.RED))
      .append(Component.text("test"));

    this.test(builder, expected);
  }

  @Test
  void testNestedColor() {
    final String expected = "<red>This is a</red><blue>blue </blue><red>test";

    final Builder builder = Component.text()
      .append(Component.text("This is a", NamedTextColor.RED))
      .append(Component.text("blue ", NamedTextColor.BLUE))
      .append(Component.text("test", NamedTextColor.RED));

    this.test(builder, expected);
  }

  @Test
  void testDecoration() {
    // TODO this minimessage string is invalid, prolly need to rewrite the whole parser for it to be fixed...
    final String expected = "<underlined>This is <bold>underlined</underlined></bold>, this isn't";

    final Builder builder = Component.text()
      .append(Component.text("This is ").decoration(TextDecoration.UNDERLINED, true)
        .append(Component.text("underlined").decoration(TextDecoration.BOLD, true)))
      .append(Component.text(", this isn't"));
    this.test(builder, expected);
  }

  @Test
  void testDecorationNegated() {
    final String expected = "<!underlined>Not underlined<!bold>not bold<underlined>underlined</underlined></!bold> not underlined";

    final Builder builder = Component.text()
            .append(Component.text("Not underlined").decoration(TextDecoration.UNDERLINED, false)
                    .append(Component.text("not bold").decoration(TextDecoration.BOLD, false)
                            .append(Component.text("underlined").decoration(TextDecoration.UNDERLINED, true))))
            .append(Component.text(" not underlined").decoration(TextDecoration.UNDERLINED, false));

    this.test(builder, expected);
  }

  @Test
  void testHover() {
    final String expected = "<hover:show_text:\"---\">Some hover</hover> that ends here";

    final Builder builder = Component.text()
      .append(Component.text("Some hover").hoverEvent(HoverEvent.showText(Component.text("---"))))
      .append(Component.text(" that ends here"));

    this.test(builder, expected);
  }

  @Test
  void testParentHover() {
    final String expected = "<hover:show_text:\"<red>---</red><blue><bold>-\">This is a child with hover";

    final Builder builder = Component.text().hoverEvent(HoverEvent.showText(Component.text()
      .content("---").color(NamedTextColor.RED)
      .append(Component.text("-", NamedTextColor.BLUE, TextDecoration.BOLD))
      .build()))
      .append(Component.text("This is a child with hover"));

    this.test(builder, expected);
  }

  @Test
  void testHoverWithNested() {
    final String expected = "<hover:show_text:\"<red>---</red><blue><bold>-\">Some hover</hover> that ends here";

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
      .append(Component.text("Some click").clickEvent(ClickEvent.runCommand("test")))
      .append(Component.text(" that ends here"));

    this.test(builder, expected);
  }

  @Test
  void testContinuedClick() {
    final String expected = "<click:run_command:\"test\">Some click<red> that doesn't end here";

    final Builder builder = Component.text()
      .append(Component.text("Some click").clickEvent(ClickEvent.runCommand("test"))
        .append(Component.text(" that doesn't end here", NamedTextColor.RED)));

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
      .append(Component.keybind("key.jump", NamedTextColor.RED)
        .append(Component.text(" to jump!")));

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
      .append(Component.text("Click "))
      .append(Component.text("this").insertion("test"))
      .append(Component.text(" to insert!"));

    this.test(builder, expected);
  }

  @Test
  void testHexColor() {
    final String expected = "<color:#ff0000>This is a </color:#ff0000>test";

    final Builder builder = Component.text()
      .append(Component.text("This is a ").color(TextColor.fromHexString("#ff0000")))
      .append(Component.text("test"));

    this.test(builder, expected);
  }

  @Test
  void testFont() {
    final String expected = "<font:minecraft:default>This is a </font>test";

    final Builder builder = Component.text()
      .append(Component.text().content("This is a ").font(Key.key("minecraft", "default")))
      .append(Component.text("test"));

    this.test(builder, expected);
  }

  @Test
  void testRainbow() {
    final String expected = "<rainbow>test</rainbow> >> reeeeeeeee";

    final Component parsed = MiniMessage.miniMessage().deserialize(expected);

    final String serialized = MiniMessage.miniMessage().serialize(parsed);
    final Component reparsed = MiniMessage.miniMessage().deserialize(serialized);

    assertEquals(this.prettyPrint(parsed), this.prettyPrint(reparsed));
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
    final Component name = MiniMessage.miniMessage().deserialize(nameString);
    final TextComponent.Builder input = text()
      .content("test")
      .hoverEvent(HoverEvent.showEntity(Key.key("minecraft", "zombie"), uuid, name));
    final String expected = String.format("<hover:show_entity:'minecraft:zombie':%s:\"%s\">test", uuid, nameString);
    this.test(input, expected);
  }

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/172
  @Test
  void testHoverWithExtraFollowing() {
    final Component component = Component.text("START", NamedTextColor.AQUA)
      .append(Component.text("HOVERED", NamedTextColor.BLUE)
        .hoverEvent(HoverEvent.showText(Component.text("Text on hover"))))
      .append(Component.text("END"));
    final String expected = "<aqua>START</aqua><blue><hover:show_text:\"Text on hover\">HOVERED</hover></blue><aqua>END";

    this.test(component, expected);
  }

  @Test
  void testNestedStyles() {
    // These are mostly arbitrary, but I don't want to test every single combination
    final ComponentLike component = Component.text()
      .append(Component.text("b+i+u", Style.style(TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED)))
      .append(Component.text("color+insert", Style.style(NamedTextColor.RED)).insertion("meow"))
      .append(Component.text("st+font", Style.style(TextDecoration.STRIKETHROUGH).font(Key.key("uniform"))))
      .append(Component.text("empty"));
    final String expected = "<bold><italic><underlined>b+i+u</underlined></italic></bold>" +
      "<red><insert:meow>color+insert</insert></red>" +
      "<strikethrough><font:minecraft:uniform>st+font</font></strikethrough>" +
      "empty";

    this.test(component, expected);
  }

  private void test(final @NotNull ComponentLike builder, final @NotNull String expected) {
    final String string = MiniMessageSerializer.serialize(builder.asComponent());
    assertEquals(expected, string);
  }
}
