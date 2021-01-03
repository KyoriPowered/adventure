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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class TextColorSerializer extends TypeAdapter<TextColor> {
  static final TypeAdapter<TextColor> INSTANCE = new TextColorSerializer(false).nullSafe();
  static final TypeAdapter<TextColor> DOWNSAMPLE_COLOR = new TextColorSerializer(true).nullSafe();

  private final boolean downsampleColor;

  private TextColorSerializer(final boolean downsampleColor) {
    this.downsampleColor = downsampleColor;
  }

  @Override
  public void write(final JsonWriter out, final TextColor value) throws IOException {
    if(value instanceof NamedTextColor) {
      out.value(NamedTextColor.NAMES.key((NamedTextColor) value));
    } else if(this.downsampleColor) {
      out.value(NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)));
    } else {
      out.value(value.asHexString());
    }
  }

  @Override
  public @Nullable TextColor read(final JsonReader in) throws IOException {
    final @Nullable TextColor color = fromString(in.nextString());
    if(color == null) return null;

    return this.downsampleColor ? NamedTextColor.nearestTo(color) : color;
  }

  static @Nullable TextColor fromString(final @NonNull String value) {
    if(value.startsWith("#")) {
      return TextColor.fromHexString(value);
    } else {
      return NamedTextColor.NAMES.value(value);
    }
  }
}
