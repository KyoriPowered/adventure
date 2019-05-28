/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.serializer.legacy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

class LinkingLegacyComponentSerializerTest {
  @Test
  void testSimpleFrom() {
    assertEquals(TextComponent.of("foo"), LegacyComponentSerializer.legacyLinking().deserialize("foo"));
  }

  @Test
  void testBareUrl() {
    final String bareUrl = "https://www.example.com";
    final TextComponent expectedNonLinkify = TextComponent.of(bareUrl);
    assertEquals(expectedNonLinkify, LegacyComponentSerializer.legacy().deserialize(bareUrl));
    final TextComponent expectedBareUrl = TextComponent.of(bareUrl)
      .clickEvent(ClickEvent.openUrl(bareUrl));
    assertEquals(expectedBareUrl, LegacyComponentSerializer.legacyLinking().deserialize(bareUrl));
  }

  @Test
  void testPrefixUrl() {
    final String bareUrl = "https://www.example.com";
    final String hasPrefix = "did you hear about https://www.example.com";
    final TextComponent expectedHasPrefix = TextComponent.builder("did you hear about ")
      .append(TextComponent.of(bareUrl).clickEvent(ClickEvent.openUrl(bareUrl)))
      .build();
    assertEquals(expectedHasPrefix, LegacyComponentSerializer.legacyLinking().deserialize(hasPrefix, '&'));
  }

  @Test
  void testPrefixSuffixUrl() {
    final String bareUrl = "https://www.example.com";
    final String hasPrefixSuffix = "did you hear about https://www.example.com? they're really cool";
    final TextComponent expectedHasPrefixSuffix = TextComponent.builder("did you hear about ")
      .append(TextComponent.of(bareUrl).clickEvent(ClickEvent.openUrl(bareUrl)))
      .append(TextComponent.of("? they're really cool"))
      .build();
    assertEquals(expectedHasPrefixSuffix, LegacyComponentSerializer.legacyLinking().deserialize(hasPrefixSuffix, '&'));
  }

  @Test
  void testPrefixSuffixUrlAndColors() {
    final String bareUrl = "https://www.example.com";
    final String hasPrefixSuffixColors = "&adid you hear about &chttps://www.example.com? &9they're really cool";
    final TextComponent expectedHasPrefixSuffixColors = TextComponent.builder("")
      .append(TextComponent.of("did you hear about ", TextColor.GREEN))
      .append(TextComponent.of("https://www.example.com", TextColor.RED).clickEvent(ClickEvent.openUrl(bareUrl)))
      .append(TextComponent.of("? ", TextColor.RED))
      .append(TextComponent.of("they're really cool", TextColor.BLUE))
      .build();
    assertEquals(expectedHasPrefixSuffixColors, LegacyComponentSerializer.legacyLinking().deserialize(hasPrefixSuffixColors, '&'));
  }

  @Test
  void testMultipleUrls() {
    final String manyUrls = "go to https://www.example.com and https://www.example.net for cat videos";
    final TextComponent expectedManyUrls = TextComponent.builder("go to ")
      .append(TextComponent.of("https://www.example.com").clickEvent(ClickEvent.openUrl("https://www.example.com")))
      .append(TextComponent.of(" and "))
      .append(TextComponent.of("https://www.example.net").clickEvent(ClickEvent.openUrl("https://www.example.net")))
      .append(TextComponent.of(" for cat videos"))
      .build();
    assertEquals(expectedManyUrls, LegacyComponentSerializer.legacyLinking().deserialize(manyUrls, '&'));
  }

  @Test
  void testLinkifyWithStyle() {
    final Style testStyle = Style.of(TextColor.GREEN, TextDecoration.UNDERLINED);
    final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyLinking(testStyle);

    final String bareUrl = "https://www.example.com";
    final Component expectedBareUrl = TextComponent.of(bareUrl).clickEvent(ClickEvent.openUrl(bareUrl)).style(testStyle);
    assertEquals(expectedBareUrl, serializer.deserialize(bareUrl));

    final String hasPrefixSuffix = "did you hear about https://www.example.com? they're really cool";
    final TextComponent expectedHasPrefixSuffix = TextComponent.builder("did you hear about ")
      .append(TextComponent.of(bareUrl).clickEvent(ClickEvent.openUrl(bareUrl)).style(testStyle))
      .append(TextComponent.of("? they're really cool"))
      .build();
    assertEquals(expectedHasPrefixSuffix, serializer.deserialize(hasPrefixSuffix, '&'));

    final String manyUrls = "go to https://www.example.com and https://www.example.net for cat videos";
    final TextComponent expectedManyUrls = TextComponent.builder("go to ")
      .append(TextComponent.of("https://www.example.com").clickEvent(ClickEvent.openUrl("https://www.example.com")).style(testStyle))
      .append(TextComponent.of(" and "))
      .append(TextComponent.of("https://www.example.net").clickEvent(ClickEvent.openUrl("https://www.example.net")).style(testStyle))
      .append(TextComponent.of(" for cat videos"))
      .build();
    assertEquals(expectedManyUrls, serializer.deserialize(manyUrls, '&'));
  }
}
