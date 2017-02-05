package net.kyori.text.format;

import net.kyori.text.Component;

/**
 * An enumeration of decorations which may be applied to a {@link Component}.
 */
public enum TextDecoration {
    /**
     * A decoration which makes text obfuscated/unreadable.
     */
    OBFUSCATED,
    /**
     * A decoration which makes text appear bold.
     */
    BOLD,
    /**
     * A decoration which makes text have a strike through it.
     */
    STRIKETHROUGH,
    /**
     * A decoration which makes text have an underline.
     */
    UNDERLINE,
    /**
     * A decoration which makes text appear in italics.
     */
    ITALIC;
}
