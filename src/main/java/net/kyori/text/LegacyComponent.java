/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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
package net.kyori.text;

import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Deprecated
public final class LegacyComponent {

  private static final char CHARACTER = '\u00A7';

  private LegacyComponent() {
  }

  @Nonnull
  public static String to(@Nonnull final Component component) {
    final StringBuilder state = new StringBuilder();
    to(state, component);
    return state.toString();
  }

  private static void to(@Nonnull final StringBuilder sb, @Nonnull final Component component) {
    @Nullable final TextColor color = component.color();
    if(color != null) {
      sb.append(CHARACTER).append(color.legacy());
    }

    for(final TextDecoration decoration : component.decorations()) {
      sb.append(CHARACTER).append(decoration.legacy());
    }

    if(component instanceof TextComponent) {
      sb.append(((TextComponent) component).content());
    }

    for(final Component child : component.children()) {
      to(sb, child);
    }
  }
}
