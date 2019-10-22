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

import com.google.gson.JsonElement;
import java.util.Map;
import java.util.stream.Stream;
import net.kyori.text.EntityNbtComponent;

class EntityNbtComponentTest extends AbstractComponentTest<EntityNbtComponent> {
  @Override
  Stream<Map.Entry<EntityNbtComponent, JsonElement>> tests() {
    return Stream.of(
      entry(
        EntityNbtComponent.builder().nbtPath("abc").selector("test").build(),
        json -> {
          json.addProperty(GsonComponentSerializer.NBT, "abc");
          json.addProperty(GsonComponentSerializer.NBT_INTERPRET, false);
          json.addProperty(GsonComponentSerializer.NBT_ENTITY, "test");
        }
      ),
      entry(
        EntityNbtComponent.builder().nbtPath("abc").selector("test").interpret(true).build(),
        json -> {
          json.addProperty(GsonComponentSerializer.NBT, "abc");
          json.addProperty(GsonComponentSerializer.NBT_INTERPRET, true);
          json.addProperty(GsonComponentSerializer.NBT_ENTITY, "test");
        }
      )
    );
  }
}
