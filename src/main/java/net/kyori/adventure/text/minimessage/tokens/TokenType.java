package net.kyori.adventure.text.minimessage.tokens;

import java.util.StringJoiner;

public enum TokenType {

    OPEN_TAG_START("<"),
    CLOSE_TAG_START("</"),
    TAG_END(">"),

    PARAM_SEPERATOR(":"),

    QUOTE_START("'"),
    QUOTE_END("'"),

    STRING(""),
    NAME("")
    ;

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TokenType.class.getSimpleName() + "[", "]")
                .add("name='" + name() + "'")
                .add("value='" + value + "'")
                .toString();
    }
}
