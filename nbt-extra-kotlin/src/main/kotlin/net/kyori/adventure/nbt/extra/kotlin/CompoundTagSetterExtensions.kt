package net.kyori.adventure.nbt.extra.kotlin

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.CompoundTagSetter

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, tag: BinaryTag): R = put(key, tag)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: Boolean): R = putBoolean(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: Byte): R = putByte(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: Short): R = putShort(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: Int): R = putInt(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: Long): R = putLong(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: Float): R = putFloat(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: Double): R = putDouble(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: ByteArray): R = putByteArray(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: IntArray): R = putIntArray(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: LongArray): R = putLongArray(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.set(key: String, value: String): R = putString(key, value)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.plus(tag: CompoundBinaryTag): R = put(tag)

@JvmSynthetic
public operator fun <R> CompoundTagSetter<R>.minus(key: String): R = remove(key)
