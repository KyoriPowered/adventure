package net.kyori.adventure.text.serializer.json;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

final class JsonComponentSerializerImpl implements JsonComponentSerializer {
  private static final JsonComponentSerializer MISSING_INSTANCE = new JsonComponentSerializerImpl();
  private static final Optional<Provider> SERVICE = Services.service(Provider.class);

  @Override
  public @NotNull Component deserialize(@NotNull String input) {
    throw new UnsupportedOperationException("no implementation found");
  }

  @Override
  public @NotNull String serialize(@NotNull Component component) {
    throw new UnsupportedOperationException("no implementation found");
  }

  // We cannot store these fields in JsonComponentSerializerImpl directly due to class initialisation issues.
  static final class Instances {
    static final JsonComponentSerializer INSTANCE = SERVICE
      .map(Provider::json)
      .orElse(MISSING_INSTANCE);
    static final JsonComponentSerializer LEGACY_INSTANCE = SERVICE
      .map(Provider::jsonLegacy)
      .orElse(MISSING_INSTANCE);
  }
}
