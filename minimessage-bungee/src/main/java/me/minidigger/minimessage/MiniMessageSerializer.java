package me.minidigger.minimessage;

import net.md_5.bungee.api.chat.BaseComponent;

import javax.annotation.Nonnull;

/**
 * @deprecated use me.minidigger.minimessage.bungee.MiniMessageSerializer instead
 */
@Deprecated
public final class MiniMessageSerializer {

    private MiniMessageSerializer(){
    }

    /**
     * @deprecated use me.minidigger.minimessage.bungee.MiniMessageSerializer instead
     */
    @Deprecated
    @Nonnull
    public static String serialize(@Nonnull BaseComponent... components) {
        return me.minidigger.minimessage.bungee.MiniMessageSerializer.serialize(components);
    }
}
