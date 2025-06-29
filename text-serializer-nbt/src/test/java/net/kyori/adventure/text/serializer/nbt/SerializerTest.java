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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class SerializerTest {

  private NBTComponentSerializer serializer;

  @BeforeEach
  public void setUpSerializer() {
    this.serializer = this.createSerializer();
  }

  protected void test(@NotNull Component component, @NotNull BinaryTag tag) {
    assertEquals(tag, this.serialize(component));
    assertEquals(component, this.deserialize(tag));
  }

  protected void test(@NotNull Style style, @NotNull CompoundBinaryTag tag) {
    this.test(this.serializer, style, tag);
  }

  protected void test(@NotNull NBTComponentSerializer serializer,
                      @NotNull Style style, @NotNull CompoundBinaryTag tag) {
    assertEquals(tag, serializer.serializeStyle(style));
    assertEquals(style, serializer.deserializeStyle(tag));
  }

  protected @NotNull Component deserialize(@NotNull BinaryTag tag) {
    return this.serializer.deserialize(tag);
  }

  protected @NotNull BinaryTag serialize(@NotNull Component component) {
    return this.serializer.serialize(component);
  }

  protected @NotNull NBTComponentSerializer createSerializer() {
    return NBTComponentSerializer.nbt();
  }
}
