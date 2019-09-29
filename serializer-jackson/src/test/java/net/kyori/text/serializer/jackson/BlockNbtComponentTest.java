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

import com.fasterxml.jackson.databind.JsonNode;
import net.kyori.text.BlockNbtComponent;
import net.kyori.text.BlockNbtComponent.WorldPos.Coordinate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlockNbtComponentTest extends AbstractComponentTest<BlockNbtComponent> {
  @Override
  Stream<Map.Entry<BlockNbtComponent, JsonNode>> tests() {
    return Stream.of(
      entry(
        BlockNbtComponent.builder().nbtPath("abc").localPos(1.23d, 2.0d, 3.89d).build(),
        json -> {
          json.put(ComponentSerializer.NBT, "abc");
          json.put(ComponentSerializer.NBT_INTERPRET, false);
          json.put(ComponentSerializer.NBT_BLOCK, "^1.23 ^2.0 ^3.89");
        }
      ),
      entry(
        BlockNbtComponent.builder().nbtPath("xyz").absoluteWorldPos(4, 5, 6).interpret(true).build(),
        json -> {
          json.put(ComponentSerializer.NBT, "xyz");
          json.put(ComponentSerializer.NBT_INTERPRET, true);
          json.put(ComponentSerializer.NBT_BLOCK, "4 5 6");
        }
      ),
      entry(
        BlockNbtComponent.builder().nbtPath("eeee").relativeWorldPos(7, 83, 900).build(),
        json -> {
          json.put(ComponentSerializer.NBT, "eeee");
          json.put(ComponentSerializer.NBT_INTERPRET, false);
          json.put(ComponentSerializer.NBT_BLOCK, "~7 ~83 ~900");
        }
      ),
      entry(
        BlockNbtComponent.builder().nbtPath("qwert").worldPos(Coordinate.absolute(12), Coordinate.relative(3), Coordinate.absolute(1200)).build(),
        json -> {
          json.put(ComponentSerializer.NBT, "qwert");
          json.put(ComponentSerializer.NBT_INTERPRET, false);
          json.put(ComponentSerializer.NBT_BLOCK, "12 ~3 1200");
        }
      )
    );
  }

  @Test
  void testLocalPosNoDecimal() throws IOException {
    assertEquals(
      BlockNbtComponent.LocalPos.of(1.0d, 2.0d, 3.89d),
      JacksonComponentSerializer.MAPPER.convertValue("^1 ^2 ^3.89", BlockNbtComponent.Pos.class)
    );
  }
}
