/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text.serializer.json;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class DummyJSONComponentSerializer implements JSONComponentSerializer {
  static final JSONComponentSerializer INSTANCE = new DummyJSONComponentSerializer();
  private static final String UNSUPPORTED_MESSAGE =
    "No JsonComponentSerializer implementation found\n" +
      "\n" +
      "Are you missing an implementation artifact like adventure-text-serializer-gson?\n" +
      "Is your environment configured in a way that causes ServiceLoader to malfunction?";

  @Override
  public @NotNull Component deserialize(final @NotNull String input) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  @Override
  public @NotNull String serialize(final @NotNull Component component) {
    throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
  }

  // A no-op builder that just returns the unsupported instance.
  static final class BuilderImpl implements Builder {
    @Override
    public @NotNull Builder downsampleColors() {
      return this;
    }

    @Override
    public @NotNull Builder legacyHoverEventSerializer(final @Nullable LegacyHoverEventSerializer serializer) {
      return this;
    }

    @Override
    public @NotNull Builder emitLegacyHoverEvent() {
      return this;
    }

    @Override
    public JSONComponentSerializer build() {
      return INSTANCE;
    }
  }
}
