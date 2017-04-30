package net.kyori.text;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KeybindComponent extends BaseComponent {

    /**
     * The keybind.
     */
    @Nonnull private final String keybind;

    public KeybindComponent(@Nonnull final String keybind) {
        this.keybind = keybind;
    }

    /**
     * Gets the keybind.
     *
     * @return the keybind
     */
    @Nonnull
    public String keybind() {
        return this.keybind;
    }

    @Nonnull
    @Override
    public Component copy() {
        final KeybindComponent that = new KeybindComponent(this.keybind);
        that.mergeStyle(this);
        for(final Component child : this.children()) {
            that.append(child);
        }
        return that;
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if(this == other) return true;
        if(other == null || !(other instanceof KeybindComponent)) return false;
        if(!super.equals(other)) return false;
        final KeybindComponent component = (KeybindComponent) other;
        return Objects.equals(this.keybind, component.keybind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.keybind);
    }

    @Override
    protected void populateToString(@Nonnull final MoreObjects.ToStringHelper builder) {
        builder.add("keybind", this.keybind);
    }
}
