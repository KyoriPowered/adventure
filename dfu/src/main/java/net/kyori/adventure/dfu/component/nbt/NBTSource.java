package net.kyori.adventure.dfu.component.nbt;

import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.StorageNBTComponent;

public abstract class NBTSource {
  public abstract NBTComponentBuilder builder();

  public abstract NBTSourceType type();

  public static NBTSource from(final NBTComponent component) {
    if (component instanceof BlockNBTComponent c) {
      return new BlockNBTSourceType(c.pos().asString());
    } else if (component instanceof EntityNBTComponent c) {
      return new EntityNBTSourceType(c.selector());
    } else if (component instanceof StorageNBTComponent c) {
      return new StorageNBTSourceType(c.storage());
    } else throw new IllegalArgumentException("Unknown NBT component type: " + component.getClass().getName());
  }
}
