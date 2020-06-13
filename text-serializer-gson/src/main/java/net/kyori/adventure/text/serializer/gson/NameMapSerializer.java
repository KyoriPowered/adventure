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
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;
import net.kyori.adventure.util.NameMap;

public final class NameMapSerializer<E> extends TypeAdapter<E> {
  private final String name;
  private final NameMap<E> map;
  
  public static  <E> TypeAdapter<E> of(final String name, final NameMap<E> map) {
    return new NameMapSerializer<>(name, map).nullSafe();
  }

  private NameMapSerializer(final String name, final NameMap<E> map) {
    this.name = name;
    this.map = map;
  }

  @Override
  public void write(final JsonWriter out, final E value) throws IOException {
    out.value(this.map.name(value));
  }

  @Override
  public E read(final JsonReader in) throws IOException {
    final String string = in.nextString();
    final Optional<E> value = this.map.value(string);
    if(value.isPresent()) {
      return value.get();
    } else {
      throw new JsonParseException("invalid " + this.name + ":  " + string);
    }
  }
}
