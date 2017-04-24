package net.kyori.text.format;

import com.google.common.base.Enums;
import com.google.gson.annotations.SerializedName;
import net.kyori.text.Component;

/**
 * An enumeration of decorations which may be applied to a {@link Component}.
 */
public enum TextDecoration {
    /**
     * A decoration which makes text obfuscated/unreadable.
     */
    @SerializedName("obfuscated")
    OBFUSCATED,
    /**
     * A decoration which makes text appear bold.
     */
    @SerializedName("bold")
    BOLD,
    /**
     * A decoration which makes text have a strike through it.
     */
    @SerializedName("strikethrough")
    STRIKETHROUGH,
    /**
     * A decoration which makes text have an underline.
     */
    @SerializedName("underline")
    UNDERLINE,
    /**
     * A decoration which makes text appear in italics.
     */
    @SerializedName("italic")
    ITALIC;

    private final String toString = Enums.getField(this).getAnnotation(SerializedName.class).value();

    @Override
    public String toString() {
        return this.toString;
    }
}
