/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.fusesource.jansi.Ansi;

import java.util.HashMap;

enum Formats {
  BLACK(NamedTextColor.BLACK, Ansi.ansi().fg(Ansi.Color.BLACK).boldOff(), false, "black"),
  DARK_BLUE(NamedTextColor.DARK_BLUE, Ansi.ansi().fg(Ansi.Color.BLUE).boldOff(), false, "darkblue"),
  DARK_GREEN(NamedTextColor.DARK_GREEN, Ansi.ansi().fg(Ansi.Color.GREEN).boldOff(), false, "darkgreen"),
  DARK_AQUA(NamedTextColor.DARK_AQUA, Ansi.ansi().fg(Ansi.Color.CYAN).boldOff(), false, "darkaqua", "darkcyan"),
  DARK_RED(NamedTextColor.DARK_RED, Ansi.ansi().fg(Ansi.Color.RED).boldOff(), false, "darkred"),
  DARK_PURPLE(NamedTextColor.DARK_PURPLE, Ansi.ansi().fg(Ansi.Color.MAGENTA).boldOff(), false, "darkpurple", "darkmagenta"),
  GOLD(NamedTextColor.GOLD, Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff(), false, "gold", "orange", "darkyellow"),
  GRAY(NamedTextColor.GRAY, Ansi.ansi().fg(Ansi.Color.WHITE).boldOff(), false, "gray", "grey", "silver"),
  DARK_GRAY(NamedTextColor.DARK_GRAY, Ansi.ansi().fgBright(Ansi.Color.BLACK), false, "darkgray", "darkgrey"),
  BLUE(NamedTextColor.BLUE, Ansi.ansi().fgBright(Ansi.Color.BLUE), false, "blue"),
  GREEN(NamedTextColor.GREEN, Ansi.ansi().fgBright(Ansi.Color.GREEN), false, "green", "lime"),
  AQUA(NamedTextColor.AQUA, Ansi.ansi().fgBright(Ansi.Color.CYAN), false, "aqua", "cyan"),
  RED(NamedTextColor.RED, Ansi.ansi().fgBright(Ansi.Color.RED), false, "red"),
  LIGHT_PURPLE(NamedTextColor.LIGHT_PURPLE, Ansi.ansi().fgBright(Ansi.Color.MAGENTA), false, "purple", "magenta"),
  YELLOW(NamedTextColor.YELLOW, Ansi.ansi().fgBright(Ansi.Color.YELLOW), false, "yellow"),
  WHITE(NamedTextColor.WHITE, Ansi.ansi().fgBright(Ansi.Color.WHITE), false, "white"),

  OBFUSCATED(TextDecoration.OBFUSCATED, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW), true, "obfuscated"),
  BOLD(TextDecoration.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE), true, "bold"),
  STRIKETHROUGH(TextDecoration.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON), true, "strikethrough", "strike"),
  UNDERLINE(TextDecoration.UNDERLINED, Ansi.ansi().a(Ansi.Attribute.UNDERLINE), true, "underline"),
  ITALIC(TextDecoration.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC), true, "italic"),
  RESET(AnsiComponentSerializerImpl.Reset.INSTANCE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.DEFAULT), false, "reset");

  static final Ansi SOFT_RESET = Ansi.ansi().reset();

  private static final HashMap<TextFormat, Formats> mMap = new HashMap<>();
  private static final HashMap<String, Formats> mNameMap = new HashMap<>();

  private final TextFormat format;
  private final Ansi escapeString;
  private final String[] mNames;
  private final boolean mIsFormat;

  static {
    for(final Formats format : values()) {
      mMap.put(format.textFormat(), format);
      for(final String name : format.mNames) {
        mNameMap.put(name, format);
      }
    }
  }

  Formats(final TextFormat textFormat, final Ansi ansi, final boolean isFormat, final String... names) {
    this.format = textFormat;
    this.escapeString = ansi;
    this.mNames = names;
    this.mIsFormat = isFormat;
  }

  public TextFormat textFormat() {
    return this.format;
  }

  public Ansi ansi() {
    return this.escapeString;
  }

  public boolean isFormat() {
    return this.mIsFormat;
  }

  @Override
  public String toString() {
    return this.escapeString.toString();
  }

  public static Formats byFormat(final TextFormat textFormat) {
    return mMap.get(textFormat);
  }

  public static Formats byName(final String name) {
    return mNameMap.get(name.toLowerCase());
  }

}
