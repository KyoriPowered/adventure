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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

/* package */ class MiniMessageImpl implements MiniMessage {

  /* package */ static final MiniMessage INSTANCE = new MiniMessageImpl(false);
  /* package */ static final MiniMessage MARKDOWN = new MiniMessageImpl(true);

  private final boolean markdown;

  MiniMessageImpl(boolean markdown) {
    this.markdown = markdown;
  }

  @Override
  public @NonNull Component deserialize(@NonNull String input) {
    if (markdown) {
      input = MiniMarkdownParser.parse(input);
    }
    return MiniMessageParser.parseFormat(input);
  }


  @Override
  public @NonNull String serialize(@NonNull Component component) {
    return MiniMessageSerializer.serialize(component);
  }

  @Override
  public @NonNull Component parse(@NonNull String input, @NonNull String... placeholders) {
    if (markdown) {
      input = MiniMarkdownParser.parse(input);
    }
    return MiniMessageParser.parseFormat(input, placeholders);
  }

  @Override
  public @NonNull Component parse(@NonNull String input, @NonNull Map<String, String> placeholders) {
    if (markdown) {
      input = MiniMarkdownParser.parse(input);
    }
    return MiniMessageParser.parseFormat(input, placeholders);
  }

  @Override
  public @NonNull Component parse(@NonNull String input, @NonNull Template... placeholders) {
    if (markdown) {
      input = MiniMarkdownParser.parse(input);
    }
    return MiniMessageParser.parseFormat(input, placeholders);
  }

  @Override
  public @NonNull Component parse(@NonNull String input, @NonNull List<Template> placeholders) {
    if (markdown) {
      input = MiniMarkdownParser.parse(input);
    }
    return MiniMessageParser.parseFormat(input, placeholders);
  }

  @Override
  public @NonNull String escapeTokens(@NonNull String input) {
    return MiniMessageParser.escapeTokens(input);
  }

  @Override
  public @NonNull String stripTokens(@NonNull String input) {
    if (markdown) {
      input = MiniMarkdownParser.stripMarkdown(input);
    }
    return MiniMessageParser.stripTokens(input);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  /* package */ static final class BuilderImpl implements Builder {
    private boolean markdown = false;

    BuilderImpl() {
    }

    BuilderImpl(final MiniMessageImpl serializer) {
      this.markdown = serializer.markdown;
    }


    @Override
    public @NonNull Builder markdown() {
      this.markdown = true;
      return this;
    }

    @Override
    public @NonNull MiniMessage build() {
      return this.markdown ? MARKDOWN : INSTANCE;
    }
  }
}
