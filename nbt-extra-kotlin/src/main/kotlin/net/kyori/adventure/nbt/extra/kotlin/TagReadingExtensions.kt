/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.nbt.extra.kotlin

import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import java.io.DataInput
import java.io.InputStream
import java.nio.file.Path

public fun InputStream.readCompoundTag(): CompoundBinaryTag = BinaryTagIO.unlimitedReader().read(this)

public fun InputStream.readCompoundTag(
  compression: BinaryTagIO.Compression
): CompoundBinaryTag = BinaryTagIO.unlimitedReader().read(this, compression)

public fun InputStream.readCompoundTag(readerSize: Long): CompoundBinaryTag = BinaryTagIO.reader(readerSize).read(this)

public fun InputStream.readCompoundTag(
  readerSize: Long,
  compression: BinaryTagIO.Compression
): CompoundBinaryTag = BinaryTagIO.reader(readerSize).read(this, compression)

public fun DataInput.readCompoundTag(): CompoundBinaryTag = BinaryTagIO.unlimitedReader().read(this)

public fun DataInput.readCompoundTag(readerSize: Long): CompoundBinaryTag = BinaryTagIO.reader(readerSize).read(this)

public fun Path.readCompoundTag(): CompoundBinaryTag = BinaryTagIO.unlimitedReader().read(this)

public fun Path.readCompoundTag(
  compression: BinaryTagIO.Compression
): CompoundBinaryTag = BinaryTagIO.unlimitedReader().read(this, compression)

public fun Path.readCompoundTag(readerSize: Long): CompoundBinaryTag = BinaryTagIO.reader(readerSize).read(this)

public fun Path.readCompoundTag(
  readerSize: Long,
  compression: BinaryTagIO.Compression
): CompoundBinaryTag = BinaryTagIO.reader(readerSize).read(this, compression)
