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
package net.kyori.adventure.text.event;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.Services;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A registry for conversions between different data component value holder classes.
 *
 * <p>Conversions are discovered by {@link ServiceLoader} lookup of implementations of the {@link Provider} interface (on the classloader which loaded Adventure).</p>
 *
 * @since 4.17.0
 */
public final class DataComponentValueConverterRegistry {
  private static final Set<Provider> PROVIDERS = Services.services(Provider.class);

  private DataComponentValueConverterRegistry() {
  }

  /**
   * Get the id's of all registered conversion providers.
   *
   * @return an unmodifiable set of the known provider ids
   * @since 4.1.7.0
   */
  public static Set<Key> knownProviders() {
    return Collections.unmodifiableSet(PROVIDERS.stream()
      .map(Provider::id)
      .collect(Collectors.toSet()));
  }

  /**
   * Try to convert the data component value {@code in} to the provided output type.
   *
   * @param target the target type
   * @param key the key this value is for
   * @param in the input value
   * @param <O> the output type
   * @return a value of target type
   * @since 4.17.0
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <O extends DataComponentValue> @NotNull O convert(final @NotNull Class<O> target, final @NotNull Key key, final @NotNull DataComponentValue in) {
    if (target.isInstance(in)) {
      return target.cast(in);
    }

    final @Nullable RegisteredConversion converter = ConversionCache.converter(in.getClass(), target);
    if (converter == null) {
      throw new IllegalArgumentException("There is no data holder converter registered to convert from a " + in.getClass() + " instance to a " + target + " (on field " + key + ")");
    }

    try {
      return (O) ((Conversion) converter.conversion).convert(key, in);
    } catch (final Exception ex) {
      throw new IllegalStateException("Failed to convert data component value of type " + in.getClass() + " to type " + target + " due to an error in a converter provided by " + converter.provider.asString() + "!", ex);
    }
  }

  /**
   * A provider for data component value converters.
   *
   * @since 4.17.0
   */
  public interface Provider {
    /**
     * An identifier for this provider.
     *
     * @return the provider id
     * @since 4.17.0
     */
    @NotNull Key id();

    /**
     * Return conversions available from this provider.
     *
     * <p>Conversions may only be queried once at application initialization, so changes to the result of this method may not have any effect.</p>
     *
     * @return the conversions available
     * @since 4.17.0
     */
    @NotNull Iterable<Conversion<?, ?>> conversions();
  }

  /**
   * A single conversion that may be provided by a provider.
   *
   * @param <I> input type
   * @param <O> output type
   * @since 4.17.0
   */
  @ApiStatus.NonExtendable
  public interface Conversion<I, O> extends Examinable {
    /**
     * Create a new conversion.
     *
     * @param src the source type
     * @param dst the destination type
     * @param op the conversion operation
     * @param <I1> the input type
     * @param <O1> the output type
     * @return a conversion object
     * @since 4.17.0
     */
    static <I1, O1> @NotNull Conversion<I1, O1> convert(final @NotNull Class<I1> src, final @NotNull Class<O1> dst, final @NotNull BiFunction<Key, I1, O1> op) {
      return new DataComponentValueConversionImpl<>(
        requireNonNull(src, "src"),
        requireNonNull(dst, "dst"),
        requireNonNull(op, "op")
      );
    }

    /**
     * The source type.
     *
     * @return the source type
     * @since 4.17.0
     */
    @Contract(pure = true)
    @NotNull Class<I> source();

    /**
     * The destination type.
     *
     * @return the destination type
     * @since 4.17.0
     */
    @Contract(pure = true)
    @NotNull Class<O> destination();

    /**
     * Perform the actual conversion.
     *
     * @param key the key used for the data holder
     * @param input the source type
     * @return a data holder of the destination type
     * @since 4.17.0
     */
    @NotNull O convert(final @NotNull Key key, final @NotNull I input);
  }

  static final class ConversionCache {
    // input -> output -> conversion
    private static final ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, RegisteredConversion>> CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Set<RegisteredConversion>> CONVERSIONS = collectConversions();

    private static Map<Class<?>, Set<RegisteredConversion>> collectConversions() {
      final Map<Class<?>, Set<RegisteredConversion>> collected = new ConcurrentHashMap<>();
      for (final Provider provider : PROVIDERS) {
        final @NotNull Key id = requireNonNull(provider.id(), () -> "ID of provider " + provider + " is null");
        for (final Conversion<?, ?> conv : provider.conversions()) {
          collected.computeIfAbsent(conv.source(), $ -> ConcurrentHashMap.newKeySet()).add(new RegisteredConversion(id, conv));
        }
      }

      for (final Map.Entry<Class<?>, Set<RegisteredConversion>> entry : collected.entrySet()) {
        entry.setValue(Collections.unmodifiableSet(entry.getValue()));
      }

      return new ConcurrentHashMap<>(collected);
    }

    static RegisteredConversion compute(final Class<?> src, final Class<?> dst) {
      final Deque<Class<?>> sourceTypes = new ArrayDeque<>();
      sourceTypes.add(src);
      // walk up the source type hierarchy to find an option
      for (Class<?> sourcePtr; (sourcePtr = sourceTypes.poll()) != null;) {
        final Set<RegisteredConversion> conversions = CONVERSIONS.get(sourcePtr);
        if (conversions != null) {
          // if we have values for the source type, evaluate each to find the one that matches either the exact destination type, or the nearest subtype
          RegisteredConversion nearest = null;
          for (final RegisteredConversion potential : conversions) {
            final Class<?> potentialDst = potential.conversion.destination();

            if (dst.equals(potentialDst)) return potential; // exact match
            if (!dst.isAssignableFrom(potentialDst)) continue; // out of hierarchy

            // if we are up the hierarchy
            if (nearest == null || potentialDst.isAssignableFrom(nearest.conversion.destination())) {
              nearest = potential;
            }
          }

          if (nearest != null) return nearest; // we found a match
        }

        addSupertypes(sourcePtr, sourceTypes);
      }

      return RegisteredConversion.NONE;
    }

    private static void addSupertypes(final Class<?> clazz, final Deque<Class<?>> queue) {
      if (clazz.getSuperclass() != null) {
        queue.add(clazz.getSuperclass());
      }

      queue.addAll(Arrays.asList(clazz.getInterfaces()));
    }

    static @Nullable RegisteredConversion converter(final Class<? extends DataComponentValue> src, final Class<? extends DataComponentValue> dst) {
      final RegisteredConversion result = CACHE.computeIfAbsent(src, $ -> new ConcurrentHashMap<>()).computeIfAbsent(dst, $$ -> compute(src, dst));
      if (result == RegisteredConversion.NONE) return null;

      return result;
    }
  }

  static final class RegisteredConversion {
    static final RegisteredConversion NONE = new RegisteredConversion(null, null);

    final Key provider;
    final Conversion<?, ?> conversion;

    RegisteredConversion(final Key provider, final Conversion<?, ?> conversion) {
      this.provider = provider;
      this.conversion = conversion;
    }
  }
}
