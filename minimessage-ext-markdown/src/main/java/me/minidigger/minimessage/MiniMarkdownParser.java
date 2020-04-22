package me.minidigger.minimessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import javax.annotation.Nonnull;

public class MiniMarkdownParser {

    private MiniMarkdownParser() {
    }

    @Nonnull
    public static String stripMarkdown(@Nonnull String input) {
        return handle(input, true);
    }

    @Nonnull
    public static String parse(@Nonnull String input) {
        return handle(input, false);
    }

    @Nonnull
    private static String handle(@Nonnull String input, boolean strip) {
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
            if (c == Constants.emphasis1) {
                char n = next(currIndex, input);
                if (n == Constants.emphasis1) {
                    if (bold == -1) {
                        bold = sb.length();
                        boldSkip = new Insert(sb.length(), c + "");
                    } else {
                        inserts.add(new Insert(bold, "<" + Constants.boldTag + ">"));
                        inserts.add(new Insert(sb.length(), "</" + Constants.boldTag + ">"));
                        bold = -1;
                    }
                    skip++;
                } else {
                    if (italic == -1) {
                        italic = sb.length();
                        italicSkip = new Insert(sb.length(), c + "");
                    } else {
                        inserts.add(new Insert(italic, "<" + Constants.italicTag + ">"));
                        inserts.add(new Insert(sb.length(), "</" + Constants.italicTag + ">"));
                        italic = -1;
                    }
                }
                shouldSkip = true;
            } else if (c == Constants.emphasis2) {
                char n = next(currIndex, input);
                if (n == Constants.emphasis2) {
                    if (bold == -1) {
                        bold = sb.length();
                        boldSkip = new Insert(sb.length(), c + "");
                    } else {
                        inserts.add(new Insert(bold, "<" + Constants.boldTag + ">"));
                        inserts.add(new Insert(sb.length(), "</" + Constants.boldTag + ">"));
                        bold = -1;
                    }
                    skip++;
                } else {
                    if (italic == -1) {
                        italic = currIndex;
                        italicSkip = new Insert(sb.length(), c + "");
                    } else {
                        inserts.add(new Insert(italic, "<" + Constants.italicTag + ">"));
                        inserts.add(new Insert(currIndex - 1, "</" + Constants.italicTag + ">"));
                        italic = -1;
                    }
                }
                shouldSkip = true;
            } else if (c == Constants.underline) {
                char n = next(currIndex, input);
                if (n == Constants.underline) {
                    if (underline == -1) {
                        underline = sb.length();
                        underlineSkip = new Insert(sb.length(), c + "");
                    } else {
                        inserts.add(new Insert(underline, "<" + Constants.underlineTag + ">"));
                        inserts.add(new Insert(sb.length(), "</" + Constants.underlineTag + ">"));
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

    private static char next(int index, @Nonnull String input) {
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

        @Nonnull
        public String getValue() {
            return value;
        }

        public Insert(int pos, @Nonnull String value) {
            this.pos = pos;
            this.value = value;
        }

        @Override
        @Nonnull
        public String toString() {
            return new StringJoiner(", ", Insert.class.getSimpleName() + "[", "]")
                    .add("pos=" + pos)
                    .add("value='" + value + "'")
                    .toString();
        }
    }
}
