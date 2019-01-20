/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.event;

import net.kyori.text.util.NameMap;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * A click event.
 *
 * <p>A click event processes an {@link Action} when clicked on.</p>
 */
public final class ClickEvent {
  /**
   * The click event action.
   */
  private final Action action;
  /**
   * The click event value.
   */
  private final String value;

  public ClickEvent(final @NonNull Action action, final @NonNull String value) {
    this.action = action;
    this.value = value;
  }

  /**
   * Gets the click event action.
   *
   * @return the click event action
   */
  public @NonNull Action action() {
    return this.action;
  }

  /**
   * Gets the click event value.
   *
   * @return the click event value
   */
  public @NonNull String value() {
    return this.value;
  }

  /**
   * Creates a copy of this click event.
   *
   * @return a copy of this click event
   */
  public @NonNull ClickEvent copy() {
    return new ClickEvent(this.action, this.value);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final ClickEvent that = (ClickEvent) other;
    return this.action == that.action && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.action, this.value);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
      .add("action=" + this.action)
      .add("value=" + this.value)
      .toString();
  }

  /**
   * An enumeration of click event actions.
   */
  public enum Action {
    /**
     * Opens a url when clicked.
     */
    OPEN_URL("open_url", true),
    /**
     * Opens a file when clicked.
     *
     * <p>This action is not readable, and may only be used locally on the client.</p>
     */
    OPEN_FILE("open_file", false),
    /**
     * Runs a command when clicked.
     */
    RUN_COMMAND("run_command", true),
    /**
     * Suggests a command into the chat box.
     */
    SUGGEST_COMMAND("suggest_command", true),
    /**
     * Changes the page of a book.
     */
    CHANGE_PAGE("change_page", true);

    /**
     * The name map.
     */
    public static final NameMap<Action> NAMES = NameMap.create(values(), constant -> constant.name);
    /**
     * The name of this action.
     */
    private final String name;
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserailized.</p>
     */
    private final boolean readable;

    Action(final String name, final boolean readable) {
      this.name = name;
      this.readable = readable;
    }

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     *     action is not readable
     */
    public boolean readable() {
      return this.readable;
    }

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     *     action is not readable
     * @deprecated use {@link #readable()}
     */
    @Deprecated
    public boolean isReadable() {
      return this.readable;
    }

    @Override
    public @NonNull String toString() {
      return this.name;
    }
  }
}
