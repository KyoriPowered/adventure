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
package net.kyori.adventure.text.event;

import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
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
    return new ClickEvent(Action.OPEN_URL, Payload.string(url));
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
    return new ClickEvent(Action.OPEN_FILE, Payload.string(file));
  }

  /**
   * Creates a click event that runs a command.
   *
   * @param command the command to run
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent runCommand(final @NotNull String command) {
    return new ClickEvent(Action.RUN_COMMAND, Payload.string(command));
  }

  /**
   * Creates a click event that suggests a command.
   *
   * @param command the command to suggest
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent suggestCommand(final @NotNull String command) {
    return new ClickEvent(Action.SUGGEST_COMMAND, Payload.string(command));
  }

  /**
   * Creates a click event that changes to a page.
   *
   * @param page the page to change to
   * @return a click event
   * @throws IllegalArgumentException if the page cannot be represented as an integer using
   * @since 4.0.0
   * @deprecated For removal since 4.22.0, pages are integers, use {@link #changePage(int)}
   */
  @Deprecated
  public static @NotNull ClickEvent changePage(final @NotNull String page) {
    requireNonNull(page, "page");
    return new ClickEvent(Action.CHANGE_PAGE, Payload.integer(Integer.parseInt(page)));
  }

  /**
   * Creates a click event that changes to a page.
   *
   * @param page the page to change to
   * @return a click event
   * @since 4.0.0
   */
  public static @NotNull ClickEvent changePage(final int page) {
    return new ClickEvent(Action.CHANGE_PAGE, Payload.integer(page));
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
    return new ClickEvent(Action.COPY_TO_CLIPBOARD, Payload.string(text));
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
   * Creates a click event that shows a dialog.
   *
   * @param dialog the dialog
   * @return the click event
   * @since 4.22.0
   */
  public static @NotNull ClickEvent showDialog(final @NotNull DialogLike dialog) {
    requireNonNull(dialog, "dialog");
    return new ClickEvent(Action.SHOW_DIALOG, Payload.dialog(dialog));
  }

  /**
   * Creates a click event sends a custom event to the server.
   *
   * @param key the key
   * @param data the data
   * @return the click event
   * @since 4.22.0
   */
  public static @NotNull ClickEvent custom(final @NotNull Key key, final @NotNull String data) {
    requireNonNull(key, "key");
    requireNonNull(data, "data");
    return new ClickEvent(Action.CUSTOM, Payload.custom(key, data));
  }

  /**
   * Creates a click event with a {@link Payload.Text string payload}.
   *
   * @param action the action
   * @param value the value
   * @return a click event
   * @throws IllegalArgumentException if the action does not support a string payload
   * @since 4.0.0
   * @deprecated For removal since 4.22.0, not all actions support string payloads
   */
  @Deprecated
  public static @NotNull ClickEvent clickEvent(final @NotNull Action action, final @NotNull String value) {
    // A special case here to ensure that page can still accept a string.
    if (action == Action.CHANGE_PAGE) return changePage(value);
    if (!action.payloadType().equals(Payload.Text.class)) throw new IllegalArgumentException("Action " + action + " does not support string payloads");
    return new ClickEvent(action, Payload.string(value));
  }

  private final Action action;
  private final Payload payload;

  private ClickEvent(final @NotNull Action action, final @NotNull Payload payload) {
    if (!action.supports(payload)) throw new IllegalArgumentException("Action " + action + " does not support payload " + payload);
    this.action = requireNonNull(action, "action");
    this.payload = requireNonNull(payload, "payload");
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
   * Gets the click event value if the payload is a {@link Payload.Text string payload}.
   *
   * @return the click event value
   * @throws IllegalStateException if the payload is not a string payload
   * @since 4.0.0
   * @deprecated For removal since 4.22.0, click events can hold more than just strings, see {@link #payload()}
   */
  @Deprecated
  public @NotNull String value() {
    if (this.payload instanceof Payload.Text) {
      return ((Payload.Text) this.payload).value();
    } else if (this.action == Action.CHANGE_PAGE) { // Special case for page.
      return String.valueOf(((Payload.Int) this.payload).integer());
    } else {
      throw new IllegalStateException("Payload is not a string payload, is " + this.payload);
    }
  }

  /**
   * Gets the payload associated with this click event.
   *
   * @return the payload
   * @since 4.22.0
   */
  public @NotNull Payload payload() {
    return this.payload;
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
    return this.action == that.action && Objects.equals(this.payload, that.payload);
  }

  @Override
  public int hashCode() {
    int result = this.action.hashCode();
    result = (31 * result) + this.payload.hashCode();
    return result;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("action", this.action),
      ExaminableProperty.of("payload", this.payload)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  /**
   * A payload for a click event.
   *
   * @since 4.22.0
   */
  public /* sealed */ interface Payload /* permits String, Dialog, Custom */ extends Examinable {
    /**
     * Creates a text payload.
     *
     * @param value the payload value
     * @return the payload
     * @since 4.22.0
     */
    static ClickEvent.Payload.@NotNull Text string(final @NotNull String value) {
      requireNonNull(value, "value");
      return new PayloadImpl.TextImpl(value);
    }

    /**
     * Creates an integer payload.
     *
     * @param integer the integer
     * @return the payload
     * @since 4.22.0
     */
    static ClickEvent.Payload.@NotNull Int integer(final int integer) {
      return new PayloadImpl.IntImpl(integer);
    }

    /**
     * Creates a dialog payload.
     *
     * @param dialog the payload value
     * @return the payload
     * @since 4.22.0
     */
    static Payload.@NotNull Dialog dialog(final @NotNull DialogLike dialog) {
      requireNonNull(dialog, "dialog");
      return new PayloadImpl.DialogImpl(dialog);
    }

    /**
     * Creates a custom payload.
     *
     * @param key the key identifying the payload
     * @param data the payload data
     * @return the payload
     * @since 4.22.0
     */
    static Payload.@NotNull Custom custom(final @NotNull Key key, final @NotNull String data) {
      requireNonNull(key, "key");
      requireNonNull(data, "data");
      return new PayloadImpl.CustomImpl(key, data);
    }

    /**
     * A payload that holds a string.
     *
     * @since 4.22.0
     */
    interface Text extends Payload {
      /**
       * The string value for this payload.
       *
       * @return the string
       * @since 4.22.0
       */
      @NotNull String value();
    }

    /**
     * A payload that holds an integer.
     *
     * @since 4.22.0
     */
    interface Int extends Payload {
      /**
       * The integer value for this payload.
       *
       * @return the integer
       * @since 4.22.0
       */
      int integer();
    }

    /**
     * A payload that holds a dialog.
     *
     * @see Action#SHOW_DIALOG
     * @since 4.22.0
     */
    interface Dialog extends Payload {
      /**
       * The dialog.
       *
       * @return the dialog
       * @since 4.22.0
       */
      @NotNull DialogLike dialog();
    }

    /**
     * A payload that holds custom data.
     *
     * @see Action#CUSTOM
     * @since 4.22.0
     */
    interface Custom extends Payload, Keyed {
      /**
       * The custom data.
       *
       * @return the data
       * @since 4.22.0
       */
      @NotNull String data();
    }
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
    OPEN_URL("open_url", true, Payload.Text.class),
    /**
     * Opens a file when clicked.
     *
     * <p>This action is not readable, and may only be used locally on the client.</p>
     *
     * @since 4.0.0
     */
    OPEN_FILE("open_file", false, Payload.Text.class),
    /**
     * Runs a command when clicked.
     *
     * @since 4.0.0
     */
    RUN_COMMAND("run_command", true, Payload.Text.class),
    /**
     * Suggests a command into the chat box.
     *
     * @since 4.0.0
     */
    SUGGEST_COMMAND("suggest_command", true, Payload.Text.class),
    /**
     * Changes the page of a book.
     *
     * @since 4.0.0
     */
    CHANGE_PAGE("change_page", true, Payload.Int.class),
    /**
     * Copies text to the clipboard.
     *
     * @since 4.0.0
     * @sinceMinecraft 1.15
     */
    COPY_TO_CLIPBOARD("copy_to_clipboard", true, Payload.Text.class),
    /**
     * Shows a dialog.
     *
     * <p>This action is not readable at this time until Adventure has a full Dialog API.</p>
     *
     * @since 4.22.0
     * @sinceMinecraft 1.21.6
     */
    SHOW_DIALOG("show_dialog", false, Payload.Dialog.class),
    /**
     * Sends a custom event to the server.
     *
     * @since 4.22.0
     * @sinceMinecraft 1.21.6
     */
    CUSTOM("custom", true, Payload.Custom.class);

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
    private final Class<? extends Payload> payloadType;

    Action(final @NotNull String name, final boolean readable, final @NotNull Class<? extends Payload> payloadType) {
      this.name = name;
      this.readable = readable;
      this.payloadType = payloadType;
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

    /**
     * Returns if this action supports the provided payload.
     *
     * @param payload the payload
     * @return {@code true} if this action supports the payload
     * @since 4.22.0
     */
    public boolean supports(final @NotNull Payload payload) {
      requireNonNull(payload, "payload");
      return this.payloadType.isAssignableFrom(payload.getClass());
    }

    /**
     * The type of the payload this click event supports.
     *
     * @return the payload type
     * @since 4.22.0
     */
    public @NotNull Class<? extends Payload> payloadType() {
      return this.payloadType;
    }

    @Override
    public @NotNull String toString() {
      return this.name;
    }
  }
}
