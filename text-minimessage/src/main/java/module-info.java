/**
 * MiniMessage, a friendly text format for representing chat components.
 *
 * @see <a href="https://docs.papermc.io/adventure/minimessage/format">Format Documentation</a>
 */
module net.kyori.adventure.text.minimessage {
  requires transitive net.kyori.adventure.api;

  exports net.kyori.adventure.text.minimessage;
  exports net.kyori.adventure.text.minimessage.tag;
  exports net.kyori.adventure.text.minimessage.tag.standard;
  exports net.kyori.adventure.text.minimessage.tag.resolver;
  exports net.kyori.adventure.text.minimessage.translation;
  exports net.kyori.adventure.text.minimessage.tree;

  uses net.kyori.adventure.text.minimessage.MiniMessage.Provider;
}
