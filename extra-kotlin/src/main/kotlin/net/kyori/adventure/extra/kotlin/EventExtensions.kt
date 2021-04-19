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
