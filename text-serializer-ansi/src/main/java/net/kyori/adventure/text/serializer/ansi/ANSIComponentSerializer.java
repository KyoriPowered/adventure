package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.ansi.ColorLevel;
import org.jetbrains.annotations.NotNull;

public interface ANSIComponentSerializer extends ComponentSerializer<Component, Component, String> {

  static @NotNull ANSIComponentSerializer ansi() {
    return ANSIComponentSerializerImpl.INSTANCE;
  }

  @NotNull String serialize(@NotNull Component component, @NotNull ColorLevel colorLevel);

  @Override
  @NotNull
  default Component deserialize(@NotNull String input) {
    throw new UnsupportedOperationException("AnsiComponentSerializer does not support deserialization");
  }

}
