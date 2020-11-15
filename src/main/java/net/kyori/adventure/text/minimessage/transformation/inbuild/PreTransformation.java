package net.kyori.adventure.text.minimessage.transformation.inbuild;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.stream.Stream;

public class PreTransformation extends Transformation {
    public static boolean canParse(final String name) {
        return name.equalsIgnoreCase(Tokens.PRE);
    }

    private PreTransformation() {
    }

    @Override
    public Component apply(Component component, TextComponent.Builder parent) {
        return component;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PreTransformation;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.empty();
    }

    public static class Parser implements TransformationParser<PreTransformation> {
        @Override
        public PreTransformation parse() {
            return new PreTransformation();
        }
    }
}
