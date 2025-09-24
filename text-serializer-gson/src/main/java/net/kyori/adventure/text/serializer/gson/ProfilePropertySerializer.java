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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_SIGNATURE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_VALUE;

final class ProfilePropertySerializer extends TypeAdapter<PlayerHeadObjectContents.ProfileProperty> {
  static final TypeAdapter<PlayerHeadObjectContents.ProfileProperty> INSTANCE = new ProfilePropertySerializer().nullSafe();

  private ProfilePropertySerializer() {
  }

  @Override
  public void write(final JsonWriter out, final PlayerHeadObjectContents.ProfileProperty property) throws IOException {
    out.beginObject();
    out.name(PROFILE_PROPERTY_NAME);
    out.value(property.name());
    out.name(PROFILE_PROPERTY_VALUE);
    out.value(property.value());
    if (property.signature() != null) {
      out.name(PROFILE_PROPERTY_SIGNATURE);
      out.value(property.signature());
    }
    out.endObject();
  }

  @Override
  public PlayerHeadObjectContents.ProfileProperty read(final JsonReader in) throws IOException {
    in.beginObject();
    String name = null;
    String value = null;
    String signature = null;
    while (in.hasNext()) {
      final String fieldName = in.nextName();
      if (fieldName.equals(PROFILE_PROPERTY_NAME)) {
        name = in.nextString();
      } else if (fieldName.equals(PROFILE_PROPERTY_VALUE)) {
        value = in.nextString();
      } else if (fieldName.equals(PROFILE_PROPERTY_SIGNATURE)) {
        signature = in.nextString();
      } else {
        in.skipValue();
      }
    }
    in.endObject();
    if (name == null || value == null) {
      throw new JsonParseException("A profile property requires both a " + PROFILE_PROPERTY_NAME
        + " and " + PROFILE_PROPERTY_VALUE);
    }
    return PlayerHeadObjectContents.property(name, value, signature);
  }
}
