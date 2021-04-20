package net.kyori.adventure.nbt.extra.kotlin

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.ListTagSetter

public operator fun <R, T : BinaryTag> ListTagSetter<R, T>.plus(tag: T): R = add(tag)

public operator fun <R, T : BinaryTag> ListTagSetter<R, T>.plus(tags: Iterable<T>): R = add(tags)

public operator fun <R, T : BinaryTag> ListTagSetter<R, T>.plusAssign(tag: T) {
  add(tag)
}

public operator fun <R, T : BinaryTag> ListTagSetter<R, T>.plusAssign(tags: Iterable<T>) {
  add(tags)
}
