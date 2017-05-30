/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
