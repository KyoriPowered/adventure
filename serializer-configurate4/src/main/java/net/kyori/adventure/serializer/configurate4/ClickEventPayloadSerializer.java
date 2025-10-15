/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.serializer.configurate4;

import io.leangen.geantyref.GenericTypeReflector;
import java.lang.reflect.Type;
import net.kyori.adventure.text.event.ClickEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

final class ClickEventPayloadSerializer implements TypeSerializer<ClickEvent.Payload> {
  static final TypeSerializer<ClickEvent.Payload> INSTANCE = new ClickEventPayloadSerializer();

  private ClickEventPayloadSerializer() {
  }

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
