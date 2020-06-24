package me.minidigger.minimessage.text.fancy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Rainbow implements Fancy {

    private int colorIndex = 0;

    private float center = 128;
    private float width = 127;
    private double frequency = 1;

    private final int phase;

    public Rainbow() {
        this(0);
    }

    public Rainbow(int phase) {
        this.phase = phase;
    }

    @Override
    public void init(int size) {
        center = 128;
        width = 127;
        frequency = Math.PI * 2 / size;
    }

    @Override
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
