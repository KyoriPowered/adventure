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

import java.util.List;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;

import static java.util.Objects.requireNonNull;

/**
 * An object component contents.
 *
 * @since 4.25.0
 * @sinceMinecraft 1.21.9
 */
public sealed interface ObjectContents extends ObjectContentsLike permits SpriteObjectContents, PlayerHeadObjectContents {
  /**
   * Creates a sprite contents with the given atlas and sprite.
   *
   * @param atlas  the atlas
   * @param sprite the sprite
   * @return a sprite contents
   * @since 4.25.0
   */
  @Contract(value = "_, _ -> new", pure = true)
  static SpriteObjectContents sprite(final Key atlas, final Key sprite) {
    return new SpriteObjectContentsImpl(requireNonNull(atlas, "atlas"), requireNonNull(sprite, "sprite"));
  }

  /**
   * Creates a sprite contents with the given sprite and the default atlas.
   *
   * @param sprite the sprite
   * @return a sprite contents
   * @since 4.25.0
   */
  @Contract(value = "_ -> new", pure = true)
  static SpriteObjectContents sprite(final Key sprite) {
    return new SpriteObjectContentsImpl(SpriteObjectContents.DEFAULT_ATLAS, requireNonNull(sprite, "sprite"));
  }

  /**
   * Creates a player head contents builder.
   *
   * @return a player head contents builder
   * @since 4.25.0
   */
  @Contract(value = "-> new", pure = true)
  static PlayerHeadObjectContents.Builder playerHead() {
    return new PlayerHeadObjectContentsImpl.BuilderImpl();
  }

  /**
   * Creates a player head contents with the given name.
   *
   * @param name the player name
   * @return a player head contents
   * @since 4.25.0
   */
  @Contract(value = "_ -> new", pure = true)
  static PlayerHeadObjectContents playerHead(final String name) {
    return new PlayerHeadObjectContentsImpl(name, null, List.of(), true, null);
  }

  /**
   * Creates a player head contents with the given id.
   *
   * @param id the player UUID
   * @return a player head contents
   * @since 4.25.0
   */
  @Contract(value = "_ -> new", pure = true)
  static PlayerHeadObjectContents playerHead(final UUID id) {
    return new PlayerHeadObjectContentsImpl(null, id, List.of(), true, null);
  }

  /**
   * Creates a player head contents with the given parameters.
   *
   * @param skinSource the skin source
   * @return a player head contents
   * @since 4.25.0
   */
  @Contract(value = "_ -> new", pure = true)
  static PlayerHeadObjectContents playerHead(final PlayerHeadObjectContents.SkinSource skinSource) {
    return playerHead().skin(skinSource).build();
  }

  @Override
  default ObjectContents asObjectContents() {
    return this;
  }
}
