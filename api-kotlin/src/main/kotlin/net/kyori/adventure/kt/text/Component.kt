/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.kt.text

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.BlockNBTComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.EntityNBTComponent
import net.kyori.adventure.text.KeybindComponent
import net.kyori.adventure.text.ScoreComponent
import net.kyori.adventure.text.SelectorComponent
import net.kyori.adventure.text.StorageNBTComponent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent

internal val TRUNCATE_MARK = Component.text("...")
internal val COMMA_SPACE = Component.text(", ")

/*
 * -------------------
 * ---- Operators ----
 * -------------------
 */

/**
 * Add [that] as a child component.
 *
 * @return a component
 * @since 4.0.0
 */
public operator fun Component.plus(that: ComponentLike): Component = this.append(that)

/**
 * Append [that] as a child of the component being built.
 *
 * @since 4.0.0
 */
public operator fun ComponentBuilder<*, *>.plusAssign(that: ComponentLike) {
  this.append(that)
}

/**
 * Append all components in [that] as children of the component being built.
 *
 * @since 4.0.0
 */
public operator fun ComponentBuilder<*, *>.plusAssign(that: Iterable<ComponentLike>) {
  this.append(that)
}

/**
 * Convert this object into a component.
 *
 * @since 4.0.0
 */
public operator fun ComponentLike.unaryPlus(): Component = asComponent()

/*
 * ------------------------
 * ---- Factory method ----
 * ------------------------
 */

/**
 * Create a new text component.
 *
 * @sample [net.kyori.adventure.example.kt.componentDsl]
 */
public fun text(maker: TextComponent.Builder.() -> Unit): TextComponent = Component.text(maker)

/**
 * Create a new translatable component.
 *
 * @sample [net.kyori.adventure.example.kt.componentDsl]
 */
public fun translatable(maker: TranslatableComponent.Builder.() -> Unit): TranslatableComponent = Component.translatable(maker)

/** Create a new keybind component. */
public fun keybind(maker: KeybindComponent.Builder.() -> Unit): KeybindComponent = Component.keybind(maker)

/** Create a new selector component. */
public fun selector(maker: SelectorComponent.Builder.() -> Unit): SelectorComponent = Component.selector(maker)

/** Create a new score component */
public fun score(maker: ScoreComponent.Builder.() -> Unit): ScoreComponent = Component.score(maker)

/** Create a new block NBT component. */
public fun blockNBT(maker: BlockNBTComponent.Builder.() -> Unit): BlockNBTComponent = Component.blockNBT(maker)

/** Create a new entity NBT component. */
public fun entityNBT(maker: EntityNBTComponent.Builder.() -> Unit): EntityNBTComponent = Component.entityNBT(maker)

/** Create a new storage NBT component. */
public fun storageNBT(path: String, storage: Key, maker: StorageNBTComponent.Builder.() -> Unit): StorageNBTComponent = Component.storageNBT(maker)

/*
 * -----------------
 * ---- Joining ----
 * -----------------
 */

/**
 * Append the [Iterable] of components to an existing builder, returning the built component
 *
 * This method should match the specification of [kotlin.collections.joinTo], but
 * acting on [Component]s rather than on Strings.
 *
 * @since 4.0.0
 */
public fun <T : ComponentLike, B : ComponentBuilder<*, B>> Iterable<T>.joinTo(
  builder: B,
  separator: Component = COMMA_SPACE,
  prefix: Component = Component.empty(),
  suffix: Component = Component.empty(),
  limit: Int = -1,
  truncated: Component = TRUNCATE_MARK,
  transform: (Component) -> Component = { it }
): B {
  val iter = iterator()
  builder.append(prefix)

  var count = 0
  while(iter.hasNext()) {
    if(limit <= 0 || count++ < limit) {
      builder.append(transform(iter.next().asComponent()))
    } else {
      builder.append(truncated)
      break
    }

    if(iter.hasNext()) {
      builder.append(separator)
    }
  }

  builder.append(suffix)
  return builder
}

/**
 * Join an iterable of components into a new [TextComponent]
 *
 * @see [joinTo] for parameter descriptions
 */
public fun <T : ComponentLike> Iterable<T>.join(
  separator: Component = COMMA_SPACE,
  prefix: Component = Component.empty(),
  suffix: Component = Component.empty(),
  limit: Int = -1,
  truncated: Component = TRUNCATE_MARK,
  transform: (Component) -> Component = { it }
): Component = joinTo(TextComponent.builder(), separator, prefix, suffix, limit, truncated, transform).build()
