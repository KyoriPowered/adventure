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
package net.kyori.adventure.audience;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

/**
 * An audience that batches operations until explicitly flushed.
 */
public interface BatchAudience extends Audience {

    /**
     * A future operation on an audience.
     */
    @FunctionalInterface
    interface Operation {
        /**
         * Processes the operation on an audience.
         *
         * @param audience an audience
         */
        void process(final @NonNull Audience audience);
    }

    /**
     * Queue an operation to be run on the audience later.
     *
     * @param operation an operation.
     */
    void queue(final @NonNull Operation operation);

    /**
     * Process all queued operations on the audience.
     *
     * @return the number of operations processed.
     */
    int flush();

    /**
     * Sends a message.
     */
    class SendMessageOperation implements Operation {
        protected final Component message;

        protected SendMessageOperation(final @NonNull Component message) {
            this.message = requireNonNull(message, "message");
        }

        @Override
        public void process(final @NonNull Audience audience) {
            audience.sendMessage(this.message);
        }
    }

    @Override
    default void sendMessage(@NonNull Component message) {
        this.queue(new SendMessageOperation(message));
    }

    /**
     * Sends an action bar.
     */
    class SendActionBarOperation implements Operation {
        protected final Component message;

        protected SendActionBarOperation(final @NonNull Component message) {
            this.message = requireNonNull(message, "message");
        }

        @Override
        public void process(final @NonNull Audience audience) {
            audience.sendActionBar(this.message);
        }
    }

    @Override
    default void sendActionBar(@NonNull Component message) {
        this.queue(new SendActionBarOperation(message));
    }

    /**
     * Shows a title.
     */
    class ShowTitleOperation implements Operation {
        protected final Title title;

        protected ShowTitleOperation(final @NonNull Title title) {
            this.title = requireNonNull(title, "title");
        }

        @Override
        public void process(final @NonNull Audience audience) {
            audience.showTitle(this.title);
        }
    }

    @Override
    default void showTitle(@NonNull Title title) {
        this.queue(new ShowTitleOperation(title));
    }

    /**
     * Clears a title.
     */
    class ClearTitleOperation implements Operation {
        @Override
        public void process(final @NonNull Audience audience) {
            audience.clearTitle();
        }
    }

    @Override
    default void clearTitle() {
        this.queue(new ClearTitleOperation());
    }

    /**
     * Resets a title.
     */
    class ResetTitleOperation implements Operation {
        @Override
        public void process(final @NonNull Audience audience) {
            audience.resetTitle();
        }
    }

    @Override
    default void resetTitle() {
        this.queue(new ResetTitleOperation());
    }

    /**
     * Shows a boss bar.
     */
    class ShowBossBarOperation implements Operation {
        protected final BossBar bar;

        protected ShowBossBarOperation(final @NonNull BossBar bar) {
            this.bar = requireNonNull(bar, "bar");
        }

        @Override
        public void process(final @NonNull Audience audience) {
            audience.showBossBar(this.bar);
        }
    }

    @Override
    default void showBossBar(@NonNull BossBar bar) {
        this.queue(new ShowBossBarOperation(bar));
    }

    /**
     * Hides a boss bar.
     */
    class HideBossBarOperation implements Operation {
        protected final BossBar bar;

        protected HideBossBarOperation(final @NonNull BossBar bar) {
            this.bar = requireNonNull(bar, "bar");
        }

        @Override
        public void process(final @NonNull Audience audience) {
            audience.hideBossBar(this.bar);
        }
    }

    @Override
    default void hideBossBar(@NonNull BossBar bar) {
        this.queue(new HideBossBarOperation(bar));
    }

    /**
     * Plays a sound.
     */
    class PlaySoundOperation implements Operation {
        protected final Sound sound;

        protected PlaySoundOperation(final @NonNull Sound sound) {
            this.sound = requireNonNull(sound, "sound");
        }

        @Override
        public void process(final @NonNull Audience audience) {
            audience.playSound(this.sound);
        }
    }

    @Override
    default void playSound(@NonNull Sound sound) {
        this.queue(new PlaySoundOperation(sound));
    }

    /**
     * Plays a sound at a location.
     */
    class PlaySoundAtOperation extends PlaySoundOperation {
        protected final double x;
        protected final double y;
        protected final double z;

        protected PlaySoundAtOperation(final @NonNull Sound sound, final double x, final double y, final double z) {
            super(sound);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void process(final @NonNull Audience audience) {
            audience.playSound(this.sound, this.x, this.y, this.z);
        }
    }

    @Override
    default void playSound(@NonNull Sound sound, double x, double y, double z) {
        this.queue(new PlaySoundAtOperation(sound, x, y, z));
    }

    /**
     * Stops a sound.
     */
    class StopSoundOperation implements Operation {
        protected final SoundStop stop;

        protected StopSoundOperation(final @NonNull SoundStop stop) {
            this.stop = requireNonNull(stop, "stop sound");
        }

        @Override
        public void process(final @NonNull Audience audience) {
            audience.stopSound(this.stop);
        }
    }

    @Override
    default void stopSound(@NonNull SoundStop stop) {
        this.queue(new StopSoundOperation(stop));
    }
}
