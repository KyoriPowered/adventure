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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;

public class JacksonComponentSerializer implements ComponentSerializer<Component, Component, String> {
  public static final JacksonComponentSerializer INSTANCE = new JacksonComponentSerializer();
  static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new KyoriTextModule());

  @NonNull
  @Override
  public Component deserialize(@NonNull final String input) throws JacksonDeserializeException {
    try {
      return MAPPER.readValue(input, Component.class);
    } catch(final IOException e) {
      throw new JacksonDeserializeException("Can't deserialize component", e);
    }
  }

  @NonNull
  @Override
  public String serialize(@NonNull final Component component) {
    try {
      return MAPPER.writeValueAsString(component);
    } catch(final JsonProcessingException e) {
      throw new JacksonSerializeException("Can't serialize component", e);
    }
  }
}
