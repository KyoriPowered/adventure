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
