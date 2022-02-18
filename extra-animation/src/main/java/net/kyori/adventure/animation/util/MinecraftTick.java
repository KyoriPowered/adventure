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
package net.kyori.adventure.animation.util;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

/**
 * Minecraft Tick time unit.
 *
 * @since 1.10.0
 */
public final class MinecraftTick implements TemporalUnit {

  /**
   * Minecraft tick unit. (1 MT = 1/20s)
   *
   * @since 1.10.0
   */
  public static final TemporalUnit MINECRAFT_TICK = new MinecraftTick();

  private MinecraftTick() {

  }

  @Override
  public Duration getDuration() {
    return Duration.ofMillis(50);
  }

  @Override
  public boolean isDurationEstimated() {
    return false;
  }

  @Override
  public boolean isDateBased() {
    return false;
  }

  @Override
  public boolean isTimeBased() {
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <R extends Temporal> R addTo(final R temporal, final long amount) {
    return (R) temporal.plus(amount, this);
  }

  @Override
  public long between(final Temporal temporal1Inclusive, final Temporal temporal2Exclusive) {
    return temporal1Inclusive.until(temporal2Exclusive, this);
  }

  @Override
  public String toString() {
    return "Ticks";
  }

}
