package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class LegacyCodeConverterImpl extends LegacyCodeConverter {

  private static final List<CharacterAndFormat> FORMATS;
  private static final String LEGACY_CHARS;

  static {
    // Enumeration order may change - manually

    final List<CharacterAndFormat> formats = new ArrayList<>(16 + 5 + 1); // colours + decorations + reset

    formats.add(new CharacterAndFormat('0', NamedTextColor.BLACK));
    formats.add(new CharacterAndFormat('1', NamedTextColor.DARK_BLUE));
    formats.add(new CharacterAndFormat('2', NamedTextColor.DARK_GREEN));
    formats.add(new CharacterAndFormat('3', NamedTextColor.DARK_AQUA));
    formats.add(new CharacterAndFormat('4', NamedTextColor.DARK_RED));
    formats.add(new CharacterAndFormat('5', NamedTextColor.DARK_PURPLE));
    formats.add(new CharacterAndFormat('6', NamedTextColor.GOLD));
    formats.add(new CharacterAndFormat('7', NamedTextColor.GRAY));
    formats.add(new CharacterAndFormat('8', NamedTextColor.DARK_GRAY));
    formats.add(new CharacterAndFormat('9', NamedTextColor.BLUE));
    formats.add(new CharacterAndFormat('a', NamedTextColor.GREEN));
    formats.add(new CharacterAndFormat('b', NamedTextColor.AQUA));
    formats.add(new CharacterAndFormat('c', NamedTextColor.RED));
    formats.add(new CharacterAndFormat('d', NamedTextColor.LIGHT_PURPLE));
    formats.add(new CharacterAndFormat('e', NamedTextColor.YELLOW));
    formats.add(new CharacterAndFormat('f', NamedTextColor.WHITE));

    formats.add(new CharacterAndFormat('k', TextDecoration.OBFUSCATED));
    formats.add(new CharacterAndFormat('l', TextDecoration.BOLD));
    formats.add(new CharacterAndFormat('m', TextDecoration.STRIKETHROUGH));
    formats.add(new CharacterAndFormat('n', TextDecoration.UNDERLINED));
    formats.add(new CharacterAndFormat('o', TextDecoration.ITALIC));

    formats.add(new CharacterAndFormat('r', LegacyCodeConverter.Reset.INSTANCE));

    StringBuilder legacyChars = new StringBuilder(formats.size());
    FORMATS = Collections.unmodifiableList(formats);
    for (int i = 0; i < FORMATS.size(); i++) {
      legacyChars.append(FORMATS.get(i).getCharacter());
    }
    LEGACY_CHARS = legacyChars.toString();
  }

  LegacyCodeConverterImpl(final char character, final char hexCharacter, final boolean hexColours, final boolean useTerriblyStupidHexFormat) {
    super(FORMATS, character, hexCharacter, hexColours, useTerriblyStupidHexFormat);
  }

  static @Nullable LegacyFormat legacyFormat(final char character) {
    final int index = LEGACY_CHARS.indexOf(character);
    if (index != -1) {
      final CharacterAndFormat characterAndFormat = FORMATS.get(index);
      final TextFormat format = characterAndFormat.getFormat();
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
  @NotNull LegacyCodeConverterSupplier createSupplier() {
    return LegacyCodeConverterImpl::new;
  }
}
