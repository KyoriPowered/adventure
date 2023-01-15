/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class KeybindComponentImpl extends AbstractComponent implements KeybindComponent {
  private final String keybind;

  static KeybindComponent create(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final @NotNull String keybind) {
    return new KeybindComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      requireNonNull(keybind, "keybind")
    );
  }

  KeybindComponentImpl(final @NotNull List<Component> children, final @NotNull Style style, final @NotNull String keybind) {
    super(children, style);
    this.keybind = keybind;
  }

  @Override
  public @NotNull String keybind() {
    return this.keybind;
  }

  @Override
  public @NotNull KeybindComponent keybind(final @NotNull String keybind) {
    if (Objects.equals(this.keybind, keybind)) return this;
    return create(this.children, this.style, keybind);
  }

  @Override
  public @NotNull KeybindComponent children(final @NotNull List<? extends ComponentLike> children) {
    return create(children, this.style, this.keybind);
  }

  @Override
  public @NotNull KeybindComponent style(final @NotNull Style style) {
    return create(this.children, style, this.keybind);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof KeybindComponent)) return false;
    if (!super.equals(other)) return false;
    final KeybindComponent that = (KeybindComponent) other;
    return Objects.equals(this.keybind, that.keybind());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.keybind.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractComponentBuilder<KeybindComponent, Builder> implements KeybindComponent.Builder {
    private @Nullable String keybind;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull KeybindComponent component) {
      super(component);
      this.keybind = component.keybind();
    }

    @Override
    public @NotNull Builder keybind(final @NotNull String keybind) {
      this.keybind = requireNonNull(keybind, "keybind");
      return this;
    }

    @Override
    public @NotNull KeybindComponent build() {
      if (this.keybind == null) throw new IllegalStateException("keybind must be set");
      return create(this.children, this.buildStyle(), this.keybind);
    }
  }
}
