/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.serializer.configurate4;

import java.lang.reflect.Type;
import java.util.function.Predicate;
import net.kyori.adventure.text.BlockNBTComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

final class BlockNBTPosSerializer extends ScalarSerializer<BlockNBTComponent.Pos> {
  static final BlockNBTPosSerializer INSTANCE = new BlockNBTPosSerializer();

  private BlockNBTPosSerializer() {
    super(BlockNBTComponent.Pos.class);
  }

  @Override
  public BlockNBTComponent.Pos deserialize(final @NotNull Type type, final @NotNull Object obj) throws SerializationException {
    try {
      return BlockNBTComponent.Pos.fromString(obj.toString());
    } catch (final IllegalArgumentException ex) {
      throw new SerializationException(ex);
    }
  }

  @Override
  public Object serialize(final BlockNBTComponent.@NotNull Pos item, final @NotNull Predicate<Class<?>> typeSupported) {
    return item.asString();
  }
}
