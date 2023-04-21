package net.kyori.adventure.text.serializer.legacy;

import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;

/**
 * A pair containing a text format and its legacy character equivalent.
 * @since 4.14.0
 */
public final class CharacterAndFormat {
  private final char character;
  private final @NotNull TextFormat format;

  public CharacterAndFormat(final char character, final @NotNull TextFormat format) {
    this.character = character;
    this.format = format;
  }

  public char getCharacter() {
    return character;
  }

  @NotNull
  public TextFormat getFormat() {
    return format;
  }
}
