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
package net.kyori.adventure.text.renderer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.text.MessageFormat;
import java.util.Locale;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslatableComponentRendererTest {
  static final Table<Locale, String, String> TRANSLATIONS = HashBasedTable.create();
  private final TranslatableComponentRenderer<Locale> renderer = TranslatableComponentRenderer.from((locale, key) -> new MessageFormat(TRANSLATIONS.get(locale, key), locale));

  static {
    TRANSLATIONS.put(Locale.US, "test", "This is a test.");
    TRANSLATIONS.put(Locale.US, "cats", "{0} and {1} are cats.");
  }

  @Test
  void testSimple() {
    testSimple(this.renderer);
  }

  static void testSimple(final ComponentRenderer<Locale> renderer) {
    assertEquals(
      TextComponent.of("This is a test.", TextColor.YELLOW),
      renderer.render(
        TranslatableComponent
          .builder()
          .key("test")
          .color(TextColor.YELLOW)
          .build(),
        Locale.US
      )
    );
  }

  @Test
  void testComplex() {
    testComplex(this.renderer);
  }

  static void testComplex(final ComponentRenderer<Locale> renderer) {
    assertEquals(
      TextComponent.builder("")
        .color(TextColor.YELLOW)
        .append(TextComponent.of("kashike"))
        .append(TextComponent.of(" and "))
        .append(TextComponent.of("lucko"))
        .append(TextComponent.of(" are cats."))
        .build(),
      renderer.render(
        TranslatableComponent
          .builder()
          .key("cats")
          .args(
            TextComponent.of("kashike"),
            TextComponent.of("lucko")
          )
          .color(TextColor.YELLOW)
          .build(),
        Locale.US
      )
    );
  }

  @Test
  void testVeryComplex() {
    testVeryComplex(this.renderer);
  }

  static void testVeryComplex(final ComponentRenderer<Locale> renderer) {
    assertEquals(
      TextComponent.builder("")
        .color(TextColor.YELLOW)
        .append(TextComponent.of("This is a test."))
        .append(
          TextComponent.of("")
            .append(TextComponent.of("kashike"))
            .append(TextComponent.of(" and "))
            .append(TextComponent.of("lucko"))
            .append(TextComponent.of(" are cats."))
            .append(TextComponent.space())
            .append(TextComponent.of("Meow!"))
            .hoverEvent(HoverEvent.showText(TextComponent.of("This is a test.")))
        )
        .build(),
      renderer.render(
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
          .color(TextColor.YELLOW)
          .build(),
        Locale.US
      )
    );
  }
}
