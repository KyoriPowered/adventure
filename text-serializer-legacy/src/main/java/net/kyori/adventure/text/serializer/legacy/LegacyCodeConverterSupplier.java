package net.kyori.adventure.text.serializer.legacy;

import org.jetbrains.annotations.NotNull;

/**
 * @since 4.14.0
 */
@FunctionalInterface
public interface LegacyCodeConverterSupplier {
  /**
   * @since 4.14.0
   */
  @NotNull LegacyCodeConverter create(final char character, final char hexCharacter, final boolean hexColours, final boolean useTerriblyStupidHexFormat);
}
