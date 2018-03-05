/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2018 KyoriPowered
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

import com.google.common.base.Enums;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import net.kyori.blizzard.Immutable;
import net.kyori.blizzard.NonNull;

/**
 * A click event.
 *
 * <p>A click event processes an {@link Action} when clicked on.</p>
 */
@Immutable
public final class ClickEvent {
  /**
   * The click event action.
   */
  @NonNull private final Action action;
  /**
   * The click event value.
   */
  @NonNull private final String value;

  public ClickEvent(@NonNull final Action action, @NonNull final String value) {
    this.action = action;
    this.value = value;
  }

  /**
   * Gets the click event action.
   *
   * @return the click event action
   */
  @NonNull
  public Action action() {
    return this.action;
  }

  /**
   * Gets the click event value.
   *
   * @return the click event value
   */
  @NonNull
  public String value() {
    return this.value;
  }

  /**
   * Creates a copy of this click event.
   *
   * @return a copy of this click event
   */
  @NonNull
  public ClickEvent copy() {
    return new ClickEvent(this.action, this.value);
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final ClickEvent that = (ClickEvent) other;
    return this.action == that.action && Objects.equal(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.action, this.value);
  }

  @NonNull
  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("action", this.action)
      .add("value", this.value)
      .toString();
  }

  /**
   * An enumeration of click event actions.
   */
  public enum Action {
    /**
     * Opens a url when clicked.
     */
    @SerializedName("open_url")
    OPEN_URL(true),
    /**
     * Opens a file when clicked.
     *
     * <p>This action is not readable, and may only be used locally on the client.</p>
     */
    @SerializedName("open_file")
    OPEN_FILE(false),
    /**
     * Runs a command when clicked.
     */
    @SerializedName("run_command")
    RUN_COMMAND(true),
    /**
     * Suggests a command into the chat box.
     */
    @SerializedName("suggest_command")
    SUGGEST_COMMAND(true),
    /**
     * Changes the page of a book.
     */
    @SerializedName("change_page")
    CHANGE_PAGE(true);

    /**
     * The serialized name of this action.
     */
    @NonNull private final String toString = Enums.getField(this).getAnnotation(SerializedName.class).value();
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserailized.</p>
     */
    private final boolean readable;

    Action(final boolean readable) {
      this.readable = readable;
    }

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     *     action is not readable
     */
    public boolean isReadable() {
      return this.readable;
    }

    @NonNull
    @Override
    public String toString() {
      return this.toString;
    }
  }
}
