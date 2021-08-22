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

import com.squareup.moshi.Moshi;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class MoshiTest<T> {
  private final Moshi moshi;
  private final Class<T> type;

  MoshiTest(final Moshi moshi, final Class<T> type) {
    this.moshi = moshi;
    this.type = type;
  }

  final void test(final T object, final Object json) {
    assertEquals(json, this.serialize(object));
    assertEquals(object, this.deserialize(json));
  }

  final Object serialize(final T object) {
    return this.moshi.adapter(this.type).toJsonValue(object);
  }

  final T deserialize(final Object json) {
    return this.moshi.adapter(this.type).fromJsonValue(json);
  }

  static List<Object> array(final Consumer<? super List<Object>> consumer) {
    final List<Object> json = new ArrayList<>();
    consumer.accept(json);
    return json;
  }

  static Map<String, Object> object(final Consumer<? super Map<String, Object>> consumer) {
    final Map<String, Object> json = new LinkedHashMap<>();
    consumer.accept(json);
    return json;
  }
}
