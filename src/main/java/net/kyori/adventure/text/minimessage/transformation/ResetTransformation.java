package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Tokens;
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
    public void applyOneTime(Component current, TextComponent.Builder parent, ArrayDeque<Transformation> transformations) {
        transformations.clear();
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.empty();
    }

    @Override
    public boolean equals(final Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    static class Parser implements TransformationParser<ResetTransformation> {
        @Override
        public ResetTransformation parse() {
            return new ResetTransformation();
        }
    }
}
