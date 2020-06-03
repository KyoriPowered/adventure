package net.kyori.adventure.tab;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Comparator;
import java.util.List;

/**
 * A tab list header and footer.
 */
public interface TabList {

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
     * @return the tab list
     */
    @NonNull TabList header(final @NonNull Component header);

    /**
     * Gets the footer.
     *
     * @return the footer
     */
    @NonNull Component footer();

    /**
     * Sets the footer.
     *
     * @param footer the footer
     * @return the tab list
     */
    @NonNull TabList footer(final @NonNull Component footer);

    /**
     * Gets the number of columns.
     *
     * @return the number of columns
     */
    int columns();

    /**
     * Sets the number of columns.
     *
     * @param columns the number of columns
     * @return the tab list
     */
    @NonNull TabList columns(final int columns);

    /**
     * Gets the tab entry list.
     *
     * @return the tab entries
     */
    @NonNull List<TabEntry> entries();

    /**
     * Sets the tab entry list.
     *
     * @param entries the tab entries, traversal order matters
     * @return the tab entries
     */
    @NonNull TabList entries(final @NonNull Iterable<TabEntry> entries);

    /**
     * Adds a tab entry.
     *
     * @param entry the tab entry
     * @return the tab list
     */
    @NonNull TabList entryAdd(final @NonNull TabEntry entry);

    /**
     * Removes a tab entry.
     *
     * @param entry the tab entry
     * @return the tab list
     */
    @NonNull TabList entryRemove(final @NonNull TabEntry entry);

    /**
     * Gets the comparator that sorts tab entries.
     *
     * @return the tab entry comparator
     */
    @NonNull Comparator<TabEntry> comparator();

    /**
     * Sets the comparator that sorts tab entries.
     *
     * @param comparator a comparator, or null for no sorting
     * @return the tab list
     */
    @NonNull TabList comparator(final @Nullable Comparator<TabEntry> comparator);
}
