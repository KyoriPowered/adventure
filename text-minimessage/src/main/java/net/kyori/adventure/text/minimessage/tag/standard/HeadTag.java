package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.UUID;
import java.util.regex.Pattern;
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
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Nullable;

/**
 * A head object tag.
 *
 * @sinceMinecraft 1.21.9
 * @since 4.25.0
 */
final class HeadTag {
  private static final String HEAD = "head";
  private static final Pattern UUIDv4_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ABCD][0-9a-f]{3}-[0-9a-f]{12}", Pattern.CASE_INSENSITIVE);

  static final TagResolver RESOLVER = SerializableResolver.claimingComponent(
    HEAD,
    HeadTag::create,
    HeadTag::claimComponent
  );

  private HeadTag() {
  }

  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    if (!args.hasNext()) {
      return Tag.selfClosingInserting(Component.object(
        ObjectContents.playerHead().build()
      ));
    }

    UUID id = null;
    String name = null;
    Boolean hat = null;

    final @Subst("empty") String firstArgString = args.pop().value();
    final @Nullable Tag.Argument secondArg = args.hasNext() ? args.pop() : null;
    final @Nullable Tag.Argument thirdArg = args.hasNext() ? args.pop() : null;

    // Special handling for textures
    if ((firstArgString.contains("/") || firstArgString.contains(":")) && thirdArg == null) {
      if (secondArg != null) {
        if (secondArg.isTrue() || secondArg.isFalse()) {
          hat = secondArg.isTrue();
        } else {
          throw ctx.newException("The second argument after a texture path must be a boolean.");
        }
      }

      if (!Key.parseable(firstArgString)) {
        throw ctx.newException("Could not parse texture path as a key.");
      }

      return Tag.selfClosingInserting(Component.object(
        ObjectContents.playerHead()
          .texture(Key.key(firstArgString))
          .hat(hat == null ? PlayerHeadObjectContents.HAT_DEFAULT : hat)
          .build()
      ));
    }

    if (UUIDv4_PATTERN.matcher(firstArgString).matches()) {
      id = UUID.fromString(firstArgString);
    } else {
      name = firstArgString;
    }

    if (secondArg == null) {
      // Only one argument was provided, meaning we can early return.
      return Tag.selfClosingInserting(Component.object(
        id == null
          ? ObjectContents.playerHead(name)
          : ObjectContents.playerHead(id)
      ));
    }

    if (thirdArg == null) {
      if (secondArg.isFalse() || secondArg.isTrue()) {
        hat = secondArg.isTrue();
      } else if (id == null) {
        // There are two arguments, where the first one is a name and the second one is not a boolean.
        // Parse it as an UUID.
        if (UUIDv4_PATTERN.matcher(secondArg.value()).matches()) {
          id = UUID.fromString(secondArg.value());
        } else {
          throw ctx.newException("The second argument must either be a valid boolean or UUID.");
        }
      } else {
        // There are two arguments, where the first one is an UUID and the second one is not a boolean.
        // Parse it as a name.
        name = secondArg.value();
      }
    } else {
      if (!thirdArg.isFalse() && !thirdArg.isTrue()) {
        throw ctx.newException("The third argument must be a boolean.");
      }
      hat = thirdArg.isTrue();

      if (id == null) {
        // The second arg should be interpreted as an UUID.
        if (UUIDv4_PATTERN.matcher(secondArg.value()).matches()) {
          id = UUID.fromString(secondArg.value());
        } else {
          throw ctx.newException("The second argument must be a valid UUID.");
        }
      } else {
        name = secondArg.value();
      }
    }

    return Tag.selfClosingInserting(Component.object(
      ObjectContents.playerHead()
        .id(id)
        .name(name)
        .hat(hat == null ? PlayerHeadObjectContents.HAT_DEFAULT : hat)
        .build()
    ));
  }

  static @Nullable Emitable claimComponent(final Component input) {
    if (!(input instanceof ObjectComponent)) {
      return null;
    }

    final ObjectContents contents = ((ObjectComponent) input).contents();
    if (!(contents instanceof PlayerHeadObjectContents)) {
      return null;
    }

    final PlayerHeadObjectContents playerHead = ((PlayerHeadObjectContents) contents);
    return emit -> {
      emit.tag(HEAD);

      final UUID id = playerHead.id();
      final String name = playerHead.name();
      final Key texture = playerHead.texture();

      if (id == null && name == null && texture != null) {
        emit.argument(texture.asMinimalString());
        if (playerHead.hat() != PlayerHeadObjectContents.HAT_DEFAULT) {
          emit.argument(Boolean.toString(playerHead.hat()));
        }
        return;
      }

      if (name != null) {
        emit.argument(name);
      }

      if (id != null) {
        emit.argument(id.toString());
      }

      if (playerHead.hat() != PlayerHeadObjectContents.HAT_DEFAULT) {
        emit.argument(Boolean.toString(playerHead.hat()));
      }
    };
  }
}
