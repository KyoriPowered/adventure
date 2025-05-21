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
package net.kyori.adventure.text.flattener;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.InheritanceAwareMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class ComponentFlattenerImpl implements ComponentFlattener {
  @SuppressWarnings("deprecation")
  static final ComponentFlattener BASIC = new BuilderImpl()
    .mapper(KeybindComponent.class, component -> component.keybind()) // IntelliJ is wrong here, this is fine
    .mapper(ScoreComponent.class, component -> {
      // Removed in Vanilla 1.16, but we keep it for backwards compat
      final @Nullable String value = component.value();
      return value != null ? value : "";
    })
    .mapper(SelectorComponent.class, SelectorComponent::pattern)
    .mapper(TextComponent.class, TextComponent::content)
    .mapper(TranslatableComponent.class, component -> {
      final @Nullable String fallback = component.fallback();
      return fallback != null ? fallback : component.key();
    })
    // The Vanilla game will not print NBT components, expecting those to be resolved with sender context
    .build();
  static final ComponentFlattener TEXT_ONLY = new BuilderImpl()
    .mapper(TextComponent.class, TextComponent::content)
    .build();

  private static final int MAX_DEPTH = 512;

  private final InheritanceAwareMap<Component, Handler> flatteners;
  private final Function<Component, String> unknownHandler;
  private final int maximumComplexity;

  ComponentFlattenerImpl(final InheritanceAwareMap<Component, Handler> flatteners, final @Nullable Function<Component, String> unknownHandler, final int maximumComplexity) {
    this.flatteners = flatteners;
    this.unknownHandler = unknownHandler;
    this.maximumComplexity = maximumComplexity;
  }

  @Override
  public void flatten(final @NotNull Component input, final @NotNull FlattenerListener listener) {
    this.flatten0(input, listener, new State(), 0);
  }

  private void flatten0(final @NotNull Component input, final @NotNull FlattenerListener listener, final @NotNull State state, final int depth) {
    requireNonNull(input, "input");
    requireNonNull(listener, "listener");
    if (input == Component.empty()) return;
    if (this.maximumComplexity != Builder.UNLIMITED_COMPLEXITY && state.complexity > this.maximumComplexity) {
      throw new IllegalStateException("Exceeded maximum complexity of " + this.maximumComplexity + " while attempting to flatten components!");
    }
    if (depth >= MAX_DEPTH) {
      throw new IllegalStateException("Exceeded maximum depth of " + MAX_DEPTH + " while attempting to flatten components!");
    }

    final @Nullable Handler flattener = this.flattener(input);
    final Style inputStyle = input.style();

    listener.pushStyle(inputStyle);
    try {
      if (flattener != null) {
        state.complexity++;
        flattener.handle(this, input, listener, state, depth + 1);
      }

      if (!input.children().isEmpty() && listener.shouldContinue()) {
        for (final Component child : input.children()) {
          state.complexity++;
          this.flatten0(child, listener, state, depth + 1);
        }
      }
    } finally {
      listener.popStyle(inputStyle);
    }
  }

  private <T extends Component> @Nullable Handler flattener(final T test) {
    final Handler flattener = this.flatteners.get(test.getClass());

    if (flattener == null && this.unknownHandler != null) {
      return (self, component, listener, state, depth) -> listener.component(this.unknownHandler.apply(component));
    } else {
      return flattener;
    }
  }

  @Override
  public ComponentFlattener.@NotNull Builder toBuilder() {
    return new BuilderImpl(this.flatteners, this.unknownHandler, this.maximumComplexity);
  }

  static final class State {
    int complexity = 0;
  }

  // A function that allows nesting other flatten operations
  @FunctionalInterface
  interface Handler {
    void handle(final ComponentFlattenerImpl self, final Component input, final FlattenerListener listener, final State state, final int depth);
  }

  static final class BuilderImpl implements Builder {
    private final InheritanceAwareMap.Builder<Component, Handler> flatteners;
    private @Nullable Function<Component, String> unknownHandler;
    private int maximumComplexity;

    BuilderImpl() {
      this.flatteners = InheritanceAwareMap.<Component, Handler>builder().strict(true);
      this.maximumComplexity = UNLIMITED_COMPLEXITY;
    }

    BuilderImpl(final InheritanceAwareMap<Component, Handler> flatteners, final @Nullable Function<Component, String> unknownHandler, final int maximumComplexity) {
      this.flatteners = InheritanceAwareMap.builder(flatteners).strict(true);
      this.unknownHandler = unknownHandler;
      this.maximumComplexity = maximumComplexity;
    }

    @Override
    public @NotNull ComponentFlattener build() {
      return new ComponentFlattenerImpl(this.flatteners.build(), this.unknownHandler, this.maximumComplexity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Component> ComponentFlattener.@NotNull Builder mapper(final @NotNull Class<T> type, final @NotNull Function<T, String> converter) {
      this.flatteners.put(type, (self, component, listener, state, depth) -> listener.component(converter.apply((T) component)));
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Component> ComponentFlattener.@NotNull Builder complexMapper(final @NotNull Class<T> type, final @NotNull BiConsumer<T, Consumer<Component>> converter) {
      this.flatteners.put(type, (self, component, listener, state, depth) -> converter.accept((T) component, c -> self.flatten0(c, listener, state, depth)));
      return this;
    }

    @Override
    public ComponentFlattener.@NotNull Builder unknownMapper(final @Nullable Function<Component, String> converter) {
      this.unknownHandler = converter;
      return this;
    }

    @Override
    public @NotNull Builder maximumComplexity(final int maximumComplexity) {
      if (maximumComplexity != UNLIMITED_COMPLEXITY && maximumComplexity <= 0) {
        throw new IllegalArgumentException("maximumComplexity must be greater than 0, was " + maximumComplexity);
      }
      this.maximumComplexity = maximumComplexity;
      return this;
    }
  }
}
