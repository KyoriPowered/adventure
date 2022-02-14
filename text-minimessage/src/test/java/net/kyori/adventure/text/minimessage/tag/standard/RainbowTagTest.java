/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;

class RainbowTagTest extends AbstractTest {

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
  void testRainbowBackwards() {
    final String input = "<yellow>Woo: <rainbow:!>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0xfd5f38)))
        .append(text("|", color(0xfe4056)))
        .append(text("|", color(0xf72676)))
        .append(text("|", color(0xe71297)))
        .append(text("|", color(0xd005b7)))
        .append(text("|", color(0xb401d3)))
        .append(text("|", color(0x9505e9)))
        .append(text("|", color(0x7412f7)))
        .append(text("|", color(0x5326fe)))
        .append(text("|", color(0x3640fc)))
        .append(text("|", color(0x1e5ff2)))
        .append(text("|", color(0x0c80e0)))
        .append(text("|", color(0x02a0c7)))
        .append(text("|", color(0x01bfa9)))
        .append(text("|", color(0x08d989)))
        .append(text("|", color(0x18ed68)))
        .append(text("|", color(0x2ffa48)))
        .append(text("|", color(0x4bff2c)))
        .append(text("|", color(0x6afa16)))
        .append(text("|", color(0x8bed08)))
        .append(text("|", color(0xacd901)))
        .append(text("|", color(0xc9bf03)))
        .append(text("|", color(0xe1a00d)))
        .append(text("|", color(0xf3801f)))
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
  void testRainbowPhaseBackwards() {
    final String input = "<yellow>Woo: <rainbow:!2>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
        .append(text("|", color(0x38fd3e)))
        .append(text("|", color(0x56fe24)))
        .append(text("|", color(0x76f710)))
        .append(text("|", color(0x97e704)))
        .append(text("|", color(0xb7d001)))
        .append(text("|", color(0xd3b406)))
        .append(text("|", color(0xe99513)))
        .append(text("|", color(0xf77428)))
        .append(text("|", color(0xfe5342)))
        .append(text("|", color(0xfc3661)))
        .append(text("|", color(0xf21e82)))
        .append(text("|", color(0xe00ca3)))
        .append(text("|", color(0xc702c1)))
        .append(text("|", color(0xa901db)))
        .append(text("|", color(0x8908ef)))
        .append(text("|", color(0x6818fb)))
        .append(text("|", color(0x482ffe)))
        .append(text("|", color(0x2c4bf9)))
        .append(text("|", color(0x166aec)))
        .append(text("|", color(0x088bd7)))
        .append(text("|", color(0x01acbd)))
        .append(text("|", color(0x03c99e)))
        .append(text("|", color(0x0de17d)))
        .append(text("|", color(0x1ff35c)))
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
  void testRainbowBackwardsWithInsertion() {
    final String input = "<yellow>Woo: <insert:test><rainbow:!>||||||||||||||||||||||||</rainbow>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty().insertion("test")
        .append(empty()
          .append(text("|", color(0xfd5f38)))
          .append(text("|", color(0xfe4056)))
          .append(text("|", color(0xf72676)))
          .append(text("|", color(0xe71297)))
          .append(text("|", color(0xd005b7)))
          .append(text("|", color(0xb401d3)))
          .append(text("|", color(0x9505e9)))
          .append(text("|", color(0x7412f7)))
          .append(text("|", color(0x5326fe)))
          .append(text("|", color(0x3640fc)))
          .append(text("|", color(0x1e5ff2)))
          .append(text("|", color(0x0c80e0)))
          .append(text("|", color(0x02a0c7)))
          .append(text("|", color(0x01bfa9)))
          .append(text("|", color(0x08d989)))
          .append(text("|", color(0x18ed68)))
          .append(text("|", color(0x2ffa48)))
          .append(text("|", color(0x4bff2c)))
          .append(text("|", color(0x6afa16)))
          .append(text("|", color(0x8bed08)))
          .append(text("|", color(0xacd901)))
          .append(text("|", color(0xc9bf03)))
          .append(text("|", color(0xe1a00d)))
          .append(text("|", color(0xf3801f)))
        ).append(text("!"))
      );

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
  void testRainbowBackwardsWithInnerClick() {
    final String input = "Rainbow: <rainbow:!0><click:open_url:'https://github.com'>GH</click></rainbow>";
    final Component expected = text("Rainbow: ")
      .append(empty().clickEvent(openUrl("https://github.com"))
        .append(text("G").color(color(0x0c80e0)))
        .append(text("H").color(color(0xf3801f)))
      );

    this.assertParsedEquals(expected, input);
  }

  // GH-125
  @Test
  void testNoRepeatedTextAfterUnclosedRainbow() {
    final Component expected = text()
      .append(text('r', color(0xf3801f)))
      .append(text('a', color(0xcdbb04)))
      .append(text('i', color(0x96e805)))
      .append(text('n', color(0x59fe22)))
      .append(text('b', color(0x25f654)))
      .append(text('o', color(0x06d490)))
      .append(text('w', color(0x039ec9)))
      .append(text("yellow", YELLOW))
      .build();
    final String input = "<rainbow>rainbow<yellow>yellow";

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testRainbowOrGradientContinuesAfterColoredInner() {
    final Component expectedRainbow = text()
      .append(text('r', color(0xf3801f)))
      .append(text('a', color(0xc9bf03)))
      .append(text('i', color(0x8bed08)))
      .append(text('n', color(0x4bff2c)))
      .append(text("white", WHITE))
      .append(text()
        .append(text('b', color(0xb401d3)))
        .append(text('o', color(0xe71297)))
        .append(text('w', color(0xfe4056))))
      .build();
    final String rainbowInput = "<rainbow>rain<white>white</white>bow";

    this.assertParsedEquals(expectedRainbow, rainbowInput);

    final Component expectedGradient = text()
      .append(text('g', WHITE))
      .append(text('r', color(0xebebeb)))
      .append(text('a', color(0xd8d8d8)))
      .append(text('d', color(0xc4c4c4)))
      .append(text("green", GREEN))
      .append(text()
        .append(text('i', color(0x4e4e4e)))
        .append(text('e', color(0x3b3b3b)))
        .append(text('n', color(0x272727)))
        .append(text('t', color(0x141414))))
      .build();
    final String gradientInput = "<gradient>grad<green>green</green>ient";

    this.assertParsedEquals(expectedGradient, gradientInput);
  }

  @Test
  void gh137() {
    final String input = "<gradient:gold:yellow:red><dum>";
    final String input2 = "<gradient:gold:yellow:red><dum>a";
    final Component expected1 = text("a", GOLD);
    final Component expected2 = text().append(text("a", GOLD), text("a", YELLOW)).build();
    final Component expected3 = text().append(text("a", GOLD), text("a", YELLOW), text("a", YELLOW)).build();
    final Component expected4 = text().append(text("a", GOLD), text("a", TextColor.color(0xffd52b)), text("a", YELLOW), text("a", YELLOW)).build();

    this.assertParsedEquals(expected1, input, component("dum", text("a")));
    this.assertParsedEquals(expected2, input, component("dum", text("aa")));
    this.assertParsedEquals(expected3, input, component("dum", text("aaa")));
    this.assertParsedEquals(expected4, input, component("dum", text("aaaa")));
    this.assertParsedEquals(expected4, input2, component("dum", text("aaa")));
  }

  @Test
  void gh147() {
    final String input = "<rainbow><msg>";
    final Component expected1 = text().append(text("y", color(0xf3801f)), text("o", color(0x0c80e0))).build();
    this.assertParsedEquals(expected1, input, component("msg", text("yo")));
  }
}
