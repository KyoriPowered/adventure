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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class GsonTest<T> {
  private final Gson gson;
  private final Class<T> type;

  GsonTest(final Gson gson, final Class<T> type) {
    this.gson = gson;
    this.type = type;
  }

  final void test(final T object, final JsonElement json) {
    assertEquals(json, this.serialize(object));
    assertEquals(object, this.deserialize(json));
  }

  final JsonElement serialize(final T object) {
    return this.gson.toJsonTree(object);
  }

  final T deserialize(final JsonElement json) {
    return this.gson.fromJson(json, this.type);
  }

  static JsonArray array(final Consumer<? super JsonArray> consumer) {
    final JsonArray json = new JsonArray();
    consumer.accept(json);
    return json;
  }

  static JsonObject object(final Consumer<? super JsonObject> consumer) {
    final JsonObject json = new JsonObject();
    consumer.accept(json);
    return json;
  }
}
