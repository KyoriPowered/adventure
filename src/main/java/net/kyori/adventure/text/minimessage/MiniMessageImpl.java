/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.markdown.MarkdownFlavor;
import net.kyori.adventure.text.minimessage.markdown.MiniMarkdownParser;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * not public api.
 *
 * @since 4.0.0
 */
public class MiniMessageImpl implements MiniMessage {
  static final Function<String, ComponentLike> DEFAULT_PLACEHOLDER_RESOLVER = s -> null;
  static final Consumer<List<String>> DEFAULT_ERROR_CONSUMER = message -> message.forEach(System.out::println);

  static final MiniMessage INSTANCE = new MiniMessageImpl(false, MarkdownFlavor.defaultFlavor(), TransformationRegistry.standard(), DEFAULT_PLACEHOLDER_RESOLVER, false, null, DEFAULT_ERROR_CONSUMER);
  static final MiniMessage MARKDOWN = new MiniMessageImpl(true, MarkdownFlavor.defaultFlavor(), TransformationRegistry.standard(), DEFAULT_PLACEHOLDER_RESOLVER, false, null, DEFAULT_ERROR_CONSUMER);

  private final boolean markdown;
  private final MarkdownFlavor markdownFlavor;
  private final MiniMessageParser parser;
  private final boolean strict;
  private final Appendable debugOutput;
  private final Consumer<List<String>> parsingErrorMessageConsumer;

  MiniMessageImpl(final boolean markdown, final @NotNull MarkdownFlavor markdownFlavor, final @NotNull TransformationRegistry registry, final @NotNull Function<String, ComponentLike> placeholderResolver, final boolean strict, final Appendable debugOutput, final @NotNull Consumer<List<String>> parsingErrorMessageConsumer) {
    this.markdown = markdown;
    this.markdownFlavor = markdownFlavor;
    this.parser = new MiniMessageParser(registry, placeholderResolver);
    this.strict = strict;
    this.debugOutput = debugOutput;
    this.parsingErrorMessageConsumer = parsingErrorMessageConsumer;
  }

  @Override
  public @NotNull Component deserialize(@NotNull String input) {
    if (this.markdown) {
      input = MiniMarkdownParser.parse(input, this.markdownFlavor);
    }
    return this.parser.parseFormat(input, Context.of(this.strict, this.debugOutput, input, this));
  }

  @Override
  public @NotNull String serialize(final @NotNull Component component) {
    return MiniMessageSerializer.serialize(component);
  }

  @Override
  public @NotNull Component parse(@NotNull String input, final @NotNull String... placeholders) {
    if (this.markdown) {
      input = MiniMarkdownParser.parse(input, this.markdownFlavor);
    }
    return this.parser.parseFormat(input, Context.of(this.strict, this.debugOutput, input, this), placeholders);
  }

  @Override
  public @NotNull Component parse(@NotNull String input, final @NotNull Map<String, String> placeholders) {
    if (this.markdown) {
      input = MiniMarkdownParser.parse(input, this.markdownFlavor);
    }
    return this.parser.parseFormat(input, placeholders, Context.of(this.strict, this.debugOutput, input, this));
  }

  @Override
  public @NotNull Component parse(final @NotNull String input, final @NotNull Object... placeholders) {
    final List<Template> templates = new ArrayList<>();
    String key = null;
    for (int i = 0; i < placeholders.length; i++) {
      final Object object = placeholders[i];
      if (object instanceof Template) {
        // add as a template directly
        templates.add((Template) object);
      } else {
        // this is a `key=[string|component]` template
        if (key == null) {
          // get the key
          if (object instanceof String) {
            key = (String) object;
          } else {
            throw new IllegalArgumentException("Argument " + i + " in placeholders is key, must be String, was " + object.getClass().getName());
          }
        } else {
          // get the value
          if (object instanceof ComponentLike) {
            templates.add(Template.of(key, ((ComponentLike) object).asComponent()));
            key = null;
          } else if (object instanceof String) {
            templates.add(Template.of(key, (String) object));
            key = null;
          } else {
            throw new IllegalArgumentException("Argument " + i + " in placeholders is a value, must be Component or String, was " + object.getClass().getName());
          }
        }
      }
    }
    if (key != null) {
      throw new IllegalArgumentException("Found a key in placeholders that wasn't followed by a value: " + key);
    }
    return this.parse(input, templates);
  }

