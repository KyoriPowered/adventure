package net.kyori.adventure.extension.kotlin

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.inventory.Book

/**
 * Create an [Audience] sending to every member of the receiver.
 */
public fun Iterable<Audience>.asAudience(): Audience {
  return Audience.of(this)
}

/**
 * Creates an [Audience] from this sequence.
 *
 * The sequence will be iterated for every audience operation.
 */
public fun Sequence<Audience>.asAudience(): Audience {
  return Audience.of(Iterable { this.iterator() })
}

/**
 * Creates an [Audience] from this sequence.
 *
 * The sequence will be eagerly evaluated at call time. This option may be
 * preferred over [asAudience] when working with a [Sequence] than can only
 * be iterated once.
 */
public fun Sequence<Audience>.toEagerAudience(): Audience {
  return Audience.of(toList())
}

/**
 * Create and open a [Book].
 */
public fun Audience.openBook(maker: Book.Builder.() -> Unit) {
  this.openBook(Book.builder().also(maker).build())
}
