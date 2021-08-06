/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text;

import java.util.regex.Pattern;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextReplacementRendererTest {
  @Test
  void testReplace() {
    final TextComponent component = Component.text()
      .content("cat says ")
      .append(Component.translatable("cat.meow")) // or any non-text component
      .build();
    final Component replaced = component.replaceText(b -> b.match("says").replacement(match -> match.color(NamedTextColor.DARK_PURPLE)));
    assertEquals(Component.text()
      .content("cat ")
      .append(Component.text("says", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" "))
      .append(Component.translatable("cat.meow"))
      .build(), replaced);
  }

  @Test
  void testReplaceFirst() {
    final TextComponent component = Component.text()
      .content("Buffalo buffalo Buffalo buffalo buffalo buffalo Buffalo buffalo ")
      .append(Component.translatable("buffalo.buffalo")) // or any non-text component
      .build();
    final Component replaced = component.replaceText(b -> b.match("buffalo").once().replacement(match -> match.color(NamedTextColor.DARK_PURPLE)));
    assertEquals(Component.text()
      .content("Buffalo ")
      .append(Component.text("buffalo", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" Buffalo buffalo buffalo buffalo Buffalo buffalo "))
      .append(Component.translatable("buffalo.buffalo"))
      .build(), replaced);
  }

  @Test
  void testReplaceN() {
    final TextComponent component = Component.text()
      .content("Buffalo buffalo Buffalo buffalo buffalo buffalo Buffalo buffalo ")
      .append(Component.translatable("buffalo.buffalo")) // or any non-text component
      .build();
    final Component replaced = component.replaceText(b -> b.match("buffalo").replacement(match -> match.color(NamedTextColor.DARK_PURPLE)).times(2));
    assertEquals(Component.text()
      .content("Buffalo ")
      .append(Component.text("buffalo", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" Buffalo "))
      .append(Component.text("buffalo", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" buffalo buffalo Buffalo buffalo "))
      .append(Component.translatable("buffalo.buffalo"))
      .build(), replaced);
  }

  @Test
  void testReplaceEveryOther() {
    final TextComponent component = Component.text()
      .content("purple purple purple purple purple purple purple purple ")
      .append(Component.translatable("purple.purple")) // or any non-text component
      .build();

    final Component replaced = component.replaceText(b -> b.match("purple")
      .replacement(match -> match.color(NamedTextColor.DARK_PURPLE))
      .condition((index, replace) -> index % 2 == 0 ? PatternReplacementResult.REPLACE : PatternReplacementResult.CONTINUE));

    assertEquals(Component.text()
      .content("purple ")
      .append(Component.text("purple", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" purple "))
      .append(Component.text("purple", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" purple "))
      .append(Component.text("purple", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" purple "))
      .append(Component.text("purple", NamedTextColor.DARK_PURPLE))
      .append(Component.text(" "))
      .append(Component.translatable("purple.purple"))
      .build(), replaced);
  }

  @Test
  void testReplaceNonStringComponents() {
    final TextComponent original = Component.text().content("Hello world")
      .append(Component.text("Child 1"), Component.keybind("key.adventure.purr"), Component.text("Child 3"))
      .build();
    final TextComponent expectedReplacement = Component.text().content("Hello world")
      .append(Component.text().append(Component.text("Parent")).append(Component.text(" 1")), Component.keybind("key.adventure.purr"), Component.text().append(Component.text("Parent")).append(Component.text(" 3")))
      .build();

    assertEquals(expectedReplacement, original.replaceText(b -> b.match("Child").replacement(match -> match.content("Parent"))));
  }

  // https://github.com/KyoriPowered/adventure/issues/129
  @Test
  void testReplaceWithChildren() {
    final TextComponent component = Component.text()
      .append(Component.text("This ").color(NamedTextColor.GRAY))
      .append(Component.text("is").color(NamedTextColor.BLUE)
        .append(Component.text(" the ").decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)))
      .append(Component.text("string").color(NamedTextColor.DARK_BLUE))
      .append(Component.text(" under test").color(NamedTextColor.DARK_AQUA))
      .build();
    final TextComponent expectedReplacement = Component.text()
      .append(Component.text("This ").color(NamedTextColor.GRAY))
      .append(Component.text("is").color(NamedTextColor.BLUE)
        .append(Component.text(" the ").decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)))
      .append(Component.text("string").color(NamedTextColor.DARK_BLUE))
      .append(Component.text(" under ").color(NamedTextColor.DARK_AQUA).append(Component.text("me")))
      .build();

    assertEquals(expectedReplacement, component.replaceText(b -> b.match("test").replacement("me")));
  }

  @Test
  void testReplaceInStyle() {
    final TextComponent component = Component.text().content("Hover on me")
      .hoverEvent(HoverEvent.showText(Component.text("meep", NamedTextColor.DARK_AQUA)))
      .build();
    final Component expectedReplacement = Component.text().content("Hover on me")
      .hoverEvent(HoverEvent.showText(Component.text("meow", NamedTextColor.DARK_RED)))
      .build();

    assertEquals(expectedReplacement, component.replaceText(b -> b.match("meep").replacement(builder -> builder.content("meow").color(NamedTextColor.DARK_RED))));
  }

  @Test
  void testReplaceTranslatableArgs() {
    final TextComponent component = Component.text().content("hello ")
      .append(Component.translatable("my.translation", Component.text("cats bad")))
      .build();
    final TextComponent expectedReplacement = Component.text().content("hello ")
      .append(Component.translatable("my.translation", Component.text().content("cats ").append(Component.text("good"))))
      .build();

    assertEquals(expectedReplacement, component.replaceText(b -> b.match(Pattern.compile("bad")).replacement(builder -> builder.content("good"))));
  }

  @Test
  void testPartialReplaceHasIsolatedStyle() {
    final TextComponent component = Component.text("Hello world", NamedTextColor.DARK_PURPLE);
    final TextComponent expectedReplacement = Component.text(b ->
      b.color(NamedTextColor.DARK_PURPLE)
        .append(Component.text("Goodbye", NamedTextColor.LIGHT_PURPLE))
        .append(Component.text(" world")));

    assertEquals(expectedReplacement, component.replaceText(b -> b.match(Pattern.compile("Hello")).replacement(builder -> builder.content("Goodbye").color(NamedTextColor.LIGHT_PURPLE))));
  }

  // https://github.com/KyoriPowered/adventure/issues/197
  @Test
  void testReplaceSameInComponentAndEvent() {
    final Component original = Component.text("/{0}", NamedTextColor.RED)
      .hoverEvent(Component.text()
        .append(Component.text("Click to run"))
        .append(Component.space())
        .append(Component.text("/{0}", NamedTextColor.RED)).build());

    final Component expected = Component.text("/", NamedTextColor.RED)
      .append(Component.text("mycommand"))
      .hoverEvent(Component.text()
        .append(Component.text("Click to run"))
        .append(Component.space())
        .append(Component.text("/", NamedTextColor.RED).append(Component.text("mycommand"))).build());

    final Pattern replaceWith = Pattern.compile("\\{(\\d+)}");

    final Component replaced = original.replaceText(builder ->
      builder.match(replaceWith)
        .replacement((matcher, comp) -> {
          assertEquals(0, Integer.parseInt(matcher.group(1))); // only one index in our test case

          return Component.text("mycommand");
        }));

    assertEquals(expected, replaced);
  }

  // https://github.com/KyoriPowered/adventure/issues/232
  @Test
  void testReplaceAtStart() {
    final Component original = Component.text("value").append(Component.text("$", NamedTextColor.DARK_PURPLE));

    final Component expected = Component.text()
      .content("1.99")
      .append(Component.text("$", NamedTextColor.DARK_PURPLE))
      .build();

    assertEquals(expected, original.replaceText(c -> c.match("value").replacement("1.99")));
  }

  @Test
  void testReplaceAtStartReturnsChild() {
    final Component base = Component.text("value");

    final Component replaced = base.replaceText(c -> c.match("value")
      .replacement((matchResult, builder) -> Component.text("").append(Component.text("1337"))));

    final Component expected = Component.text()
      .append(Component.text("1337"))
      .build();
    assertEquals(expected, replaced);
  }

  @Test
  void testReplaceWithMatchResultCondition() {
    final Component base = Component.text("get the set the get the set the get the set");

    final Component replaced = base.replaceText(c -> c.match("[gs]et")
    .condition((result, count, replacements) -> result.group().equals("set") ? PatternReplacementResult.REPLACE : PatternReplacementResult.CONTINUE)
    .replacement("pet"));

    final Component expected = Component.text().content("get the ")
      .append(
        Component.text("pet"),
        Component.text(" the get the "),
        Component.text("pet"),
        Component.text(" the get the "),
        Component.text("pet")
      )
      .build();
    assertEquals(expected, replaced);
  }

  // https://github.com/KyoriPowered/adventure/issues/387
  @Test
  void testFullMatchReplaceWithStyle() {
    final Component base = Component.text("hello", NamedTextColor.RED);

    final Component replaced = base.replaceText(c -> c.matchLiteral("hello")
      .replacement(Component.text("world").decorate(TextDecoration.BOLD)));

    final Component expected = Component.text("world", NamedTextColor.RED, TextDecoration.BOLD);
    assertEquals(expected, replaced);
  }
}
