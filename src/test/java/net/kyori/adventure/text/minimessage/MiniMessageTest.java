/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.suggestCommand;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MiniMessageTest extends TestBase {

  @Test
  void testNormalBuilder() {
    final Component expected = text("Test").color(RED);
    final String input = "<red>Test";
    final MiniMessage miniMessage = MiniMessage.builder().build();

    this.assertParsedEquals(miniMessage, expected, input);
  }

  @Test
  void testNormal() {
    final Component expected = text("Test").color(RED);
    final String input = "<red>Test";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input);
  }

  @Test
  void testNormalPlaceholders() {
    final Component expected = text("TEST").color(RED);
    final String input = "<red><test>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input, "test", "TEST");
  }

  @Test
  void testObjectPlaceholders() {
    final Component expected = empty().color(RED)
      .append(text("ONE"))
      .append(text("TWO", GREEN))
      .append(empty().color(BLUE)
        .append(text("THREE"))
        .append(text("FOUR"))
        .append(text("FIVE", YELLOW))
      );
    final String input = "<red>ONE<two><blue>THREE<four><five>";
    final MiniMessage miniMessage = MiniMessage.get();
    this.assertParsedEquals(miniMessage, expected, input,
      "two", text("TWO", GREEN),
      "four", "FOUR",
      "five", text("FIVE", YELLOW));
  }

  @Test
  void testObjectPlaceholdersUnbalanced() {
    assertThrows(IllegalArgumentException.class, () -> MiniMessage.get().parse("<red>ONE<two><blue>THREE<four><five>",
      "two", text("TWO", GREEN),
      "four", "FOUR",
      "five"));
  }

  @Test
  void testTemplateSimple() {
    final Component expected = text("TEST");
    final String input = "<test>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input, Template.of("test", "TEST"));
  }

  @Test
  void testTemplateComponent() {
    final Component expected = text("TEST", RED);
    final String string = "<test>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, string, Template.of("test", text("TEST", RED)));
  }

  @Test
  void testTemplateComponentInheritedStyle() {
    final Component expected = text("TEST", RED, UNDERLINED, BOLD);
    final String input = "<green><bold><test>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input, Template.of("test", text("TEST", RED, UNDERLINED)));
  }

  @Test
  void testTemplateComponentMixed() {
    final Component expected = empty().color(GREEN).decorate(BOLD)
        .append(text("TEST", style(RED, UNDERLINED)))
        .append(text("Test2"));
    final String input = "<green><bold><test><test2>";
    final MiniMessage miniMessage = MiniMessage.get();

    final Template t1 = Template.of("test", text("TEST", style(RED, UNDERLINED)));
    final Template t2 = Template.of("test2", "Test2");

    this.assertParsedEquals(miniMessage, expected, input, t1, t2);
  }

  // GH-103
  @Test
  void testTemplateInHover() {
    final Component expected = text("This is a test message.")
        .hoverEvent(showText(text("[Plugin]").color(color(0xff0000))));

    final String input = "<hover:show_text:'<prefix>'>This is a test message.";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input, Template.of("prefix", MiniMessage.get().parse("<#FF0000>[Plugin]<reset>")));
  }

  @Test
  void testCustomRegistry() {
    final Component expected = text("<green><bold>").append(text("TEST"));
    final String input = "<green><bold><test>";
    final MiniMessage miniMessage = MiniMessage.builder().transformations(TransformationRegistry.empty()).build();

    this.assertParsedEquals(miniMessage, expected, input, "test", "TEST");
  }

  @Test
  void testCustomRegistryBuilder() {
    final Component expected = empty().color(GREEN)
        .append(text("<bold>"))
        .append(text("TEST"));
    final String input = "<green><bold><test>";
    final TransformationRegistry registry = TransformationRegistry.builder()
            .clear()
            .add(TransformationType.COLOR)
            .build();
    final MiniMessage miniMessage = MiniMessage.builder().transformations(registry).build();

    this.assertParsedEquals(miniMessage, expected, input, "test", "TEST");
  }

  @Test
  void testPlaceholderResolver() {
    final Component expected = text("TEST", RED).decorate(BOLD);

    final String input = "<green><bold><test>";

    final Function<String, ComponentLike> resolver = name -> {
      if (name.equalsIgnoreCase("test")) {
        return text("TEST").color(RED);
      }
      return null;
    };

    final MiniMessage miniMessage = MiniMessage.builder().placeholderResolver(resolver).build();

    this.assertParsedEquals(miniMessage, expected, input);
  }

  @Test
  void testOrderOfPlaceholders() {
    final Component expected = text("A")
      .append(text("B"))
      .append(text("C"));
    final String input = "<a><b><_c>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input,
      "a", text("A"),
      "b", text("B"),
      "_c", text("C"));
  }

  @Test
  void testUnbalancedPlaceholders() {
    final String expected = "Argument 1 in placeholders is a value, must be Component or String, was java.lang.Integer";
    assertEquals(expected, assertThrows(IllegalArgumentException.class, () -> MiniMessage.get().parse("<a>", "a", 2)).getMessage());
  }

  // GH-98
  @Test
  void testTemplateInsideOfPre() {
    final Component expected = empty().color(RED)
      .append(text("MiniDigger"))
      .append(empty().color(GRAY)
        .append(text(": "))
        .append(text("<red><message>"))
      );
    final String input = "<red><username><gray>: <pre><red><message>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input, Template.of("username", text("MiniDigger")), Template.of("message", text("Hello world")));
  }

  // GH-97
  @Test
  void testUnsafePre() {
    final Component expected = empty().color(RED)
      .append(text("MiniDigger"))
      .append(empty().color(GRAY)
        .append(text(": "))
        .append(text("<red><message>"))
      );
    final String input = "<red><username><gray>: <pre><red><message>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input, Template.of("username", text("MiniDigger")), Template.of("message", text("</pre><red>Test")));
    this.assertParsedEquals(miniMessage, expected, input, Template.of("username", "MiniDigger"), Template.of("message", "</pre><red>Test"));
    this.assertParsedEquals(miniMessage, expected, input, "username", "MiniDigger", "message", "</pre><red>Test");
  }

  @Test
  void testNodesInTemplate() {
    final Component expected = empty().color(RED)
        .append(text("MiniDigger"))
        .append(empty().color(GRAY)
            .append(text(": "))
            .append(text("</pre><red>Test").color(RED))
        );
    final String input = "<red><username><gray>: <red><message>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input, Template.of("username", text("MiniDigger")), Template.of("message", text("</pre><red>Test")));
    this.assertParsedEquals(miniMessage, expected, input, Template.of("username", "MiniDigger"), Template.of("message", "</pre><red>Test"));
    this.assertParsedEquals(miniMessage, expected, input, "username", "MiniDigger", "message", "</pre><red>Test");
  }

  @Test
  void testLazyTemplate() {
    final Component expected = text("This is a ")
      .append(text("TEST"));
    final String input = "This is a <test>";
    final MiniMessage miniMessage = MiniMessage.get();

    this.assertParsedEquals(miniMessage, expected, input, Template.of("test", () -> text("TEST")));
  }

  @Test
  void testNonStrict() {
    final String input = "<gray>Example: <click:suggest_command:/plot flag set coral-dry true><gold>/plot flag set coral-dry true</gold></click></gray>";
    final Component expected = empty().color(GRAY)
      .append(text("Example: "))
      .append(text("/plot flag set coral-dry true")
          .color(GOLD)
          .clickEvent(suggestCommand("/plot flag set coral-dry true"))
      );

    final MiniMessage miniMessage = MiniMessage.builder()
      .strict(false)
      .build();

    this.assertParsedEquals(miniMessage, expected, input);
  }

  @Test
  void testNonStrictGH69() {
    final Component expected = text("<3");
    final MiniMessage miniMessage = MiniMessage.builder()
      .strict(false)
      .build();

    this.assertParsedEquals(miniMessage, expected, MiniMessage.get().escapeTokens("<3"));
  }

  @Test
  void testStrictException() {
    final String input = "<gray>Example: <click:suggest_command:/plot flag set coral-dry true><gold>/plot flag set coral-dry true<click></gold></gray>";
    assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().parse(input));
  }

  @Test
  void testMissingCloseOfHover() {
    final String input = "<hover:show_text:'<blue>Hello</blue>'<red>TEST</red></hover><click:suggest_command:'/msg <user>'><user></click> <reset>: <hover:show_text:'<date>'><message></hover>";
    assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().parse(input));
  }

  @Test
  void testNonEndingComponent() {
    final String input = "<red is already created! Try different name! :)";
    MiniMessage.builder().parsingErrorMessageConsumer(strings -> assertEquals(strings, Collections.singletonList("Expected end sometimes after open tag + name, but got name = Token{type=NAME, value=\"red is already created! Try different name! \"} and inners = []"))).build().parse(input);
  }

  @Test
  void testIncompleteTag() {
    final String input = "<red>Click <click>here</click> to win a new <bold>car!";
    final Component expected = empty().color(RED)
      .append(text("Click <click>here</click> to win a new "))
      .append(text("car!").decorate(BOLD));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void allClosedTagsStrict() {
    final String input = "<red>RED<green>GREEN</green>RED<blue>BLUE</blue></red>";
    final Component expected = empty().color(RED)
      .append(text("RED"))
      .append(text("GREEN").color(GREEN))
      .append(text("RED"))
      .append(text("BLUE").color(BLUE));

    this.assertParsedEquals(MiniMessage.builder().strict(true).build(), expected, input);
  }

  @Test
  void unclosedTagStrict() {
    final String input = "<red>RED<green>GREEN</green>RED<blue>BLUE";

    final String errorMessage = "All tags must be explicitly closed while in strict mode. End of string found with open tags: red, blue\n" +
        "\t<red>RED<green>GREEN</green>RED<blue>BLUE\n" +
        "\t^~~~^                          ^~~~~^";

    final ParsingException thrown = assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().parse(input));
    assertEquals(thrown.getMessage(), errorMessage);
  }

  @Test
  void implicitCloseStrict() {
    final String input = "<red>RED<green>GREEN</red>NO COLOR<blue>BLUE</blue>";

    final String errorMessage = "Unclosed tag encountered; green is not closed, because red was closed first.\n" +
        "\t<red>RED<green>GREEN</red>NO COLOR<blue>BLUE</blue>\n" +
        "\t^~~~^   ^~~~~~^     ^~~~~^";

    final ParsingException thrown = assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().parse(input));
    assertEquals(thrown.getMessage(), errorMessage);
  }

  @Test
  void implicitCloseNestedStrict() {
    final String input = "<red>RED<green>GREEN<blue>BLUE<yellow>YELLOW</green>";

    final String errorMessage = "Unclosed tag encountered; yellow is not closed, because green was closed first.\n" +
        "\t<red>RED<green>GREEN<blue>BLUE<yellow>YELLOW</green>\n" +
        "\t        ^~~~~~^               ^~~~~~~^      ^~~~~~~^";

    final ParsingException thrown = assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().parse(input));
    assertEquals(thrown.getMessage(), errorMessage);
  }

  @Test
  void resetWhileStrict() {
    final String input = "<red>RED<green>GREEN<reset>NO COLOR<blue>BLUE</blue>";

    final String errorMessage = "<reset> tags are not allowed when strict mode is enabled\n" +
        "\t<red>RED<green>GREEN<reset>NO COLOR<blue>BLUE</blue>\n" +
        "\t                    ^~~~~~^";

    final ParsingException thrown = assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().parse(input));
    assertEquals(thrown.getMessage(), errorMessage);
  }

  @Test
  void debugModeSimple() {
    final String input = "<red> RED </red>";

    final StringBuilder sb = new StringBuilder();
    MiniMessage.builder().debug(sb).build().parse(input);

    final String expected = "Beginning parsing message <red> RED </red>\n" +
        "Attempting to match node 'red' at column 0\n" +
        "Successfully matched node 'red' to transformation ColorTransformation\n" +
        "Text parsed into element tree:\n" +
        "Node {\n" +
        "  TagNode('red') {\n" +
        "    TextNode(' RED ')\n" +
        "  }\n" +
        "}\n";

    assertEquals(expected, sb.toString());
  }

  @Test
  void debugModeMoreComplex() {
    final String input = "<red> RED <blue> BLUE <click> bad click </click>";

    final StringBuilder sb = new StringBuilder();
    MiniMessage.builder().debug(sb).build().parse(input);

    final String expected = "Beginning parsing message <red> RED <blue> BLUE <click> bad click </click>\n" +
        "Attempting to match node 'red' at column 0\n" +
        "Successfully matched node 'red' to transformation ColorTransformation\n" +
        "Attempting to match node 'blue' at column 10\n" +
        "Successfully matched node 'blue' to transformation ColorTransformation\n" +
        "Attempting to match node 'click' at column 22\n" +
        "Could not match node 'click' - Don't know how to turn [] into a click event\n" +
        "\t<red> RED <blue> BLUE <click> bad click </click>\n" +
        "\t                      ^~~~~~^\n" +
        "Text parsed into element tree:\n" +
        "Node {\n" +
        "  TagNode('red') {\n" +
        "    TextNode(' RED ')\n" +
        "    TagNode('blue') {\n" +
        "      TextNode(' BLUE <click> bad click </click>')\n" +
        "    }\n" +
        "  }\n" +
        "}\n";

    assertEquals(expected, sb.toString());
  }

  @Test
  void debugModeMoreComplexNoError() {
    final String input = "<red> RED <blue> BLUE <click:open_url:https://github.com> good click </click>";

    final StringBuilder sb = new StringBuilder();
    MiniMessage.builder().debug(sb).build().parse(input);

    final String expected = "Beginning parsing message <red> RED <blue> BLUE <click:open_url:https://github.com> good click </click>\n" +
        "Attempting to match node 'red' at column 0\n" +
        "Successfully matched node 'red' to transformation ColorTransformation\n" +
        "Attempting to match node 'blue' at column 10\n" +
        "Successfully matched node 'blue' to transformation ColorTransformation\n" +
        "Attempting to match node 'click' at column 22\n" +
        "Successfully matched node 'click' to transformation ClickTransformation\n" +
        "Text parsed into element tree:\n" +
        "Node {\n" +
        "  TagNode('red') {\n" +
        "    TextNode(' RED ')\n" +
        "    TagNode('blue') {\n" +
        "      TextNode(' BLUE ')\n" +
        "      TagNode('click', 'open_url', 'https://github.com') {\n" +
        "        TextNode(' good click ')\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}\n";

    assertEquals(expected, sb.toString());
  }
}
