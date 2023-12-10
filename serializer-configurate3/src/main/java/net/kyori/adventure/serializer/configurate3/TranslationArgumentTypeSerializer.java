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
package net.kyori.adventure.serializer.configurate3;

import com.google.common.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

final class TranslationArgumentTypeSerializer implements TypeSerializer<TranslationArgument> {
  static final TypeToken<TranslationArgument> TYPE = TypeToken.of(TranslationArgument.class);
  static final TranslationArgumentTypeSerializer INSTANCE = new TranslationArgumentTypeSerializer();

  private TranslationArgumentTypeSerializer() {
  }

  @Override
  public TranslationArgument deserialize(final TypeToken<?> type, final ConfigurationNode node) throws ObjectMappingException {
    final Object raw = node.getValue();
    if (raw instanceof Boolean) {
      return TranslationArgument.bool((Boolean) raw);
    } else if (raw instanceof Number) {
      return TranslationArgument.numeric((Number) raw);
    } else {
      return TranslationArgument.component(node.getValue(ComponentTypeSerializer.TYPE));
    }
  }

  @Override
  public void serialize(final TypeToken<?> type, final @Nullable TranslationArgument obj, final ConfigurationNode node) throws ObjectMappingException {
    if (obj == null) {
      node.setValue(null);
      return;
    }

    final Object value = obj.value();
    if (value instanceof Boolean || value instanceof Number) {
      node.setValue(value);
    } else if (value instanceof Component) {
      node.setValue(ComponentTypeSerializer.TYPE, (Component) value);
    } else {
      throw new ObjectMappingException("Unknown translation arg value of type " + value.getClass() + ": " + value);
    }
  }
}
