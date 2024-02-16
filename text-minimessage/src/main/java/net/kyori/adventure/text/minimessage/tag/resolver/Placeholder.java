/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.resolver;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Tag resolvers producing tags that insert fixed values.
 *
 * <p>These are effectively placeholders.</p>
 *
 * @since 4.10.0
 */
public final class Placeholder {
  private Placeholder() {
  }

  /**
   * Creates a placeholder that inserts a MiniMessage string.
   *
   * <p>The inserted string will impact the rest of the parse process.</p>
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  public static TagResolver.@NotNull Single parsed(@TagPattern final @NotNull String key, final @NotNull String value) {
    return TagResolver.resolver(key, Tag.preProcessParsed(value));
  }

  /**
   * Creates a placeholder that inserts a literal string, without attempting to parse any contained tags.
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  public static TagResolver.@NotNull Single unparsed(@TagPattern final @NotNull String key, final @NotNull String value) {
    requireNonNull(value, "value");
    return Placeholder.component(key, Component.text(value));
  }

  /**
   * Creates a replacement that inserts a component.
   *
   * <p>This replacement is auto-closing, so its style will not influence the style of following components.</p>
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  public static TagResolver.@NotNull Single component(@TagPattern final @NotNull String key, final @NotNull ComponentLike value) {
    return TagResolver.resolver(key, Tag.selfClosingInserting(value));
  }

  /**
   * Creates a style tag which will modify the style of the component.
   *
   * <p>This style can be used like other styles.</p>
   *
   * @param key the key
   * @param style the style
   * @return the placeholder
   * @since 4.13.0
   */
  public static TagResolver.@NotNull Single styling(@TagPattern final @NotNull String key, final @NotNull StyleBuilderApplicable@NotNull... style) {
    return TagResolver.resolver(key, Tag.styling(style));
  }
}
