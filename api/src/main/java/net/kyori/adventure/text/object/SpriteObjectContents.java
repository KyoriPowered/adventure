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
package net.kyori.adventure.text.object;

import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * A sprite contents.
 *
 * <p>Represents a sprite in an atlas, such as a block texture.</p>
 *
 * @sinceMinecraft 1.21.9
 * @since 4.25.0
 */
public interface SpriteObjectContents extends ObjectContents {
  /**
   * The default atlas key for sprites, used by vanilla when the atlas is not specified in a serialized object component.
   *
   * @since 4.25.0
   */
  Key DEFAULT_ATLAS = Key.key("minecraft:blocks");

  /**
   * Gets the atlas key.
   *
   * @return the atlas key
   * @since 4.25.0
   */
  @NotNull Key atlas();

  /**
   * Gets the sprite key.
   *
   * @return the sprite key
   * @since 4.25.0
   */
  @NotNull Key sprite();

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("atlas", this.atlas()),
      ExaminableProperty.of("sprite", this.sprite())
    );
  }
}
