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
package net.kyori.adventure.text.minimessage.tag.standard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.AbstractTest;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
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
      .append(empty()
        .append(text("|", color(0xff0000)))
        .append(text("|", color(0xff3f00)))
        .append(text("|", color(0xff7f00)))
        .append(text("|", color(0xffbf00)))
        .append(text("|", color(0xffff00)))
        .append(text("|", color(0xbfff00)))
        .append(text("|", color(0x7fff00)))
        .append(text("|", color(0x3fff00)))
        .append(text("|", color(0x00ff00)))
        .append(text("|", color(0x00ff3f)))
        .append(text("|", color(0x00ff7f)))
        .append(text("|", color(0x00ffbf)))
        .append(text("|", color(0x00ffff)))
        .append(text("|", color(0x00bfff)))
        .append(text("|", color(0x007fff)))
        .append(text("|", color(0x003fff)))
        .append(text("|", color(0x0000ff)))
        .append(text("|", color(0x3f00ff)))
        .append(text("|", color(0x7f00ff)))
        .append(text("|", color(0xbf00ff)))
        .append(text("|", color(0xff00ff)))
        .append(text("|", color(0xff00bf)))
        .append(text("|", color(0xff007f)))
        .append(text("|", color(0xff003f)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowBackwards() {
    final String input = "<yellow>Woo: <rainbow:!>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0xff003f)))
        .append(text("|", color(0xff007f)))
        .append(text("|", color(0xff00bf)))
        .append(text("|", color(0xff00ff)))
        .append(text("|", color(0xbf00ff)))
        .append(text("|", color(0x7f00ff)))
        .append(text("|", color(0x3f00ff)))
        .append(text("|", color(0x0000ff)))
        .append(text("|", color(0x003fff)))
        .append(text("|", color(0x007fff)))
        .append(text("|", color(0x00bfff)))
        .append(text("|", color(0x00ffff)))
        .append(text("|", color(0x00ffbf)))
        .append(text("|", color(0x00ff7f)))
        .append(text("|", color(0x00ff3f)))
        .append(text("|", color(0x00ff00)))
        .append(text("|", color(0x3fff00)))
        .append(text("|", color(0x7fff00)))
        .append(text("|", color(0xbfff00)))
        .append(text("|", color(0xffff00)))
        .append(text("|", color(0xffbf00)))
        .append(text("|", color(0xff7f00)))
        .append(text("|", color(0xff3f00)))
        .append(text("|", color(0xff0000)))
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
        .append(text("|", color(0xcbff00)))
        .append(text("|", color(0x8cff00)))
        .append(text("|", color(0x4cff00)))
        .append(text("|", color(0x0cff00)))
        .append(text("|", color(0x00ff33)))
        .append(text("|", color(0x00ff72)))
        .append(text("|", color(0x00ffb2)))
        .append(text("|", color(0x00fff2)))
        .append(text("|", color(0x00cbff)))
        .append(text("|", color(0x008cff)))
        .append(text("|", color(0x004cff)))
        .append(text("|", color(0x000cff)))
        .append(text("|", color(0x3200ff)))
        .append(text("|", color(0x7200ff)))
        .append(text("|", color(0xb200ff)))
        .append(text("|", color(0xf200ff)))
        .append(text("|", color(0xff00cc)))
        .append(text("|", color(0xff008c)))
        .append(text("|", color(0xff004c)))
        .append(text("|", color(0xff000c)))
        .append(text("|", color(0xff3200)))
        .append(text("|", color(0xff7200)))
        .append(text("|", color(0xffb200)))
        .append(text("|", color(0xfff200)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowPhaseBackwards() {
    final String input = "<yellow>Woo: <rainbow:!2>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0xfff200)))
        .append(text("|", color(0xffb200)))
        .append(text("|", color(0xff7200)))
        .append(text("|", color(0xff3200)))
        .append(text("|", color(0xff000c)))
        .append(text("|", color(0xff004c)))
        .append(text("|", color(0xff008c)))
        .append(text("|", color(0xff00cc)))
        .append(text("|", color(0xf200ff)))
        .append(text("|", color(0xb200ff)))
        .append(text("|", color(0x7200ff)))
        .append(text("|", color(0x3200ff)))
        .append(text("|", color(0x000cff)))
        .append(text("|", color(0x004cff)))
        .append(text("|", color(0x008cff)))
        .append(text("|", color(0x00cbff)))
        .append(text("|", color(0x00fff2)))
        .append(text("|", color(0x00ffb2)))
        .append(text("|", color(0x00ff72)))
        .append(text("|", color(0x00ff33)))
        .append(text("|", color(0x0cff00)))
        .append(text("|", color(0x4cff00)))
        .append(text("|", color(0x8cff00)))
        .append(text("|", color(0xcbff00)))
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
          .append(text("|", color(0xff0000)))
          .append(text("|", color(0xff3f00)))
          .append(text("|", color(0xff7f00)))
          .append(text("|", color(0xffbf00)))
          .append(text("|", color(0xffff00)))
          .append(text("|", color(0xbfff00)))
          .append(text("|", color(0x7fff00)))
          .append(text("|", color(0x3fff00)))
          .append(text("|", color(0x00ff00)))
          .append(text("|", color(0x00ff3f)))
          .append(text("|", color(0x00ff7f)))
          .append(text("|", color(0x00ffbf)))
          .append(text("|", color(0x00ffff)))
          .append(text("|", color(0x00bfff)))
          .append(text("|", color(0x007fff)))
          .append(text("|", color(0x003fff)))
          .append(text("|", color(0x0000ff)))
          .append(text("|", color(0x3f00ff)))
          .append(text("|", color(0x7f00ff)))
          .append(text("|", color(0xbf00ff)))
          .append(text("|", color(0xff00ff)))
          .append(text("|", color(0xff00bf)))
          .append(text("|", color(0xff007f)))
          .append(text("|", color(0xff003f)))
        ).append(text("!"))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowBackwardsWithInsertion() {
    final String input = "<yellow>Woo: <insert:test><rainbow:!>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty().insertion("test")
        .append(empty()
          .append(text("|", color(0xff003f)))
          .append(text("|", color(0xff007f)))
          .append(text("|", color(0xff00bf)))
          .append(text("|", color(0xff00ff)))
          .append(text("|", color(0xbf00ff)))
          .append(text("|", color(0x7f00ff)))
          .append(text("|", color(0x3f00ff)))
          .append(text("|", color(0x0000ff)))
          .append(text("|", color(0x003fff)))
          .append(text("|", color(0x007fff)))
          .append(text("|", color(0x00bfff)))
          .append(text("|", color(0x00ffff)))
          .append(text("|", color(0x00ffbf)))
          .append(text("|", color(0x00ff7f)))
          .append(text("|", color(0x00ff3f)))
          .append(text("|", color(0x00ff00)))
          .append(text("|", color(0x3fff00)))
          .append(text("|", color(0x7fff00)))
          .append(text("|", color(0xbfff00)))
          .append(text("|", color(0xffff00)))
          .append(text("|", color(0xffbf00)))
          .append(text("|", color(0xff7f00)))
          .append(text("|", color(0xff3f00)))
          .append(text("|", color(0xff0000)))
        ).append(text("!"))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowWithInnerClick() {
    final String input = "Rainbow: <rainbow><click:open_url:'https://github.com'>GH</click></rainbow>";
    final Component expected = text("Rainbow: ")
      .append(empty().clickEvent(openUrl("https://github.com"))
        .append(text("G").color(color(0xff0000)))
        .append(text("H").color(color(0x00ffff)))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowBackwardsWithInnerClick() {
    final String input = "Rainbow: <rainbow:!0><click:open_url:'https://github.com'>GH</click></rainbow>";
    final Component expected = text("Rainbow: ")
      .append(empty().clickEvent(openUrl("https://github.com"))
        .append(text("G").color(color(0x00ffff)))
        .append(text("H").color(color(0xff0000)))
      );

    this.assertParsedEquals(expected, input);
  }

  // GH-125
  @Test
  void testNoRepeatedTextAfterUnclosedRainbow() {
    final Component expected = text()
      .append(text('r', color(0xff0000)))
      .append(text('a', color(0xff7500)))
      .append(text('i', color(0xffeb00)))
      .append(text('n', color(0x9cff00)))
      .append(text('b', color(0x27ff00)))
      .append(text('o', color(0x00ff4e)))
      .append(text('w', color(0x00ffc4)))
      .append(text("yellow", YELLOW))
      .build();
    final String input = "<rainbow>rainbow<yellow>yellow";

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowOrGradientContinuesAfterColoredInner() {
    final Component expectedRainbow = text()
      .append(text('r', color(0xff0000)))
      .append(text('a', color(0xff7f00)))
      .append(text('i', color(0xffff00)))
      .append(text('n', color(0x7fff00)))
      .append(text("white", WHITE))
      .append(text()
        .append(text('b', color(0x7f00ff)))
        .append(text('o', color(0xff00ff)))
        .append(text('w', color(0xff007f))))
      .build();
    final String rainbowInput = "<rainbow>rain<white>white</white>bow";

    this.assertParsedEquals(expectedRainbow, rainbowInput);

    final Component expectedGradient = text()
      .append(text('g', WHITE))
      .append(text('r', color(0xeaeaea)))
      .append(text('a', color(0xd5d5d5)))
      .append(text('d', color(0xbfbfbf)))
      .append(text("green", GREEN))
      .append(text()
        .append(text('i', color(0x404040)))
        .append(text('e', color(0x2b2b2b)))
        .append(text('n', color(0x151515)))
        .append(text('t', BLACK)))
      .build();
    final String gradientInput = "<gradient>grad<green>green</green>ient";

    this.assertParsedEquals(expectedGradient, gradientInput);
  }

  @Test
  void gh147() {
    final String input = "<rainbow><msg>";
    final Component expected1 = text().append(text("y", color(0xff0000)), text("o", color(0x00ffff))).build();
    this.assertParsedEquals(expected1, input, component("msg", text("yo")));
  }
}
