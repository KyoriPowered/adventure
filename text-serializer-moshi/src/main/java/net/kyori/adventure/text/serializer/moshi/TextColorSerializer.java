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
package net.kyori.adventure.text.serializer.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import java.io.IOException;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TextColorSerializer extends JsonAdapter<TextColor> {
  static final JsonAdapter<TextColor> INSTANCE = new TextColorSerializer(false).nullSafe();
  static final JsonAdapter<TextColor> DOWNSAMPLE_COLOR = new TextColorSerializer(true).nullSafe();

  private final boolean downsampleColor;

  private TextColorSerializer(final boolean downsampleColor) {
    this.downsampleColor = downsampleColor;
  }

  @Override
  public @Nullable TextColor fromJson(final JsonReader reader) throws IOException {
    final @Nullable TextColor color = fromString(reader.nextString());
    if (color == null) {
      return null;
    }

    return this.downsampleColor ? NamedTextColor.nearestTo(color) : color;
  }

  @Override
  public void toJson(final JsonWriter writer, final TextColor value) throws IOException {
    if (value instanceof NamedTextColor) {
      writer.value(NamedTextColor.NAMES.key((NamedTextColor) value));
    } else if (this.downsampleColor) {
      writer.value(NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)));
    } else {
      writer.value(value.asHexString());
    }
  }

  static @Nullable TextColor fromString(final @NotNull String value) {
    if (value.startsWith("#")) {
      return TextColor.fromHexString(value);
    } else {
      return NamedTextColor.NAMES.value(value);
    }
  }
}
