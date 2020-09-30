/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.kt.audience

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.inventory.Book

/**
 * Create an [Audience] sending to every member of the receiver.
 *
 * @return an audience
 * @sample [net.kyori.adventure.example.kt.audiences]
 * @since 4.0.0
 */
public fun Iterable<Audience>.asAudience(): Audience {
  return Audience.audience(this)
}

/**
 * Creates an [Audience] from this sequence.
 *
 * The sequence will be iterated for every audience operation.
 *
 * @return an audience
 * @sample [net.kyori.adventure.example.kt.audiences]
 * @since 4.0.0
 */
public fun Sequence<Audience>.asAudience(): Audience {
  return Audience.audience(Iterable { this.iterator() })
}

/**
 * Creates an [Audience] from this sequence.
 *
 * The sequence will be eagerly evaluated at call time. This option may be
 * preferred over [asAudience] when working with a [Sequence] than can only
 * be iterated once.
 *
 * @return an audience
 * @since 4.0.0
 */
public fun Sequence<Audience>.toEagerAudience(): Audience {
  return Audience.audience(toList())
}

/**
 * Create and open a [Book].
 *
 * @sample [net.kyori.adventure.example.kt.openBookExample]
 * @since 4.0.0
 */
public fun Audience.openBook(maker: Book.Builder.() -> Unit) {
  this.openBook(Book.builder().also(maker).build())
}
