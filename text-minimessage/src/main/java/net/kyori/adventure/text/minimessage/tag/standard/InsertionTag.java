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

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * A transformation that applies an insertion (shift-click) event.
 *
 * @since 4.10.0
 */
final class InsertionTag {
  private static final String INSERTION = "insert";

  static final TagResolver RESOLVER = SerializableResolver.claimingStyle(
    INSERTION,
    InsertionTag::create,
    StyleClaim.claim(INSERTION, Style::insertion, InsertionTag::emit)
  );

  private InsertionTag() {
  }

  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final String insertion = args.popOr("A value is required to produce an insertion component").value();
    return Tag.styling(b -> b.insertion(insertion));
  }

  static void emit(final String insertion, final TokenEmitter emitter) {
    emitter.tag(INSERTION).argument(insertion);
  }
}
