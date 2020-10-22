package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;

import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class ColorTransformation extends Transformation {

    private TextColor color;

    @Override
    public void load(String name, List<Token> args) {
        super.load(name, args);

        if (name.equalsIgnoreCase(Tokens.COLOR)) {
            if (args.size() != 1 || args.get(0).getType() != TokenType.STRING) {
                throw new ParsingException("Expected to find a color parameter, but found " + args, -1);
            }
            name = args.get(0).getValue();
        }

        if (name.charAt(0) == '#') {
            this.color = TextColor.fromHexString(name);
        } else {
            this.color = NamedTextColor.NAMES.value(name.toLowerCase(Locale.ROOT));
        }

        if (color == null) {
            throw new ParsingException("Don't know how to turn '" + name + "' into a color", -1);
        }
    }

    public static boolean isApplicable(String name) {
        return name.equalsIgnoreCase(Tokens.COLOR) || TextColor.fromHexString(name) != null || NamedTextColor.NAMES.value(name.toLowerCase(Locale.ROOT)) != null;
    }

    @Override
    public Component apply(Component component) {
        return component.color(color);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ColorTransformation.class.getSimpleName() + "[", "]")
                .add("color=" + color)
                .toString();
    }
}
