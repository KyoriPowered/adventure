/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
import com.google.gson.GsonBuilder;
import java.util.function.UnaryOperator;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class GsonComponentSerializerImpl implements GsonComponentSerializer {
  static final GsonComponentSerializer INSTANCE = new GsonComponentSerializerImpl(false, null, false);
  static final GsonComponentSerializer LEGACY_INSTANCE = new GsonComponentSerializerImpl(true, null, true);

  private final Gson serializer;
  private final UnaryOperator<GsonBuilder> populator;
  private final boolean downsampleColor;
  private final @Nullable LegacyHoverEventSerializer legacyHoverSerializer;
  private final boolean emitLegacyHover;

  GsonComponentSerializerImpl(final boolean downsampleColor, final @Nullable LegacyHoverEventSerializer legacyHoverSerializer, final boolean emitLegacyHover) {
    this.downsampleColor = downsampleColor;
    this.legacyHoverSerializer = legacyHoverSerializer;
    this.emitLegacyHover = emitLegacyHover;
    this.populator = builder -> {
      builder.registerTypeHierarchyAdapter(Key.class, KeySerializer.INSTANCE);
      builder.registerTypeHierarchyAdapter(Component.class, new ComponentSerializerImpl());
      builder.registerTypeHierarchyAdapter(Style.class, new StyleSerializer(legacyHoverSerializer, emitLegacyHover));
      builder.registerTypeAdapter(ClickEvent.Action.class, IndexedSerializer.of("click action", ClickEvent.Action.NAMES));
      builder.registerTypeAdapter(HoverEvent.Action.class, IndexedSerializer.of("hover action", HoverEvent.Action.NAMES));
      builder.registerTypeAdapter(HoverEvent.ShowItem.class, new ShowItemSerializer());
      builder.registerTypeAdapter(HoverEvent.ShowEntity.class, new ShowEntitySerializer());
      builder.registerTypeAdapter(TextColorWrapper.class, new TextColorWrapper.Serializer());
      builder.registerTypeHierarchyAdapter(TextColor.class, downsampleColor ? TextColorSerializer.DOWNSAMPLE_COLOR : TextColorSerializer.INSTANCE);
      builder.registerTypeAdapter(TextDecoration.class, IndexedSerializer.of("text decoration", TextDecoration.NAMES));
      builder.registerTypeHierarchyAdapter(BlockNBTComponent.Pos.class, BlockNBTComponentPosSerializer.INSTANCE);
      return builder;
    };
    this.serializer = this.populator.apply(new GsonBuilder()).create();
  }

  @Override
  public @NonNull Gson serializer() {
    return this.serializer;
  }

  @Override
  public @NonNull UnaryOperator<GsonBuilder> populator() {
    return this.populator;
  }

  @Override
  public @NonNull Component deserialize(final @NonNull String string) {
    return this.serializer().fromJson(string, Component.class);
  }

  @Override
  public @NonNull String serialize(final @NonNull Component component) {
    return this.serializer().toJson(component);
  }

  @NonNull
  @Override
  public Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl implements Builder {
    private boolean downsampleColor = false;
    private @Nullable LegacyHoverEventSerializer legacyHoverSerializer;
    private boolean emitLegacyHover = false;

    BuilderImpl() {
    }

    BuilderImpl(final GsonComponentSerializerImpl serializer) {
      this.downsampleColor = serializer.downsampleColor;
      this.emitLegacyHover = serializer.emitLegacyHover;
      this.legacyHoverSerializer = serializer.legacyHoverSerializer;
    }

    @Override
    public @NonNull Builder downsampleColors() {
      this.downsampleColor = true;
      return this;
    }

    @Override
    public @NonNull Builder legacyHoverEventSerializer(final @Nullable LegacyHoverEventSerializer serializer) {
      this.legacyHoverSerializer = serializer;
      return this;
    }

    @Override
    public @NonNull Builder emitLegacyHoverEvent() {
      this.emitLegacyHover = true;
      return this;
    }

    @Override
    public @NonNull GsonComponentSerializer build() {
      if(this.legacyHoverSerializer == null) {
        return this.downsampleColor ? LEGACY_INSTANCE : INSTANCE;
      } else {
        return new GsonComponentSerializerImpl(this.downsampleColor, this.legacyHoverSerializer, this.emitLegacyHover);
      }
    }
  }
}
