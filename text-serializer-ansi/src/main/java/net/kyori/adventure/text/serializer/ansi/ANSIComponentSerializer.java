package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.ansi.ColorLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface ANSIComponentSerializer extends ComponentSerializer<Component, Component, String> {

  static @NotNull ANSIComponentSerializer ansi() {
    return ANSIComponentSerializerImpl.Instances.INSTANCE;
  }

  @NotNull String serialize(@NotNull Component component, @NotNull ColorLevel colorLevel);

  @Override
  @NotNull
  default Component deserialize(@NotNull String input) {
    throw new UnsupportedOperationException("AnsiComponentSerializer does not support deserialization");
  }

  /**
   * A {@link ANSIComponentSerializer} service provider.
   *
   * @since 4.14.0
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a {@link ANSIComponentSerializer}.
     *
     * @return a {@link ANSIComponentSerializer}
     * @since 4.8.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull ANSIComponentSerializer ansi();
  }
}
