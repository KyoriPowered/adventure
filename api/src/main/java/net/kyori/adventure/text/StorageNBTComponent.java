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
package net.kyori.adventure.text;

import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Given a {@link Key}, this component reads the NBT of the associated command storage and displays that information.
 *
 * <p>This component consists of:</p>
 * <dl>
 *   <dt>storage</dt>
 *   <dd>a key that represents the resource location of a command storage (eg. my_plugin:actions.punches_entity)</dd>
 *   <dt>everything in</dt>
 *   <dd>{@link NBTComponent}</dd>
 * </dl>
 *
 * @see NBTComponent
 * @since 4.0.0
 * @sinceMinecraft 1.15
 */
public interface StorageNBTComponent extends NBTComponent<StorageNBTComponent, StorageNBTComponent.Builder>, ScopedComponent<StorageNBTComponent> {
  /**
   * Gets the NBT storage's ID.
   *
   * @return the NBT storage
   * @since 4.0.0
   */
  @NotNull Key storage();

  /**
   * Sets the NBT storage.
   *
   * @param storage the identifier of the NBT storage
   * @return a storage NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NotNull StorageNBTComponent storage(final @NotNull Key storage);

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("storage", this.storage())
      ),
      NBTComponent.super.examinableProperties()
    );
  }

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
    @Contract("_ -> this")
    @NotNull Builder storage(final @NotNull Key storage);
  }
}
