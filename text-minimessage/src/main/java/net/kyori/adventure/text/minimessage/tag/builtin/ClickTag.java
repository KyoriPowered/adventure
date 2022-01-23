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

import java.util.List;
import java.util.Locale;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Click events.
 *
 * @since 4.10.0
 */
@ApiStatus.Internal
public final class ClickTag {
  public static final String CLICK = "click";

  private ClickTag() {
  }

  static Tag create(final List<? extends Tag.Argument> args, final Context ctx) {
    if (args.size() != 2) {
      throw ctx.newError("Don't know how to turn " + args + " into a click event", args);
    }
    final ClickEvent.@Nullable Action action = ClickEvent.Action.NAMES.value(args.get(0).value().toLowerCase(Locale.ROOT));
    final String value = args.get(1).value();
    if (action == null) {
      throw ctx.newError("Unknown click event action '" + args.get(0).value() + "'", args);
    }

    return Tag.styling(ClickEvent.clickEvent(action, value));
  }
}
