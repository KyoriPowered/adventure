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
package net.kyori.adventure.dfu;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import net.kyori.adventure.util.Index;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.COLOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.EXTRA;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.FONT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.INSERTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.KEYBIND;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_BLOCK;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_ENTITY;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_INTERPRET;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_STORAGE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE_OBJECTIVE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE_VALUE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SELECTOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SEPARATOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_TYPE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_COMPONENTS;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_COUNT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_TAG;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TEXT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE_FALLBACK;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE_WITH;

/**
 * Adventure codecs.
 *
 * @since 4.22.0
 */
public final class AdventureCodecs {
  private AdventureCodecs() {
  }

  // key
  public static final Codec<Key> KEY = xmap(Codec.STRING, Key::key, Key::asString, "Key");
  // uuid
  public static final Codec<UUID> UUID = xmap(Codec.list(Codec.LONG), longs -> new UUID(longs.get(0), longs.get(1)), uuid -> Arrays.asList(uuid.getLeastSignificantBits(), uuid.getMostSignificantBits()), "UUID");
  // TextColor
  public static final Codec<TextColor> TEXT_COLOR = xmap(Codec.INT, TextColor::color, TextColor::value, "TextColor");
  public static final Codec<ShadowColor> SHADOW_COLOR = xmap(Codec.INT, ShadowColor::shadowColor, ShadowColor::value, "ShadowColor");
  // TranslationArgument
  public static final Codec<TranslationArgument> TRANSLATION_ARGUMENT = new TranslationArgumentCodec();
  // indexed
  public static final Codec<NamedTextColor> NAMED_TEXT_COLOR = indexCode(Codec.STRING, NamedTextColor.NAMES, "NamedTextColor");
  public static final Codec<BossBar.Color> BOSS_BAR_COLOR = indexCode(Codec.STRING, BossBar.Color.NAMES, "BossBar.Color");
  public static final Codec<BossBar.Flag> BOSS_BAR_FLAG = indexCode(Codec.STRING, BossBar.Flag.NAMES, "BossBar.Flag");
  public static final Codec<BossBar.Overlay> BOSS_BAR_OVERLAY = indexCode(Codec.STRING, BossBar.Overlay.NAMES, "BossBar.Overlay");
  public static final Codec<Sound.Source> SOUND_SOURCE = indexCode(Codec.STRING, Sound.Source.NAMES, "Sound.Source");
  public static final Codec<ClickEvent.Action> CLICK_ACTION = indexCode(Codec.STRING, ClickEvent.Action.NAMES, "ClickEvent.Action");
  public static final Codec<HoverEvent.Action<?>> HOVER_ACTION = indexCode(Codec.STRING, HoverEvent.Action.NAMES, "HoverEvent.Action");
  // TextDecoration
  public static final Codec<TextDecoration> TEXT_DECORATION = indexCode(Codec.STRING, TextDecoration.NAMES, "TextDecoration");
  public static final Codec<TextDecoration.State> TEXT_DECORATION_STATE = indexCode(Codec.STRING, Index.create(TextDecoration.State.class, TextDecoration.State::toString), "TextDecoration.State");

  public static final Codec<BinaryTagHolder> BINARY_TAG_HOLDER = xmap(Codec.STRING, BinaryTagHolder::binaryTagHolder, BinaryTagHolder::string, "BinaryTagHolder");
  public static final Codec<BlockNBTComponent.WorldPos.Coordinate> COORDINATE = RecordCodecBuilder.create(instance -> instance.group(
      Codec.INT.fieldOf("type").forGetter(c -> c.type().ordinal()),
      Codec.INT.fieldOf("value").forGetter(BlockNBTComponent.WorldPos.Coordinate::value)
    ).apply(instance, (type, value) ->
      BlockNBTComponent.WorldPos.Coordinate.coordinate(value, BlockNBTComponent.WorldPos.Coordinate.Type.values()[type]))
  );
  public static final Codec<BlockNBTComponent.Pos> POS = xmap(Codec.STRING, BlockNBTComponent.Pos::fromString, BlockNBTComponent.Pos::asString, "BlockNBTComponent.Pos");
  public static final Codec<ClickEvent> CLICK_EVENT = RecordCodecBuilder.create(instance -> instance.group(
    CLICK_ACTION.fieldOf("action").forGetter(ClickEvent::action),
    Codec.STRING.fieldOf("value").forGetter(ClickEvent::value)
  ).apply(instance, ClickEvent::clickEvent));

