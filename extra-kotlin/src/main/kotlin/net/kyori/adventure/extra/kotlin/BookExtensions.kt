package net.kyori.adventure.extra.kotlin

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component

/**
 * The title of this book
 *
 * Allows for destructuring into (title, author, pages)
 *
 * @return the title
 * @since 4.8.0
 */
public operator fun Book.component1(): Component = title()

/**
 * The author of this book
 *
 * Allows for destructuring into (title, author, pages)
 *
 * @return the author
 * @since 4.8.0
 */
public operator fun Book.component2(): Component = author()

/**
 * The pages of this book
 *
 * Allows for destructuring into (title, author, pages)
 *
 * @return the pages
 * @since 4.8.0
 */
public operator fun Book.component3(): List<Component> = pages()
