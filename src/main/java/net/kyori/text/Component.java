package net.kyori.text;

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

public interface Component {

    List<Component> EMPTY_COMPONENT_LIST = Collections.emptyList();

    List<Component> getChildren();

    Component append(final Component component);

    Component copy();

    @Nullable
    TextColor getColor();

    Component setColor(@Nullable final TextColor color);

    boolean isBold();

    @Nullable
    Boolean getBold();

    Component setBold(@Nullable final Boolean bold);

    boolean isItalic();

    @Nullable
    Boolean getItalic();

    Component setItalic(@Nullable final Boolean italic);

    boolean isUnderlined();

    @Nullable
    Boolean getUnderlined();

    Component setUnderlined(@Nullable final Boolean underlined);

    boolean isStrikethrough();

    @Nullable
    Boolean getStrikethrough();

    Component setStrikethrough(@Nullable final Boolean strikethrough);

    boolean isObfuscated();

    @Nullable
    Boolean getObfuscated();

    Component setObfuscated(@Nullable final Boolean obfuscated);

    @Nullable
    Boolean getDecoration(final TextDecoration decoration);

    boolean getDecoration(final TextDecoration decoration, final boolean defaultValue);

    Component setDecoration(final TextDecoration decoration, final Boolean flag);

    @Nullable
    ClickEvent getClickEvent();

    Component setClickEvent(@Nullable final ClickEvent event);

    @Nullable
    HoverEvent getHoverEvent();

    Component setHoverEvent(@Nullable final HoverEvent event);


    @Nullable
    String getInsertion();

    Component setInsertion(@Nullable final String insertion);

    Component mergeStyle(final Component that);

    Component mergeColor(final Component that);

    Component mergeDecorations(final Component that);

    Component mergeEvents(final Component that);

    boolean hasStyling();
}
