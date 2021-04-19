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

import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent

/**
 * The action of this click event
 *
 * Allows for destructuring into (action, value)
 *
 * @return the action
 * @since 4.8.0
 */
public operator fun ClickEvent.component1(): ClickEvent.Action = action()

/**
 * The value of this click event
 *
 * Allows for destructuring into (action, value)
 *
 * @return the value
 * @since 4.8.0
 */
public operator fun ClickEvent.component2(): String = value()

/**
 * The action of this hover event
 *
 * Allows for destructuring into (action, value)
 *
 * @return the action
 * @since 4.8.0
 */
public operator fun <V> HoverEvent<V>.component1(): HoverEvent.Action<V> = action()

/**
 * The value of this hover event
 *
 * Allows for destructuring into (action, value)
 *
 * @return the value
 * @since 4.8.0
 */
public operator fun <V> HoverEvent<V>.component2(): V = value()

/**
 * The item for this show item hover event value
 *
 * Allows for destructuring into (item, count, nbt)
 *
 * @return the item
 * @since 4.8.0
 */
public operator fun HoverEvent.ShowItem.component1(): Key = item()

/**
 * The item count for this show item hover event value
 *
 * Allows for destructuring into (item, count, nbt)
 *
 * @return the item count
 * @since 4.8.0
 */
public operator fun HoverEvent.ShowItem.component2(): Int = count()

/**
 * The nbt data for this show item hover event value
 *
 * Allows for destructuring into (item, count, nbt)
 *
 * @return the nbt data
 * @since 4.8.0
 */
public operator fun HoverEvent.ShowItem.component3(): BinaryTagHolder? = nbt()
