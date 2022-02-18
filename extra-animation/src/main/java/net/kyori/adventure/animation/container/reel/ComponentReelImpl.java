/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.animation.container.reel;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

class ComponentReelImpl extends AbstractGenericFrameReel<Component, ComponentReel, ComponentReelBuilder> implements ComponentReel {

  ComponentReelImpl(final List<Component> frames) {
    super(frames, ComponentReelImpl::new);
  }

  @Override
  public @NotNull ComponentReelBuilder toBuilder() {
    return new BuilderImpl(frames());
  }

  @Override
  public ComponentReel append(final ComponentLike componentLike) {
    return this.append(componentLike.asComponent());
  }

  @Override
  public ComponentReel appendEmpties(final int number) {
    final List<Component> newFrames = new ArrayList<>(this.frames());

    for (int i = 0; i < number; i++) newFrames.add(Component.empty());
    return new ComponentReelImpl(newFrames);
  }

  static class BuilderImpl extends AbstractGenericFrameReelBuilder<Component, ComponentReel, ComponentReelBuilder> implements ComponentReelBuilder {

    BuilderImpl(final List<Component> frames) {
      super(frames, ComponentReelImpl::new);
    }

    @Override
    protected ComponentReelBuilder instance() {
      return this;
    }

    @Override
    public ComponentReelBuilder append(final ComponentLike componentLike) {
      return this.append(componentLike.asComponent());
    }

    @Override
    public ComponentReelBuilder appendEmpties(final int number) {
      for (int i = 0; i < number; i++) framesMutable().add(Component.empty());
      return this;
    }
  }

}
