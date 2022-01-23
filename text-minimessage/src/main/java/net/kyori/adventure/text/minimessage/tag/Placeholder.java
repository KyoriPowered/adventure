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
package net.kyori.adventure.text.minimessage.tag;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Tag resolvers producing tags that insert fixed values.
 *
 * <p>These are effectively placeholders.</p>
 *
 * @since 4.10.0
 */
public final class Placeholder {
  private Placeholder() {
  }

  /**
   * Creates a placeholder that inserts a MiniMessage string. The inserted string will impact
   * the rest of the parse process.
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  public static TagResolver.@NotNull Single miniMessage(final @NotNull String key, final @NotNull String value) {
    return TagResolver.resolver(key, Tag.miniMessage(value));
  }

  /**
   * Creates a placeholder that inserts a raw string, ignoring any MiniMessage tags present.
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  public static TagResolver.@NotNull Single raw(final @NotNull String key, final @NotNull String value) {
    requireNonNull(value, "value");
    return Placeholder.component(key, Component.text(value));
  }

  /**
   * Creates a replacement that inserts a component.
   *
   * <p>This replacement is auto-closing, so its style will not influence the style of following components.</p>
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  public static TagResolver.@NotNull Single component(final @NotNull String key, final @NotNull ComponentLike value) {
    return TagResolver.resolver(
      key,
      new ComponentPlaceholder(
        key,
        Objects.requireNonNull(
          Objects.requireNonNull(value, "value").asComponent(),
          "value must not resolve to null"
        )
      )
    );
  }

  static final class ComponentPlaceholder implements Inserting, Examinable {
    private final String key;
    private final Component value;

    ComponentPlaceholder(final String key, final Component value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public Component value() {
      return this.value;
    }

    @Override
    public boolean allowsChildren() {
      return false;
    }

    @Override
    public @NotNull String examinableName() {
      return this.key;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }
}
