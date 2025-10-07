import org.jspecify.annotations.NullMarked;

/**
 * A reference composed of a namespace and a path.
 */
@NullMarked
module net.kyori.adventure.key {
  requires transitive net.kyori.examination.string;
  requires static org.jspecify;

  exports net.kyori.adventure.key;
}
