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
package net.kyori.adventure.text.renderer;

import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.VirtualComponent;

/**
 * An abstract implementation of a component renderer.
 *
 * <p><b>Note:</b> new {@code renderX} methods without default implementations may be
 * added at any time with the addition of new component types into Minecraft.</p>
 *
 * @param <C> the context type
 * @since 4.0.0
 */
public abstract class AbstractComponentRenderer<C> implements ComponentRenderer<C> {

  /**
   * Constructs a new abstract component renderer.
   *
   * @since 4.0.0
   */
  public AbstractComponentRenderer() {
  }

  /**
   * Renders a component.
   *
   * <p>This method exists to delegate calls to the protected
   * {@link #render(Component, Object, boolean)} method with {@code skipVirtual} set to {@code false}.</p>
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  @Override
  public Component render(final Component component, final C context) {
    return this.render(component, context, false);
  }

  /**
   * Renders a component.
   *
   * @param component the component
   * @param context the context
   * @param skipVirtual if virtual components should be rendered as normal text components
   * @return the rendered component
   * @since 4.0.0
   */
  protected final Component render(final Component component, final C context, final boolean skipVirtual) {
    return switch (component) {
      case TranslatableComponent tc -> this.renderTranslatable(tc, context);
      case KeybindComponent kc -> this.renderKeybind(kc, context);
      case ScoreComponent sc -> this.renderScore(sc, context);
      case SelectorComponent sc -> this.renderSelector(sc, context);
      case VirtualComponent vc when !skipVirtual -> this.render(this.renderVirtual(vc, context), context, true);
      case TextComponent tc -> this.renderText(tc, context);
      case BlockNBTComponent bnc -> this.renderBlockNbt(bnc, context);
      case EntityNBTComponent enc -> this.renderEntityNbt(enc, context);
      case StorageNBTComponent snc -> this.renderStorageNbt(snc, context);
      case ObjectComponent oc -> this.renderObject(oc, context);
    };
  }

  /**
   * Renders an object component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 5.0.0
   */
  protected abstract Component renderObject(final ObjectComponent component, final C context);

  /**
   * Renders a block NBT component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  protected abstract Component renderBlockNbt(final BlockNBTComponent component, final C context);

  /**
   * Renders an entity NBT component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  protected abstract Component renderEntityNbt(final EntityNBTComponent component, final C context);

  /**
   * Renders a storage NBT component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  protected abstract Component renderStorageNbt(final StorageNBTComponent component, final C context);

  /**
   * Renders a keybind component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  protected abstract Component renderKeybind(final KeybindComponent component, final C context);

  /**
   * Renders a score component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  protected abstract Component renderScore(final ScoreComponent component, final C context);

  /**
   * Renders a selector component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  protected abstract Component renderSelector(final SelectorComponent component, final C context);

  /**
   * Renders a text component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  protected abstract Component renderText(final TextComponent component, final C context);

  /**
   * Renders a virtual component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.18.0
   */
  protected Component renderVirtual(final VirtualComponent component, final C context) {
    return component;
  }

  /**
   * Renders a translatable component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  protected abstract Component renderTranslatable(final TranslatableComponent component, final C context);
}
