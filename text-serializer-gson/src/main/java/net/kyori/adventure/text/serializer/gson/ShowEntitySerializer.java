/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.Nullable;

final class ShowEntitySerializer extends TypeAdapter<HoverEvent.ShowEntity> {
  static final String TYPE = "type";
  static final String ID = "id";
  static final String NAME = "name";

  static TypeAdapter<HoverEvent.ShowEntity> create(final Gson gson) {
    return new ShowEntitySerializer(gson).nullSafe();
  }

  private final Gson gson;

  private ShowEntitySerializer(final Gson gson) {
    this.gson = gson;
  }

  @Override
  public HoverEvent.ShowEntity read(final JsonReader in) throws IOException {
    in.beginObject();

    Key type = null;
    UUID id = null;
    @Nullable Component name = null;

    while (in.hasNext()) {
      final String fieldName = in.nextName();
      if (fieldName.equals(TYPE)) {
        type = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
      } else if (fieldName.equals(ID)) {
        id = UUID.fromString(in.nextString());
      } else if (fieldName.equals(NAME)) {
        name = this.gson.fromJson(in, SerializerFactory.COMPONENT_TYPE);
      } else {
        in.skipValue();
      }
    }

    if (type == null || id == null) {
      throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
    }
    in.endObject();

    return HoverEvent.ShowEntity.of(type, id, name);
  }

  @Override
  public void write(final JsonWriter out, final HoverEvent.ShowEntity value) throws IOException {
    out.beginObject();

    out.name(TYPE);
    this.gson.toJson(value.type(), SerializerFactory.KEY_TYPE, out);

    out.name(ID);
    out.value(value.id().toString());

    final @Nullable Component name = value.name();
    if (name != null) {
      out.name(NAME);
      this.gson.toJson(name, SerializerFactory.COMPONENT_TYPE, out);
    }

    out.endObject();
  }
}
