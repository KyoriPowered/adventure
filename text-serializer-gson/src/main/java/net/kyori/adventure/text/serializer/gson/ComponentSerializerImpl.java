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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.option.OptionState;
import org.jspecify.annotations.Nullable;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.EXTRA;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.KEYBIND;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_BLOCK;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_ENTITY;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_INTERPRET;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_PLAIN;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_STORAGE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_ATLAS;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_FALLBACK;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_HAT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER_PROPERTIES;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER_TEXTURE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_SPRITE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE_OBJECTIVE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE_VALUE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SELECTOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SEPARATOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TEXT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE_FALLBACK;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE_WITH;

final class ComponentSerializerImpl extends TypeAdapter<Component> {
  static final Type COMPONENT_LIST_TYPE = new TypeToken<List<Component>>() {}.getType();
  static final Type TRANSLATABLE_ARGUMENT_LIST_TYPE = new TypeToken<List<TranslationArgument>>() {}.getType();
  static final Type PROPERTY_LIST_TYPE = new TypeToken<List<PlayerHeadObjectContents.ProfileProperty>>() {}.getType();

  static TypeAdapter<Component> create(final OptionState features, final Gson gson) {
    return new ComponentSerializerImpl(features.value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT), gson).nullSafe();
  }

  private final boolean emitCompactTextComponent;
  private final Gson gson;

  private ComponentSerializerImpl(final boolean emitCompactTextComponent, final Gson gson) {
    this.emitCompactTextComponent = emitCompactTextComponent;
    this.gson = gson;
  }

  @Override
  public Component read(final JsonReader in) throws IOException {
    final JsonToken token = in.peek();
    if (couldBeRepresentedAsString(token)) {
      return Component.text(GsonHacks.readString(in));
    } else if (token == JsonToken.BEGIN_ARRAY) {
      ComponentBuilder<?, ?> parent = null;
      in.beginArray();
      while (in.hasNext()) {
        final Component child = this.read(in);
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
    List<Component> extra = List.of();

    // type specific
    String text = null;
    String translate = null;
    String translateFallback = null;
    List<TranslationArgument> translateWith = null;
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
    boolean nbtPlain = false;
    Component separator = null;
    Key atlas = null;
    Key sprite = null;
    PlayerHeadObjectContents.Builder playerHeadContents = null;
    boolean playerHeadContentsHasProfile = false;
    Component objectFallback = null;

    in.beginObject();
    while (in.hasNext()) {
      final String fieldName = in.nextName();
      switch (fieldName) {
        case TEXT -> text = GsonHacks.readString(in);
        case TRANSLATE -> translate = in.nextString();
        case TRANSLATE_FALLBACK -> { // Also object fallback - they share the same name!
          final JsonToken peek = in.peek();
          if (couldBeRepresentedAsString(peek)) {
            // Store plain strings in both fallback objects.
            translateFallback = GsonHacks.readString(in);
            objectFallback = Component.text(translateFallback);
          } else if (peek == JsonToken.BEGIN_OBJECT || peek == JsonToken.BEGIN_ARRAY) {
            // This *could* be a component, let's try and convert it!
            objectFallback = this.read(in);
          } else {
            throw notSureHowToDeserialize(in.getPath());
          }
        }
        case TRANSLATE_WITH -> translateWith = this.gson.fromJson(in, TRANSLATABLE_ARGUMENT_LIST_TYPE);
        case SCORE -> {
          in.beginObject();
          while (in.hasNext()) {
            final String scoreFieldName = in.nextName();
            switch (scoreFieldName) {
              case SCORE_NAME -> scoreName = in.nextString();
              case SCORE_OBJECTIVE -> scoreObjective = in.nextString();
              case SCORE_VALUE -> scoreValue = in.nextString();
              default -> in.skipValue();
            }
          }
          if (scoreName == null || scoreObjective == null) {
            throw new JsonParseException("A score component requires a " + SCORE_NAME + " and " + SCORE_OBJECTIVE);
          }
          in.endObject();
        }
        case SELECTOR -> selector = in.nextString();
        case KEYBIND -> keybind = in.nextString();
        case NBT -> nbt = in.nextString();
        case NBT_INTERPRET -> nbtInterpret = in.nextBoolean();
        case NBT_BLOCK -> nbtBlock = this.gson.fromJson(in, SerializerFactory.BLOCK_NBT_POS_TYPE);
        case NBT_ENTITY -> nbtEntity = in.nextString();
        case NBT_STORAGE -> nbtStorage = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
        case NBT_PLAIN -> nbtPlain = in.nextBoolean();
        case EXTRA -> extra = this.gson.fromJson(in, COMPONENT_LIST_TYPE);
        case SEPARATOR -> separator = this.read(in);
        case OBJECT_ATLAS -> atlas = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
        case OBJECT_SPRITE -> sprite = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
        case OBJECT_PLAYER -> {
          if (playerHeadContents == null) playerHeadContents = ObjectContents.playerHead();
          final JsonToken playerToken = in.peek();
          // `player` can be either just the name or a partial profile
          if (playerToken == JsonToken.STRING) {
            playerHeadContentsHasProfile = true;
            playerHeadContents.name(in.nextString());
          } else if (playerToken == JsonToken.BEGIN_OBJECT) {
            playerHeadContentsHasProfile = true;
            in.beginObject();
            while (in.hasNext()) {
              final String playerHeadFieldName = in.nextName();
              switch (playerHeadFieldName) {
                case OBJECT_PLAYER_NAME -> playerHeadContents.name(in.nextString());
                case OBJECT_PLAYER_ID -> playerHeadContents.id(this.gson.fromJson(in, SerializerFactory.UUID_TYPE));
                case OBJECT_PLAYER_PROPERTIES -> {
                  final JsonToken propertyToken = in.peek();
                  if (propertyToken == JsonToken.BEGIN_ARRAY) {
                    playerHeadContents.profileProperties(this.gson.fromJson(in, PROPERTY_LIST_TYPE));
                  } else if (propertyToken == JsonToken.BEGIN_OBJECT) {
                    in.beginObject();
                    while (in.hasNext()) {
                      final String propertyName = in.nextName();
                      in.beginArray();
                      while (in.hasNext()) {
                        playerHeadContents.profileProperty(PlayerHeadObjectContents.property(propertyName, in.nextString()));
                      }
                      in.endArray();
                    }
                    in.endObject();
                  } else {
                    in.skipValue();
                  }
                }
                case OBJECT_PLAYER_TEXTURE -> playerHeadContents.texture(this.gson.fromJson(in, SerializerFactory.KEY_TYPE));
                default -> in.skipValue();
              }
            }
            in.endObject();
          } else {
            in.skipValue();
          }
        }
        case OBJECT_HAT -> {
          if (playerHeadContents == null) playerHeadContents = ObjectContents.playerHead();
          playerHeadContents.hat(in.nextBoolean());
        }
        default -> style.add(fieldName, this.gson.fromJson(in, JsonElement.class));
      }
    }

    final ComponentBuilder<?, ?> builder;
    if (text != null) {
      builder = Component.text().content(text);
    } else if (translate != null) {
      if (translateWith != null) {
        builder = Component.translatable().key(translate).fallback(translateFallback).arguments(translateWith);
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
        builder = nbt(Component.blockNBT(), nbt, nbtInterpret, separator, nbtPlain).pos(nbtBlock);
      } else if (nbtEntity != null) {
        builder = nbt(Component.entityNBT(), nbt, nbtInterpret, separator, nbtPlain).selector(nbtEntity);
      } else if (nbtStorage != null) {
        builder = nbt(Component.storageNBT(), nbt, nbtInterpret, separator, nbtPlain).storage(nbtStorage);
      } else {
        throw notSureHowToDeserialize(in.getPath());
      }
    } else if (sprite != null) {
      builder = Component.object().contents(ObjectContents.sprite(
        atlas != null ? atlas : SpriteObjectContents.DEFAULT_ATLAS,
        sprite
      )).fallback(objectFallback);
    } else if (playerHeadContents != null && playerHeadContentsHasProfile) {
      builder = Component.object().contents(playerHeadContents.build()).fallback(objectFallback);
    } else {
      throw notSureHowToDeserialize(in.getPath());
    }

    builder.style(this.gson.fromJson(style, SerializerFactory.STYLE_TYPE))
      .append(extra);
    in.endObject();
    return builder.build();
  }

  private static <C extends NBTComponent<C>, B extends NBTComponentBuilder<C, B>> B nbt(final B builder, final String nbt, final boolean interpret, final @Nullable Component separator, final boolean plain) {
    // Check manually to throw more specific exception.
    if (plain && interpret) throw new JsonParseException("Cannot have `plain` and `interpret` set to true at the same time");
    return builder
      .nbtPath(nbt)
      .interpret(interpret)
      .separator(separator)
      .plain(plain);
  }

  @Override
  public void write(final JsonWriter out, final Component value) throws IOException {
    if (
      value instanceof TextComponent tc
        && value.children().isEmpty()
        && !value.hasStyling()
        && this.emitCompactTextComponent
    ) {
      out.value(tc.content());
      return;
    }

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

    switch (value) {
      case TextComponent textComponent -> {
        out.name(TEXT);
        out.value(textComponent.content());
      }
      case TranslatableComponent translatable -> {
        out.name(TRANSLATE);
        out.value(translatable.key());
        final String fallback = translatable.fallback();
        if (fallback != null) {
          out.name(TRANSLATE_FALLBACK);
          out.value(fallback);
        }
        if (!translatable.arguments().isEmpty()) {
          out.name(TRANSLATE_WITH);
          this.gson.toJson(translatable.arguments(), TRANSLATABLE_ARGUMENT_LIST_TYPE, out);
        }
      }
      case ScoreComponent score -> {
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
      }
      case SelectorComponent selector -> {
        out.name(SELECTOR);
        out.value(selector.pattern());
        this.serializeSeparator(out, selector.separator());
      }
      case KeybindComponent keybindComponent -> {
        out.name(KEYBIND);
        out.value(keybindComponent.keybind());
      }
      case NBTComponent<?> nbt -> {
        out.name(NBT);
        out.value(nbt.nbtPath());
        out.name(NBT_INTERPRET);
        out.value(nbt.interpret());
        out.name(NBT_PLAIN);
        out.value(nbt.plain());
        this.serializeSeparator(out, nbt.separator());
        switch (value) {
          case BlockNBTComponent blockNBTComponent -> {
            out.name(NBT_BLOCK);
            this.gson.toJson(blockNBTComponent.pos(), SerializerFactory.BLOCK_NBT_POS_TYPE, out);
          }
          case EntityNBTComponent entityNBTComponent -> {
            out.name(NBT_ENTITY);
            out.value(entityNBTComponent.selector());
          }
          case StorageNBTComponent storageNBTComponent -> {
            out.name(NBT_STORAGE);
            this.gson.toJson(storageNBTComponent.storage(), SerializerFactory.KEY_TYPE, out);
          }
          default -> throw notSureHowToSerialize(value);
        }
      }
      case ObjectComponent objectComponent -> {
        final Component fallback = objectComponent.fallback();
        if (fallback != null) {
          out.name(OBJECT_FALLBACK);
          this.write(out, fallback);
        }

        final ObjectContents contents = objectComponent.contents();
        if (contents instanceof final SpriteObjectContents spriteContents) {
          if (!spriteContents.atlas().equals(SpriteObjectContents.DEFAULT_ATLAS)) {
            out.name(OBJECT_ATLAS);
            this.gson.toJson(spriteContents.atlas(), SerializerFactory.KEY_TYPE, out);
          }
          out.name(OBJECT_SPRITE);
          this.gson.toJson(spriteContents.sprite(), SerializerFactory.KEY_TYPE, out);
        } else if (contents instanceof final PlayerHeadObjectContents playerHeadContents) {
          out.name(OBJECT_HAT);
          out.value(playerHeadContents.hat());
          final String playerName = playerHeadContents.name();
          final UUID playerId = playerHeadContents.id();
          final List<PlayerHeadObjectContents.ProfileProperty> properties = playerHeadContents.profileProperties();
          final Key texture = playerHeadContents.texture();
          out.name(OBJECT_PLAYER);
          if (playerName != null && playerId == null && properties.isEmpty() && texture == null) {
            out.value(playerName);
          } else {
            out.beginObject();
            if (playerName != null) {
              out.name(OBJECT_PLAYER_NAME);
              out.value(playerName);
            }
            if (playerId != null) {
              out.name(OBJECT_PLAYER_ID);
              this.gson.toJson(playerId, SerializerFactory.UUID_TYPE, out);
            }
            if (!properties.isEmpty()) {
              out.name(OBJECT_PLAYER_PROPERTIES);
              this.gson.toJson(properties, PROPERTY_LIST_TYPE, out);
            }
            if (texture != null) {
              out.name(OBJECT_PLAYER_TEXTURE);
              this.gson.toJson(texture, SerializerFactory.KEY_TYPE, out);
            }
            out.endObject();
          }
        } else {
          throw notSureHowToSerialize(value);
        }
      }
      default -> throw notSureHowToSerialize(value);
    }

    out.endObject();
  }

  private void serializeSeparator(final JsonWriter out, final @Nullable Component separator) throws IOException {
    if (separator != null) {
      out.name(SEPARATOR);
      this.write(out, separator);
    }
  }

  private static boolean couldBeRepresentedAsString(final JsonToken type) {
    return type == JsonToken.STRING || type == JsonToken.NUMBER || type == JsonToken.BOOLEAN;
  }

  static JsonParseException notSureHowToDeserialize(final Object element) {
    return new JsonParseException("Don't know how to turn " + element + " into a Component");
  }

  private static IllegalArgumentException notSureHowToSerialize(final Component component) {
    return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
  }
}
