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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;

final class UUIDSerializer extends TypeAdapter<UUID> {
  static final TypeAdapter<UUID> INSTANCE = new UUIDSerializer().nullSafe();

  private UUIDSerializer() {
  }

  @Override
  public void write(final JsonWriter out, final UUID value) throws IOException {
    // todo: feature flag to choose whether to emit as 4-int syntax
    out.value(value.toString());
  }

  @Override
  public UUID read(final JsonReader in) throws IOException {
    // int-array format was added in 23w40a, a pre for 1.20.3
    if (in.peek() == JsonToken.BEGIN_ARRAY) {
      in.beginArray();
      final int v0 = in.nextInt();
      final int v1 = in.nextInt();
      final int v2 = in.nextInt();
      final int v3 = in.nextInt();
      in.endArray();
      return new UUID((long) v0 << 32 | ((long) v1 & 0xffffffffl), (long) v2 << 32 | ((long) v3 & 0xffffffffl));
    }

    return UUID.fromString(in.nextString());
  }
}
