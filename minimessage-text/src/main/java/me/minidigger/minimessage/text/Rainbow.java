package me.minidigger.minimessage.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Rainbow {

    private int colorIndex = 0;

    private float center = 128;
    private float width = 127;
    private double frequency = 1;

    private int phase;

    public Rainbow() {
        this(0);
    }

    public Rainbow(int phase) {
        this.phase = phase;
    }

    public void init(int steps) {
        center = 128;
        width = 127;
        frequency = Math.PI * 2 / steps;
    }

    public Component apply(Component current) {
        return current.color(getColor(phase));
    }

    private TextColor getColor(float phase) {
        int index = colorIndex++;
        int red = (int) (Math.sin(frequency * index + 2 + phase) * width + center);
        int green = (int) (Math.sin(frequency * index + 0 + phase) * width + center);
        int blue = (int) (Math.sin(frequency * index + 4 + phase) * width + center);
        return TextColor.of(red, green, blue);
    }
}
