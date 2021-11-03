/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.width;

import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A source able to return the width of text. By default accuracy can only be guaranteed for {@link TextComponent}s
 * in the standard minecraft font.
 *
 * <p>Providing context to this source is only necessary if a custom {@code Function<CX, CharacterWidthFunction>} is provided on creation</p>
 *
 * @param <CX> a context type (player, server, locale)
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public interface PixelWidthSource<CX> extends Buildable<PixelWidthSource<CX>, PixelWidthSource.Builder<CX>> {

  /**
   * A pixel width source calculating width using the provided flattener and character width function.
   *
   * <p>A null value results in using a basic counterpart:</p>
   * <p>{@link net.kyori.adventure.text.flattener.ComponentFlattener#basic()}</p>
   * <p>{@link net.kyori.adventure.text.width.DefaultCharacterWidthFunction#INSTANCE}</p>
   *
   * @param flattener a flattener used to turn components into linear text
   * @param function a function that provides a character width function
   * @param <CX> context a context type (player, server, locale)
   * @return a pixel width source
   * @since 4.10.0
   */
  static <CX> @NotNull PixelWidthSource<CX> pixelWidthSource(final @Nullable ComponentFlattener flattener, final @Nullable Function<@Nullable CX, CharacterWidthFunction> function) {
    return new PixelWidthSourceImpl<>(flattener == null ? ComponentFlattener.basic() : flattener, function == null ? cx -> DefaultCharacterWidthFunction.INSTANCE : function);
  }

  /**
   * Calculates the pixel width of a component, given a context.
   *
   * @param component a component
   * @param context the context of this calculation
   * @return the pixel width of the component
   * @since 4.10.0
   */
  float width(final @NotNull Component component, final @Nullable CX context);

  /**
   * Calculates the pixel width of a component without any context.
   *
   * @param component a component
   * @return the pixel width of the component
   * @since 4.10.0
   */
  default float width(final @NotNull Component component) {
    return this.width(component, null);
  }

  /**
   * Calculates the pixel width of a string, given a context.
   *
   * @param string a string
   * @param style the style of the string
   * @param context the context of this calculation
   * @return the pixel width of the string
   * @since 4.10.0
   */
  float width(final @NotNull String string, final @NotNull Style style, final @Nullable CX context);

  /**
   * Calculates the pixel width of a string without any context.
   *
   * @param string a string
   * @param style the style of the string
   * @return the pixel width of the string
   * @since 4.10.0
   */
  default float width(final @NotNull String string, final @NotNull Style style) {
    return this.width(string, style, null);
  }

  /**
   * Calculates the pixel width of a character, given a context.
   *
   * @param character a character
   * @param style the style of the character
   * @param context the context of this calculation
   * @return the pixel width of the character
   * @since 4.10.0
   */
  float width(final char character, final @NotNull Style style, final @Nullable CX context);

  /**
   * Calculates the pixel width of a character without any context.
   *
   * @param character a character
   * @param style the style of the character
   * @return the pixel width of the character
   * @since 4.10.0
   */
  default float width(final char character, final @NotNull Style style) {
    return this.width(character, style, null);
  }

  /**
   * Calculates the pixel width of a character represented by a codepoint, given a context.
   *
   * @param codepoint a codepoint representing a character
   * @param style the style of the character
   * @param context the context of this calculation
   * @return the pixel width of the character
   * @since 4.10.0
   */
  float width(final int codepoint, final @NotNull Style style, final @Nullable CX context);

  /**
   * Calculates the pixel width of a character represented by a codepoint without any context.
   *
   * @param codepoint a codepoint representing a character
   * @param style the style of the character
   * @return the pixel width of the character
   * @since 4.10.0
   */
  default float width(final int codepoint, final @NotNull Style style) {
    return this.width(codepoint, style, null);
  }

  /**
   * A builder for a pixel width source.
   *
   * <p>A new builder will start with a default value for each part, see the methods for each part for these values</p>
   *
   * @param <CX> a context type (player, server, locale)
   * @since 4.10.0
   */
  interface Builder<CX> extends Buildable.Builder<PixelWidthSource<CX>> {
    /**
     * Set the {@link ComponentFlattener} used by this pixel width source to turn components into plain text.
     *
     * <p>The default value for this is {@link ComponentFlattener#basic()}</p>
     *
     * @param flattener the flattener
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder<CX> flattener(final @NotNull ComponentFlattener flattener);

    /**
     * Set the function used to figure out which {@link CharacterWidthFunction} to use based on the context provided at calculation time.
     *
     * <p>The default value for this is {@link net.kyori.adventure.text.width.DefaultCharacterWidthFunction#INSTANCE}</p>
     *
     * @param characterWidthFunction the function
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder<CX> characterWidthFunction(final @NotNull Function<@Nullable CX, CharacterWidthFunction> characterWidthFunction);
  }
}
