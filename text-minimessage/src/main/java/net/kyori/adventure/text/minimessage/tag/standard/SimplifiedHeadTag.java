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

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
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
import org.jetbrains.annotations.Nullable;

/**
 * A simplified head object tag for only setting either the name,
 * uuid, or texture key of a head object component.
 *
 * @sinceMinecraft 1.21.9
 * @since 4.25.0
 */
final class SimplifiedHeadTag {
  private static final String HEAD = "head";

  static final TagResolver RESOLVER = SerializableResolver.claimingComponent(
    HEAD,
    SimplifiedHeadTag::create,
    SimplifiedHeadTag::claimComponent
  );

  private SimplifiedHeadTag() {
  }

  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    if (!args.hasNext()) {
      return Tag.selfClosingInserting(Component.object(
        ObjectContents.playerHead().build()
      ));
    }

    final String argument = args.pop().value();

    if (args.hasNext()) {
      throw ctx.newException("Too many arguments present", args);
    }

    if (HeadTag.UUIDv4_PATTERN.matcher(argument).matches()) {
      return Tag.selfClosingInserting(Component.object(
        ObjectContents.playerHead()
          .id(UUID.fromString(argument))
          .build()
      ));
    }

    if (argument.contains("/")) {
      return Tag.selfClosingInserting(Component.object(
        ObjectContents.playerHead()
          .id(UUID.fromString(argument))
          .build()
      ));
    }

    return Tag.selfClosingInserting(Component.object(
      ObjectContents.playerHead()
        .name(argument)
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

    if (playerHead.hat() != PlayerHeadObjectContents.DEFAULT_HAT) {
      return null;
    }

    PresentType present = null;

    if (playerHead.name() != null) {
      present = PresentType.NAME;
    }

    if (playerHead.id() != null) {
      if (present != null) {
        return null;
      }
      present = PresentType.ID;
    }

    if (playerHead.texture() != null) {
      if (present != null) {
        return null;
      }
      present = PresentType.TEXTURE;
    }

    if (present == null) {
      return null;
    }

    final PresentType finalPresent = present;
    return emit -> {
      emit.tag(HEAD);

      final String value = finalPresent.map(playerHead);
      emit.argument(value);
    };
  }

  private enum PresentType {
    NAME(PlayerHeadObjectContents::name),
    ID(obj -> Objects.requireNonNull(obj.id()).toString()),
    TEXTURE(obj -> Objects.requireNonNull(obj.texture()).asMinimalString());

    private final Function<PlayerHeadObjectContents, String> mappingFunction;

    PresentType(Function<PlayerHeadObjectContents, String> mappingFunction) {
      this.mappingFunction = mappingFunction;
    }

    public String map(PlayerHeadObjectContents obj) {
      return mappingFunction.apply(obj);
    }
  }
}
