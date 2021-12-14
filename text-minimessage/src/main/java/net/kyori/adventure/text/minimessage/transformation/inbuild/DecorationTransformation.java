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
package net.kyori.adventure.text.minimessage.transformation.inbuild;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A transformation that applies any {@link TextDecoration}.
 *
 * @since 4.10.0
 */
public final class DecorationTransformation extends Transformation {
  // vanilla decoration
  public static final String UNDERLINED = "underlined";
  public static final String BOLD_2 = "b";
  public static final String BOLD = "bold";
  public static final String ITALIC_3 = "i";
  public static final String ITALIC_2 = "em";
  public static final String ITALIC = "italic";
  public static final String OBFUSCATED_2 = "obf";
  public static final String OBFUSCATED = "obfuscated";
  public static final String STRIKETHROUGH_2 = "st";
  public static final String STRIKETHROUGH = "strikethrough";
  public static final String UNDERLINED_2 = "u";

  public static final String REVERT = "!";

  /**
   * An unmodifiable map of known decoration aliases.
   *
   * @since 4.10.0
   */
  public static final Map<String, TextDecoration> DECORATION_ALIASES;

  static {
    final Map<String, TextDecoration> aliases = new HashMap<>();
    aliases.put(DecorationTransformation.BOLD_2, TextDecoration.BOLD);
    aliases.put(DecorationTransformation.ITALIC_2, TextDecoration.ITALIC);
    aliases.put(DecorationTransformation.ITALIC_3, TextDecoration.ITALIC);
    aliases.put(DecorationTransformation.UNDERLINED_2, TextDecoration.UNDERLINED);
    aliases.put(DecorationTransformation.STRIKETHROUGH_2, TextDecoration.STRIKETHROUGH);
    aliases.put(DecorationTransformation.OBFUSCATED_2, TextDecoration.OBFUSCATED);
    DECORATION_ALIASES = Collections.unmodifiableMap(aliases);
  }

  private final TextDecoration decoration;
  private final boolean flag;

  /**
   * Create a new decoration.
   *
   * @param name the tag name
   * @param args the tag arguments
   * @return a new transformation
   * @since 4.10.0
   */
  public static DecorationTransformation create(String name, final List<TagPart> args) {
    boolean flag = args.size() != 1 || !args.get(0).isFalse();

    if (name.startsWith(REVERT)) {
      if (args.size() == 1) {
        throw new ParsingException("Can't use both ! short hand and a argument for decoration transformations!", args);
      }
      flag = false;
      name = name.substring(1);
    }

    final @Nullable TextDecoration decoration = parseDecoration(name);

    if (decoration == null) {
      throw new ParsingException("Don't know how to turn '" + name + "' into a decoration", args);
    }

    return new DecorationTransformation(decoration, flag);
  }

  private static TextDecoration parseDecoration(final String name) {
    final TextDecoration alias = DECORATION_ALIASES.get(name);
    return alias != null ? alias : TextDecoration.NAMES.value(name);
  }

  private DecorationTransformation(final TextDecoration decoration, final boolean flag) {
    this.decoration = decoration;
    this.flag = flag;
  }

  @Override
  public Component apply() {
    return Component.empty().decoration(this.decoration, this.flag);
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
