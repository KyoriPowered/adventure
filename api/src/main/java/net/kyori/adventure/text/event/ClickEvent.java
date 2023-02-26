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
package net.kyori.adventure.text.event;

import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.util.Index;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A click event.
 *
 * <p>A click event processes an {@link Action} when clicked on.</p>
 *
 * @since 4.0.0
 */
public final class ClickEvent implements Examinable, StyleBuilderApplicable {
  /**
   * Creates a click event that opens a url.
   *
   * @param url the url to open
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent openUrl(final @NotNull String url) {
    return new ClickEvent(Action.OPEN_URL, url);
  }

  /**
   * Creates a click event that opens a url.
   *
   * @param url the url to open
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent openUrl(final @NotNull URL url) {
    return openUrl(url.toExternalForm());
  }

  /**
   * Creates a click event that opens a file.
   *
   * <p>This action is not readable, and may only be used locally on the client.</p>
   *
   * @param file the file to open
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent openFile(final @NotNull String file) {
    return new ClickEvent(Action.OPEN_FILE, file);
  }

  /**
   * Creates a click event that runs a command.
   *
   * @param command the command to run
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent runCommand(final @NotNull String command) {
    return new ClickEvent(Action.RUN_COMMAND, command);
  }

  /**
   * Creates a click event that suggests a command.
   *
   * @param command the command to suggest
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent suggestCommand(final @NotNull String command) {
    return new ClickEvent(Action.SUGGEST_COMMAND, command);
  }

  /**
   * Creates a click event that changes to a page.
   *
   * @param page the page to change to
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent changePage(final @NotNull String page) {
    return new ClickEvent(Action.CHANGE_PAGE, page);
  }

  /**
   * Creates a click event that changes to a page.
   *
   * @param page the page to change to
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent changePage(final int page) {
    return changePage(String.valueOf(page));
  }

  /**
   * Creates a click event that copies text to the clipboard.
   *
   * @param text the text to copy to the clipboard
   * @return a click event
   * @since 4.0.0
   * @sinceMinecraft 1.15
   */
  public static @NotNull ClickEvent copyToClipboard(final @NotNull String text) {
    return new ClickEvent(Action.COPY_TO_CLIPBOARD, text);
  }

  /**
   * Create a click event that, when clicked, will schedule a callback function to be executed on the server.
   *
   * <p>By default, this will be a single-use function that expires after the value of {@link ClickCallback#DEFAULT_LIFETIME}.</p>
   *
   * @param function the function to execute
   * @return a callback click event
   * @since 4.13.0
   */
  public static @NotNull ClickEvent callback(final @NotNull ClickCallback<Audience> function) {
    return ClickCallbackInternals.PROVIDER.create(requireNonNull(function, "function"), ClickCallbackOptionsImpl.DEFAULT);
  }

  /**
   * Create a click event that, when clicked, will schedule a callback function to be executed on the server.
   *
   * @param function the function to execute
   * @param options options to control how the callback will be stored on the server.
   * @return a callback click event
   * @since 4.13.0
   */
  public static @NotNull ClickEvent callback(final @NotNull ClickCallback<Audience> function, final ClickCallback.@NotNull Options options) {
    return ClickCallbackInternals.PROVIDER.create(requireNonNull(function, "function"), requireNonNull(options, "options"));
  }

  /**
   * Create a click event that, when clicked, will schedule a callback function to be executed on the server.
   *
   * @param function the function to execute
   * @param optionsBuilder function that will be called to configure the click callback options
   * @return a callback click event
   * @since 4.13.0
   */
  public static @NotNull ClickEvent callback(final @NotNull ClickCallback<Audience> function, final @NotNull Consumer<ClickCallback.Options.@NotNull Builder> optionsBuilder) {
    return ClickCallbackInternals.PROVIDER.create(
      requireNonNull(function, "function"),
      AbstractBuilder.configureAndBuild(ClickCallback.Options.builder(), requireNonNull(optionsBuilder, "optionsBuilder"))
    );
  }

  /**
   * Creates a click event.
   *
   * @param action the action
   * @param value the value
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent clickEvent(final @NotNull Action action, final @NotNull String value) {
    return new ClickEvent(action, value);
  }

  private final Action action;
  private final String value;

  private ClickEvent(final @NotNull Action action, final @NotNull String value) {
    this.action = requireNonNull(action, "action");
    this.value = requireNonNull(value, "value");
  }

  /**
   * Gets the click event action.
   *
   * @return the click event action
   * @since 4.0.0
   */
  public @NotNull Action action() {
    return this.action;
  }

  /**
   * Gets the click event value.
   *
   * @return the click event value
   * @since 4.0.0
   */
  public @NotNull String value() {
    return this.value;
  }

  @Override
  public void styleApply(final Style.@NotNull Builder style) {
    style.clickEvent(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
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
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("action", this.action),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  /**
   * An enumeration of click event actions.
   *
   * @since 4.0.0
   */
  public enum Action {
    /**
     * Opens a url when clicked.
     *
     * @since 4.0.0
     */
    OPEN_URL("open_url", true),
    /**
     * Opens a file when clicked.
     *
     * <p>This action is not readable, and may only be used locally on the client.</p>
     *
     * @since 4.0.0
     */
    OPEN_FILE("open_file", false),
    /**
     * Runs a command when clicked.
     *
     * @since 4.0.0
     */
    RUN_COMMAND("run_command", true),
    /**
     * Suggests a command into the chat box.
     *
     * @since 4.0.0
     */
    SUGGEST_COMMAND("suggest_command", true),
    /**
     * Changes the page of a book.
     *
     * @since 4.0.0
     */
    CHANGE_PAGE("change_page", true),
    /**
     * Copies text to the clipboard.
     *
     * @since 4.0.0
     * @sinceMinecraft 1.15
     */
    COPY_TO_CLIPBOARD("copy_to_clipboard", true);

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    public static final Index<String, Action> NAMES = Index.create(Action.class, constant -> constant.name);
    private final String name;
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserialized.</p>
     */
    private final boolean readable;

    Action(final @NotNull String name, final boolean readable) {
      this.name = name;
      this.readable = readable;
    }

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     *     action is not readable
     * @since 4.0.0
     */
    public boolean readable() {
      return this.readable;
    }

    @Override
    public @NotNull String toString() {
      return this.name;
    }
  }
}
