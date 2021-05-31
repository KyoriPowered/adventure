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
package net.kyori.adventure.extra.kotlin

import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.jetbrains.annotations.Contract

/**
 * Check if this style contains the specified [decoration]
 *
 * @return true if this style contains the decoration, false otherwise
 * @since 4.8.0
 */
public operator fun Style.contains(decoration: TextDecoration): Boolean = hasDecoration(decoration)

/**
 * Get the state of the specified [decoration]
 *
 * @param decoration the decoration
 * @return the state of the specified decoration
 * @since 4.8.0
 */
public operator fun Style.get(decoration: TextDecoration): TextDecoration.State = decoration(decoration)

/**
 * Add the specified [decoration] (with its state set to [TextDecoration.State.TRUE] by default)
 *
 * @param decoration the decoration to add
 * @return a new style that is a copy of this one with the specified decoration set to true
 * @since 4.8.0
 */
@Contract("_ -> new")
public operator fun Style.plus(decoration: TextDecoration): Style = decorate(decoration)

/**
 * Allows editing using [Style.Builder] as the receiver parameter
 *
 * @param consumer the consumer to edit this style with
 * @return a new style, with the changes applied from this builder
 * @since 4.8.0
 */
@Contract("_ -> new")
public fun Style.edit(consumer: Style.Builder.() -> Unit): Style = edit(consumer)
