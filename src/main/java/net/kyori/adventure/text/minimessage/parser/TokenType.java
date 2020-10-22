package net.kyori.adventure.text.minimessage.parser;

public enum TokenType {

    OPEN_TAG_START("<"),
    CLOSE_TAG_START("</"),
    TAG_END(">"),

    PARAM_SEPARATOR(":"),

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
}
