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
package net.kyori.adventure.text.serializer.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.Nullable;

final class ShowEntityAdapter extends JsonAdapter<HoverEvent.ShowEntity> {
  static final String TYPE = "type";
  static final String ID = "id";
  static final String NAME = "name";
  private static final JsonReader.Options OPTIONS = JsonReader.Options.of(TYPE, ID, NAME);

  static JsonAdapter<HoverEvent.ShowEntity> create(final Moshi moshi) {
    return new ShowEntityAdapter(moshi).nullSafe();
  }

  private final Moshi moshi;

  private ShowEntityAdapter(final Moshi moshi) {
    this.moshi = moshi;
  }

  @Override
  @SuppressWarnings("unchecked")
  public HoverEvent.ShowEntity fromJson(final JsonReader reader) throws IOException {
    reader.beginObject();

    Key type = null;
    UUID id = null;
    @Nullable Component name = null;

    while (reader.hasNext()) {
      switch (reader.selectName(OPTIONS)) {
        case 0:
          type = this.moshi.adapter(SerializerFactory.KEY_TYPE).fromJson(reader);
          break;
        case 1:
          id = UUID.fromString(reader.nextString());
          break;
        case 2:
          name = this.moshi.adapter(SerializerFactory.COMPONENT_TYPE).fromJson(reader);
          break;
        default:
          reader.skipValue();
          break;
      }
    }

    if (type == null || id == null) {
      throw new JsonDataException("A show entity hover event needs type and id fields to be deserialized");
    }
    reader.endObject();

    return HoverEvent.ShowEntity.of(type, id, name);
  }

  @Override
  public void toJson(final JsonWriter writer, final HoverEvent.ShowEntity value) throws IOException {
    writer.beginObject();

    writer.name(TYPE);
    this.moshi.adapter(SerializerFactory.KEY_TYPE).toJson(writer, value.type());

    writer.name(ID);
    writer.value(value.id().toString());

    final @Nullable Component name = value.name();
    if (name != null) {
      writer.name(NAME);
      this.moshi.adapter(SerializerFactory.COMPONENT_TYPE).toJson(writer, name);
    }

    writer.endObject();
  }
}
