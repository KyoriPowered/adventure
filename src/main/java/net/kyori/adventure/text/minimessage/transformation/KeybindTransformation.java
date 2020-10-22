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

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

public class KeybindTransformation extends Transformation {
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.KEYBIND);
  }

  private String keybind;

  @Override
  public void load(final String name, final List<Token> args) {
    super.load(name, args);

    if(Token.oneString(args)) {
      this.keybind = args.get(0).value();
    }
  }

  @Override
  public Component apply(final Component component, final TextComponent.Builder parent) {
    parent.append(Component.keybind(this.keybind).mergeStyle(component));
    return component;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("keybind", this.keybind));
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final KeybindTransformation that = (KeybindTransformation) other;
    return Objects.equals(this.keybind, that.keybind);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.keybind);
  }
}
