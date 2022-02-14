/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.serializer.plain;

import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A plain component serializer.
 *
 * <p>Plain does <b>not</b> support more complex features such as, but not limited
 * to, colours, decorations, {@link ClickEvent}, and {@link HoverEvent}.</p>
 *
 * @since 4.0.0
 * @deprecated for removal since 4.8.0, use {@link PlainTextComponentSerializer} instead
 */
@ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
@Deprecated
public class PlainComponentSerializer implements ComponentSerializer<Component, TextComponent, String>, Buildable<PlainComponentSerializer, PlainComponentSerializer.Builder> {
  /**
   * A component serializer for plain-based serialization and deserialization.
   *
   * @return serializer instance
   * @since 4.0.0
   * @deprecated for removal since 4.8.0, use {@link PlainTextComponentSerializer#plainText()} instead
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public static @NotNull PlainComponentSerializer plain() {
    return PlainComponentSerializerImpl.INSTANCE;
  }

  /**
   * Create a new builder.
   *
   * @return a new plain serializer builder
   * @since 4.7.0
   * @deprecated for removal since 4.8.0, use {@link PlainTextComponentSerializer#builder()} instead
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public static PlainComponentSerializer.@NotNull Builder builder() {
    return new PlainComponentSerializerImpl.BuilderImpl();
  }

  @Deprecated final PlainTextComponentSerializer serializer;

  /**
   * Constructs.
   *
   * @since 4.0.0
   * @deprecated for removal since 4.7.0
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public PlainComponentSerializer() {
    this(PlainTextComponentSerializer.plainText());
  }

  /**
   * Constructs.
   *
   * @param keybind the keybind renderer
   * @param translatable the translatable renderer
   * @since 4.0.0
   * @deprecated for removal since 4.7.0, use the builder instead
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public PlainComponentSerializer(final @Nullable Function<KeybindComponent, String> keybind, final @Nullable Function<TranslatableComponent, String> translatable) {
    this(PlainComponentSerializerImpl.createRealSerializerFromLegacyFunctions(keybind, translatable));
  }

  @Deprecated
  PlainComponentSerializer(final @NotNull PlainTextComponentSerializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public @NotNull TextComponent deserialize(final @NotNull String input) {
    return this.serializer.deserialize(input);
  }

  @Override
  public @NotNull String serialize(final @NotNull Component component) {
    return this.serializer.serialize(component);
  }

  /**
   * Serializes.
   *
   * @param sb the string builder
   * @param component the component
   * @since 4.0.0
   * @deprecated for removal since 4.8.0, use {@link PlainTextComponentSerializer#serialize(StringBuilder, Component)} instead
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public void serialize(final @NotNull StringBuilder sb, final @NotNull Component component) {
    this.serializer.serialize(sb, component);
  }

  @Override
  public PlainComponentSerializer.@NotNull Builder toBuilder() {
    return new PlainComponentSerializerImpl.BuilderImpl(this);
  }

  /**
   * A builder for the plain component serializer.
   *
   * @since 4.7.0
   * @deprecated for removal since 4.8.0, use {@link PlainTextComponentSerializer.Builder} instead
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public interface Builder extends Buildable.Builder<PlainComponentSerializer> {
    /**
     * Set the component flattener to use.
     *
     * <p>The default flattener is {@link ComponentFlattener#basic()} modified to throw exceptions on unknown component types.</p>
     *
     * @param flattener the new flattener
     * @return this builder
     * @since 4.7.0
     * @deprecated for removal since 4.8.0, use {@link PlainTextComponentSerializer.Builder#flattener(ComponentFlattener)} instead
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @Deprecated
    @NotNull Builder flattener(final @NotNull ComponentFlattener flattener);
  }
}
