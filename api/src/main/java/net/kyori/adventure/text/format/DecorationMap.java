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

import java.io.Serializable;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

final @Unmodifiable class DecorationMap extends AbstractMap<TextDecoration, TextDecoration.State> implements Serializable {
  static final DecorationMap EMPTY = new DecorationMap(
    TextDecoration.State.NOT_SET,
    TextDecoration.State.NOT_SET,
    TextDecoration.State.NOT_SET,
    TextDecoration.State.NOT_SET,
    TextDecoration.State.NOT_SET
  );
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  // key set is universal, all decorations always exist in any given style
  private static final KeySet KEY_SET = new KeySet();
  private static final long serialVersionUID = 3072526425153408678L;

  static DecorationMap fromMap(final Map<TextDecoration, TextDecoration.State> decorationMap) {
    return new DecorationMap(
      decorationMap.getOrDefault(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET),
      decorationMap.getOrDefault(TextDecoration.BOLD, TextDecoration.State.NOT_SET),
      decorationMap.getOrDefault(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET),
      decorationMap.getOrDefault(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET),
      decorationMap.getOrDefault(TextDecoration.ITALIC, TextDecoration.State.NOT_SET)
    );
  }

  static DecorationMap merge(final Map<TextDecoration, TextDecoration.State> first, final Map<TextDecoration, TextDecoration.State> second) {
    return new DecorationMap(
      first.getOrDefault(TextDecoration.OBFUSCATED, second.getOrDefault(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET)),
      first.getOrDefault(TextDecoration.BOLD, second.getOrDefault(TextDecoration.BOLD, TextDecoration.State.NOT_SET)),
      first.getOrDefault(TextDecoration.STRIKETHROUGH, second.getOrDefault(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET)),
      first.getOrDefault(TextDecoration.UNDERLINED, second.getOrDefault(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET)),
      first.getOrDefault(TextDecoration.ITALIC, second.getOrDefault(TextDecoration.ITALIC, TextDecoration.State.NOT_SET))
    );
  }

  private final TextDecoration.@NotNull State obfuscated;
  private final TextDecoration.@NotNull State bold;
  private final TextDecoration.@NotNull State strikethrough;
  private final TextDecoration.@NotNull State underlined;
  private final TextDecoration.@NotNull State italic;

  // lazy
  private transient EntrySet entrySet = null;
  private transient Values values = null;

  private DecorationMap(
    final TextDecoration.@NotNull State obfuscated,
    final TextDecoration.@NotNull State bold,
    final TextDecoration.@NotNull State strikethrough,
    final TextDecoration.@NotNull State underlined,
    final TextDecoration.@NotNull State italic
  ) {
    this.obfuscated = obfuscated;
    this.bold = bold;
    this.strikethrough = strikethrough;
    this.underlined = underlined;
    this.italic = italic;
  }

  public @NotNull DecorationMap with(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
    Objects.requireNonNull(state, "state");
    if (decoration == TextDecoration.OBFUSCATED) {
      return new DecorationMap(state, this.bold, this.strikethrough, this.underlined, this.italic);
    } else if (decoration == TextDecoration.BOLD) {
      return new DecorationMap(this.obfuscated, state, this.strikethrough, this.underlined, this.italic);
    } else if (decoration == TextDecoration.STRIKETHROUGH) {
      return new DecorationMap(this.obfuscated, this.bold, state, this.underlined, this.italic);
    } else if (decoration == TextDecoration.UNDERLINED) {
      return new DecorationMap(this.obfuscated, this.bold, this.strikethrough, state, this.italic);
    } else if (decoration == TextDecoration.ITALIC) {
      return new DecorationMap(this.obfuscated, this.bold, this.strikethrough, this.underlined, state);
    }
    throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
  }

  @Override
  public TextDecoration.State get(final Object decoration) {
    if (decoration == TextDecoration.OBFUSCATED) {
      return this.obfuscated;
    } else if (decoration == TextDecoration.BOLD) {
      return this.bold;
    } else if (decoration == TextDecoration.STRIKETHROUGH) {
      return this.strikethrough;
    } else if (decoration == TextDecoration.UNDERLINED) {
      return this.underlined;
    } else if (decoration == TextDecoration.ITALIC) {
      return this.italic;
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
    return DECORATIONS.length;
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
    final DecorationMap that = (DecorationMap) other;
    return this.obfuscated == that.obfuscated
      && this.bold == that.bold
      && this.strikethrough == that.strikethrough
      && this.underlined == that.underlined
      && this.italic == that.italic;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      this.obfuscated,
      this.bold,
      this.strikethrough,
      this.underlined,
      this.italic
    );
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
      return DECORATIONS.length;
    }
  }

  final class Values extends AbstractCollection<TextDecoration.State> {
    @Override
    public @NotNull Iterator<TextDecoration.State> iterator() {
      return Spliterators.iterator(Arrays.spliterator(this.toArray(new TextDecoration.State[0])));
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Object @NotNull [] toArray() {
      return new TextDecoration.State[] {DecorationMap.this.obfuscated, DecorationMap.this.bold, DecorationMap.this.strikethrough, DecorationMap.this.underlined, DecorationMap.this.italic};
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T @NotNull [] toArray(final T @NotNull [] a) {
      if (a.length < DECORATIONS.length) {
        return (T[]) Arrays.copyOf(this.toArray(), DECORATIONS.length, a.getClass());
      }
      System.arraycopy(this.toArray(), 0, a, 0, DECORATIONS.length);
      return a;
    }

    @Override
    public boolean contains(final Object o) {
      return o instanceof TextDecoration.State && super.contains(o);
    }

    @Override
    public int size() {
      return DECORATIONS.length;
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
      return DECORATIONS.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T @NotNull [] toArray(final T @NotNull [] a) {
      if (a.length < DECORATIONS.length) {
        return (T[]) Arrays.copyOf(DECORATIONS, DECORATIONS.length, a.getClass());
      }
      System.arraycopy(DECORATIONS, 0, a, 0, DECORATIONS.length);
      return a;
    }

    @Override
    public @NotNull Iterator<TextDecoration> iterator() {
      return Spliterators.iterator(Arrays.spliterator(DECORATIONS));
    }

    @Override
    public int size() {
      return DECORATIONS.length;
    }
  }
}
