package net.kyori.text;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A plain text component.
 */
public class TextComponent extends BaseComponent {

    /**
     * The plain text content.
     */
    @Nonnull private final String content;

    public TextComponent(@Nonnull final String content) {
        this.content = content;
    }

    /**
     * Gets the plain text content.
     *
     * @return the plain text content
     */
    @Nonnull
    public String content() {
        return this.content;
    }

    @Nonnull
    @Override
    public Component copy() {
        final TextComponent that = new TextComponent(this.content);
        that.mergeStyle(this);
        for(final Component child : this.children()) {
            that.append(child);
        }
        return that;
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if(this == other) return true;
        if(other == null || !(other instanceof TextComponent)) return false;
        if(!super.equals(other)) return false;
        final TextComponent component = (TextComponent) other;
        return Objects.equal(this.content, component.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), this.content);
    }

    @Override
    protected void populateToString(@Nonnull final Objects.ToStringHelper builder) {
        builder.add("content", this.content);
    }
}
