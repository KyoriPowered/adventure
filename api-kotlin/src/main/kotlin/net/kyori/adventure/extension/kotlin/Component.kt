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
package net.kyori.adventure.extension.kotlin

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
import net.kyori.adventure.text.TextComponent.empty
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.StyleBuilderApplicable

// Joining components together //

// elipses for truncated joins
internal val TRUNCATE_MARK = TextComponent.of("...")
internal val COMMA_SPACE = TextComponent.of(", ")

/**
 * Add [that] as a child component.
 */
public operator fun Component.plus(that: ComponentLike): Component = this.append(that)

/**
 * Append [that] as a child of the component being built.
 */
public operator fun ComponentBuilder<*, *>.plusAssign(that: ComponentLike) {
  this.append(that)
}

/**
 * Append all components in [that] as children of the component being built.
 */
public operator fun ComponentBuilder<*, *>.plusAssign(that: Iterable<ComponentLike>) {
  this.append(that)
}

/**
 * Convert this object into a component
 */
public operator fun ComponentLike.unaryPlus(): Component = asComponent()

/**
 * Append the [Iterable] of components to an existing builder, returning the built component
 *
 * This method should match the specification of [kotlin.collections.joinTo], but
 * acting on [Component]s rather than on Strings.
 */
public fun <T: ComponentLike, B: ComponentBuilder<*, B>> Iterable<T>.joinTo(
  builder: B,
  joiner: Component = COMMA_SPACE,
  prefix: Component = empty(),
  suffix: Component = empty(),
  transform: (Component) -> Component = { it },
  limit: Int = -1,
  truncateWith: Component = TRUNCATE_MARK
): B {
  val iter = iterator()
  builder.append(prefix)

  var count = 0
  while(iter.hasNext()) {
    if(limit <= 0 || count++ < limit) {
      builder.append(transform(iter.next().asComponent()))
    } else {
      builder.append(truncateWith)
      break
    }

    if(iter.hasNext()) {
      builder.append(joiner)
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
public fun <T: ComponentLike> Iterable<T>.join(
  joiner: Component = COMMA_SPACE,
  prefix: Component = empty(),
  suffix: Component = empty(),
  transform: (Component) -> Component = { it },
  limit: Int = -1,
  truncateWith: Component = TRUNCATE_MARK
): Component = joinTo(TextComponent.builder(), joiner, prefix, suffix, transform, limit, truncateWith).build()

// Factory methods //

/** Create a new text component from `this` */
public fun String.text(vararg styles: StyleBuilderApplicable): TextComponent = TextComponent.of(this, Style.of(*styles))

/** Create a new translatable component from `this` */
public fun String.tr(vararg args: ComponentLike): TranslatableComponent = TranslatableComponent.of(this, *args)

/** Create a new keybind component using the key sequence from `this` */
public fun String.keybind(vararg styles: StyleBuilderApplicable): KeybindComponent = KeybindComponent.of(this, Style.of(*styles))

/** Create a new selector component, using `this` as a selector. */
public fun String.selector(vararg styles: StyleBuilderApplicable): SelectorComponent = SelectorComponent.builder(this).style(Style.of(*styles)).build()

/** Create a new score component, with the score at `this` from [objective] */
public fun String.score(objective: String, vararg styles: StyleBuilderApplicable): ScoreComponent = ScoreComponent.builder(this, objective).style(Style.of(*styles)).build()

/** Create a new block NBT component, with nbt path from `this` gotten at [pos] */
public fun String.blockNBT(pos: BlockNBTComponent.Pos, interpret: Boolean = false, vararg styles: StyleBuilderApplicable): BlockNBTComponent = BlockNBTComponent.builder()
  .nbtPath(this)
  .pos(pos)
  .interpret(interpret)
  .style(Style.of(*styles))
  .build()

/** Create a new entity NBT component, with nbt path from `this` gotten from the entity marked by [entitySelector] */
public fun String.entityNBT(entitySelector: String, interpret: Boolean = false, vararg styles: StyleBuilderApplicable): EntityNBTComponent = EntityNBTComponent.builder()
  .nbtPath(this)
  .selector(entitySelector)
  .interpret(interpret)
  .style(Style.of(*styles))
  .build()

/** Create a new storage NBT component, with nbt path from `this` gotten from the named storage at [storage] */
public fun String.storageNBT(storage: Key, interpret: Boolean = false, vararg styles: StyleBuilderApplicable): StorageNBTComponent = StorageNBTComponent.builder()
  .nbtPath(this)
  .storage(storage)
  .interpret(interpret)
  .style(Style.of(*styles))
  .build()
