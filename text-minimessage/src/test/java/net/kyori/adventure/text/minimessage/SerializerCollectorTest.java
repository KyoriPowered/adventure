/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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

import java.util.function.Consumer;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializerCollectorTest {
  @Test
  void testLiteralOnly() {
    final String expected = "hello world!";
    final String output = this.serializeToString(c -> c.text(expected));

    assertEquals(expected, output);
  }

  @Test
  void testEscapesInText() {
    final String output = this.serializeToString(c -> c.text("hello <world>"));
    assertEquals("hello \\<world>", output);
  }

  @Test
  void testEscapesInTag() {
    final String output = this.serializeToString(c -> c.tag("mini_>message").text("hi").pop());
    assertEquals("<'mini_>message'>hi</'mini_>message'>", output);
  }

  @Test
  void testEscapesInQuotedString() {
    final String output = this.serializeToString(c -> c.tag("hover").arguments("show_text", "hello im <red>cool</red>").text("meow").pop());
    assertEquals("<hover:show_text:'hello im <red>cool</red>'>meow</hover>", output);
  }

  @Test
  void testPopUnmarked() {
    final String output = this.serializeToString(c -> c.popToMark());
    assertEquals("", output);
  }

  @Test
  void testPopMarked() {
    final String output = this.serializeToString(c -> {
      c.mark();
      c.tag("red").tag("bold").text("hi there").popToMark();
    });

    assertEquals("<red><bold>hi there</bold></red>", output);
  }

  @Test
  void testPopMultiLevelMark() {
    final String output = this.serializeToString(c -> {
      c.mark();
      c.tag("red").text("did you know i'm ");
      c.mark();
      c.tag("bold").text("a cat").popToMark();

      c.text(" or something").popToMark();
      c.text("?");
    });

    assertEquals("<red>did you know i'm <bold>a cat</bold> or something</red>?", output);
  }

  String serializeToString(final Consumer<MiniMessageSerializer.Collector> handler) {
    final StringBuilder output = new StringBuilder();
    final MiniMessageSerializer.Collector collector = new MiniMessageSerializer.Collector((SerializableResolver) StandardTags.defaults(), false, output);
    handler.accept(collector);

    return output.toString();
  }
}
