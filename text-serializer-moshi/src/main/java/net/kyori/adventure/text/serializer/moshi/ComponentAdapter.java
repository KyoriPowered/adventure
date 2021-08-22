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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.Nullable;

final class ComponentAdapter extends JsonAdapter<Component> {
  static final String TEXT = "text";
  static final String TRANSLATE = "translate";
  static final String TRANSLATE_WITH = "with";
  static final String SCORE = "score";
  static final String SCORE_NAME = "name";
  static final String SCORE_OBJECTIVE = "objective";
  static final String SCORE_VALUE = "value";
  static final String SELECTOR = "selector";
  static final String KEYBIND = "keybind";
  static final String EXTRA = "extra";
  static final String NBT = "nbt";
  static final String NBT_INTERPRET = "interpret";
  static final String NBT_BLOCK = "block";
  static final String NBT_ENTITY = "entity";
  static final String NBT_STORAGE = "storage";
  static final String SEPARATOR = "separator";

  static JsonAdapter<Component> create(final Moshi moshi) {
    return new ComponentAdapter(moshi).nullSafe().lenient();
  }

  private final Moshi moshi;

  private ComponentAdapter(final Moshi moshi) {
    this.moshi = moshi;
  }

  @Override
  public Component fromJson(final JsonReader reader) throws IOException {
    return this.deserialize(reader.readJsonValue());
  }

  @SuppressWarnings("unchecked")
  private BuildableComponent<?, ?> deserialize(final Object element) throws JsonDataException {
    if (element instanceof String || element instanceof Number || element instanceof Boolean) {
      return Component.text(asString(element));
    } else if (element instanceof List<?>) {
      ComponentBuilder<?, ?> parent = null;
      for (final Object childElement : (List<?>) element) {
        final BuildableComponent<?, ?> child = this.deserialize(childElement);
        if (parent == null) {
          parent = child.toBuilder();
        } else {
          parent.append(child);
        }
      }
      if (parent == null) {
        throw notSureHowToDeserialize(element);
      }
      return parent.build();
    } else if (!(element instanceof Map<?, ?>)) {
      throw notSureHowToDeserialize(element);
    }

    final Map<String, Object> object = (Map<String, Object>) element;
    final ComponentBuilder<?, ?> component;
    if (object.containsKey(TEXT)) {
      component = Component.text().content(asString(object.get(TEXT)));
    } else if (object.containsKey(TRANSLATE)) {
      final String key = asString(object.get(TRANSLATE));
      if (!object.containsKey(TRANSLATE_WITH)) {
        component = Component.translatable().key(key);
      } else {
        final List<Object> with = (List<Object>) object.get(TRANSLATE_WITH);
        final List<Component> args = new ArrayList<>(with.size());
        for (int i = 0, size = with.size(); i < size; i++) {
          final Object argElement = with.get(i);
          args.add(this.deserialize(argElement));
        }
        component = Component.translatable().key(key).args(args);
      }
    } else if (object.containsKey(SCORE)) {
      final Map<String, Object> score = (Map<String, Object>) object.get(SCORE);
      if (!score.containsKey(SCORE_NAME) || !score.containsKey(SCORE_OBJECTIVE)) {
        throw new JsonDataException("A score component requires a " + SCORE_NAME + " and " + SCORE_OBJECTIVE);
      }
      final ScoreComponent.Builder builder = Component.score()
        .name(asString(score.get(SCORE_NAME)))
        .objective(asString(score.get(SCORE_OBJECTIVE)));
      // score components can have a value sometimes, let's grab it
      if (score.containsKey(SCORE_VALUE)) {
        component = builder.value(asString(score.get(SCORE_VALUE)));
      } else {
        component = builder;
      }
    } else if (object.containsKey(SELECTOR)) {
      final @Nullable Component separator = this.deserializeSeparator(object);
      component = Component.selector().pattern(asString(object.get(SELECTOR))).separator(separator);
    } else if (object.containsKey(KEYBIND)) {
      component = Component.keybind().keybind(asString(object.get(KEYBIND)));
    } else if (object.containsKey(NBT)) {
      final String nbt = asString(object.get(NBT));
      final boolean interpret = object.containsKey(NBT_INTERPRET) && asBoolean(object.get(NBT_INTERPRET));
      final @Nullable Component separator = this.deserializeSeparator(object);
      if (object.containsKey(NBT_BLOCK)) {
        final BlockNBTComponent.Pos pos = this.moshi.adapter(SerializerFactory.BLOCK_NBT_POS_TYPE).fromJsonValue(object.get(NBT_BLOCK));
        component = nbt(Component.blockNBT(), nbt, interpret, separator).pos(pos);
      } else if (object.containsKey(NBT_ENTITY)) {
        component = nbt(Component.entityNBT(), nbt, interpret, separator).selector(asString(object.get(NBT_ENTITY)));
      } else if (object.containsKey(NBT_STORAGE)) {
        component = nbt(Component.storageNBT(), nbt, interpret, separator).storage(Objects.requireNonNull(this.moshi.adapter(SerializerFactory.KEY_TYPE).fromJsonValue(object.get(NBT_STORAGE))));
      } else {
        throw notSureHowToDeserialize(element);
      }
    } else {
      throw notSureHowToDeserialize(element);
    }

    if (object.containsKey(EXTRA)) {
      final List<Object> extra = (List<Object>) object.get(EXTRA);
      for (int i = 0, size = extra.size(); i < size; i++) {
        final Object extraElement = extra.get(i);
        component.append(this.deserialize(extraElement));
      }
    }

    final Style style = this.moshi.adapter(SerializerFactory.STYLE_TYPE).fromJsonValue(element);
    if (!style.isEmpty()) {
      component.style(style);
    }

    return component.build();
  }

