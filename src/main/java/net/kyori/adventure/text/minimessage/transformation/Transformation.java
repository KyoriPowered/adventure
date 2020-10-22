package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.Token;

import java.util.List;

public abstract class Transformation {

    private String name;

    public abstract Component apply(Component component);

    public void load(String name, List<Token> args) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
