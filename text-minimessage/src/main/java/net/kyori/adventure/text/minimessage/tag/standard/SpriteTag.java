package net.kyori.adventure.text.minimessage.tag.standard;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Nullable;

/**
 * An object sprite tag.
 *
 * @since 4.25.0
 */
final class SpriteTag {
  private static final String SPRITE = "sprite";

  static final TagResolver RESOLVER = SerializableResolver.claimingComponent(
    SPRITE,
    SpriteTag::create,
    SpriteTag::claimComponent
  );

  private SpriteTag() {
  }

  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final @Subst("empty") String object = args.popOr("A value is required to produce an object component").value();
    return Tag.selfClosingInserting(Component.object(ObjectComponent.Contents.sprite(Key.key(object))));
  }

  static @Nullable Emitable claimComponent(final Component input) {
    if (!(input instanceof ObjectComponent)) {
      return null;
    }

    final ObjectComponent.Contents contents = ((ObjectComponent) input).contents();
    if (!(contents instanceof ObjectComponent.SpriteContents)) {
      return null;
    }

    final Key key = ((ObjectComponent.SpriteContents) contents).sprite();

    return emit -> emit.tag(SPRITE).argument(key.asString());
  }
}
