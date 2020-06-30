/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageSerializerTest {

  @Test
  public void testColor() {
    final String expected = "<red>This is a test";

    final Builder builder = TextComponent.builder().content("This is a test").color(NamedTextColor.RED);

    test(builder, expected);
  }

  @Test
  public void testColorClosing() {
    final String expected = "<red>This is a </red>test";

    final Builder builder = TextComponent.builder()
      .content("This is a ").color(NamedTextColor.RED)
      .append("test");

    test(builder, expected);
  }

  @Test
  public void testNestedColor() {
    final String expected = "<red>This is a <blue>blue <red>test";

    final Builder builder = TextComponent.builder()
      .content("This is a ").color(NamedTextColor.RED)
      .append("blue ", NamedTextColor.BLUE)
      .append("test", NamedTextColor.RED);

    test(builder, expected);
  }

  @Test
  public void testDecoration() {
    final String expected = "<underlined>This is <bold>underlined</underlined>, this</bold> isn't";

    final Builder builder = TextComponent.builder()
      .content("This is ").decoration(TextDecoration.UNDERLINED, true)
      .append("underlined", b -> b.decoration(TextDecoration.UNDERLINED, true).decoration(TextDecoration.BOLD, true))
      .append(", this", b -> b.decoration(TextDecoration.BOLD, true))
      .append(" isn't");

    test(builder, expected);
  }

  @Test
  public void testHover() {
    final String expected = "<hover:show_text:\"---\">Some hover</hover> that ends here";

    final Builder builder = TextComponent.builder()
      .content("Some hover").hoverEvent(HoverEvent.showText(TextComponent.of("---")))
      .append(" that ends here");

    test(builder, expected);
  }

  @Test
  public void testHoverWithNested() {
    final String expected = "<hover:show_text:\"<red>---<blue><bold>-\">Some hover</hover> that ends here";

    final Builder builder = TextComponent.builder()
      .content("Some hover").hoverEvent(HoverEvent.showText(TextComponent.builder()
        .content("---").color(NamedTextColor.RED)
        .append("-", NamedTextColor.BLUE, TextDecoration.BOLD)
        .build()))
      .append(" that ends here");

    test(builder, expected);
  }

  @Test
  public void testClick() {
    final String expected = "<click:run_command:\"test\">Some click</click> that ends here";

    final Builder builder = TextComponent.builder()
      .content("Some click").clickEvent(ClickEvent.runCommand("test"))
      .append(" that ends here");

    test(builder, expected);
  }

  @Test
  public void testContinuedClick() {
    final String expected = "<click:run_command:\"test\">Some click<red> that doesn't end here";

    final Builder builder = TextComponent.builder()
      .content("Some click").clickEvent(ClickEvent.runCommand("test"))
      // TODO figure out how to avoid repeating the click event here
      .append(" that doesn't end here", b -> b.color(NamedTextColor.RED).clickEvent(ClickEvent.runCommand("test")));

    test(builder, expected);
  }

  @Test
  public void testContinuedClick2() {
    final String expected = "<click:run_command:\"test\">Some click<red> that doesn't end here";

    final Builder builder = TextComponent.builder()
      .content("Some click").clickEvent(ClickEvent.runCommand("test"))
      .append(" that doesn't end here", b -> b.color(NamedTextColor.RED).clickEvent(ClickEvent.runCommand("test")));

    test(builder, expected);
  }

  @Test
  public void testKeyBind() {
    final String expected = "Press <key:key.jump> to jump!";

    final Builder builder = TextComponent.builder()
      .content("Press ")
      .append(KeybindComponent.of("key.jump"))
      .append(" to jump!");

    test(builder, expected);
  }

  @Test
  public void testKeyBindWithColor() {
    final String expected = "Press <red><key:key.jump> to jump!";

    final Builder builder = TextComponent.builder()
      .content("Press ")
      .append(KeybindComponent.of("key.jump").color(NamedTextColor.RED))
      .append(" to jump!", NamedTextColor.RED);

    test(builder, expected);
  }

  @Test
  public void testTranslatable() {
    final String expected = "You should get a <lang:block.minecraft.diamond_block>!";

    final Builder builder = TextComponent.builder()
      .content("You should get a ")
      .append(TranslatableComponent.of("block.minecraft.diamond_block"))
      .append("!");

    test(builder, expected);
  }

  @Test
  public void testInsertion() {
    final String expected = "Click <insert:test>this</insert> to insert!";

    final Builder builder = TextComponent.builder()
      .content("Click ")
      .append("this", b -> b.insertion("test"))
      .append(" to insert!");

    test(builder, expected);
  }

  @Test
  public void testHexColor() {
    final String expected = "<color:#ff0000>This is a </color:#ff0000>test";

    final Builder builder = TextComponent.builder()
      .content("This is a ").color(TextColor.fromHexString("#ff0000"))
      .append("test");

    test(builder, expected);
  }

  private void test(final @NonNull Builder builder, final @NonNull String expected) {
    final String string = MiniMessageSerializer.serialize(builder.build());
    assertEquals(expected, string);
  }
}
