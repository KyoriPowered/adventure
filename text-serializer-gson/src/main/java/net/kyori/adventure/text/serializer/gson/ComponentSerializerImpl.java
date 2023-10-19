/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.key.Key;
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
import org.jspecify.annotations.Nullable;

import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.EXTRA;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.KEYBIND;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT_BLOCK;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT_ENTITY;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT_INTERPRET;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.NBT_STORAGE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SCORE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SCORE_NAME;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SCORE_OBJECTIVE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SCORE_VALUE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SELECTOR;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.SEPARATOR;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.TEXT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.TRANSLATE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.TRANSLATE_FALLBACK;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.TRANSLATE_WITH;

final class ComponentSerializerImpl extends TypeAdapter<Component> {
  static final Type COMPONENT_LIST_TYPE = new TypeToken<List<Component>>() {}.getType();

  static TypeAdapter<Component> create(final Gson gson) {
    return new ComponentSerializerImpl(gson).nullSafe();
  }

  private final Gson gson;

  private ComponentSerializerImpl(final Gson gson) {
    this.gson = gson;
  }

  @Override
  public BuildableComponent<?, ?> read(final JsonReader in) throws IOException {
    final JsonToken token = in.peek();
    if (token == JsonToken.STRING || token == JsonToken.NUMBER || token == JsonToken.BOOLEAN) {
      return Component.text(GsonHacks.readString(in));
    } else if (token == JsonToken.BEGIN_ARRAY) {
      ComponentBuilder<?, ?> parent = null;
      in.beginArray();
      while (in.hasNext()) {
        final BuildableComponent<?, ?> child = this.read(in);
        if (parent == null) {
          parent = child.toBuilder();
        } else {
          parent.append(child);
        }
      }
      if (parent == null) {
        throw notSureHowToDeserialize(in.getPath());
      }
      in.endArray();
      return parent.build();
    } else if (token != JsonToken.BEGIN_OBJECT) {
      throw notSureHowToDeserialize(in.getPath());
    }

    // common to all component types
    final JsonObject style = new JsonObject();
    List<Component> extra = Collections.emptyList();

    // type specific
    String text = null;
    String translate = null;
    String translateFallback = null;
    List<Component> translateWith = null;
    String scoreName = null;
    String scoreObjective = null;
    String scoreValue = null;
    String selector = null;
    String keybind = null;
    String nbt = null;
    boolean nbtInterpret = false;
    BlockNBTComponent.Pos nbtBlock = null;
    String nbtEntity = null;
    Key nbtStorage = null;
    Component separator = null;

    in.beginObject();
    while (in.hasNext()) {
      final String fieldName = in.nextName();
      if (fieldName.equals(TEXT)) {
        text = GsonHacks.readString(in);
      } else if (fieldName.equals(TRANSLATE)) {
        translate = in.nextString();
      } else if (fieldName.equals(TRANSLATE_FALLBACK)) {
        translateFallback = in.nextString();
      } else if (fieldName.equals(TRANSLATE_WITH)) {
        translateWith = this.gson.fromJson(in, COMPONENT_LIST_TYPE);
      } else if (fieldName.equals(SCORE)) {
        in.beginObject();
        while (in.hasNext()) {
          final String scoreFieldName = in.nextName();
          if (scoreFieldName.equals(SCORE_NAME)) {
            scoreName = in.nextString();
          } else if (scoreFieldName.equals(SCORE_OBJECTIVE)) {
            scoreObjective = in.nextString();
          } else if (scoreFieldName.equals(SCORE_VALUE)) {
            scoreValue = in.nextString();
          } else {
            in.skipValue();
          }
        }
        if (scoreName == null || scoreObjective == null) {
          throw new JsonParseException("A score component requires a " + SCORE_NAME + " and " + SCORE_OBJECTIVE);
        }
        in.endObject();
      } else if (fieldName.equals(SELECTOR)) {
        selector = in.nextString();
      } else if (fieldName.equals(KEYBIND)) {
        keybind = in.nextString();
      } else if (fieldName.equals(NBT)) {
        nbt = in.nextString();
      } else if (fieldName.equals(NBT_INTERPRET)) {
        nbtInterpret = in.nextBoolean();
      } else if (fieldName.equals(NBT_BLOCK)) {
        nbtBlock = this.gson.fromJson(in, SerializerFactory.BLOCK_NBT_POS_TYPE);
      } else if (fieldName.equals(NBT_ENTITY)) {
        nbtEntity = in.nextString();
      } else if (fieldName.equals(NBT_STORAGE)) {
        nbtStorage = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
      } else if (fieldName.equals(EXTRA)) {
        extra = this.gson.fromJson(in, COMPONENT_LIST_TYPE);
      } else if (fieldName.equals(SEPARATOR)) {
        separator = this.read(in);
      } else {
        style.add(fieldName, this.gson.fromJson(in, JsonElement.class));
      }
    }

    final ComponentBuilder<?, ?> builder;
    if (text != null) {
      builder = Component.text().content(text);
    } else if (translate != null) {
      if (translateWith != null) {
        builder = Component.translatable().key(translate).fallback(translateFallback).args(translateWith);
      } else {
        builder = Component.translatable().key(translate).fallback(translateFallback);
      }
    } else if (scoreName != null && scoreObjective != null) {
      if (scoreValue == null) {
        builder = Component.score().name(scoreName).objective(scoreObjective);
      } else {
        builder = Component.score().name(scoreName).objective(scoreObjective).value(scoreValue);
      }
    } else if (selector != null) {
      builder = Component.selector().pattern(selector).separator(separator);
    } else if (keybind != null) {
      builder = Component.keybind().keybind(keybind);
    } else if (nbt != null) {
      if (nbtBlock != null) {
        builder = nbt(Component.blockNBT(), nbt, nbtInterpret, separator).pos(nbtBlock);
      } else if (nbtEntity != null) {
        builder = nbt(Component.entityNBT(), nbt, nbtInterpret, separator).selector(nbtEntity);
      } else if (nbtStorage != null) {
        builder = nbt(Component.storageNBT(), nbt, nbtInterpret, separator).storage(nbtStorage);
      } else {
        throw notSureHowToDeserialize(in.getPath());
      }
    } else {
      throw notSureHowToDeserialize(in.getPath());
    }

    builder.style(this.gson.fromJson(style, SerializerFactory.STYLE_TYPE))
        .append(extra);
    in.endObject();
    return builder.build();
  }

