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
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.Nullable;

/**
 * A sprite object tag.
 *
 * @since 4.25.0
 * @sinceMinecraft 1.21.9
 */
final class SpriteTag {
  static final String SPRITE = "sprite";

  static final TagResolver RESOLVER = SerializableResolver.claimingComponent(
    SPRITE,
    SpriteTag::create,
    SpriteTag::claimComponent
  );

  private SpriteTag() {
  }

  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final @Subst("empty") String firstArg = args.popOr("An atlas id and or a sprite id is required to produce a sprite object component").value();
    final @Subst("empty") String secondArg = args.hasNext() ? args.pop().value() : null;

    if (secondArg == null) {
      return Tag.selfClosingInserting(Component.object(
        ObjectContents.sprite(
          Key.key(firstArg)
        )
      ));
    }
    return Tag.selfClosingInserting(Component.object(
      ObjectContents.sprite(
        Key.key(firstArg),
        Key.key(secondArg)
      )
    ));
  }

  static @Nullable Emitable claimComponent(final Component input) {
    if (!(input instanceof ObjectComponent objectComponent)) {
      return null;
    }

    final ObjectContents contents = objectComponent.contents();
    if (!(contents instanceof SpriteObjectContents sprite)) {
      return null;
    }

    final Key atlas = sprite.atlas();
    final Key key = sprite.sprite();

    if (atlas == SpriteObjectContents.DEFAULT_ATLAS) {
      return emit -> emit.tag(SPRITE).argument(key.asString());
    }

    return emit -> emit.tag(SPRITE).argument(atlas.asString()).argument(key.asString());
  }
}