  @Override
  public void toJson(final JsonWriter writer, final Component value) throws IOException {
    writer.jsonValue(this.serialize(value));
  }

  @SuppressWarnings("unchecked")
  private Object serialize(final Component src) {
    final Map<String, Object> object = new HashMap<>();

    if (src.hasStyling()) {
      final Object style = this.moshi.adapter(SerializerFactory.STYLE_TYPE).toJsonValue(src.style());
      if (style instanceof Map<?, ?>) {
        for (final Map.Entry<String, Object> entry : ((Map<String, Object>) style).entrySet()) {
          object.put(entry.getKey(), entry.getValue());
        }
      }
    }

    final List<Component> children = src.children();
    if (!children.isEmpty()) {
      final List<Object> extra = new ArrayList<>();
      for (final Component child : children) {
        extra.add(this.serialize(child));
      }
      object.put(EXTRA, extra);
    }

    if (src instanceof TextComponent) {
      object.put(TEXT, ((TextComponent) src).content());
    } else if (src instanceof TranslatableComponent) {
      final TranslatableComponent tc = (TranslatableComponent) src;
      object.put(TRANSLATE, tc.key());
      if (!tc.args().isEmpty()) {
        final List<Object> with = new ArrayList<>();
        for (final Component arg : tc.args()) {
          with.add(this.serialize(arg));
        }
        object.put(TRANSLATE_WITH, with);
      }
    } else if (src instanceof ScoreComponent) {
      final ScoreComponent sc = (ScoreComponent) src;
      final Map<String, Object> score = new HashMap<>();
      score.put(SCORE_NAME, sc.name());
      score.put(SCORE_OBJECTIVE, sc.objective());
      // score component value is optional
      @SuppressWarnings("deprecation")
      final @Nullable String value = sc.value();
      if (value != null) {
        score.put(SCORE_VALUE, value);
      }
      object.put(SCORE, score);
    } else if (src instanceof SelectorComponent) {
      final SelectorComponent sc = (SelectorComponent) src;
      object.put(SELECTOR, sc.pattern());
      this.serializeSeparator(object, sc.separator());
    } else if (src instanceof KeybindComponent) {
      object.put(KEYBIND, ((KeybindComponent) src).keybind());
    } else if (src instanceof NBTComponent<?, ?>) {
      final NBTComponent<?, ?> nc = (NBTComponent<?, ?>) src;
      object.put(NBT, nc.nbtPath());
      object.put(NBT_INTERPRET, nc.interpret());
      if (src instanceof BlockNBTComponent) {
        final Object position = this.moshi.adapter(SerializerFactory.BLOCK_NBT_POS_TYPE).toJsonValue(((BlockNBTComponent) nc).pos());
        object.put(NBT_BLOCK, position);
        this.serializeSeparator(object, nc.separator());
      } else if (src instanceof EntityNBTComponent) {
        object.put(NBT_ENTITY, ((EntityNBTComponent) nc).selector());
      } else if (src instanceof StorageNBTComponent) {
        object.put(NBT_STORAGE, this.moshi.adapter(SerializerFactory.KEY_TYPE).toJsonValue(((StorageNBTComponent) nc).storage()));
      } else {
        throw notSureHowToSerialize(src);
      }
    } else {
      throw notSureHowToSerialize(src);
    }

    return object;
  }

  private @Nullable Component deserializeSeparator(final Map<String, Object> json) {
    if (json.containsKey(SEPARATOR)) {
      return this.fromJsonValue(json.get(SEPARATOR));
    }
    return null;
  }

  private void serializeSeparator(final Map<String, Object> json, final @Nullable Component separator) {
    if (separator != null) {
      json.put(SEPARATOR, this.serialize(separator));
    }
  }

  private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(final B builder, final String nbt, final boolean interpret, final @Nullable Component separator) {
    return builder
      .nbtPath(nbt)
      .interpret(interpret)
      .separator(separator);
  }

  private static String asString(final Object value) {
    if (value instanceof Number) {
      return value.toString();
    } else if (value instanceof Boolean) {
      return ((Boolean) value).toString();
    } else {
      return (String) value;
    }
  }

  private static boolean asBoolean(final Object value) {
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    return Boolean.parseBoolean(asString(value));
  }

  static JsonDataException notSureHowToDeserialize(final Object element) {
    return new JsonDataException("Don't know how to turn " + element + " into a Component");
  }

  private static IllegalArgumentException notSureHowToSerialize(final Component component) {
    return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
  }
}
