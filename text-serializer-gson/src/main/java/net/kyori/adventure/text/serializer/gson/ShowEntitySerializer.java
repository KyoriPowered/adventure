/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

final class ShowEntitySerializer implements JsonDeserializer<HoverEvent.ShowEntity>, JsonSerializer<HoverEvent.ShowEntity> {
  static final String TYPE = "type";
  static final String ID = "id";
  static final String NAME = "name";

  @Override
  public HoverEvent.ShowEntity deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    final JsonObject object = json.getAsJsonObject();

    if(!object.has(TYPE) || !object.has(ID)) {
      throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
    }

    final Key type = context.deserialize(object.getAsJsonPrimitive(TYPE), Key.class);
    final UUID id = UUID.fromString(object.getAsJsonPrimitive(ID).getAsString());

    @Nullable Component name = null;
    if(object.has(NAME)) {
      name = context.deserialize(object.get(NAME), Component.class);
    }

    return HoverEvent.ShowEntity.of(type, id, name);
  }

  @Override
  public JsonElement serialize(final HoverEvent.ShowEntity src, final Type typeOfSrc, final JsonSerializationContext context) {
    final JsonObject json = new JsonObject();

    json.add(TYPE, context.serialize(src.type()));
    json.addProperty(ID, src.id().toString());

    final @Nullable Component name = src.name();
    if(name != null) {
      json.add(NAME, context.serialize(name));
    }

    return json;
  }
}
