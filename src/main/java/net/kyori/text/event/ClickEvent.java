package net.kyori.text.event;

import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * A click event.
 *
 * <p>A click event processes an {@link Action} when clicked on.</p>
 */
@Immutable
public final class ClickEvent {

    /**
     * The click event action.
     */
    private final Action action;
    /**
     * The click event value.
     */
    private final String value;

    public ClickEvent(final Action action, final String value) {
        this.action = action;
        this.value = value;
    }

    /**
     * Gets the click event action.
     *
     * @return the click event action
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Gets the click event value.
     *
     * @return the click event value
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(other == null || this.getClass() != other.getClass()) return false;
        final ClickEvent that = (ClickEvent) other;
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
     * An enumeration of click event actions.
     */
    public enum Action {
        /**
         * Opens a url when clicked.
         */
        OPEN_URL("open_url", true),
        /**
         * Opens a file when clicked.
         *
         * <p>This action is not readable, and may only be used locally on the client.</p>
         */
        OPEN_FILE("open_file", false),
        /**
         * Runs a command when clicked.
         */
        RUN_COMMAND("run_command", true),
        /**
         * Suggests a command into the chat box.
         */
        SUGGEST_COMMAND("suggest_command", true),
        /**
         * Changes the page of a book.
         */
        CHANGE_PAGE("change_page", true);

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
