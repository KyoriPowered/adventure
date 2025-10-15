package net.kyori.adventure.serializer.configurate4;

import io.leangen.geantyref.GenericTypeReflector;
import java.lang.reflect.Type;
import net.kyori.adventure.text.event.ClickEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public class ClickEventPayloadSerializer implements TypeSerializer<ClickEvent.Payload> {
  @Override
  public ClickEvent.Payload deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
    final Class<? extends ClickEvent.Payload> raw = GenericTypeReflector.erase(type).asSubclass(ClickEvent.Payload.class);
    if (ClickEvent.Payload.Custom.class.equals(raw) || ClickEvent.Payload.Dialog.class.equals(raw)) {
      throw new SerializationException("Do not know how to deserialize a " + raw.getSimpleName() + " payload");
    } else if (ClickEvent.Payload.Int.class.equals(raw)) {
      return ClickEvent.Payload.integer(node.getInt());
    } else if (ClickEvent.Payload.Text.class.equals(raw)) {
      return ClickEvent.Payload.string(node.getString());
    } else {
      throw new SerializationException("Do not know how to deserialize a " + raw.getSimpleName() + " payload!");
    }
  }

  @Override
  public void serialize(final Type type, final ClickEvent.@Nullable Payload obj, final ConfigurationNode node) throws SerializationException {
    switch (obj) {
      case null -> node.set(null);
      case ClickEvent.Payload.Custom c -> throw new SerializationException("Do not know how to serialize a custom payload");
      case ClickEvent.Payload.Dialog d -> throw new SerializationException("Do not know how to serialize a dialog");
      case ClickEvent.Payload.Int i -> node.set(i.integer());
      case ClickEvent.Payload.Text s -> node.set(s.value());
    }

  }
}
