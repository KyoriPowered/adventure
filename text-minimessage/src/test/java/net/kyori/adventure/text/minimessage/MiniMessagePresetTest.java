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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MiniMessagePresetTest extends AbstractTest {
  @Test
  void testAllPresetsAreConfigured() {
    for (final MiniMessage.Preset preset : MiniMessage.Preset.values()) {
      final MiniMessage miniMessage = MiniMessage.miniMessage(preset);
      assertNotNull(miniMessage);
    }
  }

  @Test
  void testDefault() {
    final MiniMessage mm = MiniMessage.miniMessage(MiniMessage.Preset.DEFAULT);
    this.assertParsedEquals(mm,
      text().append(text("Hello ", NamedTextColor.RED)
        .append(text("world", NamedTextColor.BLUE).decorate(TextDecoration.BOLD)))
        .append(text("!", NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/test")))
        .build(),
      "<red>Hello <blue><bold>world</bold></blue></red><green><click:run_command:/test>!</click></green>"
    );
  }

  @Test
  void testNonInteractable() {
    // Click, hover, insertion tags should be ignored and thus remain as literal text.
    final MiniMessage mm = MiniMessage.miniMessage(MiniMessage.Preset.NON_INTERACTABLE);
    this.assertParsedEquals(mm,
      text("<click:run_command:/test>No click</click><hover:show_text:hover>No hover</hover><insert:insert>No insertion</insert>"),
      "<click:run_command:/test>No click</click><hover:show_text:hover>No hover</hover><insert:insert>No insertion</insert>"
    );

    // Custom tags producing interactive components should be filtered.
    final MiniMessage mmCustom = MiniMessage.builder(MiniMessage.Preset.NON_INTERACTABLE)
      .editTags(t -> t.tag("dangerous", Tag.styling(ClickEvent.runCommand("/kill"))))
      .build();
    this.assertParsedEquals(mmCustom,
      text("test"),
      "<dangerous>test</dangerous>"
    );
  }

  @Test
  void testFormattedText() {
    final MiniMessage mm = MiniMessage.miniMessage(MiniMessage.Preset.FORMATTED_TEXT);

    // Basic formatting should work fine.
    this.assertParsedEquals(mm,
      text().append(text("Hello ", NamedTextColor.RED)
        .append(text("world", NamedTextColor.BLUE).decorate(TextDecoration.BOLD)))
        .build(),
      "<red>Hello <blue><bold>world</bold></blue></red>"
    );

    // Click/hover should remain as literal text.
    this.assertParsedEquals(mm,
      text("<click:run_command:/test>No click</click>"),
      "<click:run_command:/test>No click</click>"
    );

    // Non-text components (like keybind) are also not here and should remain literal.
    this.assertParsedEquals(mm,
      text("<keybind:key.jump>"),
      "<keybind:key.jump>"
    );

    // Custom tags producing non-text components should be filtered.
    final MiniMessage mmCustom = MiniMessage.builder(MiniMessage.Preset.FORMATTED_TEXT)
      .editTags(t -> t.tag("mykey", Tag.inserting(Component.keybind("key.jump"))))
      .build();
    this.assertParsedEquals(mmCustom,
      empty(),
      "<mykey>"
    );

    // Custom tags producing interactive text should also be filtered.
    final MiniMessage mmCustom2 = MiniMessage.builder(MiniMessage.Preset.FORMATTED_TEXT)
      .editTags(t -> t.tag("dangertext", Tag.inserting(text("danger").clickEvent(ClickEvent.runCommand("/kill")))))
      .build();
    this.assertParsedEquals(mmCustom2,
      text("danger"),
      "<dangertext>"
    );

    // Custom tags producing non-text components in children should also be filtered.
    final MiniMessage mmCustom3 = MiniMessage.builder(MiniMessage.Preset.FORMATTED_TEXT)
      .editTags(t -> t.tag("nontextchild", Tag.inserting(text("parent").append(Component.keybind("key.jump")))))
      .build();
    this.assertParsedEquals(mmCustom3,
      text("parent"),
      "<nontextchild>"
    );
  }
}