  private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(final B builder, final String nbt, final boolean interpret, final @Nullable Component separator) {
    return builder
      .nbtPath(nbt)
      .interpret(interpret)
      .separator(separator);
  }

  @Override
  public void write(final JsonWriter out, final Component value) throws IOException {
    out.beginObject();

    if (value.hasStyling()) {
      final JsonElement style = this.gson.toJsonTree(value.style(), SerializerFactory.STYLE_TYPE);
      if (style.isJsonObject()) {
        for (final Map.Entry<String, JsonElement> entry : style.getAsJsonObject().entrySet()) {
          out.name(entry.getKey());
          this.gson.toJson(entry.getValue(), out);
        }
      }
    }

    if (!value.children().isEmpty()) {
      out.name(EXTRA);
      this.gson.toJson(value.children(), COMPONENT_LIST_TYPE, out);
    }

    if (value instanceof TextComponent) {
      out.name(TEXT);
      out.value(((TextComponent) value).content());
    } else if (value instanceof TranslatableComponent) {
      final TranslatableComponent translatable = (TranslatableComponent) value;
      out.name(TRANSLATE);
      out.value(translatable.key());
      final @Nullable String fallback = translatable.fallback();
      if (fallback != null) {
        out.name(TRANSLATE_FALLBACK);
        out.value(fallback);
      }
      if (!translatable.args().isEmpty()) {
        out.name(TRANSLATE_WITH);
        this.gson.toJson(translatable.args(), COMPONENT_LIST_TYPE, out);
      }
    } else if (value instanceof ScoreComponent) {
      final ScoreComponent score = (ScoreComponent) value;
      out.name(SCORE);
      out.beginObject();
      out.name(SCORE_NAME);
      out.value(score.name());
      out.name(SCORE_OBJECTIVE);
      out.value(score.objective());
      if (score.value() != null) {
        out.name(SCORE_VALUE);
        out.value(score.value());
      }
      out.endObject();
    } else if (value instanceof SelectorComponent) {
      final SelectorComponent selector = (SelectorComponent) value;
      out.name(SELECTOR);
      out.value(selector.pattern());
      this.serializeSeparator(out, selector.separator());
    } else if (value instanceof KeybindComponent) {
      out.name(KEYBIND);
      out.value(((KeybindComponent) value).keybind());
    } else if (value instanceof NBTComponent) {
      final NBTComponent<?, ?> nbt = (NBTComponent<?, ?>) value;
      out.name(NBT);
      out.value(nbt.nbtPath());
      out.name(NBT_INTERPRET);
      out.value(nbt.interpret());
      this.serializeSeparator(out, nbt.separator());
      if (value instanceof BlockNBTComponent) {
        out.name(NBT_BLOCK);
        this.gson.toJson(((BlockNBTComponent) value).pos(), SerializerFactory.BLOCK_NBT_POS_TYPE, out);
      } else if (value instanceof EntityNBTComponent) {
        out.name(NBT_ENTITY);
        out.value(((EntityNBTComponent) value).selector());
      } else if (value instanceof StorageNBTComponent) {
        out.name(NBT_STORAGE);
        this.gson.toJson(((StorageNBTComponent) value).storage(), SerializerFactory.KEY_TYPE, out);
      } else {
        throw notSureHowToSerialize(value);
      }
    } else {
      throw notSureHowToSerialize(value);
    }

    out.endObject();
  }

  private void serializeSeparator(final JsonWriter out, final @Nullable Component separator) throws IOException {
    if (separator != null) {
      out.name(SEPARATOR);
      this.write(out, separator);
    }
  }

  static JsonParseException notSureHowToDeserialize(final Object element) {
    return new JsonParseException("Don't know how to turn " + element + " into a Component");
  }

  private static IllegalArgumentException notSureHowToSerialize(final Component component) {
    return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
  }
}
