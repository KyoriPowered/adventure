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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.parser.Element;
import net.kyori.examination.Examinable;
import net.kyori.examination.string.StringExaminer;

/**
 * A transformation that can be applied while parsing a message.
 *
 * <p>A transformation instance is created for each instance of a tag in a parsed string.</p>
 *
 * @see TransformationRegistry to access and register available transformations
 * @since 4.1.0
 */
public abstract class Transformation implements Examinable {
  private String name;
  protected Context context;

  protected Transformation() {
  }

  /**
   * Initialize this transformation with a tag name and tokens.
   *
   * @param name the alias for this transformation
   * @param args tokens within the tags, used to define arguments. Each
   * @since 4.1.0
   */
  public void load(final String name, final List<Element.TagPart> args) {
    this.name = name;
  }

  /**
   * The tag alias used to refer to this instance.
   *
   * @return the name
   * @since 4.1.0
   */
  public final String name() {
    return this.name;
  }

  /**
   * Return a transformed {@code component} based on the applied parameters.
   *
   * @return the transformed component
   * @since 4.1.0
   */
  public abstract Component apply();

  /**
   * Checks if this transformation is allowed to be interpreted inside a pre tag.
   *
   * @return if this transformation is allowed to be interpreted inside a pre tag
   * @since 4.1.0
   */
  public boolean allowedInPre() {
    return false;
  }

  void context(final Context context) {
    this.context = context;
  }

  @Override
  public final String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

  @Override
  public abstract boolean equals(final Object o);

  @Override
  public abstract int hashCode();

  protected Component merge(final Component target, final Component template) {
    Component result = target.style(target.style().merge(template.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET, Style.Merge.all()));

    if(template.hoverEvent() != null) {
      result = result.hoverEvent(template.hoverEvent());
    }
    if(template.clickEvent() != null) {
      result = result.clickEvent(template.clickEvent());
    }
    if(template.insertion() != null) {
      result = result.insertion(template.insertion());
    }

    return result;
  }
}