  public static final Codec<Style> STYLE;
  public static final Codec<DataComponentValue> DATA_COMPONENT_VALUE = RecordCodecBuilder.create(instance -> instance.group(
    Codec.STRING.fieldOf("type").forGetter(c -> c instanceof DataComponentValue.Removed ? "removed" : c instanceof DataComponentValue.TagSerializable ? "tag" : "unknown"),
    Codec.STRING.optionalFieldOf("value").forGetter(c -> c instanceof DataComponentValue.TagSerializable ? Optional.of(((DataComponentValue.TagSerializable) c).asBinaryTag().string()) : Optional.empty())
  ).apply(instance, (type, value) -> {
    if (Objects.equals("removed", type)) {
      return DataComponentValue.removed();
    } else if (Objects.equals("tag", type)) {
      return value.map(BinaryTagHolder::binaryTagHolder).orElse(null);
    }
    throw new IllegalArgumentException("Unknown type: " + type);
  }));
  public static final Codec<HoverEvent.ShowItem> SHOW_ITEM = RecordCodecBuilder.create(instance -> instance.group(
      KEY.fieldOf(SHOW_ITEM_ID).forGetter(HoverEvent.ShowItem::item),
      Codec.INT.fieldOf(SHOW_ITEM_COUNT).forGetter(HoverEvent.ShowItem::count),
      BINARY_TAG_HOLDER.optionalFieldOf(SHOW_ITEM_TAG).forGetter(c -> Optional.ofNullable(c.nbt())),
      Codec.unboundedMap(KEY, DATA_COMPONENT_VALUE).fieldOf(SHOW_ITEM_COMPONENTS).forGetter(HoverEvent.ShowItem::dataComponents)
    ).apply(instance, (id, count, nbt, dataComponentValueMap) ->
      nbt.map(binaryTagHolder -> HoverEvent.ShowItem.showItem(id, count, binaryTagHolder)).orElseGet(() -> HoverEvent.ShowItem.showItem(id, count, dataComponentValueMap)))
  );
  public static final Codec<HoverEvent.ShowEntity> SHOW_ENTITY;
  public static final Codec<HoverEvent<?>> HOVER_EVENT;

  private static final Codec<BlockNBTComponent> BLOCK_NBT_CODEC;
  private static final Codec<EntityNBTComponent> ENTITY_NBT_CODEC;
  private static final Codec<StorageNBTComponent> STORAGE_NBT_CODEC;
  private static final Codec<KeybindComponent> KEYBIND_CODEC;
  private static final Codec<ScoreComponent> SCORE_CODEC;
  private static final Codec<SelectorComponent> SELECTOR_CODEC;
  private static final Codec<TranslatableComponent> TRANSLATABLE_CODEC;
  private static final Codec<Component> INTERNAL_COMPONENT;

