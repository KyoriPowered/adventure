package net.kyori.adventure.extension.kotlin

import net.kyori.adventure.util.RGBLike

/**
 * The [RGBLike.red] component.
 *
 * Allows for `(r, g, b)` value decomposition.
 */
public operator fun RGBLike.component1(): Int = this.red()

/**
 * The [RGBLike.green] component.
 *
 * Allows for `(r, g, b)` value decomposition.
 */
public operator fun RGBLike.component2(): Int = this.green()

/**
 * The [RGBLike.blue] component.
 *
 * Allows for `(r, g, b)` value decomposition.
 */
public operator fun RGBLike.component3(): Int = this.blue()
