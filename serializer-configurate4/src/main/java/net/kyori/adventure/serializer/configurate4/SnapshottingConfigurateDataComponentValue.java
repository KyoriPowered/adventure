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

import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.AttributedConfigurationNode;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

final class SnapshottingConfigurateDataComponentValue implements ConfigurateDataComponentValue {
  private final ConfigurationNode ownedNode;

  // capture the value of an existing node without exposing any mutable state
  static @NotNull SnapshottingConfigurateDataComponentValue create(final ConfigurationNode existing) {
    final ConfigurationNode owned;
    if (existing instanceof AttributedConfigurationNode) {
      owned = AttributedConfigurationNode.root(((AttributedConfigurationNode) existing).tagName(), existing.options());
    } else if (existing instanceof CommentedConfigurationNode) {
      owned = CommentedConfigurationNode.root(existing.options());
    } else {
      owned = BasicConfigurationNode.root(existing.options());
    }

    owned.from(existing);

    return new SnapshottingConfigurateDataComponentValue(owned);
  }

  private SnapshottingConfigurateDataComponentValue(final ConfigurationNode owned) {
    this.ownedNode = owned;
  }

  @Override
  public void applyTo(final @NotNull ConfigurationNode node) {
    node.from(this.ownedNode);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("ownedNode", this.ownedNode)
    );
  }
}
