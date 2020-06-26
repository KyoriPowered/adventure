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
package net.kyori.adventure.text;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

abstract class NBTComponentImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponent implements NBTComponent<C, B> {
  final String nbtPath;
  final boolean interpret;

  NBTComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPath, final boolean interpret) {
    super(children, style);
    this.nbtPath = nbtPath;
    this.interpret = interpret;
  }

  @Override
  public @NonNull String nbtPath() {
    return this.nbtPath;
  }

  @Override
  public boolean interpret() {
    return this.interpret;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof NBTComponent)) return false;
    if(!super.equals(other)) return false;
    final NBTComponent<?, ?> that = (NBTComponent<?, ?>) other;
    return Objects.equals(this.nbtPath, that.nbtPath()) && this.interpret == that.interpret();
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.nbtPath.hashCode();
    result = (31 * result) + Boolean.hashCode(this.interpret);
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("nbtPath", this.nbtPath),
        ExaminableProperty.of("interpret", this.interpret)
      ),
      super.examinableProperties()
    );
  }

  static abstract class BuilderImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NBTComponentBuilder<C, B> {
    @Nullable String nbtPath;
    boolean interpret;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull C component) {
      super(component);
      this.nbtPath = component.nbtPath();
      this.interpret = component.interpret();
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull B nbtPath(final @NonNull String nbtPath) {
      this.nbtPath = nbtPath;
      return (B) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull B interpret(final boolean interpret) {
      this.interpret = interpret;
      return (B) this;
    }
  }
}
