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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

final class ClickEventSerializer {

  private static final String ACTION = "action";
  private static final String VALUE = "value";

  private ClickEventSerializer() {
  }

  static @NotNull ClickEvent deserialize(@NotNull CompoundBinaryTag tag) {
    BinaryTag actionTag = tag.get(ACTION);

    if (actionTag == null) {
      throw new IllegalArgumentException("The serialized click event doesn't contain an action");
    }

    String actionString = ((StringBinaryTag) actionTag).value();
    ClickEvent.Action action = ClickEvent.Action.NAMES.valueOrThrow(actionString);

    return ClickEvent.clickEvent(action, tag.getString(VALUE));
  }

  static @NotNull CompoundBinaryTag serialize(@NotNull ClickEvent event) {
    return CompoundBinaryTag.builder()
      .putString(ACTION, ClickEvent.Action.NAMES.keyOrThrow(event.action()))
      .putString(VALUE, event.value())
      .build();
  }
}
