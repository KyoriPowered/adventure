package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.minimessage.parser.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TransformationRegistry {

    private final Map<Class<? extends Transformation>, Function<String, Boolean>> known = new HashMap<>();

    public TransformationRegistry() {
        known.put(ColorTransformation.class, ColorTransformation::isApplicable);
        known.put(DecorationTransformation.class, DecorationTransformation::isApplicable);
        known.put(HoverTransformation.class, HoverTransformation::isApplicable);
        known.put(ClickTransformation.class, ClickTransformation::isApplicable);
        known.put(KeybindTransformation.class, KeybindTransformation::isApplicable);
        known.put(TranslatableTransformation.class, TranslatableTransformation::isApplicable);
        known.put(InsertionTransformation.class, InsertionTransformation::isApplicable);
        known.put(FontTransformation.class, FontTransformation::isApplicable);
        known.put(GradientTransformation.class, GradientTransformation::isApplicable);
        known.put(RainbowTransformation.class, RainbowTransformation::isApplicable);
    }

    public Transformation get(String name, List<Token> inners) {
        for (Map.Entry<Class<? extends Transformation>, Function<String, Boolean>> entry : known.entrySet()) {
            if (entry.getValue().apply(name)) {
                try {
                    Transformation transformation = entry.getKey().newInstance();
                    transformation.load(name, inners);
                    return transformation;
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
