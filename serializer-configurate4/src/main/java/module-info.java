import org.jspecify.annotations.NullMarked;

/**
 * Serializers for Configurate 4.
 */
@NullMarked
module net.kyori.adventure.serializer.configurate4 {
  requires transitive net.kyori.adventure.api;
  requires net.kyori.adventure.text.serializer.commons;
  requires transitive io.leangen.geantyref;
  requires transitive org.spongepowered.configurate;
  requires static transitive org.checkerframework.checker.qual;

  exports net.kyori.adventure.serializer.configurate4;
}
