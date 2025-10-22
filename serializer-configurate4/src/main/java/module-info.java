/**
 * Serializers for Configurate 4.
 */
module net.kyori.adventure.serializer.configurate4 {
  requires transitive net.kyori.adventure.api;
  requires net.kyori.adventure.text.serializer.commons;
  requires io.leangen.geantyref;
  requires org.spongepowered.configurate;
  requires static org.checkerframework.checker.qual;

  exports net.kyori.adventure.serializer.configurate4;
}
