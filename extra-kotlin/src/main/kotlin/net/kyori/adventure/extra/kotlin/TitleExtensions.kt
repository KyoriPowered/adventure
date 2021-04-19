package net.kyori.adventure.extra.kotlin

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.time.Duration

/**
 * The title of this title
 *
 * Allows for destructuring into (title, subtitle, times)
 *
 * @return the title
 * @since 4.8.0
 */
public operator fun Title.component1(): Component = title()

/**
 * The subtitle of this title
 *
 * Allows for destructuring into (title, subtitle, times)
 *
 * @return the subtitle
 * @since 4.8.0
 */
public operator fun Title.component2(): Component = subtitle()

/**
 * The times of this title
 *
 * Allows for destructuring into (title, subtitle, times)
 *
 * @return the times
 * @since 4.8.0
 */
public operator fun Title.component3(): Title.Times? = times()

/**
 * The fade in time of this [Title.Times] object
 *
 * Allows for destructuring into (fadeIn, stay, fadeOut)
 *
 * @return the fade in time
 * @since 4.8.0
 */
public operator fun Title.Times.component1(): Duration = fadeIn()

/**
 * The stay time of this [Title.Times] object
 *
 * Allows for destructuring into (fadeIn, stay, fadeOut)
 *
 * @return the stay time
 * @since 4.8.0
 */
public operator fun Title.Times.component2(): Duration = stay()

/**
 * The fade out time of this [Title.Times] object
 *
 * Allows for destructuring into (fadeIn, stay, fadeOut)
 *
 * @return the fade out time
 * @since 4.8.0
 */
public operator fun Title.Times.component3(): Duration = fadeOut()
