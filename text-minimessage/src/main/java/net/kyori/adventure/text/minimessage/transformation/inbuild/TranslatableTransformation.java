/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage.transformation.inbuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.transformation.Inserting;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Insert a translation component into the result.
 *
 * @since 4.10.0
 */
public final class TranslatableTransformation extends Transformation implements Inserting {
  public static final String TRANSLATABLE_3 = "tr";
  public static final String TRANSLATABLE_2 = "translate";
  public static final String TRANSLATABLE = "lang";

  /**
   * Create a new translatable transformation from a tag.
   *
   * @param name the tag name
   * @param args the tag arguments
   * @return a new transformation
   * @since 4.10.0
   */
  public static TranslatableTransformation create(final Context ctx, final String name, final List<TagPart> args) {
    if (args.isEmpty()) {
      throw new ParsingException("Doesn't know how to turn " + args + " into a translatable component", args);
    }

    final List<Component> with;
    if (args.size() > 1) {
      with = new ArrayList<>();
      for (final TagPart in : args.subList(1, args.size())) {
        with.add(ctx.parse(in.value()));
      }
    } else {
      with = Collections.emptyList();
    }

    return new TranslatableTransformation(args.get(0).value(), with);
  }

  private final String key;
  private final List<Component> inners;

  private TranslatableTransformation(final String key, final List<Component> with) {
    this.key = key;
    this.inners = with;
  }

  @Override
  public Component apply() {
    if (this.inners.isEmpty()) {
      return Component.translatable(this.key);
    } else {
      return Component.translatable(this.key, this.inners);
    }
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("key", this.key),
      ExaminableProperty.of("inners", this.inners)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final TranslatableTransformation that = (TranslatableTransformation) other;
    return Objects.equals(this.key, that.key)
      && Objects.equals(this.inners, that.inners);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.key, this.inners);
  }
}
