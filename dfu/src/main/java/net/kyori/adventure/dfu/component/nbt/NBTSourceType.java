package net.kyori.adventure.dfu.component.nbt;

import com.mojang.serialization.MapCodec;
import net.kyori.adventure.dfu.Codecs;

public record NBTSourceType(String id, MapCodec<? extends NBTSource> codec) {

  public static final MapCodec<NBTSource> CODEC = Codecs.dispatchingCodec(
    new NBTSourceType[]{EntityNBTSourceType.TYPE, BlockNBTSourceType.TYPE, StorageNBTSourceType.TYPE},
    NBTSourceType::id,
    NBTSourceType::codec,
    NBTSource::type,
    "source");
}
