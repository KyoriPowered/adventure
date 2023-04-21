package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LegacyCodeConverter {
  protected static final char LEGACY_BUNGEE_HEX_CHAR = 'x';

  protected final List<TextColor> colors;
  protected final List<TextFormat> formats;
  protected final String legacyChars;

  protected final char character;
  protected final char hexCharacter;
  protected final boolean hexColours;
  protected final boolean useTerriblyStupidHexFormat; // (╯°□°)╯︵ ┻━┻

  protected LegacyCodeConverter(final List<CharacterAndFormat> characterFormats, final char character, final char hexCharacter, final boolean hexColours, final boolean useTerriblyStupidHexFormat) {
    final List<TextColor> colors = new ArrayList<>();
    final List<TextFormat> formats = new ArrayList<>(characterFormats.size());
    final StringBuilder legacyChars = new StringBuilder(characterFormats.size());
    for (int i = 0; i < characterFormats.size(); i++) {
      CharacterAndFormat characterAndFormat = characterFormats.get(i);
      legacyChars.append(characterAndFormat.getCharacter());
      final TextFormat format = characterAndFormat.getFormat();
      formats.add(format);
      if (format instanceof TextColor) {
        colors.add((TextColor) format);
      }
    }
    this.colors = Collections.unmodifiableList(colors);
    this.formats = Collections.unmodifiableList(formats);
    this.legacyChars = legacyChars.toString();

    this.character = character;
    this.hexCharacter = hexCharacter;
    this.hexColours = hexColours;
    this.useTerriblyStupidHexFormat = useTerriblyStupidHexFormat;
  }

  @NotNull abstract LegacyCodeConverterSupplier createSupplier();

  /**
   * Find the named colour nearest to the provided colour. Can be overridden to
   *
   * @param any colour to match
   * @return nearest named colour. will always return a value
   * @since 4.14.0
   */
  protected @NotNull TextColor nearestColorTo(final @NotNull TextColor any) {
    return NamedTextColor.nearestColorTo(this.colors, any);
  }

  private @Nullable FormatCodeType determineFormatType(final char legacy, final String input, final int pos) {
    if (pos >= 14) {
      // The BungeeCord RGB color format uses a repeating sequence of RGB values, each character formatted
      // as their own color format string, and to make things interesting, all the colors are also valid
      // Mojang colors. To differentiate this, we do a lookback check for &x (or equivalent) for its position
      // in the string if it is indeed a BungeeCord-style RGB color.
      final int expectedCharacterPosition = pos - 14;
      final int expectedIndicatorPosition = pos - 13;
      if (input.charAt(expectedCharacterPosition) == this.character && input.charAt(expectedIndicatorPosition) == LEGACY_BUNGEE_HEX_CHAR) {
        return FormatCodeType.BUNGEECORD_UNUSUAL_HEX;
      }
    }
    if (legacy == this.hexCharacter && input.length() - pos >= 6) {
      return FormatCodeType.KYORI_HEX;
    } else if (legacyChars.indexOf(legacy) != -1) {
      return FormatCodeType.MOJANG_LEGACY;
    }
    return null;
  }

  @Nullable DecodedFormat decodeTextFormat(final char legacy, final String input, final int pos) {
    final FormatCodeType foundFormat = this.determineFormatType(legacy, input, pos);
    if (foundFormat == null) {
      return null;
    }
    if (foundFormat == FormatCodeType.KYORI_HEX) {
      final @Nullable TextColor parsed = tryParseHexColor(input.substring(pos, pos + 6));
      if (parsed != null) {
        return new DecodedFormat(foundFormat, parsed);
      }
    } else if (foundFormat == FormatCodeType.MOJANG_LEGACY) {
      return new DecodedFormat(foundFormat, formats.get(legacyChars.indexOf(legacy)));
    } else if (foundFormat == FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
      final StringBuilder foundHex = new StringBuilder(6);
      for (int i = pos - 1; i >= pos - 11; i -= 2) {
        foundHex.append(input.charAt(i));
      }
      final @Nullable TextColor parsed = tryParseHexColor(foundHex.reverse().toString());
      if (parsed != null) {
        return new DecodedFormat(foundFormat, parsed);
      }
    }
    return null;
  }

  private static @Nullable TextColor tryParseHexColor(final String hexDigits) {
    try {
      final int color = Integer.parseInt(hexDigits, 16);
      return TextColor.color(color);
    } catch (final NumberFormatException ex) {
      return null;
    }
  }

  private static boolean isHexTextColor(final TextFormat format) {
    return format instanceof TextColor && !(format instanceof NamedTextColor);
  }

  String toLegacyCode(TextFormat format) {
    if (isHexTextColor(format)) {
      final TextColor color = (TextColor) format;
      if (this.hexColours) {
        final String hex = String.format("%06x", color.value());
        if (this.useTerriblyStupidHexFormat) {
          // ah yes, wonderful. A 14 digit long completely unreadable string.
          final StringBuilder legacy = new StringBuilder(String.valueOf(LEGACY_BUNGEE_HEX_CHAR));
          for (int i = 0, length = hex.length(); i < length; i++) {
            legacy.append(this.character).append(hex.charAt(i));
          }
          return legacy.toString();
        } else {
          // this is a bit nicer, hey?
          return this.hexCharacter + hex;
        }
      } else {
        // if we are not using hex colours, then convert the hex colour
        // to the "nearest" possible named/standard text colour
        format = nearestColorTo(color);
      }
    }
    final int index = formats.indexOf(format);
    return Character.toString(legacyChars.charAt(index));
  }

  protected enum Reset implements TextFormat {
    INSTANCE
  }
}
