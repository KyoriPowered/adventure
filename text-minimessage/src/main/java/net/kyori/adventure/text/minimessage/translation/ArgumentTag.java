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
import java.util.Objects;
import net.kyori.adventure.text.ComponentLike;
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

  private final List<? extends ComponentLike> argumentComponents;
  private final Map<String, ComponentLike> namedArguments;

  ArgumentTag(final @NotNull List<? extends ComponentLike> argumentComponents) {
    this.argumentComponents = new ArrayList<>(Objects.requireNonNull(argumentComponents, "argumentComponents"));

    final Map<String, ComponentLike> namedArgumentMap = new HashMap<>(this.argumentComponents.size());
    for (final ComponentLike argument : this.argumentComponents) {
      if (argument instanceof VirtualComponent) {
        final VirtualComponentRenderer<?> renderer = ((VirtualComponent) argument).renderer();

        if (renderer instanceof NamedTranslationArgument) {
          final NamedTranslationArgument namedArgument = (NamedTranslationArgument) argument;
          namedArgumentMap.put(namedArgument.name(), namedArgument.translationArgument());
        }
      }
    }

    this.namedArguments = Collections.unmodifiableMap(namedArgumentMap);
  }

  @Override
  public @Nullable Tag resolve(final @NotNull String name, final @NotNull ArgumentQueue arguments, final @NotNull Context ctx) throws ParsingException {
    if (name.equals(NAME) || name.equals(NAME_1)) {
      final int index = arguments.popOr("No argument number provided").asInt().orElseThrow(() -> ctx.newException("Invalid argument number", arguments));

      if (index < 0 || index >= this.argumentComponents.size()) {
        throw ctx.newException("Invalid argument number", arguments);
      }

      return Tag.inserting(this.argumentComponents.get(index));
    } else {
      final ComponentLike namedArgument = this.namedArguments.get(name);

      if (namedArgument != null) {
        return Tag.inserting(namedArgument);
      } else {
        return null;
      }
    }
  }

  @Override
  public boolean has(final @NotNull String name) {
    return name.equals(NAME) || name.equals(NAME_1) || this.namedArguments.containsKey(name);
  }
}
