/**
 * Serialization to ANSI escape sequences. Does not support deserialization.
 */
module net.kyori.adventure.text.serializer.ansi {
  requires transitive net.kyori.adventure.api;
  requires transitive net.kyori.ansi;

  exports net.kyori.adventure.text.serializer.ansi;

  uses net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer.Provider;
}
