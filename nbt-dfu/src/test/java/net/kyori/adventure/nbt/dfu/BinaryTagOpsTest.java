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

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.examination.string.StringExaminer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinaryTagOpsTest {

  private <T> T valueOrThrow(final DataResult<T> result) {
    result.error().ifPresent(err -> {
      throw new RuntimeException("Error with data result: " + err.message());
    });

    return result.result().orElseThrow(() -> new IllegalStateException("Neither result or error was present"));
  }

  @Test
  public void testStringCreation() {

  }

  @Test
  void testComplexObject() {
    final TestValue subject = new TestValue("meow", 28, Key.key("adventure", "purr"));
    final DataResult<BinaryTag> tag = TestValue.CODEC.encode(subject, BinaryTagOps.UNCOMPRESSED, BinaryTagOps.UNCOMPRESSED.empty());
    final CompoundBinaryTag compound = (CompoundBinaryTag) this.valueOrThrow(tag);

    final TestValue reversed = this.valueOrThrow(TestValue.CODEC.decode(BinaryTagOps.UNCOMPRESSED, compound)).getFirst();
    assertEquals(subject, reversed);

    System.out.println(compound.examine(StringExaminer.simpleEscaping()));
  }

  @Test
  void testMergeListWithEmpty() {
    final BinaryTag tag = this.valueOrThrow(BinaryTagOps.UNCOMPRESSED.mergeToList(BinaryTagOps.UNCOMPRESSED.empty(), StringBinaryTag.stringBinaryTag("test")));
    assertEquals(ListBinaryTag.builder().add(StringBinaryTag.stringBinaryTag("test")).build(), tag);
  }

  @Test
  void testMergeWithCompound() {

  }

  @Test
  void testListAdd() {

  }

  @Test
  void testListAddToEmpty() {

  }

  record TestValue(String name, int amount, Key ident) {
    public static final Codec<TestValue> CODEC = RecordCodecBuilder.create(o -> o.group(
      Codec.STRING.fieldOf("name").forGetter(t -> t.name),
      Codec.INT.fieldOf("amount").forGetter(t -> t.amount),
      AdventureCodecs.KEY.fieldOf("key").forGetter(t -> t.ident)
    ).apply(o, TestValue::new));
  }
}
