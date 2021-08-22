/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
import net.kyori.adventure.util.Services;

final class Instances {
  private static final Optional<JsonComponentSerializer.Provider> PROVIDER = Services.service(JsonComponentSerializer.Provider.class);

  static final Optional<JsonComponentSerializer> INSTANCE = PROVIDER.map(JsonComponentSerializer.Provider::json);
  static final Optional<JsonComponentSerializer> LEGACY_INSTANCE = PROVIDER.map(JsonComponentSerializer.Provider::jsonLegacy);

  static Optional<JsonComponentSerializer.Builder> builder() {
    return PROVIDER.map(JsonComponentSerializer.Provider::builder);
  }

  private Instances() {
  }
}
