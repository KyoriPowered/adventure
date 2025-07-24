package net.kyori.adventure.dfu.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;

public class KeybindType extends ComponentType<KeybindComponent> {
  public static final MapCodec<KeybindComponent> CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(Codec.STRING.fieldOf("keybind").forGetter(content -> content.keybind()))
      .apply(instance, Component::keybind));

  public static final KeybindType INSTANCE = new KeybindType(CODEC);

  public KeybindType(final MapCodec<KeybindComponent> codec) {
    super("keybind", codec);
  }
}
