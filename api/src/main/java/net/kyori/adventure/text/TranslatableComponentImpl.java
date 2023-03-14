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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class TranslatableComponentImpl extends AbstractComponent implements TranslatableComponent {
  static TranslatableComponent create(final @NotNull List<Component> children, final @NotNull Style style, final @NotNull String key, final @Nullable String fallback, final @NotNull ComponentLike@NotNull[] args) {
    requireNonNull(args, "args");
    return create(children, style, key, fallback, Arrays.asList(args));
  }

  static TranslatableComponent create(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final @NotNull String key, final @Nullable String fallback, final @NotNull List<? extends ComponentLike> args) {
    return new TranslatableComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      requireNonNull(key, "key"),
      fallback,
      ComponentLike.asComponents(args) // Since translation arguments can be indexed, empty components are also included.
    );
  }

  private final String key;
  private final @Nullable String fallback;
  private final List<Component> args;

  TranslatableComponentImpl(final @NotNull List<Component> children, final @NotNull Style style, final @NotNull String key, final @Nullable String fallback, final @NotNull List<Component> args) {
    super(children, style);
    this.key = key;
    this.fallback = fallback;
    this.args = args;
  }

  @Override
  public @NotNull String key() {
    return this.key;
  }

  @Override
  public @NotNull TranslatableComponent key(final @NotNull String key) {
    if (Objects.equals(this.key, key)) return this;
    return create(this.children, this.style, key, this.fallback, this.args);
  }

  @Override
  public @NotNull List<Component> args() {
    return this.args;
  }

  @Override
  public @NotNull TranslatableComponent args(final @NotNull ComponentLike@NotNull... args) {
    return create(this.children, this.style, this.key, this.fallback, args);
  }

  @Override
  public @NotNull TranslatableComponent args(final @NotNull List<? extends ComponentLike> args) {
    return create(this.children, this.style, this.key, this.fallback, args);
  }

  @Override
  public @Nullable String fallback() {
    return this.fallback;
  }

  @Override
  public @NotNull TranslatableComponent fallback(final @Nullable String fallback) {
    return create(this.children, this.style, this.key, fallback, this.args);
  }

  @Override
  public @NotNull TranslatableComponent children(final @NotNull List<? extends ComponentLike> children) {
    return create(children, this.style, this.key, this.fallback, this.args);
  }

  @Override
  public @NotNull TranslatableComponent style(final @NotNull Style style) {
    return create(this.children, style, this.key, this.fallback, this.args);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof TranslatableComponent)) return false;
    if (!super.equals(other)) return false;
    final TranslatableComponent that = (TranslatableComponent) other;
    return Objects.equals(this.key, that.key()) && Objects.equals(this.fallback, that.fallback()) && Objects.equals(this.args, that.args());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.key.hashCode();
    result = (31 * result) + Objects.hashCode(this.fallback);
    result = (31 * result) + this.args.hashCode();
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

  static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, Builder> implements TranslatableComponent.Builder {
    private @Nullable String key;
    private @Nullable String fallback;
    private List<? extends Component> args = Collections.emptyList();

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull TranslatableComponent component) {
      super(component);
      this.key = component.key();
      this.args = component.args();
      this.fallback = component.fallback();
    }

    @Override
    public @NotNull Builder key(final @NotNull String key) {
      this.key = key;
      return this;
    }

    @Override
    public @NotNull Builder args(final @NotNull ComponentBuilder<?, ?> arg) {
      return this.args(Collections.singletonList(requireNonNull(arg, "arg").build()));
    }

    @Override
    @SuppressWarnings("checkstyle:GenericWhitespace")
    public @NotNull Builder args(final @NotNull ComponentBuilder<?, ?>@NotNull... args) {
      requireNonNull(args, "args");
      if (args.length == 0) return this.args(Collections.emptyList());
      return this.args(Stream.of(args).map(ComponentBuilder::build).collect(Collectors.toList()));
    }

    @Override
    public @NotNull Builder args(final @NotNull Component arg) {
      return this.args(Collections.singletonList(requireNonNull(arg, "arg")));
    }

    @Override
    public @NotNull Builder args(final @NotNull ComponentLike@NotNull... args) {
      requireNonNull(args, "args");
      if (args.length == 0) return this.args(Collections.emptyList());
      return this.args(Arrays.asList(args));
    }

    @Override
    public @NotNull Builder args(final @NotNull List<? extends ComponentLike> args) {
      this.args = ComponentLike.asComponents(requireNonNull(args, "args"));
      return this;
    }

    @Override
    public @NotNull Builder fallback(final @Nullable String fallback) {
      this.fallback = fallback;
      return this;
    }

    @Override
    public @NotNull TranslatableComponent build() {
      if (this.key == null) throw new IllegalStateException("key must be set");
      return create(this.children, this.buildStyle(), this.key, this.fallback, this.args);
    }
  }
}
