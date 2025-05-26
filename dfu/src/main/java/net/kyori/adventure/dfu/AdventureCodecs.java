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
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

  //public static final Codec<Component> COMPONENT = new ComponentCodec();
  public static final Codec<BlockNBTComponent.WorldPos.Coordinate> COORDINATE = new CoordinateCodec();
  public static final Codec<BlockNBTComponent.Pos> POS = new PosCodec();
  public static final Codec<ClickEvent> CLICK_EVENT = new ClickEventCodec();
  public static final Codec<HoverEvent<?>> HOVER_EVENT = new HoverEventCodec();
  public static final Codec<Style> STYLE = new StyleCodec();
  public static final Codec<DataComponentValue> DATA_COMPONENT_VALUE = new DataComponentValueCodec();

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
          CLICK_EVENT.optionalFieldOf(ComponentTreeConstants.CLICK_EVENT).forGetter(c -> Optional.ofNullable(c.clickEvent())),
          HOVER_EVENT.optionalFieldOf(ComponentTreeConstants.HOVER_EVENT).forGetter(c -> Optional.ofNullable(c.hoverEvent())),
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
  }

  public static final Codec<Component> COMPONENT = INTERNAL_COMPONENT;

  private static <A, S> Codec<S> xmap(final Codec<A> codec, final Function<? super A, ? extends S> to, final Function<? super S, ? extends A> from, final String name) {
    return Codec.of(codec.comap(from), codec.map(to), name);
  }

  /**
   * Component codec.
   *
   * @since 4.22.0
   */
  public static class ComponentCodec implements Codec<Component> {
    @Override
    public <T> DataResult<T> encode(final Component input, final DynamicOps<T> ops, final T prefix) {
      final RecordBuilder<T> mapBuilder = ops.mapBuilder();
      String type = null;
      if (input instanceof TextComponent) {
        type = TEXT;
        mapBuilder.add(TEXT, Codec.STRING.encode(((TextComponent) input).content(), ops, prefix));
      } else if (input instanceof TranslatableComponent translatableComponent) {
        type = TRANSLATE;
        mapBuilder.add(TRANSLATE, Codec.STRING.encode((translatableComponent).key(), ops, prefix));
        if (!translatableComponent.arguments().isEmpty()) {
          final ListBuilder<T> args = ops.listBuilder();
          for (final TranslationArgument argument : translatableComponent.arguments()) {
            args.add(TRANSLATION_ARGUMENT.encode(argument, ops, prefix));
          }
          mapBuilder.add(TRANSLATE_WITH, args.build(prefix));
        }
        if (translatableComponent.fallback() != null) {
          mapBuilder.add(TRANSLATE_FALLBACK, Codec.STRING.encode(translatableComponent.fallback(), ops, prefix));
        }
      } else if (input instanceof ScoreComponent scoreComponent) {
        type = SCORE;
        mapBuilder.add(SCORE_NAME, Codec.STRING.encode(scoreComponent.name(), ops, prefix));
        mapBuilder.add(SCORE_OBJECTIVE, Codec.STRING.encode(scoreComponent.objective(), ops, prefix));
        if (scoreComponent.value() != null) {
          mapBuilder.add(SCORE_VALUE, Codec.STRING.encode(scoreComponent.value(), ops, prefix));
        }
      } else if (input instanceof SelectorComponent) {
        type = SELECTOR;
        mapBuilder.add(SELECTOR, Codec.STRING.encode(((SelectorComponent) input).pattern(), ops, prefix));
      } else if (input instanceof KeybindComponent) {
        type = KEYBIND;
        mapBuilder.add(KEYBIND, Codec.STRING.encode(((KeybindComponent) input).keybind(), ops, prefix));
      } else if (input instanceof NBTComponent<?, ?> nbtComponent) {
        type = NBT;
        mapBuilder.add(NBT, Codec.STRING.encode(nbtComponent.nbtPath(), ops, prefix));
        if (nbtComponent.interpret()) {
          mapBuilder.add(NBT_INTERPRET, ops.createBoolean(true));
        }
        if (nbtComponent.separator() != null) {
          mapBuilder.add(SEPARATOR, this.encode(nbtComponent.separator(), ops, prefix));
        }
        if (nbtComponent instanceof BlockNBTComponent) {
          mapBuilder.add(NBT_BLOCK, POS.encode(((BlockNBTComponent) nbtComponent).pos(), ops, prefix));
        } else if (nbtComponent instanceof EntityNBTComponent) {
          mapBuilder.add(NBT_ENTITY, ops.createString(((EntityNBTComponent) nbtComponent).selector()));
        } else if (nbtComponent instanceof StorageNBTComponent) {
          mapBuilder.add(NBT_STORAGE, KEY.encode(((StorageNBTComponent) nbtComponent).storage(), ops, prefix));
        } else {
          return DataResult.error(() -> "Unknown NBT component type");
        }
      } else {
        return DataResult.error(() -> "Unknown component type");
      }
      if (!input.children().isEmpty()) {
        final ListBuilder<T> children = ops.listBuilder();
        for (final Component child : input.children()) {
          children.add(this.encode(child, ops, prefix));
        }
        mapBuilder.add(EXTRA, children.build(prefix));
      }
      if (input.hasStyling()) {
        final Style style = input.style();
        if (!style.isEmpty()) {
          mapBuilder.add("style", STYLE.encode(style, ops, prefix));
        }
      }

      mapBuilder.add("type", ops.createString(type));
      return mapBuilder.build(prefix);
    }

    @Override
    public <T> DataResult<Pair<Component, T>> decode(final DynamicOps<T> ops, final T input) {
      return ops.getMap(input).flatMap(map -> {
        final DataResult<String> typeResult = ops.getStringValue(map.get("type"));
        if (typeResult.isError()) {
          return DataResult.error(() -> "Missing component type");
        }
        final String type = typeResult.getOrThrow();

        Component baseComponent;
        switch (type) {
          case TEXT: {
            final DataResult<String> textResult = ops.getStringValue(map.get(TEXT));
            if (textResult.isError()) {
              return DataResult.error(() -> "Missing text content");
            }
            baseComponent = Component.text(textResult.getOrThrow());
            break;
          }
          case TRANSLATE: {
            final DataResult<String> keyResult = ops.getStringValue(map.get(TRANSLATE));
            if (keyResult.isError()) {
              return DataResult.error(() -> "Missing translation key");
            }
            final TranslatableComponent.Builder builder = Component.translatable().key(keyResult.getOrThrow());

            if (map.get(TRANSLATE_WITH) != null) {
              final DataResult<Consumer<Consumer<T>>> argsResult = ops.getList(map.get(TRANSLATE_WITH));
              if (argsResult.isSuccess()) {
                final List<TranslationArgument> arguments = new ArrayList<>();
                argsResult.getOrThrow().accept(arg -> {
                  final DataResult<Pair<TranslationArgument, T>> dataResult = TRANSLATION_ARGUMENT.decode(ops, arg);
                  if (dataResult.isSuccess()) arguments.add(dataResult.getOrThrow().getFirst());
                });
                builder.arguments(arguments);
              }
            }

            if (map.get(TRANSLATE_FALLBACK) != null) {
              final DataResult<String> fallbackResult = ops.getStringValue(map.get(TRANSLATE_FALLBACK));
              fallbackResult.result().ifPresent(builder::fallback);
            }
            baseComponent = builder.build();
            break;
          }
          case SCORE: {
            final DataResult<String> nameResult = ops.getStringValue(map.get(SCORE_NAME));
            final DataResult<String> objectiveResult = ops.getStringValue(map.get(SCORE_OBJECTIVE));
            if (nameResult.isError() || objectiveResult.isError()) {
              return DataResult.error(() -> "Missing score components");
            }
            final ScoreComponent.Builder builder = Component.score().name(nameResult.getOrThrow()).objective(objectiveResult.getOrThrow());

            if (map.get(SCORE_VALUE) != null) {
              final DataResult<String> valueResult = ops.getStringValue(map.get(SCORE_VALUE));
              valueResult.result().ifPresent(builder::value);
            }
            baseComponent = builder.build();
            break;
          }
          case SELECTOR: {
            final DataResult<String> patternResult = ops.getStringValue(map.get(SELECTOR));
            if (patternResult.isError()) {
              return DataResult.error(() -> "Missing selector pattern");
            }
            baseComponent = Component.selector(patternResult.getOrThrow());
            break;
          }
          case KEYBIND: {
            final DataResult<String> keybindResult = ops.getStringValue(map.get(KEYBIND));
            if (keybindResult.isError()) {
              return DataResult.error(() -> "Missing keybind value");
            }
            baseComponent = Component.keybind(keybindResult.getOrThrow());
            break;
          }
          case NBT: {
            final DataResult<String> nbtPathResult = ops.getStringValue(map.get(NBT));
            if (nbtPathResult.isError()) {
              return DataResult.error(() -> "Missing NBT path");
            }
            final String nbtPath = nbtPathResult.getOrThrow();

            if (map.get(NBT_BLOCK) != null) {
              final DataResult<Pair<BlockNBTComponent.Pos, T>> posResult = POS.decode(ops, map.get(NBT_BLOCK));
              if (posResult.isError()) {
                return DataResult.error(() -> "Invalid block NBT position");
              }
              baseComponent = Component.blockNBT().nbtPath(nbtPath).interpret(map.get(NBT_INTERPRET) != null).pos(posResult.getOrThrow().getFirst()).build();
            } else if (map.get(NBT_ENTITY) != null) {
              final DataResult<String> selectorResult = ops.getStringValue(map.get(NBT_ENTITY));
              if (selectorResult.isError()) {
                return DataResult.error(() -> "Invalid entity NBT selector");
              }
              baseComponent = Component.entityNBT().nbtPath(nbtPath).interpret(map.get(NBT_INTERPRET) != null).selector(selectorResult.getOrThrow()).build();
            } else if (map.get(NBT_STORAGE) != null) {
              final DataResult<Pair<Key, T>> storageResult = KEY.decode(ops, map.get(NBT_STORAGE));
              if (storageResult.isError()) {
                return DataResult.error(() -> "Invalid storage NBT key");
              }
              baseComponent = Component.storageNBT().nbtPath(nbtPath).interpret(map.get(NBT_INTERPRET) != null).storage(storageResult.getOrThrow().getFirst()).build();
            } else {
              return DataResult.error(() -> "Unknown NBT component type");
            }

            if (map.get(SEPARATOR) != null) {
              final DataResult<Component> separatorResult = COMPONENT.decode(ops, map.get(SEPARATOR)).map(Pair::getFirst);
              if (separatorResult.isError()) {
                return DataResult.error(() -> "Invalid separator");
              }
              ((NBTComponent<?, ?>) baseComponent).separator(separatorResult.getOrThrow());
            }
            break;
          }
          default:
            return DataResult.error(() -> "Unknown component type: " + type);
        }

        if (map.get(EXTRA) != null) {
          final DataResult<Consumer<Consumer<T>>> childrenResult = ops.getList(map.get(EXTRA));

          if (childrenResult.isError()) {
            return DataResult.error(() -> "Invalid extra components");
          }
          final List<Component> childrens = new ArrayList<>();
          childrenResult.result().ifPresent(children -> children.accept(t -> {
            final DataResult<Component> result = this.decode(ops, t).map(Pair::getFirst);
            if (result.isError()) return;
            childrens.add(result.getOrThrow());
          }));
          baseComponent = baseComponent.children(childrens);
        }

        if (map.get("style") != null) {
          final DataResult<Style> styleResult = STYLE.decode(ops, map.get("style")).map(Pair::getFirst);
          if (styleResult.isError()) return DataResult.error(() -> "Invalid style");
          baseComponent = baseComponent.style(styleResult.getOrThrow());
        }

        return DataResult.success(Pair.of(baseComponent, ops.empty()));
      });
    }

    @Override
    public String toString() {
      return "Component";
    }
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
   * Hover event codec.
   *
   * @since 4.22.0
   */
  public static class HoverEventCodec implements Codec<HoverEvent<?>> {

    @Override
    public <T> DataResult<T> encode(final HoverEvent<?> input, final DynamicOps<T> ops, final T prefix) {
      final RecordBuilder<T> map = ops.mapBuilder();

      map.add(HOVER_EVENT_ACTION, HOVER_ACTION.encode(input.action(), ops, prefix));

      final Object value = input.value();
      if (value instanceof Component) {
        map.add(HOVER_EVENT_CONTENTS, COMPONENT.encode((Component) value, ops, prefix));
      } else if (value instanceof HoverEvent.ShowEntity entity) {
        final RecordBuilder<T> entityMap = ops.mapBuilder().add(SHOW_ENTITY_TYPE, KEY.encode(entity.type(), ops, prefix)).add(SHOW_ENTITY_ID, UUID.encode(entity.id(), ops, prefix));
        if (entity.name() != null) {
          entityMap.add(SHOW_ENTITY_NAME, COMPONENT.encode(entity.name(), ops, prefix));
        }
        map.add(HOVER_EVENT_CONTENTS, entityMap.build(prefix));
      } else if (value instanceof HoverEvent.ShowItem item) {
        final RecordBuilder<T> itemMap = ops.mapBuilder().add(SHOW_ITEM_ID, KEY.encode(item.item(), ops, prefix)).add(SHOW_ITEM_COUNT, ops.createInt(item.count()));
        if (item.nbt() != null) {
          itemMap.add(SHOW_ITEM_TAG, ops.createString(item.nbt().string()));
        }
        if (!item.dataComponents().isEmpty()) {
          final RecordBuilder<T> dataComponentMap = ops.mapBuilder();
          item.dataComponents().forEach((key, dataComponentValue) -> dataComponentMap.add(KEY.encode(key, ops, prefix), DATA_COMPONENT_VALUE.encode(dataComponentValue, ops, prefix)));
          itemMap.add(SHOW_ITEM_COMPONENTS, dataComponentMap.build(prefix));
        }
        map.add(HOVER_EVENT_CONTENTS, itemMap.build(prefix));
      }

      return map.build(prefix);
    }

    @Override
    public <T> DataResult<Pair<HoverEvent<?>, T>> decode(final DynamicOps<T> ops, final T input) {
      return ops.getMap(input).flatMap(map -> {
        final DataResult<Pair<HoverEvent.Action<?>, T>> actionResult = HOVER_ACTION.decode(ops, map.get(HOVER_EVENT_ACTION));
        if (actionResult.isError()) {
          return DataResult.error(() -> "Missing hover action type");
        }

        final HoverEvent.Action<?> action = actionResult.getOrThrow().getFirst();

        final T contents = map.get(HOVER_EVENT_CONTENTS);
        if (contents == null) {
          return DataResult.error(() -> "Missing hover contents");
        }

        if (action == HoverEvent.Action.SHOW_TEXT) {
          return COMPONENT.decode(ops, contents).map(pair -> Pair.of(HoverEvent.showText(pair.getFirst()), pair.getSecond()));
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
          return ops.getMap(contents).flatMap(entityMap -> {
            final DataResult<Key> typeResult = KEY.decode(ops, entityMap.get(SHOW_ENTITY_TYPE)).map(Pair::getFirst);
            final DataResult<UUID> idResult = UUID.decode(ops, entityMap.get(SHOW_ENTITY_ID)).map(Pair::getFirst);
            final DataResult<Component> nameResult = entityMap.get(SHOW_ENTITY_NAME) != null ? COMPONENT.decode(ops, entityMap.get(SHOW_ENTITY_NAME)).map(Pair::getFirst) : DataResult.success(null);

            return typeResult.apply2((type, id) -> Pair.of(HoverEvent.showEntity(type, id, nameResult.result().orElse(null)), ops.empty()), idResult);
          });
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
          return ops.getMap(contents).flatMap(itemMap -> {
            final DataResult<Key> idResult = KEY.decode(ops, itemMap.get(SHOW_ITEM_ID)).map(Pair::getFirst);
            final DataResult<Integer> countResult = ops.getNumberValue(itemMap.get(SHOW_ITEM_COUNT)).map(Number::intValue);
            final String nbt = ops.getStringValue(itemMap.get(SHOW_ITEM_TAG)).result().orElse(null);
            final DataResult<Consumer<BiConsumer<T, T>>> dataComponentMap = ops.getMapEntries(itemMap.get(SHOW_ITEM_COMPONENTS));
            if (dataComponentMap.isSuccess()) {
              return idResult.apply3((id, count, dataComponentConsumer) -> {
                final Map<Key, DataComponentValue> dataComponents = new HashMap<>();
                dataComponentConsumer.accept((key, value) -> {
                  final DataResult<Pair<Key, T>> keyResult = KEY.decode(ops, key);
                  final DataResult<Pair<DataComponentValue, T>> valueResult = DATA_COMPONENT_VALUE.decode(ops, value);
                  if (keyResult.isError() || valueResult.isError()) {
                    return;
                  }
                  dataComponents.put(keyResult.getOrThrow().getFirst(), valueResult.getOrThrow().getFirst());
                });
                return Pair.of(HoverEvent.showItem(id, count, dataComponents), ops.empty());
              }, countResult, dataComponentMap);
            }
            return idResult.apply2((id, count) -> {

              if (nbt != null) {
                return Pair.of(HoverEvent.showItem(id, count, BinaryTagHolder.binaryTagHolder(nbt)), ops.empty());
              }
              return Pair.of(HoverEvent.showItem(id, count), ops.empty());
            }, countResult);
          });
        }
        return DataResult.error(() -> "Unhandled hover action type: " + action);
      });
    }

    @Override
    public String toString() {
      return "HoverEvent";
    }
  }

  /**
   * Click event codec.
   *
   * @since 4.22.0
   */
  public static class ClickEventCodec implements Codec<ClickEvent> {

    @Override
    public <T> DataResult<T> encode(final ClickEvent input, final DynamicOps<T> ops, final T prefix) {
      if (!input.action().readable()) {
        return DataResult.error(() -> "Unreadable click action: " + input.action());
      }

      final RecordBuilder<T> map = ops.mapBuilder();
      map.add(CLICK_EVENT_ACTION, CLICK_ACTION.encode(input.action(), ops, prefix));
      map.add(CLICK_EVENT_VALUE, ops.createString(input.value()));
      return map.build(prefix);
    }

    @Override
    public <T> DataResult<Pair<ClickEvent, T>> decode(final DynamicOps<T> ops, final T input) {
      return ops.getMap(input).flatMap(map -> {
        final DataResult<Pair<ClickEvent.Action, T>> actionResult = CLICK_ACTION.decode(ops, map.get(CLICK_EVENT_ACTION));
        if (actionResult.isError()) {
          return DataResult.error(() -> "Missing click action type");
        }

        final ClickEvent.Action action = actionResult.getOrThrow().getFirst();

        if (!action.readable()) {
          return DataResult.error(() -> "Unreadable click action: " + action);
        }

        final DataResult<String> valueResult = ops.getStringValue(map.get(CLICK_EVENT_VALUE));
        if (valueResult.isError()) {
          return DataResult.error(() -> "Missing click value");
        }

        return DataResult.success(Pair.of(ClickEvent.clickEvent(action, valueResult.getOrThrow()), ops.empty()));
      });
    }

    @Override
    public String toString() {
      return "ClickEvent";
    }
  }

  /**
   * DataComponentValue codec.
   *
   * @since 4.22.0
   */
  public static class DataComponentValueCodec implements Codec<DataComponentValue> {

    @Override
    public <T> DataResult<T> encode(final DataComponentValue input, final DynamicOps<T> ops, final T prefix) {
      if (input instanceof DataComponentValue.TagSerializable) {
        final String nbt = ((DataComponentValue.TagSerializable) input).asBinaryTag().string();
        return ops.mapBuilder().add("type", ops.createString("tag")).add("value", ops.createString(nbt)).build(prefix);
      } else if (input instanceof DataComponentValue.Removed) {
        return ops.mapBuilder().add("type", ops.createString("remove")).add("value", ops.empty()).build(prefix);
      }
      return DataResult.error(() -> "Unhandled data component value type: " + input);
    }

    @Override
    public <T> DataResult<Pair<DataComponentValue, T>> decode(final DynamicOps<T> ops, final T input) {
      return ops.getMap(input).flatMap(map -> {
        final DataResult<String> typeResult = ops.getStringValue(map.get("type"));
        if (typeResult.isError()) {
          return DataResult.error(() -> "Missing data component value type: " + typeResult.error().get().message());
        }
        final String type = typeResult.result().get();
        return switch (type) {
          case "tag" -> ops.getStringValue(map.get("value")).flatMap(nbt -> DataResult.success(Pair.of(BinaryTagHolder.binaryTagHolder(nbt), ops.empty())));
          case "remove" -> DataResult.success(Pair.of(DataComponentValue.removed(), ops.empty()));
          default -> DataResult.error(() -> "Unhandled data component value type: " + type);
        };
      });
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
