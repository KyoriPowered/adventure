package net.kyori.adventure.dfu.component.nbt;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponentBuilder;

public class StorageNBTSourceType extends NBTSource {
  public static final MapCodec<StorageNBTSourceType> CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(AdventureCodecs.KEY.fieldOf("storage").forGetter(StorageNBTSourceType::getStorage))
      .apply(instance, StorageNBTSourceType::new));

  public static final NBTSourceType TYPE = new NBTSourceType("storage", CODEC);

  private final Key storage;

  public StorageNBTSourceType(final Key storage) {
    this.storage = storage;
  }

  @Override
  public NBTComponentBuilder builder() {
    return Component.storageNBT()
      .storage(storage);
  }

  @Override
  public NBTSourceType type() {
    return TYPE;
  }

  public Key getStorage() {
    return storage;
  }
}
