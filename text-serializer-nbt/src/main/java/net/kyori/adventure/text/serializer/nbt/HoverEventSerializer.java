package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

final class HoverEventSerializer {

  private static final String HOVER_EVENT_ACTION = "action";
  private static final String HOVER_EVENT_CONTENTS = "contents";

  private static final String HOVER_EVENT_SHOW_TEXT = "show_text";
  private static final String HOVER_EVENT_SHOW_ITEM = "show_item";
  private static final String HOVER_EVENT_SHOW_ENTITY = "show_entity";
  @Deprecated
  private static final String HOVER_EVENT_SHOW_ACHIEVEMENT = "show_achievement";

  private static final String SHOW_ITEM_ID = "id";
  private static final String SHOW_ITEM_COUNT = "count";
  private static final String SHOW_ITEM_COMPONENTS = "components";

  private static final String SHOW_ENTITY_TYPE = "type";
  private static final String SHOW_ENTITY_ID = "id";
  private static final String SHOW_ENTITY_NAME = "name";

  private HoverEventSerializer() {
  }

  static <V> @NotNull CompoundBinaryTag serialize(@NotNull HoverEvent<V> event, @NotNull NBTComponentSerializerImpl serializer) {
    HoverEvent.Action<V> action = event.action();

    BinaryTag tag;
    String actionString;

    if (action == HoverEvent.Action.SHOW_TEXT) {
      tag = serializer.serialize((Component) event.value());
      actionString = HOVER_EVENT_SHOW_TEXT;
    } else if (action == HoverEvent.Action.SHOW_ITEM) {
      HoverEvent.ShowItem item = (HoverEvent.ShowItem) event.value();

      CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
        .putString(SHOW_ITEM_ID, item.item().asString())
        .putInt(SHOW_ITEM_COUNT, item.count());

      Map<Key, NBTDataComponentValue> components = item.dataComponentsAs(NBTDataComponentValue.class);

      if (!components.isEmpty()) {
        CompoundBinaryTag.Builder dataComponentsBuilder = CompoundBinaryTag.builder();

        for (Map.Entry<Key, NBTDataComponentValue> entry : components.entrySet()) {
          dataComponentsBuilder.put(entry.getKey().asString(), entry.getValue().binaryTag());
        }

        builder.put(SHOW_ITEM_COMPONENTS, dataComponentsBuilder.build());
      }

      tag = builder.build();
      actionString = HOVER_EVENT_SHOW_ITEM;
    } else if (action == HoverEvent.Action.SHOW_ENTITY) {
      HoverEvent.ShowEntity item = (HoverEvent.ShowEntity) event.value();

      CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
        .putString(SHOW_ENTITY_TYPE, item.type().asString())
        .putString(SHOW_ENTITY_ID, item.id().toString());

      Component customName = item.name();
      if (customName != null) {
        builder.put(SHOW_ENTITY_NAME, serializer.serialize(customName));
      }

      tag = builder.build();
      actionString = HOVER_EVENT_SHOW_ENTITY;
    } else if (action == HoverEvent.Action.SHOW_ACHIEVEMENT) {
      tag = StringBinaryTag.stringBinaryTag((String) event.value());
      actionString = HOVER_EVENT_SHOW_ACHIEVEMENT;
    } else {
      throw new IllegalArgumentException("Don't know how to serialize " + event + " as a HoverEvent");
    }

    return CompoundBinaryTag.builder()
      .putString(HOVER_EVENT_ACTION, actionString)
      .put(HOVER_EVENT_CONTENTS, tag)
      .build();
  }
}
