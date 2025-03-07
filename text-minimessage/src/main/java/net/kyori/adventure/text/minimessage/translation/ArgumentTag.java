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
package net.kyori.adventure.text.minimessage.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslationArgumentLike;
import net.kyori.adventure.text.VirtualComponent;
import net.kyori.adventure.text.VirtualComponentRenderer;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ArgumentTag implements TagResolver {
  private static final String NAME = "argument";
  private static final String NAME_1 = "arg";

  private final List<Tag> arguments;
  private final Map<String, Tag> namedArguments;
  private final TagResolver fallbackTagResolver;

  ArgumentTag(final @NotNull List<? extends ComponentLike> argumentComponents) {
    final List<Tag> argumentTags = new ArrayList<>(argumentComponents.size());
    final Map<String, Tag> namedArgumentMap = new HashMap<>(argumentComponents.size());
    final TagResolver.Builder tagResolverBuilder = TagResolver.builder();

    for (final ComponentLike argument : argumentComponents) {
      if (argument instanceof VirtualComponent) {
        final VirtualComponentRenderer<?> renderer = ((VirtualComponent) argument).renderer();

        if (renderer instanceof MiniMessageTranslatorArgument) {
          final MiniMessageTranslatorArgument<?> translatorArgument = (MiniMessageTranslatorArgument<?>) renderer;
          final Object data = translatorArgument.data();

          if (data instanceof TranslationArgumentLike) {
            final Tag tag = Tag.selfClosingInserting((TranslationArgumentLike) data);
            namedArgumentMap.put(translatorArgument.name(), tag);
            argumentTags.add(tag);
          } else if (data instanceof Tag) {
            final Tag tag = (Tag) data;
            namedArgumentMap.put(translatorArgument.name(), tag);
            argumentTags.add(tag);
          } else if (data instanceof TagResolver) {
            tagResolverBuilder.resolvers((TagResolver) data);
          }
        }
      } else {
        argumentTags.add(Tag.selfClosingInserting(argument));
      }
    }

    this.arguments = Collections.unmodifiableList(argumentTags);
    this.namedArguments = Collections.unmodifiableMap(namedArgumentMap);
    this.fallbackTagResolver = tagResolverBuilder.build();
  }

  @Override
  public @Nullable Tag resolve(final @NotNull String name, final @NotNull ArgumentQueue arguments, final @NotNull Context ctx) throws ParsingException {
    if (name.equals(NAME) || name.equals(NAME_1)) {
      final int index = arguments.popOr("No argument number provided").asInt().orElseThrow(() -> ctx.newException("Invalid argument number", arguments));

      if (index < 0 || index >= this.arguments.size()) {
        throw ctx.newException("Invalid argument number", arguments);
      }

      return this.arguments.get(index);
    } else {
      final Tag tag = this.namedArguments.get(name);

      if (tag != null) {
        return tag;
      }
    }

    // Fallback to user-provided tags.
    return this.fallbackTagResolver.resolve(name, arguments, ctx);
  }

  @Override
  public boolean has(final @NotNull String name) {
    return name.equals(NAME) || name.equals(NAME_1) || this.namedArguments.containsKey(name) || this.fallbackTagResolver.has(name);
  }
}
