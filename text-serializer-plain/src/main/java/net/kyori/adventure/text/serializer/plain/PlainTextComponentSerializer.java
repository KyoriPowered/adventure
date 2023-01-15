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
package net.kyori.adventure.text.serializer.plain;

import java.util.function.Consumer;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import net.kyori.adventure.util.PlatformAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A plain-text component serializer.
 *
 * <p>Plain does <b>not</b> support more complex features such as, but not limited
 * to, colours, decorations, {@link ClickEvent}, and {@link HoverEvent}.</p>
 *
 * @since 4.8.0
 */
public interface PlainTextComponentSerializer extends ComponentSerializer<Component, TextComponent, String>, Buildable<PlainTextComponentSerializer, PlainTextComponentSerializer.Builder> {
  /**
   * A component serializer for plain-based serialization and deserialization.
   *
   * @return serializer instance
   * @since 4.8.0
   */
  static @NotNull PlainTextComponentSerializer plainText() {
    return PlainTextComponentSerializerImpl.Instances.INSTANCE;
  }

  /**
   * Create a new builder.
   *
   * @return a new plain serializer builder
   * @since 4.8.0
   */
  static PlainTextComponentSerializer.@NotNull Builder builder() {
    return new PlainTextComponentSerializerImpl.BuilderImpl();
  }

  @Override
  default @NotNull TextComponent deserialize(final @NotNull String input) {
    return Component.text(input);
  }

  @Override
  default @NotNull String serialize(final @NotNull Component component) {
    final StringBuilder sb = new StringBuilder();
    this.serialize(sb, component);
    return sb.toString();
  }

  /**
   * Serializes.
   *
   * @param sb the string builder
   * @param component the component
   * @since 4.8.0
   */
  void serialize(final @NotNull StringBuilder sb, final @NotNull Component component);

  /**
   * A builder for the plain-text component serializer.
   *
   * @since 4.8.0
   */
  interface Builder extends AbstractBuilder<PlainTextComponentSerializer>, Buildable.Builder<PlainTextComponentSerializer> {
    /**
     * Set the component flattener to use.
     *
     * <p>The default flattener is {@link ComponentFlattener#basic()} modified to throw exceptions on unknown component types.</p>
     *
     * @param flattener the new flattener
     * @return this builder
     * @since 4.8.0
     */
    @NotNull Builder flattener(final @NotNull ComponentFlattener flattener);
  }

  /**
   * A {@link PlainTextComponentSerializer} service provider.
   *
   * @since 4.8.0
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a {@link PlainTextComponentSerializer}.
     *
     * @return a {@link PlainTextComponentSerializer}
     * @since 4.8.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull PlainTextComponentSerializer plainTextSimple();

    /**
     * Completes the building process of {@link Builder}.
     *
     * @return a {@link Consumer}
     * @since 4.8.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull Consumer<Builder> plainText();
  }
}
