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
package net.kyori.adventure.text.minimessage.transformation.inbuild;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.Element;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.intellij.lang.annotations.Subst;

/**
 * A decoration that applies a font name.
 *
 * @since 4.1.0
 */
public final class FontTransformation extends Transformation {
  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.1.0
   */
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.FONT);
  }

  private Key font;

  private FontTransformation() {
  }

  @Override
  public void load(final String name, final List<Element.TagPart> args) {
    super.load(name, args);

    if(args.size() == 1) {
      @Subst("minecraft:empty") String fontKey = args.get(0).getValue();
      this.font = Key.key(fontKey);
    }

    if(args.size() != 2) {
      throw new ParsingException("Doesn't know how to turn " + args + " into a click event", -1);
    }

    @Subst(Key.MINECRAFT_NAMESPACE) String namespaceKey = args.get(0).getValue();
    @Subst("empty") String fontKey = args.get(1).getValue();
    this.font = Key.key(namespaceKey, fontKey);
  }

  @Override
  public Component apply() {
    return Component.empty().style(Style.style().font(this.font));
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("font", this.font));
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final FontTransformation that = (FontTransformation) other;
    return Objects.equals(this.font, that.font);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.font);
  }

  /**
   * Factory for {@link FontTransformation} instances.
   *
   * @since 4.1.0
   */
  public static class Parser implements TransformationParser<FontTransformation> {
    @Override
    public FontTransformation parse() {
      return new FontTransformation();
    }
  }
}
