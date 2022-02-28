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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.BLACK;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GradientTagTest extends AbstractTest {

  @Test
  void testGradient() {
    final String input = "<yellow>Woo: <gradient>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
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
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientWithHover() {
    final String input = "<yellow>Woo: <hover:show_text:'This is a test'><gradient>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty().hoverEvent(showText(text("This is a test")))
        .append(empty()
          .append(text("|", style(WHITE)))
          .append(text("|", style(color(0xf4f4f4))))
          .append(text("|", style(color(0xeaeaea))))
          .append(text("|", style(color(0xdfdfdf))))
          .append(text("|", style(color(0xd5d5d5))))
          .append(text("|", style(color(0xcacaca))))
          .append(text("|", style(color(0xbfbfbf))))
          .append(text("|", style(color(0xb5b5b5))))
          .append(text("|", style(GRAY)))
          .append(text("|", style(color(0x9f9f9f))))
          .append(text("|", style(color(0x959595))))
          .append(text("|", style(color(0x8a8a8a))))
          .append(text("|", style(color(0x808080))))
          .append(text("|", style(color(0x757575))))
          .append(text("|", style(color(0x6a6a6a))))
          .append(text("|", style(color(0x606060))))
          .append(text("|", style(DARK_GRAY)))
          .append(text("|", style(color(0x4a4a4a))))
          .append(text("|", style(color(0x404040))))
          .append(text("|", style(color(0x353535))))
          .append(text("|", style(color(0x2a2a2a))))
          .append(text("|", style(color(0x202020))))
          .append(text("|", style(color(0x151515))))
          .append(text("|", style(color(0x0b0b0b))))
        ).append(text("!")));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradient2() {
    final String input = "<yellow>Woo: <gradient:#5e4fa2:#f79459>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
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
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradient3() {
    final String input = "<yellow>Woo: <gradient:green:blue>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
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
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor() {
    final String input = "<yellow>Woo: <gradient:red:blue:green:yellow:red>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
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
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor2() {
    final String input = "<yellow>Woo: <gradient:black:white:black>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
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
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor2Phase() {
    final String input = "<yellow>Woo: <gradient:black:white:black:-0.65>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
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
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientPhase() {
    final String input = "<yellow>Woo: <gradient:green:blue:0.7>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty()
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
      ).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  // see #91
  @Test
  void testGradientWithInnerTokens() {
    final String input = "<gradient:green:blue>123<bold>456</gradient>!";
    final Component expected = empty()
      .append(text("1", GREEN))
      .append(text("2", color(0x55e371)))
      .append(text("3", color(0x55c68e)))
      .append(empty().decorate(BOLD)
        .append(text("4", color(0x55aaaa)))
        .append(text("5", color(0x558ec6)))
        .append(text("6", color(0x5571e3)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientWithInnerGradientWithInnerToken() {
    final String input = "<gradient:green:blue>123<gradient:red:yellow>456<bold>789</gradient>abc</gradient>!";
    final Component expected = empty()
      .append(text("1", GREEN))
      .append(text("2", color(0x55f163)))
      .append(text("3", color(0x55e371)))
      .append(empty()
        .append(text("4", RED))
        .append(text("5", color(0xff7155)))
        .append(text("6", color(0xff8e55)))
        .append(empty().decorate(BOLD)
          .append(text("7", color(0xffaa55)))
          .append(text("8", color(0xffc655)))
          .append(text("9", color(0xffe355)))
        )
      )
      .append(empty()
        .append(text("a", color(0x5580d5)))
        .append(text("b", color(0x5571e3)))
        .append(text("c", color(0x5563f1)))
      )
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testNonBmpCharactersInGradient() {
    assertFalse(Character.isBmpCodePoint("𐌰".codePointAt(0)));

    final String input = "Something <gradient:green:blue:1.0>𐌰𐌱𐌲</gradient>";
    final Component expected = text("Something ")
      .append(empty()
        .append(text("𐌰", BLUE))
        .append(text("𐌱", color(0x558ec6)))
        .append(text("𐌲", color(0x55c68e)))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testSingleCharGradient() {
    final String input1 = "<gradient:red:blue:green>A";
    final String input2 = "<gradient:red:blue:green:red>AB";

    final Component expected1 = text("A", RED);
    final Component expected2 = text().append(text("A", RED), text("B", BLUE)).build();

    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
  }

  // https://github.com/KyoriPowered/adventure/issues/510
  @Test
  void testNestedGradientsDontOverrideColors() {
    final String input = "<gradient:#1985ff:#2bc7ff>a<gradient:#00fffb:#00ffc3>b</gradient> <gray>gray</gray></gradient>";

    final Component expected = Component.text()
      .append(
        text("a", color(0x1985ff)),
        text("b", color(0x00fffb)),
        text(" ", color(0x1e98ff)),
        text("gray", NamedTextColor.GRAY)
      )
      .build();

    this.assertParsedEquals(expected, input);
  }

  // https://github.com/KyoriPowered/adventure/issues/510
  @Test
  void testNestedGradientsReallyDontOverrideColors() {
    final String input = "<gradient:white:blue>A <gradient:yellow:black>B <white>C";

    final Component expected = Component.textOfChildren(
        text("A", NamedTextColor.WHITE),
        text(" ", color(0xddddff)),
        Component.textOfChildren(
          text("B", NamedTextColor.YELLOW),
          text(" ", color(0xaaaa39)),
          text("C", NamedTextColor.WHITE)
        )
      );

    this.assertParsedEquals(expected, input);
  }
}
