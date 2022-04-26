/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.logger.slf4j;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods relating to creating component loggers.
 */
final class Handler {
  private static final ComponentLoggerProvider PROVIDER = Services.service(ComponentLoggerProvider.class)
    .orElse(LoggerFactory.getILoggerFactory() instanceof ComponentLoggerProvider ? (ComponentLoggerProvider) LoggerFactory.getILoggerFactory() : new DefaultProvider());

  private Handler() {
  }

  static ComponentLogger logger(final String name) {
    return PROVIDER.logger(LoggerHelperImpl.INSTANCE, name);
  }

  static final class DefaultProvider implements ComponentLoggerProvider {
    private final Map<String, ComponentLogger> loggers = new ConcurrentHashMap<>();
    @Override
    public @NotNull ComponentLogger logger(final @NotNull LoggerHelper helper, final @NotNull String name) {
      final ComponentLogger initial = this.loggers.get(name);
      if (initial != null) return initial;

      final Logger backing = LoggerFactory.getLogger(name);
      final ComponentLogger created = helper.delegating(backing, helper.plainSerializer());
      final ComponentLogger existing = this.loggers.putIfAbsent(name, created);
      return existing == null ? created : existing;
    }
  }

  static final class LoggerHelperImpl implements ComponentLoggerProvider.LoggerHelper {
    static final LoggerHelperImpl INSTANCE = new LoggerHelperImpl();

    private LoggerHelperImpl() {
    }

    @Override
    public Function<Component, String> plainSerializer() {
      return comp -> {
        final Component translated = GlobalTranslator.render(comp, Locale.getDefault());
        final StringBuilder contents = new StringBuilder();
        ComponentFlattener.basic().flatten(translated, contents::append);
        return contents.toString();
      };
    }

    @Override
    public @NotNull ComponentLogger delegating(@NotNull final Logger base, @NotNull final Function<Component, String> serializer) {
      return new WrappingComponentLoggerImpl(base, serializer);
    }
  }
}
