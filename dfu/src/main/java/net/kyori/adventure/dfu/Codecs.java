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
package net.kyori.adventure.dfu;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.dfu.component.ComponentType;
import net.kyori.adventure.dfu.component.ComponentTypes;
import net.kyori.adventure.dfu.style.StyleCodecs;
import net.kyori.adventure.dfu.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.Index;

/**
 * The {@code Codecs} class provides a collection of utility methods to create and manipulate
 * {@link Codec} and {@link MapCodec} instances. These instances enable encoding and decoding
 * of data elements in various formats, providing support for index-based mapping, raw ID validation,
 * polymorphic serialization, compression-aware codecs, and more.
 * <p>
 * This class includes methods to handle advanced use cases such as:
 * - Ad
 */
public final class Codecs {
  private Codecs() {
  }

  private static final int ERROR_RAW_ID = -1;

  public static final Codec<Object> BASIC_OBJECT = Codecs.fromOps(JavaOps.INSTANCE);
  public static final Codec<ShadowColor> SHADOW_COLOR = Codec.INT.xmap(ShadowColor::shadowColor, ShadowColor::value);

  public static final Codec<Component> COMPONENT = Codec.recursive("Component", Codecs::createCodec);


  /**
   * Creates a {@link Codec} for encoding and decoding elements using their index or key representation.
   * This codec allows encoding and decoding elements either by their string key or an integer index
   * derived from their order in the provided elements array.
   *
   * @param <T>      the type of elements to be encoded/decoded
   * @param supplier a supplier providing an array of elements to use for codec creation
   * @param index    an {@link Index} object that provides a mapping between string keys and elements
   * @return a {@link Codec} capable of encoding and decoding elements of type {@code T}
   * using both string keys and integer indexes
   */
  public static <T> Codec<T> createIndexCodec(Supplier<T[]> supplier, Index<String, T> index) {
    final T[] elements = supplier.get();
    final Map<String, T> idToElementMap = Arrays.stream(elements).collect(Collectors.toMap(index::key, v -> v));
    final ToIntFunction<T> indexFunction = Util.getLastIndexFunction(Arrays.asList(elements));

    final Codec<T> stringCodec = Codec.stringResolver(index::key, idToElementMap::get);
    final Codec<T> intCodec = createRawIdCodec(indexFunction, ordinal -> ordinal >= 0 && ordinal < elements.length ? elements[ordinal] : null, ERROR_RAW_ID);

    return orCompressed(stringCodec, intCodec);
  }

  /**
   * Creates a {@link Codec} for encoding and decoding elements based on their raw ID.
   * This method ensures that elements are properly verified against their raw ID representation
   * and validates mapping between raw IDs and elements.
   *
   * @param <E>            the type of elements to be encoded/decoded
   * @param elementToRawId a function that converts an element to its corresponding raw ID
   * @param rawIdToElement a function that converts a raw ID to its corresponding element
   * @param errorRawId     the raw ID value that represents an error or an unknown element
   * @return a {@link Codec} capable of encoding and decoding elements of type {@code E}
   * with strict checks for unknown elements and raw IDs
   */
  public static <E> Codec<E> createRawIdCodec(final ToIntFunction<E> elementToRawId, final IntFunction<E> rawIdToElement, final int errorRawId) {
    return Codec.INT.flatXmap((rawId) -> Optional.ofNullable(rawIdToElement.apply(rawId))
      .map(DataResult::success)
      .orElseGet(() -> DataResult.error(() -> "Unknown element id: " + rawId)), (element) -> {
      int rawId = elementToRawId.applyAsInt(element);
      return rawId == errorRawId
        ? DataResult.error(() -> "Element with unknown id: " + element)
        : DataResult.success(rawId);
    });
  }

  /**
   * Creates a {@link Codec} that performs transformations between a {@link Dynamic} instance and a raw value.
   * This codec uses the provided {@link DynamicOps} to handle the conversion logic, allowing
   * for adaptable encoding and decoding of values within a specific data representation.
   *
   * @param <T> the type parameter representing the encoded data format
   * @param ops the {@link DynamicOps} that provides access to the encoding and decoding logic
   * @return a {@link Codec} that adapts the provided {@link DynamicOps} for the conversion between
   * raw values and {@link Dynamic} instances
   */
  public static <T> Codec<T> fromOps(final DynamicOps<T> ops) {
    return Codec.PASSTHROUGH.xmap(
      dynamic -> dynamic.convert(ops).getValue(),
      object -> new Dynamic<>(ops, object)
    );
  }

  private static Component combine(final List<Component> components) {
    return components.stream().collect(Component.toComponent());
  }

