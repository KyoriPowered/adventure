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
package net.kyori.adventure.text.event;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.util.Index;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A click event.
 *
 * <p>A click event processes an {@link Action} when clicked on.</p>
 */
public final class ClickEvent implements Examinable, StyleBuilderApplicable {
  /**
   * Creates a click event.
   *
   * @param action the action
   * @param value the value
   * @return a click event
   */
  public static @NonNull ClickEvent of(final @NonNull Action action, final @NonNull String value) {
    return new ClickEvent(action, value);
  }

  /**
   * Creates a click event that opens a url.
   *
   * @param url the url to open
   * @return a click event
   */
  public static @NonNull ClickEvent openUrl(final @NonNull String url) {
    return of(Action.OPEN_URL, url);
  }

  /**
   * Creates a click event that opens a file.
   *
   * <p>This action is not readable, and may only be used locally on the client.</p>
   *
   * @param file the file to open
   * @return a click event
   */
  public static @NonNull ClickEvent openFile(final @NonNull String file) {
    return of(Action.OPEN_FILE, file);
  }

  /**
   * Creates a click event that runs a command.
   *
   * @param command the command to run
   * @return a click event
   */
  public static @NonNull ClickEvent runCommand(final @NonNull String command) {
    return of(Action.RUN_COMMAND, command);
  }

  /**
   * Creates a click event that suggests a command.
   *
   * @param command the command to suggest
   * @return a click event
   */
  public static @NonNull ClickEvent suggestCommand(final @NonNull String command) {
    return of(Action.SUGGEST_COMMAND, command);
  }

  /**
   * Creates a click event that changes to a page.
   *
   * @param page the page to change to
   * @return a click event
   */
  public static @NonNull ClickEvent changePage(final @NonNull String page) {
    return of(Action.CHANGE_PAGE, page);
  }

  /**
   * Creates a click event that changes to a page.
   *
   * @param page the page to change to
   * @return a click event
   */
  public static @NonNull ClickEvent changePage(final int page) {
    return changePage(String.valueOf(page));
  }

  /**
   * Creates a click event that copies text to the clipboard.
   *
   * @param text the text to copy to the clipboard
   * @return a click event
   */
  public static @NonNull ClickEvent copyToClipboard(final @NonNull String text) {
    return of(Action.COPY_TO_CLIPBOARD, text);
  }

  /**
   * The click event action.
   */
  private final Action action;
  /**
   * The click event value.
   */
  private final String value;

  private ClickEvent(final @NonNull Action action, final @NonNull String value) {
    this.action = requireNonNull(action, "action");
    this.value = requireNonNull(value, "value");
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

  @Override
  public void styleApply(final Style.@NonNull Builder style) {
    style.clickEvent(this);
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
    int result = this.action.hashCode();
    result = (31 * result) + this.value.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("action", this.action),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public String toString() {
    return StringExaminer.simpleEscaping().examine(this);
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
    CHANGE_PAGE("change_page", true),
    /**
     * Copies text to the clipboard.
     */
    COPY_TO_CLIPBOARD("copy_to_clipboard", true);

    /**
     * The name map.
     */
    public static final Index<String, Action> NAMES = Index.create(Action.class, constant -> constant.name);
    /**
     * The name of this action.
     */
    private final String name;
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserialized.</p>
     */
    private final boolean readable;

    Action(final @NonNull String name, final boolean readable) {
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

    @Override
    public @NonNull String toString() {
      return this.name;
    }
  }
}
