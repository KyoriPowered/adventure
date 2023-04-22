package net.kyori.adventure.text.serializer.legacy;

import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CharacterAndFormat {
  private final char character;
  private final @Nullable TextFormat format;

  private CharacterAndFormat(final char character) {
    this.character = character;
    this.format = null;
  }

  public CharacterAndFormat(final char character, final @NotNull TextFormat format) {
    this.character = character;
    this.format = format;
  }

  public char getCharacter() {
    return character;
  }

  @Nullable
  public TextFormat getFormat() {
    return format;
  }

  public static CharacterAndFormat ofReset(char character) {
    return new CharacterAndFormat(character);
  }
}
