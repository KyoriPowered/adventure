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
package net.kyori.adventure.chat;

import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ChatTypeImpl implements ChatType {
  private final Key key;

  ChatTypeImpl(final @NotNull Key key) {
    this.key = key;
  }

  @Override
  public @NotNull Key key() {
    return this.key;
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  static final class BoundImpl implements ChatType.Bound {
    private final ChatType chatType;
    private final Component name;
    private final @Nullable Component target;

    BoundImpl(final ChatType chatType, final Component name, final @Nullable Component target) {
      this.chatType = chatType;
      this.name = name;
      this.target = target;
    }

    @Override
    public @NotNull ChatType type() {
      return this.chatType;
    }

    @Override
    public @NotNull Component name() {
      return this.name;
    }

    @Override
    public @Nullable Component target() {
      return this.target;
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }
}
