package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.Token;

import java.util.List;

public class HoverTransformation extends Transformation {

    // TODO hover

    @Override
    public void load(String name, List<Token> args) {
        super.load(name, args);
    }

    @Override
    public Component apply(Component component) {
        return null;
    }

    public static boolean isApplicable(String name) {
        return false;
    }
}
