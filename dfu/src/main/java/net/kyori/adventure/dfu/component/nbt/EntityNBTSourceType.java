package net.kyori.adventure.dfu.component.nbt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponentBuilder;

public class EntityNBTSourceType extends NBTSource {
  public static final MapCodec<EntityNBTSourceType> CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(Codec.STRING.fieldOf("entity").forGetter(EntityNBTSourceType::getRawPath))
      .apply(instance, EntityNBTSourceType::new));

  public static final NBTSourceType TYPE = new NBTSourceType("entity", CODEC);


  private final String rawPath;


  public EntityNBTSourceType(final String rawPath) {
    this.rawPath = rawPath;
  }

  @Override
  public NBTComponentBuilder builder() {
    return Component.entityNBT()
      .selector(rawPath);
  }

  @Override
  public NBTSourceType type() {
    return TYPE;
  }

  public String getRawPath() {
    return rawPath;
  }
}
