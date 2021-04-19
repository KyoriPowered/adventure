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
package net.kyori.adventure.extra.kotlin

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.*

/**
 * The path of this NBT component.
 *
 * Allows for destructuring into (style, children, nbtPath, interpret)
 *
 * @return the path
 * @since 4.8.0
 */
public operator fun <C : NBTComponent<C, B>, B : NBTComponentBuilder<C, B>> NBTComponent<C, B>.component3(): String = nbtPath()

/**
 * If we should be interpreting this NBT component as JSON
 *
 * Allows for destructuring into (style, children, nbtPath, interpret)
 *
 * @return if we should be interpreting
 * @since 4.8.0
 */
public operator fun <C : NBTComponent<C, B>, B : NBTComponentBuilder<C, B>> NBTComponent<C, B>.component4(): Boolean = interpret()

/**
 * The position of this block NBT component.
 *
 * Allows for destructuring into (style, children, nbtPath, interpret, pos)
 *
 * @return the position
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.component5(): BlockNBTComponent.Pos = pos()

/**
 * The left component of this local position
 *
 * Allows for destructuring into (left, up, forward)
 *
 * @return the left component
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.LocalPos.component1(): Double = left()

/**
 * The up component of this local position
 *
 * Allows for destructuring into (left, up, forward)
 *
 * @return the up component
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.LocalPos.component2(): Double = up()

/**
 * The forward component of this local position
 *
 * Allows for destructuring into (left, up, forward)
 *
 * @return the forward component
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.LocalPos.component3(): Double = forwards()

/**
 * The X component of this world position
 *
 * Allows for destructuring into (x, y, z)
 *
 * @return the X component
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.WorldPos.component1(): BlockNBTComponent.WorldPos.Coordinate = x()

/**
 * The Y component of this world position
 *
 * Allows for destructuring into (x, y, z)
 *
 * @return the Y component
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.WorldPos.component2(): BlockNBTComponent.WorldPos.Coordinate = y()

/**
 * The Z component of this world position
 *
 * Allows for destructuring into (x, y, z)
 *
 * @return the Z component
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.WorldPos.component3(): BlockNBTComponent.WorldPos.Coordinate = z()

/**
 * The value of this coordinate
 *
 * Allows for destructuring into (value, type)
 *
 * @return the value
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.WorldPos.Coordinate.component1(): Int = value()

/**
 * The type of this coordinate
 *
 * Allows for destructuring into (value, type)
 *
 * @return the type
 * @since 4.8.0
 */
public operator fun BlockNBTComponent.WorldPos.Coordinate.component2(): BlockNBTComponent.WorldPos.Coordinate.Type = type()

/**
 * The selector of this entity NBT component
 *
 * Allows for destructuring into (style, children, nbtPath, interpret, selector)
 *
 * @return the selector
 * @since 4.8.0
 */
public operator fun EntityNBTComponent.component5(): String = selector()

/**
 * The storage key of this storage NBT component
 *
 * Allows for destructuring into (style, children, nbtPath, interpret, key)
 *
 * @return the storage key
 * @since 4.8.0
 */
public operator fun StorageNBTComponent.component5(): Key = storage()
