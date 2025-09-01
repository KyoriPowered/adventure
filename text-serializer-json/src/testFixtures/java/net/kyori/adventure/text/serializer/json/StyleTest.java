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
package net.kyori.adventure.text.serializer.json;

import com.google.gson.JsonNull;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StyleTest extends SerializerTest {

  @Override
  protected JSONComponentSerializer createSerializer() {
    return JSONComponentSerializer.builder()
      .editOptions(b -> b.value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT, false))
      .build();
  }

  @Test
  void testWithDecorationAsColor() {
    final Style s0 = deserialize(object(object -> {
      object.addProperty(ComponentTreeConstants.TEXT, "");
      object.addProperty(ComponentTreeConstants.COLOR, name(TextDecoration.BOLD));
    })).style();

    assertNull(s0.color());
    assertTrue(s0.hasDecoration(TextDecoration.BOLD));
  }

  @Test
  void testWithResetAsColor() {
    final Style s0 = deserialize(object(object -> {
      object.addProperty(ComponentTreeConstants.TEXT, "");
      object.addProperty(ComponentTreeConstants.COLOR, "reset");
    })).style();

    assertNull(s0.color());
  }

  @Test
  void testEmpty() {
    this.testStyle(Style.empty(), json -> {});
  }

  @Test
  void testHexColor() {
    this.testStyle(Style.style(TextColor.color(0x0a1ab9)), json -> json.addProperty(ComponentTreeConstants.COLOR, "#0A1AB9"));
  }

  @Test
  void testNamedColor() {
    this.testStyle(Style.style(NamedTextColor.LIGHT_PURPLE), json -> json.addProperty(ComponentTreeConstants.COLOR, name(NamedTextColor.LIGHT_PURPLE)));
  }

  @Test
  void testDecoration() {
    this.testStyle(Style.style(TextDecoration.BOLD), json -> json.addProperty(name(TextDecoration.BOLD), true));
    this.testStyle(Style.style(TextDecoration.BOLD.withState(false)), json -> json.addProperty(name(TextDecoration.BOLD), false));
    this.testStyle(Style.style(TextDecoration.BOLD.withState(TextDecoration.State.NOT_SET)), json -> {});

    final Style s0 = deserialize(object(object -> {
      object.addProperty(ComponentTreeConstants.TEXT, "");
      object.addProperty(name(TextDecoration.BOLD), 1);
    })).style();
    assertTrue(s0.hasDecoration(TextDecoration.BOLD));

    assertThrows(RuntimeException.class, () -> {
      deserialize(object(object -> {
        object.addProperty(ComponentTreeConstants.TEXT, "");
        object.add(name(TextDecoration.BOLD), JsonNull.INSTANCE);
      }));
    });
  }

  @Test
  void testShadowColorInt() {
    this.testStyle(Style.style(ShadowColor.shadowColor(0xCCFF0022)), json -> json.addProperty(ComponentTreeConstants.SHADOW_COLOR, 0xCCFF0022));
  }

  @Test
  void testShadowColorFloats() {
    final JSONComponentSerializer floatSerial = JSONComponentSerializer.builder()
      .editOptions(opts -> opts.value(JSONOptions.SHADOW_COLOR_MODE, JSONOptions.ShadowColorEmitMode.EMIT_ARRAY))
      .build();

    this.testStyle(floatSerial, Style.style(ShadowColor.shadowColor(0x80, 0x40, 0xcc, 0xff)), json -> json.add(ComponentTreeConstants.SHADOW_COLOR, array(arr -> {
      arr.add(0.501960813999176);
      arr.add(0.250980406999588f);
      arr.add(0.800000011920929f);
      arr.add(1f);
    })));
  }

  @Test
  void testInsertion() {
    this.testStyle(Style.style().insertion("honk").build(), json -> json.addProperty(ComponentTreeConstants.INSERTION, "honk"));
  }

  @Test
  void testMixedFontColorDecorationClickEvent() {
    this.testStyle(
      Style.style()
        .font(Key.key("kyori", "kittens"))
        .color(NamedTextColor.RED)
        .decoration(TextDecoration.BOLD, true)
        .clickEvent(ClickEvent.openUrl("https://github.com"))
        .build(),
      json -> {
        json.addProperty(ComponentTreeConstants.FONT, "kyori:kittens");
        json.addProperty(ComponentTreeConstants.COLOR, name(NamedTextColor.RED));
        json.addProperty(name(TextDecoration.BOLD), true);
        json.add(ComponentTreeConstants.CLICK_EVENT_SNAKE, object(clickEvent -> {
          clickEvent.addProperty(ComponentTreeConstants.CLICK_EVENT_ACTION, name(ClickEvent.Action.OPEN_URL));
          clickEvent.addProperty(ComponentTreeConstants.CLICK_EVENT_URL, "https://github.com");
        }));
      }
    );
  }

  @Test
  void testShowEntityHoverEvent() {
    final UUID dolores = UUID.randomUUID();
    this.testStyle(
      Style.style()
        .hoverEvent(HoverEvent.showEntity(HoverEvent.ShowEntity.showEntity(
          Key.key(Key.MINECRAFT_NAMESPACE, "pig"),
          dolores,
          Component.text("Dolores", TextColor.color(0x0a1ab9))
        )))
        .build(),
      json -> {
        json.add(ComponentTreeConstants.HOVER_EVENT_SNAKE, object(hoverEvent -> {
          hoverEvent.addProperty(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ENTITY));
          hoverEvent.addProperty(ComponentTreeConstants.SHOW_ENTITY_ID, "minecraft:pig");
          hoverEvent.add(ComponentTreeConstants.SHOW_ENTITY_UUID, uuidArray(dolores));
          hoverEvent.add(ComponentTreeConstants.SHOW_ENTITY_NAME, object(name -> {
            name.addProperty(ComponentTreeConstants.TEXT, "Dolores");
            name.addProperty(ComponentTreeConstants.COLOR, "#0A1AB9");
          }));
        }));
      }
    );
  }
}
