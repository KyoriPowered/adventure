package net.kyori.adventure.text.minimessage.transformation;

import org.checkerframework.checker.nullness.qual.NonNull;

public class Util {

    public static @NonNull String stripOuterQuotes(final @NonNull String string) {
        if(string.length() != 1 && startsAndEndsWithQuotes(string)) {
            return string.substring(1, string.length() - 2);
        }
        return string;
    }

    public static boolean startsAndEndsWithQuotes(final @NonNull String string) {
        return string.startsWith("'") && string.endsWith("'") || string.startsWith("\"") && string.endsWith("\"");
    }
}
