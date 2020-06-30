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
package net.kyori.adventure.text.minimessage;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class MiniMarkdownParser {

  private MiniMarkdownParser() {
  }

  @NonNull
  public static String stripMarkdown(@NonNull String input) {
    return handle(input, true);
  }

  @NonNull
  public static String parse(@NonNull String input) {
    return handle(input, false);
  }

  @NonNull
  private static String handle(@NonNull String input, boolean strip) {
    StringBuilder sb = new StringBuilder();

    int bold = -1;
    Insert boldSkip = null;
    int italic = -1;
    Insert italicSkip = null;
    int underline = -1;
    Insert underlineSkip = null;

    List<Insert> inserts = new ArrayList<>();
    int skip = 0;
    for (int i = 0; i + skip < input.length(); i++) {
      int currIndex = i + skip;
      char c = input.charAt(currIndex);

      boolean shouldSkip = false;
      if (c == Constants.MD_EMPHASIS_1) {
        char n = next(currIndex, input);
        if (n == Constants.MD_EMPHASIS_1) {
          if (bold == -1) {
            bold = sb.length();
            boldSkip = new Insert(sb.length(), c + "");
          } else {
            inserts.add(new Insert(bold, "<" + Constants.BOLD + ">"));
            inserts.add(new Insert(sb.length(), "</" + Constants.BOLD + ">"));
            bold = -1;
          }
          skip++;
        } else {
          if (italic == -1) {
            italic = sb.length();
            italicSkip = new Insert(sb.length(), c + "");
          } else {
            inserts.add(new Insert(italic, "<" + Constants.ITALIC + ">"));
            inserts.add(new Insert(sb.length(), "</" + Constants.ITALIC + ">"));
            italic = -1;
          }
        }
        shouldSkip = true;
      } else if (c == Constants.MD_EMPHASIS_2) {
        char n = next(currIndex, input);
        if (n == Constants.MD_EMPHASIS_2) {
          if (bold == -1) {
            bold = sb.length();
            boldSkip = new Insert(sb.length(), c + "");
          } else {
            inserts.add(new Insert(bold, "<" + Constants.BOLD + ">"));
            inserts.add(new Insert(sb.length(), "</" + Constants.BOLD + ">"));
            bold = -1;
          }
          skip++;
        } else {
          if (italic == -1) {
            italic = currIndex;
            italicSkip = new Insert(sb.length(), c + "");
          } else {
            inserts.add(new Insert(italic, "<" + Constants.ITALIC + ">"));
            inserts.add(new Insert(currIndex - 1, "</" + Constants.ITALIC + ">"));
            italic = -1;
          }
        }
        shouldSkip = true;
      } else if (c == Constants.MD_UNDERLINE) {
        char n = next(currIndex, input);
        if (n == Constants.MD_UNDERLINE) {
          if (underline == -1) {
            underline = sb.length();
            underlineSkip = new Insert(sb.length(), c + "");
          } else {
            inserts.add(new Insert(underline, "<" + Constants.UNDERLINED + ">"));
            inserts.add(new Insert(sb.length(), "</" + Constants.UNDERLINED + ">"));
            underline = -1;
          }
          skip++;
          shouldSkip = true;
        }
      }

      if (!shouldSkip) {
        sb.append(c);
      }
    }

    if (strip) {
      inserts.clear();
    } else {
      inserts.sort(Comparator.comparing(Insert::getPos).thenComparing(Insert::getValue).reversed());
    }

    if (underline != -1) {
      inserts.add(underlineSkip);
    }
    if (bold != -1) {
      inserts.add(boldSkip);
    }
    if (italic != -1) {
      inserts.add(italicSkip);
    }

    for (Insert el : inserts) {
      sb.insert(el.getPos(), el.getValue());
    }

    return sb.toString();
  }

  private static char next(int index, @NonNull String input) {
    if (index < input.length() - 1) {
      return input.charAt(index + 1);
    } else {
      return ' ';
    }
  }

  static class Insert {
    private final int pos;
    private final String value;

    public int getPos() {
      return pos;
    }

    @NonNull
    public String getValue() {
      return value;
    }

    public Insert(int pos, @NonNull String value) {
      this.pos = pos;
      this.value = value;
    }

    @Override
    @NonNull
    public String toString() {
      return new StringJoiner(", ", Insert.class.getSimpleName() + "[", "]")
        .add("pos=" + pos)
        .add("value='" + value + "'")
        .toString();
    }
  }
}
