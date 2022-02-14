/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

abstract class NBTComponentImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponent implements NBTComponent<C, B> {
  static final boolean INTERPRET_DEFAULT = false;
  final String nbtPath;
  final boolean interpret;
  final @Nullable Component separator;

  NBTComponentImpl(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final String nbtPath, final boolean interpret, final @Nullable ComponentLike separator) {
    super(children, style);
    this.nbtPath = nbtPath;
    this.interpret = interpret;
    this.separator = ComponentLike.unbox(separator);
  }

  @Override
  public @NotNull String nbtPath() {
    return this.nbtPath;
  }

  @Override
  public boolean interpret() {
    return this.interpret;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof NBTComponent)) return false;
    if (!super.equals(other)) return false;
    final NBTComponent<?, ?> that = (NBTComponent<?, ?>) other;
    return Objects.equals(this.nbtPath, that.nbtPath()) && this.interpret == that.interpret() && Objects.equals(this.separator, that.separator());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.nbtPath.hashCode();
    result = (31 * result) + Boolean.hashCode(this.interpret);
    result = (31 * result) + Objects.hashCode(this.separator);
    return result;
  }

  static abstract class BuilderImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NBTComponentBuilder<C, B> {
    protected @Nullable String nbtPath;
    protected boolean interpret = INTERPRET_DEFAULT;
    protected @Nullable Component separator;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull C component) {
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
}
