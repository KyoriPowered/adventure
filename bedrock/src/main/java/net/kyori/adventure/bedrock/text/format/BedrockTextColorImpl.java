/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.bedrock.text.format;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.Index;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

final class BedrockTextColorImpl implements BedrockTextColor {
  private final String name;
  private final int value;
  private final HSVLike hsv;

  BedrockTextColorImpl(final NamedTextColor color) {
    this(NamedTextColor.NAMES.key(color), color.value());
  }

  BedrockTextColorImpl(final String name, final int value) {
    this.name = name;
    this.value = value;
    this.hsv = HSVLike.fromRGB(this.red(), this.green(), this.blue());
  }

  @Override
  public int value() {
    return this.value;
  }

  @Override
  public @NotNull HSVLike asHSV() {
    return this.hsv;
  }

  @Override
  public @NotNull String toString() {
    return this.name;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(ExaminableProperty.of("name", this.name)),
      BedrockTextColor.super.examinableProperties()
    );
  }

  static class Lazy {
    private static final List<BedrockTextColor> VALUES = Collections.unmodifiableList(Arrays.asList(
      BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE,
      MINECOIN_GOLD, MATERIAL_QUARTZ, MATERIAL_IRON, MATERIAL_NETHERITE, MATERIAL_REDSTONE, MATERIAL_COPPER, MATERIAL_GOLD, MATERIAL_EMERALD,
      MATERIAL_DIAMOND, MATERIAL_LAPIS, MATERIAL_AMETHYST
    ));
    static final Index<String, BedrockTextColor> NAMES = Index.create(color -> ((BedrockTextColorImpl) color).name, VALUES);

    private Lazy() {
    }
  }
}
