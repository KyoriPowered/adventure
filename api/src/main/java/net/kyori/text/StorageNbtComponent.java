/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

/**
 * An storage NBT component.
 */
public interface StorageNbtComponent extends NbtComponent<StorageNbtComponent, StorageNbtComponent.Builder>, ScopedComponent<StorageNbtComponent> {
  /**
   * Creates an storage NBT component builder.
   *
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new StorageNbtComponentImpl.BuilderImpl();
  }

  /**
   * Creates a storage NBT component with a path and an storage ID.
   *
   * @param nbtPath the nbt path
   * @param storage the identifier of the storage
   * @return the storage NBT component
   */
  static @NonNull StorageNbtComponent of(final @NonNull String nbtPath, final @NonNull String storage) {
    return builder().nbtPath(nbtPath).storage(storage).build();
  }

  /**
   * Creates a storage NBT component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the storage NBT component
   */
  static @NonNull StorageNbtComponent make(final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Gets the NBT storage's ID.
   *
   * @return the NBT storage
   */
  @NonNull String storage();

  /**
   * Sets the NBT storage.
   *
   * @param storage the identifier of the NBT storage
   * @return a component
   */
  @NonNull StorageNbtComponent storage(final @NonNull String storage);

  /**
   * A command storage NBT component builder.
   */
  interface Builder extends NbtComponentBuilder<StorageNbtComponent, Builder> {
    /**
     * Sets the NBT storage.
     *
     * @param storage the id of the NBT storage
     * @return this builder
     */
    @NonNull Builder storage(final @NonNull String storage);
  }
}
