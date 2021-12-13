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

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag

@JvmSynthetic
public inline fun compound(
  builder: CompoundBinaryTag.Builder.() -> Unit
): CompoundBinaryTag = CompoundBinaryTag.builder().apply(builder).build()

@JvmSynthetic
public inline fun CompoundBinaryTag.Builder.compound(
  name: String,
  builder: CompoundBinaryTag.Builder.() -> Unit
): CompoundBinaryTag.Builder = put(name, CompoundBinaryTag.builder().apply(builder).build())

@JvmSynthetic
public inline fun CompoundBinaryTag.Builder.list(
  name: String,
  builder: ListBinaryTag.Builder<BinaryTag>.() -> Unit
): CompoundBinaryTag.Builder = put(name, ListBinaryTag.builder().apply(builder).build())
