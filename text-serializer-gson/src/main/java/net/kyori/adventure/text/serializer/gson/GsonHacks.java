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

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import org.jetbrains.annotations.Nullable;

final class GsonHacks {
  private GsonHacks() {
  }

  static boolean isNullOrEmpty(final @Nullable JsonElement element) {
    return element == null ||
           element.isJsonNull() ||
           (element.isJsonArray() && element.getAsJsonArray().size() == 0) ||
           (element.isJsonObject() && element.getAsJsonObject().entrySet().isEmpty());
  }

  static boolean readBoolean(final JsonReader in) throws IOException {
    final JsonToken peek = in.peek();
    if (peek == JsonToken.BOOLEAN) {
      return in.nextBoolean();
    } else if (peek == JsonToken.STRING) {
      return Boolean.parseBoolean(in.nextString());
    } else if (peek == JsonToken.NUMBER) {
      return in.nextString().equals("1");
    } else {
      throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a boolean");
    }
  }

  static String readString(final JsonReader in) throws IOException {
    final JsonToken peek = in.peek();
    if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
      return in.nextString();
    } else if (peek == JsonToken.BOOLEAN) {
      return String.valueOf(in.nextBoolean());
    } else {
      throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a string");
    }
  }
}
