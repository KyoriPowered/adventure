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
package net.kyori.adventure.util;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import net.kyori.adventure.internal.properties.AdventureProperties;
import org.jetbrains.annotations.NotNull;

/**
 * Tools for working with {@link ServiceLoader}s.
 *
 * @since 4.8.0
 */
public final class Services {
  private static final boolean SERVICE_LOAD_FAILURES_ARE_FATAL = Boolean.TRUE.equals(AdventureProperties.SERVICE_LOAD_FAILURES_ARE_FATAL.value());

  private Services() {
  }

  /**
   * Locates a service.
   *
   * @param type the service type
   * @param <P> the service type
   * @return a service, or {@link Optional#empty()}
   * @since 4.8.0
   */
  public static <P> @NotNull Optional<P> service(final @NotNull Class<P> type) {
    final ServiceLoader<P> loader = Services0.loader(type);
    final Iterator<P> it = loader.iterator();
    while (it.hasNext()) {
      final P instance;
      try {
        instance = it.next();
      } catch (final Throwable t) {
        if (SERVICE_LOAD_FAILURES_ARE_FATAL) {
          throw new IllegalStateException("Encountered an exception loading service " + type, t);
        } else {
          continue;
        }
      }
      if (it.hasNext()) {
        throw new IllegalStateException("Expected to find one service " + type + ", found multiple");
      }
      return Optional.of(instance);
    }
    return Optional.empty();
  }
}
