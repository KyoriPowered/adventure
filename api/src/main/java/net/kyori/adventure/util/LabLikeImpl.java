package net.kyori.adventure.util;

import net.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LabLikeImpl implements LABLike {

  private final double lightness;
  private final double a;
  private final double b;

  LabLikeImpl(final double lightness, final double a, final double b) {
    if (lightness < 0d || lightness > 1d) {
      throw new IllegalArgumentException("lightness (" + lightness + ") is not inside the required range: [0.0, 1.0]");
    }
    this.lightness = lightness;
    this.a = a;
    this.b = b;
  }

  @Override
  public double lightness() {
    return this.lightness;
  }

  @Override
  public double a() {
    return this.a;
  }

  @Override
  public double b() {
    return this.b;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof LabLikeImpl)) return false;
    final LabLikeImpl that = (LabLikeImpl) other;
    return ShadyPines.equals(that.lightness, this.lightness) && ShadyPines.equals(that.a, this.a) && ShadyPines.equals(that.b, this.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.lightness, this.a, this.b);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }
}
