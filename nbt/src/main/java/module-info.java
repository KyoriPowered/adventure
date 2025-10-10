import org.jspecify.annotations.NullMarked;

/**
 * An implementation of the <a href="https://wiki.vg/NBT">NBT</a> format.
 *
 * <p>Adventure supports serializing to both binary and string representations
 * of the tags, both through {@link net.kyori.adventure.nbt.BinaryTagIO}</p>
 */
@NullMarked
module net.kyori.adventure.nbt {
  requires transitive org.jspecify;
  requires transitive org.jetbrains.annotations;

  exports net.kyori.adventure.nbt;
}
