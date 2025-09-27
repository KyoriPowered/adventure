package net.kyori.adventure.util;

import org.jetbrains.annotations.Range;

class RGBLikeImpl implements RGBLike {
  private final int red;
  private final int green;
  private final int blue;

  public RGBLikeImpl(final @Range(from = 0x0, to = 0xff) int red, final @Range(from = 0x0, to = 0xff) int green, final @Range(from = 0x0, to = 0xff) int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public RGBLikeImpl(final @Range(from = 0x0, to = 0xffffff) int value) {
    this.red = value >> 16 & 0xFF;
    this.green = value >> 8 & 0xFF;
    this.blue = value & 0xFF;
  }

  @Override
  public @Range(from = 0x0, to = 0xff) int red() {
    return red;
  }

  @Override
  public @Range(from = 0x0, to = 0xff) int green() {
    return green;
  }

  @Override
  public @Range(from = 0x0, to = 0xff) int blue() {
    return blue;
  }
}