  private static Codec<Component> createCodec(final Codec<Component> selfCodec) {
    final MapCodec<Component> componentMapCodec = createComponentMapCodec();
    final Codec<Component> structuredCodec = createStructuredCodec(selfCodec, componentMapCodec);
    final Codec<Either<String, List<Component>>> stringOrListCodec = createStringOrListCodec(selfCodec);

    final Codec<Either<Either<String, List<Component>>, Component>> combinedCodec =
      Codec.either(stringOrListCodec, structuredCodec);
    return combinedCodec.xmap(
      either -> either.map(Codecs::handleStringOrList, component -> component),
      Codecs::encodeComponent
    );
  }

  private static MapCodec<Component> createComponentMapCodec() {
    return Codecs.dispatchingCodec(
      ComponentTypes.ALL,
      ComponentType::getId,
      ComponentType::getCodec,
      ComponentTypes::from,
      "type"
    );
  }

  private static Codec<Component> createStructuredCodec(final Codec<Component> selfCodec, final MapCodec<Component> componentMapCodec) {
    return RecordCodecBuilder.create(instance ->
      instance.group(
        componentMapCodec.forGetter(c -> c),
        Codecs.nonEmptyList(selfCodec.listOf()).optionalFieldOf("extra", List.of()).forGetter(Component::children),
        StyleCodecs.MAP_CODEC.forGetter(Component::style)
      ).apply(instance, Codecs::component)
    );
  }

  private static Codec<Either<String, List<Component>>> createStringOrListCodec(final Codec<Component> selfCodec) {
    return Codec.either(
      Codec.STRING,
      Codecs.nonEmptyList(selfCodec.listOf())
    );
  }

  private static Component handleStringOrList(final Either<String, List<Component>> stringOrList) {
    return stringOrList.map(Component::text, Codecs::combine);
  }

  private static Either<Either<String, List<Component>>, Component> encodeComponent(final Component component) {
    if (component instanceof TextComponent textComponent) {
      final String content = textComponent.content();
      return Either.left(Either.left(content));
    }
    return Either.right(component);
  }

  private static Component component(final Component baseComponent, final List<Component> children, Style style) {
    return baseComponent.children(children).style(style);
  }

  /**
   * Creates a {@link Codec} that ensures a list is non-empty during validation.
   * This method wraps the provided codec and adds a validation step that
   * checks the list is not empty before encoding or decoding.
   *
   * @param <T>           the type of elements in the list
   * @param originalCodec the original {@link Codec} to wrap
   * @return a {@link Codec} that ensures the list is non-empty
   */
  public static <T> Codec<List<T>> nonEmptyList(final Codec<List<T>> originalCodec) {
    return originalCodec.validate((list) -> list.isEmpty()
      ? DataResult.error(() -> "List must have contents")
      : DataResult.success(list));
  }

  /**
   * Creates a {@link MapCodec} that dynamically selects and applies different codecs based on
   * the provided key in the input, enabling advanced polymorphic serialization/deserialization.
   *
   * @param <T>            The type representing the dispatching categories.
   * @param <V>            The type of the objects to be encoded/decoded.
   * @param types          An array of objects representing the possible categories.
   * @param idFunction     A function that maps category objects to their unique identifiers.
   * @param codecFunction  A function that generates a {@link MapCodec} for a given category.
   * @param typeExtractor  A function that retrieves the category from a given object of type {@code V}.
   * @param dispatchingKey The key used to identify the dispatching category in the serialized input.
   * @return A {@link MapCodec} that handles polymorphic serialization/deserialization
   * based on the dispatching category.
   */
  public static <T, V> MapCodec<V> dispatchingCodec(final T[] types, final Function<T, String> idFunction,
                                                    Function<T, MapCodec<? extends V>> codecFunction, final Function<V, T> typeExtractor, final String dispatchingKey) {
    final List<MapCodec<? extends V>> mapCodecs = Arrays.stream(types).map(codecFunction).toList();
    final MapCodec<V> fuzzyCodec = new FuzzyCodec<>(mapCodecs, v -> codecFunction.apply(typeExtractor.apply(v)));

    final Map<String, T> idToTypeMap = Arrays.stream(types).collect(Collectors.toMap(idFunction, v -> v));
    final Function<String, T> idMapper = idToTypeMap::get;
    final Codec<T> typeCodec = Codec.STRING.xmap(idMapper, idFunction);
    final MapCodec<V> dispatchMapCodec = typeCodec.dispatchMap(dispatchingKey, typeExtractor, codecFunction);

    final MapCodec<V> dispatchingCodec = new DispatchingCodec<>(dispatchingKey, dispatchMapCodec, fuzzyCodec);
    return Codecs.orCompressed(dispatchingCodec, dispatchMapCodec);
  }

