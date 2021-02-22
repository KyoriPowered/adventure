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

import net.kyori.adventure.text.BlockNBTComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.EntityNBTComponent
import net.kyori.adventure.text.KeybindComponent
import net.kyori.adventure.text.ScoreComponent
import net.kyori.adventure.text.SelectorComponent
import net.kyori.adventure.text.StorageNBTComponent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent

fun blockNBT(builder: BlockNBTComponent.Builder.() -> Unit): BlockNBTComponent = Component.blockNBT(builder)

fun entityNBT(builder: EntityNBTComponent.Builder.() -> Unit): EntityNBTComponent = Component.entityNBT(builder)

fun keybind(builder: KeybindComponent.Builder.() -> Unit): KeybindComponent = Component.keybind(builder)

fun score(builder: ScoreComponent.Builder.() -> Unit): ScoreComponent = Component.score(builder)

fun selector(builder: SelectorComponent.Builder.() -> Unit): SelectorComponent = Component.selector(builder)

fun storageNBT(builder: StorageNBTComponent.Builder.() -> Unit): StorageNBTComponent = Component.storageNBT(builder)

fun text(builder: TextComponent.Builder.() -> Unit): TextComponent = Component.text(builder)

fun translatable(builder: TranslatableComponent.Builder.() -> Unit): TranslatableComponent = Component.translatable(builder)
