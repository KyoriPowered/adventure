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

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract

/**
 * Checks if this boss bar has the specified [flag].
 *
 * @return true if this bar contains the flag, false otherwise
 * @since 4.8.0
 */
public operator fun BossBar.contains(flag: BossBar.Flag): Boolean = hasFlag(flag)

/**
 * Add a flag to this boss bar.
 *
 * @return this bar, with the flag added
 * @since 4.8.0
 */
@Contract("_ -> this")
public operator fun BossBar.plus(flag: BossBar.Flag): BossBar = addFlag(flag)

/**
 * Add flags to this boss bar.
 *
 * @return this bar, with the flags added
 * @since 4.8.0
 */
@Contract("_ -> this")
public operator fun BossBar.plus(flags: Set<BossBar.Flag>): BossBar = addFlags(flags)

/**
 * Add a flag to this boss bar.
 *
 * @since 4.8.0
 */
public operator fun BossBar.plusAssign(flag: BossBar.Flag) {
  addFlag(flag)
}

/**
 * Add flags to this boss bar.
 *
 * @since 4.8.0
 */
public operator fun BossBar.plusAssign(flags: Set<BossBar.Flag>) {
  addFlags(flags)
}

/**
 * Remove a flag from this boss bar.
 *
 * @return this bar, with the flag removed
 * @since 4.8.0
 */
@Contract("_ -> this")
public operator fun BossBar.minus(flag: BossBar.Flag): BossBar = removeFlag(flag)

/**
 * Remove flags from this boss bar.
 *
 * @return this boss bar, with the flags removed
 * @since 4.8.0
 */
@Contract("_ -> this")
public operator fun BossBar.minus(flags: Set<BossBar.Flag>): BossBar = removeFlags(flags)

/**
 * Remove a flag from this boss bar.
 *
 * @since 4.8.0
 */
public operator fun BossBar.minusAssign(flag: BossBar.Flag) {
  removeFlag(flag)
}

/**
 * Remove flags from this boss bar.
 *
 * @since 4.8.0
 */
public operator fun BossBar.minusAssign(flags: Set<BossBar.Flag>) {
  removeFlags(flags)
}

/**
 * The name of this boss bar.
 *
 * Allows destructuring into (name, progress, color, overlay, flags)
 *
 * @return the name
 * @since 4.8.0
 */
public operator fun BossBar.component1(): Component = name()

/**
 * The progress of this boss bar.
 *
 * Allows destructuring into (name, progress, color, overlay, flags)
 *
 * @return the progress
 * @since 4.8.0
 */
public operator fun BossBar.component2(): Float = progress()

/**
 * The colour of this boss bar.
 *
 * Allows destructuring into (name, progress, color, overlay, flags)
 *
 * @return the colour
 * @since 4.8.0
 */
public operator fun BossBar.component3(): BossBar.Color = color()

/**
 * The overlay of this boss bar.
 *
 * Allows destructuring into (name, progress, color, overlay, flags)
 *
 * @return the overlay
 * @since 4.8.0
 */
public operator fun BossBar.component4(): BossBar.Overlay = overlay()

/**
 * The flags of this boss bar.
 *
 * Allows destructuring into (name, progress, color, overlay, flags)
 *
 * @return the flags
 * @since 4.8.0
 */
public operator fun BossBar.component5(): Set<BossBar.Flag> = flags()
