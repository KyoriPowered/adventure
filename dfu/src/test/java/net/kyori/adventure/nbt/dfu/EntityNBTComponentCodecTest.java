package net.kyori.adventure.nbt.dfu;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

public class EntityNBTComponentCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertComponentCodec(
      Component.entityNBT().nbtPath("abc").selector("test").build()
    );
    assertComponentCodec(
      Component.entityNBT().nbtPath("abc").selector("test").interpret(true).build()
    );
  }
}
