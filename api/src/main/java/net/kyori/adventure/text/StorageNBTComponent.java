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
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An storage NBT component.
 *
 * @since 4.0.0
 */
public interface StorageNBTComponent extends NBTComponent<StorageNBTComponent, StorageNBTComponent.Builder>, ScopedComponent<StorageNBTComponent> {
  /**
   * Creates an storage NBT component builder.
   *
   * @return a builder
   * @since 4.0.0
   * @deprecated use {@link Component#storageNBT()}
   */
  @Deprecated
  static @NonNull Builder builder() {
    return Component.storageNBT();
  }

  /**
   * Creates a storage NBT component with a path and an storage ID.
   *
   * @param nbtPath the nbt path
   * @param storage the identifier of the storage
   * @return a storage NBT component
   * @since 4.0.0
   * @deprecated use {@link Component#storageNBT(String, Key)}
   */
  @Deprecated
  static @NonNull StorageNBTComponent of(final @NonNull String nbtPath, final @NonNull Key storage) {
    return Component.storageNBT(nbtPath, storage);
  }

  /**
   * Creates a storage NBT component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return a storage NBT component
   * @since 4.0.0
   * @deprecated use {@link Component#storageNBT(Consumer)}
   */
  @Deprecated
  static @NonNull StorageNBTComponent make(final @NonNull Consumer<? super Builder> consumer) {
    return Component.storageNBT(consumer);
  }

  /**
   * Gets the NBT storage's ID.
   *
   * @return the NBT storage
   * @since 4.0.0
   */
  @NonNull Key storage();

  /**
   * Sets the NBT storage.
   *
   * @param storage the identifier of the NBT storage
   * @return a storage NBT component
   * @since 4.0.0
   */
  @NonNull StorageNBTComponent storage(final @NonNull Key storage);

  /**
   * A command storage NBT component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends NBTComponentBuilder<StorageNBTComponent, Builder> {
    /**
     * Sets the NBT storage.
     *
     * @param storage the id of the NBT storage
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder storage(final @NonNull Key storage);
  }
}
