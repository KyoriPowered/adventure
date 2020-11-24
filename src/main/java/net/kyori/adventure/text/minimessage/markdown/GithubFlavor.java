package net.kyori.adventure.text.minimessage.markdown;

public class GithubFlavor implements MarkdownFlavor {

    public static MarkdownFlavor get() {
        return new GithubFlavor();
    }

    @Override
    public boolean isBold(char current, char next) {
        return (current == '*' && next == current) || (current == '_' && next == current);
    }

    @Override
    public boolean isItalic(char current, char next) {
        return (current == '*' && next != current) || (current == '_' && next != current);
    }

    @Override
    public boolean isUnderline(char current, char next) {
        return false;
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
