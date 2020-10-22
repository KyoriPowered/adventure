package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;

import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class DecorationTransformation extends Transformation {

    private TextDecoration decoration;

    @Override
    public void load(String name, List<Token> args) {
        super.load(name, args);
        this.decoration = TextDecoration.NAMES.value(name.toLowerCase(Locale.ROOT));

        if (decoration == null) {
            throw new ParsingException("Don't know how to turn '" + name + "' into a decoration", -1);
        }
    }

    public static boolean isApplicable(String name) {
        return TextDecoration.NAMES.value(name.toLowerCase(Locale.ROOT)) != null;
    }

    @Override
    public Component apply(Component component) {
        return component.decorate(decoration);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DecorationTransformation.class.getSimpleName() + "[", "]")
                .add("decoration=" + decoration)
                .toString();
    }
}
