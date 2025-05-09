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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.option.OptionState;

final class UUIDSerializer extends TypeAdapter<UUID> {
  private final boolean emitIntArray;

  static TypeAdapter<UUID> uuidSerializer(final OptionState features) {
    return new UUIDSerializer(features.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY)).nullSafe();
  }

  private UUIDSerializer(final boolean emitIntArray) {
    this.emitIntArray = emitIntArray;
  }

  @Override
  public void write(final JsonWriter out, final UUID value) throws IOException {
    if (this.emitIntArray) {
      final int msb0 = (int) (value.getMostSignificantBits() >> 32);
      final int msb1 = (int) (value.getMostSignificantBits() & 0xffffffffl);
      final int lsb0 = (int) (value.getLeastSignificantBits() >> 32);
      final int lsb1 = (int) (value.getLeastSignificantBits() & 0xffffffffl);

      out.beginArray()
        .value(msb0)
        .value(msb1)
        .value(lsb0)
        .value(lsb1)
        .endArray();
    } else {
      out.value(value.toString());
    }
  }

  @Override
  public UUID read(final JsonReader in) throws IOException {
    // int-array format was added in 23w40a, a pre for 1.20.3
    if (in.peek() == JsonToken.BEGIN_ARRAY) {
      in.beginArray();
      final int msb0 = in.nextInt();
      final int msb1 = in.nextInt();
      final int lsb0 = in.nextInt();
      final int lsb1 = in.nextInt();
      in.endArray();
      return new UUID((long) msb0 << 32 | ((long) msb1 & 0xffffffffl), (long) lsb0 << 32 | ((long) lsb1 & 0xffffffffl));
    }

    return UUID.fromString(in.nextString());
  }
}
