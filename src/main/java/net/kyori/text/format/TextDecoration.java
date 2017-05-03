package net.kyori.text.format;

import com.google.common.base.Enums;
import com.google.gson.annotations.SerializedName;
import net.kyori.text.Component;

import javax.annotation.Nonnull;

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

    /**
     * The serialized name of this decoration.
     */
    @Nonnull private final String toString = Enums.getField(this).getAnnotation(SerializedName.class).value();

    @Nonnull
    @Override
    public String toString() {
        return this.toString;
    }

    /**
     * A state that a {@link TextDecoration} can be in.
     */
    public enum State {
        NOT_SET,
        FALSE,
        TRUE;

        /**
         * Gets a state from a {@code boolean}.
         *
         * @param flag the boolean
         * @return the state
         */
        public static State byBoolean(final boolean flag) {
            return flag ? TRUE : FALSE;
        }

        /**
         * Gets a state from a {@code Boolean}.
         *
         * @param flag the boolean
         * @return the state
         */
        public static State byBoolean(final Boolean flag) {
            return flag == null ? NOT_SET : byBoolean(flag.booleanValue());
        }
    }
}
