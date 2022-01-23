/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.builtin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;

/**
 * Insert a translation component into the result.
 *
 * @since 4.10.0
 */
public final class TranslatableTag {
  public static final String TRANSLATABLE_3 = "tr";
  public static final String TRANSLATABLE_2 = "translate";
  public static final String TRANSLATABLE = "lang";

  private TranslatableTag() {
  }

  static Tag create(final List<? extends Tag.Argument> args, final Context ctx) {
    if (args.isEmpty()) {
      throw ctx.newError("Doesn't know how to turn " + args + " into a translatable component", args);
    }

    final List<Component> with;
    if (args.size() > 1) {
      with = new ArrayList<>();
      for (final Tag.Argument in : args.subList(1, args.size())) {
        with.add(ctx.parse(in.value()));
      }
    } else {
      with = Collections.emptyList();
    }

    return Tag.inserting(Component.translatable(args.get(0).value(), with));
  }
}
