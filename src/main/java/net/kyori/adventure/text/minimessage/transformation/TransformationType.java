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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Tokens;
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

import static java.util.Objects.requireNonNull;

/**
 * Available types of transformation.
 *
 * @param <T> transformation class
 * @since 4.1.0
 */
public final class TransformationType<T extends Transformation> {

  public static final TransformationType<?> COLOR = transformationType(
    ColorTransformation::canParse,
    ColorTransformation::create
  );
  public static final TransformationType<?> DECORATION = transformationType(
    acceptingNames(
      Stream.of(TextDecoration.NAMES.keys(), DecorationTransformation.DECORATION_ALIASES.keySet())
        .flatMap(Collection::stream)
        .collect(Collectors.toSet())
    ),
    DecorationTransformation::create
  );
  public static final TransformationType<?> HOVER_EVENT = transformationType(
    acceptingNames(Tokens.HOVER),
    HoverTransformation::create
  );
  public static final TransformationType<?> CLICK_EVENT = transformationType(
    acceptingNames(Tokens.CLICK),
    ClickTransformation::create
  );
  public static final TransformationType<?> KEYBIND = transformationType(
    acceptingNames(Tokens.KEYBIND),
    KeybindTransformation::create
  );
  public static final TransformationType<?> TRANSLATABLE = transformationType(
    acceptingNames(Tokens.TRANSLATABLE, Tokens.TRANSLATABLE_2, Tokens.TRANSLATABLE_3),
    TranslatableTransformation::create
  );
  public static final TransformationType<?> INSERTION = transformationType(
    acceptingNames(Tokens.INSERTION),
    InsertionTransformation::create
  );
  public static final TransformationType<?> FONT = transformationType(
    acceptingNames(Tokens.FONT),
    FontTransformation::create
  );
  public static final TransformationType<?> GRADIENT = transformationType(
    acceptingNames(Tokens.GRADIENT),
    GradientTransformation::create
  );
  public static final TransformationType<?> RAINBOW = transformationType(
    acceptingNames(Tokens.RAINBOW),
    RainbowTransformation::create
  );
  /**
   * Don't use.
   *
   * @deprecated since 4.2.0 this is handled at parser level
   */
  @Deprecated
  public static final TransformationType<?> RESET = transformationType(
    acceptingNames(Tokens.RESET, Tokens.RESET_2),
    ResetTransformation::create
  );
  /**
   * Don't use.
   *
   * @deprecated since 4.2.0 this is handled at parser level
   */
  @Deprecated
  public static final TransformationType<?> PRE = transformationType(
    acceptingNames(Tokens.PRE),
    PreTransformation::create
  );

  final Predicate<String> canParse;
  final TransformationFactory<T> factory;

  /**
   * Constructs a new transformation type.
   *
   * @param canParse the predicate used to check if a tag can be parsed by this type
   * @param parser the parser that should be used to parse this type
   * @since 4.1.0
   * @deprecated for removal since 4.2.0, use {@link #transformationType(Predicate, TransformationFactory)} instead
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
  private TransformationType(final Predicate<String> canParse, final TransformationFactory<T> factory) {
    this.canParse = canParse;
    this.factory = factory;
  }

  /**
   * Create a new transformation type with dynamically determined names.
   *
   * <p>It is assumed that the {@code nameMatcher} function is side-effect free, meaning that any time
   * it is called for a certain input {@code x}, it will always return the same value, and not modify any other state.</p>
   *
   * <p>All input to the {@code nameMatcher} function will be lower-case in the {@code ROOT} locale.</p>
   *
   * @param nameMatcher the name matcher predicate
   * @param factory a factory
   * @param <T> transformation instance type
   * @return a new transformation type definition
   * @since 4.2.0
   */
  public static <T extends Transformation> TransformationType<T> transformationType(final Predicate<String> nameMatcher, final TransformationFactory<T> factory) {
    return new TransformationType<>(
      requireNonNull(nameMatcher, "nameMatcher"),
      requireNonNull(factory, "factory")
    );
  }

  /**
   * Create a new transformation type that can be parsed without performing recursive parses.
   *
   * @param nameMatcher filter for tag names to apply this transformation to
   * @param contextFreeFactory the context-free factory
   * @param <T> transformation instance type
   * @return a new transformation type definition
   * @see #transformationType(Predicate, TransformationFactory)
   * @since 4.2.0
   */
  public static <T extends Transformation> TransformationType<T> transformationType(final Predicate<String> nameMatcher, final TransformationFactory.ContextFree<T> contextFreeFactory) {
    return new TransformationType<>(
      requireNonNull(nameMatcher, "nameMatcher"),
      requireNonNull(contextFreeFactory, "contextFreeFactory")
    );
  }

  /**
   * Create a name matcher function that will accept the provided lowercase tag names.
   *
   * @param elements the accepted names
   * @return a name matcher function
   * @since 4.2.0
   */
  public static Predicate<String> acceptingNames(final String... elements) {
    return acceptingNames(Arrays.asList(elements));
  }

  /**
   * Create a name matcher function that will accept the provided lowercase tag names.
   *
   * @param elements the accepted names
   * @return a name matcher function
   * @since 4.2.0
   */
  public static Predicate<String> acceptingNames(final Collection<String> elements) {
    if (elements.size() == 1) {
      final String name = elements.iterator().next().toLowerCase(Locale.ROOT);
      return tag -> tag.equals(name);
    } else {
      final Set<String> names = new HashSet<>(elements.size());
      for (final String name : elements) {
        names.add(name.toLowerCase(Locale.ROOT));
      }
      return names::contains;
    }
  }
}
