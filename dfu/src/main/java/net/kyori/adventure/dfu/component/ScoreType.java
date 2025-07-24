package net.kyori.adventure.dfu.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ScoreComponent;

public class ScoreType extends ComponentType<ScoreComponent> {
  public static final MapCodec<ScoreComponent> INNER_CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(
      Codec.STRING.fieldOf("name").forGetter(ScoreComponent::name),
      Codec.STRING.fieldOf("objective").forGetter(ScoreComponent::objective)
    ).apply(instance, Component::score));
  public static final MapCodec<ScoreComponent> CODEC = INNER_CODEC.fieldOf("score");

  public static final ScoreType INSTANCE = new ScoreType(CODEC);

  private ScoreType(final MapCodec<ScoreComponent> codec) {
    super("score", codec);
  }
}
