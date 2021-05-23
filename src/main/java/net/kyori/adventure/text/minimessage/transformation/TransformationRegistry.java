/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage.transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.parser.Element;
import net.kyori.adventure.text.minimessage.parser.ElementNode;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.transformation.inbuild.TemplateTransformation;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A registry of transformation types understood by the MiniMessage parser.
 *
 * @since 4.1.0
 */
public final class TransformationRegistry {

  public static final TransformationRegistry EMPTY = new TransformationRegistry();

  static {
    EMPTY.clear();
  }

  private final List<TransformationType<? extends Transformation>> types = new ArrayList<>();

  /**
   * Create a transformation registry with default transformations.
   *
   * @since 4.1.0
   */
  public TransformationRegistry() {
    this.register(TransformationType.COLOR);
    this.register(TransformationType.DECORATION);
    this.register(TransformationType.HOVER_EVENT);
    this.register(TransformationType.CLICK_EVENT);
    this.register(TransformationType.KEYBIND);
    this.register(TransformationType.TRANSLATABLE);
    this.register(TransformationType.INSERTION);
    this.register(TransformationType.FONT);
    this.register(TransformationType.GRADIENT);
    this.register(TransformationType.RAINBOW);
    this.register(TransformationType.RESET);
    this.register(TransformationType.PRE);
  }

  /**
   * Create a transformation registry with only the specified transformation types.
   *
   * @param types known transformation types
   * @since 4.1.0
   */
  @SafeVarargs
  public TransformationRegistry(final TransformationType<? extends Transformation>... types) {
    for(final TransformationType<? extends Transformation> type : types) {
      this.register(type);
    }
  }

  /**
   * Remove all entries from this registry.
   *
   * @since 4.1.0
   */
  public void clear() {
    this.types.clear();
  }

  /**
   * Register a new transformation type.
   *
   * @param type the type of transformation to register
   * @param <T> transformation
   * @since 4.1.0
   */
  public <T extends Transformation> void register(final TransformationType<T> type) {
    this.types.add(type);
  }

  /**
   * Get a transformation from this registry based on the current state.
   *
   * @param name tag name
   * @param inners tokens that make up the tag arguments
   * @param templates available templates
   * @param placeholderResolver function to resolve other component types
   * @param context the debug context
   * @return a possible transformation
   * @since 4.1.0
   */
  public @Nullable Transformation get(final String name, final List<Element.TagPart> inners, final Map<String, Template.ComponentTemplate> templates, final Function<String, ComponentLike> placeholderResolver, final Context context) {
    // first try if we have a custom placeholder resolver
    final ComponentLike potentialTemplate = placeholderResolver.apply(name);
    if(potentialTemplate != null) {
      return this.tryLoad(new TemplateTransformation(new Template.ComponentTemplate(name, potentialTemplate.asComponent())), name, inners, context);
    }
    // then check our registry
    for(final TransformationType<? extends Transformation> type : this.types) {
      if(type.canParse.test(name)) {
        return this.tryLoad(type.parser.parse(), name, inners, context);
      } else if(templates.containsKey(name)) {
        return this.tryLoad(new TemplateTransformation(templates.get(name)), name, inners, context);
      }
    }

    return null;
  }

  private Transformation tryLoad(final Transformation transformation, final String name, final List<Element.TagPart> inners, final Context context) {
    try {
      transformation.context(context);
      transformation.load(name, inners.subList(1, inners.size()));
      return transformation;
    } catch(final ParsingException exception) {
      if(context.isStrict()) {
        throw exception;
      }
      // TODO nicer message format?
      final List<String> errorMessage = new ArrayList<>(Arrays.asList(
              "[MiniMessage] Encountered parse exception while trying to load " + transformation.getClass().getSimpleName(),
              "\tmsg=" + exception.getMessage(),
              "\twith name=" + name + " and inners=" + inners + "",
              "\tinput=" + context.ogMessage()
      ));
      if(context.replacedMessage() != null) {
        errorMessage.add("\twith placeholders=" + context.replacedMessage());
      }
      if(inners != null && inners.isEmpty()) {
        errorMessage.add("\thint: did you mean to enter '</" + name + ">'?");
      }
      context.miniMessage().parsingErrorMessageConsumer().accept(errorMessage);
      return null;
    }
  }

  /**
   * Test if any registered transformation type matches the provided key.
   *
   * @param name tag name
   * @return whether any transformation exists
   * @since 4.1.0
   */
  public boolean exists(final String name) {
    for(final TransformationType<? extends Transformation> type : this.types) {
      if(type.canParse.test(name)) {
        return true;
      }
    }
    return false;
  }
}
