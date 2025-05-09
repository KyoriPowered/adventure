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
package net.kyori.adventure.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

abstract class AbstractNBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NBTComponentBuilder<C, B> {
  protected @Nullable String nbtPath;
  protected boolean interpret = NBTComponentImpl.INTERPRET_DEFAULT;
  protected @Nullable Component separator;

  AbstractNBTComponentBuilder() {
  }

  AbstractNBTComponentBuilder(final @NotNull C component) {
    super(component);
    this.nbtPath = component.nbtPath();
    this.interpret = component.interpret();
    this.separator = component.separator();
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B nbtPath(final @NotNull String nbtPath) {
    this.nbtPath = requireNonNull(nbtPath, "nbtPath");
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B interpret(final boolean interpret) {
    this.interpret = interpret;
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B separator(final @Nullable ComponentLike separator) {
    this.separator = ComponentLike.unbox(separator);
    return (B) this;
  }
}
