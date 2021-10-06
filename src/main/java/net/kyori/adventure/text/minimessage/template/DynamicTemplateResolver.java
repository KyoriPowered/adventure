/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage.template;

import java.util.function.Function;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class DynamicTemplateResolver implements TemplateResolver {
  private final Function<String, ?> resolver;

  DynamicTemplateResolver(final Function<String, ?> resolver) {
    this.resolver = resolver;
  }

  @Override
  public boolean canResolve(final @NotNull String key) {
    final Object result = this.resolver.apply(key);
    return result instanceof String || result instanceof ComponentLike || result instanceof Template;
  }

  @Override
  public @Nullable Template resolve(final @NotNull String key) {
    final Object result = this.resolver.apply(key);

    if (result == null) return null;
    else if (result instanceof String) return Template.template(key, (String) result);
    else if (result instanceof ComponentLike) return Template.template(key, (ComponentLike) result);
    else if (result instanceof Template) return (Template) result;

    throw new IllegalArgumentException("Dynamic template resolver must return instances of String or ComponentLike, instead found " + result.getClass().getName());
  }
}
