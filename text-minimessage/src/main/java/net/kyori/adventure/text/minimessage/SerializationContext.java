package net.kyori.adventure.text.minimessage;

import java.util.Map;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface SerializationContext {
  @NotNull
  @Unmodifiable
  Map<String, TextColor> namedColors();

  @NotNull
  @Unmodifiable
  Map<String, String> namedColorAliases();

  default @Nullable TextColor namedColor(final @NotNull String name) {
    final TextColor color = namedColors().get(name);
    if (color != null) {
      return color;
    }

    final String alias = namedColorAliases().get(name);
    if (alias != null) {
      return namedColor(alias);
    }

    return null;
  }
}