  /**
   * Creates a {@link Codec} that dynamically switches between two codecs based on the compression
   * support of the data representation. If the data representation supports compression, the
   * {@code compressedCodec} is used; otherwise, the {@code uncompressedCodec} is used.
   *
   * @param <E>               the type of elements to be encoded/decoded
   * @param uncompressedCodec the {@link Codec} used when the data representation does not support compression
   * @param compressedCodec   the {@link Codec} used when the data representation supports compression
   * @return a {@link Codec} that dynamically chooses between the uncompressed and compressed codecs
   */
  public static <E> Codec<E> orCompressed(final Codec<E> uncompressedCodec, final Codec<E> compressedCodec) {
    return new Codec<E>() {
      public <T> DataResult<T> encode(final E input, final DynamicOps<T> ops, final T prefix) {
        return ops.compressMaps() ? compressedCodec.encode(input, ops, prefix) : uncompressedCodec.encode(input, ops, prefix);
      }

      public <T> DataResult<Pair<E, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.compressMaps() ? compressedCodec.decode(ops, input) : uncompressedCodec.decode(ops, input);
      }

      public String toString() {
        return uncompressedCodec + " orCompressed " + compressedCodec;
      }
    };
  }

  /**
   * Creates a {@link MapCodec} that dynamically chooses between two provided codecs
   * based on whether the data representation supports compression. If compression
   * is supported, the {@code compressedCodec} is used; otherwise, the {@code uncompressedCodec}
   * is used. This allows for more efficient serialization and deserialization depending
   * on the underlying data representation.
   *
   * @param <E>               the type of elements to be encoded/decoded
   * @param uncompressedCodec the {@link MapCodec} used when the data representation does not support compression
   * @param compressedCodec   the {@link MapCodec} used when the data representation supports compression
   * @return a {@link MapCodec} that dynamically selects between the uncompressed and compressed codecs
   */
  public static <E> MapCodec<E> orCompressed(final MapCodec<E> uncompressedCodec, final MapCodec<E> compressedCodec) {
    return new MapCodec<>() {
      public <T> RecordBuilder<T> encode(final E input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return ops.compressMaps() ? compressedCodec.encode(input, ops, prefix) : uncompressedCodec.encode(input, ops, prefix);
      }

      public <T> DataResult<E> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        return ops.compressMaps() ? compressedCodec.decode(ops, input) : uncompressedCodec.decode(ops, input);
      }

      public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return compressedCodec.keys(ops);
      }

      public String toString() {
        return uncompressedCodec + " orCompressed " + compressedCodec;
      }
    };
  }

  static class FuzzyCodec<T> extends MapCodec<T> {
    private final List<MapCodec<? extends T>> codecs;
    private final Function<T, MapEncoder<? extends T>> codecGetter;

    public FuzzyCodec(final List<MapCodec<? extends T>> codecs, final Function<T, MapEncoder<? extends T>> codecGetter) {
      this.codecs = codecs;
      this.codecGetter = codecGetter;
    }

    public <S> DataResult<T> decode(final DynamicOps<S> ops, final MapLike<S> input) {
      for (MapCodec<? extends T> codec : this.codecs) {
        MapDecoder<T> mapDecoder = (MapDecoder<T>) codec;
        DataResult<T> result = mapDecoder.decode(ops, input);
        if (result.result().isPresent()) {
          return result;
        }
      }
      return DataResult.error(() -> "No matching codec found");
    }

    public <S> RecordBuilder<S> encode(final T input, final DynamicOps<S> ops, final RecordBuilder<S> prefix) {
      MapEncoder<T> mapEncoder = (MapEncoder<T>) this.codecGetter.apply(input);
      return mapEncoder.encode(input, ops, prefix);
    }

    public <S> Stream<S> keys(final DynamicOps<S> ops) {
      return this.codecs.stream()
        .flatMap(codec -> codec.keys(ops))
        .distinct();
    }

    public String toString() {
      return "FuzzyCodec[" + this.codecs + "]";
    }
  }

  static class DispatchingCodec<T> extends MapCodec<T> {
    private final String dispatchingKey;
    private final MapCodec<T> withKeyCodec;
    private final MapCodec<T> withoutKeyCodec;

    public DispatchingCodec(final String dispatchingKey, final MapCodec<T> withKeyCodec, final MapCodec<T> withoutKeyCodec) {
      this.dispatchingKey = dispatchingKey;
      this.withKeyCodec = withKeyCodec;
      this.withoutKeyCodec = withoutKeyCodec;
    }

    public <O> DataResult<T> decode(final DynamicOps<O> ops, final MapLike<O> input) {
      return input.get(this.dispatchingKey) != null
        ? this.withKeyCodec.decode(ops, input)
        : this.withoutKeyCodec.decode(ops, input);
    }

    public <O> RecordBuilder<O> encode(final T input, final DynamicOps<O> ops, final RecordBuilder<O> prefix) {
      return this.withoutKeyCodec.encode(input, ops, prefix);
    }

    public <T1> Stream<T1> keys(final DynamicOps<T1> ops) {
      return Stream.concat(this.withKeyCodec.keys(ops), this.withoutKeyCodec.keys(ops)).distinct();
    }
  }
}
