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

import com.google.common.collect.ImmutableSet;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.util.CheckedConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public interface ConfigurateTestBase {
  ConfigurationOptions OPTIONS = ConfigurationOptions.defaults()
    .serializers(s -> s.registerAll(ConfigurateComponentSerializer.builder().build().serializers()))
    .nativeTypes(ImmutableSet.of(String.class, Integer.class, Boolean.class, Double.class, Float.class));

  default BasicConfigurationNode node() {
    return BasicConfigurationNode.root(OPTIONS);
  }

  default BasicConfigurationNode node(final Object value) {
    return BasicConfigurationNode.root(OPTIONS).raw(value);
  }

  default <E extends Exception> BasicConfigurationNode node(final CheckedConsumer<BasicConfigurationNode, E> actor) throws E {
    return BasicConfigurationNode.root(OPTIONS, actor);
  }

  default ConfigurationNode serialize(final Component component) {
    return ConfigurateComponentSerializer.configurate().serialize(component);
  }

  default Component deserialize(final ConfigurationNode node) {
    return ConfigurateComponentSerializer.configurate().deserialize(node);
  }

  // make sure the value can be roundtripped
  default <T> void assertRoundtrippable(final TypeToken<T> type, final T value, final ConfigurationNode holder) {
    try {
      assertEquals(holder, this.node().set(type, value));
      assertEquals(value, holder.get(type));
    } catch (final SerializationException ex) {
      fail(ex);
    }
  }

  // make sure the value can be roundtripped
  default <T> void assertRoundtrippable(final Class<T> type, final T value, final ConfigurationNode holder) {
    try {
      assertEquals(holder, this.node().set(type, value));
      assertEquals(value, holder.get(type));
    } catch (final SerializationException ex) {
      fail(ex);
    }
  }

  default void assertRoundtrippable(final Component value, final ConfigurationNode holder) {
    assertEquals(value, this.deserialize(holder));
    assertEquals(holder, this.serialize(value));
  }
}
