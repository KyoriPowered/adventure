package net.kyori.adventure.dfu.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class TextType extends ComponentType<TextComponent> {
  public static final MapCodec<TextComponent> CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(Codec.STRING.fieldOf("text").forGetter(TextComponent::content))
      .apply(instance, Component::text));

  public static final TextType INSTANCE = new TextType(CODEC);

  public TextType(final MapCodec<TextComponent> codec) {
    super("text", codec);
  }
}
