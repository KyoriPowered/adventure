package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayDeque;

public abstract class OneTimeTransformation extends Transformation {

    public abstract void applyOneTime(Component current, TextComponent.Builder parent, ArrayDeque<Transformation> transformations);

    @Override
    public Component apply(Component component, TextComponent.Builder parent) {
        return null;
    }
}
