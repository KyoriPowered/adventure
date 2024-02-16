/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

/**
 * Insert a selector component into the result.
 *
 * @since 4.11.0
 */
final class SelectorTag {
  private static final String SEL = "sel";
  private static final String SELECTOR = "selector";

  static final TagResolver RESOLVER = SerializableResolver.claimingComponent(
    StandardTags.names(SEL, SELECTOR),
    SelectorTag::create,
    SelectorTag::claim
  );

  private SelectorTag() {
  }

  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final String key = args.popOr("A selection key is required").value();
    ComponentLike separator = null;
    if (args.hasNext()) {
      separator = ctx.deserialize(args.pop().value());
    }

    return Tag.inserting(Component.selector(key, separator));
  }

  static @Nullable Emitable claim(final Component input) {
    if (!(input instanceof SelectorComponent)) return null;

    final SelectorComponent st = (SelectorComponent) input;
    return emit -> {
      emit.tag(SEL);
      emit.argument(st.pattern());
      if (st.separator() != null) {
        emit.argument(st.separator());
      }
    };
  }
}
