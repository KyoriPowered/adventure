package net.kyori.adventure.dfu;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.util.Index;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public final class AdventureCodecs {
  private AdventureCodecs() {
  }

  // key
  public static final Codec<Key> KEY = xmap(Codec.STRING, Key::key, Key::asString, "Key");
  // uuid
  public static final Codec<UUID> UUID = xmap(
    Codec.list(Codec.LONG),
    longs -> new UUID(longs.get(0), longs.get(1)),
    uuid -> Arrays.asList(uuid.getLeastSignificantBits(), uuid.getMostSignificantBits()),
    "UUID"
  );
  // TextColor
  public static final Codec<TextColor> TEXT_COLOR = xmap(Codec.INT, TextColor::color, TextColor::value, "TextColor");
  // TranslationArgument
  public static final Codec<TranslationArgument> TRANSLATION_ARGUMENT = new TranslationArgumentCodec();
  // indexed
  public static final Codec<NamedTextColor> NAMED_TEXT_COLOR = new IndexCodec<>(Codec.STRING, NamedTextColor.NAMES, "NamedTextColor");
  public static final Codec<BossBar.Color> BOSS_BAR_COLOR = new IndexCodec<>(Codec.STRING, BossBar.Color.NAMES, "BossBar.Color");
  public static final Codec<BossBar.Flag> BOSS_BAR_FLAG = new IndexCodec<>(Codec.STRING, BossBar.Flag.NAMES, "BossBar.Flag");
  public static final Codec<BossBar.Overlay> BOSS_BAR_OVERLAY = new IndexCodec<>(Codec.STRING, BossBar.Overlay.NAMES, "BossBar.Overlay");
  public static final Codec<Sound.Source> SOUND_SOURCE = new IndexCodec<>(Codec.STRING, Sound.Source.NAMES, "Sound.Source");
  public static final Codec<ClickEvent.Action> CLICK_ACTION = new IndexCodec<>(Codec.STRING, ClickEvent.Action.NAMES, "ClickEvent.Action");
  // TextDecoration
  public static final Codec<TextDecoration> TEXT_DECORATION = new IndexCodec<>(Codec.STRING, TextDecoration.NAMES, "TextDecoration");

  public static final Codec<Component> COMPONENT = new ComponentCodec(GsonComponentSerializer.gson(), JsonOps.INSTANCE);

  private static <A, S> Codec<S> xmap(Codec<A> codec, final Function<? super A, ? extends S> to, final Function<? super S, ? extends A> from, String name) {
    return Codec.of(codec.comap(from), codec.map(to), name);
  }

  public static class ComponentCodec implements Codec<Component> {
    private final GsonComponentSerializer serializer;
    private final JsonOps jsonOps;

    public ComponentCodec(GsonComponentSerializer serializer, JsonOps jsonOps) {
      this.serializer = serializer;
      this.jsonOps = jsonOps;
    }

    @Override
    public <T> DataResult<T> encode(Component input, DynamicOps<T> ops, T prefix) {
      JsonElement jsonElement = serializer.serializeToTree(input);
      return DataResult.success(jsonOps.convertTo(ops, jsonElement));
    }

    @Override
    public <T> DataResult<Pair<Component, T>> decode(DynamicOps<T> ops, T input) {
      JsonElement jsonElement = ops.convertTo(jsonOps, input);
      Component component = serializer.deserializeFromTree(jsonElement);
      return DataResult.success(Pair.of(component, ops.empty()));
    }
  }

  public static class TranslationArgumentCodec implements Codec<TranslationArgument> {
    @Override
    public <T> DataResult<T> encode(TranslationArgument input, DynamicOps<T> ops, T prefix) {
      Object object = input.value();
      T type = null, value = null;
      if (object instanceof Boolean) {
        type = ops.createString("bool");
        value = ops.createBoolean((Boolean) object);
      } else if (object instanceof Number) {
        type = ops.createString("number");
        value = ops.createNumeric((Number) object);
      } else if (object instanceof Component) {
        type = ops.createString("component");
        DataResult<T> result = COMPONENT.encode((Component) object, ops, prefix);
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
    public <T> DataResult<Pair<TranslationArgument, T>> decode(DynamicOps<T> ops, T input) {

      return ops.getMap(input).map(map -> ops.getStringValue(map.get("type")).map(type -> {
        final T value = map.get("value");
        if (Objects.equals(type, "component")) {
          return COMPONENT.decode(ops, value).map(t -> Pair.of(TranslationArgument.component(t.getFirst()), t.getSecond())).getOrThrow();
        }
        return Pair.of(switch (type) {
          case "bool" -> ops.getBooleanValue(value).map(TranslationArgument::bool).getOrThrow();
          case "number" -> ops.getNumberValue(value).map(TranslationArgument::numeric).getOrThrow();
          default -> throw new IllegalArgumentException("Unknown value: " + type);
        }, value);
      }).getOrThrow());
    }
  }

  public static class IndexCodec<K, V> implements Codec<V> {
    private final Codec<K> keyCodec;
    private final Index<K, V> index;
    private final String name;

    public IndexCodec(Codec<K> keyCodec, Index<K, V> index, String name) {
      this.keyCodec = keyCodec;
      this.index = index;
      this.name = name;
    }

    @Override
    public <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
      K key = this.index.key(input);
      if (key == null) {
        return DataResult.error(() -> "Unknown value: " + input);
      }
      return keyCodec.encode(key, ops, prefix);
    }

    @Override
    public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
      DataResult<Pair<K, T>> result = keyCodec.decode(ops, input);
      return result.map(pair -> {
        V value = this.index.value(pair.getFirst());
        if (value == null) {
          throw new IllegalArgumentException("Unknown value: " + pair.getFirst());
        }
        return Pair.of(value, pair.getSecond());
      });
    }

    @Override
    public String toString() {
      return "Index[" + name + "]";
    }
  }
}
