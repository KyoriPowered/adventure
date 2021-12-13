package net.kyori.adventure.nbt.extra.kotlin

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.ListTagSetter

@JvmSynthetic
public operator fun <R, T : BinaryTag> ListTagSetter<R, T>.plus(tag: T): R = add(tag)

@JvmSynthetic
public operator fun <R, T : BinaryTag> ListTagSetter<R, T>.plus(tags: Iterable<T>): R = add(tags)
