package net.kyori.adventure.text.serializer.legacy;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.Index;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public final class BedrockNamedTextColor implements TextColor {
  private static final int MINECOIN_GOLD_VALUE = 0xddd605;
  private static final int MATERIAL_QUARTZ_VALUE = 0xe3d4d1;
  private static final int MATERIAL_IRON_VALUE = 0xcecaca;
  private static final int MATERIAL_NETHERITE_VALUE = 0x443a3b;
  private static final int MATERIAL_REDSTONE_VALUE = 0x971607;
  private static final int MATERIAL_COPPER_VALUE = 0xb4684d;
  private static final int MATERIAL_GOLD_VALUE = 0xdeb12d;
  private static final int MATERIAL_EMERALD_VALUE = 0x47a036;
  private static final int MATERIAL_DIAMOND_VALUE = 0x2cbaa8;
  private static final int MATERIAL_LAPIS_VALUE = 0x21497b;
  private static final int MATERIAL_AMETHYST_VALUE = 0x9a5cc6;

  /**
   * The standard {@code minecoin_gold} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MINECOIN_GOLD = new BedrockNamedTextColor("minecoin_gold", MINECOIN_GOLD_VALUE);

  /**
   * The standard {@code material_quartz} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_QUARTZ = new BedrockNamedTextColor("material_quartz", MATERIAL_QUARTZ_VALUE);

  /**
   * The standard {@code material_iron} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_IRON = new BedrockNamedTextColor("material_iron", MATERIAL_IRON_VALUE);

  /**
   * The standard {@code material_netherite} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_NETHERITE = new BedrockNamedTextColor("material_netherite", MATERIAL_NETHERITE_VALUE);

  /**
   * The standard {@code material_redstone} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_REDSTONE = new BedrockNamedTextColor("material_redstone", MATERIAL_REDSTONE_VALUE);

  /**
   * The standard {@code material_copper} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_COPPER = new BedrockNamedTextColor("material_copper", MATERIAL_COPPER_VALUE);

  /**
   * The standard {@code material_gold} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_GOLD = new BedrockNamedTextColor("material_gold", MATERIAL_GOLD_VALUE);

  /**
   * The standard {@code material_emerald} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_EMERALD = new BedrockNamedTextColor("material_emerald", MATERIAL_EMERALD_VALUE);

  /**
   * The standard {@code material_diamond} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_DIAMOND = new BedrockNamedTextColor("material_diamond", MATERIAL_DIAMOND_VALUE);

  /**
   * The standard {@code material_lapis} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_LAPIS = new BedrockNamedTextColor("material_lapis", MATERIAL_LAPIS_VALUE);

  /**
   * The standard {@code material_amethyst} colour.
   *
   * @since 4.15.0
   */
  public static final BedrockNamedTextColor MATERIAL_AMETHYST = new BedrockNamedTextColor("material_amethyst", MATERIAL_AMETHYST_VALUE);


  private static final List<BedrockNamedTextColor> VALUES = Collections.unmodifiableList(Arrays.asList(MINECOIN_GOLD, MATERIAL_QUARTZ, MATERIAL_IRON, MATERIAL_NETHERITE, MATERIAL_REDSTONE, MATERIAL_COPPER, MATERIAL_GOLD, MATERIAL_EMERALD, MATERIAL_DIAMOND, MATERIAL_LAPIS, MATERIAL_AMETHYST));
  /**
   * An index of name to color.
   *
   * @since 4.15.0
   */
  public static final Index<String, BedrockNamedTextColor> NAMES = Index.create(constant -> constant.name, VALUES);

  /**
   * Gets the named color exactly matching the provided color.
   *
   * @param value the color to match
   * @return the matched color, or null
   * @since 4.15.0
   */
  public static @Nullable BedrockNamedTextColor namedColor(final int value) {
    switch (value) {
      case MINECOIN_GOLD_VALUE: return MINECOIN_GOLD;
      case MATERIAL_QUARTZ_VALUE: return MATERIAL_QUARTZ;
      case MATERIAL_IRON_VALUE: return MATERIAL_IRON;
      case MATERIAL_NETHERITE_VALUE: return MATERIAL_NETHERITE;
      case MATERIAL_REDSTONE_VALUE: return MATERIAL_REDSTONE;
      case MATERIAL_COPPER_VALUE: return MATERIAL_COPPER;
      case MATERIAL_GOLD_VALUE: return MATERIAL_GOLD;
      case MATERIAL_EMERALD_VALUE: return MATERIAL_EMERALD;
      case MATERIAL_DIAMOND_VALUE: return MATERIAL_DIAMOND;
      case MATERIAL_LAPIS_VALUE: return MATERIAL_LAPIS;
      case MATERIAL_AMETHYST_VALUE: return MATERIAL_AMETHYST;
      default: return null;
    }
  }

  /**
   * Find the named colour nearest to the provided colour.
   *
   * @param any colour to match
   * @return nearest named colour. will always return a value
   * @since 4.15.0
   */
  public static @NotNull BedrockNamedTextColor nearestTo(final @NotNull TextColor any) {
    if (any instanceof BedrockNamedTextColor) {
      return (BedrockNamedTextColor) any;
    }

    return TextColor.nearestColorTo(VALUES, any);
  }

  private final String name;
  private final int value;
  private final HSVLike hsv;

  private BedrockNamedTextColor(final String name, final int value) {
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
      TextColor.super.examinableProperties()
    );
  }
}
