package net.kyori.adventure.nbt.dfu;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

public class StorgeNBTComponentCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertComponentCodec(
      Component.storageNBT().nbtPath("abc").storage(Key.key("doom:apple")).build()
    );
    assertComponentCodec(
      Component.storageNBT().nbtPath("abc").storage(Key.key("doom:apple")).interpret(true).build()
    );
  }
}
