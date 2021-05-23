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
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;

import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.keybind;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.event.ClickEvent.suggestCommand;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.BLACK;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MiniMessageParserTest {

  private static final MiniMessage PARSER = MiniMessage.builder().strict(true).build();

  @Test
  void test() {
    final Component expected1 = empty().color(YELLOW)
                    .append(text("TEST"))
                    .append(empty().color(GREEN)
                            .append(text(" nested"))
                    ).append(text("Test"));
    final Component expected2 = empty().color(YELLOW)
                    .append(text("TEST"))
                    .append(empty().color(GREEN)
                            .append(text(" nested"))
                            .append(empty().color(YELLOW)
                                    .append(text("Test"))
                            )
                    );

    final String input1 = "<yellow>TEST<green> nested</green>Test";
    final String input2 = "<yellow>TEST<green> nested<yellow>Test";

    assertParsedEquals(expected1, input1);
    assertParsedEquals(expected2, input2);
  }

  @Test
  void testBritish() {
    final String input1 = "<grey>This is english"; // no it's british
    final String input2 = "<gray>This is english";
    final String input3 = "<dark_grey>This is still english"; // British is superior english
    final String input4 = "<dark_gray>This is still english";
    final Component out1 = PARSER.parse(input1);
    final Component out2 = PARSER.parse(input2);
    final Component out3 = PARSER.parse(input3);
    final Component out4 = PARSER.parse(input4);

    assertEquals(out1, out2);
    assertEquals(out3, out4);
  }

  @Test
  void testNewColor() {
    final Component expected1 = empty().color(YELLOW)
                    .append(text("TEST"))
                    .append(empty().color(GREEN)
                            .append(text(" nested"))
                    ).append(text("Test"));
    final Component expected2 = empty().color(YELLOW)
                    .append(text("TEST"))
                    .append(empty().color(GREEN)
                            .append(text(" nested"))
                            .append(empty().color(YELLOW)
                                    .append(text("Test"))
                            )
                    );

    final String input1 = "<color:yellow>TEST<color:green> nested</color:green>Test";
    final String input2 = "<color:yellow>TEST<color:green> nested<color:yellow>Test";

    assertParsedEquals(expected1, input1);
    assertParsedEquals(expected2, input2);
  }

  @Test
  void testHexColor() {
    final Component expected1 = empty().color(color(0xff00ff))
                    .append(text("TEST"))
                    .append(empty().color(color(0x00ff00))
                            .append(text(" nested"))
                    ).append(text("Test"));
    final Component expected2 = empty().color(color(0xff00ff))
                    .append(text("TEST"))
                    .append(empty().color(color(0x00ff00))
                            .append(text(" nested"))
                            .append(empty().color(color(0xff00ff))
                                    .append(text("Test"))
                            )
                    );

    final String input1 = "<color:#ff00ff>TEST<color:#00ff00> nested</color:#00ff00>Test";
    final String input2 = "<color:#ff00ff>TEST<color:#00ff00> nested<color:#ff00ff>Test";

    assertParsedEquals(expected1, input1);
    assertParsedEquals(expected2, input2);
  }

  @Test
  void testHexColorShort() {
    final Component expected1 = empty().color(color(0xff00ff))
                    .append(text("TEST"))
                    .append(empty().color(color(0x00ff00))
                            .append(text(" nested"))
                    ).append(text("Test"));
    final Component expected2 = empty().color(color(0xff00ff))
                    .append(text("TEST"))
                    .append(empty().color(color(0x00ff00))
                            .append(text(" nested"))
                            .append(empty().color(color(0xff00ff))
                                    .append(text("Test"))
                            )
                    );

    final String input1 = "<#ff00ff>TEST<#00ff00> nested</#00ff00>Test";
    final String input2 = "<#ff00ff>TEST<#00ff00> nested<#ff00ff>Test";

    assertParsedEquals(expected1, input1);
    assertParsedEquals(expected2, input2);
  }

  @Test
  void testStripSimple() {
    final String input = "<yellow>TEST<green> nested</green>Test";
    final String expected = "TEST nestedTest";
    assertEquals(expected, PARSER.stripTokens(input));
  }

  @Test
  void testStripComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = " random strangerclick here to FEEL it";
    assertEquals(expected, PARSER.stripTokens(input));
  }

  @Test
  void testStripInner() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "TEST";
    assertEquals(expected, PARSER.stripTokens(input));
  }

  @Test
  void testEscapeSimple() {
    final String input = "<yellow>TEST<green> nested</green>Test";
    final String expected = "\\<yellow>TEST\\<green> nested\\</green>Test";
    assertEquals(expected, PARSER.escapeTokens(input));
  }

  @Test
  void testEscapeComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = "\\<yellow>\\<test> random \\<bold>stranger\\</bold>\\<click:run_command:test command>\\<underlined>\\<red>click here\\</click>\\<blue> to \\<bold>FEEL\\</underlined> it";
    assertEquals(expected, PARSER.escapeTokens(input));
  }

  @Test
  void testEscapeInner() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "\\<hover:show_text:\"\\<red>test:TEST\">TEST";
    assertEquals(expected, PARSER.escapeTokens(input));
  }

  @Test
  void testUnescape() {
    final String input = "<yellow>TEST\\<green> nested\\</green>Test";
    final String expected = "TEST<green> nested</green>Test";
    final Component comp = PARSER.parse(input);

    assertEquals(expected, PlainComponentSerializer.plain().serialize(comp));
  }

  @Test
  @Disabled // TODO, better escape handling
  void testNoUnescape() {
    final String input = "<yellow>TEST\\<green> \\< nested\\</green>Test";
    final String expected = "TEST<green> \\< nested</green>Test";
    final TextComponent comp = (TextComponent) PARSER.parse(input);

    assertEquals(expected, comp.content());
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
    final Component comp = PARSER.parse(input, "test", "Hello!");

    assertEquals(expected, comp);
  }

  @Test
  void testNiceMix() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <b>FEEL</underlined> it";
    final Component expected = empty().color(YELLOW)
            .append(text("Hello! random "))
            .append(empty().decorate(BOLD)
                    .append(text("stranger")))
            .append(empty().clickEvent(runCommand("test command"))
                    .append(empty().decorate(UNDERLINED)
                            .append(empty().color(RED)
                                    .append(text("click here"))
                            )
                    )
            )
            .append(empty().color(BLUE)
                    .append(text(" to "))
                    .append(empty().decorate(BOLD)
                            .append(text("FEEL"))
                            .append(text(" it")
                    )
            ));

    final Component comp = PARSER.parse(input, "test", "Hello!");

    assertEquals(expected, comp);
  }

  @Test
  void testColorSimple() {
    final String input = "<yellow>TEST";

    assertParsedEquals(empty().color(YELLOW).append(text("TEST")), input);
  }

  @Test
  void testHover() {
    final String input = "<hover:show_text:\"<red>test\">TEST";
    final Component expected = empty().hoverEvent(empty().color(RED).append(text("test")))
            .append(text("TEST"));

    assertParsedEquals(expected, input);
  }

  @Test
  void testHover2() {
    final String input = "<hover:show_text:'<red>test'>TEST";
    final Component expected = empty().hoverEvent(empty().color(RED).append(text("test")))
            .append(text("TEST"));

    assertParsedEquals(expected, input);
  }

  @Test
  void testHoverWithColon() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final Component expected = empty().hoverEvent(empty().color(RED).append(text("test:TEST")))
            .append(text("TEST"));

    assertParsedEquals(expected, input);
  }

  @Test
  void testHoverMultiline() {
    final String input = "<hover:show_text:'<red>test\ntest2'>TEST";
    final Component expected = empty().hoverEvent(empty().color(RED).append(text("test\ntest2")))
            .append(text("TEST"));

    assertParsedEquals(expected, input);
  }

  @Test // GH-101
  void testHoverWithInsertingComponent() {
    final String input = "<red><hover:show_text:\"Test\"><lang:item.minecraft.stick>";
    final Component expected = empty().color(RED)
            .append(empty().hoverEvent(showText(text("Test")))
                    .append(translatable("item.minecraft.stick"))
            );

    assertParsedEquals(expected, input);
  }

  @Test
  void testClick() {
    final String input = "<click:run_command:test>TEST";
    final Component expected = empty().clickEvent(runCommand("test")).append(text("TEST"));

    assertParsedEquals(expected, input);
  }

  @Test
  void testClickExtendedCommand() {
    final String input = "<click:run_command:/test command>TEST";
    final Component expected = empty().clickEvent(runCommand("/test command")).append(text("TEST"));

    assertParsedEquals(expected, input);
  }

  @Test
  void testInvalidTag() {
    final String input = "<test>";
    final Component expected = text("<test>");

    assertParsedEquals(expected, input);

    // TODO am not totally happy about this yet, invalid tags arent getting colored for example, but good enough for now
  }

  @Test
  void testInvalidTagComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><oof></oof><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final Component expected = text()
      .append(text("<test>", YELLOW))
      .append(text(" random ", YELLOW))
      .append(text("stranger", style(YELLOW, BOLD)))
      .append(text("<oof>", style(YELLOW, runCommand("test command"))))
      .append(text("</oof>", style(YELLOW, runCommand("test command"))))
      .append(text("click here", style(RED, TextDecoration.UNDERLINED, runCommand("test command"))))
      .append(text(" to ", style(BLUE, TextDecoration.UNDERLINED)))
      .append(text("FEEL", style(BLUE, BOLD, TextDecoration.UNDERLINED)))
      .append(text(" it", style(BLUE, BOLD)))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testKeyBind() {
    final String input = "Press <key:key.jump> to jump!";
    final Component expected = empty()
            .append(text("Press "))
            .append(keybind("key.jump")
                    .append(text(" to jump!"))
            );

    assertParsedEquals(expected, input);
  }

  @Test
  void testKeyBindWithColor() {
    final String input = "Press <red><key:key.jump> to jump!";
    final Component expected = empty()
            .append(text("Press "))
            .append(empty().color(RED)
                    .append(keybind("key.jump")
                            .append(text(" to jump!"))
                    )
            );

    assertParsedEquals(expected, input);
  }

  @Test
  void testTranslatable() {
    final String input = "You should get a <lang:block.minecraft.diamond_block>!";
    final Component expected = empty()
      .append(text("You should get a "))
      .append(translatable("block.minecraft.diamond_block")
              .append(text("!"))
      );

    assertParsedEquals(expected, input);
  }

  @Test
  void testTranslatableWith() {
    final String input = "Test: <lang:commands.drop.success.single:'<red>1':'<blue>Stone'>!";
    final Component expected = empty()
      .append(text("Test: "))
      .append(translatable("commands.drop.success.single",  empty().color(RED).append(text("1")),  empty().color(BLUE).append(text("Stone")))
              .append(text("!"))
      );

    assertParsedEquals(expected, input);
  }

  @Test
  void testTranslatableWithHover() {
    final String input = "Test: <lang:commands.drop.success.single:'<hover:show_text:\\'<red>dum\\'><red>1':'<blue>Stone'>!";
    final Component expected = empty()
      .append(text("Test: "))
      .append(translatable(
        "commands.drop.success.single",
        empty().hoverEvent(showText(empty().color(RED).append(text("dum")))).append(empty().color(RED).append(text("1"))),
        empty().color(BLUE).append(text("Stone"))
      ).
              append(text("!"))
      );

    assertParsedEquals(expected, input);
  }

  @Test
  void testKingAlter() {
    final String input = "Ahoy <lang:offset.-40:'<red>mates!'>";
    final Component expected = empty()
            .append(text("Ahoy "))
            .append(translatable("offset.-40", empty().color(RED).append(text("mates!"))));

    assertParsedEquals(expected, input);
  }

  @Test
  void testInsertion() {
    final String input = "Click <insert:test>this</insert> to insert!";
    final Component expected = empty()
            .append(text("Click "))
            .append(empty().insertion("test")
                    .append(text("this")))
            .append(text(" to insert!"));

    assertParsedEquals(expected, input);
  }

  @Test
  void testGH5() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:<pack_url>><hover:show_text:\"<green>/!\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
            .append(text("»"))
            .append(empty().color(GRAY)
                    .append(text(" To download it from the internet, "))
                    .append(empty().clickEvent(openUrl("https://www.google.com"))
                            .append(empty().hoverEvent(showText(empty().color(GREEN).append(text("/!\\ install it from Options/ResourcePacks in your game"))))
                                    .append(empty().color(GREEN)
                                            .append(empty().decorate(BOLD)
                                                    .append(text("CLICK HERE")))
                                    )
                            )
                    )
            );

    final Component comp1 = PARSER.parse(input, "pack_url", "https://www.google.com");

    assertEquals(expected, comp1);
  }

  @Test
  void testGH5Modified() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:<pack_url>><hover:show_text:'<green>/!\\ install it from \\'Options/ResourcePacks\\' in your game'><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
            .append(text("»"))
            .append(empty().color(GRAY)
                    .append(text(" To download it from the internet, "))
                    .append(empty().clickEvent(openUrl("https://www.google.com"))
                            .append(empty().hoverEvent(showText(empty().color(GREEN).append(text("/!\\ install it from 'Options/ResourcePacks' in your game"))))
                                    .append(empty().color(GREEN)
                                            .append(empty().decorate(BOLD)
                                                    .append(text("CLICK HERE")))
                                    )
                            )
                    )
            );

    // should work
    final Component comp1 = PARSER.parse(input, "pack_url", "https://www.google.com");
    assertEquals(expected, comp1);
  }

  @Test
  void testGH5Quoted() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:\"https://www.google.com\"><hover:show_text:\"<green>/!\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
            .append(text("»"))
            .append(empty().color(GRAY)
                    .append(text(" To download it from the internet, "))
                    .append(empty().clickEvent(openUrl("https://www.google.com"))
                            .append(empty().hoverEvent(showText(empty().color(GREEN).append(text("/!\\ install it from Options/ResourcePacks in your game"))))
                                    .append(empty().color(GREEN)
                                            .append(empty().decorate(BOLD)
                                                    .append(text("CLICK HERE")))
                                    )
                            )
                    )
            );

    // should work
    final Component comp1 = PARSER.parse(input);
    assertEquals(expected, comp1);

    // shouldnt throw an error
    PARSER.parse(input, "url", "https://www.google.com");
  }

  @Test
  void testReset() {
    final String input = "Click <yellow><insert:test>this<rainbow> wooo<reset> to insert!";
    final Component expected = text()
      .append(text("Click "))
      .append(text("this", YELLOW).insertion("test"))
      .append(text(" ", color(0xf3801f)).insertion("test"))
      .append(text("w", color(0x71f813)).insertion("test"))
      .append(text("o", color(0x03ca9c)).insertion("test"))
      .append(text("o", color(0x4135fe)).insertion("test"))
      .append(text("o", color(0xd507b1)).insertion("test"))
      .append(text(" to insert!"))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testPre() {
    final String input = "Click <yellow><pre><insert:test>this</pre> to <red>insert!";
    final Component expected = text()
      .append(text("Click "))
      .append(text("<insert:test>", YELLOW))
      .append(text("this", YELLOW))
      .append(text(" to ", YELLOW))
      .append(text("insert!", RED))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testRainbow() {
    final String input = "<yellow>Woo: <rainbow>||||||||||||||||||||||||</rainbow>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowPhase() {
    final String input = "<yellow>Woo: <rainbow:2>||||||||||||||||||||||||</rainbow>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowWithInsertion() {
    final String input = "<yellow>Woo: <insert:test><rainbow>||||||||||||||||||||||||</rainbow>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
      .append(text("|", color(0xf3801f)).insertion("test"))
      .append(text("|", color(0xe1a00d)).insertion("test"))
      .append(text("|", color(0xc9bf03)).insertion("test"))
      .append(text("|", color(0xacd901)).insertion("test"))
      .append(text("|", color(0x8bed08)).insertion("test"))
      .append(text("|", color(0x6afa16)).insertion("test"))
      .append(text("|", color(0x4bff2c)).insertion("test"))
      .append(text("|", color(0x2ffa48)).insertion("test"))
      .append(text("|", color(0x18ed68)).insertion("test"))
      .append(text("|", color(0x08d989)).insertion("test"))
      .append(text("|", color(0x01bfa9)).insertion("test"))
      .append(text("|", color(0x02a0c7)).insertion("test"))
      .append(text("|", color(0x0c80e0)).insertion("test"))
      .append(text("|", color(0x1e5ff2)).insertion("test"))
      .append(text("|", color(0x3640fc)).insertion("test"))
      .append(text("|", color(0x5326fe)).insertion("test"))
      .append(text("|", color(0x7412f7)).insertion("test"))
      .append(text("|", color(0x9505e9)).insertion("test"))
      .append(text("|", color(0xb401d3)).insertion("test"))
      .append(text("|", color(0xd005b7)).insertion("test"))
      .append(text("|", color(0xe71297)).insertion("test"))
      .append(text("|", color(0xf72676)).insertion("test"))
      .append(text("|", color(0xfe4056)).insertion("test"))
      .append(text("|", color(0xfd5f38)).insertion("test"))
      .append(text("!", YELLOW).insertion("test"))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testGradient() {
    final String input = "<yellow>Woo: <gradient>||||||||||||||||||||||||</gradient>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testGradientWithHover() {
    final String input = "<yellow>Woo: <hover:show_text:'This is a test'><gradient>||||||||||||||||||||||||</gradient>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
      .append(text("|", style(WHITE, showText(text("This is a test")))))
      .append(text("|", style(color(0xf4f4f4), showText(text("This is a test")))))
      .append(text("|", style(color(0xeaeaea), showText(text("This is a test")))))
      .append(text("|", style(color(0xdfdfdf), showText(text("This is a test")))))
      .append(text("|", style(color(0xd5d5d5), showText(text("This is a test")))))
      .append(text("|", style(color(0xcacaca), showText(text("This is a test")))))
      .append(text("|", style(color(0xbfbfbf), showText(text("This is a test")))))
      .append(text("|", style(color(0xb5b5b5), showText(text("This is a test")))))
      .append(text("|", style(GRAY, showText(text("This is a test")))))
      .append(text("|", style(color(0x9f9f9f), showText(text("This is a test")))))
      .append(text("|", style(color(0x959595), showText(text("This is a test")))))
      .append(text("|", style(color(0x8a8a8a), showText(text("This is a test")))))
      .append(text("|", style(color(0x808080), showText(text("This is a test")))))
      .append(text("|", style(color(0x757575), showText(text("This is a test")))))
      .append(text("|", style(color(0x6a6a6a), showText(text("This is a test")))))
      .append(text("|", style(color(0x606060), showText(text("This is a test")))))
      .append(text("|", style(DARK_GRAY, showText(text("This is a test")))))
      .append(text("|", style(color(0x4a4a4a), showText(text("This is a test")))))
      .append(text("|", style(color(0x404040), showText(text("This is a test")))))
      .append(text("|", style(color(0x353535), showText(text("This is a test")))))
      .append(text("|", style(color(0x2a2a2a), showText(text("This is a test")))))
      .append(text("|", style(color(0x202020), showText(text("This is a test")))))
      .append(text("|", style(color(0x151515), showText(text("This is a test")))))
      .append(text("|", style(color(0x0b0b0b), showText(text("This is a test")))))
      .append(text("!", style(YELLOW, showText(text("This is a test")))))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testGradient2() {
    final String input = "<yellow>Woo: <gradient:#5e4fa2:#f79459>||||||||||||||||||||||||</gradient>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testGradient3() {
    final String input = "<yellow>Woo: <gradient:green:blue>||||||||||||||||||||||||</gradient>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor() {
    final String input = "<yellow>Woo: <gradient:red:blue:green:yellow:red>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor2() {
    final String input = "<yellow>Woo: <gradient:black:white:black>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor2Phase() {
    final String input = "<yellow>Woo: <gradient:black:white:black:-0.65>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testGradientPhase() {
    final String input = "<yellow>Woo: <gradient:green:blue:0.7>||||||||||||||||||||||||</gradient>!";
    final Component expected = text()
      .append(text("Woo: ", YELLOW))
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
      .append(text("!", YELLOW))
      .build();

    assertParsedEquals(expected, input);
  }

  @Test
  @Disabled // see #91
  void testGradientWithInnerTokens() {
    final String input = "<gradient:green:blue>123<bold>123</gradient>!";
    final Component expected = text()
            .append(text("1", GREEN))
            .append(text("2", color(0x55e371)))
            .append(text("3", color(0x55c68e)))
            .append(text("1", color(0x55aaaa), BOLD))
            .append(text("2", color(0x558ec6), BOLD))
            .append(text("3", color(0x5571e3), BOLD))
            .build();

    System.out.println(GsonComponentSerializer.gson().serialize(PARSER.parse(input)));

    assertParsedEquals(expected, input);
  }

  @Test
  void testFont() {
    final String input = "Nothing <font:minecraft:uniform>Uniform <font:minecraft:alt>Alt  </font> Uniform";
    final Component expected = empty()
            .append(text("Nothing "))
            .append(empty().style((s) -> s.font(key("uniform")))
                    .append(text("Uniform "))
                    .append(empty().style((s) -> s.font(key("alt")))
                            .append(text("Alt  "))
                    ).append(text(" Uniform"))
            );

    assertParsedEquals(expected, input);
  }

  @Test // GH-37
  void testPhil() {
    final String input = "<red><hover:show_text:'Message 1\nMessage 2'>My Message";
    final Component expected = text("My Message", style(RED, showText(text("Message 1\nMessage 2"))));

    assertParsedEquals(expected, input);
  }

  @Test
  void testNonBmpCharactersInGradient() {
    assertFalse(Character.isBmpCodePoint("𐌰".codePointAt(0)));

    final String input = "Something <gradient:green:blue:1.0>𐌰𐌱𐌲</gradient>";
    final Component expected = text()
            .append(text("Something "))
            .append(text("𐌰", BLUE))
            .append(text("𐌱", color(0x5571e3)))
            .append(text("𐌲", color(0x558ec6)))
            .build();

    assertParsedEquals(expected, input);
  }

  @Test
  void testNonStrict() {
    final String input = "<gray>Example: <click:suggest_command:/plot flag set coral-dry true><gold>/plot flag set coral-dry true<click></gold></gray>";
    final Component expected = text()
      .append(text("Example: ", GRAY))
      .append(text("/plot flag set coral-dry true", style(GOLD, suggestCommand("/plot flag set coral-dry true"))))
      .append(text("<click>", style(GOLD, suggestCommand("/plot flag set coral-dry true"))))
      .build();

    final Component parsed = MiniMessage.builder()
      .strict(false)
      .build()
      .parse(input);
    assertEquals(expected, parsed);
  }

  @Test
  void testNonStrictGH69() {
    final Component expected = text("<");
    final Component comp = MiniMessage.builder()
      .strict(false)
      .build()
      .parse(MiniMessage.get().escapeTokens("<3"));

    assertEquals(expected, comp);
  }

  @Test
  void testGH78() {
    final Component expected = empty().color(GRAY)
            .append(text("\\<"))
            .append(empty().color(YELLOW)
                    .append(text("Patbox"))
                    .append(empty().color(GRAY)
                            .append(text(">"))
                    ));
    // TODO handle pre
    final String input = "<gray>\\<<yellow><player><gray>> <reset><pre><message></pre>";

    assertParsedEquals(expected, input, "player", "Patbox", "message", "am dum");
  }

  @Test
  void testStrictException() {
    final String input = "<gray>Example: <click:suggest_command:/plot flag set coral-dry true><gold>/plot flag set coral-dry true<click></gold></gray>";
    assertThrows(ParseException.class, () -> MiniMessage.builder().strict(true).build().parse(input));
  }

  @Test
  void testMissingCloseOfHover() {
    final String input = "<hover:show_text:'<blue>Hello</blue>'<red>TEST</red></hover><click:suggest_command:'/msg <user>'><user></click> <reset>: <hover:show_text:'<date>'><message></hover>";
    assertThrows(ParseException.class, () -> MiniMessage.builder().strict(true).build().parse(input));
  }

  @Test
  void testNonEndingComponent() {
    final String input = "<red is already created! Try different name! :)";
    MiniMessage.builder().parsingErrorMessageConsumer(strings -> assertEquals(strings, Collections.singletonList("Expected end sometimes after open tag + name, but got name = Token{type=NAME, value=\"red is already created! Try different name! \"} and inners = []"))).build().parse(input);
  }

  @Test
  void testDoubleNewLine() {
    final Component expected = empty().color(RED).append(text("Hello\n\nWorld"));
    final String input = "<red>Hello\n\nWorld";
    assertParsedEquals(expected, input);
  }

  @Test
  void testMismatchedTags() {
    final Component expected = empty().color(GREEN)
            .append(text("hello"))
            .append(text("</red>"));
    final String input = "<green>hello</red>";
    assertParsedEquals(expected, input);
  }

  @Test
  void testShowItemHover() {
    final Component expected = empty().hoverEvent(HoverEvent.showItem(Key.key("minecraft", "stone"), 5))
            .append(text("test"));
    final String input = "<hover:show_item:'minecraft:stone':5>test";
    final String input1 = "<hover:show_item:'minecraft:stone':'5'>test";
    assertParsedEquals(expected, input);
    assertParsedEquals(expected, input1);
  }

  @Test
  void testShowEntityHover() {
    final UUID uuid = UUID.randomUUID();
    final String nameString = "<gold>Custom Name!";
    final Component name = PARSER.parse(nameString);
    final Component expected = empty().hoverEvent(HoverEvent.showEntity(Key.key("minecraft", "zombie"), uuid, name))
            .append(text("test"));
    final String input = String.format("<hover:show_entity:'minecraft:zombie':%s:'%s'>test", uuid, nameString);
    final String input1 = String.format("<hover:show_entity:zombie:'%s':'%s'>test", uuid, nameString);
    assertParsedEquals(expected, input);
    assertParsedEquals(expected, input1);
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
    assertParsedEquals(expected, input);
    assertParsedEquals(expected1, input1);
    assertParsedEquals(expected2, input2);
    assertParsedEquals(expected3, input3);
  }

  @Test
  void testTemplateOrder() {
    final Component expected = text()
            .append(text("ONE", GRAY))
            .append(text("TWO", RED))
            .append(text(" ", RED))
            .append(text("THREE", RED))
            .append(text(" ", RED))
            .append(text("FOUR", RED)).build();
    final String input = "<gray><arg1><red><arg2> <arg3> <arg4>";

    assertParsedEquals(expected, input, "arg1", text("ONE"), "arg2", text("TWO"), "arg3", text("THREE"), "arg4",
            text("FOUR"));
  }

  @Test
  void testTemplateOrder2() {
    final Component expected = text()
            .append(text("ONE", GRAY))
            .append(text("TWO", RED))
            .append(text("THREE", BLUE))
            .append(text(" "))
            .append(text("FOUR", GREEN)).build();
    final String input = "<gray><arg1></gray><red><arg2></red><blue><arg3></blue> <green><arg4>";

    assertParsedEquals(expected, input, "arg1", text("ONE"), "arg2", text("TWO"), "arg3", text("THREE"), "arg4",
            text("FOUR"));
  }

  @Test
  @Disabled // GH-68, GH-93
  void testAngleBracketsShit() {
    final Component expected = empty().color(GREEN)
            .append(text("<"))
            .append(empty().color(YELLOW)
                    .append(text("TEST")
                            .append(empty().color(GRAY)
                                    .append(text("> Woo << double <3"))
                            )
                    )
            );

    final String input = "<gray><<yellow>TEST<gray>> Woooo << double <3";

    System.out.println(GsonComponentSerializer.gson().serialize(MiniMessage.get().parse(input)));

    assertEquals(expected, MiniMessage.get().parse(input));
  }

  private static void assertParsedEquals(final @NonNull Component expected, final @NonNull String input) {
    assertEquals(expected, PARSER.parse(input));
  }

  private static void assertParsedEquals(final @NonNull Component expected, final @NonNull String input, final @NonNull Object... args) {
    assertEquals(expected, PARSER.parse(input, args));
  }
}
