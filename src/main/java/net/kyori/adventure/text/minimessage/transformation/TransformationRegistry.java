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
import net.kyori.adventure.text.minimessage.parser.Token;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TransformationRegistry {
  private final List<Entry<? extends Transformation>> transformations = new ArrayList<>();

  public TransformationRegistry() {
    this.register(ColorTransformation::canParse, ColorTransformation::new);
    this.register(DecorationTransformation::canParse, DecorationTransformation::new);
    this.register(HoverTransformation::canParse, HoverTransformation::new);
    this.register(ClickTransformation::canParse, ClickTransformation::new);
    this.register(KeybindTransformation::canParse, KeybindTransformation::new);
    this.register(TranslatableTransformation::canParse, TranslatableTransformation::new);
    this.register(InsertionTransformation::canParse, InsertionTransformation::new);
    this.register(FontTransformation::canParse, FontTransformation::new);
    this.register(GradientTransformation::canParse, GradientTransformation::new);
    this.register(RainbowTransformation::canParse, RainbowTransformation::new);
  }

  private <T extends Transformation> void register(final Predicate<String> canParse, final TransformationParser<T> parser) {
    this.transformations.add(new Entry<>(canParse, parser));
  }

  public @Nullable Transformation get(final String name, final List<Token> inners) {
    for(final Entry<? extends Transformation> entry : this.transformations) {
      if(entry.canParse.test(name)) {
        final Transformation transformation = entry.parser.parse();
        transformation.load(name, inners);
        return transformation;
      }
    }

    return null;
  }

  static class Entry<T extends Transformation> {
    final Predicate<String> canParse;
    final TransformationParser<T> parser;

    Entry(final Predicate<String> canParse, final TransformationParser<T> parser) {
      this.canParse = canParse;
      this.parser = parser;
    }
  }
}
