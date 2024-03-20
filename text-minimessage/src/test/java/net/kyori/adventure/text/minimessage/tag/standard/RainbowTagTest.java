/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.standard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.AbstractTest;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.format.NamedTextColor.BLACK;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RainbowTagTest extends AbstractTest {
  @Test
  void testSerializeRainbow() {
    final String expected = "<rainbow>test</rainbow> >> reeeeeeeee";

    final Component parsed = MiniMessage.miniMessage().deserialize(expected);

    final String serialized = MiniMessage.miniMessage().serialize(parsed);
    final Component reparsed = MiniMessage.miniMessage().deserialize(serialized);

    assertEquals(this.prettyPrint(parsed), this.prettyPrint(reparsed));
  }

  @Test
  void testRainbow() {
    final String input = "<yellow>Woo: <rainbow>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", color(0xff0000)),
        text("|", color(0xff3f00)),
        text("|", color(0xff7f00)),
        text("|", color(0xffbf00)),
        text("|", color(0xffff00)),
        text("|", color(0xbfff00)),
        text("|", color(0x7fff00)),
        text("|", color(0x3fff00)),
        text("|", color(0x00ff00)),
        text("|", color(0x00ff3f)),
        text("|", color(0x00ff7f)),
        text("|", color(0x00ffbf)),
        text("|", color(0x00ffff)),
        text("|", color(0x00bfff)),
        text("|", color(0x007fff)),
        text("|", color(0x003fff)),
        text("|", color(0x0000ff)),
        text("|", color(0x3f00ff)),
        text("|", color(0x7f00ff)),
        text("|", color(0xbf00ff)),
        text("|", color(0xff00ff)),
        text("|", color(0xff00bf)),
        text("|", color(0xff007f)),
        text("|", color(0xff003f))
      )))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowBackwards() {
    final String input = "<yellow>Woo: <rainbow:!>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", color(0xff003f)),
        text("|", color(0xff007f)),
        text("|", color(0xff00bf)),
        text("|", color(0xff00ff)),
        text("|", color(0xbf00ff)),
        text("|", color(0x7f00ff)),
        text("|", color(0x3f00ff)),
        text("|", color(0x0000ff)),
        text("|", color(0x003fff)),
        text("|", color(0x007fff)),
        text("|", color(0x00bfff)),
        text("|", color(0x00ffff)),
        text("|", color(0x00ffbf)),
        text("|", color(0x00ff7f)),
        text("|", color(0x00ff3f)),
        text("|", color(0x00ff00)),
        text("|", color(0x3fff00)),
        text("|", color(0x7fff00)),
        text("|", color(0xbfff00)),
        text("|", color(0xffff00)),
        text("|", color(0xffbf00)),
        text("|", color(0xff7f00)),
        text("|", color(0xff3f00)),
        text("|", color(0xff0000))
      )))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowPhase() {
    final String input = "<yellow>Woo: <rainbow:2>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", color(0xcbff00)),
        text("|", color(0x8cff00)),
        text("|", color(0x4cff00)),
        text("|", color(0x0cff00)),
        text("|", color(0x00ff33)),
        text("|", color(0x00ff72)),
        text("|", color(0x00ffb2)),
        text("|", color(0x00fff2)),
        text("|", color(0x00cbff)),
        text("|", color(0x008cff)),
        text("|", color(0x004cff)),
        text("|", color(0x000cff)),
        text("|", color(0x3200ff)),
        text("|", color(0x7200ff)),
        text("|", color(0xb200ff)),
        text("|", color(0xf200ff)),
        text("|", color(0xff00cc)),
        text("|", color(0xff008c)),
        text("|", color(0xff004c)),
        text("|", color(0xff000c)),
        text("|", color(0xff3200)),
        text("|", color(0xff7200)),
        text("|", color(0xffb200)),
        text("|", color(0xfff200))
      )))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowPhaseBackwards() {
    final String input = "<yellow>Woo: <rainbow:!2>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", color(0xfff200)),
        text("|", color(0xffb200)),
        text("|", color(0xff7200)),
        text("|", color(0xff3200)),
        text("|", color(0xff000c)),
        text("|", color(0xff004c)),
        text("|", color(0xff008c)),
        text("|", color(0xff00cc)),
        text("|", color(0xf200ff)),
        text("|", color(0xb200ff)),
        text("|", color(0x7200ff)),
        text("|", color(0x3200ff)),
        text("|", color(0x000cff)),
        text("|", color(0x004cff)),
        text("|", color(0x008cff)),
        text("|", color(0x00cbff)),
        text("|", color(0x00fff2)),
        text("|", color(0x00ffb2)),
        text("|", color(0x00ff72)),
        text("|", color(0x00ff33)),
        text("|", color(0x0cff00)),
        text("|", color(0x4cff00)),
        text("|", color(0x8cff00)),
        text("|", color(0xcbff00))
      )))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowWithInsertion() {
    final String input = "<yellow>Woo: <insert:test><rainbow>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty().insertion("test")
        .append(virtualOfChildren(textOfChildren(
          text("|", color(0xff0000)),
          text("|", color(0xff3f00)),
          text("|", color(0xff7f00)),
          text("|", color(0xffbf00)),
          text("|", color(0xffff00)),
          text("|", color(0xbfff00)),
          text("|", color(0x7fff00)),
          text("|", color(0x3fff00)),
          text("|", color(0x00ff00)),
          text("|", color(0x00ff3f)),
          text("|", color(0x00ff7f)),
          text("|", color(0x00ffbf)),
          text("|", color(0x00ffff)),
          text("|", color(0x00bfff)),
          text("|", color(0x007fff)),
          text("|", color(0x003fff)),
          text("|", color(0x0000ff)),
          text("|", color(0x3f00ff)),
          text("|", color(0x7f00ff)),
          text("|", color(0xbf00ff)),
          text("|", color(0xff00ff)),
          text("|", color(0xff00bf)),
          text("|", color(0xff007f)),
          text("|", color(0xff003f))
        )))
        .append(text("!"))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowBackwardsWithInsertion() {
    final String input = "<yellow>Woo: <insert:test><rainbow:!>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty().insertion("test")
        .append(virtualOfChildren(textOfChildren(
          text("|", color(0xff003f)),
          text("|", color(0xff007f)),
          text("|", color(0xff00bf)),
          text("|", color(0xff00ff)),
          text("|", color(0xbf00ff)),
          text("|", color(0x7f00ff)),
          text("|", color(0x3f00ff)),
          text("|", color(0x0000ff)),
          text("|", color(0x003fff)),
          text("|", color(0x007fff)),
          text("|", color(0x00bfff)),
          text("|", color(0x00ffff)),
          text("|", color(0x00ffbf)),
          text("|", color(0x00ff7f)),
          text("|", color(0x00ff3f)),
          text("|", color(0x00ff00)),
          text("|", color(0x3fff00)),
          text("|", color(0x7fff00)),
          text("|", color(0xbfff00)),
          text("|", color(0xffff00)),
          text("|", color(0xffbf00)),
          text("|", color(0xff7f00)),
          text("|", color(0xff3f00)),
          text("|", color(0xff0000))
        )))
        .append(text("!"))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowWithInnerClick() {
    final String input = "Rainbow: <rainbow><click:open_url:'https://github.com'>GH</click></rainbow>";
    final Component expected = text("Rainbow: ")
      .append(virtualOfChildren(
        empty()
          .clickEvent(openUrl("https://github.com"))
          .append(text("G").color(color(0xff0000)))
          .append(text("H").color(color(0x00ffff)))
      ));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowBackwardsWithInnerClick() {
    final String input = "Rainbow: <rainbow:!0><click:open_url:'https://github.com'>GH</click></rainbow>";
    final Component expected = text("Rainbow: ")
      .append(virtualOfChildren(
        empty()
          .clickEvent(openUrl("https://github.com"))
          .append(text("G").color(color(0x00ffff)))
          .append(text("H").color(color(0xff0000)))
      ));

    this.assertParsedEquals(expected, input);
  }

  // GH-125
  @Test
  void testNoRepeatedTextAfterUnclosedRainbow() {
    final Component expected = virtualOfChildren(
      text()
        .append(text('r', color(0xff0000)))
        .append(text('a', color(0xff7500)))
        .append(text('i', color(0xffeb00)))
        .append(text('n', color(0x9cff00)))
        .append(text('b', color(0x27ff00)))
        .append(text('o', color(0x00ff4e)))
        .append(text('w', color(0x00ffc4))),
      text("yellow", YELLOW)
    );
    final String input = "<rainbow>rainbow<yellow>yellow";

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowOrGradientContinuesAfterColoredInner() {
    final Component expectedRainbow = virtualOfChildren(
      text()
        .append(text('r', color(0xff0000)))
        .append(text('a', color(0xff7f00)))
        .append(text('i', color(0xffff00)))
        .append(text('n', color(0x7fff00))),
      text("white", WHITE),
      text()
        .append(text('b', color(0x7f00ff)))
        .append(text('o', color(0xff00ff)))
        .append(text('w', color(0xff007f)))
    );
    final String rainbowInput = "<rainbow>rain<white>white</white>bow";

    this.assertParsedEquals(expectedRainbow, rainbowInput);

    final Component expectedGradient = virtualOfChildren(
      textOfChildren(
        text('g', WHITE),
        text('r', color(0xeaeaea)),
        text('a', color(0xd5d5d5)),
        text('d', color(0xbfbfbf))
      ),
      text("green", GREEN),
      textOfChildren(
        text('i', color(0x404040)),
        text('e', color(0x2b2b2b)),
        text('n', color(0x151515)),
        text('t', BLACK)
      )
    );

    final String gradientInput = "<gradient>grad<green>green</green>ient";

    this.assertParsedEquals(expectedGradient, gradientInput);
  }

  @Test
  void gh147() {
    final String input = "<rainbow><msg>";
    final Component expected1 = virtualOfChildren(text().append(text("y", color(0xff0000)), text("o", color(0x00ffff))).build());
    this.assertParsedEquals(expected1, input, component("msg", text("yo")));
  }

  // https://github.com/KyoriPowered/adventure/issues/1040
  @Test
  void gh1040() {
    final String input = "<rainbow:16777215>||||||||||";
    final Component expected = textOfChildren(
      text("|", color(0x00ffff)),
      text("|", color(0x0065ff)),
      text("|", color(0x3200ff)),
      text("|", color(0xcc00ff)),
      text("|", color(0xff0099)),
      text("|", color(0xff0000)),
      text("|", color(0xff9900)),
      text("|", color(0xccff00)),
      text("|", color(0x32ff00)),
      text("|", color(0x00ff65))
    );

    this.assertParsedEquals(expected, input);
  }
}
