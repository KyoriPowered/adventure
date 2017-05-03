package net.kyori.text;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A scoreboard score component.
 */
public class ScoreComponent extends BaseComponent {

    /**
     * The score name.
     */
    @Nonnull private final String name;
    /**
     * The score objective.
     */
    @Nonnull private final String objective;
    /**
     * The value.
     */
    @Nullable private final String value;

    public ScoreComponent(@Nonnull final String name, @Nonnull final String objective) {
        this(name, objective, null);
    }

    public ScoreComponent(@Nonnull final String name, @Nonnull final String objective, @Nullable final String value) {
        this.name = name;
        this.objective = objective;
        this.value = value;
    }

    /**
     * Gets the score name.
     *
     * @return the score name
     */
    @Nonnull
    public String name() {
        return this.name;
    }

    /**
     * Gets the objective name.
     *
     * @return the objective name
     */
    @Nonnull
    public String objective() {
        return this.objective;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    @Nullable
    public String value() {
        return this.value;
    }

    @Nonnull
    @Override
    public Component copy() {
        final ScoreComponent that = new ScoreComponent(this.name, this.objective, this.value);
        that.mergeStyle(this);
        for(final Component child : this.children()) {
            that.append(child);
        }
        return that;
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if(this == other) return true;
        if(other == null || !(other instanceof ScoreComponent)) return false;
        if(!super.equals(other)) return false;
        final ScoreComponent that = (ScoreComponent) other;
        return Objects.equal(this.name, that.name) && Objects.equal(this.objective, that.objective) && Objects.equal(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), this.name, this.objective, this.value);
    }

    @Override
    protected void populateToString(@Nonnull final Objects.ToStringHelper builder) {
        builder
            .add("name", this.name)
            .add("objective", this.objective)
            .add("value", this.value);
    }
}
