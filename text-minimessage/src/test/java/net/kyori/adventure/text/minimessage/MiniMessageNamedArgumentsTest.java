package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageNamedArgumentsTest extends AbstractTest {

  private static final TagResolver INSERT_VALUE_RESOLVER = TagResolver.namedResolver("insert", (args, ctx) -> Tag.selfClosingInserting(
    text(args.getOrThrow("value", "value is missing").value())
  ));

  @Test
  void testBasicInsertingNamedTagArguments() {
    final String input = "<insert value=twentyfive>";
    final Component parsed = MiniMessage.builder()
      .editTags(b -> b.resolver(INSERT_VALUE_RESOLVER))
      .build()
      .deserialize(input);

    final String parsedString = PlainTextComponentSerializer.plainText().serialize(parsed);
    assertEquals("twentyfive", parsedString);
  }

  @Test
  void testWithColorNesting() {
    final String input = "<red>This is <blue><insert value=some></blue> text!";
    final Component expected = text()
      .color(RED)
      .append(text("This is "))
      .append(text("some", BLUE))
      .append(text(" text!"))
      .build();

    assertParsedEquals(MiniMessage.miniMessage(), expected, input, INSERT_VALUE_RESOLVER);
  }

  @Test
  void testMultipleArguments() {
    final String input = "<repeat amount=5 text='Hello, World'>";
    final Component expected = text("Hello, World Hello, World Hello, World Hello, World Hello, World");

    assertParsedEquals(MiniMessage.miniMessage(), expected, input, TagResolver.namedResolver("repeat",
      (args, ctx) -> {
        final int amount = args.isPresent("amount") ? args.get("amount").asInt().getAsInt() : 1;
        final String text = args.getOrThrow("text", "text is missing").value();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
          builder.append(text);
          if (i + 1 < amount) {
            builder.append(" ");
          }
        }
        return Tag.selfClosingInserting(text(builder.toString()));
      })
    );
  }

  @Test
  void testComplexArguments() {
    final String input = "This is <styled color=#ffaa00 bold italic>orange, bold, and italic styled</styled> text!";
    final Component expected = text()
      .append(text("This is "))
      .append(text("orange, bold, and italic styled", TextColor.color(0xffaa00), BOLD, ITALIC))
      .append(text(" text!"))
      .build();

    assertParsedEquals(MiniMessage.miniMessage(), expected, input, TagResolver.namedResolver("styled",
      (args, ctx) -> Tag.styling(builder -> {
        if (args.isPresent("color")) {
          builder.color(TextColor.fromCSSHexString(args.getOrThrow("color", "color is missing").value()));
        }

        if (args.isPresent("bold")) {
          builder.decorate(BOLD);
        }

        if (args.isPresent("italic")) {
          builder.decorate(ITALIC);
        }
      }))
    );
  }

  @Test
  void testWithExtraWhitespace() {
    final String input = "<insert                                  value=\"too much?\">";
    final Component expected = text("too much?");
    assertParsedEquals(MiniMessage.miniMessage(), expected, input, INSERT_VALUE_RESOLVER);
  }

  @Test
  void testWithQueuedAndExtraWhitespace() {
    final String input = "This <red > tag does not count, this <insert value='<red>'> does not either, but the <red>red one does!";
    final Component expected = text()
      .append(text("This <red > tag does not count, this <red> does not either, but the "))
      .append(text("red one does!", RED))
      .build();
    assertParsedEquals(MiniMessage
      .builder()
      .debug(System.out::print)
      .build(), expected, input, INSERT_VALUE_RESOLVER);
  }
}
