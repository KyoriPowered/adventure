import org.jspecify.annotations.NullMarked;

/**
 * Plain text based serialization and deserialization.
 */
@NullMarked
module net.kyori.adventure.text.serializer.plain {
  requires transitive net.kyori.adventure.api;

  exports net.kyori.adventure.text.serializer.plain;
}
