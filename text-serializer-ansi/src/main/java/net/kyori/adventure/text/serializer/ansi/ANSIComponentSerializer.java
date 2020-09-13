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
package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ANSIComponentSerializer extends ComponentSerializer<Component, Component, String>, Buildable<ANSIComponentSerializer, ANSIComponentSerializer.Builder> {
  char ESC_CHAR = '\u001B';

  /**
   * This AnsiComponentSerializer produces full colour RGB codes and supports True colour terminals.  Otherwise colours are down sampled to the nearest Named Colour.
   *
   * @return {@link ANSIComponentSerializer}
   */
  static @NonNull ANSIComponentSerializer fullColour() {
    return ANSIComponentSerializerImpl.TRUE_COLOR;
  }

  /**
   * Construct the Builder
   * @return {@link ANSIComponentSerializer.Builder}
   */
  static @NonNull Builder builder() {
    return new ANSIComponentSerializerImpl.BuilderImpl();
  }

  /**
   * A builder for {@link ANSIComponentSerializer}.
   */
  interface Builder extends Buildable.Builder<ANSIComponentSerializer> {

    @NonNull Builder downSample(final boolean downSample);

    @Override
    @NonNull ANSIComponentSerializer build();
  }
}
