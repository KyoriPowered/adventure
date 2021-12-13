package net.kyori.adventure.nbt.extra.kotlin

import net.kyori.adventure.nbt.ByteArrayBinaryTag
import net.kyori.adventure.nbt.ByteBinaryTag
import net.kyori.adventure.nbt.DoubleBinaryTag
import net.kyori.adventure.nbt.FloatBinaryTag
import net.kyori.adventure.nbt.IntArrayBinaryTag
import net.kyori.adventure.nbt.IntBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.LongBinaryTag
import net.kyori.adventure.nbt.ShortBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag

@JvmSynthetic
public fun Byte.toBinaryTag(): ByteBinaryTag = ByteBinaryTag.of(this)

@JvmSynthetic
public fun Short.toBinaryTag(): ShortBinaryTag = ShortBinaryTag.of(this)

@JvmSynthetic
public fun Int.toBinaryTag(): IntBinaryTag = IntBinaryTag.of(this)

@JvmSynthetic
public fun Long.toBinaryTag(): LongBinaryTag = LongBinaryTag.of(this)

@JvmSynthetic
public fun Float.toBinaryTag(): FloatBinaryTag = FloatBinaryTag.of(this)

@JvmSynthetic
public fun Double.toBinaryTag(): DoubleBinaryTag = DoubleBinaryTag.of(this)

@JvmSynthetic
public fun ByteArray.toBinaryTag(): ByteArrayBinaryTag = ByteArrayBinaryTag.of(*this)

@JvmSynthetic
public fun IntArray.toBinaryTag(): IntArrayBinaryTag = IntArrayBinaryTag.of(*this)

@JvmSynthetic
public fun LongArray.toBinaryTag(): LongArrayBinaryTag = LongArrayBinaryTag.of(*this)

@JvmSynthetic
public fun String.toBinaryTag(): StringBinaryTag = StringBinaryTag.of(this)
