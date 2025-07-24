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
package net.kyori.adventure.dfu.style;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.dfu.Codecs;
import net.kyori.adventure.text.event.ClickEvent;

public class ClickEventCodecs {
  private static final Map<ClickEvent.Action, MapCodec<ClickEvent>> MAPPING = createMapping();

  public static final Codec<ClickEvent.Action> ACTION_CODEC = Codecs.createIndexCodec(ClickEvent.Action::values, ClickEvent.Action.NAMES);
  public static final Codec<ClickEvent> CODEC = ACTION_CODEC.dispatch("action", ClickEvent::action, MAPPING::get);

  private static Map<ClickEvent.Action, MapCodec<ClickEvent>> createMapping() {
    final HashMap<ClickEvent.Action, MapCodec<ClickEvent>> map = new HashMap<>();
    map.put(ClickEvent.Action.OPEN_URL, RecordCodecBuilder.mapCodec(instance ->
      instance.group(
        Codec.STRING.fieldOf("url").forGetter(ClickEvent::value)
      ).apply(instance, ClickEvent::openUrl)
    ));
    map.put(ClickEvent.Action.OPEN_FILE, RecordCodecBuilder.mapCodec(instance ->
      instance.group(
        Codec.STRING.fieldOf("path").forGetter(ClickEvent::value)
      ).apply(instance, ClickEvent::openFile)
    ));
    map.put(ClickEvent.Action.RUN_COMMAND, RecordCodecBuilder.mapCodec(instance ->
      instance.group(
        Codec.STRING.fieldOf("command").forGetter(ClickEvent::value)
      ).apply(instance, ClickEvent::runCommand)
    ));
    map.put(ClickEvent.Action.SUGGEST_COMMAND, RecordCodecBuilder.mapCodec(instance ->
      instance.group(
        Codec.STRING.fieldOf("command").forGetter(ClickEvent::value)
      ).apply(instance, ClickEvent::suggestCommand)
    ));
    // todo show dialog
    map.put(ClickEvent.Action.CHANGE_PAGE, RecordCodecBuilder.mapCodec(instance ->
      instance.group(
        Codec.INT.fieldOf("page").forGetter(e -> ((ClickEvent.Payload.Int) e.payload()).integer())
      ).apply(instance, ClickEvent::changePage)
    ));
    map.put(ClickEvent.Action.COPY_TO_CLIPBOARD, RecordCodecBuilder.mapCodec(instance ->
      instance.group(
        Codec.STRING.fieldOf("text").forGetter(ClickEvent::value)
      ).apply(instance, ClickEvent::copyToClipboard)
    ));

    map.put(ClickEvent.Action.CUSTOM, RecordCodecBuilder.mapCodec(instance ->
      instance.group(
        AdventureCodecs.KEY.fieldOf("id").forGetter(e -> ((ClickEvent.Payload.Custom) e.payload()).key()),
        AdventureCodecs.BINARY_TAG_HOLDER.fieldOf("payload").forGetter(e -> ((ClickEvent.Payload.Custom) e.payload()).nbt())
      ).apply(instance, ClickEvent::custom)
    ));

    return map;
  }
}
