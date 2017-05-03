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
