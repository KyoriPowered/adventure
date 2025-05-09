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
package net.kyori.adventure.text.format;

import net.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class TextDecorationAndStateImpl implements TextDecorationAndState {
  private final TextDecoration decoration;
  private final TextDecoration.State state;

  TextDecorationAndStateImpl(final TextDecoration decoration, final TextDecoration.State state) {
    // no null check is required on the decoration since this constructor is always invoked in such a way that
    // decoration is always non-null
    this.decoration = decoration;
    this.state = requireNonNull(state, "state");
  }

  @Override
  public @NotNull TextDecoration decoration() {
    return this.decoration;
  }

  @Override
  public TextDecoration.@NotNull State state() {
    return this.state;
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final TextDecorationAndStateImpl that = (TextDecorationAndStateImpl) other;
    return this.decoration == that.decoration && this.state == that.state;
  }

  @Override
  public int hashCode() {
    int result = this.decoration.hashCode();
    result = (31 * result) + this.state.hashCode();
    return result;
  }
}
