package me.minidigger.minimessage.text;

import javax.annotation.Nonnull;

public class ParseException extends RuntimeException {
    public ParseException(@Nonnull String message) {
        super(message);
    }
}
