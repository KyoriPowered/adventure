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
package net.kyori.adventure.text;

import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record TextReplacementConfigImpl(
  Pattern matchPattern,
  BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement,
  Condition continuer,
  boolean replaceInsideHoverEvents
) implements TextReplacementConfig {

  @Override
  public Pattern matchPattern() {
    return this.matchPattern;
  }

  TextReplacementRenderer.State createState() {
    return new TextReplacementRenderer.State(this.matchPattern, this.replacement, this.continuer, this.replaceInsideHoverEvents);
  }

  static final class Builder implements TextReplacementConfig.Builder {
    @Nullable Pattern matchPattern;
    @Nullable BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement;
    Condition continuer = (matchResult, index, replacement) -> PatternReplacementResult.REPLACE;
    boolean replaceInsideHoverEvents = true;

    Builder() {
    }

    @Override
    public Builder match(final Pattern pattern) {
      this.matchPattern = requireNonNull(pattern, "pattern");
      return this;
    }

    @Override
    public Builder condition(final Condition condition) {
      this.continuer = requireNonNull(condition, "continuation");
      return this;
    }

    @Override
    public Builder replacement(final BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement) {
      this.replacement = requireNonNull(replacement, "replacement");
      return this;
    }

    @Override
    public TextReplacementConfig.Builder replaceInsideHoverEvents(final boolean replace) {
      this.replaceInsideHoverEvents = replace;
      return this;
    }

    @Override
    public TextReplacementConfig build() {
      if (this.matchPattern == null) throw new IllegalStateException("A pattern must be provided to match against");
      if (this.replacement == null) throw new IllegalStateException("A replacement action must be provided");
      return new TextReplacementConfigImpl(this.matchPattern, this.replacement, this.continuer, this.replaceInsideHoverEvents);
    }
  }
}
