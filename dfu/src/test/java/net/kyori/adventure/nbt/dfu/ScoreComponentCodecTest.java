package net.kyori.adventure.nbt.dfu;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

public class ScoreComponentCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertComponentCodec(
      Component.score("test", "obj")
    );
    assertComponentCodec(
      Component.score("test", "obj", "value")
    );
  }
}