  @Override
  public @NotNull Component parse(@NotNull String input, final @NotNull Template... placeholders) {
    if (this.markdown) {
      input = MiniMarkdownParser.parse(input, this.markdownFlavor);
    }
    return this.parser.parseFormat(input, Context.of(this.strict, this.debugOutput, input, this, placeholders), placeholders);
  }

  @Override
  public @NotNull Component parse(@NotNull String input, final @NotNull List<Template> placeholders) {
    if (this.markdown) {
      input = MiniMarkdownParser.parse(input, this.markdownFlavor);
    }
    return this.parser.parseFormat(input, placeholders, Context.of(this.strict, this.debugOutput, input, this, placeholders.toArray(new Template[0])));
  }

  @Override
  public @NotNull String escapeTokens(final @NotNull String input) {
    return this.parser.escapeTokens(input);
  }

  @Override
  public @NotNull String stripTokens(@NotNull String input) {
    if (this.markdown) {
      input = MiniMarkdownParser.stripMarkdown(input, this.markdownFlavor);
    }
    return this.parser.stripTokens(input);
  }

  /**
   * not public api.
   *
   * @return huhu.
   * @since 4.1.0
   */
  public @NotNull Consumer<List<String>> parsingErrorMessageConsumer() {
    return this.parsingErrorMessageConsumer;
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  /* package */ static final class BuilderImpl implements Builder {
    private boolean markdown = false;
    private MarkdownFlavor markdownFlavor = MarkdownFlavor.defaultFlavor();
    private TransformationRegistry registry = TransformationRegistry.standard();
    private Function<String, ComponentLike> placeholderResolver = DEFAULT_PLACEHOLDER_RESOLVER;
    private boolean strict = false;
    private Appendable debug = null;
    private Consumer<List<String>> parsingErrorMessageConsumer = DEFAULT_ERROR_CONSUMER;

    BuilderImpl() {
    }

    BuilderImpl(final MiniMessageImpl serializer) {
      this.markdown = serializer.markdown;
    }

    @Override
    public @NotNull Builder markdown() {
      this.markdown = true;
      return this;
    }

    @Override
    public @NotNull Builder transformations(final TransformationRegistry transformationRegistry) {
      this.registry = transformationRegistry;
      return this;
    }

    @Override
    public @NotNull Builder markdownFlavor(final MarkdownFlavor markdownFlavor) {
      this.markdownFlavor = markdownFlavor;
      return this;
    }

    @Override
    public @NotNull Builder placeholderResolver(final Function<String, ComponentLike> placeholderResolver) {
      this.placeholderResolver = placeholderResolver;
      return this;
    }

    @Override
    public @NotNull Builder strict(final boolean strict) {
      this.strict = strict;
      return this;
    }

    @Override
    public @NotNull Builder debug(final Appendable debugOutput) {
      this.debug = debugOutput;
      return this;
    }

    @Override
    public @NotNull Builder parsingErrorMessageConsumer(final Consumer<List<String>> consumer) {
      this.parsingErrorMessageConsumer = consumer;
      return this;
    }

    @Override
    public @NotNull MiniMessage build() {
      if (this.markdown) {
        return new MiniMessageImpl(true, this.markdownFlavor, this.registry, this.placeholderResolver, this.strict, this.debug, this.parsingErrorMessageConsumer);
      } else {
        return new MiniMessageImpl(false, MarkdownFlavor.defaultFlavor(), this.registry, this.placeholderResolver, this.strict, this.debug, this.parsingErrorMessageConsumer);
      }
    }
  }
}
