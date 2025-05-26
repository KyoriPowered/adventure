package net.kyori.adventure.nbt.dfu;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

public class KeybindComponentCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertComponentCodec(
      Component.keybind("key.jump")
    );
  }
}
