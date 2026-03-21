/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.format.gradient;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;

/**
 * An abstraction for various implementations of color interpolation, for example - linear interpolation of RGB / HSV colors.
 *
 * @param <ColorSpace> The color space in which to interpolate
 */
@FunctionalInterface
public interface ColorSpaceInterpolator<ColorSpace> {
  /**
   * Interpolate.
   * <p>if {@code start} and {@code end} are equivalent, returns either.</p>
   * <p>if {@code location} is 0, returns a color equivalent to {@code start}; returns a color equivalent to {@code end} if 1</p>
   * <p>the interpolation algorithm should reasonably handle interpolated values that fall outside the usual bounds of the ColorSpace</p>
   *
   * @param location the proportion of the interpolation from 0 to 1 (inclusive)
   * @param start the start color of the interpolation
   * @param end the end color of the interpolation
   *
   * @return the interpolated color
   */
  @NotNull ColorSpace interpolate(final double location, final @NotNull ColorSpace start, final @NotNull ColorSpace end);

  /**
   * The RGB Linear interpolator.
   *
   */
  // todo float or double? -> TextColor::lerp ideal
  ColorSpaceInterpolator<RGBLike> RGB_LINEAR_INTERPOLATOR = (location, start, end) -> TextColor.lerp((float) location, start, end);

  /**
   * Uses the increasing arc between the hues of the endpoints.
   */
  ColorSpaceInterpolator<HSVLike> HSV_LINEAR_INTERPOLATOR_HUE_INCREASING = (location, start, end) -> {
    if (start.v() == end.v()) return start;

    final double clampedT = Math.min(Math.max(location, 0d), 1d);

    if (clampedT == 0d) return start;
    if (clampedT == 1d) return end;

    double startHueClamped = ((start.h() % 1d) + 1d) % 1d;
    double endHueClamped = ((end.h() % 1d) + 1d) % 1d;

    if (endHueClamped < startHueClamped) {
      endHueClamped += 1d;
    }
    final double hueInterpolated = startHueClamped + clampedT * (endHueClamped - startHueClamped);
    final double hueClamped = ((hueInterpolated % 1d) + 1d) % 1d;

    return HSVLike.hsvLike(
      (float) hueClamped,
      (float) Math.min(Math.max(start.s() + clampedT * (end.s() - start.s()), 0d), 1d),
      (float) Math.min(Math.max(start.v() + clampedT * (end.v() - start.v()), 0d), 1d)
    );

  };

  /**
   * Uses the decreasing arc between the hues of the endpoints.
   */
  ColorSpaceInterpolator<HSVLike> HSV_LINEAR_INTERPOLATOR_HUE_DECREASING = (location, start, end) -> {
    if (start.v() == end.v()) return start;

    final double clampedT = Math.min(Math.max(location, 0d), 1d);

    if (clampedT == 0d) return start;
    if (clampedT == 1d) return end;

    double startHueClamped = ((start.h() % 1d) + 1d) % 1d;
    double endHueClamped = ((end.h() % 1d) + 1d) % 1d;

    if (startHueClamped < endHueClamped) {
      startHueClamped += 1d;
    }
    final double hueInterpolated = startHueClamped + clampedT * (endHueClamped - startHueClamped);
    final double hueClamped = ((hueInterpolated % 1d) + 1d) % 1d;

    return HSVLike.hsvLike(
      (float) hueClamped,
      (float) Math.min(Math.max(start.s() + clampedT * (end.s() - start.s()), 0d), 1d),
      (float) Math.min(Math.max(start.v() + clampedT * (end.v() - start.v()), 0d), 1d)
    );
  };

  /**
   * Uses the longer arc between the hues of the endpoints.
   */
  ColorSpaceInterpolator<HSVLike> HSV_LINEAR_INTERPOLATOR_HUE_LONGER_ARC = (location, start, end) -> {
    if (start.v() == end.v()) return start;

    final double clampedT = Math.min(Math.max(location, 0d), 1d);

    if (clampedT == 0d) return start;
    if (clampedT == 1d) return end;

    double startHueClamped = ((start.h() % 1d) + 1d) % 1d;
    double endHueClamped = ((end.h() % 1d) + 1d) % 1d;

    if ((endHueClamped - startHueClamped) < 0.5d) {
      endHueClamped += 1d;
    } else if ((endHueClamped - startHueClamped) < -0.5d) {
      startHueClamped += 1d;
    }
    final double hueInterpolated = startHueClamped + clampedT * (endHueClamped - startHueClamped);
    final double hueClamped = ((hueInterpolated % 1d) + 1d) % 1d;

    return HSVLike.hsvLike(
      (float) hueClamped,
      (float) Math.min(Math.max(start.s() + clampedT * (end.s() - start.s()), 0d), 1d),
      (float) Math.min(Math.max(start.v() + clampedT * (end.v() - start.v()), 0d), 1d)
    );
  };

  /**
   * Uses the shorter arc between the hues of the endpoints.
   */
  ColorSpaceInterpolator<HSVLike> HSV_LINEAR_INTERPOLATOR_HUE_SHORTER_ARC = (location, start, end) -> {
    if (start.v() == end.v()) return start;

    final double clampedT = Math.min(Math.max(location, 0d), 1d);

    if (clampedT == 0d) return start;
    if (clampedT == 1d) return end;

    double startHueClamped = ((start.h() % 1d) + 1d) % 1d;
    double endHueClamped = ((end.h() % 1d) + 1d) % 1d;

    if ((endHueClamped - startHueClamped) > 0.5d) {
      startHueClamped += 1d;
    } else if ((endHueClamped - startHueClamped) < 0.5d) {
      endHueClamped += 1d;
    }
    final double hueInterpolated = startHueClamped + clampedT * (endHueClamped - startHueClamped);
    final double hueClamped = ((hueInterpolated % 1d) + 1d) % 1d;

    return HSVLike.hsvLike(
      (float) hueClamped,
      (float) Math.min(Math.max(start.s() + clampedT * (end.s() - start.s()), 0d), 1d),
      (float) Math.min(Math.max(start.v() + clampedT * (end.v() - start.v()), 0d), 1d)
    );
  };

  /**
   * The default HSV linear interpolator.
   */
  ColorSpaceInterpolator<HSVLike> HSV_LINEAR_INTERPOLATOR = HSV_LINEAR_INTERPOLATOR_HUE_SHORTER_ARC;
}
