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

import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.Index;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The text colours in Minecraft (Bedrock Edition).
 *
 * @since 4.14.0
 */
@ApiStatus.NonExtendable
public interface BedrockTextColor extends TextColor {
  /**
   * The standard {@code black} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor BLACK = new BedrockTextColorImpl(NamedTextColor.BLACK);
  /**
   * The standard {@code dark_blue} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor DARK_BLUE = new BedrockTextColorImpl(NamedTextColor.DARK_BLUE);
  /**
   * The standard {@code dark_green} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor DARK_GREEN = new BedrockTextColorImpl(NamedTextColor.DARK_GREEN);
  /**
   * The standard {@code dark_aqua} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor DARK_AQUA = new BedrockTextColorImpl(NamedTextColor.DARK_AQUA);
  /**
   * The standard {@code dark_red} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor DARK_RED = new BedrockTextColorImpl(NamedTextColor.DARK_RED);
  /**
   * The standard {@code dark_purple} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor DARK_PURPLE = new BedrockTextColorImpl(NamedTextColor.DARK_PURPLE);
  /**
   * The standard {@code gold} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor GOLD = new BedrockTextColorImpl(NamedTextColor.GOLD);
  /**
   * The standard {@code gray} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor GRAY = new BedrockTextColorImpl(NamedTextColor.GRAY);
  /**
   * The standard {@code dark_gray} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor DARK_GRAY = new BedrockTextColorImpl(NamedTextColor.DARK_GRAY);
  /**
   * The standard {@code blue} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor BLUE = new BedrockTextColorImpl(NamedTextColor.BLUE);
  /**
   * The standard {@code green} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor GREEN = new BedrockTextColorImpl(NamedTextColor.GREEN);
  /**
   * The standard {@code aqua} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor AQUA = new BedrockTextColorImpl(NamedTextColor.AQUA);
  /**
   * The standard {@code red} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor RED = new BedrockTextColorImpl(NamedTextColor.RED);
  /**
   * The standard {@code light_purple} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor LIGHT_PURPLE = new BedrockTextColorImpl(NamedTextColor.LIGHT_PURPLE);
  /**
   * The standard {@code yellow} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor YELLOW = new BedrockTextColorImpl(NamedTextColor.YELLOW);
  /**
   * The standard {@code white} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor WHITE = new BedrockTextColorImpl(NamedTextColor.WHITE);

  /**
   * The bedrock {@code minecoin_gold} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MINECOIN_GOLD = new BedrockTextColorImpl("minecoin_gold", 0xddd605);
  /**
   * The bedrock {@code material_quartz} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_QUARTZ = new BedrockTextColorImpl("material_quartz", 0xe3d4d1);
  /**
   * The bedrock {@code material_iron} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_IRON = new BedrockTextColorImpl("material_iron", 0xcecaca);
  /**
   * The bedrock {@code material_netherite} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_NETHERITE = new BedrockTextColorImpl("material_netherite", 0x443a3b);
  /**
   * The bedrock {@code material_redstone} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_REDSTONE = new BedrockTextColorImpl("material_redstone", 0x971607);
  /**
   * The bedrock {@code material_copper} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_COPPER = new BedrockTextColorImpl("material_copper", 0xb4684d);
  /**
   * The bedrock {@code material_gold} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_GOLD = new BedrockTextColorImpl("material_gold", 0xdEB12d);
  /**
   * The bedrock {@code material_emerald} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_EMERALD = new BedrockTextColorImpl("material_emerald", 0x47a036);
  /**
   * The bedrock {@code material_diamond} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_DIAMOND = new BedrockTextColorImpl("material_diamond", 0x2cbaa8);
  /**
   * The bedrock {@code material_lapis} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_LAPIS = new BedrockTextColorImpl("material_lapis", 0x21497b);
  /**
   * The bedrock {@code material_amethyst} colour.
   *
   * @since 4.14.0
   */
  BedrockTextColor MATERIAL_AMETHYST = new BedrockTextColorImpl("material_amethyst", 0x9a5cc6);

  /**
   * Gets an index of name to color.
   *
   * @since 4.14.0
   */
  static @NotNull Index<String, BedrockTextColor> names() {
    return BedrockTextColorImpl.Lazy.NAMES;
  }

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return TextColor.super.examinableProperties();
  }
}
