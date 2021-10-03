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
package net.kyori.adventure.text.minimessage.transformation.inbuild;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A transformation that applies any {@link TextDecoration}.
 *
 * @since 4.1.0
 */
public final class DecorationTransformation extends Transformation {
  /**
   * An unmodifiable map of known decoration aliases.
   *
   * @since 4.2.0
   */
  public static final Map<String, TextDecoration> DECORATION_ALIASES;

  static {
    final Map<String, TextDecoration> aliases = new HashMap<>();
    aliases.put(Tokens.BOLD_2, TextDecoration.BOLD);
    aliases.put(Tokens.ITALIC_2, TextDecoration.ITALIC);
    aliases.put(Tokens.ITALIC_3, TextDecoration.ITALIC);
    aliases.put(Tokens.UNDERLINED_2, TextDecoration.UNDERLINED);
    aliases.put(Tokens.STRIKETHROUGH_2, TextDecoration.STRIKETHROUGH);
    aliases.put(Tokens.OBFUSCATED_2, TextDecoration.OBFUSCATED);
    DECORATION_ALIASES = Collections.unmodifiableMap(aliases);
  }

  private final TextDecoration decoration;

  /**
   * Create a new decoration.
   *
   * @param name the tag name
   * @param args the tag arguments
   * @return a new transformation
   * @since 4.2.0
   */
  public static DecorationTransformation create(final String name, final List<TagPart> args) {
    final @Nullable TextDecoration decoration = parseDecoration(name);

    if (decoration == null) {
      throw new ParsingException("Don't know how to turn '" + name + "' into a decoration", args);
    }

    return new DecorationTransformation(decoration);
  }

  private static TextDecoration parseDecoration(final String name) {
    final TextDecoration alias = DECORATION_ALIASES.get(name);
    return alias != null ? alias : TextDecoration.NAMES.value(name);
  }

  private DecorationTransformation(final TextDecoration decoration) {
    this.decoration = decoration;
  }

  @Override
  public Component apply() {
    return Component.empty().decorate(this.decoration);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("decoration", this.decoration));
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final DecorationTransformation that = (DecorationTransformation) other;
    return this.decoration == that.decoration;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.decoration);
  }
}
