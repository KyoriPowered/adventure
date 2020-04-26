package me.minidigger.minimessage;

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Map;
import javax.annotation.Nonnull;

/**
 * @deprecated use me.minidigger.minimessage.bungee.MiniMessageParser instead
 */
@Deprecated
public class MiniMessageParser {

    private MiniMessageParser() {

    }

    /**
     * @deprecated use me.minidigger.minimessage.bungee.MiniMessageParser instead
     */
    @Deprecated
    @Nonnull
    public static String escapeTokens(@Nonnull String richMessage) {
        return me.minidigger.minimessage.bungee.MiniMessageParser.escapeTokens(richMessage);
    }

    /**
     * @deprecated use me.minidigger.minimessage.bungee.MiniMessageParser instead
     */
    @Deprecated
    @Nonnull
    public static String stripTokens(@Nonnull String richMessage) {
        return me.minidigger.minimessage.bungee.MiniMessageParser.stripTokens(richMessage);
    }

    /**
     * @deprecated use me.minidigger.minimessage.bungee.MiniMessageParser instead
     */
    @Deprecated
    @Nonnull
    public static String handlePlaceholders(@Nonnull String richMessage, @Nonnull String... placeholders) {
        return me.minidigger.minimessage.bungee.MiniMessageParser.handlePlaceholders(richMessage, placeholders);
    }

    /**
     * @deprecated use me.minidigger.minimessage.bungee.MiniMessageParser instead
     */
    @Deprecated
    @Nonnull
    public static String handlePlaceholders(@Nonnull String richMessage, @Nonnull Map<String, String> placeholders) {
        return me.minidigger.minimessage.bungee.MiniMessageParser.handlePlaceholders(richMessage, placeholders);
    }

    /**
     * @deprecated use me.minidigger.minimessage.bungee.MiniMessageParser instead
     */
    @Deprecated
    @Nonnull
    public static BaseComponent[] parseFormat(@Nonnull String richMessage) {
        return me.minidigger.minimessage.bungee.MiniMessageParser.parseFormat(richMessage);
    }

    /**
     * @deprecated use me.minidigger.minimessage.bungee.MiniMessageParser instead
     */
    @Deprecated
    @Nonnull
    public static BaseComponent[] parseFormat(@Nonnull String richMessage, @Nonnull String... placeholders) {
        return me.minidigger.minimessage.bungee.MiniMessageParser.parseFormat(richMessage, placeholders);
    }

    /**
     * @deprecated use me.minidigger.minimessage.bungee.MiniMessageParser instead
     */
    @Deprecated
    @Nonnull
    public static BaseComponent[] parseFormat(@Nonnull String richMessage, @Nonnull Map<String, String> placeholders) {
        return me.minidigger.minimessage.bungee.MiniMessageParser.parseFormat(richMessage, placeholders);
    }
}
