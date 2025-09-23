/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
import net.kyori.adventure.text.minimessage.tag.resolver.NamedArgumentMap;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Nullable;

/**
 * A head object tag.
 *
 * @since 4.25.0
 * @sinceMinecraft 1.21.9
 */
final class HeadTag {
  private static final String HEAD = "head";
  private static final Pattern UUIDv4_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ABCD][0-9a-f]{3}-[0-9a-f]{12}", Pattern.CASE_INSENSITIVE);

  static final TagResolver RESOLVER = SerializableResolver.claimingComponentNamed(
    HEAD,
    HeadTag::create,
    HeadTag::claimComponent
  );

  private HeadTag() {
  }

  static Tag create(final NamedArgumentMap args, final Context ctx) throws ParsingException {
    if (args.size() == 0) {
      return Tag.selfClosingInserting(Component.object(
        ObjectContents.playerHead().build()
      ));
    }

    final boolean hat = args.flag("hat").toBooleanOrElse(PlayerHeadObjectContents.DEFAULT_HAT);

    if (args.isPresent("texture")) {
      final @Subst("empty") String texture = args.orThrow("texture").value();
      if (!Key.parseable(texture)) {
        throw ctx.newException("invalid textures key: '" + texture + "'");
      }

      return Tag.selfClosingInserting(Component.object(
        ObjectContents.playerHead()
          .texture(Key.key(texture))
          .hat(hat)
          .build()
      ));
    }

    final String uuidString = args.isPresent("uuid") ? args.orThrow("uuid").value() : "";
    final UUID uuid = UUIDv4_PATTERN.matcher(uuidString).matches() ? UUID.fromString(uuidString) : null;

    final String name = args.isPresent("name") ? args.orThrow("name").value() : null;

    return Tag.selfClosingInserting(Component.object(
      ObjectContents.playerHead()
        .id(uuid)
        .name(name)
        .hat(hat)
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

      final String name = playerHead.name();
      final UUID id = playerHead.id();
      final Key texture = playerHead.texture();

      if (name != null) {
        emit.namedArgument("name", name);
      }

      if (id != null) {
        emit.namedArgument("uuid", id.toString());
      }

      if (texture != null) {
        emit.namedArgument("texture", texture.asMinimalString());
      }

      if (playerHead.hat() != PlayerHeadObjectContents.DEFAULT_HAT) {
        emit.flag("hat", playerHead.hat());
      }
    };
  }
}
