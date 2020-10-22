package net.kyori.adventure.text.minimessage.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.kyori.adventure.text.minimessage.parser.Token;

public class TransformationRegistry {
  private final List<Entry<? extends Transformation>> transformations = new ArrayList<>();

  public TransformationRegistry() {
    this.register(ColorTransformation::new, ColorTransformation::isApplicable);
    this.register(DecorationTransformation::new, DecorationTransformation::isApplicable);
    this.register(HoverTransformation::new, HoverTransformation::isApplicable);
    this.register(ClickTransformation::new, ClickTransformation::isApplicable);
    this.register(KeybindTransformation::new, KeybindTransformation::isApplicable);
    this.register(TranslatableTransformation::new, TranslatableTransformation::isApplicable);
    this.register(InsertionTransformation::new, InsertionTransformation::isApplicable);
    this.register(FontTransformation::new, FontTransformation::isApplicable);
    this.register(GradientTransformation::new, GradientTransformation::isApplicable);
    this.register(RainbowTransformation::new, RainbowTransformation::isApplicable);
  }

  private <T extends Transformation> void register(final Supplier<T> factory, final Predicate<String> applicable) {
    this.transformations.add(new Entry<>(applicable, factory));
  }

  public Transformation get(final String name, final List<Token> inners) {
    for(final Entry<? extends Transformation> entry : this.transformations) {
      if(entry.applicable.test(name)) {
        final Transformation transformation = entry.factory.get();
        transformation.load(name, inners);
        return transformation;
      }
    }

    return null;
  }

  static class Entry<T extends Transformation> {
    final Predicate<String> applicable;
    final Supplier<T> factory;

    Entry(final Predicate<String> applicable, final Supplier<T> factory) {
      this.applicable = applicable;
      this.factory = factory;
    }
  }
}
