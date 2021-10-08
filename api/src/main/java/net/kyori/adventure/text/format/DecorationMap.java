/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.format;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterators;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

@Unmodifiable
final class DecorationMap extends AbstractMap<TextDecoration, TextDecoration.State> implements Examinable {
  private static final TextDecoration.State[] STATES = TextDecoration.State.values();
  private static final int MAP_SIZE = StyleImpl.DECORATIONS.length;
  private static final TextDecoration.State[] EMPTY_STATE_ARRAY = {};

  static final DecorationMap EMPTY = createEmptyDecorationMap();
  // key set is universal, all decorations always exist in any given style
  private static final KeySet KEY_SET = new KeySet();

  static DecorationMap fromMap(final Map<TextDecoration, TextDecoration.State> decorationMap) {
    if (decorationMap instanceof DecorationMap) return (DecorationMap) decorationMap;
    int bitSet = 0;
    for (int i = 0; i < MAP_SIZE; i++) {
      final TextDecoration decoration = StyleImpl.DECORATIONS[i];
      bitSet |= decorationMap.getOrDefault(decoration, TextDecoration.State.NOT_SET).ordinal() * offset(decoration);
    }
    return withBitSet(bitSet);
  }

  static DecorationMap merge(final Map<TextDecoration, TextDecoration.State> first, final Map<TextDecoration, TextDecoration.State> second) {
    int bitSet = 0;
    for (int i = 0; i < MAP_SIZE; i++) {
      final TextDecoration decoration = StyleImpl.DECORATIONS[i];
      bitSet |= first.getOrDefault(decoration, second.getOrDefault(decoration, TextDecoration.State.NOT_SET)).ordinal() * offset(decoration);
    }
    return withBitSet(bitSet);
  }

  private static DecorationMap withBitSet(final int bitSet) {
    return bitSet == EMPTY.bitSet ? EMPTY : new DecorationMap(bitSet);
  }

  private static DecorationMap createEmptyDecorationMap() {
    int bitSet = 0;
    for (int i = 0; i < MAP_SIZE; i++) {
      bitSet |= TextDecoration.State.NOT_SET.ordinal() * offset(StyleImpl.DECORATIONS[i]);
    }
    return new DecorationMap(bitSet);
  }

  private static int offset(final TextDecoration decoration) {
    // ordinal * 2, decoration states are tristate so they occupy two bits each [0b00, 0b01, 0b10]
    return 1 << decoration.ordinal() * 2;
  }

  private final int bitSet;

  // lazy
  private EntrySet entrySet = null;
  private Values values = null;

  private DecorationMap(final int bitSet) {
    this.bitSet = bitSet;
  }

  public @NotNull DecorationMap with(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
    Objects.requireNonNull(state, "state");
    Objects.requireNonNull(decoration, "decoration");
    final int offset = offset(decoration);
    return withBitSet(
      this.bitSet & ~(0b11 * offset) // 'reset' the state bits
        | state.ordinal() * offset
    );
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Arrays.stream(StyleImpl.DECORATIONS)
      .map(decoration -> ExaminableProperty.of(decoration.toString(), this.get(decoration)));
  }

  @Override
  public TextDecoration.State get(final Object o) {
    if (o instanceof TextDecoration) {
      return STATES[this.bitSet >> ((TextDecoration) o).ordinal() * 2 & 0b11];
    }
    return null;
  }

  @Override
  public boolean containsKey(final Object key) {
    // null-safe
    return key instanceof TextDecoration;
  }

  @Override
  public int size() {
    return MAP_SIZE;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public @NotNull Set<Entry<TextDecoration, TextDecoration.State>> entrySet() {
    if (this.entrySet == null) {
      this.entrySet = new EntrySet();
    }
    return this.entrySet;
  }

  @Override
  public @NotNull Set<TextDecoration> keySet() {
    return KEY_SET;
  }

  @Override
  public @NotNull Collection<TextDecoration.State> values() {
    if (this.values == null) {
      this.values = new Values();
    }
    return this.values;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == this) return true;
    if (other == null || other.getClass() != DecorationMap.class) return false;
    return this.bitSet == ((DecorationMap) other).bitSet;
  }

  @Override
  public int hashCode() {
    return this.bitSet;
  }

  final class EntrySet extends AbstractSet<Entry<TextDecoration, TextDecoration.State>> {
    @Override
    public @NotNull Iterator<Entry<TextDecoration, TextDecoration.State>> iterator() {
      return new Iterator<Entry<TextDecoration, TextDecoration.State>>() {
        private final Iterator<TextDecoration> decorations = KEY_SET.iterator();
        private final Iterator<TextDecoration.State> states = DecorationMap.this.values().iterator();

        @Override
        public boolean hasNext() {
          return this.decorations.hasNext() && this.states.hasNext();
        }

        @Override
        public Entry<TextDecoration, TextDecoration.State> next() {
          if (this.hasNext()) {
            return new SimpleImmutableEntry<>(this.decorations.next(), this.states.next());
          }
          throw new NoSuchElementException();
        }
      };
    }

    @Override
    public int size() {
      return MAP_SIZE;
    }
  }

  final class Values extends AbstractCollection<TextDecoration.State> {
    @Override
    public @NotNull Iterator<TextDecoration.State> iterator() {
      return Spliterators.iterator(Arrays.spliterator(this.toArray(EMPTY_STATE_ARRAY)));
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Object @NotNull [] toArray() {
      return Arrays.stream(StyleImpl.DECORATIONS).map(DecorationMap.this::get).toArray(TextDecoration.State[]::new);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T @NotNull [] toArray(final T @NotNull [] dest) {
      if (dest.length < MAP_SIZE) {
        return (T[]) Arrays.copyOf(this.toArray(), MAP_SIZE, dest.getClass());
      }
      System.arraycopy(this.toArray(), 0, dest, 0, MAP_SIZE);
      if (dest.length > MAP_SIZE) {
        dest[MAP_SIZE] = null;
      }
      return dest;
    }

    @Override
    public boolean contains(final Object o) {
      return o instanceof TextDecoration.State && super.contains(o);
    }

    @Override
    public int size() {
      return MAP_SIZE;
    }
  }

  static final class KeySet extends AbstractSet<TextDecoration> {
    @Override
    public boolean contains(final Object o) {
      // null-safe
      return o instanceof TextDecoration;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Object @NotNull [] toArray() {
      return StyleImpl.DECORATIONS.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T @NotNull [] toArray(final T @NotNull [] dest) {
      if (dest.length < MAP_SIZE) {
        return (T[]) Arrays.copyOf(StyleImpl.DECORATIONS, MAP_SIZE, dest.getClass());
      }
      System.arraycopy(StyleImpl.DECORATIONS, 0, dest, 0, MAP_SIZE);
      if (dest.length > MAP_SIZE) {
        dest[MAP_SIZE] = null;
      }
      return dest;
    }

    @Override
    public @NotNull Iterator<TextDecoration> iterator() {
      return Spliterators.iterator(Arrays.spliterator(StyleImpl.DECORATIONS));
    }

    @Override
    public int size() {
      return MAP_SIZE;
    }
  }
}
