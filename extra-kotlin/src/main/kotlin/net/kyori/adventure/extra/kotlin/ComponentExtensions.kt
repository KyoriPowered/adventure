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

import net.kyori.adventure.text.*
import net.kyori.adventure.text.format.Style

/**
 * Add [that] as a child component.
 *
 * @return a component
 * @since 4.6.0
 */
public operator fun Component.plus(that: ComponentLike): Component = this.append(that)

/**
 * The style of this component.
 *
 * Allows for destructuring into (style, children)
 *
 * @return the style
 * @since 4.8.0
 */
public operator fun Component.component1(): Style = style()

/**
 * The children of this component.
 *
 * Allows for destructuring into (style, children)
 *
 * @return the children
 * @since 4.8.0
 */
public operator fun Component.component2(): List<ComponentLike> = children()

/**
 * The keybind of this keybind component.
 *
 * Allows for destructuring into (style, children, keybind)
 *
 * @return the keybind
 * @since 4.8.0
 */
public operator fun KeybindComponent.component3(): String = keybind()

/**
 * The name of this score component.
 *
 * Allows for destructuring into (style, children, name, objective, value)
 *
 * @return the score
 * @since 4.8.0
 */
public operator fun ScoreComponent.component3(): String = name()

/**
 * The objective of this score component.
 *
 * Allows for destructuring into (style, children, name, objective, value)
 *
 * @return the objective
 * @since 4.8.0
 */
public operator fun ScoreComponent.component4(): String = objective()

/**
 * The value of this score component.
 *
 * Allows for destructuring into (style, children, name, objective, value)
 *
 * @return the value
 * @since 4.8.0
 */
@Deprecated("No longer present in 1.16.5, will not be replaced", ReplaceWith(""))
public operator fun ScoreComponent.component5(): String? = value()

/**
 * The pattern of this selector component.
 *
 * Allows for destructuring into (style, children, pattern)
 *
 * @return the pattern
 * @since 4.8.0
 */
public operator fun SelectorComponent.component3(): String = pattern()

/**
 * The content of this text component.
 *
 * Allows for destructuring into (style, children, text)
 *
 * @return the content
 * @since 4.8.0
 */
public operator fun TextComponent.component3(): String = content()

/**
 * The translation key of this translatable component.
 *
 * Allows for destructuring into (style, children, key)
 *
 * @return the key
 * @since 4.8.0
 */
public operator fun TranslatableComponent.component3(): String = key()
