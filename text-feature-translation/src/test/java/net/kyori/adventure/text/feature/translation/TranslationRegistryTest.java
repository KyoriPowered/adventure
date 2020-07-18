/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.feature.translation;

import java.text.MessageFormat;
import java.util.Locale;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TranslationRegistryTest {
  static final TranslationRegistry REGISTRY = TranslationRegistry.get();
  static final TranslatableComponentRenderer RENDERER = new TranslatableComponentRenderer();

  @BeforeAll
  static void testRegister() {
    REGISTRY.registerAll(Locale.US, Locale.US.toLanguageTag(), true);
  }

  @Test
  void testRegister_duplicate() {
    assertThrows(IllegalArgumentException.class, () -> REGISTRY.register("test", Locale.US, new MessageFormat("Another test.")));
  }

  @Test
  void testTranslate() {
    assertEquals(new MessageFormat("This is a test.", Locale.US), REGISTRY.translate("test", Locale.US));
  }

  @Test
  void testTranslate_escapeQuotes() {
    assertEquals(new MessageFormat("{0} and ''{1}'' are cats.", Locale.US), REGISTRY.translate("cats", Locale.US));
  }

  @Test
  void testTranslate_utf8() {
    assertEquals(new MessageFormat("☃", Locale.US), REGISTRY.translate("snowperson", Locale.US));
  }

  @Test
  void testRender_simple() {
    assertEquals(
      TextComponent.of("This is a test.", NamedTextColor.YELLOW),
      RENDERER.render(
        TranslatableComponent
          .builder()
          .key("test")
          .color(NamedTextColor.YELLOW)
          .build(),
        Locale.US
      )
    );
  }

  @Test
  void testRender_complex() {
    assertEquals(
      TextComponent.builder("")
        .color(NamedTextColor.YELLOW)
        .append(TextComponent.of("kashike"))
        .append(TextComponent.of(" and '"))
        .append(TextComponent.of("lucko"))
        .append(TextComponent.of("' are cats."))
        .build(),
      RENDERER.render(
        TranslatableComponent
          .builder()
          .key("cats")
          .args(
            TextComponent.of("kashike"),
            TextComponent.of("lucko")
          )
          .color(NamedTextColor.YELLOW)
          .build(),
        Locale.US
      )
    );
  }

  @Test
  void testRender_veryComplex() {
    assertEquals(
      TextComponent.builder("")
        .color(NamedTextColor.YELLOW)
        .append(TextComponent.of("This is a test."))
        .append(
          TextComponent.of("")
            .append(TextComponent.of("kashike"))
            .append(TextComponent.of(" and '"))
            .append(TextComponent.of("lucko"))
            .append(TextComponent.of("' are cats."))
            .append(TextComponent.space())
            .append(TextComponent.of("Meow!"))
            .hoverEvent(HoverEvent.showText(TextComponent.of("This is a test.")))
        )
        .build(),
      RENDERER.render(
        TextComponent
          .builder("")
          .append(
            TranslatableComponent.of("test")
          )
          .append(
            TranslatableComponent
              .builder()
              .key("cats")
              .args(
                TextComponent.of("kashike"),
                TextComponent.of("lucko")
              )
              .hoverEvent(HoverEvent.showText(TranslatableComponent.of("test")))
              .append(TextComponent.space())
              .append(TextComponent.of("Meow!"))
              .build()
          )
          .color(NamedTextColor.YELLOW)
          .build(),
        Locale.US
      )
    );
  }

  @Test
  @AfterAll
  static void testUnregister() {
    REGISTRY.unregister("test");
    assertNull(REGISTRY.translate("test", Locale.US));
  }
}
