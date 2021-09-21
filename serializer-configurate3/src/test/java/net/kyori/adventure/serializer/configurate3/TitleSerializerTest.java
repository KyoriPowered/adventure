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
package net.kyori.adventure.serializer.configurate3;

import java.time.Duration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import ninja.leaping.configurate.ConfigurationNode;
import org.junit.jupiter.api.Test;

class TitleSerializerTest implements ConfigurateTestBase {
  @Test
  void testTitleNoTimes() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(TitleSerializer.TITLE, ComponentTypeSerializer.TEXT).setValue("Title");
      n.getNode(TitleSerializer.SUBTITLE).act(sub -> {
        sub.getNode(ComponentTypeSerializer.TEXT).setValue("Subtitle");
        sub.getNode(StyleSerializer.COLOR).setValue("dark_purple");
      });
    });

    final Title title = Title.title(Component.text("Title"), Component.text("Subtitle", NamedTextColor.DARK_PURPLE));

    this.assertRoundtrippable(TitleSerializer.TYPE, title, node);
  }

  @Test
  void testTitleWithTimes() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(TitleSerializer.TITLE, ComponentTypeSerializer.TEXT).setValue("Title");
      n.getNode(TitleSerializer.SUBTITLE).act(sub -> {
        sub.getNode(ComponentTypeSerializer.TEXT).setValue("Subtitle");
        sub.getNode(StyleSerializer.COLOR).setValue("dark_purple");
      });
      n.getNode(TitleSerializer.TIMES).act(times -> {
        times.getNode(TitleSerializer.FADE_IN).setValue("PT50S");
        times.getNode(TitleSerializer.STAY).setValue("PT20S");
        times.getNode(TitleSerializer.FADE_OUT).setValue("PT50S");
      });
    });

    final Title title = Title.title(Component.text("Title"), Component.text("Subtitle", NamedTextColor.DARK_PURPLE),
      Title.Times.times(Duration.ofSeconds(50), Duration.ofSeconds(20), Duration.ofSeconds(50)));

    this.assertRoundtrippable(TitleSerializer.TYPE, title, node);
  }
}
