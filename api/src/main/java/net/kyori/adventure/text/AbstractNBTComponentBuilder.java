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

import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

abstract sealed class AbstractNBTComponentBuilder<C extends NBTComponent<C>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NBTComponentBuilder<C, B> permits BlockNBTComponentImpl.BuilderImpl, EntityNBTComponentImpl.BuilderImpl, StorageNBTComponentImpl.BuilderImpl {
  protected @Nullable String nbtPath;
  protected boolean interpret = NBTComponent.INTERPRET_DEFAULT;
  protected @Nullable Component separator;
  protected boolean plain;

  AbstractNBTComponentBuilder() {
  }

  AbstractNBTComponentBuilder(final C component) {
    super(component);
    this.nbtPath = component.nbtPath();
    this.interpret = component.interpret();
    this.separator = component.separator();
    this.plain = component.plain();
  }

  @Override
  @SuppressWarnings("unchecked")
  public B nbtPath(final String nbtPath) {
    this.nbtPath = requireNonNull(nbtPath, "nbtPath");
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B interpret(final boolean interpret) {
    if (this.interpret == interpret) return (B) this;
    checkInterpretPlainState(interpret, this.plain);
    this.interpret = interpret;
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B plain(final boolean plain) {
    if (this.plain == plain) return (B) this;
    checkInterpretPlainState(this.interpret, plain);
    this.plain = plain;
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B separator(final @Nullable ComponentLike separator) {
    this.separator = ComponentLike.unbox(separator);
    return (B) this;
  }

  static void checkInterpretPlainState(final boolean interpret, final boolean plain) {
    if (interpret && plain) {
      throw new IllegalArgumentException("Cannot have `interpret` and `plain` set to `true` at the same time");
    }
  }
}
