/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.Nullable;

/*
 * This is a hack.
 */
final class TextColorWrapper {
  final @Nullable TextColor color;
  final @Nullable TextDecoration decoration;
  final boolean reset;

  TextColorWrapper(final @Nullable TextColor color, final @Nullable TextDecoration decoration, final boolean reset) {
    this.color = color;
    this.decoration = decoration;
    this.reset = reset;
  }

  static class Serializer implements JsonDeserializer<TextColorWrapper> {
    @Override
    public TextColorWrapper deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
      final TextColor color = deserializeColor(json, context);
      final TextDecoration decoration = color == null ? deserializeDecoration(json, context) : null;
      final boolean reset = decoration == null && (json.isJsonPrimitive() && json.getAsString().equals("reset"));
      if(color == null && decoration == null && !reset) {
        throw new JsonParseException("Don't know how to parse " + json);
      }
      return new TextColorWrapper(color, decoration, reset);
    }
  }

  private static @Nullable TextColor deserializeColor(final JsonElement json, final JsonDeserializationContext context) {
    try {
      return context.deserialize(json, TextColor.class);
    } catch(final JsonParseException e) {
      return null;
    }
  }

  private static @Nullable TextDecoration deserializeDecoration(final JsonElement json, final JsonDeserializationContext context) {
    try {
      return context.deserialize(json, TextDecoration.class);
    } catch(final JsonParseException e) {
      return null;
    }
  }
}
