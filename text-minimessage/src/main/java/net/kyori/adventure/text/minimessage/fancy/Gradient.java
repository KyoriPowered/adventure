package net.kyori.adventure.text.minimessage.fancy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Gradient implements Fancy {

  private int colorIndex = 0;

  private float factorStep = 0;
  private final TextColor color1;
  private final TextColor color2;

  public Gradient() {
    this(TextColor.fromHexString("#ffffff"), TextColor.fromHexString("#000000"));
  }

  public Gradient(TextColor c1, TextColor c2) {
    this.color1 = c1;
    this.color2 = c2;
  }

  @Override
  public void init(int size) {
    this.factorStep = (float) (1. / (size + this.colorIndex - 1));
    this.colorIndex = 0;
  }

  @Override
  public Component apply(Component current) {
    return current.color(getColor());
  }

  private TextColor getColor() {
    float factor = factorStep * colorIndex++;
    return interpolate(color1, color2, factor);
  }

  private TextColor interpolate(TextColor color1, TextColor color2, float factor) {
    return TextColor.of(
      Math.round(color1.red() + factor * (color2.red() - color1.red())),
      Math.round(color1.green() + factor * (color2.green() - color1.green())),
      Math.round(color1.blue() + factor * (color2.blue() - color1.blue()))
    );
  }
}
