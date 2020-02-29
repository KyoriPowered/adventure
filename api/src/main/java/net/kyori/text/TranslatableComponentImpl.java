/*
 * This file is part of text, licensed under the MIT License.
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
package net.kyori.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

final class TranslatableComponentImpl extends AbstractComponent implements TranslatableComponent {
  private final String key;
  private final List<Component> args;

  TranslatableComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final @NonNull String key, final @NonNull Component@NonNull[] args) {
    this(children, style, key, Arrays.asList(args));
  }

  TranslatableComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final @NonNull String key, final @NonNull List<? extends Component> args) {
    super(children, style);
    this.key = key;
    this.args = Collections.unmodifiableList(new ArrayList<>(args));
  }

  @Override
  public @NonNull String key() {
    return this.key;
  }

  @Override
  public @NonNull TranslatableComponent key(final @NonNull String key) {
    if(Objects.equals(this.key, key)) return this;
    return new TranslatableComponentImpl(this.children, this.style, requireNonNull(key, "key"), this.args);
  }

  @Override
  public @NonNull List<Component> args() {
    return this.args;
  }

  @Override
  public @NonNull TranslatableComponent args(final @NonNull Component@NonNull... args) {
    return new TranslatableComponentImpl(this.children, this.style, this.key, args);
  }

  @Override
  public @NonNull TranslatableComponent args(final @NonNull List<? extends Component> args) {
    return new TranslatableComponentImpl(this.children, this.style, this.key, args);
  }

  @Override
  public @NonNull TranslatableComponent children(final @NonNull List<Component> children) {
    return new TranslatableComponentImpl(children, this.style, this.key, this.args);
  }

  @Override
  public @NonNull TranslatableComponent style(final @NonNull Style style) {
    if(Objects.equals(this.style, style)) return this;
    return new TranslatableComponentImpl(this.children, style, this.key, this.args);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof TranslatableComponent)) return false;
    if(!super.equals(other)) return false;
    final TranslatableComponent that = (TranslatableComponent) other;
    return Objects.equals(this.key, that.key()) && Objects.equals(this.args, that.args());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.key.hashCode();
    result = (31 * result) + this.args.hashCode();
    return result;
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("key", this.key);
    builder.put("args", this.args);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, Builder> implements TranslatableComponent.Builder {
    private @Nullable String key;
    private List<? extends Component> args = EMPTY_COMPONENT_LIST;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull TranslatableComponent component) {
      super(component);
      this.key = component.key();
      this.args = component.args();
    }

    @Override
    public @NonNull Builder key(final @NonNull String key) {
      this.key = key;
      return this;
    }

    @Override
    public @NonNull Builder args(final @NonNull ComponentBuilder<?, ?>... args) {
      return this.args(Stream.of(args).map(ComponentBuilder::build).collect(Collectors.toList()));
    }

    @Override
    public @NonNull Builder args(final @NonNull Component... args) {
      return this.args(Arrays.asList(args));
    }

    @Override
    public @NonNull Builder args(final @NonNull List<? extends Component> args) {
      this.args = args;
      return this;
    }

    @Override
    public @NonNull TranslatableComponentImpl build() {
      if(this.key == null) throw new IllegalStateException("key must be set");
      return new TranslatableComponentImpl(this.children, this.buildStyle(), this.key, this.args);
    }
  }
}
