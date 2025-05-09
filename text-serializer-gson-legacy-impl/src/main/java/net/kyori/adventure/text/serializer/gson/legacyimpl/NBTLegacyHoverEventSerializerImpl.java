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
package net.kyori.adventure.text.serializer.gson.legacyimpl;

import java.io.IOException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import net.kyori.adventure.text.serializer.json.legacyimpl.NBTLegacyHoverEventSerializer;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;

@Deprecated
final class NBTLegacyHoverEventSerializerImpl implements LegacyHoverEventSerializer {
  static final NBTLegacyHoverEventSerializerImpl INSTANCE = new NBTLegacyHoverEventSerializerImpl();

  static final net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer NEW_INSTANCE = NBTLegacyHoverEventSerializer.get();

  private NBTLegacyHoverEventSerializerImpl() {
  }

  @Override
  public HoverEvent.@NotNull ShowItem deserializeShowItem(final @NotNull Component input) throws IOException {
    return NEW_INSTANCE.deserializeShowItem(input);
  }

  @Override
  public HoverEvent.@NotNull ShowEntity deserializeShowEntity(final @NotNull Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
    return NEW_INSTANCE.deserializeShowEntity(input, componentCodec);
  }

  @Override
  public @NotNull Component serializeShowItem(final HoverEvent.@NotNull ShowItem input) throws IOException {
    return NEW_INSTANCE.serializeShowItem(input);
  }

  @Override
  public @NotNull Component serializeShowEntity(final HoverEvent.@NotNull ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
    return NEW_INSTANCE.serializeShowEntity(input, componentCodec);
  }
}
