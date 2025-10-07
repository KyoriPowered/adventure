import org.jspecify.annotations.NullMarked;

/**
 * JSON legacy hover event serialization and deserialization.
 *
 * <p>This is a replacement for the old {@code adventure-text-serializer-gson-legacyimpl} module.</p>
 */
@NullMarked
module net.kyori.adventure.text.serializer.json.legacyimpl {
  requires transitive net.kyori.adventure.text.serializer.json;
  requires net.kyori.adventure.nbt;

  exports net.kyori.adventure.text.serializer.json.legacyimpl;
}
