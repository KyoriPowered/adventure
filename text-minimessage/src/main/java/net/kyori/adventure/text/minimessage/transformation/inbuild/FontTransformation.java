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

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.examination.ExaminableProperty;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

/**
 * A decoration that applies a font name.
 *
 * @since 4.10.0
 */
public final class FontTransformation extends Transformation {
  public static final String FONT = "font";

  private final Key font;

  /**
   * Create a new font transformation from a tag.
   *
   * @param name the tag name
   * @param args the tag arguments
   * @return a new transformation
   * @since 4.10.0
   */
  public static FontTransformation create(final String name, final List<TagPart> args) {
    final Key font;
    if (args.size() == 1) {
      @Subst("empty") final String fontKey = args.get(0).value();
      font = Key.key(fontKey);
    } else if (args.size() == 2) {
      @Subst(Key.MINECRAFT_NAMESPACE) final String namespaceKey = args.get(0).value();
      @Subst("empty") final String fontKey = args.get(1).value();
      font = Key.key(namespaceKey, fontKey);
    } else {
      throw new ParsingException("Don't know how to turn " + args + " into a font", args);
    }

    return new FontTransformation(font);
  }

  private FontTransformation(final Key font) {
    this.font = font;
  }

  @Override
  public Component apply() {
    return Component.empty().style(Style.style().font(this.font));
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("font", this.font));
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final FontTransformation that = (FontTransformation) other;
    return Objects.equals(this.font, that.font);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.font);
  }
}
