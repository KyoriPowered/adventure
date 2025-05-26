package net.kyori.adventure.nbt.dfu;

import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.Test;

public class ShowItemCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertCodec(
      AdventureCodecs.SHOW_ITEM,
      HoverEvent.ShowItem.showItem(
        Key.key("stone"), 1
      )
    );
    assertCodec(
      AdventureCodecs.SHOW_ITEM,
      HoverEvent.ShowItem.showItem(
        Key.key("stone"), 1,
        BinaryTagHolder.binaryTagHolder("{inValidPath: '123'}")
      )
    );
    Map<Key, DataComponentValue> componentValueMap = new HashMap<>();
    componentValueMap.put(Key.key("in_valid_path"), BinaryTagHolder.binaryTagHolder("123"));
    assertCodec(
      AdventureCodecs.SHOW_ITEM,
      HoverEvent.ShowItem.showItem(
        Key.key("stone"), 1,
        componentValueMap
      )
    );
  }
}
