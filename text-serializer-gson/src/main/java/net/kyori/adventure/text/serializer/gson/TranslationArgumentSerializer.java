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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;

final class TranslationArgumentSerializer extends TypeAdapter<TranslationArgument> {
  private final Gson gson;

  static TypeAdapter<TranslationArgument> create(final Gson gson) {
    return new TranslationArgumentSerializer(gson).nullSafe();
  }

  private TranslationArgumentSerializer(final Gson gson) {
    this.gson = gson;
  }

  @Override
  public void write(final JsonWriter out, final TranslationArgument value) throws IOException {
    final Object raw = value.value();
    if (raw instanceof Boolean) {
      out.value((Boolean) raw);
    } else if (raw instanceof Number) {
      out.value((Number) raw);
    } else if (raw instanceof Component) {
      this.gson.toJson(raw, SerializerFactory.COMPONENT_TYPE, out);
    } else {
      throw new IllegalStateException("Unable to serialize translatable argument of type " + raw.getClass() + ": " + raw);
    }
  }

  @Override
  public TranslationArgument read(final JsonReader in) throws IOException {
    switch (in.peek()) {
      case BOOLEAN: return TranslationArgument.bool(in.nextBoolean());
      case NUMBER: return TranslationArgument.numeric(this.gson.fromJson(in, Number.class));
      default: return TranslationArgument.component(this.gson.fromJson(in, SerializerFactory.COMPONENT_TYPE));
    }
  }
}
