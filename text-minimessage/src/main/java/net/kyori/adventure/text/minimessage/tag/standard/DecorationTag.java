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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;

/**
 * A transformation that applies any {@link TextDecoration}.
 *
 * @since 4.10.0
 */
public final class DecorationTag {
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
  static final Map<String, TextDecoration> DECORATION_ALIASES;

  static {
    final Map<String, TextDecoration> aliases = new HashMap<>();
    aliases.put(BOLD_2, TextDecoration.BOLD);
    aliases.put(ITALIC_2, TextDecoration.ITALIC);
    aliases.put(ITALIC_3, TextDecoration.ITALIC);
    aliases.put(UNDERLINED_2, TextDecoration.UNDERLINED);
    aliases.put(STRIKETHROUGH_2, TextDecoration.STRIKETHROUGH);
    aliases.put(OBFUSCATED_2, TextDecoration.OBFUSCATED);
    DECORATION_ALIASES = Collections.unmodifiableMap(aliases);
  }

  private DecorationTag() {
  }

  static Tag create(final TextDecoration toApply, final ArgumentQueue args, final Context ctx) {
    final boolean flag = !args.hasNext() || !args.pop().isFalse();

    return Tag.styling(toApply.withState(flag));
  }

  static Tag createNegated(final TextDecoration toApply) {
    return Tag.styling(toApply.withState(false));
  }
}
