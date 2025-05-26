package net.kyori.adventure.nbt.dfu;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

public class SelectorComponentCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertComponentCodec(
      Component.selector("selector")
    );
    assertComponentCodec(
      Component.selector("selector", Component.text(","))
    );
  }
}
