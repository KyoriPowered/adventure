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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record TranslatableComponentImpl(List<Component> children, Style style, String key, @Nullable String fallback, List<TranslationArgument> args) implements TranslatableComponent {
  static TranslatableComponent create(final List<Component> children, final Style style, final String key, final @Nullable String fallback, final ComponentLike[] args) {
    requireNonNull(args, "args");
    return create(children, style, key, fallback, Arrays.asList(args));
  }

  static TranslatableComponent create(final List<? extends ComponentLike> children, final Style style, final String key, final @Nullable String fallback, final List<? extends ComponentLike> args) {
    return new TranslatableComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      requireNonNull(key, "key"),
      fallback,
      asArguments(args) // Since translation arguments can be indexed, empty components are also included.
    );
  }

  @Override
  public String key() {
    return this.key;
  }

  @Override
  public TranslatableComponent key(final String key) {
    if (Objects.equals(this.key, key)) return this;
    return create(this.children, this.style, key, this.fallback, this.args);
  }

  @Override
  public List<TranslationArgument> arguments() {
    return this.args;
  }

  @Override
  public TranslatableComponent arguments(final ComponentLike ... args) {
    return create(this.children, this.style, this.key, this.fallback, args);
  }

  @Override
  public TranslatableComponent arguments(final List<? extends ComponentLike> args) {
    return create(this.children, this.style, this.key, this.fallback, args);
  }

  @Override
  public TranslatableComponent fallback(final @Nullable String fallback) {
    return create(this.children, this.style, this.key, fallback, this.args);
  }

  @Override
  public TranslatableComponent children(final List<? extends ComponentLike> children) {
    return create(children, this.style, this.key, this.fallback, this.args);
  }

  @Override
  public TranslatableComponent style(final Style style) {
    return create(this.children, style, this.key, this.fallback, this.args);
  }

  @Override
  public Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, Builder> implements Builder {
    private @Nullable String key;
    private @Nullable String fallback;
    private List<TranslationArgument> args = Collections.emptyList();

    BuilderImpl() {
    }

    BuilderImpl(final TranslatableComponent component) {
      super(component);
      this.key = component.key();
      this.args = component.arguments();
      this.fallback = component.fallback();
    }

    @Override
    public Builder key(final String key) {
      this.key = key;
      return this;
    }

    @Override
    public Builder arguments(final ComponentLike ... args) {
      requireNonNull(args, "args");
      if (args.length == 0) return this.arguments(Collections.emptyList());
      return this.arguments(Arrays.asList(args));
    }

    @Override
    public Builder arguments(final List<? extends ComponentLike> args) {
      this.args = asArguments(requireNonNull(args, "args"));
      return this;
    }

    @Override
    public Builder fallback(final @Nullable String fallback) {
      this.fallback = fallback;
      return this;
    }

    @Override
    public TranslatableComponent build() {
      if (this.key == null) throw new IllegalStateException("key must be set");
      return create(this.children, this.buildStyle(), this.key, this.fallback, this.args);
    }
  }

  static List<TranslationArgument> asArguments(final List<? extends ComponentLike> likes) {
    if (likes.isEmpty()) {
      return Collections.emptyList();
    }

    final List<TranslationArgument> ret = new ArrayList<>(likes.size());
    for (int i = 0; i < likes.size(); i++) {
      final ComponentLike like = likes.get(i);
      switch (like) {
        case null -> throw new NullPointerException("likes[" + i + "]");
        case TranslationArgument translationArgument -> ret.add(translationArgument);
        case TranslationArgumentLike translationArgumentLike -> ret.add(requireNonNull(translationArgumentLike.asTranslationArgument(), "likes[" + i + "].asTranslationArgument()"));
        default -> ret.add(TranslationArgument.component(like));
      }
    }

    return Collections.unmodifiableList(ret);
  }
}
