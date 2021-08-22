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
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.Nullable;

final class ShowItemAdapter extends JsonAdapter<HoverEvent.ShowItem> {
  static final String ID = "id";
  static final String COUNT = "count";
  static final String TAG = "tag";
  private static final JsonReader.Options OPTIONS = JsonReader.Options.of(ID, COUNT, TAG);

  static JsonAdapter<HoverEvent.ShowItem> create(final Moshi moshi) {
    return new ShowItemAdapter(moshi).nullSafe();
  }

  private final Moshi moshi;

  private ShowItemAdapter(final Moshi moshi) {
    this.moshi = moshi;
  }

  @Override
  public HoverEvent.ShowItem fromJson(final JsonReader reader) throws IOException {
    reader.beginObject();

    Key key = null;
    int count = 1;
    @Nullable BinaryTagHolder nbt = null;

    while (reader.hasNext()) {
      switch (reader.selectName(OPTIONS)) {
        case 0:
          key = this.moshi.adapter(SerializerFactory.KEY_TYPE).fromJson(reader);
          break;
        case 1:
          count = reader.nextInt();
          break;
        case 2:
          final JsonReader.Token token = reader.peek();
          switch (token) {
            case STRING:
            case NUMBER:
              nbt = BinaryTagHolder.of(reader.nextString());
              break;
            case BOOLEAN:
              nbt = BinaryTagHolder.of(String.valueOf(reader.nextBoolean()));
              break;
            case NULL:
              reader.nextNull();
              break;
            default:
              throw new JsonDataException("Expected " + TAG + " to be a string");
          }
          break;
        default:
          reader.skipValue();
          break;
      }
    }

    if (key == null) {
      throw new JsonDataException("Not sure how to deserialize show_item hover event");
    }
    reader.endObject();

    return HoverEvent.ShowItem.of(key, count, nbt);
  }

  @Override
  public void toJson(final JsonWriter writer, final HoverEvent.ShowItem value) throws IOException {
    writer.beginObject();

    writer.name(ID);
    this.moshi.adapter(SerializerFactory.KEY_TYPE).toJson(writer, value.item());

    final int count = value.count();
    if (count != 1) {
      writer.name(COUNT);
      writer.value(count);
    }

    final @Nullable BinaryTagHolder nbt = value.nbt();
    if (nbt != null) {
      writer.name(TAG);
      writer.value(nbt.string());
    }

    writer.endObject();
  }
}
