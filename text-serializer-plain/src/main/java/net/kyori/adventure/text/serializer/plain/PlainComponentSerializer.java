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
package net.kyori.adventure.text.serializer.plain;

import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A plain component serializer.
 *
 * <p>Plain does <b>not</b> support more complex features such as, but not limited
 * to, colours, decorations, {@link ClickEvent}, and {@link HoverEvent}.</p>
 *
 * @since 4.0.0
 */
public class PlainComponentSerializer implements ComponentSerializer<Component, TextComponent, String> {
  private static final PlainComponentSerializer INSTANCE = new PlainComponentSerializer();

  /**
   * A component serializer for plain-based serialization and deserialization.
   *
   * @return serializer instance
   * @since 4.0.0
   */
  public static @NonNull PlainComponentSerializer plain() {
    return INSTANCE;
  }

  private final @Nullable Function<KeybindComponent, String> keybind;
  private final @Nullable Function<TranslatableComponent, String> translatable;

  /**
   * Constructs.
   *
   * @since 4.0.0
   */
  public PlainComponentSerializer() {
    this(null, null);
  }

  /**
   * Constructs.
   *
   * @param keybind the keybind renderer
   * @param translatable the translatable renderer
   * @since 4.0.0
   */
  public PlainComponentSerializer(final @Nullable Function<KeybindComponent, String> keybind, final @Nullable Function<TranslatableComponent, String> translatable) {
    this.keybind = keybind;
    this.translatable = translatable;
  }

  @Override
  public @NonNull TextComponent deserialize(final @NonNull String input) {
    return Component.text(input);
  }

  @Override
  public @NonNull String serialize(final @NonNull Component component) {
    final StringBuilder sb = new StringBuilder();
    this.serialize(sb, component);
    return sb.toString();
  }

  /**
   * Serializes.
   *
   * @param sb the string builder
   * @param component the component
   * @since 4.0.0
   */
  public void serialize(final @NonNull StringBuilder sb, final @NonNull Component component) {
    if(component instanceof KeybindComponent) {
      if(this.keybind != null) sb.append(this.keybind.apply((KeybindComponent) component));
    } else if(component instanceof ScoreComponent) {
      sb.append(((ScoreComponent) component).value());
    } else if(component instanceof SelectorComponent) {
      sb.append(((SelectorComponent) component).pattern());
    } else if(component instanceof TextComponent) {
      sb.append(((TextComponent) component).content());
    } else if(component instanceof TranslatableComponent) {
      if(this.translatable != null) sb.append(this.translatable.apply((TranslatableComponent) component));
    } else {
      throw new UnsupportedOperationException("Don't know how to turn " + component.getClass().getSimpleName() + " into a string");
    }

    for(final Component child : component.children()) {
      this.serialize(sb, child);
    }
  }
}
