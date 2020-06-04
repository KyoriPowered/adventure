package net.kyori.adventure.scoreboard;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * A scoreboard.
 */
public interface Scoreboard extends List<Component> {
    /**
     * Gets the header.
     *
     * @return the header
     */
    @NonNull Component header();

    /**
     * Sets the header.
     *
     * @param header the header
     * @return the tab view
     */
    @NonNull Scoreboard header(final @NonNull Component header);
}
