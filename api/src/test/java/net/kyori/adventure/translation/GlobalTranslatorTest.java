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
package net.kyori.adventure.translation;

import java.text.MessageFormat;
import java.util.Locale;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalTranslatorTest {
  @BeforeEach
  void removeDummySourceBeforeEachTest() {
    GlobalTranslator.translator().removeSource(DummyTranslator.INSTANCE);
  }

  @Test
  void testRender() {
    GlobalTranslator.translator().addSource(DummyTranslator.INSTANCE);
    assertEquals(
      Component.text()
        .append(Component.text("Hello "))
        .append(Component.text("kashike", NamedTextColor.DARK_PURPLE))
        .append(Component.text("!"))
        .build(),
      GlobalTranslator.render(Component.translatable("testDummy", Component.text("kashike", NamedTextColor.DARK_PURPLE)), Locale.US)
    );
    assertEquals(
      Component.entityNBT()
        .nbtPath("ignored")
        .selector("@p")
        .separator(
          Component.text()
            .append(Component.text("Hello "))
            .append(Component.text("you"))
            .append(Component.text("!"))
        )
        .build(),
      GlobalTranslator.render(Component.entityNBT("ignored", "@p").separator(Component.translatable("testDummy", Component.text("you"))), Locale.US)
    );
  }

  @Test
  void testAddingSelf() {
    assertThrows(IllegalArgumentException.class, () -> GlobalTranslator.translator().addSource(GlobalTranslator.translator()));
  }

  @Test
  void testAddAndRemoveSource() {
    assertTrue(GlobalTranslator.translator().addSource(DummyTranslator.INSTANCE));
    assertThat(GlobalTranslator.translator().sources()).contains(DummyTranslator.INSTANCE);
    assertTrue(GlobalTranslator.translator().removeSource(DummyTranslator.INSTANCE));
    assertThat(GlobalTranslator.translator().sources()).doesNotContain(DummyTranslator.INSTANCE);
  }

  @Test
  void testTranslate() {
    assertNull(GlobalTranslator.translator().translate("testDummy", Locale.US));
    GlobalTranslator.translator().addSource(DummyTranslator.INSTANCE);
    assertEquals(new MessageFormat("Hello {0}!"), GlobalTranslator.translator().translate("testDummy", Locale.US));
  }

  static class DummyTranslator implements Translator {
    static final DummyTranslator INSTANCE = new DummyTranslator();

    @Override
    public @NotNull Key name() {
      return Key.key("adventure", "test_dummy");
    }

    @Override
    public @Nullable MessageFormat translate(final @NotNull String key, final @NotNull Locale locale) {
      return (key.equals("testDummy") && locale.equals(Locale.US))
        ? new MessageFormat("Hello {0}!")
        : null;
    }
  }
}
