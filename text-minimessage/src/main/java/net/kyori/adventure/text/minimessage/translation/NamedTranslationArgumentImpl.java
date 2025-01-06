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

import java.util.Objects;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.minimessage.internal.TagInternals;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import org.jetbrains.annotations.NotNull;

final class NamedTranslationArgumentImpl implements NamedTranslationArgument {

  private final @TagPattern @NotNull String name;
  private final @NotNull TranslationArgument argument;

  NamedTranslationArgumentImpl(final @TagPattern @NotNull String name, final @NotNull TranslationArgument argument) {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(argument, "argument");
    TagInternals.assertValidTagName(name);

    this.name = name;
    this.argument = argument;
  }

  @Override
  public @TagPattern @NotNull String name() {
    return this.name;
  }

  @Override
  public @NotNull TranslationArgument asTranslationArgument() {
    return this.argument;
  }
}
