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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MiniMessageParserTest {

  private final MiniMessage parser = MiniMessage.builder().strict(true).build();

  @Test
  void test() {
    final String input1 = "<yellow>TEST<green> nested</green>Test";
    final String input2 = "<yellow>TEST<green> nested<yellow>Test";
    final String out1 = GsonComponentSerializer.gson().serialize(this.parser.parse(input1));
    final String out2 = GsonComponentSerializer.gson().serialize(this.parser.parse(input2));

    assertEquals(out1, out2);
  }

  @Test
  void testNewColor() {
    final String input1 = "<color:yellow>TEST<color:green> nested</color:green>Test";
    final String input2 = "<color:yellow>TEST<color:green> nested<color:yellow>Test";
    final String out1 = GsonComponentSerializer.gson().serialize(this.parser.parse(input1));
    final String out2 = GsonComponentSerializer.gson().serialize(this.parser.parse(input2));

    assertEquals(out1, out2);
  }

  @Test
  void testHexColor() {
    final String input1 = "<color:#ff00ff>TEST<color:#00ff00> nested</color:#00ff00>Test";
    final String input2 = "<color:#ff00ff>TEST<color:#00ff00> nested<color:#ff00ff>Test";
    final String out1 = GsonComponentSerializer.gson().serialize(this.parser.parse(input1));
    final String out2 = GsonComponentSerializer.gson().serialize(this.parser.parse(input2));

    assertEquals(out1, out2);
  }

  @Test
  void testHexColorShort() {
    final String input1 = "<#ff00ff>TEST<#00ff00> nested</#00ff00>Test";
    final String input2 = "<#ff00ff>TEST<#00ff00> nested<#ff00ff>Test";
    final String out1 = GsonComponentSerializer.gson().serialize(this.parser.parse(input1));
    final String out2 = GsonComponentSerializer.gson().serialize(this.parser.parse(input2));

    assertEquals(out1, out2);
  }

  @Test
  void testStripSimple() {
    final String input = "<yellow>TEST<green> nested</green>Test";
    final String expected = "TEST nestedTest";
    assertEquals(expected, this.parser.stripTokens(input));
  }

  @Test
  void testStripComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = " random strangerclick here to FEEL it";
    assertEquals(expected, this.parser.stripTokens(input));
  }

  @Test
  void testStripInner() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "TEST";
    assertEquals(expected, this.parser.stripTokens(input));
  }

  @Test
  void testEscapeSimple() {
    final String input = "<yellow>TEST<green> nested</green>Test";
    final String expected = "\\<yellow>TEST\\<green> nested\\</green>Test";
    assertEquals(expected, this.parser.escapeTokens(input));
  }

  @Test
  void testEscapeComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = "\\<yellow>\\<test> random \\<bold>stranger\\</bold>\\<click:run_command:test command>\\<underlined>\\<red>click here\\</click>\\<blue> to \\<bold>FEEL\\</underlined> it";
    assertEquals(expected, this.parser.escapeTokens(input));
  }

  @Test
  void testEscapeInner() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "\\<hover:show_text:\"\\<red>test:TEST\">TEST";
    assertEquals(expected, this.parser.escapeTokens(input));
  }

  @Test
  void testUnescape() {
    final String input ="<yellow>TEST\\<green> nested\\</green>Test";
    final String expected = "TEST<green> nested</green>Test";
    final TextComponent comp = (TextComponent) this.parser.parse(input);

    assertEquals(expected, PlainComponentSerializer.plain().serialize(comp));
  }

  @Test
  @Disabled // TODO, better escape handling
  void testNoUnescape() {
    final String input ="<yellow>TEST\\<green> \\< nested\\</green>Test";
    final String expected = "TEST<green> \\< nested</green>Test";
    final TextComponent comp = (TextComponent) this.parser.parse(input);

    assertEquals(expected, PlainComponentSerializer.plain().serialize(comp));
  }

  @Test
  void testEscapeParse() {
    final String expected = "<red>test</red>";
    final String escaped = MiniMessage.get().escapeTokens(expected);
    final TextComponent comp = (TextComponent) MiniMessage.get().parse(escaped);

    assertEquals(expected, PlainComponentSerializer.plain().serialize(comp));
  }

  @Test
  void checkPlaceholder() {
    final String input = "<test>";
    final String expected = "{\"text\":\"Hello!\"}";
    final Component comp = this.parser.parse(input, "test", "Hello!");

    this.test(comp, expected);
  }

  @Test
  void testNiceMix() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <b>FEEL</underlined> it";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Hello! random \",\"color\":\"yellow\"},{\"text\":\"stranger\",\"color\":\"yellow\",\"bold\":true},{\"text\":\"click here\",\"color\":\"red\",\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\" to \",\"color\":\"blue\",\"underlined\":true},{\"text\":\"FEEL\",\"color\":\"blue\",\"bold\":true,\"underlined\":true},{\"text\":\" it\",\"color\":\"blue\",\"bold\":true}]}";
    final Component comp = this.parser.parse(input, "test", "Hello!");

    this.test(comp, expected);
  }

  @Test
  void testColorSimple() {
    final String input = "<yellow>TEST";
    final String expected = "{\"text\":\"TEST\",\"color\":\"yellow\"}";

    this.test(input, expected);
  }

  @Test
  void testColorNested() {
    final String input = "<yellow>TEST<green>nested</green>Test";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"TEST\",\"color\":\"yellow\"},{\"text\":\"nested\",\"color\":\"green\"},{\"text\":\"Test\",\"color\":\"yellow\"}]}";

    this.test(input, expected);
  }

  @Test
  void testColorNotNested() {
    final String input = "<yellow>TEST</yellow><green>nested</green>Test";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"TEST\",\"color\":\"yellow\"},{\"text\":\"nested\",\"color\":\"green\"},{\"text\":\"Test\"}]}";

    this.test(input, expected);
  }

  @Test
  void testHover() {
    final String input = "<hover:show_text:\"<red>test\">TEST";
    final String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"test\",\"color\":\"red\"}}}";

    this.test(input, expected);
  }

  @Test
  void testHover2() {
    final String input = "<hover:show_text:'<red>test'>TEST";
    final String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"test\",\"color\":\"red\"}}}";

    this.test(input, expected);
  }

  @Test
  void testHoverWithColon() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"test:TEST\",\"color\":\"red\"}}}";

    this.test(input, expected);
  }

  @Test
  void testHoverMultiline() {
    final String input = "<hover:show_text:'<red>test\ntest2'>TEST";
    final String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"test\\ntest2\",\"color\":\"red\"}}}";

    this.test(input, expected);
  }

  @Test
  void testClick() {
    final String input = "<click:run_command:test>TEST";
    final String expected = "{\"text\":\"TEST\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test\"}}";

    this.test(input, expected);
  }

  @Test
  void testClickExtendedCommand() {
    final String input = "<click:run_command:/test command>TEST";
    final String expected = "{\"text\":\"TEST\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/test command\"}}";

    this.test(input, expected);
  }

  @Test
  void testInvalidTag() {
    final String input = "<test>";
    final String expected = "{\"text\":\"\\u003ctest\\u003e\"}"; // gson makes it html save
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);

    // TODO am not totally happy about this yet, invalid tags arent getting colored for example, but good enough for now
  }

  @Test
  void testInvalidTagComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><oof></oof><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"\\u003ctest\\u003e\",\"color\":\"yellow\"},{\"text\":\" random \",\"color\":\"yellow\"},{\"text\":\"stranger\",\"color\":\"yellow\",\"bold\":true},{\"text\":\"\\u003coof\\u003e\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\"\\u003c/oof\\u003e\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\"click here\",\"color\":\"red\",\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\" to \",\"color\":\"blue\",\"underlined\":true},{\"text\":\"FEEL\",\"color\":\"blue\",\"bold\":true,\"underlined\":true},{\"text\":\" it\",\"color\":\"blue\",\"bold\":true}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testKeyBind() {
    final String input = "Press <key:key.jump> to jump!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Press \"},{\"keybind\":\"key.jump\"},{\"text\":\" to jump!\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testKeyBindWithColor() {
    final String input = "Press <red><key:key.jump> to jump!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Press \"},{\"keybind\":\"key.jump\",\"color\":\"red\"},{\"text\":\" to jump!\",\"color\":\"red\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testTranslatable() {
    final String input = "You should get a <lang:block.minecraft.diamond_block>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"You should get a \"},{\"translate\":\"block.minecraft.diamond_block\"},{\"text\":\"!\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testTranslatableWith() {
    final String input = "Test: <lang:commands.drop.success.single:'<red>1':'<blue>Stone'>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Test: \"},{\"translate\":\"commands.drop.success.single\",\"with\":[{\"text\":\"1\",\"color\":\"red\"},{\"text\":\"Stone\",\"color\":\"blue\"}]},{\"text\":\"!\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testTranslatableWithHover() {
    final String input = "Test: <lang:commands.drop.success.single:'<hover:show_text:\\'<red>dum\\'><red>1':'<blue>Stone'>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Test: \"},{\"translate\":\"commands.drop.success.single\",\"with\":[{\"text\":\"1\",\"color\":\"red\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"dum\",\"color\":\"red\"}}},{\"text\":\"Stone\",\"color\":\"blue\"}]},{\"text\":\"!\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testKingAlter() {
    final String input = "Ahoy <lang:offset.-40:'<red>mates!'>";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Ahoy \"},{\"translate\":\"offset.-40\",\"with\":[{\"text\":\"mates!\",\"color\":\"red\"}]}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testInsertion() {
    final String input = "Click <insert:test>this</insert> to insert!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Click \"},{\"text\":\"this\",\"insertion\":\"test\"},{\"text\":\" to insert!\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGH5() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:<pack_url>><hover:show_text:\"<green>/!\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"»\",\"color\":\"dark_gray\"},{\"text\":\" To download it from the internet, \",\"color\":\"gray\"},{\"text\":\"CLICK HERE\",\"color\":\"green\",\"bold\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"/!\\\\ install it from Options/ResourcePacks in your game\",\"color\":\"green\"}}}]}";

    final Component comp1 = this.parser.parse(input, "pack_url", "https://www.google.com");
    this.test(comp1, expected);
  }

  @Test
  void testGH5Modified() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:<pack_url>><hover:show_text:'<green>/!\\ install it from \\'Options/ResourcePacks\\' in your game'><green><bold>CLICK HERE</bold></hover></click>";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"»\",\"color\":\"dark_gray\"},{\"text\":\" To download it from the internet, \",\"color\":\"gray\"},{\"text\":\"CLICK HERE\",\"color\":\"green\",\"bold\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"/!\\\\ install it from \\u0027Options/ResourcePacks\\u0027 in your game\",\"color\":\"green\"}}}]}";

    // should work
    final Component comp1 = this.parser.parse(input, "pack_url", "https://www.google.com");
    this.test(comp1, expected);
  }

  @Test
  void testGH5Quoted() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:\"https://www.google.com\"><hover:show_text:\"<green>/!\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"»\",\"color\":\"dark_gray\"},{\"text\":\" To download it from the internet, \",\"color\":\"gray\"},{\"text\":\"CLICK HERE\",\"color\":\"green\",\"bold\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"/!\\\\ install it from Options/ResourcePacks in your game\",\"color\":\"green\"}}}]}";

    // should work
    final Component comp1 = this.parser.parse(input);
    this.test(comp1, expected);

    // shouldnt throw an error
    this.parser.parse(input, "url", "https://www.google.com");
  }

  @Test
  void testReset() {
    final String input = "Click <yellow><insert:test>this<rainbow> wooo<reset> to insert!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Click \"},{\"text\":\"this\",\"color\":\"yellow\",\"insertion\":\"test\"},{\"text\":\" \",\"color\":\"#f3801f\",\"insertion\":\"test\"},{\"text\":\"w\",\"color\":\"#71f813\",\"insertion\":\"test\"},{\"text\":\"o\",\"color\":\"#03ca9c\",\"insertion\":\"test\"},{\"text\":\"o\",\"color\":\"#4135fe\",\"insertion\":\"test\"},{\"text\":\"o\",\"color\":\"#d507b1\",\"insertion\":\"test\"},{\"text\":\" to insert!\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testPre() {
    final String input = "Click <yellow><pre><insert:test>this</pre> to <red>insert!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Click \"},{\"text\":\"\\u003cinsert:test\\u003e\",\"color\":\"yellow\"},{\"text\":\"this\",\"color\":\"yellow\"},{\"text\":\" to \",\"color\":\"yellow\"},{\"text\":\"insert!\",\"color\":\"red\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testRainbow() {
    final String input = "<yellow>Woo: <rainbow>||||||||||||||||||||||||</rainbow>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"#f3801f\"},{\"text\":\"|\",\"color\":\"#e1a00d\"},{\"text\":\"|\",\"color\":\"#c9bf03\"},{\"text\":\"|\",\"color\":\"#acd901\"},{\"text\":\"|\",\"color\":\"#8bed08\"},{\"text\":\"|\",\"color\":\"#6afa16\"},{\"text\":\"|\",\"color\":\"#4bff2c\"},{\"text\":\"|\",\"color\":\"#2ffa48\"},{\"text\":\"|\",\"color\":\"#18ed68\"},{\"text\":\"|\",\"color\":\"#08d989\"},{\"text\":\"|\",\"color\":\"#01bfa9\"},{\"text\":\"|\",\"color\":\"#02a0c7\"},{\"text\":\"|\",\"color\":\"#0c80e0\"},{\"text\":\"|\",\"color\":\"#1e5ff2\"},{\"text\":\"|\",\"color\":\"#3640fc\"},{\"text\":\"|\",\"color\":\"#5326fe\"},{\"text\":\"|\",\"color\":\"#7412f7\"},{\"text\":\"|\",\"color\":\"#9505e9\"},{\"text\":\"|\",\"color\":\"#b401d3\"},{\"text\":\"|\",\"color\":\"#d005b7\"},{\"text\":\"|\",\"color\":\"#e71297\"},{\"text\":\"|\",\"color\":\"#f72676\"},{\"text\":\"|\",\"color\":\"#fe4056\"},{\"text\":\"|\",\"color\":\"#fd5f38\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testRainbowPhase() {
    final String input = "<yellow>Woo: <rainbow:2>||||||||||||||||||||||||</rainbow>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"#1ff35c\"},{\"text\":\"|\",\"color\":\"#0de17d\"},{\"text\":\"|\",\"color\":\"#03c99e\"},{\"text\":\"|\",\"color\":\"#01acbd\"},{\"text\":\"|\",\"color\":\"#088bd7\"},{\"text\":\"|\",\"color\":\"#166aec\"},{\"text\":\"|\",\"color\":\"#2c4bf9\"},{\"text\":\"|\",\"color\":\"#482ffe\"},{\"text\":\"|\",\"color\":\"#6818fb\"},{\"text\":\"|\",\"color\":\"#8908ef\"},{\"text\":\"|\",\"color\":\"#a901db\"},{\"text\":\"|\",\"color\":\"#c702c1\"},{\"text\":\"|\",\"color\":\"#e00ca3\"},{\"text\":\"|\",\"color\":\"#f21e82\"},{\"text\":\"|\",\"color\":\"#fc3661\"},{\"text\":\"|\",\"color\":\"#fe5342\"},{\"text\":\"|\",\"color\":\"#f77428\"},{\"text\":\"|\",\"color\":\"#e99513\"},{\"text\":\"|\",\"color\":\"#d3b406\"},{\"text\":\"|\",\"color\":\"#b7d001\"},{\"text\":\"|\",\"color\":\"#97e704\"},{\"text\":\"|\",\"color\":\"#76f710\"},{\"text\":\"|\",\"color\":\"#56fe24\"},{\"text\":\"|\",\"color\":\"#38fd3e\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testRainbowWithInsertion() {
    final String input = "<yellow>Woo: <insert:test><rainbow>||||||||||||||||||||||||</rainbow>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"#f3801f\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#e1a00d\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#c9bf03\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#acd901\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#8bed08\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#6afa16\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#4bff2c\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#2ffa48\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#18ed68\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#08d989\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#01bfa9\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#02a0c7\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#0c80e0\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#1e5ff2\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#3640fc\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#5326fe\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#7412f7\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#9505e9\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#b401d3\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#d005b7\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#e71297\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#f72676\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#fe4056\",\"insertion\":\"test\"},{\"text\":\"|\",\"color\":\"#fd5f38\",\"insertion\":\"test\"},{\"text\":\"!\",\"color\":\"yellow\",\"insertion\":\"test\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGradient() {
    final String input = "<yellow>Woo: <gradient>||||||||||||||||||||||||</gradient>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"white\"},{\"text\":\"|\",\"color\":\"#f4f4f4\"},{\"text\":\"|\",\"color\":\"#eaeaea\"},{\"text\":\"|\",\"color\":\"#dfdfdf\"},{\"text\":\"|\",\"color\":\"#d5d5d5\"},{\"text\":\"|\",\"color\":\"#cacaca\"},{\"text\":\"|\",\"color\":\"#bfbfbf\"},{\"text\":\"|\",\"color\":\"#b5b5b5\"},{\"text\":\"|\",\"color\":\"gray\"},{\"text\":\"|\",\"color\":\"#9f9f9f\"},{\"text\":\"|\",\"color\":\"#959595\"},{\"text\":\"|\",\"color\":\"#8a8a8a\"},{\"text\":\"|\",\"color\":\"#808080\"},{\"text\":\"|\",\"color\":\"#757575\"},{\"text\":\"|\",\"color\":\"#6a6a6a\"},{\"text\":\"|\",\"color\":\"#606060\"},{\"text\":\"|\",\"color\":\"dark_gray\"},{\"text\":\"|\",\"color\":\"#4a4a4a\"},{\"text\":\"|\",\"color\":\"#404040\"},{\"text\":\"|\",\"color\":\"#353535\"},{\"text\":\"|\",\"color\":\"#2a2a2a\"},{\"text\":\"|\",\"color\":\"#202020\"},{\"text\":\"|\",\"color\":\"#151515\"},{\"text\":\"|\",\"color\":\"#0b0b0b\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGradientWithHover() {
    final String input = "<yellow>Woo: <hover:show_text:'This is a test'><gradient>||||||||||||||||||||||||</gradient>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"white\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#f4f4f4\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#eaeaea\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#dfdfdf\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#d5d5d5\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#cacaca\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#bfbfbf\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#b5b5b5\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"gray\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#9f9f9f\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#959595\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#8a8a8a\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#808080\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#757575\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#6a6a6a\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#606060\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"dark_gray\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#4a4a4a\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#404040\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#353535\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#2a2a2a\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#202020\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#151515\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"|\",\"color\":\"#0b0b0b\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}},{\"text\":\"!\",\"color\":\"yellow\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"This is a test\"}}}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGradient2() {
    final String input = "<yellow>Woo: <gradient:#5e4fa2:#f79459>||||||||||||||||||||||||</gradient>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"#5e4fa2\"},{\"text\":\"|\",\"color\":\"#64529f\"},{\"text\":\"|\",\"color\":\"#6b559c\"},{\"text\":\"|\",\"color\":\"#715899\"},{\"text\":\"|\",\"color\":\"#785b96\"},{\"text\":\"|\",\"color\":\"#7e5d93\"},{\"text\":\"|\",\"color\":\"#846090\"},{\"text\":\"|\",\"color\":\"#8b638d\"},{\"text\":\"|\",\"color\":\"#91668a\"},{\"text\":\"|\",\"color\":\"#976987\"},{\"text\":\"|\",\"color\":\"#9e6c84\"},{\"text\":\"|\",\"color\":\"#a46f81\"},{\"text\":\"|\",\"color\":\"#ab727e\"},{\"text\":\"|\",\"color\":\"#b1747a\"},{\"text\":\"|\",\"color\":\"#b77777\"},{\"text\":\"|\",\"color\":\"#be7a74\"},{\"text\":\"|\",\"color\":\"#c47d71\"},{\"text\":\"|\",\"color\":\"#ca806e\"},{\"text\":\"|\",\"color\":\"#d1836b\"},{\"text\":\"|\",\"color\":\"#d78668\"},{\"text\":\"|\",\"color\":\"#de8965\"},{\"text\":\"|\",\"color\":\"#e48b62\"},{\"text\":\"|\",\"color\":\"#ea8e5f\"},{\"text\":\"|\",\"color\":\"#f1915c\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGradient3() {
    final String input = "<yellow>Woo: <gradient:green:blue>||||||||||||||||||||||||</gradient>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"green\"},{\"text\":\"|\",\"color\":\"#55f85c\"},{\"text\":\"|\",\"color\":\"#55f163\"},{\"text\":\"|\",\"color\":\"#55ea6a\"},{\"text\":\"|\",\"color\":\"#55e371\"},{\"text\":\"|\",\"color\":\"#55dc78\"},{\"text\":\"|\",\"color\":\"#55d580\"},{\"text\":\"|\",\"color\":\"#55cd87\"},{\"text\":\"|\",\"color\":\"#55c68e\"},{\"text\":\"|\",\"color\":\"#55bf95\"},{\"text\":\"|\",\"color\":\"#55b89c\"},{\"text\":\"|\",\"color\":\"#55b1a3\"},{\"text\":\"|\",\"color\":\"#55aaaa\"},{\"text\":\"|\",\"color\":\"#55a3b1\"},{\"text\":\"|\",\"color\":\"#559cb8\"},{\"text\":\"|\",\"color\":\"#5595bf\"},{\"text\":\"|\",\"color\":\"#558ec6\"},{\"text\":\"|\",\"color\":\"#5587cd\"},{\"text\":\"|\",\"color\":\"#5580d5\"},{\"text\":\"|\",\"color\":\"#5578dc\"},{\"text\":\"|\",\"color\":\"#5571e3\"},{\"text\":\"|\",\"color\":\"#556aea\"},{\"text\":\"|\",\"color\":\"#5563f1\"},{\"text\":\"|\",\"color\":\"#555cf8\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGradientMultiColor() {
    final String input = "<yellow>Woo: <gradient:red:blue:green:yellow:red>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"red\"},{\"text\":\"|\",\"color\":\"#f25562\"},{\"text\":\"|\",\"color\":\"#e5556f\"},{\"text\":\"|\",\"color\":\"#d8557c\"},{\"text\":\"|\",\"color\":\"#cb5589\"},{\"text\":\"|\",\"color\":\"#be5596\"},{\"text\":\"|\",\"color\":\"#b155a3\"},{\"text\":\"|\",\"color\":\"#a355b1\"},{\"text\":\"|\",\"color\":\"#9655be\"},{\"text\":\"|\",\"color\":\"#8955cb\"},{\"text\":\"|\",\"color\":\"#7c55d8\"},{\"text\":\"|\",\"color\":\"#6f55e5\"},{\"text\":\"|\",\"color\":\"#6255f2\"},{\"text\":\"|\",\"color\":\"blue\"},{\"text\":\"|\",\"color\":\"blue\"},{\"text\":\"|\",\"color\":\"#5562f2\"},{\"text\":\"|\",\"color\":\"#556fe5\"},{\"text\":\"|\",\"color\":\"#557cd8\"},{\"text\":\"|\",\"color\":\"#5589cb\"},{\"text\":\"|\",\"color\":\"#5596be\"},{\"text\":\"|\",\"color\":\"#55a3b1\"},{\"text\":\"|\",\"color\":\"#55b1a3\"},{\"text\":\"|\",\"color\":\"#55be96\"},{\"text\":\"|\",\"color\":\"#55cb89\"},{\"text\":\"|\",\"color\":\"#55d87c\"},{\"text\":\"|\",\"color\":\"#55e56f\"},{\"text\":\"|\",\"color\":\"#55f262\"},{\"text\":\"|\",\"color\":\"green\"},{\"text\":\"|\",\"color\":\"green\"},{\"text\":\"|\",\"color\":\"#62ff55\"},{\"text\":\"|\",\"color\":\"#6fff55\"},{\"text\":\"|\",\"color\":\"#7cff55\"},{\"text\":\"|\",\"color\":\"#89ff55\"},{\"text\":\"|\",\"color\":\"#96ff55\"},{\"text\":\"|\",\"color\":\"#a3ff55\"},{\"text\":\"|\",\"color\":\"#b1ff55\"},{\"text\":\"|\",\"color\":\"#beff55\"},{\"text\":\"|\",\"color\":\"#cbff55\"},{\"text\":\"|\",\"color\":\"#d8ff55\"},{\"text\":\"|\",\"color\":\"#e5ff55\"},{\"text\":\"|\",\"color\":\"#f2ff55\"},{\"text\":\"|\",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"#fff255\"},{\"text\":\"|\",\"color\":\"#ffe555\"},{\"text\":\"|\",\"color\":\"#ffd855\"},{\"text\":\"|\",\"color\":\"#ffcb55\"},{\"text\":\"|\",\"color\":\"#ffbe55\"},{\"text\":\"|\",\"color\":\"#ffb155\"},{\"text\":\"|\",\"color\":\"#ffa355\"},{\"text\":\"|\",\"color\":\"#ff9655\"},{\"text\":\"|\",\"color\":\"#ff8955\"},{\"text\":\"|\",\"color\":\"#ff7c55\"},{\"text\":\"|\",\"color\":\"#ff6f55\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGradientMultiColor2() {
    final String input = "<yellow>Woo: <gradient:black:white:black>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"black\"},{\"text\":\"|\",\"color\":\"#090909\"},{\"text\":\"|\",\"color\":\"#131313\"},{\"text\":\"|\",\"color\":\"#1c1c1c\"},{\"text\":\"|\",\"color\":\"#262626\"},{\"text\":\"|\",\"color\":\"#2f2f2f\"},{\"text\":\"|\",\"color\":\"#393939\"},{\"text\":\"|\",\"color\":\"#424242\"},{\"text\":\"|\",\"color\":\"#4c4c4c\"},{\"text\":\"|\",\"color\":\"dark_gray\"},{\"text\":\"|\",\"color\":\"#5e5e5e\"},{\"text\":\"|\",\"color\":\"#686868\"},{\"text\":\"|\",\"color\":\"#717171\"},{\"text\":\"|\",\"color\":\"#7b7b7b\"},{\"text\":\"|\",\"color\":\"#848484\"},{\"text\":\"|\",\"color\":\"#8e8e8e\"},{\"text\":\"|\",\"color\":\"#979797\"},{\"text\":\"|\",\"color\":\"#a1a1a1\"},{\"text\":\"|\",\"color\":\"gray\"},{\"text\":\"|\",\"color\":\"#b3b3b3\"},{\"text\":\"|\",\"color\":\"#bdbdbd\"},{\"text\":\"|\",\"color\":\"#c6c6c6\"},{\"text\":\"|\",\"color\":\"#d0d0d0\"},{\"text\":\"|\",\"color\":\"#d9d9d9\"},{\"text\":\"|\",\"color\":\"#e3e3e3\"},{\"text\":\"|\",\"color\":\"#ececec\"},{\"text\":\"|\",\"color\":\"#f6f6f6\"},{\"text\":\"|\",\"color\":\"white\"},{\"text\":\"|\",\"color\":\"white\"},{\"text\":\"|\",\"color\":\"#f6f6f6\"},{\"text\":\"|\",\"color\":\"#ececec\"},{\"text\":\"|\",\"color\":\"#e3e3e3\"},{\"text\":\"|\",\"color\":\"#d9d9d9\"},{\"text\":\"|\",\"color\":\"#d0d0d0\"},{\"text\":\"|\",\"color\":\"#c6c6c6\"},{\"text\":\"|\",\"color\":\"#bdbdbd\"},{\"text\":\"|\",\"color\":\"#b3b3b3\"},{\"text\":\"|\",\"color\":\"gray\"},{\"text\":\"|\",\"color\":\"#a1a1a1\"},{\"text\":\"|\",\"color\":\"#979797\"},{\"text\":\"|\",\"color\":\"#8e8e8e\"},{\"text\":\"|\",\"color\":\"#848484\"},{\"text\":\"|\",\"color\":\"#7b7b7b\"},{\"text\":\"|\",\"color\":\"#717171\"},{\"text\":\"|\",\"color\":\"#686868\"},{\"text\":\"|\",\"color\":\"#5e5e5e\"},{\"text\":\"|\",\"color\":\"dark_gray\"},{\"text\":\"|\",\"color\":\"#4c4c4c\"},{\"text\":\"|\",\"color\":\"#424242\"},{\"text\":\"|\",\"color\":\"#393939\"},{\"text\":\"|\",\"color\":\"#2f2f2f\"},{\"text\":\"|\",\"color\":\"#262626\"},{\"text\":\"|\",\"color\":\"#1c1c1c\"},{\"text\":\"|\",\"color\":\"#131313\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGradientMultiColor2Phase() {
    final String input = "<yellow>Woo: <gradient:black:white:black:-0.65>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"#a6a6a6\"},{\"text\":\"|\",\"color\":\"#9c9c9c\"},{\"text\":\"|\",\"color\":\"#939393\"},{\"text\":\"|\",\"color\":\"#898989\"},{\"text\":\"|\",\"color\":\"#808080\"},{\"text\":\"|\",\"color\":\"#777777\"},{\"text\":\"|\",\"color\":\"#6d6d6d\"},{\"text\":\"|\",\"color\":\"#646464\"},{\"text\":\"|\",\"color\":\"#5a5a5a\"},{\"text\":\"|\",\"color\":\"#515151\"},{\"text\":\"|\",\"color\":\"#474747\"},{\"text\":\"|\",\"color\":\"#3e3e3e\"},{\"text\":\"|\",\"color\":\"#343434\"},{\"text\":\"|\",\"color\":\"#2b2b2b\"},{\"text\":\"|\",\"color\":\"#222222\"},{\"text\":\"|\",\"color\":\"#181818\"},{\"text\":\"|\",\"color\":\"#0f0f0f\"},{\"text\":\"|\",\"color\":\"#050505\"},{\"text\":\"|\",\"color\":\"#040404\"},{\"text\":\"|\",\"color\":\"#0e0e0e\"},{\"text\":\"|\",\"color\":\"#171717\"},{\"text\":\"|\",\"color\":\"#212121\"},{\"text\":\"|\",\"color\":\"#2a2a2a\"},{\"text\":\"|\",\"color\":\"#333333\"},{\"text\":\"|\",\"color\":\"#3d3d3d\"},{\"text\":\"|\",\"color\":\"#464646\"},{\"text\":\"|\",\"color\":\"#505050\"},{\"text\":\"|\",\"color\":\"#595959\"},{\"text\":\"|\",\"color\":\"#595959\"},{\"text\":\"|\",\"color\":\"#636363\"},{\"text\":\"|\",\"color\":\"#6c6c6c\"},{\"text\":\"|\",\"color\":\"#767676\"},{\"text\":\"|\",\"color\":\"#7f7f7f\"},{\"text\":\"|\",\"color\":\"#888888\"},{\"text\":\"|\",\"color\":\"#929292\"},{\"text\":\"|\",\"color\":\"#9b9b9b\"},{\"text\":\"|\",\"color\":\"#a5a5a5\"},{\"text\":\"|\",\"color\":\"#aeaeae\"},{\"text\":\"|\",\"color\":\"#b8b8b8\"},{\"text\":\"|\",\"color\":\"#c1c1c1\"},{\"text\":\"|\",\"color\":\"#cbcbcb\"},{\"text\":\"|\",\"color\":\"#d4d4d4\"},{\"text\":\"|\",\"color\":\"#dddddd\"},{\"text\":\"|\",\"color\":\"#e7e7e7\"},{\"text\":\"|\",\"color\":\"#f0f0f0\"},{\"text\":\"|\",\"color\":\"#fafafa\"},{\"text\":\"|\",\"color\":\"#fbfbfb\"},{\"text\":\"|\",\"color\":\"#f1f1f1\"},{\"text\":\"|\",\"color\":\"#e8e8e8\"},{\"text\":\"|\",\"color\":\"#dedede\"},{\"text\":\"|\",\"color\":\"#d5d5d5\"},{\"text\":\"|\",\"color\":\"#cccccc\"},{\"text\":\"|\",\"color\":\"#c2c2c2\"},{\"text\":\"|\",\"color\":\"#b9b9b9\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testGradientPhase() {
    final String input = "<yellow>Woo: <gradient:green:blue:0.7>||||||||||||||||||||||||</gradient>!";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"color\":\"#5588cc\"},{\"text\":\"|\",\"color\":\"#5581d3\"},{\"text\":\"|\",\"color\":\"#557ada\"},{\"text\":\"|\",\"color\":\"#5573e1\"},{\"text\":\"|\",\"color\":\"#556ce8\"},{\"text\":\"|\",\"color\":\"#5565ef\"},{\"text\":\"|\",\"color\":\"#555ef7\"},{\"text\":\"|\",\"color\":\"#5556fe\"},{\"text\":\"|\",\"color\":\"#555bf9\"},{\"text\":\"|\",\"color\":\"#5562f2\"},{\"text\":\"|\",\"color\":\"#5569eb\"},{\"text\":\"|\",\"color\":\"#5570e4\"},{\"text\":\"|\",\"color\":\"#5577dd\"},{\"text\":\"|\",\"color\":\"#557ed6\"},{\"text\":\"|\",\"color\":\"#5585cf\"},{\"text\":\"|\",\"color\":\"#558cc8\"},{\"text\":\"|\",\"color\":\"#5593c1\"},{\"text\":\"|\",\"color\":\"#559aba\"},{\"text\":\"|\",\"color\":\"#55a2b3\"},{\"text\":\"|\",\"color\":\"#55a9ab\"},{\"text\":\"|\",\"color\":\"#55b0a4\"},{\"text\":\"|\",\"color\":\"#55b79d\"},{\"text\":\"|\",\"color\":\"#55be96\"},{\"text\":\"|\",\"color\":\"#55c58f\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testFont() {
    final String input = "Nothing <font:minecraft:uniform>Uniform <font:minecraft:alt>Alt  </font> Uniform";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Nothing \"},{\"text\":\"Uniform \",\"font\":\"minecraft:uniform\"},{\"text\":\"Alt  \",\"font\":\"minecraft:alt\"},{\"text\":\" Uniform\",\"font\":\"minecraft:uniform\"}]}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test // GH-37
  void testPhil() {
    final String input = "<red><hover:show_text:'Message 1\nMessage 2'>My Message";
    final String expected = "{\"text\":\"My Message\",\"color\":\"red\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Message 1\\nMessage 2\"}}}";
    final Component comp = this.parser.parse(input);

    this.test(comp, expected);
  }

  @Test
  void testNonBmpCharactersInGradient() {
    assertFalse(Character.isBmpCodePoint("𐌰".codePointAt(0)));

    final String input = "Something <gradient:green:blue:1.0>𐌰𐌱𐌲</gradient>";
    final Component expected = Component.text()
            .append(Component.text("Something "))
            .append(Component.text("𐌰", NamedTextColor.BLUE))
            .append(Component.text("𐌱", TextColor.color(0x5571e3)))
            .append(Component.text("𐌲", TextColor.color(0x558ec6)))
            .build();

    assertEquals(this.parser.parse(input), expected);
  }

  @Test
  void testNonStrict() {
    final String input = "<gray>Example: <click:suggest_command:/plot flag set coral-dry true><gold>/plot flag set coral-dry true<click></gold></gray>";
    final String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Example: \",\"color\":\"gray\"},{\"text\":\"/plot flag set coral-dry true\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/plot flag set coral-dry true\"}},{\"text\":\"\\u003cclick\\u003e\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/plot flag set coral-dry true\"}}]}";
    final Component comp = MiniMessage.builder().strict(false).build().parse(input);

    this.test(comp, expected);
  }

  @Test
  void testNonStrictGH69() {
    final String expected = "{\"text\":\"\\u003c\"}";
    final Component comp = MiniMessage.builder().strict(false).build().parse(MiniMessage.get().escapeTokens("<3"));

    this.test(comp, expected);
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

  private void test(final @NonNull String input, final @NonNull String expected) {
    this.test(this.parser.parse(input), expected);
  }

  private void test(final @NonNull Component comp, final @NonNull String expected) {
    assertEquals(expected, GsonComponentSerializer.gson().serialize(comp));
  }
}
