package net.kyori.adventure.dfu.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.SelectorComponent;

public class SelectorType extends ComponentType<SelectorComponent> {
  public static final MapCodec<SelectorComponent> CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(
      Codec.STRING.fieldOf("selector").forGetter(SelectorComponent::pattern),
      AdventureCodecs.COMPONENT.optionalFieldOf("separator").forGetter(selectorComponent -> Optional.ofNullable(selectorComponent.separator()))
    ).apply(instance, (selector, separator) -> Component.selector(selector, separator.orElse(null))));

  public static final SelectorType INSTANCE = new SelectorType(CODEC);

  private SelectorType(final MapCodec<SelectorComponent> codec) {
    super("selector", codec);
  }
}
