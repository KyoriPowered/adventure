package net.kyori.text.event;

import com.google.common.base.Enums;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nonnull;
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
    @Nonnull private final Action action;
    /**
     * The click event value.
     */
    @Nonnull private final String value;

    public ClickEvent(@Nonnull final Action action, @Nonnull final String value) {
        this.action = action;
        this.value = value;
    }

    /**
     * Gets the click event action.
     *
     * @return the click event action
     */
    @Nonnull
    public Action action() {
        return this.action;
    }

    /**
     * Gets the click event value.
     *
     * @return the click event value
     */
    @Nonnull
    public String value() {
        return this.value;
    }

    /**
     * Create a copy of this click event.
     *
     * @return a copy of this click event
     */
    @Nonnull
    public ClickEvent copy() {
        return new ClickEvent(this.action, this.value);
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

    @Nonnull
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
        @SerializedName("open_url")
        OPEN_URL(true),
        /**
         * Opens a file when clicked.
         *
         * <p>This action is not readable, and may only be used locally on the client.</p>
         */
        @SerializedName("open_file")
        OPEN_FILE(false),
        /**
         * Runs a command when clicked.
         */
        @SerializedName("run_command")
        RUN_COMMAND(true),
        /**
         * Suggests a command into the chat box.
         */
        @SerializedName("suggest_command")
        SUGGEST_COMMAND(true),
        /**
         * Changes the page of a book.
         */
        @SerializedName("change_page")
        CHANGE_PAGE(true);

        /**
         * The serialized name of this action.
         */
        @Nonnull private final String toString = Enums.getField(this).getAnnotation(SerializedName.class).value();
        /**
         * If this action is readable.
         *
         * <p>When an action is not readable it will not be deserailized.</p>
         */
        private final boolean readable;

        Action(final boolean readable) {
            this.readable = readable;
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

        @Nonnull
        @Override
        public String toString() {
            return this.toString;
        }
    }
}
