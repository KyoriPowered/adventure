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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("CodeBlock2Expr")
class StyleTest extends GsonTest<Style> {
  private static final Key FANCY_FONT = Key.key("kyori", "kittens");

  StyleTest() {
    super(GsonComponentSerializer.gson().serializer(), Style.class);
  }

  @Test
  void testWithDecorationAsColor() {
    final Style s0 = GsonComponentSerializer.gson().serializer().fromJson(object(object -> {
      object.addProperty(StyleSerializer.COLOR, TextDecoration.NAMES.key(TextDecoration.BOLD));
    }), Style.class);
    assertNull(s0.color());
    assertTrue(s0.hasDecoration(TextDecoration.BOLD));
  }

  @Test
  void testWithResetAsColor() {
    final Style s0 = GsonComponentSerializer.gson().serializer().fromJson(object(object -> {
      object.addProperty(StyleSerializer.COLOR, "reset");
    }), Style.class);
    assertNull(s0.color());
    assertThat(Style.empty().decorations()).containsExactlyEntriesIn(Stream.of(TextDecoration.values()).collect(Collectors.toMap(Function.identity(), decoration -> TextDecoration.State.NOT_SET)));
  }

  @Test
  void testEmpty() {
    this.test(Style.empty(), object(json -> {
    }));
  }

  @Test
  void testHexColor() {
    this.test(Style.style(TextColor.color(0x0a1ab9)), object(json -> json.addProperty(StyleSerializer.COLOR, "#0a1ab9")));
  }

  @Test
  void testNamedColor() {
    this.test(Style.style(NamedTextColor.LIGHT_PURPLE), object(json -> json.addProperty(StyleSerializer.COLOR, name(NamedTextColor.LIGHT_PURPLE))));
  }

  @Test
  void testDecoration() {
    this.test(Style.style(TextDecoration.BOLD), object(json -> json.addProperty(name(TextDecoration.BOLD), true)));

    Style s0 = GsonComponentSerializer.gson().serializer().fromJson(object(object -> {
      object.addProperty(name(TextDecoration.BOLD), "true");
    }), Style.class);
    assertTrue(s0.hasDecoration(TextDecoration.BOLD));

    s0 = GsonComponentSerializer.gson().serializer().fromJson(object(object -> {
      object.addProperty(name(TextDecoration.BOLD), "false");
    }), Style.class);
    assertFalse(s0.hasDecoration(TextDecoration.BOLD));

    s0 = GsonComponentSerializer.gson().serializer().fromJson(object(object -> {
      object.addProperty(name(TextDecoration.BOLD), 1);
    }), Style.class);
    assertFalse(s0.hasDecoration(TextDecoration.BOLD));

    assertThrows(JsonParseException.class, () -> {
      GsonComponentSerializer.gson().serializer().fromJson(object(object -> {
        object.add(name(TextDecoration.BOLD), JsonNull.INSTANCE);
      }), Style.class);
    });
  }

  @Test
  void testInsertion() {
    this.test(Style.style().insertion("honk").build(), object(json -> json.addProperty(StyleSerializer.INSERTION, "honk")));
  }

  @Test
  void testMixedFontColorDecorationClickEvent() {
    this.test(
      Style.style()
        .font(FANCY_FONT)
        .color(NamedTextColor.RED)
        .decoration(TextDecoration.BOLD, true)
        .clickEvent(ClickEvent.openUrl("https://github.com"))
        .build(),
      object(json -> {
        json.addProperty(StyleSerializer.FONT, "kyori:kittens");
        json.addProperty(StyleSerializer.COLOR, name(NamedTextColor.RED));
        json.addProperty(name(TextDecoration.BOLD), true);
        json.add(StyleSerializer.CLICK_EVENT, object(clickEvent -> {
          clickEvent.addProperty(StyleSerializer.CLICK_EVENT_ACTION, name(ClickEvent.Action.OPEN_URL));
          clickEvent.addProperty(StyleSerializer.CLICK_EVENT_VALUE, "https://github.com");
        }));
      })
    );
  }

  @Test
  void testShowEntityHoverEvent() {
    final UUID dolores = UUID.randomUUID();
    this.test(
      Style.style()
        .hoverEvent(HoverEvent.showEntity(HoverEvent.ShowEntity.of(
          Key.key(Key.MINECRAFT_NAMESPACE, "pig"),
          dolores,
          Component.text("Dolores", TextColor.color(0x0a1ab9))
        )))
        .build(),
      object(json -> {
        json.add(StyleSerializer.HOVER_EVENT, object(hoverEvent -> {
          hoverEvent.addProperty(StyleSerializer.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ENTITY));
          hoverEvent.add(StyleSerializer.HOVER_EVENT_CONTENTS, object(contents -> {
            contents.addProperty(ShowEntitySerializer.TYPE, "minecraft:pig");
            contents.addProperty(ShowEntitySerializer.ID, dolores.toString());
            contents.add(ShowEntitySerializer.NAME, object(name -> {
              name.addProperty(ComponentSerializerImpl.TEXT, "Dolores");
              name.addProperty(StyleSerializer.COLOR, "#0a1ab9");
            }));
          }));
        }));
      })
    );
  }

  @Test
  void testShowItemHoverEvent() {
    this.test(showItemStyle(1), showItemJson(1));
    this.test(showItemStyle(2), showItemJson(2));
  }

  private static Style showItemStyle(final int count) {
    return Style.style()
      .hoverEvent(HoverEvent.showItem(HoverEvent.ShowItem.of(
        Key.key(Key.MINECRAFT_NAMESPACE, "stone"),
        count,
        null // TODO: test for NBT?
      )))
      .build();
  }

  private static JsonElement showItemJson(final int count) {
    return object(json -> {
      json.add(StyleSerializer.HOVER_EVENT, object(hoverEvent -> {
        hoverEvent.addProperty(StyleSerializer.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM));
        hoverEvent.add(StyleSerializer.HOVER_EVENT_CONTENTS, object(contents -> {
          contents.addProperty(ShowItemSerializer.ID, "minecraft:stone");
          if (count != 1) { // default count is 1, we don't serialize the value in this case
            contents.addProperty(ShowItemSerializer.COUNT, count);
          }
        }));
      }));
    });
  }

  static String name(final NamedTextColor color) {
    return NamedTextColor.NAMES.key(color);
  }

  static String name(final TextDecoration decoration) {
    return TextDecoration.NAMES.key(decoration);
  }

  static String name(final ClickEvent.Action action) {
    return ClickEvent.Action.NAMES.key(action);
  }

  static <V> String name(final HoverEvent.Action<V> action) {
    return HoverEvent.Action.NAMES.key(action);
  }
}
