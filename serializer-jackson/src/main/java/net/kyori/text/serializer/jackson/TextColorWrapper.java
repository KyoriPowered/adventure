/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;

class TextColorWrapper {
  static final Deserializer DESERIALIZER = new Deserializer();

  final @Nullable TextColor color;
  final @Nullable TextDecoration decoration;
  final boolean reset;

  private TextColorWrapper(final @Nullable TextColor color, final @Nullable TextDecoration decoration, final boolean reset) {
    this.color = color;
    this.decoration = decoration;
    this.reset = reset;
  }

  private static TextColor deserializeColor(final JsonParser p, final DeserializationContext ctxt) {
    try {
      return p.readValueAs(TextColor.class);
    } catch(final IOException e) {
      return null;
    }
  }

  private static TextDecoration deserializeDecoration(final JsonParser p, final DeserializationContext ctxt) {
    try {
      return p.readValueAs(TextDecoration.class);
    } catch(final IOException e) {
      return null;
    }
  }

  static class Deserializer extends JsonDeserializer<TextColorWrapper> {
    @Override
    public TextColorWrapper deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
      final TextColor color = deserializeColor(p, ctxt);
      final TextDecoration decoration = color == null ? deserializeDecoration(p, ctxt) : null;
      final boolean reset = decoration == null && "reset".equals(p.getValueAsString());
      if(color == null && decoration == null && !reset) {
        return ctxt.reportInputMismatch(TextColorWrapper.class, "Don't know how to parse TextColorWrapper");
      }
      return new TextColorWrapper(color, decoration, reset);
    }
  }
}
