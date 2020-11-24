package net.kyori.adventure.text.minimessage.markdown;

public class DiscordFlavor implements MarkdownFlavor {

    public static MarkdownFlavor get() {
        return new DiscordFlavor();
    }

    @Override
    public boolean isBold(char current, char next) {
        return (current == '*' && next == current);
    }

    @Override
    public boolean isItalic(char current, char next) {
        return (current == '*' && next != current);
    }

    @Override
    public boolean isUnderline(char current, char next) {
        return current == '_' && next == current;
    }

    @Override
    public boolean isStrikeThrough(char current, char next) {
        return current == '~' && next == current;
    }

    @Override
    public boolean isObfuscate(char current, char next) {
        return current == '|' && next == current;
    }
}
