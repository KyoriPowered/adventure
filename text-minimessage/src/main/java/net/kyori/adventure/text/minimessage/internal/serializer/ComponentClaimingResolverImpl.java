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
package net.kyori.adventure.text.minimessage.internal.serializer;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ComponentClaimingResolverImpl implements TagResolver, SerializableResolver.Single {
  private final @NotNull Set<String> names;
  private final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler;
  private final @NotNull Function<Component, @Nullable Emitable> componentClaim;

  ComponentClaimingResolverImpl(final Set<String> names, final BiFunction<ArgumentQueue, Context, Tag> handler, final Function<Component, @Nullable Emitable> componentClaim) {
    this.names = names;
    this.handler = handler;
    this.componentClaim = componentClaim;
  }

  @Override
  public @Nullable Tag resolve(final @NotNull String name, final @NotNull ArgumentQueue arguments, final @NotNull Context ctx) throws ParsingException {
    if (!this.names.contains(name)) return null;

    return this.handler.apply(arguments, ctx);
  }

  @Override
  public boolean has(final @NotNull String name) {
    return this.names.contains(name);
  }

  @Override
  public @Nullable Emitable claimComponent(final @NotNull Component component) {
    return this.componentClaim.apply(component);
  }
}
