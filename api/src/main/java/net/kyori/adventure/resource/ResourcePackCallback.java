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
package net.kyori.adventure.resource;

import java.util.UUID;
import java.util.function.BiConsumer;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

/**
 * A callback for a resource pack application operation.
 *
 * @since 4.15.0
 */
@FunctionalInterface
public interface ResourcePackCallback {
  /**
   * Create a pack callback that performs no operation.
   *
   * <p>Multiple calls to this method are guaranteed to return callback functions with equal identity.</p>
   *
   * @return the no-op callback
   * @since 4.15.0
   */
  static @NotNull ResourcePackCallback noOp() {
    return ResourcePackCallbacks.NO_OP;
  }

  /**
   * Create a pack callback that will only execute the provided functions when the pack application has completed, discarding all intermediate events.
   *
   * @param success the success callback
   * @param failure the failure callback
   * @return the created callback
   * @since 4.15.0
   */
  static @NotNull ResourcePackCallback onTerminal(final @NotNull BiConsumer<UUID, Audience> success, final @NotNull BiConsumer<UUID, Audience> failure) {
    return (uuid, status, audience) -> {
      if (status == ResourcePackStatus.SUCCESSFULLY_LOADED) {
        success.accept(uuid, audience);
      } else if (!status.intermediate()) {
        failure.accept(uuid, audience);
      }
    };
  }

  /**
   * Called when a pack event has been received.
   *
   * <p>If the pack apply action was executed on a group audience, {@code audience} will referer to the
   * individual member audiences the action is executed on. Forwarding audiences may wrap callbacks to ensure they receive the appropriate wrapped audience.</p>
   *
   * @param uuid the uuid of the pack that has been applied.
   * @param status the current pack status
   * @param audience the audience the pack is being applied to
   * @since 4.15.0
   */
  void packEventReceived(final @NotNull UUID uuid, final @NotNull ResourcePackStatus status, final @NotNull Audience audience);
}
