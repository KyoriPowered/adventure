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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tree.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import static net.kyori.adventure.text.Component.text;

/**
 * Replaces text inside the tag with the "small caps" character set.
 *
 * @since 4.24.0
 */
final class SmallCapsTag implements Modifying {

  private static final Map<Character, Character> smallFontCharMap = new HashMap<>();

  static {
    smallFontCharMap.put('a', 'ᴀ');
    smallFontCharMap.put('b', 'ʙ');
    smallFontCharMap.put('c', 'ᴄ');
    smallFontCharMap.put('d', 'ᴅ');
    smallFontCharMap.put('e', 'ᴇ');
    smallFontCharMap.put('f', 'ꜰ');
    smallFontCharMap.put('g', 'ɢ');
    smallFontCharMap.put('h', 'ʜ');
    smallFontCharMap.put('i', 'ɪ');
    smallFontCharMap.put('j', 'ᴊ');
    smallFontCharMap.put('k', 'ᴋ');
    smallFontCharMap.put('l', 'ʟ');
    smallFontCharMap.put('m', 'ᴍ');
    smallFontCharMap.put('n', 'ɴ');
    smallFontCharMap.put('o', 'ᴏ');
    smallFontCharMap.put('p', 'ᴘ');
    smallFontCharMap.put('q', 'ǫ');
    smallFontCharMap.put('r', 'ʀ');
    smallFontCharMap.put('s', 's');
    smallFontCharMap.put('t', 'ᴛ');
    smallFontCharMap.put('u', 'ᴜ');
    smallFontCharMap.put('v', 'ᴠ');
    smallFontCharMap.put('w', 'ᴡ');
    smallFontCharMap.put('x', 'x');
    smallFontCharMap.put('y', 'ʏ');
    smallFontCharMap.put('z', 'ᴢ');

    // TODO might be worth putting modifying non-alphabet chars behind a toggle or something? (or remove entirely?)
    smallFontCharMap.put('(', '₍');
    smallFontCharMap.put(')', '₎');
    smallFontCharMap.put('0', '₀');
    smallFontCharMap.put('1', '₁');
    smallFontCharMap.put('2', '₂');
    smallFontCharMap.put('3', '₃');
    smallFontCharMap.put('4', '₄');
    smallFontCharMap.put('5', '₅');
    smallFontCharMap.put('6', '₆');
    smallFontCharMap.put('7', '₇');
    smallFontCharMap.put('8', '₈');
    smallFontCharMap.put('9', '₉');
  }

  @VisibleForTesting
  static String convert(String text) {
    text = text.toLowerCase(Locale.ROOT);
    final StringBuilder result = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      final char character = text.charAt(i);
      if (smallFontCharMap.containsKey(character)) {
        result.append(smallFontCharMap.get(character));
      } else {
        result.append(character);
      }
    }

    return result.toString();
  }

  static final String SMALL = "small";
  static final TagResolver RESOLVER = TagResolver.resolver(SMALL, SmallCapsTag::create);
  private static final ComponentFlattener LENGTH_CALCULATOR = ComponentFlattener.builder()
    .mapper(TextComponent.class, TextComponent::content)
    .unknownMapper(x -> "_")
    .build();
  private boolean visited;
  private int size = 0;

  private SmallCapsTag() {
  }

  static Tag create(final ArgumentQueue args, final Context ctx) {
    return new SmallCapsTag();
  }

  @Override
  public void visit(final @NotNull Node current, final int depth) {
    if (this.visited) {
      throw new IllegalStateException("Small caps tag instances cannot be re-used, return a new one for each resolve");
    }

    if (current instanceof ValueNode) {
      final String value = ((ValueNode) current).value();
      this.size += value.codePointCount(0, value.length());
    } else if (current instanceof TagNode) {
      final TagNode tagNode = (TagNode) current;
      if (tagNode.tag() instanceof Inserting) {
        // ComponentTransformation.apply() returns the value of the component placeholder
        LENGTH_CALCULATOR.flatten(((Inserting) tagNode.tag()).value(), s -> this.size += s.codePointCount(0, s.length()));
      }
    }
  }

  @Override
  public void postVisit() {
    this.visited = true;
  }

  @Override
  public Component apply(final @NotNull Component current, final int depth) {
    if (current instanceof TextComponent && !((TextComponent) current).content().isEmpty()) {
      return text(convert(((TextComponent) current).content()));
    } else if (!(current instanceof TextComponent)) {
      return current.children(Collections.emptyList());
    }

    return Component.empty().mergeStyle(current);
  }

}
