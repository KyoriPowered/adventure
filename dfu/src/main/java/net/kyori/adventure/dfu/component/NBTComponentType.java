package net.kyori.adventure.dfu.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.dfu.component.nbt.NBTSource;
import net.kyori.adventure.dfu.component.nbt.NBTSourceType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;

public class NBTComponentType extends ComponentType<NBTComponent> {

  public static final MapCodec<NBTComponent> CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(
      Codec.STRING.fieldOf("nbt").forGetter(nbtComponent -> nbtComponent.nbtPath()),
      Codec.BOOL.lenientOptionalFieldOf("interpret", false).forGetter(nbtComponent -> nbtComponent.interpret()),
      AdventureCodecs.COMPONENT.lenientOptionalFieldOf("separator").forGetter(nbtComponent -> Optional.ofNullable(nbtComponent.separator())),
      NBTSourceType.CODEC.forGetter(NBTSource::from)
    ).apply(instance, NBTComponentType::of));

  public static final NBTComponentType INSTANCE = new NBTComponentType(CODEC);

  private NBTComponentType(final MapCodec<NBTComponent> codec) {
    super("nbt", codec);
  }

  private static NBTComponent of(String nbtPath, Boolean interpret, Optional<Component> separator, NBTSource nbtSource) {
    return ((NBTComponent) nbtSource.builder()
      .interpret(interpret)
      .nbtPath(nbtPath)
      .separator(separator.orElse(null))
      .build());
  }
}
