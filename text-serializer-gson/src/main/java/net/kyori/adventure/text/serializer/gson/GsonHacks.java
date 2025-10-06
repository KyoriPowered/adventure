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
    return switch (peek) {
      case BOOLEAN -> in.nextBoolean();
      case STRING -> Boolean.parseBoolean(in.nextString());
      case NUMBER -> in.nextString().equals("1");
      case null, default -> throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a boolean");
    };
  }

  static String readString(final JsonReader in) throws IOException {
    final JsonToken peek = in.peek();
    return switch (peek) {
      case STRING, NUMBER -> in.nextString();
      case BOOLEAN -> String.valueOf(in.nextBoolean());
      case null, default -> throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a string");
    };
  }
}
