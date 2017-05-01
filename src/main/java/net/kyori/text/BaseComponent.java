package net.kyori.text;

import com.google.common.base.Objects;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

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
    private TextDecoration.State obfuscated = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#BOLD bold} decoration.
     */
    private TextDecoration.State bold = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#STRIKETHROUGH strikethrough} decoration.
     */
    private TextDecoration.State strikethrough = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#UNDERLINE underlined} decoration.
     */
    private TextDecoration.State underlined = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#ITALIC italic} decoration.
     */
    private TextDecoration.State italic = TextDecoration.State.NOT_SET;
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
    public List<Component> children() {
        return Collections.unmodifiableList(this.children);
    }

    @Override
    public Component append(final Component component) {
        this.detectCycle(component); // detect cycle before modifying
        if(this.children == EMPTY_COMPONENT_LIST) this.children = new ArrayList<>();
        this.children.add(component);
        return this;
    }

    @Nullable
    @Override
    public TextColor color() {
        return this.color;
    }

    @Override
    public Component color(@Nullable final TextColor color) {
        this.color = color;
        return this;
    }

    @Override
    public TextDecoration.State decoration(final TextDecoration decoration) {
        switch(decoration) {
            case BOLD: return this.bold;
            case ITALIC: return this.italic;
            case UNDERLINE: return this.underlined;
            case STRIKETHROUGH: return this.strikethrough;
            case OBFUSCATED: return this.obfuscated;
            default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
        }
    }

    @Override
    public Component decoration(final TextDecoration decoration, final TextDecoration.State state) {
        switch(decoration) {
            case BOLD: this.bold = checkNotNull(state, "flag"); return this;
            case ITALIC: this.italic = checkNotNull(state, "flag"); return this;
            case UNDERLINE: this.underlined = checkNotNull(state, "flag"); return this;
            case STRIKETHROUGH: this.strikethrough = checkNotNull(state, "flag"); return this;
            case OBFUSCATED: this.obfuscated = checkNotNull(state, "flag"); return this;
            default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
        }
    }

    @Nullable
    @Override
    public ClickEvent clickEvent() {
        return this.clickEvent;
    }

    @Override
    public Component clickEvent(@Nullable final ClickEvent event) {
        this.clickEvent = event;
        return this;
    }

    @Nullable
    @Override
    public HoverEvent hoverEvent() {
        return this.hoverEvent;
    }

    @Override
    public Component hoverEvent(@Nullable final HoverEvent event) {
        if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
        this.hoverEvent = event;
        return this;
    }

    @Nullable
    @Override
    public String insertion() {
        return this.insertion;
    }

    @Override
    public Component insertion(@Nullable final String insertion) {
        this.insertion = insertion;
        return this;
    }

    @Override
    public boolean hasStyling() {
        // A component has styling when any of these fields are set.
        return this.color != null
            || this.obfuscated != TextDecoration.State.NOT_SET
            || this.bold != TextDecoration.State.NOT_SET
            || this.strikethrough != TextDecoration.State.NOT_SET
            || this.underlined != TextDecoration.State.NOT_SET
            || this.italic != TextDecoration.State.NOT_SET
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
        final Objects.ToStringHelper builder = Objects.toStringHelper(this);
        this.populateToString(builder);
        builder
            .add("children", this.children)
            .add("color", this.color)
            .add("obfuscated", this.obfuscated)
            .add("bold", this.bold)
            .add("strikethrough", this.strikethrough)
            .add("underlined", this.underlined)
            .add("italic", this.italic)
            .add("clickEvent", this.clickEvent)
            .add("hoverEvent", this.hoverEvent)
            .add("insertion", this.insertion);
        return builder.toString();
    }

    protected void populateToString(final Objects.ToStringHelper builder) {
    }
}
