package net.kyori.text;

import com.google.common.base.Objects;

public class TextComponent extends BaseComponent {

    private final String content;

    public TextComponent(final String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public Component copy() {
        final TextComponent that = new TextComponent(this.content);
        that.mergeStyle(this);
        for(final Component child : this.getChildren()) {
            that.append(child);
        }
        return that;
    }

    @Override
    public boolean equals(final Object other) {
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
    public String toString() {
        return Objects.toStringHelper(this)
            .add("content", this.content)
            .toString();
    }
}
