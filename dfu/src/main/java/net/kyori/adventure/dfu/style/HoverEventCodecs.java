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
import java.util.Optional;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.dfu.Codecs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_NAME;

public class HoverEventCodecs {
  public static final MapCodec<HoverEvent.ShowItem> SHOW_ITEM_CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(
      AdventureCodecs.KEY.fieldOf("id").forGetter(HoverEvent.ShowItem::item),
      Codec.INT.fieldOf("count").forGetter(HoverEvent.ShowItem::count),
      AdventureCodecs.BINARY_TAG_HOLDER.optionalFieldOf("tag").forGetter(t -> Optional.ofNullable(t.nbt())),
      Codec.unboundedMap(AdventureCodecs.KEY, AdventureCodecs.DATA_COMPONENT_VALUE).fieldOf("components").forGetter(HoverEvent.ShowItem::dataComponents)
    ).apply(instance, (id, count, nbt, dataComponentValueMap) -> {
      if (!dataComponentValueMap.isEmpty()) {
        return HoverEvent.ShowItem.showItem(id, count, dataComponentValueMap);
      }
      return nbt.map(binaryTagHolder -> HoverEvent.ShowItem.showItem(id, count, binaryTagHolder))
        .orElseGet(() -> HoverEvent.ShowItem.showItem(id, count));
    })
  );
  public static final MapCodec<HoverEvent.ShowEntity> SHOW_ENTITY_CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(
      AdventureCodecs.KEY.fieldOf("type").forGetter(HoverEvent.ShowEntity::type),
      AdventureCodecs.UUID.fieldOf(SHOW_ENTITY_ID).forGetter(HoverEvent.ShowEntity::id),
      Codecs.COMPONENT.optionalFieldOf(SHOW_ENTITY_NAME).forGetter(c -> Optional.ofNullable(c.name()))
    ).apply(instance, (key, uuid, name) ->
      name.map(component -> HoverEvent.ShowEntity.showEntity(key, uuid, component)).orElseGet(() -> HoverEvent.ShowEntity.showEntity(key, uuid))
    ));

  private static final Map<HoverEvent.Action<?>, MapCodec<HoverEvent<?>>> MAPPING = createMapping();

  public static final Codec<HoverEvent.Action<?>> ACTION_CODEC = Codecs.createIndexCodec(() -> HoverEvent.Action.NAMES.values().toArray(new HoverEvent.Action<?>[0]), HoverEvent.Action.NAMES);
  public static final Codec<HoverEvent<?>> CODEC = ACTION_CODEC.dispatch("action", HoverEvent::action, MAPPING::get);

  @SuppressWarnings("unchecked")
  private static Map<HoverEvent.Action<?>, MapCodec<HoverEvent<?>>> createMapping() {
    final HashMap<HoverEvent.Action<?>, MapCodec<HoverEvent<?>>> map = new HashMap<>();
    map.put(HoverEvent.Action.SHOW_TEXT, RecordCodecBuilder.mapCodec(instance ->
      instance.group(
        Codecs.COMPONENT.fieldOf("value").forGetter(h -> ((Component) h.value()))
      ).apply(instance, HoverEvent::showText)
    ));
    map.put(HoverEvent.Action.SHOW_ITEM, ((MapCodec) SHOW_ITEM_CODEC));
    map.put(HoverEvent.Action.SHOW_ENTITY, ((MapCodec) SHOW_ENTITY_CODEC));

    return map;
  }
}
