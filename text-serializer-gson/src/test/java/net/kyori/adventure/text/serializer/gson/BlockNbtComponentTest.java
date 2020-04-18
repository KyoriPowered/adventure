/*
 * This file is part of text, licensed under the MIT License.
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

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Map;
import java.util.stream.Stream;
import net.kyori.adventure.text.BlockNbtComponent;
import net.kyori.adventure.text.BlockNbtComponent.WorldPos.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlockNbtComponentTest extends AbstractComponentTest<BlockNbtComponent> {
  @Override
  Stream<Map.Entry<BlockNbtComponent, JsonElement>> tests() {
    return Stream.of(
      entry(
        BlockNbtComponent.builder().nbtPath("abc").localPos(1.23d, 2.0d, 3.89d).build(),
        json -> {
          json.addProperty(ComponentSerializerImpl.NBT, "abc");
          json.addProperty(ComponentSerializerImpl.NBT_INTERPRET, false);
          json.addProperty(ComponentSerializerImpl.NBT_BLOCK, "^1.23 ^2.0 ^3.89");
        }
      ),
      entry(
        BlockNbtComponent.builder().nbtPath("xyz").absoluteWorldPos(4, 5, 6).interpret(true).build(),
        json -> {
          json.addProperty(ComponentSerializerImpl.NBT, "xyz");
          json.addProperty(ComponentSerializerImpl.NBT_INTERPRET, true);
          json.addProperty(ComponentSerializerImpl.NBT_BLOCK, "4 5 6");
        }
      ),
      entry(
        BlockNbtComponent.builder().nbtPath("eeee").relativeWorldPos(7, 83, 900).build(),
        json -> {
          json.addProperty(ComponentSerializerImpl.NBT, "eeee");
          json.addProperty(ComponentSerializerImpl.NBT_INTERPRET, false);
          json.addProperty(ComponentSerializerImpl.NBT_BLOCK, "~7 ~83 ~900");
        }
      ),
      entry(
        BlockNbtComponent.builder().nbtPath("qwert").worldPos(Coordinate.absolute(12), Coordinate.relative(3), Coordinate.absolute(1200)).build(),
        json -> {
          json.addProperty(ComponentSerializerImpl.NBT, "qwert");
          json.addProperty(ComponentSerializerImpl.NBT_INTERPRET, false);
          json.addProperty(ComponentSerializerImpl.NBT_BLOCK, "12 ~3 1200");
        }
      )
    );
  }

  @Test
  void testLocalPosNoDecimal() {
    assertEquals(
      BlockNbtComponent.LocalPos.of(1.0d, 2.0d, 3.89d),
      GsonComponentSerializer.GSON.fromJson(new JsonPrimitive("^1 ^2 ^3.89"), BlockNbtComponent.Pos.class)
    );
  }
}
