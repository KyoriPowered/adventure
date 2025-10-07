import org.jspecify.annotations.NullMarked;

/**
 * Legacy text based serialization and deserialization.
 */
@NullMarked
module net.kyori.adventure.text.serializer.legacy {
  requires transitive net.kyori.adventure.api;

  exports net.kyori.adventure.text.serializer.legacy;
}
