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

import static java.util.Objects.requireNonNull;

class KeybindComponentImpl extends AbstractComponent implements KeybindComponent {
  private final String keybind;

  protected KeybindComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final @NonNull String keybind) {
    super(children, style);
    this.keybind = keybind;
  }

  @Override
  public @NonNull String keybind() {
    return this.keybind;
  }

  @Override
  public @NonNull KeybindComponent keybind(final @NonNull String keybind) {
    return new KeybindComponentImpl(this.children, this.style, requireNonNull(keybind, "keybind"));
  }

  @Override
  public @NonNull KeybindComponent children(final @NonNull List<Component> children) {
    return new KeybindComponentImpl(children, this.style, this.keybind);
  }

  @Override
  public @NonNull KeybindComponent style(final @NonNull Style style) {
    return new KeybindComponentImpl(this.children, style, this.keybind);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof KeybindComponent)) return false;
    if(!super.equals(other)) return false;
    final KeybindComponent component = (KeybindComponent) other;
    return Objects.equals(this.keybind, component.keybind());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.keybind);
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("keybind", this.keybind);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static class BuilderImpl extends AbstractComponentBuilder<KeybindComponent, Builder> implements KeybindComponent.Builder {
    private @Nullable String keybind;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull KeybindComponent component) {
      super(component);
      this.keybind = component.keybind();
    }

    @Override
    public @NonNull Builder keybind(final @NonNull String keybind) {
      this.keybind = keybind;
      return this;
    }

    @Override
    public @NonNull KeybindComponent build() {
      if(this.keybind == null) throw new IllegalStateException("keybind must be set");
      return new KeybindComponentImpl(this.children, this.buildStyle(), this.keybind);
    }
  }
}
