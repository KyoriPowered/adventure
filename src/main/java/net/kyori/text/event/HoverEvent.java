package net.kyori.text.event;

import com.google.common.base.Objects;
import net.kyori.text.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * A hover event.
 *
 * <p>A hover event displays a {@link HoverEvent#value component} when hovered
 * over by a mouse on the client.</p>
 */
public final class HoverEvent {

    /**
     * The hover event action.
     */
    private final Action action;
    /**
     * The hover event value.
     */
    private final Component value;

    public HoverEvent(final Action action, final Component value) {
        this.action = action;
        this.value = value;
    }

    /**
     * Gets the hover event action.
     *
     * @return the hover event action
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Gets the hover event value.
     *
     * @return the hover event value
     */
    public Component getValue() {
        return this.value;
    }

    /**
     * Create a copy of this hover event.
     *
     * @return a copy of this hover event
     */
    public HoverEvent copy() {
        return new HoverEvent(this.action, this.value.copy());
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(other == null || this.getClass() != other.getClass()) return false;
        final HoverEvent that = (HoverEvent) other;
        return this.action == that.action && Objects.equal(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.action, this.value);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("action", this.action)
            .add("value", this.value)
            .toString();
    }

    /**
     * An enumeration of hover event actions.
     */
    public enum Action {
        /**
         * Shows a {@link Component} when hovered over.
         */
        SHOW_TEXT("show_text", true),
        /**
         * Shows an achievement when hovered over.
         */
        SHOW_ACHIEVEMENT("show_achievement", true),
        /**
         * Shows an item instance when hovered over.
         */
        SHOW_ITEM("show_item", true),
        /**
         * Shows an entity when hovered over.
         */
        SHOW_ENTITY("show_entity", true);

        /**
         * A mapping of {@link #id} to the action for lookups.
         */
        private static final Map<String, Action> BY_ID = new HashMap<>();
        /**
         * The id of this action.
         *
         * <p>This is {@link #name()} in lowercase form.</p>
         */
        private final String id;
        /**
         * If this action is readable.
         *
         * <p>When an action is not readable it will not be deserailized.</p>
         */
        private final boolean readable;

        Action(final String id, final boolean readable) {
            this.id = id;
            this.readable = readable;
        }

        /**
         * Gets the id of this action.
         *
         * @return the id of this action
         */
        public String getId() {
            return this.id;
        }

        /**
         * Tests if this action is readable.
         *
         * @return {@code true} if this action is readable, {@code false} if this
         *     action is not readable
         */
        public boolean isReadable() {
            return this.readable;
        }

        /**
         * Gets an action by its id.
         *
         * @param id the action id
         * @return the action
         */
        @Nullable
        public static Action getById(final String id) {
            return BY_ID.get(id);
        }

        static {
            for(final Action action : values()) {
                BY_ID.put(action.name(), action);
            }
        }
    }
}
