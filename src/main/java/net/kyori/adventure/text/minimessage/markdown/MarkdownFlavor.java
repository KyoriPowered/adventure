package net.kyori.adventure.text.minimessage.markdown;

public interface MarkdownFlavor {

    boolean isBold(char current, char next);

    boolean isItalic(char current, char next);

    boolean isUnderline(char current, char next);

    boolean isStrikeThrough(char current, char next);

    boolean isObfuscate(char current, char next);
}
