package net.kyori.adventure.extra.kotlin

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

/**
 * The name of this sound
 *
 * Allows for destructuring into (name, source, volume, pitch)
 *
 * @return the name
 * @since 4.8.0
 */
public operator fun Sound.component1(): Key = name()

/**
 * The source of this sound
 *
 * Allows for destructuring into (name, source, volume, pitch)
 *
 * @return the source
 * @since 4.8.0
 */
public operator fun Sound.component2(): Sound.Source = source()

/**
 * The volume of this sound
 *
 * Allows for destructuring into (name, source, volume, pitch)
 *
 * @return the volume
 * @since 4.8.0
 */
public operator fun Sound.component3(): Float = volume()

/**
 * The pitch of this sound
 *
 * Allows for destructuring into (name, source, volume, pitch)
 *
 * @return the pitch
 * @since 4.8.0
 */
public operator fun Sound.component4(): Float = pitch()
