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
import net.kyori.adventure.util.Index;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.fusesource.jansi.Ansi;

import java.util.HashMap;
import java.util.function.Function;

enum Formats {
  //NamedColours
  BLACK(NamedTextColor.BLACK, Ansi.ansi().fg(Ansi.Color.BLACK).boldOff()),
  DARK_BLUE(NamedTextColor.DARK_BLUE, Ansi.ansi().fg(Ansi.Color.BLUE).boldOff()),
  DARK_GREEN(NamedTextColor.DARK_GREEN, Ansi.ansi().fg(Ansi.Color.GREEN).boldOff()),
  DARK_AQUA(NamedTextColor.DARK_AQUA, Ansi.ansi().fg(Ansi.Color.CYAN).boldOff()),
  DARK_RED(NamedTextColor.DARK_RED, Ansi.ansi().fg(Ansi.Color.RED).boldOff()),
  DARK_PURPLE(NamedTextColor.DARK_PURPLE, Ansi.ansi().fg(Ansi.Color.MAGENTA).boldOff()),
  GOLD(NamedTextColor.GOLD, Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff()),
  GRAY(NamedTextColor.GRAY, Ansi.ansi().fg(Ansi.Color.WHITE).boldOff()),
  DARK_GRAY(NamedTextColor.DARK_GRAY, Ansi.ansi().fgBright(Ansi.Color.BLACK)),
  BLUE(NamedTextColor.BLUE, Ansi.ansi().fgBright(Ansi.Color.BLUE)),
  GREEN(NamedTextColor.GREEN, Ansi.ansi().fgBright(Ansi.Color.GREEN)),
  AQUA(NamedTextColor.AQUA, Ansi.ansi().fgBright(Ansi.Color.CYAN)),
  RED(NamedTextColor.RED, Ansi.ansi().fgBright(Ansi.Color.RED)),
  LIGHT_PURPLE(NamedTextColor.LIGHT_PURPLE, Ansi.ansi().fgBright(Ansi.Color.MAGENTA)),
  YELLOW(NamedTextColor.YELLOW, Ansi.ansi().fgBright(Ansi.Color.YELLOW)),
  WHITE(NamedTextColor.WHITE, Ansi.ansi().fgBright(Ansi.Color.WHITE)),
  //Decorations
  OBFUSCATED(TextDecoration.OBFUSCATED, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW)),
  BOLD(TextDecoration.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE)),
  STRIKETHROUGH(TextDecoration.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON)),
  UNDERLINE(TextDecoration.UNDERLINED, Ansi.ansi().a(Ansi.Attribute.UNDERLINE)),
  ITALIC(TextDecoration.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC)),
  RESET(ANSIComponentSerializerImpl.Reset.INSTANCE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.DEFAULT));

  static final Ansi SOFT_RESET = Ansi.ansi().reset();

  private static final Index<TextFormat, Formats> textFormatIndex;

  private final TextFormat format;
  private final Ansi escapeString;

  static {
    textFormatIndex = Index.create(Formats::textFormat,Formats.values());
  }

  Formats(final TextFormat textFormat, final Ansi ansi){
    this.format = textFormat;
    this.escapeString = ansi;
  }

  public TextFormat textFormat(){
    return this.format;
  }

  public Ansi ansi() {
    return this.escapeString;
  }

  @Override
  public String toString(){
    return this.escapeString.toString();
  }

  public @Nullable static Formats byFormat(final TextFormat textFormat){
    return textFormatIndex.value(textFormat);
  }

}
