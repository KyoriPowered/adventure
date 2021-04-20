package net.kyori.adventure.extra.kotlin

import net.kyori.adventure.key.Key

/**
 * The namespace of this key.
 *
 * Allows for destructuring into (namespace, key)
 *
 * @return the namespace
 * @since 4.8.0
 */
public fun Key.component1(): String = namespace()

/**
 * The value of this key.
 *
 * Allows for destructuring into (namespace, key)
 *
 * @return the value
 * @since 4.8.0
 */
public fun Key.component2(): String = value()
