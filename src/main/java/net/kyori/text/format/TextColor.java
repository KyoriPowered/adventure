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
package net.kyori.text.format;

import com.google.common.base.Enums;
import com.google.gson.annotations.SerializedName;
import net.kyori.text.Component;

import javax.annotation.Nonnull;

/**
 * An enumeration of colors which may be applied to a {@link Component}.
 */
public enum TextColor {
    @SerializedName("black")
    BLACK,
    @SerializedName("dark_blue")
    DARK_BLUE,
    @SerializedName("dark_green")
    DARK_GREEN,
    @SerializedName("dark_aqua")
    DARK_AQUA,
    @SerializedName("dark_red")
    DARK_RED,
    @SerializedName("dark_purple")
    DARK_PURPLE,
    @SerializedName("gold")
    GOLD,
    @SerializedName("gray")
    GRAY,
    @SerializedName("dark_gray")
    DARK_GRAY,
    @SerializedName("blue")
    BLUE,
    @SerializedName("green")
    GREEN,
    @SerializedName("aqua")
    AQUA,
    @SerializedName("red")
    RED,
    @SerializedName("light_purple")
    LIGHT_PURPLE,
    @SerializedName("yellow")
    YELLOW,
    @SerializedName("white")
    WHITE;

    /**
     * The serialized name of this color.
     */
    @Nonnull private final String toString = Enums.getField(this).getAnnotation(SerializedName.class).value();

    @Nonnull
    @Override
    public String toString() {
        return this.toString;
    }
}
