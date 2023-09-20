/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.internal.parser.Token;
import net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tree.Node;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows; // DiamondFire

public class MiniMessageParserTest extends AbstractTest {

  @Test
  void testStripSimple() {
    final String input = "<yellow>TEST<green> nested</green>Test";
    final String expected = "TEST nestedTest";
    assertEquals(expected, PARSER.stripTags(input));
  }

  @Test
  void testStripComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = "<test> random strangerclick here to FEEL it";
    assertEquals(expected, PARSER.stripTags(input));
  }

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/169
  @Test
  void testStripComplexInner() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here <please></click><blue> to <bold>FEEL</underlined> it";
    final String expected = "<test> random strangerclick here <please> to FEEL it";
    assertEquals(expected, PARSER.stripTags(input));
  }

  @Test
  void testStripInner() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "TEST";
    assertEquals(expected, PARSER.stripTags(input));
  }

  @Test
  void testStripTags() {
    final String input = "Hello, <red><name>!";
    final String expected = "Hello, !";
    assertEquals(expected, PARSER.stripTags(input, parsed("name", "you")));
  }

  @Test
  void testEscapeSimple() {
    final String input = "<yellow>TEST<green> nested</green>Test";
    final String expected = "\\<yellow>TEST\\<green> nested\\</green>Test";
    assertEquals(expected, PARSER.escapeTags(input));
  }

  @Test
  void testEscapeComplex() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
    final String expected = "\\<yellow><test> random \\<bold>stranger\\</bold>\\<click:run_command:test command>\\<underlined>\\<red>click here\\</click>\\<blue> to \\<bold>FEEL\\</underlined> it";
    assertEquals(expected, PARSER.escapeTags(input));
  }

  @Test
  void testEscapeInner() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final String expected = "\\<hover:show_text:\"\\<red>test:TEST\">TEST";
    assertEquals(expected, PARSER.escapeTags(input));
  }

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/169
  @Test
  void testEscapeComplexInner() {
    final String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here <notToken></click><blue> to <bold>FEEL</underlined> it";
    final String expected = "\\<yellow><test> random \\<bold>stranger\\</bold>\\<click:run_command:test command>\\<underlined>\\<red>click here <notToken>\\</click>\\<blue> to \\<bold>FEEL\\</underlined> it";
    assertEquals(expected, PARSER.escapeTags(input));
  }

  @Test
  void testEscapePlaceholders() {
    final String input = "Hello, <red><name>!";
    final String expected = "Hello, \\<red>\\<name>!";
    assertEquals(expected, PARSER.escapeTags(input, parsed("name", "you")));
  }

  @Test
  void testUnescape() {
    final String input = "<yellow>TEST\\<green> nested\\</green>Test";
    final String expected = "TEST<green> nested</green>Test";
    final Component comp = PARSER.deserialize(input);

    assertEquals(expected, PlainTextComponentSerializer.plainText().serialize(comp));
  }

  @Test
  void testNoUnescape() {
    final String input = "<yellow>TEST\\<green> \\\\< nested\\</green>Test";
    final String expected = "TEST<green> \\< nested</green>Test";
    final TextComponent comp = (TextComponent) PARSER.deserialize(input);

    assertEquals(expected, PlainTextComponentSerializer.plainText().serialize(comp));
  }

  @Test
  void testEscapeParse() {
    final String expected = "<red>test</red>";
    final String escaped = MiniMessage.miniMessage().escapeTags(expected);
    final Component comp = MiniMessage.miniMessage().deserialize(escaped);

    assertEquals(expected, PlainTextComponentSerializer.plainText().serialize(comp));
  }

  // https://github.com/KyoriPowered/adventure/issues/799
  @Test
  void testEscapeIgnoresSectionSigns() {
    final String input = "Hello, read §64 for <red> information";
    final String expected = "Hello, read §64 for \\<red> information";

    assertEquals(expected, PARSER.escapeTags(input));
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

    this.assertParsedEquals(expected, input, component("test", text("Hello!")));
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
  void testBackSpace() {
    final String input = "\\!/ IMPORTANT \\!/";
    final Component expected = text("\\!/ IMPORTANT \\!/");

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGH5() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:'<pack_url>'><hover:show_text:\"<green>/!\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
      .append(text("»"))
      .append(empty().color(GRAY)
        .append(text(" To download it from the internet, "))
        .append(text("CLICK HERE").decorate(BOLD).color(GREEN).clickEvent(openUrl("https://www.google.com")).hoverEvent(showText(text("/!\\ install it from Options/ResourcePacks in your game").color(GREEN))))
      );

    this.assertParsedEquals(expected, input, parsed("pack_url", "https://www.google.com"));
  }

  @Test
  void testGH5Modified() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:'<pack_url>'><hover:show_text:'<green>/!\\ install it from \\'Options/ResourcePacks\\' in your game'><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
      .append(text("»"))
      .append(empty().color(GRAY)
        .append(text(" To download it from the internet, "))
        .append(text("CLICK HERE").decorate(BOLD).color(GREEN).hoverEvent(showText(text("/!\\ install it from 'Options/ResourcePacks' in your game").color(GREEN))).clickEvent(openUrl("https://www.google.com")))
      );

    // should work
    this.assertParsedEquals(expected, input, parsed("pack_url", "https://www.google.com"));
  }

  @Test
  void testGH5Quoted() {
    final String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:\"https://www.google.com\"><hover:show_text:\"<green>/!\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
    final Component expected = empty().color(DARK_GRAY)
      .append(text("»"))
      .append(empty().color(GRAY)
        .append(text(" To download it from the internet, "))
        .append(text("CLICK HERE").decorate(BOLD).color(GREEN).clickEvent(openUrl("https://www.google.com")).hoverEvent(showText(text("/!\\ install it from Options/ResourcePacks in your game").color(GREEN))))
      );

    // should work
    this.assertParsedEquals(expected, input);

    // shouldnt throw an error
    PARSER.deserialize(input, parsed("url", "https://www.google.com"));
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
  void testNonTerminatingQuote() {
    final Component expected = empty().append(text("Remember the<3\"").color(RED)).append(text(" bug"));
    final Component expected1 = empty().append(text("Remember the<3'").color(RED)).append(text(" bug"));
    final Component expected2 = empty().append(text("Remember the<h:\"").color(RED)).append(text(" bug"));
    final Component expected3 = empty().append(text("Remember the<h:\"").color(RED)).append(text(" \"bug"));
    // This one is an actually valid use of quotes
    final Component expected4 = empty().append(text("Remember the<h:\"</red> \">bug").color(RED));
    final String input = "<red>Remember the<3\"</red> bug";
    final String input1 = "<red>Remember the<3'</red> bug";
    final String input2 = "<red>Remember the<h:\"</red> bug";
    final String input3 = "<red>Remember the<h:\"</red> \"bug";
    final String input4 = "<red>Remember the<h:\"</red> \">bug";
    this.assertParsedEquals(expected, input);
    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
    this.assertParsedEquals(expected3, input3);
    this.assertParsedEquals(expected4, input4);
  }

  // https://github.com/KyoriPowered/adventure/issues/821
  @Test
  void testEscapeIncompleteTags() {
    final String input = "<<aqua> a";
    final String escaped = PARSER.escapeTags(input);

    assertEquals("<\\<aqua> a", escaped);

    final List<Token> expectedTokens = Collections.singletonList(new Token(0, escaped.length(), TokenType.TEXT));
    assertIterableEquals(expectedTokens, TokenParser.tokenize(escaped, false));

    final Component expected = text("<<aqua> a");
    this.assertParsedEquals(expected, escaped);
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

  // GH-134
  @Test
  void testEscapeOutsideOfContext() {
    final String input = "\\\"";
    final Component expected = text("\\\"");

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testEscapesEscapablePlainText() {
    final String input = "\\\\<red>hi";
    final Component expected = text()
      .content("\\")
      .append(Component.text("hi", NamedTextColor.RED))
      .build();
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testEscapeInsideOfContext() {
    final String input = "<hover:show_text:'Look at\\\\ this \\''>Test";
    final Component expected = text()
            .content("Test")
            .hoverEvent(text("Look at\\ this '"))
            .build();

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testEscapeAtEnd() {
    final String input = "Please don't crash \\";
    final Component expected = text("Please don't crash \\");

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testCaseInsensitive() {
    final String input1 = "<red>this is <BOLD>an error</bold> message";
    final String input2 = "<C:reD>also red";

    final Component expected1 = text()
      .color(RED)
      .append(text("this is "))
      .append(text("an error", style(BOLD)))
      .append(text(" message"))
      .build();
    final Component expected2 = text("also red", RED);

    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
  }

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/165
  @Test
  void testClosingTagAtRootLevel() {
    final String input = "one</blue>two";

    final Component expected = text("one</blue>two");

    this.assertParsedEquals(expected, input);
  }

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/166
  @Test
  void testEmptyTagPart() {
    final String input = "<hover:show_text:\"\">text</hover>";
    final String input2 = "<hover:show_text:>text</hover>";

    final Component expected = text("text").hoverEvent(showText(empty()));

    this.assertParsedEquals(expected, input);
    this.assertParsedEquals(expected, input2);
  }

  @Test
  void testTreeOutput() {
    final String input = "<red> RED <blue> <name> <click:open_url:https://github.com> good <action> </click>";

    final TagResolver resolver = TagResolver.resolver(Placeholder.parsed("name", "you"), Placeholder.component("action", Component.text("click")));
    final Node tree = MiniMessage.miniMessage().deserializeToTree(input, resolver);
    final String expected = "Node {\n" +
      "  TagNode('red') {\n" +
      "    TextNode(' RED ')\n" +
      "    TagNode('blue') {\n" +
      "      TextNode(' you ')\n" +
      "      TagNode('click', 'open_url', 'https://github.com') {\n" +
      "        TextNode(' good ')\n" +
      "        TagNode('action') {\n" +
      "        }\n" +
      "        TextNode(' ')\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}\n";

    assertEquals(expected, tree.toString());
  }

  @Test
  void testTagsSelfClosable() {
    final String input = "<red>hello <lang:gameMode.creative/> there";

    final Component parsed = Component.text()
      .content("hello ")
      .color(NamedTextColor.RED)
      .append(
        Component.translatable("gameMode.creative"),
        Component.text(" there")
      )
      .build();

    this.assertParsedEquals(parsed, input);
  }

  @Test
  void testIgnorableSelfClosable() {
    final String input = "<red/>things";

    final Component parsed = Component.text().append(
        Component.text("", NamedTextColor.RED),
        Component.text("things")
      )
      .build();

    this.assertParsedEquals(parsed, input);
  }

  // DiamondFire start
  /*
  @Test
  void testLegacySymbolForbidden() {
    final String failingTest = "Hello §Cfriends";

    // Non-strict
    System.out.println(assertThrows(ParsingException.class, () -> PARSER.deserialize(failingTest)).getMessage());
  }
  */
  // DiamondFire end

  @Test
  void testInvalidTagNames() {
    final String input1 = "Hello <this_is_%not_allowed> but ignored?";
    final String input2 = "Hello <this_is_not_allowed!> but ignored?";
    final String input3 = "Hello <!?this_is_not_allowed> but ignored?";
    final String input4 = "Hello <##this_is_%not_allowed> but ignored?";
    final String input5 = "<3 >Mini<3 />Message</3 >";
    final String input6 = "this message <\"red\">isn't red";

    this.assertParsedEquals(Component.text(input1), input1);
    this.assertParsedEquals(Component.text(input2), input2);
    this.assertParsedEquals(Component.text(input3), input3);
    this.assertParsedEquals(Component.text(input4), input4);
    this.assertParsedEquals(Component.text(input5), input5);
    this.assertParsedEquals(Component.text(input6), input6);
  }

  @Test
  void testValidTagNames() {
    final String passingTest = "Hello <this_is_allowed> but cool?";
    final String passingTest1 = "Hello <this-is-allowed> but cool?";
    final String passingTest2 = "Hello <!allowed> but cool?";
    final String passingTest3 = "Hello <?allowed> but cool?";
    final String passingTest4 = "Hello <#allowed> but cool?";

    assertDoesNotThrow(() -> PARSER.deserialize(passingTest));
    assertDoesNotThrow(() -> PARSER.deserialize(passingTest1));
    assertDoesNotThrow(() -> PARSER.deserialize(passingTest2));
    assertDoesNotThrow(() -> PARSER.deserialize(passingTest3));
    assertDoesNotThrow(() -> PARSER.deserialize(passingTest4));

    // DiamondFire start
    final String passingTestDF1 = "Hello <&a> but cool?";
    final String passingTestDF2 = "Hello <&x&c&0&f&f&e&e> but cool?";

    assertDoesNotThrow(() -> PARSER.deserialize(passingTestDF1));
    assertDoesNotThrow(() -> PARSER.deserialize(passingTestDF2));
    // DiamondFire end
  }

  @Test
  void invalidPreprocessTagNames() {
    final String input = "Some<##>of<>these<tag>are<3 >tags";
    final Component expected = Component.text("Some<##>of<>these(meow)are<3 >tags");
    final TagResolver alwaysMatchingResolver = new TagResolver() {
      @Override
      public Tag resolve(final @NotNull String name, final @NotNull ArgumentQueue arguments, final @NotNull Context ctx) throws ParsingException {
        return Tag.preProcessParsed("(meow)");
      }

      @Override
      public boolean has(final @NotNull String name) {
        return true;
      }
    };

    this.assertParsedEquals(expected, input, alwaysMatchingResolver);
  }
}
