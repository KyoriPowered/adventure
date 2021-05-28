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
  public static final TransformationType<ColorTransformation> COLOR = new TransformationType<>(ColorTransformation::canParse, new ColorTransformation.Parser());
  public static final TransformationType<DecorationTransformation> DECORATION = new TransformationType<>(DecorationTransformation::canParse, new DecorationTransformation.Parser());
  public static final TransformationType<HoverTransformation> HOVER_EVENT = new TransformationType<>(HoverTransformation::canParse, new HoverTransformation.Parser());
  public static final TransformationType<ClickTransformation> CLICK_EVENT = new TransformationType<>(ClickTransformation::canParse, new ClickTransformation.Parser());
  public static final TransformationType<KeybindTransformation> KEYBIND = new TransformationType<>(KeybindTransformation::canParse, new KeybindTransformation.Parser());
  public static final TransformationType<TranslatableTransformation> TRANSLATABLE = new TransformationType<>(TranslatableTransformation::canParse, new TranslatableTransformation.Parser());
  public static final TransformationType<InsertionTransformation> INSERTION = new TransformationType<>(InsertionTransformation::canParse, new InsertionTransformation.Parser());
  public static final TransformationType<FontTransformation> FONT = new TransformationType<>(FontTransformation::canParse, new FontTransformation.Parser());
  public static final TransformationType<GradientTransformation> GRADIENT = new TransformationType<>(GradientTransformation::canParse, new GradientTransformation.Parser());
  public static final TransformationType<RainbowTransformation> RAINBOW = new TransformationType<>(RainbowTransformation::canParse, new RainbowTransformation.Parser());
  /**
   * Don't use.
   *
   * @deprecated since 4.2.0 this is handled at parser level
   */
  @Deprecated
  public static final TransformationType<ResetTransformation> RESET = new TransformationType<>(ResetTransformation::canParse, new ResetTransformation.Parser());
  /**
   * Don't use.
   *
   * @deprecated since 4.2.0 this is handled at parser level
   */
  @Deprecated
  public static final TransformationType<PreTransformation> PRE = new TransformationType<>(PreTransformation::canParse, new PreTransformation.Parser());

  final Predicate<String> canParse;
  final TransformationParser<T> parser;

  TransformationType(final Predicate<String> canParse, final TransformationParser<T> parser) {
    this.canParse = canParse;
    this.parser = parser;
  }
}
