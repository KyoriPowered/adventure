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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

final class TranslationArgumentTypeSerializer implements TypeSerializer<TranslationArgument> {
  static final TranslationArgumentTypeSerializer INSTANCE = new TranslationArgumentTypeSerializer();

  private TranslationArgumentTypeSerializer() {
  }

  @Override
  public TranslationArgument deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
    final Object raw = node.rawScalar();
    if (raw instanceof Boolean) {
      return TranslationArgument.bool((Boolean) raw);
    } else if (raw instanceof Number) {
      return TranslationArgument.numeric((Number) raw);
    } else {
      return TranslationArgument.component(node.require(Component.class));
    }
  }

  @Override
  public void serialize(final Type type, final @Nullable TranslationArgument obj, final ConfigurationNode node) throws SerializationException {
    if (obj == null) {
      node.set(null);
      return;
    }

    final Object value = obj.value();
    if (value instanceof Boolean || value instanceof Number) {
      node.set(value);
    } else if (value instanceof Component) {
      node.set(Component.class, value);
    } else {
      throw new SerializationException(node, type, "Unknown translation arg value of type " + value.getClass() + ": " + value);
    }
  }
}
