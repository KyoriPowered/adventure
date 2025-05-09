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

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.option.OptionState;

final class ShadowColorSerializer extends TypeAdapter<ShadowColor> {
  static TypeAdapter<ShadowColor> create(final OptionState options) {
    return new ShadowColorSerializer(options.value(JSONOptions.SHADOW_COLOR_MODE) == JSONOptions.ShadowColorEmitMode.EMIT_ARRAY).nullSafe();
  }

  private final boolean emitArray;

  private ShadowColorSerializer(final boolean emitArray) {
    this.emitArray = emitArray;
  }

  @Override
  public void write(final JsonWriter out, final ShadowColor value) throws IOException {
    if (this.emitArray) {
      out.beginArray()
        .value(componentAsFloat(value.red()))
        .value(componentAsFloat(value.green()))
        .value(componentAsFloat(value.blue()))
        .value(componentAsFloat(value.alpha()))
        .endArray();

    } else {
      out.value(value.value());
    }
  }

  @Override
  public ShadowColor read(final JsonReader in) throws IOException {
    if (in.peek() == JsonToken.BEGIN_ARRAY) {
      in.beginArray();
      final double r = in.nextDouble();
      final double g = in.nextDouble();
      final double b = in.nextDouble();
      final double a = in.nextDouble();
      if (in.peek() != JsonToken.END_ARRAY) {
        throw new JsonParseException("Failed to parse shadow colour at " + in.getPath() + ": expected end of 4-element array but got " + in.peek() + " instead.");
      }
      in.endArray();

      return ShadowColor.shadowColor(
        componentFromFloat(r),
        componentFromFloat(g),
        componentFromFloat(b),
        componentFromFloat(a)
      );
    }

    return ShadowColor.shadowColor(in.nextInt());
  }

  static float componentAsFloat(final int element) {
    return (float) element / 0xff;
  }

  static int componentFromFloat(final double element) {
    return (int) (((float) element) * 0xff);
  }
}
