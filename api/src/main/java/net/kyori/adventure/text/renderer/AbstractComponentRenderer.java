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
package net.kyori.adventure.text.renderer;

import net.kyori.adventure.text.BlockNbtComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNbtComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NbtComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNbtComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An abstract implementation of a component renderer.
 *
 * @param <C> the context type
 */
@SuppressWarnings("Duplicates")
public abstract class AbstractComponentRenderer<C> implements ComponentRenderer<C> {
  @Override
  public @NonNull Component render(final @NonNull Component component, final @NonNull C context) {
    if(component instanceof TextComponent) {
      return this.renderText((TextComponent) component, context);
    } else if(component instanceof TranslatableComponent) {
      return this.renderTranslatable((TranslatableComponent) component, context);
    } else if(component instanceof KeybindComponent) {
      return this.renderKeybind((KeybindComponent) component, context);
    } else if(component instanceof ScoreComponent) {
      return this.renderScore((ScoreComponent) component, context);
    } else if(component instanceof SelectorComponent) {
      return this.renderSelector((SelectorComponent) component, context);
    } else if(component instanceof NbtComponent<?, ?>) {
      if(component instanceof BlockNbtComponent) {
        return this.renderBlockNbt((BlockNbtComponent) component, context);
      } else if(component instanceof EntityNbtComponent) {
        return this.renderEntityNbt((EntityNbtComponent) component, context);
      } else if(component instanceof StorageNbtComponent) {
        return this.renderStorageNbt((StorageNbtComponent) component, context);
      }
    }
    return component;
  }

  /**
   * Renders a block NBT component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract @NonNull Component renderBlockNbt(final @NonNull BlockNbtComponent component, final @NonNull C context);

  /**
   * Renders an entity NBT component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract @NonNull Component renderEntityNbt(final @NonNull EntityNbtComponent component, final @NonNull C context);

  /**
   * Renders a storage NBT component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract @NonNull Component renderStorageNbt(final @NonNull StorageNbtComponent component, final @NonNull C context);

  /**
   * Renders a keybind component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract @NonNull Component renderKeybind(final @NonNull KeybindComponent component, final @NonNull C context);

  /**
   * Renders a score component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract @NonNull Component renderScore(final @NonNull ScoreComponent component, final @NonNull C context);

  /**
   * Renders a selector component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract @NonNull Component renderSelector(final @NonNull SelectorComponent component, final @NonNull C context);

  /**
   * Renders a text component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract @NonNull Component renderText(final @NonNull TextComponent component, final @NonNull C context);

  /**
   * Renders a translatable component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract @NonNull Component renderTranslatable(final @NonNull TranslatableComponent component, final @NonNull C context);
}
