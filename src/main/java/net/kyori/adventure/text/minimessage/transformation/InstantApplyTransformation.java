package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayDeque;

public abstract class InstantApplyTransformation extends Transformation {

    public abstract void applyInstant(TextComponent.Builder parent, ArrayDeque<Transformation> transformations);

    @Override
    public Component apply(Component component, TextComponent.Builder parent) {
        return null;
    }
}
