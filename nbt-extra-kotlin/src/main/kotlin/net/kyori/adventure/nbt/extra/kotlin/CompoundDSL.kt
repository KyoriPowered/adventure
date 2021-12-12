package net.kyori.adventure.nbt.extra.kotlin

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag

public inline fun compound(
  builder: CompoundBinaryTag.Builder.() -> Unit
): CompoundBinaryTag = CompoundBinaryTag.builder().apply(builder).build()

public inline fun CompoundBinaryTag.Builder.compound(
  name: String,
  builder: CompoundBinaryTag.Builder.() -> Unit
): CompoundBinaryTag.Builder = put(name, CompoundBinaryTag.builder().apply(builder).build())

public inline fun CompoundBinaryTag.Builder.list(
  name: String,
  builder: ListBinaryTag.Builder<BinaryTag>.() -> Unit
): CompoundBinaryTag.Builder = put(name, ListBinaryTag.builder().apply(builder).build())
