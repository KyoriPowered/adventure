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

/**
 * A scoreboard score component.
 */
public class ScoreComponent extends BaseComponent {

    /**
     * The score name.
     */
    @Nonnull private final String name;
    /**
     * The score objective.
     */
    @Nonnull private final String objective;
    /**
     * The value.
     */
    @Nullable private final String value;

    public ScoreComponent(@Nonnull final String name, @Nonnull final String objective) {
        this(name, objective, null);
    }

    public ScoreComponent(@Nonnull final String name, @Nonnull final String objective, @Nullable final String value) {
        this.name = name;
        this.objective = objective;
        this.value = value;
    }

    /**
     * Gets the score name.
     *
     * @return the score name
     */
    @Nonnull
    public String name() {
        return this.name;
    }

    /**
     * Gets the objective name.
     *
     * @return the objective name
     */
    @Nonnull
    public String objective() {
        return this.objective;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    @Nullable
    public String value() {
        return this.value;
    }

    @Nonnull
    @Override
    public Component copy() {
        final ScoreComponent that = new ScoreComponent(this.name, this.objective, this.value);
        that.mergeStyle(this);
        for(final Component child : this.children()) {
            that.append(child);
        }
        return that;
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if(this == other) return true;
        if(other == null || !(other instanceof ScoreComponent)) return false;
        if(!super.equals(other)) return false;
        final ScoreComponent that = (ScoreComponent) other;
        return Objects.equals(this.name, that.name) && Objects.equals(this.objective, that.objective) && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.name, this.objective, this.value);
    }

    @Override
    protected void populateToString(@Nonnull final MoreObjects.ToStringHelper builder) {
        builder
            .add("name", this.name)
            .add("objective", this.objective)
            .add("value", this.value);
    }
}
