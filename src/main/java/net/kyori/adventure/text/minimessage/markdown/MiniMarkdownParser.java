/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage.markdown;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Internal class for markdown handling.
 *
 * @since 4.0.0
 */
public final class MiniMarkdownParser {

  private MiniMarkdownParser() {
  }

  /**
   * Strip any markdown formatting that would be interpreted by {@code markdownFlavor}.
   *
   * @param input the input string
   * @param markdownFlavor markdown flavor to test against
   * @return the stripped input
   * @since 4.1.0
   */
  public static @NonNull String stripMarkdown(final @NonNull String input, final @NonNull MarkdownFlavor markdownFlavor) {
    return handle(input, true, markdownFlavor);
  }

  /**
   * Parse the markdown input and return it as a MiniMessage string.
   *
   * @param input the input string
   * @param markdownFlavor the flavour of markdown to recognize
   * @return a modified string
   * @since 4.1.0
   */
  public static @NonNull String parse(final @NonNull String input, final @NonNull MarkdownFlavor markdownFlavor) {
    return handle(input, false, markdownFlavor);
  }

  private static @NonNull String handle(final @NonNull String input, final boolean strip, final @NonNull MarkdownFlavor markdownFlavor) {
    final StringBuilder sb = new StringBuilder();

    int bold = -1;
    Insert boldSkip = null;
    int italic = -1;
    Insert italicSkip = null;
    int underline = -1;
    Insert underlineSkip = null;
    int strikeThrough = -1;
    Insert strikeThroughSkip = null;
    int obfuscate = -1;
    Insert obfuscateSkip = null;

    final List<Insert> inserts = new ArrayList<>();
    int skip = 0;
    for(int i = 0; i + skip < input.length(); i++) {
      final int currIndex = i + skip;
      final char c = input.charAt(currIndex);
      final char n = next(currIndex, input);

      boolean shouldSkip = false;
      if(markdownFlavor.isBold(c, n)) {
        if(bold == -1) {
          bold = sb.length();
          boldSkip = new Insert(sb.length(), c + "");
        } else {
          inserts.add(new Insert(bold, "<" + Tokens.BOLD + ">"));
          inserts.add(new Insert(sb.length(), "</" + Tokens.BOLD + ">"));
          bold = -1;
        }
        skip += c == n ? 1 : 0;
        shouldSkip = true;
      } else if(markdownFlavor.isItalic(c, n)) {
        if(italic == -1) {
          italic = sb.length();
          italicSkip = new Insert(sb.length(), c + "");
        } else {
          inserts.add(new Insert(italic, "<" + Tokens.ITALIC + ">"));
          inserts.add(new Insert(sb.length(), "</" + Tokens.ITALIC + ">"));
          italic = -1;
        }
        skip += c == n ? 1 : 0;
        shouldSkip = true;
      } else if(markdownFlavor.isUnderline(c, n)) {
        if(underline == -1) {
          underline = sb.length();
          underlineSkip = new Insert(sb.length(), c + "");
        } else {
          inserts.add(new Insert(underline, "<" + Tokens.UNDERLINED + ">"));
          inserts.add(new Insert(sb.length(), "</" + Tokens.UNDERLINED + ">"));
          underline = -1;
        }
        skip += c == n ? 1 : 0;
        shouldSkip = true;
      } else if(markdownFlavor.isStrikeThrough(c, n)) {
        if(strikeThrough == -1) {
          strikeThrough = sb.length();
          strikeThroughSkip = new Insert(sb.length(), c + "");
        } else {
          inserts.add(new Insert(strikeThrough, "<" + Tokens.STRIKETHROUGH + ">"));
          inserts.add(new Insert(sb.length(), "</" + Tokens.STRIKETHROUGH + ">"));
          strikeThrough = -1;
        }
        skip += c == n ? 1 : 0;
        shouldSkip = true;
      } else if(markdownFlavor.isObfuscate(c, n)) {
        if(obfuscate == -1) {
          obfuscate = sb.length();
          obfuscateSkip = new Insert(sb.length(), c + "");
        } else {
          inserts.add(new Insert(obfuscate, "<" + Tokens.OBFUSCATED + ">"));
          inserts.add(new Insert(sb.length(), "</" + Tokens.OBFUSCATED + ">"));
          obfuscate = -1;
        }
        skip += c == n ? 1 : 0;
        shouldSkip = true;
      }

      if(!shouldSkip) {
        sb.append(c);
      }
    }

    if(strip) {
      inserts.clear();
    } else {
      inserts.sort(Comparator.comparing(Insert::pos).thenComparing(Insert::value).reversed());
    }

    if(underline != -1) {
      inserts.add(underlineSkip);
    }
    if(bold != -1) {
      inserts.add(boldSkip);
    }
    if(italic != -1) {
      inserts.add(italicSkip);
    }
    if(strikeThrough != -1) {
      inserts.add(strikeThroughSkip);
    }
    if(obfuscate != -1) {
      inserts.add(obfuscateSkip);
    }

    for(final Insert el : inserts) {
      sb.insert(el.pos(), el.value());
    }

    return sb.toString();
  }

  private static char next(final int index, final @NonNull String input) {
    if(index < input.length() - 1) {
      return input.charAt(index + 1);
    } else {
      return ' ';
    }
  }

  static final class Insert implements Examinable {
    private final int pos;
    private final String value;

    Insert(final int pos, final @NonNull String value) {
      this.pos = pos;
      this.value = value;
    }

    private int pos() {
      return this.pos;
    }

    private @NonNull String value() {
      return this.value;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("pos", this.pos),
        ExaminableProperty.of("value", this.value)
      );
    }

    @Override
    public String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }
  }
}
