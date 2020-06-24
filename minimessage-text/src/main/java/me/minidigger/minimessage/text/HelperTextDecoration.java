package me.minidigger.minimessage.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.function.UnaryOperator;
import javax.annotation.Nonnull;

enum HelperTextDecoration {
    BOLD(b -> b.decoration(TextDecoration.BOLD, true)),
    ITALIC(b -> b.decoration(TextDecoration.ITALIC, true)),
    UNDERLINED(b -> b.decoration(TextDecoration.UNDERLINED, true)),
    STRIKETHROUGH(b -> b.decoration(TextDecoration.STRIKETHROUGH, true)),
    OBFUSCATED(b -> b.decoration(TextDecoration.OBFUSCATED, true));

    private final UnaryOperator<Component> builder;

    HelperTextDecoration(@Nonnull UnaryOperator<Component> builder) {
        this.builder = builder;
    }

    @Nonnull
    public Component apply(@Nonnull Component comp) {
        return builder.apply(comp);
    }
}
