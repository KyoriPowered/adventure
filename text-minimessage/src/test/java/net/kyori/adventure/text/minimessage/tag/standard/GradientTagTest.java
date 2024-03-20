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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.AbstractTest;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.BLACK;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GradientTagTest extends AbstractTest {

  @Test
  void testGradient() {
    final String input = "<yellow>Woo: <gradient>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", WHITE),
        text("|", color(0xf4f4f4)),
        text("|", color(0xe9e9e9)),
        text("|", color(0xdedede)),
        text("|", color(0xd3d3d3)),
        text("|", color(0xc8c8c8)),
        text("|", color(0xbcbcbc)),
        text("|", color(0xb1b1b1)),
        text("|", color(0xa6a6a6)),
        text("|", color(0x9b9b9b)),
        text("|", color(0x909090)),
        text("|", color(0x858585)),
        text("|", color(0x7a7a7a)),
        text("|", color(0x6f6f6f)),
        text("|", color(0x646464)),
        text("|", color(0x595959)),
        text("|", color(0x4e4e4e)),
        text("|", color(0x434343)),
        text("|", color(0x373737)),
        text("|", color(0x2c2c2c)),
        text("|", color(0x212121)),
        text("|", color(0x161616)),
        text("|", color(0x0b0b0b)),
        text("|", BLACK)
      ))).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientWithHover() {
    final String input = "<yellow>Woo: <hover:show_text:'This is a test'><gradient>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(empty().hoverEvent(showText(text("This is a test")))
      .append(virtualOfChildren(textOfChildren(
          text("|", style(WHITE)),
          text("|", style(color(0xf4f4f4))),
          text("|", style(color(0xe9e9e9))),
          text("|", style(color(0xdedede))),
          text("|", style(color(0xd3d3d3))),
          text("|", style(color(0xc8c8c8))),
          text("|", style(color(0xbcbcbc))),
          text("|", style(color(0xb1b1b1))),
          text("|", style(color(0xa6a6a6))),
          text("|", style(color(0x9b9b9b))),
          text("|", style(color(0x909090))),
          text("|", style(color(0x858585))),
          text("|", style(color(0x7a7a7a))),
          text("|", style(color(0x6f6f6f))),
          text("|", style(color(0x646464))),
          text("|", style(color(0x595959))),
          text("|", style(color(0x4e4e4e))),
          text("|", style(color(0x434343))),
          text("|", style(color(0x373737))),
          text("|", style(color(0x2c2c2c))),
          text("|", style(color(0x212121))),
          text("|", style(color(0x161616))),
          text("|", style(color(0x0b0b0b))),
          text("|", style(BLACK))
        ))).append(text("!")));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradient2() {
    final String input = "<yellow>Woo: <gradient:#5e4fa2:#f79459>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", color(0x5e4fa2)),
        text("|", color(0x65529f)),
        text("|", color(0x6b559c)),
        text("|", color(0x725898)),
        text("|", color(0x795b95)),
        text("|", color(0x7f5e92)),
        text("|", color(0x86618f)),
        text("|", color(0x8d648c)),
        text("|", color(0x936789)),
        text("|", color(0x9a6a85)),
        text("|", color(0xa16d82)),
        text("|", color(0xa7707f)),
        text("|", color(0xae737c)),
        text("|", color(0xb47679)),
        text("|", color(0xbb7976)),
        text("|", color(0xc27c72)),
        text("|", color(0xc87f6f)),
        text("|", color(0xcf826c)),
        text("|", color(0xd68569)),
        text("|", color(0xdc8866)),
        text("|", color(0xe38b63)),
        text("|", color(0xea8e5f)),
        text("|", color(0xf0915c)),
        text("|", color(0xf79459))
      )))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradient3() {
    final String input = "<yellow>Woo: <gradient:green:blue>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", GREEN),
        text("|", color(0x55f85c)),
        text("|", color(0x55f064)),
        text("|", color(0x55e96b)),
        text("|", color(0x55e173)),
        text("|", color(0x55da7a)),
        text("|", color(0x55d381)),
        text("|", color(0x55cb89)),
        text("|", color(0x55c490)),
        text("|", color(0x55bc98)),
        text("|", color(0x55b59f)),
        text("|", color(0x55aea6)),
        text("|", color(0x55a6ae)),
        text("|", color(0x559fb5)),
        text("|", color(0x5598bc)),
        text("|", color(0x5590c4)),
        text("|", color(0x5589cb)),
        text("|", color(0x5581d3)),
        text("|", color(0x557ada)),
        text("|", color(0x5573e1)),
        text("|", color(0x556be9)),
        text("|", color(0x5564f0)),
        text("|", color(0x555cf8)),
        text("|", style(BLUE))
      )))
      .append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor() {
    final String input = "<yellow>Woo: <gradient:red:blue:green:yellow:red>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", RED),
        text("|", color(0xf25562)),
        text("|", color(0xe5556f)),
        text("|", color(0xd9557b)),
        text("|", color(0xcc5588)),
        text("|", color(0xbf5595)),
        text("|", color(0xb255a2)),
        text("|", color(0xa555af)),
        text("|", color(0x9855bc)),
        text("|", color(0x8c55c8)),
        text("|", color(0x7f55d5)),
        text("|", color(0x7255e2)),
        text("|", color(0x6555ef)),
        text("|", color(0x5855fc)),
        text("|", color(0x555ff5)),
        text("|", color(0x556be9)),
        text("|", color(0x5578dc)),
        text("|", color(0x5585cf)),
        text("|", color(0x5592c2)),
        text("|", color(0x559fb5)),
        text("|", color(0x55aca8)),
        text("|", color(0x55b89c)),
        text("|", color(0x55c58f)),
        text("|", color(0x55d282)),
        text("|", color(0x55df75)),
        text("|", color(0x55ec68)),
        text("|", color(0x55f95b)),
        text("|", color(0x5bff55)),
        text("|", color(0x68ff55)),
        text("|", color(0x75ff55)),
        text("|", color(0x82ff55)),
        text("|", color(0x8fff55)),
        text("|", color(0x9cff55)),
        text("|", color(0xa8ff55)),
        text("|", color(0xb5ff55)),
        text("|", color(0xc2ff55)),
        text("|", color(0xcfff55)),
        text("|", color(0xdcff55)),
        text("|", color(0xe9ff55)),
        text("|", color(0xf5ff55)),
        text("|", color(0xfffc55)),
        text("|", color(0xffef55)),
        text("|", color(0xffe255)),
        text("|", color(0xffd555)),
        text("|", color(0xffc855)),
        text("|", color(0xffbc55)),
        text("|", color(0xffaf55)),
        text("|", color(0xffa255)),
        text("|", color(0xff9555)),
        text("|", color(0xff8855)),
        text("|", color(0xff7b55)),
        text("|", color(0xff6f55)),
        text("|", color(0xff6255)),
        text("|", RED)
      ))).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor2() {
    final String input = "<yellow>Woo: <gradient:black:white:black>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", BLACK),
        text("|", color(0x0a0a0a)),
        text("|", color(0x131313)),
        text("|", color(0x1d1d1d)),
        text("|", color(0x262626)),
        text("|", color(0x303030)),
        text("|", color(0x3a3a3a)),
        text("|", color(0x434343)),
        text("|", color(0x4d4d4d)),
        text("|", color(0x575757)),
        text("|", color(0x606060)),
        text("|", color(0x6a6a6a)),
        text("|", color(0x737373)),
        text("|", color(0x7d7d7d)),
        text("|", color(0x878787)),
        text("|", color(0x909090)),
        text("|", color(0x9a9a9a)),
        text("|", color(0xa4a4a4)),
        text("|", color(0xadadad)),
        text("|", color(0xb7b7b7)),
        text("|", color(0xc0c0c0)),
        text("|", color(0xcacaca)),
        text("|", color(0xd4d4d4)),
        text("|", color(0xdddddd)),
        text("|", color(0xe7e7e7)),
        text("|", color(0xf1f1f1)),
        text("|", color(0xfafafa)),
        text("|", color(0xfafafa)),
        text("|", color(0xf1f1f1)),
        text("|", color(0xe7e7e7)),
        text("|", color(0xdddddd)),
        text("|", color(0xd4d4d4)),
        text("|", color(0xcacaca)),
        text("|", color(0xc0c0c0)),
        text("|", color(0xb7b7b7)),
        text("|", color(0xadadad)),
        text("|", color(0xa4a4a4)),
        text("|", color(0x9a9a9a)),
        text("|", color(0x909090)),
        text("|", color(0x878787)),
        text("|", color(0x7d7d7d)),
        text("|", color(0x737373)),
        text("|", color(0x6a6a6a)),
        text("|", color(0x606060)),
        text("|", color(0x575757)),
        text("|", color(0x4d4d4d)),
        text("|", color(0x434343)),
        text("|", color(0x3a3a3a)),
        text("|", color(0x303030)),
        text("|", color(0x262626)),
        text("|", color(0x1d1d1d)),
        text("|", color(0x131313)),
        text("|", color(0x0a0a0a)),
        text("|", BLACK)
      ))).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientMultiColor2Phase() {
    final String input = "<yellow>Woo: <gradient:black:white:black:-0.65>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", color(0xb3b3b3)),
        text("|", color(0xbcbcbc)),
        text("|", color(0xc6c6c6)),
        text("|", color(0xcfcfcf)),
        text("|", color(0xd9d9d9)),
        text("|", color(0xe3e3e3)),
        text("|", color(0xececec)),
        text("|", color(0xf6f6f6)),
        text("|", color(0xffffff)),
        text("|", color(0xf5f5f5)),
        text("|", color(0xebebeb)),
        text("|", color(0xe2e2e2)),
        text("|", color(0xd8d8d8)),
        text("|", color(0xcecece)),
        text("|", color(0xc5c5c5)),
        text("|", color(0xbbbbbb)),
        text("|", color(0xb2b2b2)),
        text("|", color(0xa8a8a8)),
        text("|", color(0x9e9e9e)),
        text("|", color(0x959595)),
        text("|", color(0x8b8b8b)),
        text("|", color(0x818181)),
        text("|", color(0x787878)),
        text("|", color(0x6e6e6e)),
        text("|", color(0x656565)),
        text("|", color(0x5b5b5b)),
        text("|", color(0x515151)),
        text("|", color(0x484848)),
        text("|", color(0x3e3e3e)),
        text("|", color(0x343434)),
        text("|", color(0x2b2b2b)),
        text("|", color(0x212121)),
        text("|", color(0x181818)),
        text("|", color(0x0e0e0e)),
        text("|", color(0x040404)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000)),
        text("|", color(0x000000))
      ))).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientPhase() {
    final String input = "<yellow>Woo: <gradient:green:blue:0.7>||||||||||||||||||||||||</gradient>!";
    final Component expected = empty().color(YELLOW)
      .append(text("Woo: "))
      .append(virtualOfChildren(textOfChildren(
        text("|", color(0x5588cc)),
        text("|", color(0x5581d3)),
        text("|", color(0x5579db)),
        text("|", color(0x5572e2)),
        text("|", color(0x556aea)),
        text("|", color(0x5563f1)),
        text("|", color(0x555cf8)),
        text("|", color(0x5556fe)),
        text("|", color(0x555df7)),
        text("|", color(0x5565ef)),
        text("|", color(0x556ce8)),
        text("|", color(0x5573e1)),
        text("|", color(0x557bd9)),
        text("|", color(0x5582d2)),
        text("|", color(0x5589cb)),
        text("|", color(0x5591c3)),
        text("|", color(0x5598bc)),
        text("|", color(0x55a0b4)),
        text("|", color(0x55a7ad)),
        text("|", color(0x55aea6)),
        text("|", color(0x55b69e)),
        text("|", color(0x55bd97)),
        text("|", color(0x55c58f)),
        text("|", color(0x55cc88))
      ))).append(text("!"));

    this.assertParsedEquals(expected, input);
  }

  // see #91
  @Test
  void testGradientWithInnerTokens() {
    final String input = "<gradient:green:blue>123<bold>456</gradient>!";
    final Component expected = textOfChildren(
      virtualOfChildren(
        textOfChildren(
          text("1", GREEN),
          text("2", color(0x55dd77)),
          text("3", color(0x55bb99))
        ),
        empty().decorate(BOLD)
          .append(text("4", color(0x5599bb)))
          .append(text("5", color(0x5577dd)))
          .append(text("6", color(0x5555ff)))
      ),
      text("!")
    );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testGradientWithInnerGradientWithInnerToken() {
    final String input = "<gradient:green:blue>123<gradient:red:yellow>456<bold>789</gradient>abc</gradient>!";
    final Component expected = textOfChildren(
      virtualOfChildren(
        textOfChildren(
          text("1", GREEN),
          text("2", color(0x55f064)),
          text("3", color(0x55e074))
        ),
        virtualOfChildren(
          empty()
            .append(text("4", RED))
            .append(text("5", color(0xff7755)))
            .append(text("6", color(0xff9955))),
          empty().decorate(BOLD)
            .append(text("7", color(0xffbb55)))
            .append(text("8", color(0xffdd55)))
            .append(text("9", YELLOW))
        ),
        empty()
          .append(text("a", color(0x5574e0)))
          .append(text("b", color(0x5564f0)))
          .append(text("c", BLUE))
      ),
      text("!")
    );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testNonBmpCharactersInGradient() {
    assertFalse(Character.isBmpCodePoint("𐌰".codePointAt(0)));

    final String input = "Something <gradient:green:blue:1.0>𐌰𐌱𐌲</gradient>";
    final Component expected = text("Something ")
      .append(virtualOfChildren(textOfChildren(
        text("𐌰", BLUE),
        text("𐌱", color(0x55aaaa)),
        text("𐌲", GREEN)
      )));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testSingleCharGradient() {
    final String input1 = "<gradient:red:blue:green>A";
    final String input2 = "<gradient:red:blue:green:red>AB";

    final Component expected1 = virtualOfChildren(text("A", RED));
    final Component expected2 = virtualOfChildren(textOfChildren(text("AB", RED)));

    this.assertParsedEquals(expected1, input1);
    this.assertParsedEquals(expected2, input2);
  }

  // https://github.com/KyoriPowered/adventure/issues/510
  @Test
  void testNestedGradientsDontOverrideColors() {
    final String input = "<gradient:#1985ff:#2bc7ff>a<gradient:#00fffb:#00ffc3>b</gradient> <gray>gray</gray></gradient>";

    final Component expected = virtualOfChildren(
        text("a", color(0x1985ff)),
        virtualOfChildren(text("b", color(0x00fffb))),
        text(" ", color(0x1f9bff)),
        text("gray", NamedTextColor.GRAY)
      );

    this.assertParsedEquals(expected, input);
  }

  // https://github.com/KyoriPowered/adventure/issues/510
  @Test
  void testNestedGradientsReallyDontOverrideColors() {
    final String input = "<gradient:white:blue>A <gradient:yellow:black>B <white>C";

    final Component expected = virtualOfChildren(
      textOfChildren(
        text("A", NamedTextColor.WHITE),
        text(" ", color(0xd5d5ff))
      ),
      virtualOfChildren(
        textOfChildren(
          text("B", YELLOW),
          text(" ", color(0x80802b))
        ),
        text("C", WHITE)
      )
    );

    this.assertParsedEquals(expected, input);
  }

  // https://github.com/KyoriPowered/adventure/issues/790
  @Test
  void testDecorationsPreserved() {
    final Component placeholder = Component.text("b", style(TextDecoration.ITALIC.withState(true)));
    final String input = "<gradient>a<placeholder/>c<bold>d</bold>!</gradient>";
    final Component expected = virtualOfChildren(
      text("a", WHITE),
      text("b", color(0xbfbfbf), TextDecoration.ITALIC),
      text("c", color(0x808080)),
      text("d", color(0x404040), BOLD),
      text("!", BLACK)
    );

    this.assertParsedEquals(expected, input, Placeholder.component("placeholder", placeholder));
  }

  // https://github.com/KyoriPowered/adventure/issues/827
  @Test
  void testLangTagInGradient() {
    final String input = "<gradient:red:blue>ab<lang:block.minecraft.diamond_block>!</gradient>";
    final Component expected = virtualOfChildren(
      textOfChildren(
        text("a", RED),
        text("b", color(0xc6558e))
      ),
      translatable("block.minecraft.diamond_block", color(0x8e55c6))
        .append(text("!", color(0x5555ff)))
    );

    this.assertParsedEquals(expected, input);
  }

  // https://github.com/KyoriPowered/adventure/issues/889
  @Test
  void testMultiColorPhased() {
    final String input = "<gradient:#aa0000:#ff5555:#aaaaaa:1>-------";
    final Component expected = virtualOfChildren(textOfChildren(
      text("-", color(0xaaaaaa)),
      text("-", color(0xaa7171)),
      text("-", color(0xaa3939)),
      text("-", color(0xaa0000)),
      text("-", color(0xc61c1c)),
      text("-", color(0xe33939)),
      text("-", color(0xff5555))
    ));
    this.assertParsedEquals(expected, input);
  }

  @Test
  void gh137() {
    final String input = "<gradient:gold:yellow:red><dum>";
    final String input2 = "<gradient:gold:yellow:red><dum>a";
    final Component expected1 = virtualOfChildren(text("a", GOLD));
    final Component expected2 = virtualOfChildren(textOfChildren(
      text("a", GOLD),
      text("a", RED)
    ));
    final Component expected3 = virtualOfChildren(textOfChildren(
      text("a", GOLD),
      text("a", YELLOW),
      text("a", RED)
    ));
    final Component expected4 = virtualOfChildren(textOfChildren(
      text("a", GOLD),
      text("a", TextColor.color(0xffe339)),
      text("a", TextColor.color(0xffc655)),
      text("a", RED)
    ));
    final Component expected5 = virtualOfChildren(
      textOfChildren(
        text("a", GOLD),
        text("a", TextColor.color(0xffe339)),
        text("a", TextColor.color(0xffc655))
      ),
      text("a", RED)
    );

    this.assertParsedEquals(expected1, input, component("dum", text("a")));
    this.assertParsedEquals(expected2, input, component("dum", text("aa")));
    this.assertParsedEquals(expected3, input, component("dum", text("aaa")));
    this.assertParsedEquals(expected4, input, component("dum", text("aaaa")));
    this.assertParsedEquals(expected5, input2, component("dum", text("aaa")));
  }

  @Test
  void testRoundTripGradient() {
    final String input = "<gradient>hello <bold>world</bold>!";
    final Component parsed = PARSER.deserialize(input);

    this.assertSerializedEquals(input, parsed);
  }

  @Test
  void testRoundTripNestedGradient() {
    final String input = "<gradient:white:blue>A <gradient:yellow:black>B <white>C";
    final Component parsed = PARSER.deserialize(input);

    this.assertSerializedEquals(input, parsed);
  }
}
