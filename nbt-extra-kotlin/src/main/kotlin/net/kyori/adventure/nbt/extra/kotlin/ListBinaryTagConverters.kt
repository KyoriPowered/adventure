package net.kyori.adventure.nbt.extra.kotlin

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.ByteArrayBinaryTag
import net.kyori.adventure.nbt.ByteBinaryTag
import net.kyori.adventure.nbt.DoubleBinaryTag
import net.kyori.adventure.nbt.FloatBinaryTag
import net.kyori.adventure.nbt.IntArrayBinaryTag
import net.kyori.adventure.nbt.IntBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.LongBinaryTag
import net.kyori.adventure.nbt.ShortBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag

public fun <T : BinaryTag> List<T>.toBinaryTag(): ListBinaryTag = ListBinaryTag.from(this)

@JvmName("toBinaryTagByte")
public fun List<Byte>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.BYTE, map(ByteBinaryTag::of))

@JvmName("toBinaryTagShort")
public fun List<Short>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.SHORT, map(ShortBinaryTag::of))

@JvmName("toBinaryTagInt")
public fun List<Int>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.INT, map(IntBinaryTag::of))

@JvmName("toBinaryTagLong")
public fun List<Long>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.LONG, map(LongBinaryTag::of))

@JvmName("toBinaryTagFloat")
public fun List<Float>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.FLOAT, map(FloatBinaryTag::of))

@JvmName("toBinaryTagDouble")
public fun List<Double>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.DOUBLE, map(DoubleBinaryTag::of))

@JvmName("toBinaryTagByteArray")
public fun List<ByteArray>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.BYTE_ARRAY, map(ByteArrayBinaryTag::of))

@JvmName("toBinaryTagIntArray")
public fun List<IntArray>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.INT_ARRAY, map(IntArrayBinaryTag::of))

@JvmName("toBinaryTagLongArray")
public fun List<LongArray>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.LONG_ARRAY, map(LongArrayBinaryTag::of))

@JvmName("toBinaryTagString")
public fun List<String>.toBinaryTag(): ListBinaryTag = ListBinaryTag.of(BinaryTagTypes.STRING, map(StringBinaryTag::of))
