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
package net.kyori.adventure.text.minimessage;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MiniMessageTranslatorTest extends AbstractTest {
  private static final Pointered TARGET = new Pointered() { };
  private static final MiniMessageTranslator TRANSLATOR = new MiniMessageTranslator() {
    @Override
    protected @Nullable String getMiniMessageString(final @NotNull String key, final @NotNull Locale locale) {
      // hack the test here by just returning the key
      return key;
    }

    @Override
    public @NotNull Key name() {
      return Key.key("mini:trans");
    }
  };
  private static final Locale LOCALE = Locale.UK;

  @BeforeAll
  public static void beforeAll() {
    GlobalTranslator.translator().addSource(TRANSLATOR);
  }

  @Test
  public void testTarget() {
    final AtomicBoolean targetFound = new AtomicBoolean(false);
    this.translate(
      Component.translatable(
        "<test>",
        Argument.target(TARGET),
        Argument.tagResolver(TagResolver.resolver("test", (argumentQueue, context) -> {
          if (context.target() == TARGET) targetFound.set(true);
          return Tag.preProcessParsed("");
        }))
      )
    );
    assertTrue(targetFound.get(), "target was not found");

    final AtomicBoolean targetSetToDefault = new AtomicBoolean(false);
    this.translate(
      Component.translatable(
        "<test>",
        Argument.tagResolver(TagResolver.resolver("test", (argumentQueue, context) -> {
          final Pointered target = context.target();
          if (target != null && LOCALE.equals(target.getOrDefault(Identity.LOCALE, null))) {
            targetSetToDefault.set(true);
          }
          return Tag.preProcessParsed("");
        }))
      )
    );
    assertTrue(targetSetToDefault.get(), "target was not set to the default");
  }

  @Test
  public void testIndexedArguments() {
    assertEquals(
      Component.text("Kezz is cool!"),
      this.translate(
        Component.translatable(
          "<arg:0> is <arg:1>!",
          Component.text("Kezz"),
          Component.text("cool")
        )
      )
    );
  }

  @Test
  public void testNamedArguments() {
    assertEquals(
      Component.text("Kezz is cool!"),
      this.translate(
        Component.translatable(
          "<name> is <thing>!",
          Argument.component("name", Component.text("Kezz")),
          Argument.component("thing", Component.text("cool"))
        )
      )
    );
    assertEquals(
      Component.text("Kezz is cool!"),
      this.translate(
        Component.translatable(
          "<arg:0> is <arg:1>!",
          Argument.component("name", Component.text("Kezz")),
          Argument.component("thing", Component.text("cool"))
        )
      )
    );
  }

  @Test
  public void testTags() {
    assertEquals(
      Component.text("Kezz is cool!"),
      this.translate(
        Component.translatable(
          "<name> is <thing>!",
          Argument.tagResolver(Placeholder.component("name", Component.text("Kezz"))),
          Argument.tag("thing", Tag.preProcessParsed("cool"))
        )
      )
    );
  }

  @Test
  public void testRecursiveArguments() {
    assertEquals(
      Component.text("Kezz is cool!"),
      this.translate(
        Component.translatable(
          "<arg:0> is <arg:1>!",
          Component.translatable("<arg:0>", Component.text("Kezz")),
          Component.translatable(
            "<arg:0>",
            Component.translatable("<arg:0>", Component.text("cool"))
          )
        )
      )
    );
  }

  @Test
  public void testChildren() {
    assertEquals(
      Component.text()
        .content("Kezz is ")
        .append(Component.text("cool!"))
        .build(),
      this.translate(
        Component.translatable()
          .key("<arg:0> is ")
          .arguments(Component.text("Kezz"))
          .append(Component.translatable("<arg:0>", Component.text("cool!")))
          .build()
      )
    );
  }

  @AfterAll
  public static void afterAll() {
    GlobalTranslator.translator().removeSource(TRANSLATOR);
  }

  private Component translate(final TranslatableComponent component) {
    return GlobalTranslator.render(component, LOCALE);
  }
}
