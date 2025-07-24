package net.kyori.adventure.dfu.component;

import com.mojang.serialization.MapCodec;
import net.kyori.adventure.text.Component;

public class ComponentType<T extends Component> {
  private final String id;
  private final MapCodec<T> codec;

  public ComponentType(final String id, final MapCodec<T> codec) {
    this.id = id;
    this.codec = codec;
  }

  public MapCodec<T> getCodec() {
    return codec;
  }

  public String getId() {
    return id;
  }
}
