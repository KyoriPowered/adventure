/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.nbt.dfu;

import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.Test;

public class ShowItemCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertCodec(
      AdventureCodecs.SHOW_ITEM,
      HoverEvent.ShowItem.showItem(
        Key.key("stone"), 1
      )
    );
    assertCodec(
      AdventureCodecs.SHOW_ITEM,
      HoverEvent.ShowItem.showItem(
        Key.key("stone"), 1,
        BinaryTagHolder.binaryTagHolder("{inValidPath: '123'}")
      )
    );
    final Map<Key, DataComponentValue> componentValueMap = new HashMap<>();
    componentValueMap.put(Key.key("in_valid_path"), BinaryTagHolder.binaryTagHolder("123"));
    assertCodec(
      AdventureCodecs.SHOW_ITEM,
      HoverEvent.ShowItem.showItem(
        Key.key("stone"), 1,
        componentValueMap
      )
    );
  }
}
