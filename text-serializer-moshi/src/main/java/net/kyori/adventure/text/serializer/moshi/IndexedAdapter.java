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
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import java.io.IOException;
import net.kyori.adventure.util.Index;

final class IndexedAdapter<E> extends JsonAdapter<E> {

  public static <E> JsonAdapter<E> of(final String name, final Index<String, E> map) {
    return new IndexedAdapter<>(name, map);
  }

  private final String name;
  private final Index<String, E> map;

  private IndexedAdapter(final String name, final Index<String, E> map) {
    this.name = name;
    this.map = map;
  }

  @Override
  public E fromJson(final JsonReader reader) throws IOException {
    final String string = reader.nextString();
    final E value = this.map.value(string);
    if (value != null) {
      return value;
    } else {
      throw new JsonDataException("invalid " + this.name + ": " + string);
    }
  }

  @Override
  public void toJson(final JsonWriter writer, final E value) throws IOException {
    writer.value(this.map.key(value));
  }
}
