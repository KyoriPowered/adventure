/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

abstract class NbtComponentImpl<C extends NbtComponent<C, B>, B extends NbtComponentBuilder<C, B>> extends AbstractComponent implements NbtComponent<C, B> {
  final String nbtPath;
  final boolean interpret;

  NbtComponentImpl(final @NonNull List<Component> children, final Style.@Nullable Builder style, final String nbtPath, final boolean interpret) {
    super(children, style);
    this.nbtPath = nbtPath;
    this.interpret = interpret;
  }

  NbtComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPath, final boolean interpret) {
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
    if(!(other instanceof NbtComponent)) return false;
    if(!super.equals(other)) return false;
    final NbtComponent<?, ?> that = (NbtComponent<?, ?>) other;
    return Objects.equals(this.nbtPath, that.nbtPath()) && this.interpret == that.interpret();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.nbtPath, this.interpret);
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("nbtPath", this.nbtPath);
    builder.put("interpret", this.interpret);
  }

  public static abstract class BuilderImpl<C extends NbtComponent<C, B>, B extends NbtComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NbtComponentBuilder<C, B> {
    @Nullable String nbtPath;
    boolean interpret;

    BuilderImpl() {
    }

    BuilderImpl(@NonNull final C component) {
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
