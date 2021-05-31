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

import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.keybind;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.BLACK;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MiniMessageParserTest extends TestBase {

  @Test
  void test() {
    final Component expected1 = empty().color(YELLOW)
      .append(text("TEST"))
      .append(text(" nested").color(GREEN))
      .append(text("Test"));
    final Component expected2 = empty().color(YELLOW)
      .append(text("TEST"))
      .append(empty().color(GREEN)
        .append(text(" nested"))
        .append(text("Test").color(YELLOW))
      );

    final String input1 = "<yellow>TEST<green> nested</green>Test";
    final String input2 = "<yellow>TEST<green> nested<yellow>Test";

    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
  }

  @Test
  void testBritish() {
    final String input1 = "<grey>This is english"; // no it's british
    final String input2 = "<gray>This is english";
    final String input3 = "<dark_grey>This is still english"; // British is superior english
    final String input4 = "<dark_gray>This is still english";
    final Component out1 = this.PARSER.parse(input1);
    final Component out2 = this.PARSER.parse(input2);
    final Component out3 = this.PARSER.parse(input3);
    final Component out4 = this.PARSER.parse(input4);

    assertEquals(out1, out2);
    assertEquals(out3, out4);
  }

  @Test
  void testNewColor() {
    final Component expected1 = empty().color(YELLOW)
      .append(text("TEST"))
      .append(text(" nested").color(GREEN))
      .append(text("Test"));
    final Component expected2 = empty().color(YELLOW)
      .append(text("TEST"))
      .append(empty().color(GREEN)
        .append(text(" nested"))
        .append(text("Test").color(YELLOW))
      );

    final String input1 = "<color:yellow>TEST<color:green> nested</color:green>Test";
    final String input2 = "<color:yellow>TEST<color:green> nested<color:yellow>Test";

    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
  }

  @Test
  void testHexColor() {
    final Component expected1 = empty().color(color(0xff00ff))
      .append(text("TEST"))
      .append(text(" nested").color(color(0x00ff00)))
      .append(text("Test"));
    final Component expected2 = empty().color(color(0xff00ff))
      .append(text("TEST"))
      .append(empty().color(color(0x00ff00))
        .append(text(" nested"))
        .append(text("Test").color(color(0xff00ff)))
      );

    final String input1 = "<color:#ff00ff>TEST<color:#00ff00> nested</color:#00ff00>Test";
    final String input2 = "<color:#ff00ff>TEST<color:#00ff00> nested<color:#ff00ff>Test";

    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
  }

  @Test
  void testHexColorShort() {
    final Component expected1 = empty().color(color(0xff00ff))
      .append(text("TEST"))
      .append(text(" nested").color(color(0x00ff00)))
      .append(text("Test"));
    final Component expected2 = empty().color(color(0xff00ff))
      .append(text("TEST"))
      .append(empty().color(color(0x00ff00))
        .append(text(" nested"))
        .append(text("Test").color(color(0xff00ff)))
      );

    final String input1 = "<#ff00ff>TEST<#00ff00> nested</#00ff00>Test";
    final String input2 = "<#ff00ff>TEST<#00ff00> nested<#ff00ff>Test";

    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
  }

  @Test
  void testStripSimple() {
    final String input = "<yellow>TEST<green> nested</green>Test";
    final String expected = "TEST nestedTest";
    assertEquals(expected, this.PARSER.stripTokens(input));
  }

  @Test
  void testStripComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = " random strangerclick here to FEEL it";
    assertEquals(expected, this.PARSER.stripTokens(input));
  }

  @Test
  void testStripInner() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "TEST";
    assertEquals(expected, this.PARSER.stripTokens(input));
  }

  @Test
  void testEscapeSimple() {
    final String input = "<yellow>TEST<green> nested</green>Test";
    final String expected = "\\<yellow>TEST\\<green> nested\\</green>Test";
    assertEquals(expected, this.PARSER.escapeTokens(input));
  }

  @Test
  void testEscapeComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = "\\<yellow>\\<test> random \\<bold>stranger\\</bold>\\<click:run_command:test command>\\<underlined>\\<red>click here\\</click>\\<blue> to \\<bold>FEEL\\</underlined> it";
    assertEquals(expected, this.PARSER.escapeTokens(input));
  }

  @Test
  void testEscapeInner() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "\\<hover:show_text:\"\\<red>test:TEST\">TEST";
    assertEquals(expected, this.PARSER.escapeTokens(input));
  }

  @Test
  void testUnescape() {
    final String input = "<yellow>TEST\\<green> nested\\</green>Test";
    final String expected = "TEST<green> nested</green>Test";
    final Component comp = this.PARSER.parse(input);

    assertEquals(expected, PlainComponentSerializer.plain().serialize(comp));
  }

  @Test
  void testNoUnescape() {
    final String input = "<yellow>TEST\\<green> \\\\< nested\\</green>Test";
    final String expected = "TEST<green> \\< nested</green>Test";
    final TextComponent comp = (TextComponent) this.PARSER.parse(input);

    assertEquals(expected, PlainComponentSerializer.plain().serialize(comp));
  }

  @Test
  void testEscapeParse() {
    final String expected = "<red>test</red>";
    final String escaped = MiniMessage.get().escapeTokens(expected);
    final Component comp = MiniMessage.get().parse(escaped);

    assertEquals(expected, PlainComponentSerializer.plain().serialize(comp));
  }

  @Test
  void checkPlaceholder() {
    final String input = "<test>";
    final Component expected = text("Hello!");
    final Component comp = this.PARSER.parse(input, "test", "Hello!");

    assertEquals(expected, comp);
  }

  @Test
  void testNiceMix() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <b>FEEL</underlined> it";
    final Component expected = empty().color(YELLOW)
      .append(text("Hello!"))
      .append(text(" random "))
      .append(text("stranger").decorate(BOLD))
      .append(text("click here").color(RED).decorate(UNDERLINED).clickEvent(runCommand("test command")))
      .append(empty().color(BLUE)
        .append(text(" to "))
        .append(text("FEEL</underlined> it").decorate(BOLD))
      );

    this.assertParsedEquals(expected, input, "test", "Hello!");
  }

  @Test
  void testColorSimple() {
    final String input = "<yellow>TEST";

    this.assertParsedEquals(text("TEST").color(YELLOW), input);
  }

  @Test
  void testHover() {
    final String input = "<hover:show_text:\"<red>test\">TEST";
    final Component expected = text("TEST").hoverEvent(text("test").color(RED));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testHover2() {
    final String input = "<hover:show_text:'<red>test'>TEST";
    final Component expected = text("TEST").hoverEvent(text("test").color(RED));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testHoverWithColon() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final Component expected = text("TEST").hoverEvent(text("test:TEST").color(RED));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testHoverMultiline() {
    final String input = "<hover:show_text:'<red>test\ntest2'>TEST";
    final Component expected = text("TEST").hoverEvent(text("test\ntest2").color(RED));

    this.assertParsedEquals(expected, input);
  }

  // GH-101
  @Test
  void testHoverWithInsertingComponent() {
    final String input = "<red><hover:show_text:\"Test\"><lang:item.minecraft.stick>";
    final Component expected = translatable("item.minecraft.stick").hoverEvent(showText(text("Test"))).color(RED);

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testClick() {
    final String input = "<click:run_command:test>TEST";
    final Component expected = text("TEST").clickEvent(runCommand("test"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testClickExtendedCommand() {
    final String input = "<click:run_command:/test command>TEST";
    final Component expected = text("TEST").clickEvent(runCommand("/test command"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testInvalidTag() {
    final String input = "<red><test>";
    final Component expected = text("<test>").color(RED);

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testInvalidTagComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><oof></oof><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final Component expected = empty().color(YELLOW)
      .append(text("<test> random "))
      .append(text("stranger").decorate(BOLD))
      .append(empty().clickEvent(runCommand("test command"))
        .append(text("<oof></oof>"))
        .append(text("click here").color(RED).decorate(UNDERLINED))
      ).append(empty().color(BLUE)
        .append(text(" to "))
        .append(text("FEEL</underlined> it").decorate(BOLD))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testKeyBind() {
    final String input = "Press <key:key.jump> to jump!";
    final Component expected = text("Press ")
      .append(keybind("key.jump"))
      .append(text(" to jump!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testKeyBindWithColor() {
    final String input = "Press <red><key:key.jump> to jump!";
    final Component expected = text("Press ")
      .append(empty().color(RED)
        .append(keybind("key.jump"))
        .append(text(" to jump!"))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testTranslatable() {
    final String input = "You should get a <lang:block.minecraft.diamond_block>!";
    final Component expected = text("You should get a ")
      .append(translatable("block.minecraft.diamond_block"))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testTranslatableWith() {
    final String input = "Test: <lang:commands.drop.success.single:'<red>1':'<blue>Stone'>!";
    final Component expected = text("Test: ")
      .append(translatable("commands.drop.success.single", text("1").color(RED), text("Stone").color(BLUE)))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testTranslatableWithHover() {
    final String input = "Test: <lang:commands.drop.success.single:'<hover:show_text:\\'<red>dum\\'><red>1':'<blue>Stone'>!";
    final Component expected = text("Test: ")
      .append(translatable(
        "commands.drop.success.single",
        text("1").color(RED).hoverEvent(showText(text("dum").color(RED))),
        text("Stone").color(BLUE)
      ))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testKingAlter() {
    final String input = "Ahoy <lang:offset.-40:'<red>mates!'>";
    final Component expected = text("Ahoy ")
      .append(translatable("offset.-40", text("mates!").color(RED)));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testInsertion() {
    final String input = "Click <insert:test>this</insert> to insert!";
    final Component expected = text("Click ")
      .append(text("this").insertion("test"))
      .append(text(" to insert!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testBackSpace() {
    final String input = "\\\\!/ IMPORTANT \\\\!/";
    final Component expected = text("\\!/ IMPORTANT \\!/");

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGH5() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:'<pack_url>'><hover:show_text:\"<green>/!\\\\\\\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
      .append(text("»"))
      .append(empty().color(GRAY)
        .append(text(" To download it from the internet, "))
        .append(text("CLICK HERE").decorate(BOLD).color(GREEN).clickEvent(openUrl("https://www.google.com")).hoverEvent(showText(text("/!\\ install it from Options/ResourcePacks in your game").color(GREEN))))
      );

    this.assertParsedEquals(expected, input, "pack_url", "https://www.google.com");
  }

  @Test
  void testGH5Modified() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:'<pack_url>'><hover:show_text:'<green>/!\\\\\\\\ install it from \\'Options/ResourcePacks\\' in your game'><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
      .append(text("»"))
      .append(empty().color(GRAY)
        .append(text(" To download it from the internet, "))
        .append(text("CLICK HERE").decorate(BOLD).color(GREEN).hoverEvent(showText(text("/!\\ install it from 'Options/ResourcePacks' in your game").color(GREEN))).clickEvent(openUrl("https://www.google.com")))
      );

    // should work
    this.assertParsedEquals(expected, input, "pack_url", "https://www.google.com");
  }

  @Test
  void testGH5Quoted() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:\"https://www.google.com\"><hover:show_text:\"<green>/!\\\\\\\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
      .append(text("»"))
      .append(empty().color(GRAY)
        .append(text(" To download it from the internet, "))
        .append(text("CLICK HERE").decorate(BOLD).color(GREEN).clickEvent(openUrl("https://www.google.com")).hoverEvent(showText(text("/!\\ install it from Options/ResourcePacks in your game").color(GREEN))))
      );

    // should work
    this.assertParsedEquals(expected, input);

    // shouldnt throw an error
    this.PARSER.parse(input, "url", "https://www.google.com");
  }

  @Test
  void testReset() {
    final String input = "Click <yellow><insert:test>this<rainbow> wooo<reset> to insert!";
    final Component expected = text("Click ")
      .append(empty().color(YELLOW).insertion("test")
        .append(text("this"))
        .append(empty()
          .append(text(" ", color(0xf3801f)))
          .append(text("w", color(0x71f813)))
          .append(text("o", color(0x03ca9c)))
          .append(text("o", color(0x4135fe)))
          .append(text("o", color(0xd507b1)))
        )
      ).append(text(" to insert!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testPre() {
    final String input = "Click <yellow><pre><insert:test>this</pre> to <red>insert!";
    final Component expected = text("Click ")
      .append(empty().color(YELLOW)
        .append(text("<insert:test>this"))
        .append(text(" to "))
        .append(text("insert!").color(RED))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbow() {
    final String input = "<yellow>Woo: <rainbow>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0xf3801f)))
        .append(text("|", color(0xe1a00d)))
        .append(text("|", color(0xc9bf03)))
        .append(text("|", color(0xacd901)))
        .append(text("|", color(0x8bed08)))
        .append(text("|", color(0x6afa16)))
        .append(text("|", color(0x4bff2c)))
        .append(text("|", color(0x2ffa48)))
        .append(text("|", color(0x18ed68)))
        .append(text("|", color(0x08d989)))
        .append(text("|", color(0x01bfa9)))
        .append(text("|", color(0x02a0c7)))
        .append(text("|", color(0x0c80e0)))
        .append(text("|", color(0x1e5ff2)))
        .append(text("|", color(0x3640fc)))
        .append(text("|", color(0x5326fe)))
        .append(text("|", color(0x7412f7)))
        .append(text("|", color(0x9505e9)))
        .append(text("|", color(0xb401d3)))
        .append(text("|", color(0xd005b7)))
        .append(text("|", color(0xe71297)))
        .append(text("|", color(0xf72676)))
        .append(text("|", color(0xfe4056)))
        .append(text("|", color(0xfd5f38)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowPhase() {
    final String input = "<yellow>Woo: <rainbow:2>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0x1ff35c)))
        .append(text("|", color(0x0de17d)))
        .append(text("|", color(0x03c99e)))
        .append(text("|", color(0x01acbd)))
        .append(text("|", color(0x088bd7)))
        .append(text("|", color(0x166aec)))
        .append(text("|", color(0x2c4bf9)))
        .append(text("|", color(0x482ffe)))
        .append(text("|", color(0x6818fb)))
        .append(text("|", color(0x8908ef)))
        .append(text("|", color(0xa901db)))
        .append(text("|", color(0xc702c1)))
        .append(text("|", color(0xe00ca3)))
        .append(text("|", color(0xf21e82)))
        .append(text("|", color(0xfc3661)))
        .append(text("|", color(0xfe5342)))
        .append(text("|", color(0xf77428)))
        .append(text("|", color(0xe99513)))
        .append(text("|", color(0xd3b406)))
        .append(text("|", color(0xb7d001)))
        .append(text("|", color(0x97e704)))
        .append(text("|", color(0x76f710)))
        .append(text("|", color(0x56fe24)))
        .append(text("|", color(0x38fd3e)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowWithInsertion() {
    final String input = "<yellow>Woo: <insert:test><rainbow>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty().insertion("test")
        .append(empty()
          .append(text("|", color(0xf3801f)))
          .append(text("|", color(0xe1a00d)))
          .append(text("|", color(0xc9bf03)))
          .append(text("|", color(0xacd901)))
          .append(text("|", color(0x8bed08)))
          .append(text("|", color(0x6afa16)))
          .append(text("|", color(0x4bff2c)))
          .append(text("|", color(0x2ffa48)))
          .append(text("|", color(0x18ed68)))
          .append(text("|", color(0x08d989)))
          .append(text("|", color(0x01bfa9)))
          .append(text("|", color(0x02a0c7)))
          .append(text("|", color(0x0c80e0)))
          .append(text("|", color(0x1e5ff2)))
          .append(text("|", color(0x3640fc)))
          .append(text("|", color(0x5326fe)))
          .append(text("|", color(0x7412f7)))
          .append(text("|", color(0x9505e9)))
          .append(text("|", color(0xb401d3)))
          .append(text("|", color(0xd005b7)))
          .append(text("|", color(0xe71297)))
          .append(text("|", color(0xf72676)))
          .append(text("|", color(0xfe4056)))
          .append(text("|", color(0xfd5f38)))
        ).append(text("!"))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradient() {
    final String input = "<yellow>Woo: <gradient>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", WHITE))
        .append(text("|", color(0xf4f4f4)))
        .append(text("|", color(0xeaeaea)))
        .append(text("|", color(0xdfdfdf)))
        .append(text("|", color(0xd5d5d5)))
        .append(text("|", color(0xcacaca)))
        .append(text("|", color(0xbfbfbf)))
        .append(text("|", color(0xb5b5b5)))
        .append(text("|", GRAY))
        .append(text("|", color(0x9f9f9f)))
        .append(text("|", color(0x959595)))
        .append(text("|", color(0x8a8a8a)))
        .append(text("|", color(0x808080)))
        .append(text("|", color(0x757575)))
        .append(text("|", color(0x6a6a6a)))
        .append(text("|", color(0x606060)))
        .append(text("|", DARK_GRAY))
        .append(text("|", color(0x4a4a4a)))
        .append(text("|", color(0x404040)))
        .append(text("|", color(0x353535)))
        .append(text("|", color(0x2a2a2a)))
        .append(text("|", color(0x202020)))
        .append(text("|", color(0x151515)))
        .append(text("|", color(0x0b0b0b)))
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientWithHover() {
    final String input = "<yellow>Woo: <hover:show_text:'This is a test'><gradient>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty().hoverEvent(showText(text("This is a test")))
        .append(empty()
          .append(text("|", style(WHITE)))
          .append(text("|", style(color(0xf4f4f4))))
          .append(text("|", style(color(0xeaeaea))))
          .append(text("|", style(color(0xdfdfdf))))
          .append(text("|", style(color(0xd5d5d5))))
          .append(text("|", style(color(0xcacaca))))
          .append(text("|", style(color(0xbfbfbf))))
          .append(text("|", style(color(0xb5b5b5))))
          .append(text("|", style(GRAY)))
          .append(text("|", style(color(0x9f9f9f))))
          .append(text("|", style(color(0x959595))))
          .append(text("|", style(color(0x8a8a8a))))
          .append(text("|", style(color(0x808080))))
          .append(text("|", style(color(0x757575))))
          .append(text("|", style(color(0x6a6a6a))))
          .append(text("|", style(color(0x606060))))
          .append(text("|", style(DARK_GRAY)))
          .append(text("|", style(color(0x4a4a4a))))
          .append(text("|", style(color(0x404040))))
          .append(text("|", style(color(0x353535))))
          .append(text("|", style(color(0x2a2a2a))))
          .append(text("|", style(color(0x202020))))
          .append(text("|", style(color(0x151515))))
          .append(text("|", style(color(0x0b0b0b))))
        ).append(text("!")));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradient2() {
    final String input = "<yellow>Woo: <gradient:#5e4fa2:#f79459>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0x5e4fa2)))
        .append(text("|", color(0x64529f)))
        .append(text("|", color(0x6b559c)))
        .append(text("|", color(0x715899)))
        .append(text("|", color(0x785b96)))
        .append(text("|", color(0x7e5d93)))
        .append(text("|", color(0x846090)))
        .append(text("|", color(0x8b638d)))
        .append(text("|", color(0x91668a)))
        .append(text("|", color(0x976987)))
        .append(text("|", color(0x9e6c84)))
        .append(text("|", color(0xa46f81)))
        .append(text("|", color(0xab727e)))
        .append(text("|", color(0xb1747a)))
        .append(text("|", color(0xb77777)))
        .append(text("|", color(0xbe7a74)))
        .append(text("|", color(0xc47d71)))
        .append(text("|", color(0xca806e)))
        .append(text("|", color(0xd1836b)))
        .append(text("|", color(0xd78668)))
        .append(text("|", color(0xde8965)))
        .append(text("|", color(0xe48b62)))
        .append(text("|", color(0xea8e5f)))
        .append(text("|", color(0xf1915c)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradient3() {
    final String input = "<yellow>Woo: <gradient:green:blue>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", GREEN))
        .append(text("|", color(0x55f85c)))
        .append(text("|", color(0x55f163)))
        .append(text("|", color(0x55ea6a)))
        .append(text("|", color(0x55e371)))
        .append(text("|", color(0x55dc78)))
        .append(text("|", color(0x55d580)))
        .append(text("|", color(0x55cd87)))
        .append(text("|", color(0x55c68e)))
        .append(text("|", color(0x55bf95)))
        .append(text("|", color(0x55b89c)))
        .append(text("|", color(0x55b1a3)))
        .append(text("|", color(0x55aaaa)))
        .append(text("|", color(0x55a3b1)))
        .append(text("|", color(0x559cb8)))
        .append(text("|", color(0x5595bf)))
        .append(text("|", color(0x558ec6)))
        .append(text("|", color(0x5587cd)))
        .append(text("|", color(0x5580d5)))
        .append(text("|", color(0x5578dc)))
        .append(text("|", color(0x5571e3)))
        .append(text("|", color(0x556aea)))
        .append(text("|", color(0x5563f1)))
        .append(text("|", color(0x555cf8)))
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor() {
    final String input = "<yellow>Woo: <gradient:red:blue:green:yellow:red>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", RED))
        .append(text("|", color(0xf25562)))
        .append(text("|", color(0xe5556f)))
        .append(text("|", color(0xd8557c)))
        .append(text("|", color(0xcb5589)))
        .append(text("|", color(0xbe5596)))
        .append(text("|", color(0xb155a3)))
        .append(text("|", color(0xa355b1)))
        .append(text("|", color(0x9655be)))
        .append(text("|", color(0x8955cb)))
        .append(text("|", color(0x7c55d8)))
        .append(text("|", color(0x6f55e5)))
        .append(text("|", color(0x6255f2)))
        .append(text("|", BLUE))
        .append(text("|", BLUE))
        .append(text("|", color(0x5562f2)))
        .append(text("|", color(0x556fe5)))
        .append(text("|", color(0x557cd8)))
        .append(text("|", color(0x5589cb)))
        .append(text("|", color(0x5596be)))
        .append(text("|", color(0x55a3b1)))
        .append(text("|", color(0x55b1a3)))
        .append(text("|", color(0x55be96)))
        .append(text("|", color(0x55cb89)))
        .append(text("|", color(0x55d87c)))
        .append(text("|", color(0x55e56f)))
        .append(text("|", color(0x55f262)))
        .append(text("|", GREEN))
        .append(text("|", GREEN))
        .append(text("|", color(0x62ff55)))
        .append(text("|", color(0x6fff55)))
        .append(text("|", color(0x7cff55)))
        .append(text("|", color(0x89ff55)))
        .append(text("|", color(0x96ff55)))
        .append(text("|", color(0xa3ff55)))
        .append(text("|", color(0xb1ff55)))
        .append(text("|", color(0xbeff55)))
        .append(text("|", color(0xcbff55)))
        .append(text("|", color(0xd8ff55)))
        .append(text("|", color(0xe5ff55)))
        .append(text("|", color(0xf2ff55)))
        .append(text("|", YELLOW))
        .append(text("|", YELLOW))
        .append(text("|", color(0xfff255)))
        .append(text("|", color(0xffe555)))
        .append(text("|", color(0xffd855)))
        .append(text("|", color(0xffcb55)))
        .append(text("|", color(0xffbe55)))
        .append(text("|", color(0xffb155)))
        .append(text("|", color(0xffa355)))
        .append(text("|", color(0xff9655)))
        .append(text("|", color(0xff8955)))
        .append(text("|", color(0xff7c55)))
        .append(text("|", color(0xff6f55)))
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor2() {
    final String input = "<yellow>Woo: <gradient:black:white:black>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", BLACK))
        .append(text("|", color(0x90909)))
        .append(text("|", color(0x131313)))
        .append(text("|", color(0x1c1c1c)))
        .append(text("|", color(0x262626)))
        .append(text("|", color(0x2f2f2f)))
        .append(text("|", color(0x393939)))
        .append(text("|", color(0x424242)))
        .append(text("|", color(0x4c4c4c)))
        .append(text("|", DARK_GRAY))
        .append(text("|", color(0x5e5e5e)))
        .append(text("|", color(0x686868)))
        .append(text("|", color(0x717171)))
        .append(text("|", color(0x7b7b7b)))
        .append(text("|", color(0x848484)))
        .append(text("|", color(0x8e8e8e)))
        .append(text("|", color(0x979797)))
        .append(text("|", color(0xa1a1a1)))
        .append(text("|", GRAY))
        .append(text("|", color(0xb3b3b3)))
        .append(text("|", color(0xbdbdbd)))
        .append(text("|", color(0xc6c6c6)))
        .append(text("|", color(0xd0d0d0)))
        .append(text("|", color(0xd9d9d9)))
        .append(text("|", color(0xe3e3e3)))
        .append(text("|", color(0xececec)))
        .append(text("|", color(0xf6f6f6)))
        .append(text("|", WHITE))
        .append(text("|", WHITE))
        .append(text("|", color(0xf6f6f6)))
        .append(text("|", color(0xececec)))
        .append(text("|", color(0xe3e3e3)))
        .append(text("|", color(0xd9d9d9)))
        .append(text("|", color(0xd0d0d0)))
        .append(text("|", color(0xc6c6c6)))
        .append(text("|", color(0xbdbdbd)))
        .append(text("|", color(0xb3b3b3)))
        .append(text("|", GRAY))
        .append(text("|", color(0xa1a1a1)))
        .append(text("|", color(0x979797)))
        .append(text("|", color(0x8e8e8e)))
        .append(text("|", color(0x848484)))
        .append(text("|", color(0x7b7b7b)))
        .append(text("|", color(0x717171)))
        .append(text("|", color(0x686868)))
        .append(text("|", color(0x5e5e5e)))
        .append(text("|", DARK_GRAY))
        .append(text("|", color(0x4c4c4c)))
        .append(text("|", color(0x424242)))
        .append(text("|", color(0x393939)))
        .append(text("|", color(0x2f2f2f)))
        .append(text("|", color(0x262626)))
        .append(text("|", color(0x1c1c1c)))
        .append(text("|", color(0x131313)))
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor2Phase() {
    final String input = "<yellow>Woo: <gradient:black:white:black:-0.65>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0xa6a6a6)))
        .append(text("|", color(0x9c9c9c)))
        .append(text("|", color(0x939393)))
        .append(text("|", color(0x898989)))
        .append(text("|", color(0x808080)))
        .append(text("|", color(0x777777)))
        .append(text("|", color(0x6d6d6d)))
        .append(text("|", color(0x646464)))
        .append(text("|", color(0x5a5a5a)))
        .append(text("|", color(0x515151)))
        .append(text("|", color(0x474747)))
        .append(text("|", color(0x3e3e3e)))
        .append(text("|", color(0x343434)))
        .append(text("|", color(0x2b2b2b)))
        .append(text("|", color(0x222222)))
        .append(text("|", color(0x181818)))
        .append(text("|", color(0xf0f0f)))
        .append(text("|", color(0x50505)))
        .append(text("|", color(0x40404)))
        .append(text("|", color(0xe0e0e)))
        .append(text("|", color(0x171717)))
        .append(text("|", color(0x212121)))
        .append(text("|", color(0x2a2a2a)))
        .append(text("|", color(0x333333)))
        .append(text("|", color(0x3d3d3d)))
        .append(text("|", color(0x464646)))
        .append(text("|", color(0x505050)))
        .append(text("|", color(0x595959)))
        .append(text("|", color(0x595959)))
        .append(text("|", color(0x636363)))
        .append(text("|", color(0x6c6c6c)))
        .append(text("|", color(0x767676)))
        .append(text("|", color(0x7f7f7f)))
        .append(text("|", color(0x888888)))
        .append(text("|", color(0x929292)))
        .append(text("|", color(0x9b9b9b)))
        .append(text("|", color(0xa5a5a5)))
        .append(text("|", color(0xaeaeae)))
        .append(text("|", color(0xb8b8b8)))
        .append(text("|", color(0xc1c1c1)))
        .append(text("|", color(0xcbcbcb)))
        .append(text("|", color(0xd4d4d4)))
        .append(text("|", color(0xdddddd)))
        .append(text("|", color(0xe7e7e7)))
        .append(text("|", color(0xf0f0f0)))
        .append(text("|", color(0xfafafa)))
        .append(text("|", color(0xfbfbfb)))
        .append(text("|", color(0xf1f1f1)))
        .append(text("|", color(0xe8e8e8)))
        .append(text("|", color(0xdedede)))
        .append(text("|", color(0xd5d5d5)))
        .append(text("|", color(0xcccccc)))
        .append(text("|", color(0xc2c2c2)))
        .append(text("|", color(0xb9b9b9)))
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientPhase() {
    final String input = "<yellow>Woo: <gradient:green:blue:0.7>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0x5588cc)))
        .append(text("|", color(0x5581d3)))
        .append(text("|", color(0x557ada)))
        .append(text("|", color(0x5573e1)))
        .append(text("|", color(0x556ce8)))
        .append(text("|", color(0x5565ef)))
        .append(text("|", color(0x555ef7)))
        .append(text("|", color(0x5556fe)))
        .append(text("|", color(0x555bf9)))
        .append(text("|", color(0x5562f2)))
        .append(text("|", color(0x5569eb)))
        .append(text("|", color(0x5570e4)))
        .append(text("|", color(0x5577dd)))
        .append(text("|", color(0x557ed6)))
        .append(text("|", color(0x5585cf)))
        .append(text("|", color(0x558cc8)))
        .append(text("|", color(0x5593c1)))
        .append(text("|", color(0x559aba)))
        .append(text("|", color(0x55a2b3)))
        .append(text("|", color(0x55a9ab)))
        .append(text("|", color(0x55b0a4)))
        .append(text("|", color(0x55b79d)))
        .append(text("|", color(0x55be96)))
        .append(text("|", color(0x55c58f)))
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  // see #91
  @Test
  void testGradientWithInnerTokens() {
    final String input = "<gradient:green:blue>123<bold>456</gradient>!";
    final Component expected = empty()
      .append(text("1", GREEN))
      .append(text("2", color(0x55e371)))
      .append(text("3", color(0x55c68e)))
      .append(empty().decorate(BOLD)
        .append(text("4", color(0x55aaaa)))
        .append(text("5", color(0x558ec6)))
        .append(text("6", color(0x5571e3)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientWithInnerGradientWithInnerToken() {
    final String input = "<gradient:green:blue>123<gradient:red:yellow>456<bold>789</gradient>abc</gradient>!";
    final Component expected = empty()
      .append(text("1", GREEN))
      .append(text("2", color(0x55f163)))
      .append(text("3", color(0x55e371)))
      .append(empty()
        .append(text("4", RED))
        .append(text("5", color(0xff7155)))
        .append(text("6", color(0xff8e55)))
        .append(empty().decorate(BOLD)
          .append(text("7", color(0xffaa55)))
          .append(text("8", color(0xffc655)))
          .append(text("9", color(0xffe355)))
        )
      )
      .append(empty()
        .append(text("a", color(0x5580d5)))
        .append(text("b", color(0x5571e3)))
        .append(text("c", color(0x5563f1)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowWithInnerClick() {
    final String input = "Rainbow: <rainbow><click:open_url:'https://github.com'>GH</click></rainbow>";
    final Component expected = text("Rainbow: ")
      .append(empty().clickEvent(openUrl("https://github.com"))
        .append(text("G").color(color(0xf3801f)))
        .append(text("H").color(color(0x0c80e0)))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testFont() {
    final String input = "Nothing <font:minecraft:uniform>Uniform <font:minecraft:alt>Alt  </font> Uniform";
    final Component expected = text("Nothing ")
      .append(empty().style(s -> s.font(key("uniform")))
        .append(text("Uniform "))
        .append(text("Alt  ").style(s -> s.font(key("alt"))))
        .append(text(" Uniform"))
      );

    this.assertParsedEquals(expected, input);
  }

  // GH-37
  @Test
  void testPhil() {
    final String input = "<red><hover:show_text:'Message 1\nMessage 2'>My Message";
    final Component expected = text("My Message").hoverEvent(showText(text("Message 1\nMessage 2"))).color(RED);

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testNonBmpCharactersInGradient() {
    assertFalse(Character.isBmpCodePoint("𐌰".codePointAt(0)));

    final String input = "Something <gradient:green:blue:1.0>𐌰𐌱𐌲</gradient>";
    final Component expected = text("Something ")
      .append(empty()
        .append(text("𐌰", BLUE))
        .append(text("𐌱", color(0x558ec6)))
        .append(text("𐌲", color(0x55c68e)))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGH78() {
    final Component expected = empty()
      .append(empty().color(GRAY)
        .append(text("<"))
        .append(empty().color(YELLOW)
          .append(text("Patbox"))
          .append(text("> ").color(GRAY))
        )
      ).append(text("<message>"));
    final String input = "<gray>\\<<yellow><player><gray>> <reset><pre><message></pre>";

    this.assertParsedEquals(expected, input, "player", "Patbox", "message", "am dum");
  }

  @Test
  void testDoubleNewLine() {
    final Component expected = text("Hello\n\nWorld").color(RED);
    final String input = "<red>Hello\n\nWorld";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testMismatchedTags() {
    final Component expected = text("hello</red>").color(GREEN);
    final String input = "<green>hello</red>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testShowItemHover() {
    final Component expected = text("test").hoverEvent(HoverEvent.showItem(Key.key("minecraft", "stone"), 5));
    final String input = "<hover:show_item:'minecraft:stone':5>test";
    final String input1 = "<hover:show_item:'minecraft:stone':'5'>test";
    this.assertParsedEquals(expected, input);
    this.assertParsedEquals(expected, input1);
  }

  @Test
  void testShowEntityHover() {
    final UUID uuid = UUID.randomUUID();
    final String nameString = "<gold>Custom Name!";
    final Component name = this.PARSER.parse(nameString);
    final Component expected = text("test").hoverEvent(HoverEvent.showEntity(Key.key("minecraft", "zombie"), uuid, name));
    final String input = String.format("<hover:show_entity:'minecraft:zombie':%s:'%s'>test", uuid, nameString);
    final String input1 = String.format("<hover:show_entity:zombie:'%s':'%s'>test", uuid, nameString);
    this.assertParsedEquals(expected, input);
    this.assertParsedEquals(expected, input1);
  }

  @Test
  void testQuoteEscapingInArguments() {
    final Component expected = translatable("test", text("\"\""));
    final Component expected1 = translatable("test", text("''"));
    final Component expected2 = translatable("test", text("''"));
    final Component expected3 = translatable("test", text("\"\""));
    final String input = "<lang:test:'\"\"'>";
    final String input1 = "<lang:test:'\\'\\''>";
    final String input2 = "<lang:test:\"''\">";
    final String input3 = "<lang:test:\"\\\"\\\"\">";
    this.assertParsedEquals(expected, input);
    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
    this.assertParsedEquals(expected3, input3);
  }

  @Test
  void testTemplateOrder() {
    final Component expected = empty().color(GRAY)
      .append(text("ONE"))
      .append(empty().color(RED)
        .append(text("TWO"))
        .append(text(" "))
        .append(text("THREE"))
        .append(text(" "))
        .append(text("FOUR"))
      );
    final String input = "<gray><arg1><red><arg2> <arg3> <arg4>";

    this.assertParsedEquals(expected, input, "arg1", text("ONE"), "arg2", text("TWO"), "arg3", text("THREE"), "arg4",
      text("FOUR"));
  }

  @Test
  void testTemplateOrder2() {
    final Component expected = empty()
      .append(text("ONE").color(GRAY))
      .append(text("TWO").color(RED))
      .append(text("THREE").color(BLUE))
      .append(text(" "))
      .append(text("FOUR").color(GREEN));
    final String input = "<gray><arg1></gray><red><arg2></red><blue><arg3></blue> <green><arg4>";

    this.assertParsedEquals(expected, input, "arg1", text("ONE"), "arg2", text("TWO"), "arg3", text("THREE"), "arg4",
      text("FOUR"));
  }

  // GH-68, GH-93
  @Test
  void testAngleBracketsShit() {
    final Component expected = empty().color(GRAY)
      .append(text("<"))
      .append(empty().color(YELLOW)
        .append(text("TEST"))
        .append(text("> Woo << double <3").color(GRAY))
      );

    final String input = "<gray><<yellow>TEST<gray>> Woo << double <3";

    this.assertParsedEquals(expected, input);
  }

  // GH-111
  @Test
  void testNoSwallowSpace() {
    final Component expected = empty().color(RED).hoverEvent(showText(text("Test")))
      .append(text(" "))
      .append(translatable("item.minecraft.stick"));
    final String input = "<red><hover:show_text:\"Test\"> <lang:item.minecraft.stick>";

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testEscape() {
    final String input = "\\a";
    final Component expected = text("a");

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testBareEscape() {
    final String input = "\\";
    final Component expected = empty();

    this.assertParsedEquals(expected, input);
  }
}
