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
     * Gets the unmodifiable list of children.
     *
     * @return the unmodifiable list of children
     */
    List<Component> children();

    /**
     * Checks if this component contains a component.
     *
     * @param that the other component
     * @return {@code true} if this component contains the provided
     *     component, {@code false} otherwise
     */
    default boolean contains(final Component that) {
        if(this == that) return true;
        for(final Component child : this.children()) {
            if(child.contains(that)) return true;
        }
        if(this.hoverEvent() != null) {
            final Component hover = this.hoverEvent().value();
            if(that == hover) return true;
            for(final Component child : hover.children()) {
                if(child.contains(that)) return true;
            }
        }
        return false;
    }

    /**
     * Prevents a cycle between this component and the provided component.
     *
     * @param that the other component
     */
    default void detectCycle(final Component that) {
        if(that.contains(this)) {
            throw new IllegalStateException("Component cycle detected between " + this + " and " + that);
        }
    }

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
    TextColor color();

    /**
     * Sets the color of this component.
     *
     * @param color the color
     * @return this component
     */
    Component color(@Nullable final TextColor color);

    /**
     * Tests if this component has a decoration.
     *
     * @param decoration the decoration
     * @return {@code true} if this component has the decoration, {@code false} if this
     *     component does not have the decoration
     */
    default boolean hasDecoration(final TextDecoration decoration) {
        return this.decoration(decoration) == TextDecoration.State.TRUE;
    }

    /**
     * Gets the state of a decoration on this component.
     *
     * @param decoration the decoration
     * @return {@link TextDecoration.State#TRUE} if this component has the decoration,
     *     {@link TextDecoration.State#FALSE} if this component does not have the decoration,
     *     and {@link TextDecoration.State#NOT_SET} if not set
     */
    TextDecoration.State decoration(final TextDecoration decoration);

    /**
     * Sets the state of a decoration on this component.
     *
     * @param decoration the decoration
     * @param flag {@code true} if this component should have the decoration, {@code false} if
     *     this component should not have the decoration
     * @return this component
     */
    default Component decoration(final TextDecoration decoration, final boolean flag) {
        return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
    }

    /**
     * Sets the value of a decoration on this component.
     *
     * @param decoration the decoration
     * @param state {@link TextDecoration.State#TRUE} if this component should have the
     *     decoration, {@link TextDecoration.State#FALSE} if this component should not
     *     have the decoration, and {@link TextDecoration.State#NOT_SET} if the decoration
     *     should not have a set value
     * @return this component
     */
    Component decoration(final TextDecoration decoration, final TextDecoration.State state);

    /**
     * Gets a set of decorations this component has.
     *
     * @return a set of decorations this component has
     */
    default Set<TextDecoration> decorations() {
        return this.decorations(Collections.emptySet());
    }

    /**
     * Gets a set of decorations this component has.
     *
     * @param defaultValues a set of default values
     * @return a set of decorations this component has
     */
    default Set<TextDecoration> decorations(final Set<TextDecoration> defaultValues) {
        final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);
        for(final TextDecoration decoration : TextDecoration.values()) {
            final TextDecoration.State value = this.decoration(decoration);
            if(value == TextDecoration.State.TRUE || (value == TextDecoration.State.NOT_SET && defaultValues.contains(decoration))) {
                decorations.add(decoration);
            }
        }
        return decorations;
    }

    /**
     * Gets the click event of this component.
     *
     * @return the click event
     */
    @Nullable
    ClickEvent clickEvent();

    /**
     * Sets the click event of this component.
     *
     * @param event the click event
     * @return this component
     */
    Component clickEvent(@Nullable final ClickEvent event);

    /**
     * Gets the hover event of this component.
     *
     * @return the hover event
     */
    @Nullable
    HoverEvent hoverEvent();

    /**
     * Sets the hover event of this component.
     *
     * @param event the hover event
     * @return this component
     */
    Component hoverEvent(@Nullable final HoverEvent event);

    /**
     * Gets the string to be inserted when this component is shift-clicked.
     *
     * @return the insertion string
     */
    @Nullable
    String insertion();

    /**
     * Sets the string to be inserted when this component is shift-clicked.
     *
     * @param insertion the insertion string
     * @return this component
     */
    Component insertion(@Nullable final String insertion);

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
        if(that.color() != null) this.color(that.color());
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
            final TextDecoration.State state = that.decoration(decoration);
            if(state != TextDecoration.State.NOT_SET) this.decoration(decoration, state);
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
        if(that.clickEvent() != null) this.clickEvent(that.clickEvent());
        if(that.hoverEvent() != null) this.hoverEvent(that.hoverEvent().copy()); // hard copy, hover events have a component
        return this;
    }

    /**
     * Resets all styling on this component.
     *
     * @return this component
     */
    default Component resetStyle() {
        this.color(null);
        for(final TextDecoration decoration : TextDecoration.values()) this.decoration(decoration, TextDecoration.State.NOT_SET);
        this.clickEvent(null);
        this.hoverEvent(null);
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
