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
package net.kyori.adventure.text.event;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.renderer.ComponentRenderer;

final class HoverEventRenderers {
  private HoverEventRenderers() {
  }

  static final ShowText SHOW_TEXT = new ShowText();
  static final ShowItem SHOW_ITEM = new ShowItem();
  static final ShowEntity SHOW_ENTITY = new ShowEntity();
  static final ShowAchievement SHOW_ACHIEVEMENT = new ShowAchievement();

  static final class ShowText implements HoverEvent.Action.Renderer<Component> {
    private ShowText() {
    }

    @Override
    public <C> Component render(final ComponentRenderer<C> renderer, final C context, final Component value) {
      return renderer.render(value, context);
    }
  }

  static final class ShowItem implements HoverEvent.Action.Renderer<HoverEvent.ShowItem> {
    private ShowItem() {
    }

    @Override
    public <C> HoverEvent.ShowItem render(final ComponentRenderer<C> renderer, final C context, final HoverEvent.ShowItem value) {
      return value;
    }
  }

  static final class ShowEntity implements HoverEvent.Action.Renderer<HoverEvent.ShowEntity> {
    private ShowEntity() {
    }

    @Override
    public <C> HoverEvent.ShowEntity render(final ComponentRenderer<C> renderer, final C context, final HoverEvent.ShowEntity value) {
      final Component name = value.name();
      if (name == null) return value;
      return value.name(renderer.render(name, context));
    }
  }

  static final class ShowAchievement implements HoverEvent.Action.Renderer<String> {
    private ShowAchievement() {
    }

    @Override
    public <C> String render(final ComponentRenderer<C> renderer, final C context, final String value) {
      return value;
    }
  }
}
