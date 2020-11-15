package net.kyori.adventure.text.minimessage.transformation.inbuild;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.transformation.OneTimeTransformation;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayDeque;
import java.util.stream.Stream;

public class ResetTransformation extends OneTimeTransformation {
    public static boolean canParse(final String name) {
        return name.equalsIgnoreCase(Tokens.RESET);
    }

    private ResetTransformation() {
    }

    @Override
    public Component applyOneTime(Component current, TextComponent.Builder parent, ArrayDeque<Transformation> transformations) {
        transformations.clear();
        return current;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.empty();
    }

    @Override
    public boolean equals(final Object other) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static class Parser implements TransformationParser<ResetTransformation> {
        @Override
        public ResetTransformation parse() {
            return new ResetTransformation();
        }
    }
}
