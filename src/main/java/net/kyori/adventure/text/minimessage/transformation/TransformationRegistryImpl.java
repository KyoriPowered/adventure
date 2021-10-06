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
package net.kyori.adventure.text.minimessage.transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import net.kyori.adventure.text.minimessage.transformation.inbuild.TemplateTransformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TransformationRegistryImpl implements TransformationRegistry {

  private static final List<TransformationType<? extends Transformation>> DEFAULT_TRANSFORMATIONS = new ArrayList<>();

  static final TransformationRegistry EMPTY;
  static final TransformationRegistry STANDARD;

  static {
    DEFAULT_TRANSFORMATIONS.add(TransformationType.COLOR);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.DECORATION);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.HOVER_EVENT);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.CLICK_EVENT);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.KEYBIND);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.TRANSLATABLE);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.INSERTION);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.FONT);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.GRADIENT);
    DEFAULT_TRANSFORMATIONS.add(TransformationType.RAINBOW);

    EMPTY = new TransformationRegistryImpl(Collections.emptyList());
    STANDARD = TransformationRegistry.builder().build();
  }

  private final List<TransformationType<? extends Transformation>> types;

  /**
   * Create a transformation registry with the specified transformation types.
   *
   * @param types known transformation types
   * @since 4.1.0
   */
  TransformationRegistryImpl(final List<TransformationType<? extends Transformation>> types) {
    this.types = Collections.unmodifiableList(types);
  }

  @SuppressWarnings("deprecation")
  private Transformation tryLoad(final TransformationFactory<?> factory, final String name, final List<TagPart> inners, final Context context) {
    try {
      final Transformation xform = factory.parse(context, name, inners.subList(1, inners.size()));
      // initialize fields for accessing deprecated content
      xform.context(context);
      xform.load(name, inners.subList(1, inners.size()));
      return xform;
    } catch (final ParsingException exception) {
      exception.originalText(context.originalMessage());
      throw exception;
    }
  }

  @Override
  public @Nullable Transformation get(final String name, final List<TagPart> inners, final TemplateResolver templateResolver, final Context context) {
    // first try if we have a custom placeholder resolver
    final Template template = templateResolver.resolve(name);
    if (template != null) {
      // The parser handles StringTemplates
      if (template instanceof Template.ComponentTemplate) {
        return this.tryLoad(TemplateTransformation.factory(new Template.ComponentTemplate(name, ((Template.ComponentTemplate) template).value())), name, inners, context);
      }
    }
    // then check our registry
    for (final TransformationType<? extends Transformation> type : this.types) {
      if (type.canParse.test(name)) {
        return this.tryLoad(type.factory, name, inners, context);
      }
    }

    return null;
  }

  @Override
  public boolean exists(final String name, final TemplateResolver templateResolver) {
    // first check the placeholder resolver
    if (templateResolver.canResolve(name)) {
      return true;
    }
    // then check registry
    for (final TransformationType<? extends Transformation> type : this.types) {
      if (type.canParse.test(name)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public @NotNull TransformationRegistry.Builder toBuilder() {
    return new TransformationRegistryImpl.BuilderImpl(this);
  }

  static final class BuilderImpl implements TransformationRegistry.Builder {

    private final List<TransformationType<? extends Transformation>> types;

    BuilderImpl() {
      this.types = new ArrayList<>(DEFAULT_TRANSFORMATIONS);
    }

    BuilderImpl(final TransformationRegistryImpl registry) {
      this.types = new ArrayList<>(registry.types);
    }

    @Override
    public @NotNull Builder clear() {
      this.types.clear();
      return this;
    }

    @Override
    public @NotNull Builder add(final @NotNull TransformationType<? extends Transformation> transformation) {
      this.types.add(transformation);
      return this;
    }

    @SafeVarargs
    @Override
    public final @NotNull Builder add(final @NotNull TransformationType<? extends Transformation>... transformations) {
      Collections.addAll(this.types, transformations);
      return this;
    }

    @Override
    public @NotNull TransformationRegistry build() {
      return new TransformationRegistryImpl(this.types);
    }
  }

}
