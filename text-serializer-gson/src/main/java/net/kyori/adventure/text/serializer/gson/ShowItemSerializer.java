/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.lang.reflect.Type;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTagIo;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.event.HoverEvent;

/* package */ final class ShowItemSerializer implements JsonDeserializer<HoverEvent.ShowItem>, JsonSerializer<HoverEvent.ShowItem> {
  static final String ID = "id";
  static final String COUNT = "count";
  static final String TAG = "tag";

  @Override
  public HoverEvent.ShowItem deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    final JsonObject object = json.getAsJsonObject();

    if(!object.has(ID)) {
      throw new JsonParseException("Not sure how to deserialize show_item hover event");
    }

    final Key id = context.deserialize(object.getAsJsonPrimitive(ID), Key.class);

    int count = 1;
    if(object.has(COUNT)) {
      count = object.get(COUNT).getAsInt();
    }

    CompoundBinaryTag tag = null;
    if(object.has(TAG)) {
      try {
        tag = BinaryTagIo.readString(object.get(TAG).getAsString());
      } catch(IOException e) {
        throw new JsonParseException(e);
      }
    }

    return new HoverEvent.ShowItem(id, count, tag);
  }

  @Override
  public JsonElement serialize(final HoverEvent.ShowItem src, final Type typeOfSrc, final JsonSerializationContext context) {
    final JsonObject json = new JsonObject();

    json.add(ID, context.serialize(src.item()));

    final int count = src.count();
    if(count != 1) {
      json.addProperty(COUNT, count);
    }

    final /* @Nullable */ CompoundBinaryTag tag = src.tag();
    if(tag != null) {
      try {
        json.addProperty(TAG, BinaryTagIo.writeAsString(tag));
      } catch(IOException e) {
        throw new JsonSyntaxException(e);
      }
    }

    return json;
  }
}
