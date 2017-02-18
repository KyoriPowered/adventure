package net.kyori.text;

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * A text component.
 */
public interface Component {

    /**
     * An empty, unmodifiable, list of components.
     */
    List<Component> EMPTY_COMPONENT_LIST = Collections.emptyList();

    /**
     * Gets the modifiable list of children.
     *
     * @return the list of children
     */
    List<Component> getChildren();

    /**
     * Append a component to this component.
     *
     * @param component the component to append
     * @return this component
     */
    Component append(final Component component);

    /**
     * Create a copy of this component.
     *
     * @return a copy of this component
     */
    Component copy();

    /**
     * Gets the color of this component.
     *
     * @return the color of this component
     */
    @Nullable
    TextColor getColor();

    /**
     * Sets the color of this component.
     *
     * @param color the color
     * @return this component
     */
    Component setColor(@Nullable final TextColor color);

    /**
     * Tests if this component is bold.
     *
     * @return {@code true} if this component is bold, {@code false} otherwise
     * @see TextDecoration#BOLD
     */
    boolean isBold();

    /**
     * Gets if this component is bold.
     *
     * @return {@code true} if this component is bold, {@code false} if not bold, and
     *     {@code null} if not set
     * @see TextDecoration#BOLD
     */
    @Nullable
    Boolean getBold();

    /**
     * Sets if this component should have a bold decoration.
     *
     * @param bold if this component should have a bold decoration
     * @return this component
     * @see TextDecoration#BOLD
     */
    Component setBold(@Nullable final Boolean bold);

    /**
     * Tests if this component is italic.
     *
     * @return {@code true} if this component is italic, {@code false} otherwise
     * @see TextDecoration#ITALIC
     */
    boolean isItalic();

    /**
     * Gets if this component is italic.
     *
     * @return {@code true} if this component is italic, {@code false} if not italic, and
     *     {@code null} if not set
     * @see TextDecoration#ITALIC
     */
    @Nullable
    Boolean getItalic();

    /**
     * Sets if this component should have a italic decoration.
     *
     * @param italic if this component should have a italic decoration
     * @return this component
     * @see TextDecoration#ITALIC
     */
    Component setItalic(@Nullable final Boolean italic);

    /**
     * Tests if this component is underlined.
     *
     * @return {@code true} if this component is underlined, {@code false} if not
     *     underlined, and {@code null} if not set
     * @see TextDecoration#UNDERLINE
     */
    boolean isUnderlined();

    /**
     * Gets if this component is underlined.
     *
     * @return {@code true} if this component is underlined, {@code false} if not
     *     underlined, and {@code null} if not set
     * @see TextDecoration#UNDERLINE
     */
    @Nullable
    Boolean getUnderlined();

    /**
     * Sets if this component should have an underline decoration.
     *
     * @param underlined if this component should have an underline decoration
     * @return this component
     * @see TextDecoration#UNDERLINE
     */
    Component setUnderlined(@Nullable final Boolean underlined);

    /**
     * Tests if this component is underlined.
     *
     * @return {@code true} if this component is underlined, {@code false} if not
     *     underlined, and {@code null} if not set
     * @see TextDecoration#STRIKETHROUGH
     */
    boolean isStrikethrough();

    /**
     * Gets if this component is strikethrough.
     *
     * @return {@code true} if this component is strikethrough, {@code false} if not strikethrough, and
     *     {@code null} if not set
     * @see TextDecoration#STRIKETHROUGH
     */
    @Nullable
    Boolean getStrikethrough();

    /**
     * Sets if this component should have a strikethrough decoration.
     *
     * @param strikethrough if this component should have a strikethrough decoration
     * @return this component
     * @see TextDecoration#STRIKETHROUGH
     */
    Component setStrikethrough(@Nullable final Boolean strikethrough);

    /**
     * Tests if this component is obfuscated.
     *
     * @return {@code true} if this component is obfuscated, {@code false} if not
     *     obfuscated, and {@code null} if not set
     * @see TextDecoration#OBFUSCATED
     */
    boolean isObfuscated();

    /**
     * Gets if this component is obfuscated.
     *
     * @return {@code true} if this component is obfuscated, {@code false} if not
     *     obfuscated, and {@code null} if not set
     * @see TextDecoration#OBFUSCATED
     */
    @Nullable
    Boolean getObfuscated();

    /**
     * Sets if this component should have an obfuscated decoration.
     *
     * @param obfuscated if this component should have an obfuscated decoration
     * @return this component
     * @see TextDecoration#OBFUSCATED
     */
    Component setObfuscated(@Nullable final Boolean obfuscated);

    /**
     * Tests if this component has a decoration.
     *
     * @param decoration the decoration
     * @return {@code true} if this component has the decoration, {@code false} if this
     *     component does not have the decoration
     */
    default boolean hasDecoration(final TextDecoration decoration) {
        return this.hasDecoration(decoration, false);
    }

    /**
     * Tests if this component has a decoration.
     *
     * @param decoration the decoration
     * @param defaultValue the default value when this component does not have the decoration set
     * @return {@code true} if this component has the decoration, {@code false} if this
     *     component does not have the decoration
     */
    default boolean hasDecoration(final TextDecoration decoration, final boolean defaultValue) {
        @Nullable final Boolean flag = this.getDecoration(decoration);
        return flag != null ? flag : defaultValue;
    }

