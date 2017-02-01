package net.kyori.text;

import com.google.common.base.Objects;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public abstract class BaseComponent implements Component {

    private List<Component> children = EMPTY_COMPONENT_LIST;
    @Nullable private TextColor color;
    @Nullable private Boolean obfuscated;
    @Nullable private Boolean bold;
    @Nullable private Boolean strikethrough;
    @Nullable private Boolean underlined;
    @Nullable private Boolean italic;
    @Nullable private ClickEvent clickEvent;
    @Nullable private HoverEvent hoverEvent;
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
    public Boolean getDecoration(final TextDecoration decoration) {
        switch(decoration) {
            case BOLD: return this.getBold();
            case ITALIC: return this.getItalic();
            case UNDERLINE: return this.getUnderlined();
            case STRIKETHROUGH: return this.getStrikethrough();
            case OBFUSCATED: return this.getObfuscated();
            default: return null;
        }
    }

    @Override
    public boolean getDecoration(final TextDecoration decoration, final boolean defaultValue) {
        @Nullable final Boolean flag = this.getDecoration(decoration);
        return flag != null ? flag : defaultValue;
    }

    @Override
    public Component setDecoration(final TextDecoration decoration, final Boolean flag) {
        switch(decoration) {
            case BOLD: return this.setBold(flag);
            case ITALIC: return this.setItalic(flag);
            case UNDERLINE: return this.setUnderlined(flag);
            case STRIKETHROUGH: return this.setStrikethrough(flag);
            case OBFUSCATED: return this.setObfuscated(flag);
            default: return this;
        }
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
    public Component mergeStyle(final Component that) {
        this.mergeColor(that);
        this.mergeDecorations(that);
        this.mergeEvents(that);
        return this;
    }

    @Override
    public Component mergeColor(final Component that) {
        if(that.getColor() != null) this.setColor(that.getColor());
        return this;
    }

    @Override
    public Component mergeDecorations(final Component that) {
        for(final TextDecoration decoration : TextDecoration.values()) {
            @Nullable final Boolean flag = that.getDecoration(decoration);
            if(flag != null) this.setDecoration(decoration, flag);
        }
        return this;
    }

    @Override
    public Component mergeEvents(final Component that) {
        if(that.getClickEvent() != null) this.setClickEvent(that.getClickEvent());
        if(that.getHoverEvent() != null) this.setHoverEvent(new HoverEvent(that.getHoverEvent().getAction(), that.getHoverEvent().getValue().copy()));
        return this;
    }

    @Override
    public boolean hasStyling() {
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
        final BaseComponent that = (BaseComponent) other;
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
