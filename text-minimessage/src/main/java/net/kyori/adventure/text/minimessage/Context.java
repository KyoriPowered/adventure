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
package net.kyori.adventure.text.minimessage;

import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;

/**
 * Parser context for use within transformations.
 *
 * @since 4.10.0
 */
public interface Context {
  /**
   * Returns strict mode.
   *
   * @return if strict mode is enabled
   * @since 4.10.0
   */
  boolean strict();

  /**
   * Returns the appendable to print debug output to.
   *
   * @return the debug output to print to
   * @since 4.10.0
   */
  Appendable debugOutput();

  /**
   * Returns the root element.
   *
   * @return root
   * @since 4.10.0
   */
  ElementNode tokens();

  /**
   * Returns original message.
   *
   * @return ogMessage
   * @since 4.10.0
   */
  String originalMessage();

  /**
   * Returns replaced message.
   *
   * @return replacedMessage
   * @since 4.10.0
   */
  String replacedMessage();

  /**
   * Returns the placeholder resolver.
   *
   * @return the placeholder resolver
   * @since 4.10.0
   */
  PlaceholderResolver placeholderResolver();

  /**
   * Returns callback ran at the end of parsing which could be used to compact the output.
   *
   * @return Post-processing function
   * @since 4.10.0
   */
  UnaryOperator<Component> postProcessor();

  /**
   * Parses a MiniMessage using all the settings of this context, including placeholders.
   *
   * @param message the message to parse
   * @return the parsed message
   * @since 4.10.0
   */
  Component parse(String message);
}
