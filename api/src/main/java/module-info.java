/**
 * Adventure: a serverside user interface library for Minecraft: Java Edition.
 *
 * <p>See the <a href="https://docs.papermc.io/adventure/">documentation</a>
 * for usage and dependency information for this project and associated libraries.</p>
 */
module net.kyori.adventure.api {
  requires transitive net.kyori.adventure.key;
  requires transitive org.jspecify;
  requires transitive org.jetbrains.annotations;

  exports net.kyori.adventure;
  exports net.kyori.adventure.audience;
  exports net.kyori.adventure.bossbar;
  exports net.kyori.adventure.builder;
  exports net.kyori.adventure.chat;
  exports net.kyori.adventure.dialog;
  exports net.kyori.adventure.identity;
  exports net.kyori.adventure.inventory;
  exports net.kyori.adventure.nbt.api;
  exports net.kyori.adventure.permission;
  exports net.kyori.adventure.pointer;
  exports net.kyori.adventure.resource;
  exports net.kyori.adventure.sound;
  exports net.kyori.adventure.text;
  exports net.kyori.adventure.text.event;
  exports net.kyori.adventure.text.flattener;
  exports net.kyori.adventure.text.format;
  exports net.kyori.adventure.text.object;
  exports net.kyori.adventure.text.renderer;
  exports net.kyori.adventure.text.serializer;
  exports net.kyori.adventure.title;
  exports net.kyori.adventure.translation;
  exports net.kyori.adventure.util;
}
