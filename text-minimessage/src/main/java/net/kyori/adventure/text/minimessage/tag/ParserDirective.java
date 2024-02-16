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
package net.kyori.adventure.text.minimessage.tag;

import org.jetbrains.annotations.ApiStatus;

/**
 * Tags implementing this interface are used to provide directives, or instructions, to the parser directly.
 *
 * @see #RESET
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public /* sealed */ interface ParserDirective extends Tag {
  /**
   * Instructs the parser to reset all style, events, insertions, etc.
   *
   * <p>If {@link net.kyori.adventure.text.minimessage.MiniMessage.Builder#strict(boolean) strict mode} is enabled, usage of
   * this tag is disallowed and a parse exception will be thrown if this tag is present.</p>
   *
   * @since 4.10.0
   */
  Tag RESET = new ParserDirective() {
    @Override
    public String toString() {
      return "ParserDirective.RESET";
    }
  };
}
