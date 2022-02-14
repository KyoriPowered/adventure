/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public interface ConfigurateTestBase {
  ConfigurationOptions OPTIONS = ConfigurationOptions.defaults()
    .withSerializers(s -> ConfigurateComponentSerializer.builder().build().addSerializersTo(s))
    .withNativeTypes(ImmutableSet.of(String.class, Integer.class, Boolean.class, Double.class, Float.class));

  default ConfigurationNode node() {
    return ConfigurationNode.root(OPTIONS);
  }

  default ConfigurationNode node(final Object value) {
    return ConfigurationNode.root(OPTIONS).setValue(value);
  }

  default ConfigurationNode node(final Consumer<ConfigurationNode> actor) {
    return ConfigurationNode.root(OPTIONS, actor);
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
      assertEquals(holder, this.node().setValue(type, value));
      assertEquals(value, holder.getValue(type));
    } catch (final ObjectMappingException ex) {
      fail(ex);
    }
  }

  default void assertRoundtrippable(final Component value, final ConfigurationNode holder) {
    assertEquals(value, this.deserialize(holder));
    assertEquals(holder, this.serialize(value));
  }
}
