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
package net.kyori.adventure.nbt.dfu;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ByteArrayBinaryTag;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.DoubleBinaryTag;
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.LongArrayBinaryTag;
import net.kyori.adventure.nbt.LongBinaryTag;
import net.kyori.adventure.nbt.ShortBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public class BinaryTagOps implements DynamicOps<BinaryTag> {
  public static final BinaryTagOps UNCOMPRESSED = new BinaryTagOps(false);
  public static final BinaryTagOps COMPRESSED = new BinaryTagOps(true);

  static String unwrap(final @NotNull BinaryTag tag) {
    if (tag instanceof StringBinaryTag) {
      return ((StringBinaryTag) tag).value();
    }
    throw new IllegalArgumentException("Unsupported tag type " + tag.type());
  }

  private final boolean compressed;

  public BinaryTagOps(final boolean compressed) {
    this.compressed = compressed;
  }

  @Override
  public BinaryTag empty() {
    return EndBinaryTag.endBinaryTag();
  }

  @Override
  public <U> U convertTo(final DynamicOps<U> outOps, final BinaryTag input) {
    if (input instanceof CompoundBinaryTag) {
      return this.convertMap(outOps, input);
    } else if (input instanceof ListBinaryTag) {
      return this.convertList(outOps, input);
    } else if (input instanceof ByteArrayBinaryTag) {
      return outOps.createByteList(ByteBuffer.wrap(((ByteArrayBinaryTag) input).value()));
    } else if (input instanceof IntArrayBinaryTag) {
      return outOps.createIntList(IntStream.of(((IntArrayBinaryTag) input).value()));
    } else if (input instanceof LongArrayBinaryTag) {
      return outOps.createLongList(LongStream.of(((LongArrayBinaryTag) input).value()));
    } else if (input instanceof StringBinaryTag) {
      return outOps.createString(((StringBinaryTag) input).value());
    } else if (input instanceof ByteBinaryTag) {
      return outOps.createByte(((ByteBinaryTag) input).value());
    } else if (input instanceof ShortBinaryTag) {
      return outOps.createShort(((ShortBinaryTag) input).value());
    } else if (input instanceof IntBinaryTag) {
      return outOps.createInt(((IntBinaryTag) input).value());
    } else if (input instanceof LongBinaryTag) {
      return outOps.createLong(((LongBinaryTag) input).value());
    } else if (input instanceof FloatBinaryTag) {
      return outOps.createFloat(((FloatBinaryTag) input).value());
    } else if (input instanceof DoubleBinaryTag) {
      return outOps.createDouble(((DoubleBinaryTag) input).value());
    } else if (input instanceof EndBinaryTag) {
      return outOps.empty();
    }
    throw new IllegalArgumentException("Unknown tag type " + input.getClass());
  }

  @Override
  public DataResult<Number> getNumberValue(final BinaryTag input) {
    if (input instanceof ByteBinaryTag) {
      return DataResult.success(((ByteBinaryTag) input).value());
    } else if (input instanceof ShortBinaryTag) {
      return DataResult.success(((ShortBinaryTag) input).value());
    } else if (input instanceof IntBinaryTag) {
      return DataResult.success(((IntBinaryTag) input).value());
    } else if (input instanceof LongBinaryTag) {
      return DataResult.success(((LongBinaryTag) input).value());
    } else if (input instanceof FloatBinaryTag) {
      return DataResult.success(((FloatBinaryTag) input).value());
    } else if (input instanceof DoubleBinaryTag) {
      return DataResult.success(((DoubleBinaryTag) input).value());
    } else {
      return DataResult.error(() -> "not a number");
    }
  }

  @Override
  public BinaryTag createNumeric(final Number i) {
    if (i instanceof Byte) {
      return ByteBinaryTag.byteBinaryTag((Byte) i);
    } else if (i instanceof Short) {
      return ShortBinaryTag.shortBinaryTag((Short) i);
    } else if (i instanceof Integer) {
      return IntBinaryTag.intBinaryTag((Integer) i);
    } else if (i instanceof Long) {
      return LongBinaryTag.longBinaryTag((Long) i);
    } else if (i instanceof Float) {
      return FloatBinaryTag.floatBinaryTag((Float) i);
    } else { // oh well?
      return DoubleBinaryTag.doubleBinaryTag(i.doubleValue());
    }
  }

  @Override
  public ByteBinaryTag createByte(final byte value) {
    return ByteBinaryTag.byteBinaryTag(value);
  }

  @Override
  public ShortBinaryTag createShort(final short value) {
    return ShortBinaryTag.shortBinaryTag(value);
  }

  @Override
  public IntBinaryTag createInt(final int value) {
    return IntBinaryTag.intBinaryTag(value);
  }

  @Override
  public LongBinaryTag createLong(final long value) {
    return LongBinaryTag.longBinaryTag(value);
  }

  @Override
  public FloatBinaryTag createFloat(final float value) {
    return FloatBinaryTag.floatBinaryTag(value);
  }

  @Override
  public DoubleBinaryTag createDouble(final double value) {
    return DoubleBinaryTag.doubleBinaryTag(value);
  }

  @Override
  public DataResult<String> getStringValue(final BinaryTag input) {
    if (input instanceof StringBinaryTag) {
      return DataResult.success(((StringBinaryTag) input).value());
    }
    return DataResult.error(() -> "Value was not a string");
  }

  @Override
  public BinaryTag createString(final String value) {
    return StringBinaryTag.stringBinaryTag(requireNonNull(value, "value"));
  }

  @Override
  public DataResult<BinaryTag> mergeToList(final BinaryTag list, final BinaryTag value) {
    if (list instanceof ListBinaryTag) {
      return DataResult.success(((ListBinaryTag) list).add(value));
    } else if (list.equals(this.empty())) {
      return DataResult.success(ListBinaryTag.listBinaryTag(value.type(), Collections.singletonList(value)));
    }
    return DataResult.error(() -> "Input value is not a list");
  }

  @Override
  public DataResult<BinaryTag> mergeToMap(final BinaryTag map, final BinaryTag key, final BinaryTag value) {
    if (requireNonNull(map, "map").equals(this.empty())) {
      return DataResult.success(CompoundBinaryTag.builder()
        .put(unwrap(key), value)
        .build());
    } else if (!(map instanceof CompoundBinaryTag)) {
      return DataResult.error(() -> "not a map");
    }
    return DataResult.success(((CompoundBinaryTag) map).put(unwrap(key), value));
  }

  @Override
  public DataResult<Stream<Pair<BinaryTag, BinaryTag>>> getMapValues(final BinaryTag input) {
    if (input instanceof CompoundBinaryTag) {
      return DataResult.success(StreamSupport.stream(((CompoundBinaryTag) input).spliterator(), false)
        .map(entry -> Pair.of(StringBinaryTag.stringBinaryTag(entry.getKey()), entry.getValue())));
    }
    return DataResult.error(() -> "Tag " + input + " was not a Compound");
  }

  @Override
  public BinaryTag createMap(final Stream<Pair<BinaryTag, BinaryTag>> map) {
    final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
    map.forEach(p -> builder.put(unwrap(p.getFirst()), p.getSecond()));
    return builder.build();
  }

  @Override
  public DataResult<Stream<BinaryTag>> getStream(final BinaryTag input) {
    if (input instanceof ListBinaryTag) {
      return DataResult.success(StreamSupport.stream(((ListBinaryTag) input).spliterator(), false));
    }
    return DataResult.error(() -> "not a list");
  }

  @Override
  public BinaryTag createList(final Stream<BinaryTag> input) {
    final ListBinaryTag.Builder<BinaryTag> builder = ListBinaryTag.builder();
    input.forEach(builder::add);
    return builder.build();
  }

  @Override
  public DataResult<MapLike<BinaryTag>> getMap(final BinaryTag input) {
    if (input instanceof CompoundBinaryTag) {
      return DataResult.success(new CompoundMaplike((CompoundBinaryTag) input));
    }
    return DataResult.error(() -> "not a map");
  }

  @Override
  public DataResult<Consumer<Consumer<BinaryTag>>> getList(final BinaryTag input) {
    if (input instanceof ListBinaryTag) {
      return DataResult.success(((ListBinaryTag) input)::forEach);
    }
    return DataResult.error(() -> "not a list");
  }

  @Override
  public DataResult<ByteBuffer> getByteBuffer(final BinaryTag input) {
    if (input instanceof ByteArrayBinaryTag) {
      return DataResult.success(ByteBuffer.wrap(((ByteArrayBinaryTag) input).value()));
    } else {
      return DynamicOps.super.getByteBuffer(input);
    }
  }

  @Override
  public BinaryTag createByteList(final ByteBuffer input) {
    return ByteArrayBinaryTag.byteArrayBinaryTag(input.array());
  }

  @Override
  public DataResult<IntStream> getIntStream(final BinaryTag input) {
    if (input instanceof IntArrayBinaryTag) {
      return DataResult.success(IntStream.of(((IntArrayBinaryTag) input).value()));
    }
    return DataResult.error(() -> "not an int stream");
  }

  @Override
  public BinaryTag createIntList(final IntStream input) {
    return IntArrayBinaryTag.intArrayBinaryTag(input.toArray());
  }

  @Override
  public DataResult<LongStream> getLongStream(final BinaryTag input) {
    if (input instanceof LongArrayBinaryTag) {
      return DataResult.success(LongStream.of(((LongArrayBinaryTag) input).value()));
    } else {
      return DynamicOps.super.getLongStream(input); // may be a list of longs
    }
  }

  @Override
  public BinaryTag createLongList(final LongStream input) {
    return LongArrayBinaryTag.longArrayBinaryTag(input.toArray());
  }

  @Override
  public DataResult<BinaryTag> get(final BinaryTag input, final String key) {
    if (input instanceof CompoundBinaryTag) {
      final @Nullable BinaryTag value = ((CompoundBinaryTag) input).get(key);
      if (value != null) {
        return DataResult.success(value);
      } else {
        return DataResult.error(() -> "Unknown key " + key + " in " + input);
      }
    }
    return DataResult.error(() -> "not a map");
  }

  @Override
  public DataResult<BinaryTag> getGeneric(final BinaryTag input, final BinaryTag key) {
    return this.get(input, unwrap(key));
  }

  @Override
  public BinaryTag set(final BinaryTag input, final String key, final BinaryTag value) {
    if (this.empty().equals(input)) {
      return CompoundBinaryTag.builder().put(key, value).build();
    } else if (input instanceof CompoundBinaryTag) {
      return ((CompoundBinaryTag) input).put(key, value);
    }
    return input;
  }

  @Override
  public BinaryTag update(final BinaryTag input, final String key, final Function<BinaryTag, BinaryTag> function) {
    if (this.empty().equals(input)) {
      return input;
    } else if (input instanceof CompoundBinaryTag compound) {
      final @Nullable BinaryTag old = compound.get(requireNonNull(key, "key"));
      if (old != null) {
        return compound.put(key, function.apply(old));
      }
    }
    return input;
  }

  @Override
  public BinaryTag updateGeneric(final BinaryTag input, final BinaryTag key, final Function<BinaryTag, BinaryTag> function) {
    return this.update(input, unwrap(key), function);
  }

  @Override
  public ListBuilder<BinaryTag> listBuilder() {
    return new ListBinaryTagBuilder(this);
  }

  @Override
  public RecordBuilder<BinaryTag> mapBuilder() {
    return new CompoundRecordBuilder(this);
  }

  @Override
  public <U> U convertList(final DynamicOps<U> outOps, final BinaryTag input) {
    if (!(input instanceof ListBinaryTag)) {
      throw new IllegalStateException("Input was not a list!");
    }
    final ListBuilder<U> builder = outOps.listBuilder();
    for (final BinaryTag item : (ListBinaryTag) input) {
      builder.add(this.convertTo(outOps, item));
    }

    return builder.build(outOps.empty()).result().orElseThrow(() -> new IllegalArgumentException("Unable to convert tag"));
  }

  @Override
  public <U> U convertMap(final DynamicOps<U> outOps, final BinaryTag input) {
    if (!(input instanceof CompoundBinaryTag)) {
      throw new IllegalStateException("Input was not a compound!");
    }
    final RecordBuilder<U> builder = outOps.mapBuilder();
    for (final Map.Entry<String, ? extends BinaryTag> item : (CompoundBinaryTag) input) {
      builder.add(item.getKey(), this.convertTo(outOps, item.getValue()));
    }

    return builder.build(outOps.empty()).result().orElseThrow(() -> new IllegalArgumentException("Unable to convert tag"));
  }

  @Override
  public BinaryTag remove(final BinaryTag input, final String key) {
    if (input instanceof CompoundBinaryTag) {
      return ((CompoundBinaryTag) input).put(key, null); // TODO: actual thing
    }
    return input;
  }

  @Override
  public String toString() {
    return "BinaryTag";
  }
}
