package net.kyori.text;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A scoreboard selector component.
 */
public class SelectorComponent extends BaseComponent {

    /**
     * The selector pattern.
     */
    @Nonnull private final String pattern;

    public SelectorComponent(@Nonnull final String pattern) {
        this.pattern = pattern;
    }

    /**
     * Gets the selector pattern.
     *
     * @return the selector pattern
     */
    @Nonnull
    public String pattern() {
        return this.pattern;
    }

    @Nonnull
    @Override
    public Component copy() {
        final SelectorComponent that = new SelectorComponent(this.pattern);
        that.mergeStyle(this);
        for(final Component child : this.children()) {
            that.append(child);
        }
        return that;
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if(this == other) return true;
        if(other == null || !(other instanceof SelectorComponent)) return false;
        if(!super.equals(other)) return false;
        final SelectorComponent that = (SelectorComponent) other;
        return Objects.equals(this.pattern, that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.pattern);
    }

    @Override
    protected void populateToString(@Nonnull final MoreObjects.ToStringHelper builder) {
        builder.add("pattern", this.pattern);
    }
}
