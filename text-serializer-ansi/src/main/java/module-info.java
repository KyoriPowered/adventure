import org.jspecify.annotations.NullMarked;

/**
 * Serialization to ANSI escape sequences. Does not support deserialization.
 */
@NullMarked
module net.kyori.adventure.text.serializer.ansi {
  requires transitive net.kyori.adventure.api;
  requires transitive net.kyori.ansi;

  exports net.kyori.adventure.text.serializer.ansi;
}
