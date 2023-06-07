/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SHOW_ITEM_COUNT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SHOW_ITEM_ID;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SHOW_ITEM_TAG;

final class ShowItemSerializer extends TypeAdapter<HoverEvent.ShowItem> {
  static TypeAdapter<HoverEvent.ShowItem> create(final Gson gson) {
    return new ShowItemSerializer(gson).nullSafe();
  }

  private final Gson gson;

  private ShowItemSerializer(final Gson gson) {
    this.gson = gson;
  }

  @Override
  public HoverEvent.ShowItem read(final JsonReader in) throws IOException {
    in.beginObject();

    Key key = null;
    int count = 1;
    @Nullable BinaryTagHolder nbt = null;

    while (in.hasNext()) {
      final String fieldName = in.nextName();
      if (fieldName.equals(SHOW_ITEM_ID)) {
        key = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
      } else if (fieldName.equals(SHOW_ITEM_COUNT)) {
        count = in.nextInt();
      } else if (fieldName.equals(SHOW_ITEM_TAG)) {
        final JsonToken token = in.peek();
        if (token == JsonToken.STRING || token == JsonToken.NUMBER) {
          nbt = BinaryTagHolder.binaryTagHolder(in.nextString());
        } else if (token == JsonToken.BOOLEAN) {
          nbt = BinaryTagHolder.binaryTagHolder(String.valueOf(in.nextBoolean()));
        } else if (token == JsonToken.NULL) {
          in.nextNull();
        } else {
          throw new JsonParseException("Expected " + SHOW_ITEM_TAG + " to be a string");
        }
      } else {
        in.skipValue();
      }
    }

    if (key == null) {
      throw new JsonParseException("Not sure how to deserialize show_item hover event");
    }
    in.endObject();

    return HoverEvent.ShowItem.showItem(key, count, nbt);
  }

  @Override
  public void write(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
    out.beginObject();

    out.name(SHOW_ITEM_ID);
    this.gson.toJson(value.item(), SerializerFactory.KEY_TYPE, out);

    final int count = value.count();
    if (count != 1) {
      out.name(SHOW_ITEM_COUNT);
      out.value(count);
    }

    final @Nullable BinaryTagHolder nbt = value.nbt();
    if (nbt != null) {
      out.name(SHOW_ITEM_TAG);
      out.value(nbt.string());
    }

    out.endObject();
  }
}
