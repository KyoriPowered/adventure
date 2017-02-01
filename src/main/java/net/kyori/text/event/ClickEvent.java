package net.kyori.text.event;

import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ClickEvent {

    private final Action action;
    private final String value;

    public ClickEvent(final Action action, final String value) {
        this.action = action;
        this.value = value;
    }

    public Action getAction() {
        return this.action;
    }

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

    public enum Action {
        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true);

        private static final Map<String, Action> BY_ID = new HashMap<>();
        private final String id;
        private final boolean readable;

        Action(final String id, final boolean readable) {
            this.id = id;
            this.readable = readable;
        }

        public String getId() {
            return this.id;
        }

        public boolean isReadable() {
            return this.readable;
        }

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
