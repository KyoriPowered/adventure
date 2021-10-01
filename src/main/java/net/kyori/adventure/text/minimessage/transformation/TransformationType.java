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

import java.util.function.Predicate;
import net.kyori.adventure.text.minimessage.transformation.inbuild.ClickTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.ColorTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.DecorationTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.FontTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.GradientTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.HoverTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.InsertionTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.KeybindTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.PreTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.RainbowTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.ResetTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.TranslatableTransformation;

/**
 * Available types of transformation.
 *
 * @param <T> transformation class
 * @since 4.1.0
 */
public final class TransformationType<T extends Transformation> {
  public static final TransformationType<ColorTransformation> COLOR = new TransformationType<>(ColorTransformation::canParse, ColorTransformation::create);
  public static final TransformationType<DecorationTransformation> DECORATION = new TransformationType<>(DecorationTransformation::canParse, DecorationTransformation::create);
  public static final TransformationType<HoverTransformation> HOVER_EVENT = new TransformationType<>(HoverTransformation::canParse, HoverTransformation::create);
  public static final TransformationType<ClickTransformation> CLICK_EVENT = new TransformationType<>(ClickTransformation::canParse, ClickTransformation::create);
  public static final TransformationType<KeybindTransformation> KEYBIND = new TransformationType<>(KeybindTransformation::canParse, KeybindTransformation::create);
  public static final TransformationType<TranslatableTransformation> TRANSLATABLE = new TransformationType<>(TranslatableTransformation::canParse, TranslatableTransformation::create);
  public static final TransformationType<InsertionTransformation> INSERTION = new TransformationType<>(InsertionTransformation::canParse, InsertionTransformation::create);
  public static final TransformationType<FontTransformation> FONT = new TransformationType<>(FontTransformation::canParse, FontTransformation::create);
  public static final TransformationType<GradientTransformation> GRADIENT = new TransformationType<>(GradientTransformation::canParse, GradientTransformation::create);
  public static final TransformationType<RainbowTransformation> RAINBOW = new TransformationType<>(RainbowTransformation::canParse, RainbowTransformation::create);
  /**
   * Don't use.
   *
   * @deprecated since 4.2.0 this is handled at parser level
   */
  @Deprecated
  public static final TransformationType<ResetTransformation> RESET = new TransformationType<>(ResetTransformation::canParse, ResetTransformation::create);
  /**
   * Don't use.
   *
   * @deprecated since 4.2.0 this is handled at parser level
   */
  @Deprecated
  public static final TransformationType<PreTransformation> PRE = new TransformationType<>(PreTransformation::canParse, PreTransformation::create);

  final Predicate<String> canParse;
  final TransformationFactory<T> factory;

  /**
   * Constructs a new transformation type.
   *
   * @param canParse the predicate used to check if a tag can be parsed by this type
   * @param parser the parser that should be used to parse this type
   * @since 4.1.0
   */
  @Deprecated
  public TransformationType(final Predicate<String> canParse, final TransformationParser<T> parser) {
    this.canParse = canParse;
    this.factory = (ctx, name, args) -> {
      final T ret = parser.parse();
      ret.context(ctx);
      ret.load(name, args);
      return ret;
    };
  }

  /**
   * Constructs a new transformation type.
   *
   * @param canParse the predicate used to check if a tag can be parsed by this type
   * @param factory the factory that should be used to create this type
   * @since 4.2.0
   */
  public TransformationType(final Predicate<String> canParse, final TransformationFactory<T> factory) {
    this.canParse = canParse;
    this.factory = factory;
  }

  /**
   * Create a new transformation type that can be parsed without performing recursive parses.
   *
   * @param nameMatcher filter for tag names to apply this transformation to
   * @param contextFree the context-free factory
   * @since 4.2.0
   */
  public TransformationType(final Predicate<String> nameMatcher, final TransformationFactory.ContextFree<T> contextFree) {
    this.canParse = nameMatcher;
    this.factory = contextFree;
  }
}
