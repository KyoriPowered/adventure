package net.kyori.adventure.nbt.dfu;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

public class BlockNBTComponentCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertComponentCodec(
      Component.blockNBT()
        .nbtPath("test")
        .absoluteWorldPos(1, 2, 3)
        .build()
    );
    assertComponentCodec(
      Component.blockNBT()
        .nbtPath("test")
        .localPos(1, 2, 3)
        .build()
    );
    assertComponentCodec(
      Component.blockNBT()
        .nbtPath("test")
        .relativeWorldPos(1, 2, 3)
        .build()
    );
  }
}
