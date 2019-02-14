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
package net.kyori.text.serializer.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.text.util.NameMap;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NameMapSerializer<T> implements JsonDeserializer<T>, JsonSerializer<T> {
  private final String name;
  private final NameMap<T> map;
  private final Collection<String> ignore;

  public static <T> @NonNull NameMapSerializer<T> of(final String name, final NameMap<T> map) {
    return new NameMapSerializer<>(name, map, Collections.emptySet());
  }

  public static <T> @NonNull NameMapSerializer<T> ofIgnoring(final String name, final NameMap<T> map, final String... ignores) {
    final Set<String> ignore = new HashSet<>(ignores.length);
    Collections.addAll(ignore, ignores);
    return new NameMapSerializer<>(name, map, ignore);
  }

  @Deprecated
  public NameMapSerializer(final String name, final NameMap<T> map) {
    this(name, map, Collections.emptySet());
  }

  private NameMapSerializer(final String name, final NameMap<T> map, final Collection<String> ignore) {
    this.name = name;
    this.map = map;
    this.ignore = ignore;
  }

  @Override
  public T deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
    final String string = json.getAsString();
    if(this.ignore.contains(string)) {
      return null;
    }
    return this.map.get(string).orElseThrow(() -> new IllegalArgumentException("invalid " + this.name + ":  " + string));
  }

  @Override
  public JsonElement serialize(final T src, final Type typeOfT, final JsonSerializationContext context) {
    return new JsonPrimitive(this.map.name(src));
  }
}
