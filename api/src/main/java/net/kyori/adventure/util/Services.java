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
package net.kyori.adventure.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import net.kyori.adventure.internal.properties.AdventureProperties;
import org.jetbrains.annotations.ApiStatus;

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
   * @deprecated This method creates the {@link ServiceLoader} in the API module and is not compatible with JPMS service lookup.
   *     Use {@link #service(ServiceLoader, Class)} instead.
   */
  @Deprecated(since = "5.1.0", forRemoval = true)
  @ApiStatus.ScheduledForRemoval(inVersion = "6.0.0")
  public static <P> Optional<P> service(final Class<P> type) {
    return service(ServiceLoader.load(type, type.getClassLoader()), type);
  }

  /**
   * Collects a service from the loader.
   *
   * @param loader the service loader
   * @param type the service type
   * @param <P> the service type
   * @return a service, or {@link Optional#empty()}
   * @since 5.1.0
   */
  public static <P> Optional<P> service(final ServiceLoader<P> loader, final Class<P> type) {
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

  /**
   * A fallback service.
   *
   * <p>When used in tandem with {@link #serviceWithFallback(ServiceLoader, Class)}, classes that implement this interface
   * will be ignored in favour of classes that do not implement this interface.</p>
   *
   * @since 4.14.0
   */
  public interface Fallback {
  }

  /**
   * Locates a service.
   *
   * <p>If multiple services of this type exist, the first non-fallback service will be returned.</p>
   *
   * @param type the service type
   * @param <P> the service type
   * @return a service, or {@link Optional#empty()}
   * @see Fallback
   * @since 4.14.0
   * @deprecated This method creates the {@link ServiceLoader} in the API module and is not compatible with JPMS service lookup.
   *     Use {@link #serviceWithFallback(ServiceLoader, Class)} instead.
   */
  @Deprecated(since = "5.1.0", forRemoval = true)
  @ApiStatus.ScheduledForRemoval(inVersion = "6.0.0")
  public static <P> Optional<P> serviceWithFallback(final Class<P> type) {
    return serviceWithFallback(ServiceLoader.load(type, type.getClassLoader()), type);
  }

  /**
   * Collects the services.
   *
   * <p>If multiple services of this type exist, the first non-fallback service will be returned.</p>
   *
   * @param loader the service loader
   * @param type the service type
   * @param <P> the service type
   * @return a service, or {@link Optional#empty()}
   * @see Fallback
   * @since 5.1.0
   */
  public static <P> Optional<P> serviceWithFallback(final ServiceLoader<P> loader, final Class<P> type) {
    final Iterator<P> it = loader.iterator();
    P firstFallback = null;

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

      if (instance instanceof Fallback) {
        if (firstFallback == null) {
          firstFallback = instance;
        }
      } else {
        return Optional.of(instance);
      }
    }

    return Optional.ofNullable(firstFallback);
  }

  /**
   * Locates all providers for a certain service and initializes them.
   *
   * @param clazz the service interface
   * @param <P> the service interface type
   * @return an unmodifiable set of all known providers of the service
   * @since 4.17.0
   * @deprecated This method creates the {@link ServiceLoader} in the API module and is not compatible with JPMS service lookup.
   *     Use {@link #services(ServiceLoader, Class)} instead.
   */
  @Deprecated(since = "5.1.0", forRemoval = true)
  @ApiStatus.ScheduledForRemoval(inVersion = "6.0.0")
  public static <P> Set<P> services(final Class<? extends P> clazz) {
    return services(ServiceLoader.load(clazz, clazz.getClassLoader()), clazz);
  }

  /**
   * Collects all providers for a certain service and initializes them.
   *
   * @param loader the service loader
   * @param type the service type
   * @param <P> the service interface type
   * @return an unmodifiable set of all known providers of the service
   * @since 5.1.0
   */
  public static <P> Set<P> services(final ServiceLoader<? extends P> loader, final Class<? extends P> type) {
    final Set<P> providers = new HashSet<>();
    for (final Iterator<? extends P> it = loader.iterator(); it.hasNext();) {
      final P instance;
      try {
        instance = it.next();
      } catch (final ServiceConfigurationError ex) {
        if (SERVICE_LOAD_FAILURES_ARE_FATAL) {
          throw new IllegalStateException("Encountered an exception loading a provider for " + type + ": ", ex);
        } else {
          continue;
        }
      }
      providers.add(instance);
    }
    return Set.copyOf(providers);
  }
}
