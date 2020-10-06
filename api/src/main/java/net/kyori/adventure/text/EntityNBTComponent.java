/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text;

import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An entity NBT component.
 *
 * @since 4.0.0
 */
public interface EntityNBTComponent extends NBTComponent<EntityNBTComponent, EntityNBTComponent.Builder>, ScopedComponent<EntityNBTComponent> {
  /**
   * Creates an entity NBT component builder.
   *
   * @return a builder
   * @since 4.0.0
   * @deprecated use {@link Component#entityNBT()}
   */
  @Deprecated
  static @NonNull Builder builder() {
    return Component.entityNBT();
  }

  /**
   * Creates a entity NBT component with a position.
   *
   * @param nbtPath the nbt path
   * @param selector the selector
   * @return an entity NBT component
   * @since 4.0.0
   * @deprecated use {@link Component#entityNBT(String, String)}
   */
  @Deprecated
  static @NonNull EntityNBTComponent of(final @NonNull String nbtPath, final @NonNull String selector) {
    return Component.entityNBT(nbtPath, selector);
  }

  /**
   * Creates a entity NBT component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return an entity NBT component
   * @since 4.0.0
   * @deprecated use {@link Component#entityNBT(Consumer)}
   */
  @Deprecated
  static @NonNull EntityNBTComponent make(final @NonNull Consumer<? super Builder> consumer) {
    return Component.entityNBT(consumer);
  }

  /**
   * Gets the entity selector.
   *
   * @return the entity selector
   * @since 4.0.0
   */
  @NonNull String selector();

  /**
   * Sets the entity selector.
   *
   * @param selector the entity selector
   * @return an entity NBT component
   * @since 4.0.0
   */
  @NonNull EntityNBTComponent selector(final @NonNull String selector);

  /**
   * An entity NBT component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends NBTComponentBuilder<EntityNBTComponent, Builder> {
    /**
     * Sets the entity selector.
     *
     * @param selector the entity selector
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder selector(final @NonNull String selector);
  }
}
