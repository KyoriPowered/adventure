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
package net.kyori.text.renderer;

import java.text.MessageFormat;
import java.util.function.BiFunction;
import net.kyori.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A friendly component renderer.
 *
 * @deprecated use {@link TranslatableComponentRenderer}
 */
@Deprecated
public abstract class FriendlyComponentRenderer<C> implements ComponentRenderer<C> {
  public static <C> @NonNull FriendlyComponentRenderer<C> from(final @NonNull BiFunction<C, String, MessageFormat> translations) {
    return new FriendlyComponentRenderer<C>() {
      @Override
      protected @NonNull MessageFormat translation(final @NonNull C context, final @NonNull String key) {
        return translations.apply(context, key);
      }
    };
  }

  private final TranslatableComponentRenderer<C> renderer = new TranslatableComponentRenderer<C>() {
    @Override
    protected @Nullable MessageFormat translation(@NonNull final C context, final @NonNull String key) {
      return FriendlyComponentRenderer.this.translation(context, key);
    }
  };

  @Override
  public @NonNull Component render(final @NonNull Component component, final @NonNull C context) {
    return this.renderer.render(component, context);
  }

  /**
   * Gets a translation for a translation key in the given context.
   *
   * @param context the context
   * @param key the translation key
   * @return the translation
   */
  protected abstract @Nullable MessageFormat translation(final @NonNull C context, final @NonNull String key);
}
