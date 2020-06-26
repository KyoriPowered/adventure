/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
interface UrlClickEventExtractor {
  /**
   * Extracts URLs in the given component.
   *
   * @param original the component
   * @return the component with links extracted
   */
  @NonNull TextComponent extract(final @NonNull TextComponent original);

  /**
   * No-op instance.
   */
  UrlClickEventExtractor NO_OP = original -> original;

  /**
   * An extractor which uses no style for links.
   */
  UrlClickEventExtractor NO_STYLE = new Impl(null);

  /**
   * Creates an extractor which extracts links with the given {@code style}.
   *
   * @param style the style
   * @return the extractor instance
   */
  static UrlClickEventExtractor withStyle(final @Nullable Style style) {
    return style == null ? NO_OP : new Impl(style);
  }

  class Impl implements UrlClickEventExtractor {
    private static final Pattern URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?");

    private final @Nullable Style style;

    Impl(final @Nullable Style style) {
      this.style = style;
    }

    @Override
    public @NonNull TextComponent extract(final @NonNull TextComponent original) {
      final List<Component> produced = new ArrayList<>();
      final Queue<TextComponent> queue = new ArrayDeque<>();
      queue.add(original);

      while(!queue.isEmpty()) {
        final TextComponent current = queue.remove();
        final String content = current.content();
        final Matcher matcher = URL_PATTERN.matcher(content);
        final TextComponent withoutChildren = current.children(Collections.emptyList());

        if(matcher.find()) {
          int lastEnd = 0;
          do {
            final int start = matcher.start();
            final int end = matcher.end();
            final String matched = matcher.group();

            final String prefix = content.substring(lastEnd, start);
            if(!prefix.isEmpty()) {
              produced.add(withoutChildren.content(prefix));
            }

            Component link = withoutChildren.content(matched).clickEvent(ClickEvent.openUrl(matched));
            if(this.style != null) {
              link = link.style(link.style().merge(this.style));
            }
            produced.add(link);
            lastEnd = end;
          } while(matcher.find());

          if(content.length() - lastEnd > 0) {
            produced.add(withoutChildren.content(content.substring(lastEnd)));
          }
        } else {
          // children are handled separately
          produced.add(withoutChildren);
        }

        for(final Component child : current.children()) {
          // we can guarantee that all children are TextComponents
          queue.add((TextComponent) child);
        }
      }

      if(produced.size() == 1) {
        return (TextComponent) produced.get(0);
      } else {
        final List<Component> children = produced.subList(1, produced.size());
        return (TextComponent) produced.get(0).children(children);
      }
    }
  }
}
