package net.kyori.adventure.skin;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A skin.
 */
public interface Skin {

    /**
     * Creates an empty skin.
     *
     * @return an empty skin
     */
    static Skin empty() {
        return SkinImpl.EMPTY;
    }

    /**
     * Creates a skin.
     *
     * @param data the data, or null for empty
     * @param signature the signature, or null for unsigned
     * @return a skin
     */
    static Skin of(@Nullable String data, @Nullable String signature) {
        return new SkinImpl(data, signature);
    }

    // TODO: consider fetching Skin from sessionserver URL

    /**
     * Gets the data.
     *
     * @return the data, null if empty
     */
    @Nullable String data();

    /**
     * Gets the signature.
     *
     * @return the signature, null if unsigned
     */
    @Nullable String signature();
}
