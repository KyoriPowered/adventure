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

import net.kyori.adventure.text.event.DataComponentValue;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;

/**
 * A data component value that can integrate with configuration nodes.
 *
 * @since 4.17.0
 */
public interface ConfigurateDataComponentValue extends DataComponentValue {
  /**
   * Create a data component value capturing the value of an existing node.
   *
   * @param existing the existing node
   * @return the captured value
   * @since 4.17.0
   */
  static @NotNull ConfigurateDataComponentValue capturingDataComponentValue(final @NotNull ConfigurationNode existing) {
    return SnapshottingConfigurateDataComponentValue.create(existing);
  }

  /**
   * Apply the contained value to the supplied node.
   *
   * @param node the node to apply this value to
   * @since 4.17.0
   */
  void applyTo(final @NotNull ConfigurationNode node);
}
