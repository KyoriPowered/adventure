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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

import java.util.UUID;

final class TranslatableComponentTest extends SerializerTest {
  @Test
  public void testNoArgs() {
    String translationKey = "multiplayer.player.left";
    this.test(
      Component.translatable(translationKey),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.TRANSLATE, translationKey)
        .build()
    );
  }

  @Test
  public void testFallback() {
    String translationKey = "thisIsA";
    String fallback = "This is a test.";
    this.test(
      Component.translatable()
        .key(translationKey)
        .fallback(fallback)
        .build(),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.TRANSLATE, translationKey)
        .putString(ComponentTreeConstants.TRANSLATE_FALLBACK, fallback)
        .build()
    );
  }

  @Test
  public void testSingleArgWithEvents() {
    String translationKey = "translatable.message";

    UUID id = UUID.fromString("86365c36-e272-4d32-8ab8-d4fee19f6231");
    String name = "Codestech";
    String command = String.format("/msg %s ", name);

    this.test(
      Component.translatable()
        .key(translationKey)
        .color(NamedTextColor.YELLOW)
        .arguments(Component.text()
          .content(name)
          .clickEvent(ClickEvent.suggestCommand(command))
          .hoverEvent(HoverEvent.showEntity(Key.key("minecraft", "player"), id, Component.text(name)))
          .build())
        .build(),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.TRANSLATE, translationKey)
        .putString(ComponentTreeConstants.COLOR, "yellow")
        .put(
          ComponentTreeConstants.TRANSLATE_WITH,
          ListBinaryTag.builder()
            .add(
              CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, name)
              .put(
                ComponentTreeConstants.CLICK_EVENT_SNAKE,
                CompoundBinaryTag.builder()
                  .putString(ComponentTreeConstants.CLICK_EVENT_ACTION, "suggest_command")
                  .putString(ComponentTreeConstants.CLICK_EVENT_COMMAND, command)
                  .build()
              )
              .put(
                ComponentTreeConstants.HOVER_EVENT_SNAKE,
                CompoundBinaryTag.builder()
                  .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, "show_entity")
                  .putString(ComponentTreeConstants.SHOW_ENTITY_ID, "minecraft:player")
                  .put(
                    ComponentTreeConstants.SHOW_ENTITY_UUID,
                    IntArrayBinaryTag.intArrayBinaryTag(-2043257802, -495825614, -1967598338, -509648335)
                  )
                  .putString(ComponentTreeConstants.SHOW_ENTITY_NAME, name)
                  .build()
              )
              .build()
            )
            .build()
        )
        .build()
    );
  }
}
