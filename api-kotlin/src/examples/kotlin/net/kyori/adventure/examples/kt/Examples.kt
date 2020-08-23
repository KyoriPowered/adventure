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
package net.kyori.adventure.examples.kt

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.kt.audience.asAudience
import net.kyori.adventure.kt.util.component1
import net.kyori.adventure.kt.util.component2
import net.kyori.adventure.kt.util.component3
import net.kyori.adventure.kt.audience.openBook
import net.kyori.adventure.kt.text.text
import net.kyori.adventure.kt.text.translatable
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

/**
 * A simple example of using extensions to build a component
 */
fun componentDsl() {
  text("Welcome to ") {
    color(NamedTextColor.DARK_PURPLE)
    append(translatable("kyori.test") {
      decoration(TextDecoration.BOLD, true)
      args(text("meow"), text("purr"))
    })
  }
}

fun darken(color: TextColor): TextColor {
  // we add decomposition to colors
  val (r, g, b) = color
  return TextColor.of((r * 0.9).toInt(), (g * 0.9).toInt(), (b * 0.9).toInt())
}

fun audiences(targets: Iterable<Audience>) {
  // Create an audience that sends to the first 5 elements of the iterable at each send
  val audience = targets.asSequence()
          .take(5)
          .asAudience()
}

fun openBookExample(audience: Audience) {
  audience.openBook {
    title(text("Come on an adventure!", NamedTextColor.LIGHT_PURPLE))
    author(text("The Kyori Team"))

    addPage(text("This is a demonstration of the ") {
      append(text("Kotlin", NamedTextColor.DARK_AQUA))
      append(text(" extension functions"))
    })
  }
}
