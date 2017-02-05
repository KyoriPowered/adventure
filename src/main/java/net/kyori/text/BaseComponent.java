package net.kyori.text;

import com.google.common.base.Objects;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * An abstract implementation of a text component.
 */
public abstract class BaseComponent implements Component {

    /**
     * The list of children.
     *
     * <p>This list is set to {@link #EMPTY_COMPONENT_LIST an empty list of components}
     * by default to prevent unnecessary list creation for components with no children.</p>
     */
    private List<Component> children = EMPTY_COMPONENT_LIST;
    /**
     * The color of this component.
     */
    @Nullable private TextColor color;
    /**
     * If this component should have the {@link TextDecoration#OBFUSCATED obfuscated} decoration.
     */
    @Nullable private Boolean obfuscated;
    /**
     * If this component should have the {@link TextDecoration#BOLD bold} decoration.
     */
    @Nullable private Boolean bold;
    /**
     * If this component should have the {@link TextDecoration#STRIKETHROUGH strikethrough} decoration.
     */
    @Nullable private Boolean strikethrough;
    /**
     * If this component should have the {@link TextDecoration#UNDERLINE underlined} decoration.
     */
    @Nullable private Boolean underlined;
    /**
     * If this component should have the {@link TextDecoration#ITALIC italic} decoration.
     */
    @Nullable private Boolean italic;
    /**
     * The click event to apply to this component.
     */
    @Nullable private ClickEvent clickEvent;
    /**
     * The hover event to apply to this component.
     */
    @Nullable private HoverEvent hoverEvent;
    /**
     * The string to insert when this component is shift-clicked in chat.
     */
    @Nullable private String insertion;

    @Override
    public List<Component> getChildren() {
        return this.children;
    }

    @Override
    public Component append(final Component component) {
        if(this.children == EMPTY_COMPONENT_LIST) this.children = new ArrayList<>();
        this.children.add(component);
        return this;
    }

    @Nullable
    @Override
    public TextColor getColor() {
        return this.color;
    }

    @Override
    public Component setColor(@Nullable final TextColor color) {
        this.color = color;
        return this;
    }

    @Override
    public boolean isBold() {
        return this.bold != null && this.bold;
    }

    @Nullable
    @Override
    public Boolean getBold() {
        return this.bold;
    }

    @Override
    public Component setBold(@Nullable final Boolean bold) {
        this.bold = bold;
        return this;
    }

    @Override
    public boolean isItalic() {
        return this.italic != null && this.italic;
    }

    @Nullable
    @Override
    public Boolean getItalic() {
        return this.italic;
    }

    @Override
    public Component setItalic(@Nullable final Boolean italic) {
        this.italic = italic;
        return this;
    }

    @Override
    public boolean isUnderlined() {
        return this.underlined != null && this.underlined;
    }

    @Nullable
    @Override
    public Boolean getUnderlined() {
        return this.underlined;
    }

    @Override
    public Component setUnderlined(@Nullable final Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    @Override
    public boolean isStrikethrough() {
        return this.strikethrough != null && this.strikethrough;
    }

    @Nullable
    @Override
    public Boolean getStrikethrough() {
        return this.strikethrough;
    }

    @Override
    public Component setStrikethrough(@Nullable final Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    @Override
    public boolean isObfuscated() {
        return this.obfuscated != null && this.obfuscated;
    }

    @Nullable
    @Override
    public Boolean getObfuscated() {
        return this.obfuscated;
    }

    @Override
    public Component setObfuscated(@Nullable final Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    @Nullable
    @Override
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }

    @Override
    public Component setClickEvent(@Nullable final ClickEvent event) {
        this.clickEvent = event;
        return this;
    }

    @Nullable
    @Override
    public HoverEvent getHoverEvent() {
        return this.hoverEvent;
    }

    @Override
    public Component setHoverEvent(@Nullable final HoverEvent event) {
        this.hoverEvent = event;
        return this;
    }

    @Nullable
    @Override
    public String getInsertion() {
        return this.insertion;
    }

    @Override
    public Component setInsertion(@Nullable final String insertion) {
        this.insertion = insertion;
        return this;
    }

    @Override
    public boolean hasStyling() {
        // A component has styling when any of these fields are set.
        return this.color != null
            || this.bold != null
            || this.strikethrough != null
            || this.underlined != null
            || this.italic != null
            || this.clickEvent != null
            || this.hoverEvent != null
            || this.insertion != null;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(other == null || !(other instanceof BaseComponent)) return false;
        return this.equals((BaseComponent) other);
    }

    protected boolean equals(final BaseComponent that) {
        return Objects.equal(this.children, that.children)
            && this.color == that.color
            && Objects.equal(this.obfuscated, that.obfuscated)
            && Objects.equal(this.bold, that.bold)
            && Objects.equal(this.strikethrough, that.strikethrough)
            && Objects.equal(this.underlined, that.underlined)
            && Objects.equal(this.italic, that.italic)
            && Objects.equal(this.clickEvent, that.clickEvent)
            && Objects.equal(this.hoverEvent, that.hoverEvent)
            && Objects.equal(this.insertion, that.insertion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("children", this.children)
            .add("color", this.color)
            .add("obfuscated", this.obfuscated)
            .add("bold", this.bold)
            .add("strikethrough", this.strikethrough)
            .add("underlined", this.underlined)
            .add("italic", this.italic)
            .add("clickEvent", this.clickEvent)
            .add("hoverEvent", this.hoverEvent)
            .add("insertion", this.insertion)
            .toString();
    }
}
