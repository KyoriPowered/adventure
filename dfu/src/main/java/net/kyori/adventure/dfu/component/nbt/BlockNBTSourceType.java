package net.kyori.adventure.dfu.component.nbt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponentBuilder;

public final class BlockNBTSourceType extends NBTSource {
  public static final MapCodec<BlockNBTSourceType> CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(Codec.STRING.fieldOf("block").forGetter(BlockNBTSourceType::rawPos))
      .apply(instance, BlockNBTSourceType::new));
  public static final NBTSourceType TYPE = new NBTSourceType("block", CODEC);
  private final String rawPos;

  public BlockNBTSourceType(String rawPos) {
    this.rawPos = rawPos;
  }

  @Override
  public NBTComponentBuilder builder() {
    return Component.blockNBT()
      .pos(BlockNBTComponent.Pos.fromString(rawPos));
  }

  @Override
  public NBTSourceType type() {
    return TYPE;
  }

  public String rawPos() {
    return rawPos;
  }
}
