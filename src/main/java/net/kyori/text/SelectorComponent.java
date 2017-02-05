package net.kyori.text;

import com.google.common.base.Objects;

/**
 * A scoreboard selector component.
 */
public class SelectorComponent extends BaseComponent {

    /**
     * The selector pattern.
     */
    private final String pattern;

    public SelectorComponent(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * Gets the selector pattern.
     *
     * @return the selector pattern
     */
    public String getPattern() {
        return this.pattern;
    }

    @Override
    public Component copy() {
        final SelectorComponent that = new SelectorComponent(this.pattern);
        that.mergeStyle(this);
        for(final Component child : this.getChildren()) {
            that.append(child);
        }
        return that;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null || !(other instanceof SelectorComponent)) return false;
        if(!super.equals(other)) return false;
        final SelectorComponent that = (SelectorComponent) other;
        return Objects.equal(this.pattern, that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), this.pattern);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("pattern", this.pattern)
            .toString();
    }
}
