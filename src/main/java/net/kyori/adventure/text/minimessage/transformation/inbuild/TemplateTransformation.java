package net.kyori.adventure.text.minimessage.transformation.inbuild;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.transformation.Inserting;
import net.kyori.adventure.text.minimessage.transformation.OneTimeTransformation;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.examination.ExaminableProperty;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.stream.Stream;

public class TemplateTransformation extends OneTimeTransformation implements Inserting {

    private final Template.ComponentTemplate template;

    public TemplateTransformation(Template.ComponentTemplate template) {
        this.template = template;
    }

    @Override
    public Component applyOneTime(Component current, TextComponent.Builder parent, ArrayDeque<Transformation> transformations) {
        parent.append(merge( template.getValue(), current));
        return current;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("template", this.template));
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(other == null || this.getClass() != other.getClass()) return false;
        final TemplateTransformation that = (TemplateTransformation) other;
        return Objects.equals(this.template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.template);
    }

}
