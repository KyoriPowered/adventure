package net.kyori.adventure.tab;

import net.kyori.adventure.skin.Skin;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A tab list entry.
 */
public interface TabEntry {

    /**
     * Gets the text.
     *
     * @return the text
     */
    @NonNull Component text();

    /**
     * Sets the text.
     *
     * @param text the text
     * @return the tab entry
     */
    @NonNull TabEntry text(final @NonNull Component text);

    /**
     * Gets the ping.
     *
     * @return the ping, in milliseconds
     */
    int ping();

    /**
     * Sets the ping.
     *
     * @param ping the ping
     * @return the tab entry
     */
    @NonNull TabEntry ping(final int ping);

    /**
     * Gets the skin.
     *
     * @return the skin
     */
    @NonNull Skin skin();

    /**
     * Sets the skin.
     *
     * @param skin the skin
     * @return the tab entry
     */
    @NonNull TabEntry skin(final @NonNull Skin skin);
}
