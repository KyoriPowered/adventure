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
package net.kyori.adventure.tab;

import net.kyori.adventure.skin.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.world.GameMode;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A tab list entry.
 */
public interface TabEntry {
  /**
   * Gets the text.
   *
   * @return the text
   */
  @NonNull Component text();

  /**
   * Sets the text.
   *
   * @param text the text
   * @return the tab entry
   */
  @NonNull TabEntry text(final @NonNull Component text);

  /**
   * Gets the latency.
   *
   * @return the latency, in milliseconds
   */
  int latency();

  /**
   * Sets the latency.
   *
   * @param latency the latency
   * @return the tab entry
   */
  @NonNull TabEntry latency(final int latency);

  // TODO: docs
  @NonNull GameMode gameMode();

  // TODO: docs
  @NonNull TabEntry gameMode(final @NonNull GameMode gameMode);

  /**
   * Gets the skin.
   *
   * @return the skin
   */
  @NonNull Skin skin();

  /**
   * Sets the skin.
   *
   * @param skin the skin
   * @return the tab entry
   */
  @NonNull TabEntry skin(final @NonNull Skin skin);
}
