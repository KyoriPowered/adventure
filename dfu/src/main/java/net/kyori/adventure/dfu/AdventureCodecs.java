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
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
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

import static net.kyori.adventure.text.event.HoverEvent.Action.SHOW_TEXT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.COLOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.EXTRA;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.FONT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_CONTENTS;
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
  public static final Codec<NamedTextColor> NAMED_TEXT_COLOR = new IndexCodec<>(Codec.STRING, NamedTextColor.NAMES, "NamedTextColor");
  public static final Codec<BossBar.Color> BOSS_BAR_COLOR = new IndexCodec<>(Codec.STRING, BossBar.Color.NAMES, "BossBar.Color");
  public static final Codec<BossBar.Flag> BOSS_BAR_FLAG = new IndexCodec<>(Codec.STRING, BossBar.Flag.NAMES, "BossBar.Flag");
  public static final Codec<BossBar.Overlay> BOSS_BAR_OVERLAY = new IndexCodec<>(Codec.STRING, BossBar.Overlay.NAMES, "BossBar.Overlay");
  public static final Codec<Sound.Source> SOUND_SOURCE = new IndexCodec<>(Codec.STRING, Sound.Source.NAMES, "Sound.Source");
  public static final Codec<ClickEvent.Action> CLICK_ACTION = new IndexCodec<>(Codec.STRING, ClickEvent.Action.NAMES, "ClickEvent.Action");
  public static final Codec<HoverEvent.Action<?>> HOVER_ACTION = new IndexCodec<>(Codec.STRING, HoverEvent.Action.NAMES, "HoverEvent.Action");
  // TextDecoration
  public static final Codec<TextDecoration> TEXT_DECORATION = new IndexCodec<>(Codec.STRING, TextDecoration.NAMES, "TextDecoration");
  public static final Codec<TextDecoration.State> TEXT_DECORATION_STATE = new IndexCodec<>(Codec.STRING, Index.create(TextDecoration.State.class, TextDecoration.State::toString), "TextDecoration.State");

  public static final Codec<BinaryTagHolder> BINARY_TAG_HOLDER = xmap(Codec.STRING, BinaryTagHolder::binaryTagHolder, BinaryTagHolder::string, "BinaryTagHolder");
  public static final Codec<BlockNBTComponent.WorldPos.Coordinate> COORDINATE = new CoordinateCodec();
  public static final Codec<BlockNBTComponent.Pos> POS = new PosCodec();
  public static final Codec<ClickEvent> CLICK_EVENT = RecordCodecBuilder.create(instance -> instance.group(
    CLICK_ACTION.fieldOf("action").forGetter(c -> c.action()),
    Codec.STRING.fieldOf("value").forGetter(c -> c.value())
  ).apply(instance, (action, value) -> ClickEvent.clickEvent(action, value)));

  public static final Codec<Style> STYLE = new StyleCodec();
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
    KEY.fieldOf(SHOW_ITEM_ID).forGetter(c -> c.item()),
    Codec.INT.fieldOf(SHOW_ITEM_COUNT).forGetter(c -> c.count()),
    BINARY_TAG_HOLDER.optionalFieldOf(SHOW_ITEM_TAG).forGetter(c -> Optional.ofNullable(c.nbt())),
    Codec.unboundedMap(KEY, DATA_COMPONENT_VALUE).fieldOf(SHOW_ITEM_COMPONENTS).forGetter(c -> c.dataComponents())
  ).apply(instance, (id, count, nbt, dataComponentValueMap) -> {
    if (nbt.isPresent()) {
      return HoverEvent.ShowItem.showItem(id, count, nbt.get());
    }
    return HoverEvent.ShowItem.showItem(id, count, dataComponentValueMap);
  }));
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
    INTERNAL_COMPONENT = RecordCodecBuilder.create(new Function<RecordCodecBuilder.Instance<Component>, App<RecordCodecBuilder.Mu<Component>, Component>>() {
      @Override
      public App<RecordCodecBuilder.Mu<Component>, Component> apply(final RecordCodecBuilder.Instance<Component> instance) {
        return instance.group(
          STYLE.optionalFieldOf("style").forGetter(c -> Optional.of(c.style())),
          KEY.optionalFieldOf(FONT).forGetter(c -> Optional.ofNullable(c.font())),
          Codec.lazyInitialized(() -> INTERNAL_COMPONENT).listOf().optionalFieldOf(EXTRA, Collections.emptyList()).forGetter(Component::children),
          Codec.lazyInitialized(() -> CLICK_EVENT).optionalFieldOf(ComponentTreeConstants.CLICK_EVENT).forGetter(c -> Optional.ofNullable(c.clickEvent())),
          Codec.lazyInitialized(() -> HOVER_EVENT).optionalFieldOf(ComponentTreeConstants.HOVER_EVENT).forGetter(c -> Optional.ofNullable(c.hoverEvent())),
          Codec.STRING.optionalFieldOf(INSERTION).forGetter(c -> Optional.ofNullable(c.insertion())),
          Codec.STRING.optionalFieldOf(TEXT).forGetter(c -> Optional.ofNullable(c instanceof TextComponent ? ((TextComponent) c).content() : null)),
          Codec.lazyInitialized(() -> BLOCK_NBT_CODEC).optionalFieldOf(NBT_BLOCK).forGetter(c -> Optional.ofNullable(c instanceof BlockNBTComponent ? ((BlockNBTComponent) c) : null)),
          Codec.lazyInitialized(() -> ENTITY_NBT_CODEC).optionalFieldOf(NBT_ENTITY).forGetter(c -> Optional.ofNullable(c instanceof EntityNBTComponent ? ((EntityNBTComponent) c) : null)),
          Codec.lazyInitialized(() -> STORAGE_NBT_CODEC).optionalFieldOf(NBT_STORAGE).forGetter(c -> Optional.ofNullable(c instanceof StorageNBTComponent ? ((StorageNBTComponent) c) : null)),
          Codec.lazyInitialized(() -> SCORE_CODEC).optionalFieldOf(SCORE).forGetter(c -> Optional.ofNullable(c instanceof ScoreComponent ? ((ScoreComponent) c) : null)),
          Codec.lazyInitialized(() -> SELECTOR_CODEC).optionalFieldOf(SELECTOR).forGetter(c -> Optional.ofNullable(c instanceof SelectorComponent ? ((SelectorComponent) c) : null)),
          Codec.lazyInitialized(() -> KEYBIND_CODEC).optionalFieldOf(KEYBIND).forGetter(c -> Optional.ofNullable(c instanceof KeybindComponent ? ((KeybindComponent) c) : null)),
          Codec.lazyInitialized(() -> TRANSLATABLE_CODEC).optionalFieldOf(TRANSLATE).forGetter(c -> Optional.ofNullable(c instanceof TranslatableComponent ? ((TranslatableComponent) c) : null))
        ).apply(instance, (style, font, children, clickEvent, hoverEvent, insertion, textContent, blockNBTComponent, entityNBTComponent, storageNBTComponent, scoreComponent, selectorComponent, keybindComponent, translatableComponent) -> {
          final Consumer<ComponentBuilder<?, ?>> consumer = builder -> {
            style.ifPresent(builder::style);
            font.ifPresent(builder::font);
            builder.append(children);
            clickEvent.ifPresent(builder::clickEvent);
            hoverEvent.ifPresent(builder::hoverEvent);
            insertion.ifPresent(builder::insertion);
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
            throw new IllegalArgumentException("Component is not a NBTComponent");
          }
          final ComponentBuilder<?, ?> builder = baseComponent.toBuilder();
          consumer.accept(builder);
          return builder.build();
        });
      }
    });
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

    SHOW_ENTITY = RecordCodecBuilder.create(i -> i.group(
        KEY.fieldOf(SHOW_ENTITY_TYPE).forGetter(HoverEvent.ShowEntity::type),
        UUID.fieldOf(SHOW_ENTITY_ID).forGetter(HoverEvent.ShowEntity::id),
        INTERNAL_COMPONENT.optionalFieldOf(SHOW_ENTITY_NAME).forGetter(c -> Optional.ofNullable(c.name()))
      ).apply(i, (key, uuid, name) -> name.map(component -> HoverEvent.ShowEntity.showEntity(key, uuid, component)).orElseGet(() -> HoverEvent.ShowEntity.showEntity(key, uuid)))
    );
    HOVER_EVENT = RecordCodecBuilder.create(instance -> instance.group(
      HOVER_ACTION.fieldOf(HOVER_EVENT_ACTION).forGetter(c -> c.action()),
      SHOW_ITEM.optionalFieldOf("showItem").forGetter(c -> c.value() instanceof HoverEvent.ShowItem ? Optional.of(((HoverEvent.ShowItem) c.value())) : Optional.empty()),
      INTERNAL_COMPONENT.optionalFieldOf("showText").forGetter(c -> c.value() instanceof Component ? Optional.of((Component) c.value()) : Optional.empty()),
      SHOW_ENTITY.optionalFieldOf("showEntity").forGetter(c -> c.value() instanceof HoverEvent.ShowEntity ? Optional.of(((HoverEvent.ShowEntity) c.value())) : Optional.empty()),
      Codec.STRING.optionalFieldOf("showAchievement").forGetter(c -> c.value() instanceof String ? Optional.of((String) c.value()) : Optional.empty())
    ).apply(instance, new Function5<>() {
      @Override
      public HoverEvent<?> apply(final HoverEvent.Action<?> action, final Optional<HoverEvent.ShowItem> showItem, final Optional<Component> component, final Optional<HoverEvent.ShowEntity> showEntity, final Optional<String> achievement) {
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
      }
    }));
  }

  public static final Codec<Component> COMPONENT = INTERNAL_COMPONENT;

  private static <A, S> Codec<S> xmap(final Codec<A> codec, final Function<? super A, ? extends S> to, final Function<? super S, ? extends A> from, final String name) {
    return Codec.of(codec.comap(from), codec.map(to), name);
  }

  /**
   * Style codec.
   *
   * @since 4.22.0
   */
  public static class StyleCodec implements Codec<Style> {

    @Override
    public <T> DataResult<T> encode(final Style input, final DynamicOps<T> ops, final T prefix) {
      if (input.isEmpty()) {
        return DataResult.success(ops.empty());
      }
      final RecordBuilder<T> mapBuilder = ops.mapBuilder();
      if (input.font() != null) {
        mapBuilder.add(FONT, KEY.encode(input.font(), ops, prefix));
      }
      if (input.color() != null) {
        mapBuilder.add(COLOR, TEXT_COLOR.encode(input.color(), ops, prefix));
      }
      if (input.insertion() != null) {
        mapBuilder.add(INSERTION, Codec.STRING.encode(input.insertion(), ops, prefix));
      }
      if (input.shadowColor() != null) {
        mapBuilder.add(ComponentTreeConstants.SHADOW_COLOR, SHADOW_COLOR.encode(input.shadowColor(), ops, prefix));
      }
      if (input.clickEvent() != null) {
        mapBuilder.add(ComponentTreeConstants.CLICK_EVENT, CLICK_EVENT.encode(input.clickEvent(), ops, prefix));
      }
      if (input.hoverEvent() != null) {
        mapBuilder.add(ComponentTreeConstants.HOVER_EVENT, HOVER_EVENT.encode(input.hoverEvent(), ops, prefix));
      }
      final RecordBuilder<T> decorationMap = ops.mapBuilder();
      for (final TextDecoration decoration : TextDecoration.values()) {
        if (input.hasDecoration(decoration)) {
          decorationMap.add(TEXT_DECORATION.encode(decoration, ops, prefix), TEXT_DECORATION_STATE.encode(input.decorations().get(decoration), ops, prefix));
        }
      }
      mapBuilder.add("decoration", decorationMap.build(prefix));
      return mapBuilder.build(prefix);
    }

    @Override
    public <T> DataResult<Pair<Style, T>> decode(final DynamicOps<T> ops, final T input) {
      final MapLike<T> map = ops.getMap(input).getOrThrow();

      final DataResult<Key> fontResult = KEY.decode(ops, map.get(FONT)).map(Pair::getFirst);
      final Key font = fontResult.result().orElse(null);

      final DataResult<TextColor> colorResult = TEXT_COLOR.decode(ops, map.get(COLOR)).map(Pair::getFirst);
      final DataResult<ShadowColor> shadowColorResult = SHADOW_COLOR.decode(ops, map.get(ComponentTreeConstants.SHADOW_COLOR)).map(Pair::getFirst);
      final TextColor color = colorResult.result().orElse(null);
      final ShadowColor shadowColor = shadowColorResult.result().orElse(null);
      final Style.Builder builder = Style.style();
      builder.color(color).shadowColor(shadowColor).font(font);

      final DataResult<Consumer<BiConsumer<T, T>>> decorationMap = ops.getMapEntries(map.get("decoration"));
      if (decorationMap.isError()) {
        return DataResult.error(() -> "Invalid decoration map");
      }
      decorationMap.result().ifPresent(bi -> bi.accept((k, v) -> {
        final DataResult<TextDecoration> key = TEXT_DECORATION.decode(ops, k).map(Pair::getFirst);
        final DataResult<TextDecoration.State> value = TEXT_DECORATION_STATE.decode(ops, v).map(Pair::getFirst);
        if (key.isError() || value.isError()) {
          return;
        }
        builder.decoration(key.getOrThrow(), value.getOrThrow());
      }));

      if (map.get(INSERTION) != null) {
        final DataResult<String> insertionResult = Codec.STRING.decode(ops, map.get(INSERTION)).map(Pair::getFirst);
        builder.insertion(insertionResult.result().orElse(null));
      }
      if (map.get(ComponentTreeConstants.CLICK_EVENT) != null) {
        final DataResult<ClickEvent> clickEventResult = CLICK_EVENT.decode(ops, map.get(ComponentTreeConstants.CLICK_EVENT)).map(Pair::getFirst);
        builder.clickEvent(clickEventResult.result().orElse(null));
      }
      if (map.get(ComponentTreeConstants.HOVER_EVENT) != null) {
        final DataResult<HoverEvent<?>> hoverEventResult = HOVER_EVENT.decode(ops, map.get(ComponentTreeConstants.HOVER_EVENT)).map(Pair::getFirst);
        builder.hoverEvent(hoverEventResult.result().orElse(null));
      }

      final Style style = builder.build();
      return DataResult.success(Pair.of(style, ops.empty()));
    }

    @Override
    public String toString() {
      return "Style";
    }
  }

  /**
   * Pos codec.
   *
   * @since 4.22.0
   */
  public static class PosCodec implements Codec<BlockNBTComponent.Pos> {

    @Override
    public <T> DataResult<T> encode(final BlockNBTComponent.Pos input, final DynamicOps<T> ops, final T prefix) {

      if (input instanceof BlockNBTComponent.LocalPos) {
        return ops.mapBuilder().add("left", Codec.DOUBLE.encode(((BlockNBTComponent.LocalPos) input).left(), ops, prefix)).add("up", Codec.DOUBLE.encode(((BlockNBTComponent.LocalPos) input).up(), ops, prefix)).add("forward", Codec.DOUBLE.encode(((BlockNBTComponent.LocalPos) input).forwards(), ops, prefix)).add("type", ops.createString("local")).build(prefix);
      } else if (input instanceof BlockNBTComponent.WorldPos) {
        return ops.mapBuilder().add("x", COORDINATE.encode(((BlockNBTComponent.WorldPos) input).x(), ops, prefix)).add("y", COORDINATE.encode(((BlockNBTComponent.WorldPos) input).y(), ops, prefix)).add("z", COORDINATE.encode(((BlockNBTComponent.WorldPos) input).z(), ops, prefix)).add("type", ops.createString("world")).build(prefix);
      }
      return DataResult.error(() -> "Unknown pos type");
    }

    @Override
    public <T> DataResult<Pair<BlockNBTComponent.Pos, T>> decode(final DynamicOps<T> ops, final T input) {
      final DataResult<MapLike<T>> mapResult = ops.getMap(input);
      if (mapResult.isError()) {
        return DataResult.error(() -> "Not a map");
      }
      final MapLike<T> map = mapResult.getOrThrow();
      final DataResult<String> typeResult = ops.getStringValue(map.get("type"));
      if (typeResult.isError()) {
        return DataResult.error(() -> "No type");
      }
      switch (typeResult.getOrThrow()) {
        case "local":
          return ops.getNumberValue(map.get("left")).apply3((left, up, forwards) -> Pair.of(BlockNBTComponent.LocalPos.localPos(left.doubleValue(), up.doubleValue(), forwards.doubleValue()), ops.empty()), ops.getNumberValue(map.get("up")), ops.getNumberValue(map.get("forward")));
        case "world": {
          final DataResult<Pair<BlockNBTComponent.WorldPos.Coordinate, T>> x = COORDINATE.decode(ops, map.get("x"));
          final DataResult<Pair<BlockNBTComponent.WorldPos.Coordinate, T>> y = COORDINATE.decode(ops, map.get("y"));
          final DataResult<Pair<BlockNBTComponent.WorldPos.Coordinate, T>> z = COORDINATE.decode(ops, map.get("z"));
          return x.apply3((x1, y1, z1) -> Pair.of(BlockNBTComponent.WorldPos.worldPos(x1.getFirst(), y1.getFirst(), z1.getFirst()), ops.empty()), y, z);
        }
      }
      return DataResult.error(() -> "Unknown pos type");
    }

    @Override
    public String toString() {
      return "BlockNBTComponent.Pos";
    }
  }

  /**
   * Coordinate codec.
   *
   * @since 4.22.0
   */
  public static class CoordinateCodec implements Codec<BlockNBTComponent.WorldPos.Coordinate> {

    @Override
    public <T> DataResult<T> encode(final BlockNBTComponent.WorldPos.Coordinate input, final DynamicOps<T> ops, final T prefix) {
      return ops.mapBuilder().add("type", ops.createString(input.type().name())).add("value", ops.createInt(input.value())).build(prefix);
    }

    @Override
    public <T> DataResult<Pair<BlockNBTComponent.WorldPos.Coordinate, T>> decode(final DynamicOps<T> ops, final T input) {
      final DataResult<MapLike<T>> mapResult = ops.getMap(input);
      if (mapResult.isError()) {
        return DataResult.error(() -> "Not a map");
      }
      final MapLike<T> map = mapResult.getOrThrow();
      final DataResult<String> typeResult = ops.getStringValue(map.get("type"));
      return typeResult.apply2((type, value) -> {
        final BlockNBTComponent.WorldPos.Coordinate coordinate = BlockNBTComponent.WorldPos.Coordinate.coordinate(value.intValue(), BlockNBTComponent.WorldPos.Coordinate.Type.valueOf(type));
        return Pair.of(coordinate, ops.empty());
      }, ops.getNumberValue(map.get("value")));
    }

    @Override
    public String toString() {
      return "Coordinate";
    }
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

  /**
   * Index codec.
   *
   * @param <K> key type.
   * @param <V> value type.
   * @since 4.22.0
   */
  public static class IndexCodec<K, V> implements Codec<V> {
    private final Codec<K> keyCodec;
    private final Index<K, V> index;
    private final String name;

    /**
     * Construct a new index codec.
     *
     * @param keyCodec The key codec.
     * @param index    The index.
     * @param name     Current codec name.
     * @since 4.22.0
     */
    public IndexCodec(final Codec<K> keyCodec, final Index<K, V> index, final String name) {
      this.keyCodec = keyCodec;
      this.index = index;
      this.name = name;
    }

    @Override
    public <T> DataResult<T> encode(final V input, final DynamicOps<T> ops, final T prefix) {
      final K key = this.index.key(input);
      if (key == null) {
        return DataResult.error(() -> "Unknown value: " + input);
      }
      return this.keyCodec.encode(key, ops, prefix);
    }

    @Override
    public <T> DataResult<Pair<V, T>> decode(final DynamicOps<T> ops, final T input) {
      final DataResult<Pair<K, T>> result = this.keyCodec.decode(ops, input);
      return result.flatMap(pair -> {
        final V value = this.index.value(pair.getFirst());
        if (value == null) {
          return DataResult.error(() -> "Unknown value: " + pair.getFirst());
        }
        return DataResult.success(Pair.of(value, pair.getSecond()));
      });
    }

    @Override
    public String toString() {
      return "Index[" + this.name + "]";
    }
  }
}
