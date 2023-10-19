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
package net.kyori.adventure.text.renderer;

import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;

/**
 * An abstract implementation of a component renderer.
 *
 * @param <C> the context type
 * @since 4.0.0
 */
@NullMarked
public abstract class AbstractComponentRenderer<C> implements ComponentRenderer<C> {
  @Override
  public Component render(final Component component, final C context) {
    if (component instanceof TextComponent) {
      return this.renderText((TextComponent) component, context);
    } else if (component instanceof TranslatableComponent) {
      return this.renderTranslatable((TranslatableComponent) component, context);
    } else if (component instanceof KeybindComponent) {
      return this.renderKeybind((KeybindComponent) component, context);
    } else if (component instanceof ScoreComponent) {
      return this.renderScore((ScoreComponent) component, context);
    } else if (component instanceof SelectorComponent) {
      return this.renderSelector((SelectorComponent) component, context);
    } else if (component instanceof NBTComponent<?, ?>) {
      if (component instanceof BlockNBTComponent) {
        return this.renderBlockNbt((BlockNBTComponent) component, context);
      } else if (component instanceof EntityNBTComponent) {
        return this.renderEntityNbt((EntityNBTComponent) component, context);
      } else if (component instanceof StorageNBTComponent) {
        return this.renderStorageNbt((StorageNBTComponent) component, context);
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
  protected abstract Component renderBlockNbt(final BlockNBTComponent component, final C context);

  /**
   * Renders an entity NBT component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract Component renderEntityNbt(final EntityNBTComponent component, final C context);

  /**
   * Renders a storage NBT component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract Component renderStorageNbt(final StorageNBTComponent component, final C context);

  /**
   * Renders a keybind component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract Component renderKeybind(final KeybindComponent component, final C context);

  /**
   * Renders a score component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract Component renderScore(final ScoreComponent component, final C context);

  /**
   * Renders a selector component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract Component renderSelector(final SelectorComponent component, final C context);

  /**
   * Renders a text component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract Component renderText(final TextComponent component, final C context);

  /**
   * Renders a translatable component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   */
  protected abstract Component renderTranslatable(final TranslatableComponent component, final C context);
}
