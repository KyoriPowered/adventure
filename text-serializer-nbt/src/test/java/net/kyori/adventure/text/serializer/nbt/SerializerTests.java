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

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class SerializerTests {

  private static final NBTComponentSerializer DEFAULT_SERIALIZER = NBTComponentSerializer.nbt();

  private SerializerTests() {}

  static void testComponent(@NotNull Component component, @NotNull BinaryTag tag) {
    testComponent(DEFAULT_SERIALIZER, component, tag);
  }

  static void testComponent(@NotNull NBTComponentSerializer serializer,
                            @NotNull Component component, @NotNull BinaryTag tag) {
    assertEquals(tag, serializer.serialize(component));
    assertEquals(component, serializer.deserialize(tag));
  }

  static void testStyle(@NotNull Style style, @NotNull CompoundBinaryTag tag) {
    testStyle(DEFAULT_SERIALIZER, style, tag);
  }

  static void testStyle(@NotNull NBTComponentSerializer serializer,
                        @NotNull Style style, @NotNull CompoundBinaryTag tag) {
    assertEquals(tag, serializer.serializeStyle(style));
    assertEquals(style, serializer.deserializeStyle(tag));
  }

  static @NotNull Component deserializeComponent(@NotNull BinaryTag tag) {
    return DEFAULT_SERIALIZER.deserialize(tag);
  }

  static @NotNull BinaryTag serializeComponent(@NotNull Component component) {
    return DEFAULT_SERIALIZER.serialize(component);
  }

  static @NotNull Style deserializeStyle(@NotNull CompoundBinaryTag tag) {
    return DEFAULT_SERIALIZER.deserializeStyle(tag);
  }

  static @NotNull String name(@NotNull TextDecoration decoration) {
    return TextDecoration.NAMES.keyOrThrow(decoration);
  }

  static @NotNull String name(@NotNull NamedTextColor decoration) {
    return NamedTextColor.NAMES.keyOrThrow(decoration);
  }

  static @NotNull String name(ClickEvent.@NotNull Action action) {
    return ClickEvent.Action.NAMES.keyOrThrow(action);
  }

  static @NotNull String name(HoverEvent.@NotNull Action<?> action) {
    return HoverEvent.Action.NAMES.keyOrThrow(action);
  }
}
