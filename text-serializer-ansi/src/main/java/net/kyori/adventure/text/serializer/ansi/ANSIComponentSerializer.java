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
package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An Ansi component serializer.
 *
 * <p>Use {@link Builder#downsampleColors(boolean)} to support consoles that cannot render
 * 256 colors.</p>
 *
 * @since 4.0.0
 */
public interface ANSIComponentSerializer extends ComponentSerializer<Component, Component, String>, Buildable<ANSIComponentSerializer, ANSIComponentSerializer.Builder> {
  char ESCAPE_CHAR = '\u001B';

  /**
   * This AnsiComponentSerializer produces full colour RGB codes and supports True colour terminals.  Otherwise colours are down sampled to the nearest Named Colour.
   *
   * @return {@link ANSIComponentSerializer}
   * @since 4.0.0
   */
  static @NonNull ANSIComponentSerializer fullColour() {
    return ANSIComponentSerializerImpl.TRUE_COLOR;
  }

  /**
   * Creates a new {@link ANSIComponentSerializer.Builder}.
   *
   * @return a builder
   * @since 4.0.0
   */
  static @NonNull Builder builder() {
    return new ANSIComponentSerializerImpl.BuilderImpl();
  }

  /**
   * A builder for {@link ANSIComponentSerializer}.
   *
   * @since 4.0.0
   */
  interface Builder extends Buildable.Builder<ANSIComponentSerializer> {

    /**
     * Sets that the serializer should down sample hex colors to named colors.
     *
     * @param downSample if true down sample the colour.
     * @return this builder
     */
    @NonNull Builder downsampleColors(final boolean downSample);

    /**
     * Creates a new {@link ANSIComponentSerializer.Builder}.
     *
     * @return a builder
     */
    @Override
    @NonNull ANSIComponentSerializer build();
  }
}
