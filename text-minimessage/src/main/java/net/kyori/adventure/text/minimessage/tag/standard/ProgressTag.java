/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Progress tag.
 *
 * @since 4.13.0
 */
@ApiStatus.Internal
public final class ProgressTag implements Inserting, Examinable {
  private static final String PROGRESS = "progress";
  private static final int DEFAULT_LENGTH = 10;
  private static final NamedTextColor DEFAULT_FILLED_COLOR = NamedTextColor.GREEN;
  private static final NamedTextColor DEFAULT_REMAINING_COLOR = NamedTextColor.RED;
  static final TagResolver RESOLVER = TagResolver.resolver(PROGRESS, ProgressTag::create);
  private final String text;
  private final int totalLength;
  private final TextColor fillColor;
  private final TextColor remainingColor;

  private final Supplier<Double> progressSupplier;

  private ProgressTag(final String text, final int totalLength, final TextColor fillColor, final TextColor remainingColor, final Supplier<Double> progressSupplier) {
    this.text = text;
    this.totalLength = totalLength;
    this.fillColor = fillColor;
    this.remainingColor = remainingColor;
    this.progressSupplier = progressSupplier;
  }

  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {

    final OptionalDouble optionalProgress = args.popOr("Invalid progress! Progress must be a double in the range [0d, 1.0d] (inclusive).").asDouble();
    if (!optionalProgress.isPresent()) {
      throw ctx.newException("Progress must be a double in the range [0d, 1.0d] (inclusive).");
    }
    final double progress = optionalProgress.getAsDouble();
    if (progress < 0 || progress > 1) {
      throw ctx.newException("Progress is out of range (%s). Must be in the range [0d, 1.0d] (inclusive).");
    }
    final Supplier<Double> progressSupplier = () -> progress;

    return createProgressProvided(args, ctx, progressSupplier);
  }

  /**
   * Creates a progress tag that uses progress from the supplier to calculate the progress when the value is requested.
   *
   * @param args the arguments
   * @param ctx the context
   * @param progressSupplier the progress supplier
   *
   * @return progress tag
   * @throws ParsingException if length is not an integer greater than or equal to 1 or if the colors are invalid
   * @since 4.13.0
   */
  @ApiStatus.Internal
  public static Tag createProgressProvided(final ArgumentQueue args, final Context ctx, final Supplier<Double> progressSupplier) throws ParsingException {
    final String text = args.popOr("No text found to use for progress bar.").value();

    final int length;
    if (args.hasNext()) {
      final OptionalInt optionalLength = args.pop().asInt();
      if (!optionalLength.isPresent()) {
        throw ctx.newException("Length must be an integer.");
      }
      length = optionalLength.getAsInt();
      if (length < 1) {
        throw ctx.newException("Progress bar length must be at least 1.");
      }
    } else {
      length = DEFAULT_LENGTH;
    }

    final TextColor fillColor;
    if (args.hasNext()) {
      fillColor = ColorTagResolver.resolveColor(args.pop().value(), ctx);
    } else {
      fillColor = DEFAULT_FILLED_COLOR;
    }

    final TextColor remainingColor;
    if (args.hasNext()) {
      remainingColor = ColorTagResolver.resolveColor(args.pop().value(), ctx);
    } else {
      remainingColor = DEFAULT_REMAINING_COLOR;
    }
    return new ProgressTag(text, length, fillColor, remainingColor, progressSupplier);
  }

  @Override
  public @NotNull Component value() {
    final Component filledComponent = Component.text(this.text, this.fillColor);
    final Component remainingComponent = Component.text(this.text, this.remainingColor);
    final TextComponent.Builder barBuilder = Component.text();
    final double progress = this.progressSupplier.get();
    if (progress < 0 || progress > 1) {
      throw new IllegalStateException("Progress must be a double in the range [0d, 1.0d] (inclusive).");
    }
    final int fillLength = (int) (this.totalLength * progress);
    for (int i = 0; i < fillLength; i++) {
      barBuilder.append(filledComponent);
    }
    for (int i = 0; i < this.totalLength - fillLength; i++) {
      barBuilder.append(remainingComponent);
    }
    return barBuilder.build();
  }

  @Override
  public boolean allowsChildren() {
    return false;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("text", this.text),
      ExaminableProperty.of("totalLength", this.totalLength),
      ExaminableProperty.of("fillColor", this.fillColor),
      ExaminableProperty.of("remainingColor", this.remainingColor),
      ExaminableProperty.of("progressSupplier", this.progressSupplier)
    );
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    final ProgressTag that = (ProgressTag) other;
    return this.text.equals(that.text)
      && this.totalLength == that.totalLength
      && this.fillColor.equals(that.fillColor)
      && this.remainingColor.equals(that.remainingColor)
      && this.progressSupplier.equals(that.progressSupplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.text, this.totalLength, this.fillColor, this.remainingColor, this.progressSupplier);
  }
}