  static {
    INTERNAL_COMPONENT = RecordCodecBuilder.create(new Function<>() {
      @Override
      public App<RecordCodecBuilder.Mu<Component>, Component> apply(final RecordCodecBuilder.Instance<Component> instance) {
        return instance.group(
          Codec.lazyInitialized(() -> STYLE).optionalFieldOf("style").forGetter(c -> Optional.of(c.style())),
          Codec.lazyInitialized(() -> INTERNAL_COMPONENT).listOf().optionalFieldOf(EXTRA, Collections.emptyList()).forGetter(Component::children),
          Codec.STRING.optionalFieldOf(TEXT).forGetter(c -> Optional.ofNullable(c instanceof TextComponent ? ((TextComponent) c).content() : null)),
          Codec.lazyInitialized(() -> BLOCK_NBT_CODEC).optionalFieldOf(NBT_BLOCK).forGetter(c -> Optional.ofNullable(c instanceof BlockNBTComponent ? ((BlockNBTComponent) c) : null)),
          Codec.lazyInitialized(() -> ENTITY_NBT_CODEC).optionalFieldOf(NBT_ENTITY).forGetter(c -> Optional.ofNullable(c instanceof EntityNBTComponent ? ((EntityNBTComponent) c) : null)),
          Codec.lazyInitialized(() -> STORAGE_NBT_CODEC).optionalFieldOf(NBT_STORAGE).forGetter(c -> Optional.ofNullable(c instanceof StorageNBTComponent ? ((StorageNBTComponent) c) : null)),
          Codec.lazyInitialized(() -> SCORE_CODEC).optionalFieldOf(SCORE).forGetter(c -> Optional.ofNullable(c instanceof ScoreComponent ? ((ScoreComponent) c) : null)),
          Codec.lazyInitialized(() -> SELECTOR_CODEC).optionalFieldOf(SELECTOR).forGetter(c -> Optional.ofNullable(c instanceof SelectorComponent ? ((SelectorComponent) c) : null)),
          Codec.lazyInitialized(() -> KEYBIND_CODEC).optionalFieldOf(KEYBIND).forGetter(c -> Optional.ofNullable(c instanceof KeybindComponent ? ((KeybindComponent) c) : null)),
          Codec.lazyInitialized(() -> TRANSLATABLE_CODEC).optionalFieldOf(TRANSLATE).forGetter(c -> Optional.ofNullable(c instanceof TranslatableComponent ? ((TranslatableComponent) c) : null))
        ).apply(instance, (style, children, textContent, blockNBTComponent, entityNBTComponent, storageNBTComponent, scoreComponent, selectorComponent, keybindComponent, translatableComponent) -> {
          final Consumer<ComponentBuilder<?, ?>> consumer = builder -> {
            style.ifPresent(builder::style);
            builder.append(children);
          };
          final BuildableComponent<?, ?> baseComponent;
          if (textContent.isPresent()) {
            baseComponent = Component.text(textContent.get());
          } else if (translatableComponent.isPresent()) {
            baseComponent = translatableComponent.get();
          } else if (keybindComponent.isPresent()) {
            baseComponent = keybindComponent.get();
          } else if (scoreComponent.isPresent()) {
            baseComponent = scoreComponent.get();
          } else if (selectorComponent.isPresent()) {
            baseComponent = selectorComponent.get();
          } else if (storageNBTComponent.isPresent()) {
            baseComponent = storageNBTComponent.get();
          } else if (entityNBTComponent.isPresent()) {
            baseComponent = entityNBTComponent.get();
          } else if (blockNBTComponent.isPresent()) {
            baseComponent = blockNBTComponent.get();
          } else {
            throw new IllegalArgumentException("Component is not a valid type");
          }
          final ComponentBuilder<?, ?> builder = baseComponent.toBuilder();
          consumer.accept(builder);
          return builder.build();
        });
      }
    });
    SHOW_ENTITY = RecordCodecBuilder.create(i -> i.group(
        KEY.fieldOf(SHOW_ENTITY_TYPE).forGetter(HoverEvent.ShowEntity::type),
        UUID.fieldOf(SHOW_ENTITY_ID).forGetter(HoverEvent.ShowEntity::id),
        INTERNAL_COMPONENT.optionalFieldOf(SHOW_ENTITY_NAME).forGetter(c -> Optional.ofNullable(c.name()))
      ).apply(i, (key, uuid, name) -> name.map(component -> HoverEvent.ShowEntity.showEntity(key, uuid, component)).orElseGet(() -> HoverEvent.ShowEntity.showEntity(key, uuid)))
    );
    HOVER_EVENT = RecordCodecBuilder.create(instance -> instance.group(
      HOVER_ACTION.fieldOf(HOVER_EVENT_ACTION).forGetter(HoverEvent::action),
      SHOW_ITEM.optionalFieldOf("showItem").forGetter(c -> c.value() instanceof HoverEvent.ShowItem ? Optional.of(((HoverEvent.ShowItem) c.value())) : Optional.empty()),
      INTERNAL_COMPONENT.optionalFieldOf("showText").forGetter(c -> c.value() instanceof Component ? Optional.of((Component) c.value()) : Optional.empty()),
      SHOW_ENTITY.optionalFieldOf("showEntity").forGetter(c -> c.value() instanceof HoverEvent.ShowEntity ? Optional.of(((HoverEvent.ShowEntity) c.value())) : Optional.empty()),
      Codec.STRING.optionalFieldOf("showAchievement").forGetter(c -> c.value() instanceof String ? Optional.of((String) c.value()) : Optional.empty())
    ).apply(instance, (action, showItem, component, showEntity, achievement) -> {
      if (action.equals(HoverEvent.Action.SHOW_TEXT)) {
        return HoverEvent.showText(component.get());
      } else if (action.equals(HoverEvent.Action.SHOW_ENTITY)) {
        return HoverEvent.showEntity(showEntity.get());
      } else if (action.equals(HoverEvent.Action.SHOW_ITEM)) {
        return HoverEvent.showItem(showItem.get());
      } else if (action.equals(HoverEvent.Action.SHOW_ACHIEVEMENT)) {
        return HoverEvent.showAchievement(achievement.get());
      } else {
        throw new IllegalArgumentException("Unknown hover event action " + action);
      }
    }));
    STYLE = RecordCodecBuilder.create(instance -> instance.group(
      KEY.optionalFieldOf(FONT).forGetter(s -> Optional.ofNullable(s.font())),
      TEXT_COLOR.optionalFieldOf(COLOR).forGetter(s -> Optional.ofNullable(s.color())),
      SHADOW_COLOR.optionalFieldOf(ComponentTreeConstants.SHADOW_COLOR).forGetter(s -> Optional.ofNullable(s.shadowColor())),
      Codec.STRING.optionalFieldOf(INSERTION).forGetter(s -> Optional.ofNullable(s.insertion())),
      CLICK_EVENT.optionalFieldOf(ComponentTreeConstants.CLICK_EVENT).forGetter(s -> Optional.ofNullable(s.clickEvent())),
      Codec.lazyInitialized(() -> HOVER_EVENT).optionalFieldOf(ComponentTreeConstants.HOVER_EVENT).forGetter(s -> Optional.ofNullable(s.hoverEvent())),
      Codec.unboundedMap(TEXT_DECORATION, TEXT_DECORATION_STATE).optionalFieldOf(
        "decoration",
        Collections.emptyMap()
      ).forGetter(Style::decorations)
    ).apply(instance, (font, textColor, shadowColor, insertion, clickEvent, hoverEvent, decoration) -> {
      final Style.Builder builder = Style.style();
      font.ifPresent(builder::font);
      textColor.ifPresent(builder::color);
      shadowColor.ifPresent(builder::shadowColor);
      insertion.ifPresent(builder::insertion);
      clickEvent.ifPresent(builder::clickEvent);
      hoverEvent.ifPresent(builder::hoverEvent);
      decoration.forEach(builder::decoration);
      return builder.build();
    }));
    BLOCK_NBT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      POS.fieldOf(NBT_BLOCK).forGetter(BlockNBTComponent::pos),
      Codec.STRING.fieldOf(NBT).forGetter(NBTComponent::nbtPath),
      Codec.BOOL.fieldOf(NBT_INTERPRET).forGetter(NBTComponent::interpret),
      INTERNAL_COMPONENT.optionalFieldOf(SEPARATOR).forGetter(c -> Optional.ofNullable(c.separator()))
    ).apply(instance, (pos, nbtPath, interpret, separator) -> {
      final BlockNBTComponent.Builder builder = Component.blockNBT();
      builder.pos(pos).interpret(interpret);
      builder.nbtPath(nbtPath);
      separator.ifPresent(builder::separator);
      return builder.build();
    }));
    ENTITY_NBT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.fieldOf(SELECTOR).forGetter(c -> c.selector()),
      Codec.BOOL.fieldOf(NBT_INTERPRET).forGetter(NBTComponent::interpret),
      Codec.STRING.fieldOf(NBT).forGetter(NBTComponent::nbtPath),
      INTERNAL_COMPONENT.optionalFieldOf(SEPARATOR).forGetter(c -> Optional.ofNullable(c.separator()))
    ).apply(instance, (selector, interpret, nbtPath, separator) -> {
      final EntityNBTComponent.Builder builder = Component.entityNBT()
        .selector(selector)
        .interpret(interpret)
        .nbtPath(nbtPath);
      separator.ifPresent(builder::separator);
      return builder.build();
    }));
    STORAGE_NBT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      KEY.fieldOf(NBT_STORAGE).forGetter(StorageNBTComponent::storage),
      Codec.BOOL.fieldOf(NBT_INTERPRET).forGetter(NBTComponent::interpret),
      Codec.STRING.fieldOf(NBT).forGetter(NBTComponent::nbtPath),
      INTERNAL_COMPONENT.optionalFieldOf(SEPARATOR).forGetter(c -> Optional.ofNullable(c.separator()))
    ).apply(instance, (storage, interpret, nbtPath, separator) -> {
      final StorageNBTComponent.Builder builder = Component.storageNBT()
        .storage(storage)
        .interpret(interpret)
        .nbtPath(nbtPath);
      separator.ifPresent(builder::separator);
      return builder.build();
    }));
    KEYBIND_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.fieldOf(KEYBIND).forGetter(c -> c.keybind())
    ).apply(instance, Component::keybind));
    SCORE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.fieldOf(SCORE_NAME).forGetter(ScoreComponent::name),
      Codec.STRING.fieldOf(SCORE_OBJECTIVE).forGetter(ScoreComponent::objective),
      Codec.STRING.optionalFieldOf(SCORE_VALUE).forGetter(c -> Optional.ofNullable(c.value()))
    ).apply(instance, (name, objective, value) -> {
      final ScoreComponent.Builder builder = Component.score()
        .name(name)
        .objective(objective);
      value.ifPresent(builder::value);
      return builder.build();
    }));
    SELECTOR_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.fieldOf(SELECTOR).forGetter(SelectorComponent::pattern),
      INTERNAL_COMPONENT.optionalFieldOf(SEPARATOR).forGetter(c -> Optional.ofNullable(c.separator()))
    ).apply(instance, (pattern, separator) -> {
      final SelectorComponent.Builder builder = Component.selector()
        .pattern(pattern);
      separator.ifPresent(builder::separator);
      return builder.build();
    }));
    TRANSLATABLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.STRING.fieldOf(TRANSLATE).forGetter(TranslatableComponent::key),
      TRANSLATION_ARGUMENT.listOf().fieldOf(TRANSLATE_WITH).forGetter(TranslatableComponent::arguments),
      Codec.STRING.optionalFieldOf(TRANSLATE_FALLBACK).forGetter(c -> Optional.ofNullable(c.fallback()))
    ).apply(instance, (key, translationArguments, fallback) -> {
      final TranslatableComponent.Builder builder = Component.translatable()
        .key(key)
        .arguments(translationArguments);
      fallback.ifPresent(builder::fallback);
      return builder.build();
    }));
  }

  public static final Codec<Component> COMPONENT = INTERNAL_COMPONENT;

  private static <A, S> Codec<S> xmap(final Codec<A> codec, final Function<? super A, ? extends S> to, final Function<? super S, ? extends A> from, final String name) {
    return Codec.of(codec.comap(from), codec.map(to), name);
  }

  /**
   * Creates a codec for an indexed type.
   *
   * @param codec the codec
   * @param index the index
   * @param name  the name
   * @param <K>   the key type
   * @param <C>   the component type
   * @return a codec
   * @since 4.22.0
   */
  public static <K, C> Codec<C> indexCode(final Codec<K> codec, final Index<K, C> index, final String name) {
    return Codec.of(
      codec.comap(index::keyOrThrow),
      codec.map(index::valueOrThrow),
      name
    );
  }

  /**
   * Translation argument codec.
   *
   * @since 4.22.0
   */
  public static class TranslationArgumentCodec implements Codec<TranslationArgument> {
    @Override
    public <T> DataResult<T> encode(final TranslationArgument input, final DynamicOps<T> ops, final T prefix) {
      final Object object = input.value();
      T type = null;
      T value = null;
      if (object instanceof Boolean) {
        type = ops.createString("bool");
        value = ops.createBoolean((Boolean) object);
      } else if (object instanceof Number) {
        type = ops.createString("number");
        value = ops.createNumeric((Number) object);
      } else if (object instanceof Component) {
        type = ops.createString("component");
        final DataResult<T> result = COMPONENT.encode((Component) object, ops, prefix);
        if (result.isError()) {
          return result;
        }
        value = result.getOrThrow();
      }
      if (type != null && value != null) {
        return ops.mapBuilder().add("type", type).add("value", value).build(prefix);
      }
      return DataResult.error(() -> "Unknown value: " + object);
    }

    @Override
    public <T> DataResult<Pair<TranslationArgument, T>> decode(final DynamicOps<T> ops, final T input) {

      return ops.getMap(input).flatMap(map -> ops.getStringValue(map.get("type")).flatMap(type -> {
        final T value = map.get("value");
        if (Objects.equals(type, "component")) {
          return COMPONENT.decode(ops, value).map(t -> Pair.of(TranslationArgument.component(t.getFirst()), t.getSecond()));
        }
        if (Objects.equals(type, "bool")) {
          return DataResult.success(Pair.of(ops.getBooleanValue(value).map(TranslationArgument::bool).getOrThrow(), value));
        }
        if (Objects.equals(type, "number")) {
          return DataResult.success(Pair.of(ops.getNumberValue(value).map(TranslationArgument::numeric).getOrThrow(), value));
        }
        return DataResult.error(() -> "Unexpected value: " + type);
      }));
    }

    @Override
    public String toString() {
      return "TranslationArgument";
    }
  }

}
