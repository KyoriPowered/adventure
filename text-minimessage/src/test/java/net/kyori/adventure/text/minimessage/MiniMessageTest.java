/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
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
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MiniMessageTest extends AbstractTest {

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
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(miniMessage, expected, input);
  }

  @Test
  void testNormalPlaceholders() {
    final Component expected = text("TEST").color(RED);
    final String input = "<red><test>";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(miniMessage, expected, input, component("test", text("TEST")));
  }

  @Test
  void testPlaceholderSimple() {
    final Component expected = text("TEST");
    final String input = "<test>";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(miniMessage, expected, input, Placeholder.parsed("test", "TEST"));
  }

  @Test
  void testPlaceholderComponent() {
    final Component expected = text("TEST", RED);
    final String string = "<test>";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(miniMessage, expected, string, component("test", text("TEST", RED)));
  }

  @Test
  void testPlaceholderComponentInheritedStyle() {
    final Component expected = text("TEST", RED, UNDERLINED, BOLD);
    final String input = "<green><bold><test>";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(miniMessage, expected, input, component("test", text("TEST", RED, UNDERLINED)));
  }

  @Test
  void testPlaceholderComponentMixed() {
    final Component expected = empty().color(GREEN).decorate(BOLD)
        .append(text("TEST", style(RED, UNDERLINED)))
        .append(text("Test2"));
    final String input = "<green><bold><test><test2>";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    final TagResolver t1 = component("test", text("TEST", style(RED, UNDERLINED)));
    final TagResolver t2 = Placeholder.parsed("test2", "Test2");

    this.assertParsedEquals(miniMessage, expected, input, t1, t2);
  }

  @Test
  void testPreprocessing() {
    final Component expected = MiniMessage.miniMessage().deserialize("<red>Hello, world!</red>");

    final String input = "Hello";
    final MiniMessage miniMessage = MiniMessage.builder().preProcessor(str -> "<red>" + str + ", world!</red>").build();
    this.assertParsedEquals(miniMessage, expected, input);
  }

  // GH-103
  @Test
  void testPlaceholderInHover() {
    final Component expected = text("This is a test message.")
        .hoverEvent(showText(text("[Plugin]").color(color(0xff0000))));

    final String input = "<hover:show_text:'<prefix>'>This is a test message.";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(miniMessage, expected, input, component("prefix", MiniMessage.miniMessage().deserialize("<#FF0000>[Plugin]<reset>")));
  }

  @Test
  void testCustomRegistry() {
    final Component expected = text("<green><bold>").append(text("TEST"));
    final String input = "<green><bold><test>";
    final MiniMessage miniMessage = MiniMessage.builder().tags(TagResolver.empty()).build();

    this.assertParsedEquals(miniMessage, expected, input, component("test", text("TEST")));
  }

  @Test
  void testCustomRegistryBuilder() {
    final Component expected = empty().color(GREEN)
        .append(text("<bold>"))
        .append(text("TEST"));
    final String input = "<green><bold><test>";
    final TagResolver registry = TagResolver.builder()
            .resolver(StandardTags.color())
            .build();
    final MiniMessage miniMessage = MiniMessage.builder().tags(registry).build();

    this.assertParsedEquals(miniMessage, expected, input, component("test", text("TEST")));
  }

  @Test
  void testPlaceholderResolver() {
    final Component expected = text("TEST", RED).decorate(BOLD);

    final String input = "<green><bold><test>";

    final TagResolver.WithoutArguments resolver = name -> {
      if (name.equalsIgnoreCase("test")) {
        return Tag.inserting(text("TEST", RED));
      }
      return null;
    };

    final MiniMessage miniMessage = MiniMessage.builder().editTags(b -> b.resolver(TagResolver.caching(resolver))).build();

    this.assertParsedEquals(miniMessage, expected, input);
  }

  @Test
  void testGroupingPlaceholderResolver() {
    final Component expected = empty()
        .append(text("ONE", RED))
        .append(text("<none>"))
        .append(text("TWO", GREEN));

    final String input = "<one><none><two>";

    final TagResolver.Single resolver = Placeholder.component("one", text("ONE", RED));

    final MiniMessage miniMessage = MiniMessage.builder().editTags(b -> b.resolvers(
        component("two", text("TWO", GREEN)),
        TagResolver.caching(resolver)
      ))
      .build();

    this.assertParsedEquals(miniMessage, expected, input);
  }

  @Test
  void testOrderOfPlaceholders() {
    final Component expected = text("A")
      .append(text("B"))
      .append(text("C"));
    final String input = "<a><b><!c>";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(
      miniMessage,
      expected,
      input,
      component("a", text("A")),
      component("b", text("B")),
      component("!c", text("C"))
    );
  }

  @Test
  void testNodesInPlaceholder() {
    final Component expected = empty().color(RED)
        .append(text("MiniDigger"))
        .append(empty().color(GRAY)
            .append(text(": "))
            .append(text("</pre><red>Test").color(RED))
        );
    final String input = "<red><username><gray>: <red><message>";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(miniMessage, expected, input, component("username", text("MiniDigger")), component("message", text("</pre><red>Test")));
  }

  @Test
  void testLazyPlaceholder() {
    final Component expected = text("This is a ")
      .append(text("TEST"));
    final String input = "This is a <test>";
    final MiniMessage miniMessage = MiniMessage.miniMessage();

    this.assertParsedEquals(miniMessage, expected, input, component("test", () -> text("TEST")));
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

    this.assertParsedEquals(miniMessage, expected, MiniMessage.miniMessage().escapeTags("<3"));
  }

  @Test
  void testStrictException() {
    final String input = "<gray>Example: <click:suggest_command:/plot flag set coral-dry true><gold>/plot flag set coral-dry true<click></gold></gray>";
    assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().deserialize(input));
  }

  @Test
  void testMissingCloseOfHover() {
    final String input = "<hover:show_text:'<blue>Hello</blue>'<red>TEST</red></hover><click:suggest_command:'/msg <user>'><user></click> <reset>: <hover:show_text:'<date>'><message></hover>";
    assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().deserialize(input));
  }

  @Test
  void testNonEndingComponent() {
    final String input = "<red is already created! Try different name! :)";
    this.assertParsedEquals(Component.text(input), input);
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

    final ParsingException thrown = assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().deserialize(input));
    assertEquals(thrown.getMessage(), errorMessage);
  }

  @Test
  void implicitCloseStrict() {
    final String input = "<red>RED<green>GREEN</red>NO COLOR<blue>BLUE</blue>";

    final String errorMessage = "Unclosed tag encountered; green is not closed, because red was closed first.\n" +
        "\t<red>RED<green>GREEN</red>NO COLOR<blue>BLUE</blue>\n" +
        "\t^~~~^   ^~~~~~^     ^~~~~^";

    final ParsingException thrown = assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().deserialize(input));
    assertEquals(thrown.getMessage(), errorMessage);
  }

  @Test
  void implicitCloseNestedStrict() {
    final String input = "<red>RED<green>GREEN<blue>BLUE<yellow>YELLOW</green>";

    final String errorMessage = "Unclosed tag encountered; yellow is not closed, because green was closed first.\n" +
        "\t<red>RED<green>GREEN<blue>BLUE<yellow>YELLOW</green>\n" +
        "\t        ^~~~~~^               ^~~~~~~^      ^~~~~~~^";

    final ParsingException thrown = assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().deserialize(input));
    assertEquals(thrown.getMessage(), errorMessage);
  }

  @Test
  void resetWhileStrict() {
    final String input = "<red>RED<green>GREEN<reset>NO COLOR<blue>BLUE</blue>";

    final String errorMessage = "<reset> tags are not allowed when strict mode is enabled\n" +
        "\t<red>RED<green>GREEN<reset>NO COLOR<blue>BLUE</blue>\n" +
        "\t                    ^~~~~~^";

    final ParsingException thrown = assertThrows(ParsingException.class, () -> MiniMessage.builder().strict(true).build().deserialize(input));
    assertEquals(thrown.getMessage(), errorMessage);
  }

  @Test
  void debugModeSimple() {
    final String input = "<red> RED </red>";

    final StringBuilder sb = new StringBuilder();
    MiniMessage.builder().debug(sb::append).build().deserialize(input);
    final List<String> messages = Arrays.asList(sb.toString().split("\n"));

    assertTrue(messages.contains("Beginning parsing message <red> RED </red>"));
    assertTrue(messages.contains("Attempting to match node 'red' at column 0"));
    assertTrue(anyMatch(messages, it -> it.startsWith("Successfully matched node 'red' to tag ")));
    assertTrue(messages.contains("Text parsed into element tree:"));
    assertTrue(messages.contains("Node {"));
    assertTrue(messages.contains("  TagNode('red') {"));
    assertTrue(messages.contains("    TextNode(' RED ')"));
    assertTrue(messages.contains("  }"));
    assertTrue(messages.contains("}"));
  }

  @Test
  void debugModeMoreComplex() {
    final String input = "<red> RED <blue> BLUE <click> bad click </click>";

    final StringBuilder sb = new StringBuilder();
    MiniMessage.builder().debug(sb::append).build().deserialize(input);
    final List<String> messages = Arrays.asList(sb.toString().split("\n"));

    assertTrue(messages.contains("Beginning parsing message <red> RED <blue> BLUE <click> bad click </click>"));
    assertTrue(messages.contains("Attempting to match node 'red' at column 0"));
    assertTrue(anyMatch(messages, it -> it.startsWith("Successfully matched node 'red' to tag ")));
    assertTrue(messages.contains("Attempting to match node 'blue' at column 10"));
    assertTrue(anyMatch(messages, it -> it.startsWith("Successfully matched node 'blue' to tag ")));
    assertTrue(messages.contains("Attempting to match node 'click' at column 22"));
    assertTrue(messages.contains("Could not match node 'click' - A click tag requires an action of one of [open_url, run_command, copy_to_clipboard, open_file, change_page, suggest_command]"));
    assertTrue(messages.contains("\t<red> RED <blue> BLUE <click> bad click </click>"));
    assertTrue(messages.contains("\t                      ^~~~~~^"));
    assertTrue(messages.contains("Text parsed into element tree:"));
    assertTrue(messages.contains("Node {"));
    assertTrue(messages.contains("  TagNode('red') {"));
    assertTrue(messages.contains("    TextNode(' RED ')"));
    assertTrue(messages.contains("    TagNode('blue') {"));
    assertTrue(messages.contains("      TextNode(' BLUE <click> bad click </click>')"));
    assertTrue(messages.contains("    }"));
    assertTrue(messages.contains("  }"));
    assertTrue(messages.contains("}"));
  }

  @Test
  void debugModeMoreComplexNoError() {
    final String input = "<red> RED <blue> BLUE <click:open_url:https://github.com> good click </click>";

    final StringBuilder sb = new StringBuilder();
    MiniMessage.builder().debug(sb::append).build().deserialize(input);
    final List<String> messages = Arrays.asList(sb.toString().split("\n"));

    assertTrue(messages.contains("Beginning parsing message <red> RED <blue> BLUE <click:open_url:https://github.com> good click </click>"));
    assertTrue(messages.contains("Attempting to match node 'red' at column 0"));
    assertTrue(anyMatch(messages, it -> it.startsWith("Successfully matched node 'red' to tag ")));
    assertTrue(messages.contains("Attempting to match node 'blue' at column 10"));
    assertTrue(anyMatch(messages, it -> it.startsWith("Successfully matched node 'blue' to tag ")));
    assertTrue(messages.contains("Attempting to match node 'click' at column 22"));
    assertTrue(anyMatch(messages, it -> it.startsWith("Successfully matched node 'click' to tag ")));
    assertTrue(messages.contains("Text parsed into element tree:"));
    assertTrue(messages.contains("Node {"));
    assertTrue(messages.contains("  TagNode('red') {"));
    assertTrue(messages.contains("    TextNode(' RED ')"));
    assertTrue(messages.contains("    TagNode('blue') {"));
    assertTrue(messages.contains("      TextNode(' BLUE ')"));
    assertTrue(messages.contains("      TagNode('click', 'open_url', 'https://github.com') {"));
    assertTrue(messages.contains("        TextNode(' good click ')"));
    assertTrue(messages.contains("      }"));
    assertTrue(messages.contains("    }"));
    assertTrue(messages.contains("  }"));
    assertTrue(messages.contains("}"));
  }

  static class TestTarget1 implements Pointered {
    public String data;
  }

  static class TestTarget2 implements Pointered {
  }

  @Test
  void contextTargetUtilMethods() {
    final TagResolver tagResolver0 = TagResolver.resolver("tag", (argumentQueue, context) -> {
      assertThrows(ParsingException.class, () -> context.targetAsType(TestTarget2.class));
      return Tag.inserting(Component.text(context.targetAsType(TestTarget1.class).data));
    });
    final TestTarget1 target = new TestTarget1();
    target.data = "hello";
    assertEquals(Component.text("hello"), MiniMessage.miniMessage().deserialize("<tag>", target, tagResolver0));

    final TagResolver tagResolver1 = TagResolver.resolver("tag", (argumentQueue, context) -> {
      assertThrows(ParsingException.class, () -> context.targetAsType(TestTarget1.class));
      assertThrows(ParsingException.class, () -> context.targetAsType(TestTarget2.class));
      assertThrows(ParsingException.class, context::targetOrThrow);
      assertNull(context.target());

      return Tag.inserting(Component.text("whoops"));
    });
    assertEquals(Component.text("whoops"), MiniMessage.miniMessage().deserialize("<tag>", tagResolver1));

  }

  private static <T> boolean anyMatch(final Collection<T> items, final Predicate<T> test) {
    return items.stream()
      .anyMatch(test);
  }
}
