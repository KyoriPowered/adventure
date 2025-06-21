package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

final class TextColorSerializer {

  private TextColorSerializer() {
  }

  static @NotNull TextColor deserialize(@NotNull StringBinaryTag tag) {
    String value = tag.value();
    if (value.startsWith(TextColor.HEX_PREFIX)) {
      return TextColor.fromHexString(value);
    } else {
      return NamedTextColor.NAMES.value(value);
    }
  }

  static @NotNull StringBinaryTag serialize(@NotNull TextColor color) {
    String value = color instanceof NamedTextColor
      ? NamedTextColor.NAMES.keyOrThrow((NamedTextColor) color)
      : asUpperCaseHexString(color);
    return StringBinaryTag.stringBinaryTag(value);
  }

  private static String asUpperCaseHexString(final TextColor color) {
    return String.format(Locale.ROOT, "%c%06X", TextColor.HEX_CHARACTER, color.value()); // to be consistent with vanilla
  }
}
