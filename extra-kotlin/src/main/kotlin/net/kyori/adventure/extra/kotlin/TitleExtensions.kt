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

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.time.Duration

/**
 * The title of this title
 *
 * Allows for destructuring into (title, subtitle, times)
 *
 * @return the title
 * @since 4.8.0
 */
public operator fun Title.component1(): Component = title()

/**
 * The subtitle of this title
 *
 * Allows for destructuring into (title, subtitle, times)
 *
 * @return the subtitle
 * @since 4.8.0
 */
public operator fun Title.component2(): Component = subtitle()

/**
 * The times of this title
 *
 * Allows for destructuring into (title, subtitle, times)
 *
 * @return the times
 * @since 4.8.0
 */
public operator fun Title.component3(): Title.Times? = times()

/**
 * The fade in time of this [Title.Times] object
 *
 * Allows for destructuring into (fadeIn, stay, fadeOut)
 *
 * @return the fade in time
 * @since 4.8.0
 */
public operator fun Title.Times.component1(): Duration = fadeIn()

/**
 * The stay time of this [Title.Times] object
 *
 * Allows for destructuring into (fadeIn, stay, fadeOut)
 *
 * @return the stay time
 * @since 4.8.0
 */
public operator fun Title.Times.component2(): Duration = stay()

/**
 * The fade out time of this [Title.Times] object
 *
 * Allows for destructuring into (fadeIn, stay, fadeOut)
 *
 * @return the fade out time
 * @since 4.8.0
 */
public operator fun Title.Times.component3(): Duration = fadeOut()
