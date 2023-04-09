package net.kyori.adventure.text.serializer.legacy;

import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextFormat;
import net.kyori.adventure.util.HSVLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public abstract class LegacyCodeConverter {
  protected static final char LEGACY_BUNGEE_HEX_CHAR = 'x';

  protected final char character;
  protected final char hexCharacter;
  protected final boolean hexColours;
  protected final boolean useTerriblyStupidHexFormat; // (╯°□°)╯︵ ┻━┻

  protected LegacyCodeConverter(final char character, final char hexCharacter, final boolean hexColours, final boolean useTerriblyStupidHexFormat) {
    this.character = character;
    this.hexCharacter = hexCharacter;
    this.hexColours = hexColours;
    this.useTerriblyStupidHexFormat = useTerriblyStupidHexFormat;
  }

  @NotNull abstract List<TextFormat> getFormats();

  @NotNull abstract String getAllLegacyChars();

  @NotNull abstract List<? extends TextColor> getLegacyColors();

  @NotNull abstract LegacyCodeConverterSupplier createSupplier();

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
    } else if (getAllLegacyChars().indexOf(legacy) != -1) {
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
      return new DecodedFormat(foundFormat, getFormats().get(getAllLegacyChars().indexOf(legacy)));
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
    final int index = getFormats().indexOf(format);
    return Character.toString(getAllLegacyChars().charAt(index));
  }

  /**
   * Find the named colour nearest to the provided colour.
   *
   * @param any colour to match
   * @return nearest named colour. will always return a value
   * @since 4.14.0
   */
  public @NotNull TextColor nearestColorTo(final @NotNull TextColor any) {
    if (any instanceof NamedTextColor) {
      return any;
    }

    requireNonNull(any, "color");

    float matchedDistance = Float.MAX_VALUE;
    final List<? extends TextColor> values = getLegacyColors();
    TextColor match = values.get(0);
    for (int i = 0, length = values.size(); i < length; i++) {
      final TextColor potential = values.get(i);
      final float distance = distance(any.asHSV(), potential.asHSV());
      if (distance < matchedDistance) {
        match = potential;
        matchedDistance = distance;
      }
      if (distance == 0) {
        break; // same colour! whoo!
      }
    }
    return match;
  }

  /**
   * Returns a distance metric to the other colour.
   *
   * <p>This value is unitless and should only be used to compare with other text colours.</p>
   *
   * @param self the base colour
   * @param other colour to compare to
   * @return distance metric
   */
  protected static float distance(final @NotNull HSVLike self, final @NotNull HSVLike other) {
    // weight hue more heavily than saturation and brightness. kind of magic numbers, but is fine for our use case of downsampling to a set of colors
    final float hueDistance = 3 * Math.min(Math.abs(self.h() - other.h()), 1f - Math.abs(self.h() - other.h()));
    final float saturationDiff = self.s() - other.s();
    final float valueDiff = self.v() - other.v();
    return hueDistance * hueDistance + saturationDiff * saturationDiff + valueDiff * valueDiff;
  }

  protected enum Reset implements TextFormat {
    INSTANCE
  }
}
