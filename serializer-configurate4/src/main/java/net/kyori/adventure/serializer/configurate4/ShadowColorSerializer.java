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
package net.kyori.adventure.serializer.configurate4;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.format.ShadowColor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

final class ShadowColorSerializer implements TypeSerializer<ShadowColor> {
  static final TypeSerializer<ShadowColor> INSTACE = new ShadowColorSerializer(false);

  private boolean emitFloats;

  private ShadowColorSerializer(final boolean emitFloats) {
    this.emitFloats = emitFloats;
  }

  @Override
  public ShadowColor deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
    if (node.isList()) {
      final List<Float> floats = node.getList(Float.class);
      if (floats.size() != 4) {
        throw new SerializationException(node, ShadowColor.class, "Expected a 4-element float array ([R, G, B, A]), but got a " + floats.size() + "-long array instead.");
      }
      final int r = componentFromFloat(floats.get(0));
      final int g = componentFromFloat(floats.get(1));
      final int b = componentFromFloat(floats.get(2));
      final int a = componentFromFloat(floats.get(3));

      return ShadowColor.shadowColor(r, g, b, a);
    } else {
      return ShadowColor.shadowColor(node.getInt());
    }
  }

  @Override
  public void serialize(final Type type, final @Nullable ShadowColor obj, final ConfigurationNode node) throws SerializationException {
    if (obj == null) {
      node.raw(null);
      return;
    }

    if (this.emitFloats) {
      node.set(Collections.emptyList());
      node.appendListNode().set(componentAsFloat(obj.red()));
      node.appendListNode().set(componentAsFloat(obj.green()));
      node.appendListNode().set(componentAsFloat(obj.blue()));
      node.appendListNode().set(componentAsFloat(obj.alpha()));
    } else {
      node.set(obj.value());
    }
  }

  static float componentAsFloat(final int element) {
    return (float) element / 0xff;
  }

  static int componentFromFloat(final double element) {
    return (int) (((float) element) * 0xff);
  }
}
