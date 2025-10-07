import org.jspecify.annotations.NullMarked;

/**
 * A common abstraction providing an API to serializer components with multiple JSON libraries.
 */
@NullMarked
module net.kyori.adventure.text.serializer.json {
  requires transitive net.kyori.adventure.api;
  requires transitive net.kyori.option;

  exports net.kyori.adventure.text.serializer.json;
}
