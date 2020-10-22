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
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.kyori.adventure.text.minimessage.parser.Token;

public class TransformationRegistry {
  private final List<Entry<? extends Transformation>> transformations = new ArrayList<>();

  public TransformationRegistry() {
    this.register(ColorTransformation::new, ColorTransformation::isApplicable);
    this.register(DecorationTransformation::new, DecorationTransformation::isApplicable);
    this.register(HoverTransformation::new, HoverTransformation::isApplicable);
    this.register(ClickTransformation::new, ClickTransformation::isApplicable);
    this.register(KeybindTransformation::new, KeybindTransformation::isApplicable);
    this.register(TranslatableTransformation::new, TranslatableTransformation::isApplicable);
    this.register(InsertionTransformation::new, InsertionTransformation::isApplicable);
    this.register(FontTransformation::new, FontTransformation::isApplicable);
    this.register(GradientTransformation::new, GradientTransformation::isApplicable);
    this.register(RainbowTransformation::new, RainbowTransformation::isApplicable);
  }

  private <T extends Transformation> void register(final Supplier<T> factory, final Predicate<String> applicable) {
    this.transformations.add(new Entry<>(applicable, factory));
  }

  public Transformation get(final String name, final List<Token> inners) {
    for(final Entry<? extends Transformation> entry : this.transformations) {
      if(entry.applicable.test(name)) {
        final Transformation transformation = entry.factory.get();
        transformation.load(name, inners);
        return transformation;
      }
    }

    return null;
  }

  static class Entry<T extends Transformation> {
    final Predicate<String> applicable;
    final Supplier<T> factory;

    Entry(final Predicate<String> applicable, final Supplier<T> factory) {
      this.applicable = applicable;
      this.factory = factory;
    }
  }
}