    /**
     * Gets the value of a decoration on this component.
     *
     * @param decoration the decoration
     * @param defaultValue the default value when the decoration has not been set
     * @return {@code true} if this component has the decoration, {@code false} if this
     *     component does not have the decoration, and {@code null} if not set
     */
    @Nullable
    default Boolean getDecoration(final TextDecoration decoration, final boolean defaultValue) {
        @Nullable final Boolean value = this.getDecoration(decoration);
        return value != null ? value : defaultValue;
    }

    /**
     * Gets the value of a decoration on this component.
     *
     * @param decoration the decoration
     * @return {@code true} if this component has the decoration, {@code false} if this
     *     component does not have the decoration, and {@code null} if not set
     */
    @Nullable
    default Boolean getDecoration(final TextDecoration decoration) {
        switch(decoration) {
            case BOLD: return this.getBold();
            case ITALIC: return this.getItalic();
            case UNDERLINE: return this.getUnderlined();
            case STRIKETHROUGH: return this.getStrikethrough();
            case OBFUSCATED: return this.getObfuscated();
            default: return null;
        }
    }

    /**
     * Sets the value of a decoration on this component.
     *
     * @param decoration the decoration
     * @param flag {@code true} if this component should have the decoration, {@code false} if
     *     this component should not have the decoration, and {@code null} if the decoration
     *     should not have a set value
     * @return this component
     */
    default Component setDecoration(final TextDecoration decoration, final Boolean flag) {
        switch(decoration) {
            case BOLD: return this.setBold(flag);
            case ITALIC: return this.setItalic(flag);
            case UNDERLINE: return this.setUnderlined(flag);
            case STRIKETHROUGH: return this.setStrikethrough(flag);
            case OBFUSCATED: return this.setObfuscated(flag);
            default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
        }
    }

    /**
     * Gets a set of decorations this component has.
     *
     * @return a set of decorations this component has
     */
    default Set<TextDecoration> getDecorations() {
        return this.getDecorations(Collections.emptySet());
    }

    /**
     * Gets a set of decorations this component has.
     *
     * @param defaultValues a set of default values
     * @return a set of decorations this component has
     */
    default Set<TextDecoration> getDecorations(final Set<TextDecoration> defaultValues) {
        final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);
        for(final TextDecoration decoration : TextDecoration.values()) {
            @Nullable final Boolean value = this.getDecoration(decoration, defaultValues.contains(decoration));
            if(value != null && value) decorations.add(decoration);
        }
        return decorations;
    }

    /**
     * Gets the click event of this component.
     *
     * @return the click event
     */
    @Nullable
    ClickEvent getClickEvent();

    /**
     * Sets the click event of this component.
     *
     * @param event the click event
     * @return this component
     */
    Component setClickEvent(@Nullable final ClickEvent event);

    /**
     * Gets the hover event of this component.
     *
     * @return the hover event
     */
    @Nullable
    HoverEvent getHoverEvent();

    /**
     * Sets the hover event of this component.
     *
     * @param event the hover event
     * @return this component
     */
    Component setHoverEvent(@Nullable final HoverEvent event);

    /**
     * Gets the string to be inserted when this component is shift-clicked.
     *
     * @return the insertion string
     */
    @Nullable
    String getInsertion();

    /**
     * Sets the string to be inserted when this component is shift-clicked.
     *
     * @param insertion the insertion string
     * @return this component
     */
    Component setInsertion(@Nullable final String insertion);

    /**
     * Merges styling from another component into this component.
     *
     * @param that the other component
     * @return this component
     */
    default Component mergeStyle(final Component that) {
        this.mergeColor(that);
        this.mergeDecorations(that);
        this.mergeEvents(that);
        return this;
    }

    /**
     * Merges the color from another component into this component.
     *
     * @param that the other component
     * @return this component
     */
    default Component mergeColor(final Component that) {
        if(that.getColor() != null) this.setColor(that.getColor());
        return this;
    }

    /**
     * Merges the decorations from another component into this component.
     *
     * @param that the other component
     * @return this component
     */
    default Component mergeDecorations(final Component that) {
        for(final TextDecoration decoration : TextDecoration.values()) {
            @Nullable final Boolean flag = that.getDecoration(decoration);
            if(flag != null) this.setDecoration(decoration, flag);
        }
        return this;
    }

    /**
     * Merges the events from another component into this component.
     *
     * @param that the other component
     * @return this component
     */
    default Component mergeEvents(final Component that) {
        if(that.getClickEvent() != null) this.setClickEvent(that.getClickEvent());
        if(that.getHoverEvent() != null) this.setHoverEvent(that.getHoverEvent().copy()); // hard copy, hover events have a component
        return this;
    }

    /**
     * Resets all styling on this component.
     *
     * @return this component
     */
    default Component resetStyle() {
        this.setColor(null);
        for(final TextDecoration decoration : TextDecoration.values()) this.setDecoration(decoration, null);
        this.setClickEvent(null);
        this.setHoverEvent(null);
        return this;
    }

    /**
     * Tests if this component has any styling.
     *
     * @return {@code true} if this component has any styling, {@code false} if this
     *     component does not have any styling
     */
    boolean hasStyling();
}
