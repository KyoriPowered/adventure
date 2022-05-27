/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import java.util.OptionalInt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Repeat tag.
 *
 * @since 4.11.0
 */
final class RepeatTag {

  private static final String REPEAT = "repeat";

  static final TagResolver RESOLVER = TagResolver.resolver(REPEAT, RepeatTag::create);

  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final OptionalInt optionalCount = args.popOr("No repeat count found.").asInt();
    if (!optionalCount.isPresent()) {
      throw ctx.newException("Repeat count must be an integer.");
    }
    final int count = optionalCount.getAsInt();
    if (count < 0) {
      throw ctx.newException("Repeat count must be non-negative.");
    }
    final String text = args.popOr("No text found to repeat.").value();
    final Component toRepeat = ctx.deserialize(text);
    Component bar = Component.empty();
    for (int i = 0; i < count; i++) {
      bar = bar.append(toRepeat);
    }
    return Tag.selfClosingInserting(bar);
  }

  private RepeatTag() {
  }

}
