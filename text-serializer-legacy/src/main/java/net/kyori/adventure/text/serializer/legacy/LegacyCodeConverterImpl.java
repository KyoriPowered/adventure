package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class LegacyCodeConverterImpl extends LegacyCodeConverter {

  private static final List<TextFormat> FORMATS;
  private static final String LEGACY_CHARS;

  static {
    // Enumeration order may change - manually

    final Map<TextFormat, String> formats = new LinkedHashMap<>(16 + 5 + 1); // colours + decorations + reset

    formats.put(NamedTextColor.BLACK, "0");
    formats.put(NamedTextColor.DARK_BLUE, "1");
    formats.put(NamedTextColor.DARK_GREEN, "2");
    formats.put(NamedTextColor.DARK_AQUA, "3");
    formats.put(NamedTextColor.DARK_RED, "4");
    formats.put(NamedTextColor.DARK_PURPLE, "5");
    formats.put(NamedTextColor.GOLD, "6");
    formats.put(NamedTextColor.GRAY, "7");
    formats.put(NamedTextColor.DARK_GRAY, "8");
    formats.put(NamedTextColor.BLUE, "9");
    formats.put(NamedTextColor.GREEN, "a");
    formats.put(NamedTextColor.AQUA, "b");
    formats.put(NamedTextColor.RED, "c");
    formats.put(NamedTextColor.LIGHT_PURPLE, "d");
    formats.put(NamedTextColor.YELLOW, "e");
    formats.put(NamedTextColor.WHITE, "f");

    formats.put(TextDecoration.OBFUSCATED, "k");
    formats.put(TextDecoration.BOLD, "l");
    formats.put(TextDecoration.STRIKETHROUGH, "m");
    formats.put(TextDecoration.UNDERLINED, "n");
    formats.put(TextDecoration.ITALIC, "o");

    formats.put(LegacyCodeConverter.Reset.INSTANCE, "r");

    FORMATS = Collections.unmodifiableList(new ArrayList<>(formats.keySet()));
    LEGACY_CHARS = String.join("", formats.values());

    // assert same length
    if (FORMATS.size() != LEGACY_CHARS.length()) {
      throw new IllegalStateException("FORMATS length differs from LEGACY_CHARS length");
    }
  }

  LegacyCodeConverterImpl(final char character, final char hexCharacter, final boolean hexColours, final boolean useTerriblyStupidHexFormat) {
    super(character, hexCharacter, hexColours, useTerriblyStupidHexFormat);
  }

  static @Nullable LegacyFormat legacyFormat(final char character) {
    final int index = LEGACY_CHARS.indexOf(character);
    if (index != -1) {
      final TextFormat format = FORMATS.get(index);
      if (format instanceof TextColor) {
        return new LegacyFormat((TextColor) format);
      } else if (format instanceof TextDecoration) {
        return new LegacyFormat((TextDecoration) format);
      } else if (format instanceof LegacyCodeConverter.Reset) {
        return LegacyFormat.RESET;
      }
    }
    return null;
  }

  @Override
  public @NotNull NamedTextColor nearestColorTo(final @NotNull TextColor color) {
    return NamedTextColor.nearestTo(color);
  }

  @Override
  @NotNull List<TextFormat> getFormats() {
    return FORMATS;
  }

  @Override
  @NotNull String getAllLegacyChars() {
    return LEGACY_CHARS;
  }

  @Override
  @NotNull List<? extends TextColor> getLegacyColors() {
    return NamedTextColor.VALUES;
  }

  @Override
  @NotNull LegacyCodeConverterSupplier createSupplier() {
    return LegacyCodeConverterImpl::new;
  }
}
