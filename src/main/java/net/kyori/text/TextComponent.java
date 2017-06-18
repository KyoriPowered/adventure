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

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A plain text component.
 */
public class TextComponent extends BaseComponent {

  /**
   * The plain text content.
   */
  @Nonnull private final String content;

  public TextComponent(@Nonnull final String content) {
    this.content = content;
  }

  /**
   * Gets the plain text content.
   *
   * @return the plain text content
   */
  @Nonnull
  public String content() {
    return this.content;
  }

  @Nonnull
  @Override
  public Component copy() {
    final TextComponent that = new TextComponent(this.content);
    that.mergeStyle(this);
    for(final Component child : this.children()) {
      that.append(child);
    }
    return that;
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof TextComponent)) return false;
    if(!super.equals(other)) return false;
    final TextComponent component = (TextComponent) other;
    return Objects.equal(this.content, component.content);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), this.content);
  }

  @Override
  protected void populateToString(@Nonnull final Objects.ToStringHelper builder) {
    builder.add("content", this.content);
  }
}
