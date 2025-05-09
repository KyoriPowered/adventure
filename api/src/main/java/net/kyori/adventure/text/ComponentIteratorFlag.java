/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text;

import org.jetbrains.annotations.ApiStatus;

/**
 * Flags to modify the behaviour of a component iterator.
 *
 * @see Component#iterator(ComponentIteratorType, java.util.Set)
 * @see Component#iterable(ComponentIteratorType, java.util.Set)
 * @see Component#spliterator(ComponentIteratorType, java.util.Set)
 * @since 4.9.0
 */
@ApiStatus.NonExtendable
public enum ComponentIteratorFlag {
  /**
   * Includes the name of entities inside {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_ENTITY entity} hover events.
   *
   * @since 4.9.0
   */
  INCLUDE_HOVER_SHOW_ENTITY_NAME,
  /**
   * Includes the components inside {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT text} hover events.
   *
   * @since 4.9.0
   */
  INCLUDE_HOVER_SHOW_TEXT_COMPONENT,
  /**
   * Includes the arguments of {@link TranslatableComponent translatable components}.
   *
   * @since 4.9.0
   */
  INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS;
}
