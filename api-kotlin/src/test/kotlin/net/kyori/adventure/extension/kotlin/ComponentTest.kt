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

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentTest {
  @Test
  fun `joining components together`() {
    val expected = TextComponent.builder().append(TextComponent.of("one"), COMMA_SPACE, TranslatableComponent.of("kyori.meow")).build()
    val input = listOf("one".text(), "kyori.meow".tr())
    assertEquals(expected, input.join())
  }

  @Test
  fun `joining an empty collection should just append the barriers`() {
    assertEquals(TextComponent.empty(), emptyList<Component>().join())
    assertEquals(TextComponent.builder().append("[".text(), "]".text()).build(), emptyList<Component>().join(prefix = "[".text(), suffix = "]".text()))
  }

  @Test
  fun `joining over the limit`() {
    val expected = TextComponent.builder().append("one".text(), COMMA_SPACE, "two".text(), COMMA_SPACE, TRUNCATE_MARK).build()
    val input = listOf("one".text(), "two".text(), "three".text())
    assertEquals(expected, input.join(limit = 2))
  }
}
