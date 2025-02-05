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
package net.kyori.adventure.text.serializer.bedrock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BedrockComponentSerializerTest {
  @Test
  void testSimpleColor() {
    final String expected = "§eDoctorMad9952 joined the game";
    final String actual = BedrockComponentSerializer.bedrock().serialize(Component.text("DoctorMad9952 joined the game", NamedTextColor.YELLOW));
    assertEquals(expected, actual);
  }

  @Test
  void testColorCodeReset() {
    final String expected = "§7[§eH§7]§f §7§lGUEST §r§9»§7 §frtm516§7: §fThis is an amazing bedrock test message";
    final String actual = BedrockComponentSerializer
      .bedrock()
      .serialize(Component.text("")
        .append(Component.text("")
          .append(Component.text("[", NamedTextColor.GRAY))
          .append(Component.text("H", NamedTextColor.YELLOW))
          .append(Component.text("]", NamedTextColor.GRAY))
          .append(Component.text(" ", NamedTextColor.WHITE))
          .append(Component.text("GUEST", TextColor.color(0xb7b7b7)).decoration(TextDecoration.BOLD, true))
        )
        .append(Component.text("")
          .append(Component.text(" ").decoration(TextDecoration.BOLD, true))
          .append(Component.text("»", NamedTextColor.BLUE))
          .append(Component.text(" ", NamedTextColor.GRAY))
        )
        .append(Component.text("")
          .append(Component.text("rtm516", NamedTextColor.WHITE))
          .append(Component.text(": ", NamedTextColor.GRAY))
          .append(Component.text("", NamedTextColor.WHITE))
        )
        .append(Component.text("This is an amazing bedrock test message", NamedTextColor.WHITE))
      );
    assertEquals(expected, actual);
  }

  @Test
  void testNewlineColorRestore() {
    final String expected = "§eContribute to a weekly community goal.\n" +
      "§eAll participants will receive a reward\n" +
      "§eand the top 3 will get extra bonus prizes!";

    final String actual = BedrockComponentSerializer
      .bedrock()
      .serialize(Component.text("Contribute to a weekly community goal.\nAll participants will receive a reward\nand the top 3 will get extra bonus prizes!", NamedTextColor.YELLOW));

    assertEquals(expected, actual);
  }

//  @Test
//  void testSimpleFrom() {
//    final TextComponent component = Component.text("foo");
//    assertEquals(component, BedrockComponentSerializer.legacySection().deserialize("foo"));
//  }
//
//  @Test
//  void testFromColor() {
//    final TextComponent component = Component.text().content("")
//      .append(Component.text("foo").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
//      .append(Component.text("bar").color(NamedTextColor.BLUE))
//      .build();
//
//    assertEquals(component, BedrockComponentSerializer.legacy('&').deserialize("&a&lfoo&9bar"));
//  }
//
//  @Test
//  void testFromColorOverride() {
//    final TextComponent component = Component.text("foo").color(NamedTextColor.BLUE);
//
//    assertEquals(component, BedrockComponentSerializer.legacy('&').deserialize("&a&9foo"));
//  }
//
//  @Test
//  void testInvalidColors() {
//    // https://github.com/KyoriPowered/adventure/issues/266
//    assertEquals(Component.text("&q"), BedrockComponentSerializer.legacyAmpersand().deserialize("&q"));
//    assertEquals(Component.text("&#no"), BedrockComponentSerializer.legacyAmpersand().deserialize("&#no"));
//  }
//
//  @Test
//  void testJustColor() {
//    assertEquals(Component.text("", TextColor.color(0xabcdef)), BedrockComponentSerializer.legacyAmpersand().deserialize("&#abcdef"));
//  }
//
//  @Test
//  void testResetOverride() {
//    final TextComponent component = Component.text().content("")
//      .append(Component.text("foo").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
//      .append(Component.text("bar").color(NamedTextColor.DARK_GRAY))
//      .build();
//
//    assertEquals(component, BedrockComponentSerializer.legacy('&').deserialize("&a&lfoo&r&8bar"));
//  }
//
//  @SuppressWarnings("checkstyle:AvoidEscapedUnicodeCharacters")
//  @Test
//  void testCompound() {
//    final TextComponent component = Component.text()
//      .content("hi there ")
//      .append(Component.text().content("this bit is green ")
//        .color(NamedTextColor.GREEN)
//        .build())
//      .append(Component.text("this isn't ").style(Style.empty()))
//      .append(Component.text().content("and woa, this is again")
//        .color(NamedTextColor.GREEN)
//        .build())
//      .build();
//
//    assertEquals("hi there &athis bit is green &rthis isn't &aand woa, this is again", BedrockComponentSerializer.legacy('&').serialize(component));
//  }
//
//  @Test
//  void testToLegacy() {
//    final TextComponent c1 = Component.text().content("hi")
//      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
//      .append(
//        Component.text("foo")
//          .color(NamedTextColor.GREEN)
//          .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
//      )
//      .append(
//        Component.text("bar")
//          .color(NamedTextColor.BLUE)
//      )
//      .append(Component.text("baz"))
//      .build();
//    assertEquals("§lhi§afoo§9§lbar§r§lbaz", BedrockComponentSerializer.legacySection().serialize(c1));
//
//    final TextComponent c2 = Component.text()
//      .content("")
//      .color(NamedTextColor.YELLOW)
//      .append(Component.text()
//        .content("Hello ")
//        .append(
//          Component.text()
//            .content("world")
//            .color(NamedTextColor.GREEN)
//            .build()
//        )
//        .append(Component.text("!")) // Should be yellow
//        .build()
//      )
//      .build();
//    assertEquals("§eHello §aworld§e!", BedrockComponentSerializer.legacySection().serialize(c2));
//
//    final TextComponent c3 = Component.text()
//      .content("")
//      .decoration(TextDecoration.BOLD, true)
//      .append(
//        Component.text()
//          .content("")
//          .color(NamedTextColor.YELLOW)
//          .append(Component.text()
//            .content("Hello ")
//            .append(
//              Component.text()
//                .content("world")
//                .color(NamedTextColor.GREEN)
//                .build()
//            )
//            .append(Component.text("!"))
//            .build()
//          )
//          .build())
//      .build();
//    assertEquals("§e§lHello §a§lworld§e§l!", BedrockComponentSerializer.legacySection().serialize(c3));
//  }
//
//  @Test
//  void testToLegacyWithHexColor() {
//    final TextComponent c0 = Component.text("Kittens!", TextColor.color(0xffefd5));
//    assertEquals("§#ffefd5Kittens!", BedrockComponentSerializer.builder().hexColors().build().serialize(c0));
//  }
//
//  @Test
//  void testToLegacyWithHexColorDownsampling() {
//    final TextComponent comp = Component.text("purr", TextColor.color(0xff0000));
//    assertEquals("§4purr", BedrockComponentSerializer.builder().build().serialize(comp));
//  }
//
//  @Test
//  void testFromLegacyWithHexColor() {
//    final TextComponent component = Component.text().content("")
//      .append(Component.text("pretty").color(TextColor.fromHexString("#ffb6c1")))
//      .append(Component.text("in").color(TextColor.fromHexString("#ff69b4")).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
//      .append(Component.text("pink").color(TextColor.fromHexString("#ffc0cb")))
//      .build();
//    assertEquals(component, BedrockComponentSerializer.builder().character('&').hexColors().build().deserialize("&#ffb6c1pretty&#ff69b4&lin&#ffc0cbpink"));
//  }
//
//  @Test
//  void testToLegacyWithHexColorTerribleFormat() {
//    final TextComponent c0 = Component.text("Kittens!", TextColor.color(0xffefd5));
//    assertEquals("§x§f§f§e§f§d§5Kittens!", BedrockComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build().serialize(c0));
//  }
//
//  @Test
//  void testFromLegacyWithHexColorTerribleFormat() {
//    final TextComponent expected = Component.text("Kittens!", TextColor.color(0xffefd5));
//    assertEquals(expected, BedrockComponentSerializer.builder().hexColors().build().deserialize("§x§f§f§e§f§d§5Kittens!"));
//  }
//
//  @Test
//  void testFromLegacyWithHexColorTerribleFormatMixed() {
//    final TextComponent expected = Component.text().content("")
//      .append(Component.text("Hugs and ", NamedTextColor.RED))
//      .append(Component.text("Kittens!", TextColor.color(0xffefd5)))
//      .build();
//    assertEquals(expected, BedrockComponentSerializer.builder().hexColors().build().deserialize("§cHugs and §x§f§f§e§f§d§5Kittens!"));
//  }
//
//  @Test
//  void testFromLegacyWithHexColorTerribleFormatEnsureProperLookahead() {
//    final TextComponent expected = Component.text().content("")
//      .append(Component.text("Hugs and ", NamedTextColor.RED))
//      .append(Component.text("Kittens!", NamedTextColor.DARK_PURPLE))
//      .build();
//    assertEquals(expected, BedrockComponentSerializer.builder().hexColors().build().deserialize("§cHugs and §f§f§e§f§d§5Kittens!"));
//  }
//
//  @Test
//  void testFromLegacyWithHexColorTerribleFormatEnsureMultipleColorsWork() {
//    final TextComponent expected = Component.text().content("Happy with ")
//      .append(Component.text("Lavender and ", TextColor.color(0x6b4668)))
//      .append(Component.text("Cyan!", TextColor.color(0xffefd5)))
//      .build();
//    assertEquals(expected, BedrockComponentSerializer.builder().hexColors().build().deserialize("Happy with §x§6§b§4§6§6§8Lavender and §x§f§f§e§f§d§5Cyan!"));
//  }
//
//  @Test
//  void testFromLegacyWithHexColorTerribleFormatHangingCharacter() {
//    final TextComponent expected = Component.text().content("§x")
//      .append(Component.text("Kittens!", NamedTextColor.YELLOW))
//      .build();
//    assertEquals(expected, BedrockComponentSerializer.builder().hexColors().build().deserialize("§x§eKittens!"));
//  }
//
//  // https://github.com/KyoriPowered/adventure/issues/108
//  @Test
//  void testFromLegacyWithNewline() {
//    final TextComponent comp = Component.text().content("One: Test ")
//      .append(Component.text("String\nTwo: ", NamedTextColor.GREEN))
//      .append(Component.text("Test ", NamedTextColor.AQUA))
//      .append(Component.text("String", NamedTextColor.GREEN))
//      .build();
//    final String in = "One: Test &aString\nTwo: &bTest &aString";
//    assertEquals(comp, BedrockComponentSerializer.legacy('&').deserialize(in));
//  }
//
//  // https://github.com/KyoriPowered/adventure/issues/108
//  @Test
//  void testBeginningTextUnformatted() {
//    final String input = "Test &cString";
//    final TextComponent expected = Component.text().content("Test ")
//      .append(Component.text("String", NamedTextColor.RED))
//      .build();
//
//    assertEquals(expected, BedrockComponentSerializer.legacy(BedrockComponentSerializer.AMPERSAND_CHAR).deserialize(input));
//  }
//
//  // https://github.com/KyoriPowered/adventure/issues/92
//  @Test
//  void testStackedFormattingFlags() {
//    final String input = "§r§r§c§k||§e§lProfile§c§k||";
//    final TextComponent output = Component.text().append(
//      Component.text("||", Style.style(NamedTextColor.RED, TextDecoration.OBFUSCATED)),
//      Component.text("Profile", Style.style(NamedTextColor.YELLOW, TextDecoration.BOLD)),
//      Component.text("||", Style.style(NamedTextColor.RED, TextDecoration.OBFUSCATED))
//    ).build();
//    assertEquals(output, BedrockComponentSerializer.legacySection().deserialize(input));
//  }
//
//  @Test
//  void testResetClearsColorInSameBlock() {
//    final String input = "§c§rCleared";
//    final TextComponent output = Component.text("Cleared");
//    assertEquals(output, BedrockComponentSerializer.legacySection().deserialize(input));
//  }
//
//  @Test
//  void testParseColourChar() {
//    final LegacyFormat lf = BedrockComponentSerializer.parseChar('5');
//    assertNotNull(lf);
//    assertEquals(NamedTextColor.DARK_PURPLE, lf.color());
//    assertNull(lf.decoration());
//    assertFalse(lf.reset());
//  }
//
//  @Test
//  void testParseDecorationChar() {
//    final LegacyFormat lf = BedrockComponentSerializer.parseChar('l');
//    assertNotNull(lf);
//    assertNull(lf.color());
//    assertEquals(TextDecoration.BOLD, lf.decoration());
//    assertFalse(lf.reset());
//  }
//
//  @Test
//  void testParseResetChar() {
//    final LegacyFormat lf = BedrockComponentSerializer.parseChar('r');
//    assertNotNull(lf);
//    assertNull(lf.color());
//    assertNull(lf.decoration());
//    assertTrue(lf.reset());
//  }
//
//  // https://github.com/KyoriPowered/adventure/issues/287
//  @Test
//  void testNoRedundantReset() {
//    final String text = "&a&lP&eaper";
//    final Component expectedDeserialized = Component.text()
//      .append(Component.text("P", NamedTextColor.GREEN, TextDecoration.BOLD))
//      .append(Component.text("aper", NamedTextColor.YELLOW))
//      .build();
//    final Component deserialized = BedrockComponentSerializer.legacyAmpersand().deserialize(text);
//
//    assertEquals(expectedDeserialized, deserialized);
//
//    final String roundtripped = BedrockComponentSerializer.legacyAmpersand().serialize(deserialized);
//    assertEquals(text, roundtripped);
//  }
//
//  @Test
//  void testInvalidHexStringsPassedThrough() {
//    final String text = "Hello&#hellos world";
//    final Component deserialized = BedrockComponentSerializer.legacyAmpersand().deserialize(text);
//
//    assertEquals(Component.text(text), deserialized);
//  }
//
//  @Test
//  void testNullTextFormat() {
//    final List<CharacterAndFormat> formats = new ArrayList<>(CharacterAndFormat.defaults());
//    formats.remove(CharacterAndFormat.STRIKETHROUGH);
//    final BedrockComponentSerializer serializer = BedrockComponentSerializer.legacySection().toBuilder().formats(formats).build();
//
//    final Component strikethough = Component.text("Hello World", Style.style(TextDecoration.STRIKETHROUGH));
//    final String serialized = serializer.serialize(strikethough);
//    assertEquals(serialized, "Hello World");
//  }
//
//  // https://github.com/KyoriPowered/adventure/issues/1043
//  @Test
//  void testCaseInsensitivity() {
//    final Component expected = Component.text("pop4959", NamedTextColor.YELLOW);
//    final Component lowercaseActual = BedrockComponentSerializer.legacyAmpersand().deserialize("&epop4959");
//    assertEquals(expected, lowercaseActual);
//
//    final Component uppercaseActual = BedrockComponentSerializer.legacyAmpersand().deserialize("&Epop4959");
//    assertEquals(expected, uppercaseActual);
//  }
//
//  @Test
//  void testCaseSensitivity() {
//    final Component expected = Component.text("&Epop4959");
//    final Component lowercaseActual = BedrockComponentSerializer
//      .legacyAmpersand()
//      .toBuilder()
//      .formats(Collections.singletonList(CharacterAndFormat.characterAndFormat('e', NamedTextColor.YELLOW)))
//      .build()
//      .deserialize("&Epop4959");
//    assertEquals(expected, lowercaseActual);
//  }
}
