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

import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;

final class JsonComponentSerializerImpl implements JsonComponentSerializer {
  private static final JsonComponentSerializer MISSING_INSTANCE = new JsonComponentSerializerImpl();
  private static final Optional<Provider> SERVICE = Services.serviceWithFallback(Provider.class);

  @Override
  public @NotNull Component deserialize(final @NotNull String input) {
    throw new UnsupportedOperationException("No JsonComponentSerializer implementation found");
  }

  @Override
  public @NotNull String serialize(final @NotNull Component component) {
    throw new UnsupportedOperationException("No JsonComponentSerializer implementation found");
  }

  // We cannot store these fields in JsonComponentSerializerImpl directly due to class initialisation issues.
  static final class Instances {
    static final JsonComponentSerializer INSTANCE = SERVICE
      .map(Provider::json)
      .orElse(MISSING_INSTANCE);
  }
}
