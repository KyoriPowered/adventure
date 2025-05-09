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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.util.Services;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;

final class ClickCallbackInternals {
  private ClickCallbackInternals() {
  }

  static final PermissionChecker ALWAYS_FALSE = PermissionChecker.always(TriState.FALSE);

  static final ClickCallback.Provider PROVIDER = Services.service(ClickCallback.Provider.class)
    .orElseGet(Fallback::new);

  static final class Fallback implements ClickCallback.Provider {
    @Override
    public @NotNull ClickEvent create(final @NotNull ClickCallback<Audience> callback, final ClickCallback.@NotNull Options options) {
      return ClickEvent.suggestCommand("Callbacks are not supported on this platform!");
    }
  }
}
