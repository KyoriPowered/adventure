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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import net.kyori.adventure.util.TriState;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

final class StyleTest extends SerializerTest {
  @Test
  public void testEmpty() {
    this.test(Style.empty(), CompoundBinaryTag.empty());
  }

  @Test
  public void testHexColor() {
    this.test(
      Style.style(TextColor.color(0x0a1ab9)),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.COLOR, "#0A1AB9")
        .build()
    );
  }

  @Test
  public void testNamedColor() {
    this.test(
      Style.style(NamedTextColor.LIGHT_PURPLE),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.COLOR, "light_purple")
        .build()
    );
  }

  @Test
  public void testDecoration() {
    this.test(
      Style.style(TextDecoration.BOLD),
      CompoundBinaryTag.builder()
        .putBoolean("bold", true)
        .build()
    );

    this.test(
      Style.style(TextDecoration.BOLD.withState(false)),
      CompoundBinaryTag.builder()
        .putBoolean("bold", false)
        .build()
    );

    this.test(
      Style.style(TextDecoration.BOLD.withState(TriState.NOT_SET)),
      CompoundBinaryTag.empty()
    );

    assertThrows(
      IllegalArgumentException.class,
      () -> this.deserialize(
        CompoundBinaryTag.builder()
          .putString(ComponentTreeConstants.TEXT, "")
          .put("bold", EndBinaryTag.endBinaryTag())
          .build()
      )
    );
  }

  @Test
  public void testShadowColorInt() {
    int shadowColorValue = 0xCCFF0022;
    this.test(
      Style.style(ShadowColor.shadowColor(shadowColorValue)),
      CompoundBinaryTag.builder()
        .putInt(ComponentTreeConstants.SHADOW_COLOR, shadowColorValue)
        .build()
    );
  }

  @Test
  public void testShadowColorFloats() {
    this.test(
      NBTComponentSerializer.builder()
        .editOptions(builder -> builder.value(NBTSerializerOptions.SHADOW_COLOR_MODE, NBTSerializerOptions.ShadowColorEmitMode.EMIT_ARRAY))
        .build(),
      Style.style(ShadowColor.shadowColor(0x80, 0x40, 0xcc, 0xff)),
      CompoundBinaryTag.builder()
        .put(
          ComponentTreeConstants.SHADOW_COLOR,
          ListBinaryTag.builder(BinaryTagTypes.FLOAT)
            .add(FloatBinaryTag.floatBinaryTag(0.501960813999176f))
            .add(FloatBinaryTag.floatBinaryTag(0.250980406999588f))
            .add(FloatBinaryTag.floatBinaryTag(0.800000011920929f))
            .add(FloatBinaryTag.floatBinaryTag(1f))
            .build()
        )
        .build()
    );
  }

  @Test
  public void testInsertion() {
    String insertion = "honk";
    this.test(
      Style.style()
        .insertion(insertion)
        .build(),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.INSERTION, insertion)
        .build()
    );
  }

  @Test
  public void testMixedFontColorDecorationClickEvent() {
    String clickEventUrl = "https://github.com";
    this.test(
      Style.style()
        .font(Key.key("kyori", "kittens"))
        .color(NamedTextColor.RED)
        .decoration(TextDecoration.BOLD, true)
        .clickEvent(ClickEvent.openUrl(clickEventUrl))
        .build(),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.FONT, "kyori:kittens")
        .putString(ComponentTreeConstants.COLOR, "red")
        .putBoolean("bold", true)
        .put(
          ComponentTreeConstants.CLICK_EVENT_SNAKE,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.CLICK_EVENT_ACTION, "open_url")
            .putString(ComponentTreeConstants.CLICK_EVENT_URL, clickEventUrl)
            .build()
        )
        .build()
    );
  }

  @Test
  public void testShowEntityHoverEvent() {
    UUID showEntityUUID = UUID.randomUUID();
    String showEntityName = "Dolores";

    this.test(
      Style.style()
        .hoverEvent(HoverEvent.showEntity(
          Key.key(Key.MINECRAFT_NAMESPACE, "pig"),
          showEntityUUID,
          Component.text(showEntityName, TextColor.color(0x0a1ab9))
        ))
        .build(),
      CompoundBinaryTag.builder()
        .put(
          ComponentTreeConstants.HOVER_EVENT_SNAKE,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, "show_entity")
            .putString(ComponentTreeConstants.SHOW_ENTITY_ID, "minecraft:pig")
            .put(
              ComponentTreeConstants.SHOW_ENTITY_UUID,
              IntArrayBinaryTag.intArrayBinaryTag(
                (int) (showEntityUUID.getMostSignificantBits() >> 32),
                (int) (showEntityUUID.getMostSignificantBits() & 0xffffffffL),
                (int) (showEntityUUID.getLeastSignificantBits() >> 32),
                (int) (showEntityUUID.getLeastSignificantBits() & 0xffffffffL)
              )
            )
            .put(
              ComponentTreeConstants.SHOW_ENTITY_NAME,
              CompoundBinaryTag.builder()
                .putString(ComponentTreeConstants.TEXT, showEntityName)
                .putString(ComponentTreeConstants.COLOR, "#0A1AB9")
                .build()
            )
            .build()
        )
        .build()
    );
  }
}
