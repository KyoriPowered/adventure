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
import java.io.ByteArrayOutputStream
import java.io.DataOutput
import java.io.OutputStream
import java.nio.file.Path

public fun CompoundBinaryTag.write(outputSize: Int = 4096): OutputStream {
  val output = ByteArrayOutputStream(outputSize)
  writeTo(output)
  return output
}

public fun CompoundBinaryTag.write(compression: BinaryTagIO.Compression, outputSize: Int = 4096): OutputStream {
  val output = ByteArrayOutputStream(outputSize)
  writeTo(output, compression)
  return output
}

public fun CompoundBinaryTag.writeTo(output: OutputStream): Unit = BinaryTagIO.writer().write(this, output)

public fun CompoundBinaryTag.writeTo(
  output: OutputStream,
  compression: BinaryTagIO.Compression
): Unit = BinaryTagIO.writer().write(this, output, compression)

public fun CompoundBinaryTag.writeTo(output: DataOutput): Unit = BinaryTagIO.writer().write(this, output)

public fun CompoundBinaryTag.writeTo(path: Path): Unit = BinaryTagIO.writer().write(this, path)

public fun CompoundBinaryTag.writeTo(
  path: Path,
  compression: BinaryTagIO.Compression
): Unit = BinaryTagIO.writer().write(this, path, compression)
