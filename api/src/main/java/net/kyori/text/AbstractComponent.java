/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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

import net.kyori.text.format.Style;
import net.kyori.text.util.ToStringer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An abstract implementation of a text component.
 */
public abstract class AbstractComponent implements Component {
  /**
   * An empty, unmodifiable, list of components.
   */
  protected static final List<Component> EMPTY_COMPONENT_LIST = Collections.emptyList();
  /**
   * The list of children.
   */
  protected final List<Component> children;
  /**
   * The style of this component.
   */
  protected final Style style;

  protected AbstractComponent(final @NonNull List<Component> children, final @NonNull Style style) {
    this.children = Collections.unmodifiableList(new ArrayList<>(children));
    this.style = style;
  }

  @Override
  public @NonNull List<Component> children() {
    return this.children;
  }

  @Override
  public @NonNull Style style() {
    return this.style;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof AbstractComponent)) return false;
    return this.equals((AbstractComponent) other);
  }

  protected boolean equals(final @NonNull AbstractComponent that) {
    return Objects.equals(this.children, that.children)
      && Objects.equals(this.style, that.style);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.children, this.style);
  }

  @Override
  public @NonNull String toString() {
    final Map<String, Object> builder = new LinkedHashMap<>();
    this.populateToString(builder);
    builder.put("children", this.children);
    builder.put("style", this.style);
    return ToStringer.toString(this, builder);
  }

  protected void populateToString(final @NonNull Map<String, Object> builder) {
  }
}
