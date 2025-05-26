package net.kyori.adventure.nbt.dfu;

import java.util.UUID;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.Test;

public class ShowEntityCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertCodec(
      AdventureCodecs.SHOW_ENTITY,
      HoverEvent.ShowEntity.showEntity(Key.key("minecraft:player"), UUID.randomUUID())
    );
    assertCodec(
      AdventureCodecs.SHOW_ENTITY,
      HoverEvent.ShowEntity.showEntity(Key.key("minecraft:player"), UUID.randomUUID(), Component.text("name"))
    );
  }
}
