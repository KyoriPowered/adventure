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
 * A scoreboard selector component.
 */
public class SelectorComponent extends BaseComponent {

  /**
   * The selector pattern.
   */
  @Nonnull private final String pattern;

  public SelectorComponent(@Nonnull final String pattern) {
    this.pattern = pattern;
  }

  /**
   * Gets the selector pattern.
   *
   * @return the selector pattern
   */
  @Nonnull
  public String pattern() {
    return this.pattern;
  }

  @Nonnull
  @Override
  public Component copy() {
    final SelectorComponent that = new SelectorComponent(this.pattern);
    that.mergeStyle(this);
    for(final Component child : this.children()) {
      that.append(child);
    }
    return that;
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof SelectorComponent)) return false;
    if(!super.equals(other)) return false;
    final SelectorComponent that = (SelectorComponent) other;
    return Objects.equal(this.pattern, that.pattern);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), this.pattern);
  }

  @Override
  protected void populateToString(@Nonnull final Objects.ToStringHelper builder) {
    builder.add("pattern", this.pattern);
  }
}
