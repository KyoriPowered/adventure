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
package net.kyori.adventure.text.format;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Index;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * An enumeration of decorations which may be applied to a {@link Component}.
 *
 * @since 4.0.0
 */
public enum TextDecoration implements StyleBuilderApplicable, TextFormat {
  /**
   * A decoration which makes text obfuscated/unreadable.
   *
   * @since 4.0.0
   */
  OBFUSCATED("obfuscated"),
  /**
   * A decoration which makes text appear bold.
   *
   * @since 4.0.0
   */
  BOLD("bold"),
  /**
   * A decoration which makes text have a strike through it.
   *
   * @since 4.0.0
   */
  STRIKETHROUGH("strikethrough"),
  /**
   * A decoration which makes text have an underline.
   *
   * @since 4.0.0
   */
  UNDERLINED("underlined"),
  /**
   * A decoration which makes text appear in italics.
   *
   * @since 4.0.0
   */
  ITALIC("italic");

  /**
   * The name map.
   *
   * @since 4.0.0
   */
  public static final Index<String, TextDecoration> NAMES = Index.create(TextDecoration.class, constant -> constant.name);
  private final String name;

  TextDecoration(final String name) {
    this.name = name;
  }

  /**
   * Creates a {@link TextDecorationAndState}, annotating this decoration with the given {@code state}.
   *
   * @param state the state
   * @return a {@link TextDecorationAndState}
   * @since 4.8.0
   * @deprecated for removal since 4.10.0, use {@link #withState(boolean)} instead
   */
  @Deprecated
  public final @NotNull TextDecorationAndState as(final boolean state) {
    return this.withState(state);
  }

  /**
   * Creates a {@link TextDecorationAndState}, annotating this decoration with the given {@code state}.
   *
   * @param state the state
   * @return a {@link TextDecorationAndState}
   * @since 4.8.0
   * @deprecated for removal since 4.10.0, use {@link #withState(State)} instead
   */
  @Deprecated
  public final @NotNull TextDecorationAndState as(final @NotNull State state) {
    return this.withState(state);
  }

  /**
   * An alias for {@link #as(boolean)}.
   *
   * @param state the state
   * @return a {@link TextDecorationAndState}
   * @since 4.10.0
   */
  public final @NotNull TextDecorationAndState withState(final boolean state) {
    return new TextDecorationAndStateImpl(this, State.byBoolean(state));
  }

  /**
   * An alias for {@link #as(State)}.
   *
   * @param state the state
   * @return a {@link TextDecorationAndState}
   * @since 4.10.0
   */
  public final @NotNull TextDecorationAndState withState(final @NotNull State state) {
    return new TextDecorationAndStateImpl(this, state);
  }

  /**
   * An alias for {@link #as(State)}.
   *
   * @param state the state
   * @return a {@link TextDecorationAndState}
   * @since 4.10.0
   */
  public final @NotNull TextDecorationAndState withState(final @NotNull TriState state) {
    return new TextDecorationAndStateImpl(this, State.byTriState(state));
  }

  @Override
  public void styleApply(final Style.@NotNull Builder style) {
    style.decorate(this);
  }

  @Override
  public @NotNull String toString() {
    return this.name;
  }

  /**
   * A state that a {@link TextDecoration} can be in.
   *
   * @since 4.0.0
   */
  public enum State {
    /**
     * State describing the absence of a value.
     *
     * @since 4.0.0
     */
    NOT_SET("not_set"),
    /**
     * State describing a {@code false} value.
     *
     * @since 4.0.0
     */
    FALSE("false"),
    /**
     * State describing a {@code true} value.
     *
     * @since 4.0.0
     */
    TRUE("true");

    private final String name;

    State(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }

    /**
     * Gets a state from a {@code boolean}.
     *
     * @param flag the boolean
     * @return the state
     * @since 4.0.0
     */
    public static @NotNull State byBoolean(final boolean flag) {
      return flag ? TRUE : FALSE;
    }

    /**
     * Gets a state from a {@code Boolean}.
     *
     * @param flag the boolean
     * @return the state
     * @since 4.0.0
     */
    public static @NotNull State byBoolean(final @Nullable Boolean flag) {
      return flag == null ? NOT_SET : byBoolean(flag.booleanValue());
    }

    /**
     * Gets a state from a {@link net.kyori.adventure.util.TriState}.
     *
     * @param flag the tristate
     * @return the state
     * @since 4.10.0
     */
    public static @NotNull State byTriState(final @NotNull TriState flag) {
      requireNonNull(flag);
      switch (flag) {
        case TRUE: return TRUE;
        case FALSE: return FALSE;
        case NOT_SET: return NOT_SET;
      }
      throw new IllegalArgumentException("Unable to turn TriState: " + flag + " into a TextDecoration.State");
    }
  }
}
