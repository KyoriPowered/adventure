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
import java.util.function.Consumer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.util.Index;

import static java.util.Objects.requireNonNull;

/**
 * A click event.
 *
 * <p>A click event processes an {@link Action} when clicked on.</p>
 *
 * @param <T> the payload type
 * @since 4.0.0
 */
public sealed interface ClickEvent<T extends ClickEvent.Payload> extends StyleBuilderApplicable permits ClickEventImpl {
  /**
   * Creates a click event that opens a url.
   *
   * <p>Since <em>Minecraft: Java Edition</em> 1.21.5 the url will fail to parse if not a {@code http://} or {@code https://} scheme.</p>
   *
   * @param url the url to open
   * @return a click event
   * @since 4.0.0
   */
  static ClickEvent<Payload.Text> openUrl(final String url) {
    return ClickEventImpl.create(Action.OPEN_URL, Payload.string(url));
  }

  /**
   * Creates a click event that opens a url.
   *
   * @param url the url to open
   * @return a click event
   * @since 4.0.0
   */
  static ClickEvent<Payload.Text> openUrl(final URL url) {
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
  static ClickEvent<Payload.Text> openFile(final String file) {
    return ClickEventImpl.create(Action.OPEN_FILE, Payload.string(file));
  }

  /**
   * Creates a click event that runs a command.
   *
   * @param command the command to run
   * @return a click event
   * @since 4.0.0
   */
  static ClickEvent<Payload.Text> runCommand(final String command) {
    return ClickEventImpl.create(Action.RUN_COMMAND, Payload.string(command));
  }

  /**
   * Creates a click event that suggests a command.
   *
   * @param command the command to suggest
   * @return a click event
   * @since 4.0.0
   */
  static ClickEvent<Payload.Text> suggestCommand(final String command) {
    return ClickEventImpl.create(Action.SUGGEST_COMMAND, Payload.string(command));
  }

  /**
   * Creates a click event that changes to a page.
   *
   * @param page the page to change to
   * @return a click event
   * @since 4.0.0
   */
  static ClickEvent<Payload.Int> changePage(final int page) {
    return ClickEventImpl.create(Action.CHANGE_PAGE, Payload.integer(page));
  }

  /**
   * Creates a click event that copies text to the clipboard.
   *
   * @param text the text to copy to the clipboard
   * @return a click event
   * @since 4.0.0
   * @sinceMinecraft 1.15
   */
  static ClickEvent<Payload.Text> copyToClipboard(final String text) {
    return ClickEventImpl.create(Action.COPY_TO_CLIPBOARD, Payload.string(text));
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
  static ClickEvent<?> callback(final ClickCallback<Audience> function) {
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
  static ClickEvent<?> callback(final ClickCallback<Audience> function, final ClickCallback.Options options) {
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
  static ClickEvent<?> callback(final ClickCallback<Audience> function, final Consumer<ClickCallback.Options.Builder> optionsBuilder) {
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
  static ClickEvent<Payload.Dialog> showDialog(final DialogLike dialog) {
    requireNonNull(dialog, "dialog");
    return ClickEventImpl.create(Action.SHOW_DIALOG, Payload.dialog(dialog));
  }

  /**
   * Creates a click event that sends a custom event to the server.
   *
   * <p>See {@link BinaryTagHolder#binaryTagHolder(String)} for a simple way to create NBT from SNBT.
   * For simple use cases, you can use plain strings directly as SNBT.</p>
   *
   * @param key the key identifying the payload
   * @param nbt the nbt data
   * @return the click event
   * @since 4.23.0
   */
  static ClickEvent<Payload.Custom> custom(final Key key, final BinaryTagHolder nbt) {
    requireNonNull(key, "key");
    requireNonNull(nbt, "nbt");
    return ClickEventImpl.create(Action.CUSTOM, Payload.custom(key, nbt));
  }

  /**
   * Creates a click event with a {@link Payload payload}.
   *
   * @param action the action
   * @param payload the payload
   * @param <T> the payload type
   * @return a click event
   * @throws IllegalArgumentException if the action does not support that payload
   * @since 4.25.0
   */
  static <T extends ClickEvent.Payload> ClickEvent<T> clickEvent(final Action<T> action, final T payload) {
    return ClickEventImpl.create(action, payload);
  }

  /**
   * Gets the click event action.
   *
   * @return the click event action
   * @since 4.0.0
   */
  Action<T> action();

  /**
   * Gets the payload associated with this click event.
   *
   * @return the payload
   * @since 4.22.0
   */
  Payload payload();

  /**
   * An enumeration of click event actions.
   *
   * @param <T> the payload type
   * @since 4.0.0
   */
  sealed interface Action<T extends Payload> permits ClickEventImpl.ActionImpl {
    /**
     * Opens a url when clicked.
     *
     * @since 4.0.0
     */
    Action<Payload.Text> OPEN_URL = new ClickEventImpl.ActionImpl<>("open_url", true, Payload.Text.class);

    /**
     * Opens a file when clicked.
     *
     * <p>This action is not readable, and may only be used locally on the client.</p>
     *
     * @since 4.0.0
     */
    Action<Payload.Text> OPEN_FILE = new ClickEventImpl.ActionImpl<>("open_file", false, Payload.Text.class);

    /**
     * Runs a command when clicked.
     *
     * @since 4.0.0
     */
    Action<Payload.Text> RUN_COMMAND = new ClickEventImpl.ActionImpl<>("run_command", true, Payload.Text.class);

    /**
     * Suggests a command into the chat box.
     *
     * @since 4.0.0
     */
    Action<Payload.Text> SUGGEST_COMMAND = new ClickEventImpl.ActionImpl<>("suggest_command", true, Payload.Text.class);

    /**
     * Changes the page of a book.
     *
     * @since 4.0.0
     */
    Action<Payload.Int> CHANGE_PAGE = new ClickEventImpl.ActionImpl<>("change_page", true, Payload.Int.class);

    /**
     * Copies text to the clipboard.
     *
     * @since 4.0.0
     * @sinceMinecraft 1.15
     */
    Action<Payload.Text> COPY_TO_CLIPBOARD = new ClickEventImpl.ActionImpl<>("copy_to_clipboard", true, Payload.Text.class);

    /**
     * Shows a dialog.
     *
     * <p>This action is not readable at this time until Adventure has a full Dialog API.</p>
     *
     * @since 4.22.0
     * @sinceMinecraft 1.21.6
     */
    Action<Payload.Dialog> SHOW_DIALOG = new ClickEventImpl.ActionImpl<>("show_dialog", false, Payload.Dialog.class);

    /**
     * Sends a custom event to the server.
     *
     * @since 4.22.0
     * @sinceMinecraft 1.21.6
     */
    Action<Payload.Custom> CUSTOM = new ClickEventImpl.ActionImpl<>("custom", true, Payload.Custom.class);

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    Index<String, ClickEvent.Action<?>> NAMES = Index.create(Action::toString, OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE, COPY_TO_CLIPBOARD, SHOW_DIALOG, CUSTOM);

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     *     action is not readable
     * @since 4.0.0
     */
    boolean readable();

    /**
     * Returns if this action supports the provided payload.
     *
     * @param payload the payload
     * @return {@code true} if this action supports the payload
     * @since 4.22.0
     */
    boolean supports(final Payload payload);

    /**
     * The type of the payload this click event supports.
     *
     * @return the payload type
     * @since 4.22.0
     * @deprecated For removal, action now supports generics.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    Class<? extends Payload> payloadType();
  }

  /**
   * A payload for a click event.
   *
   * @since 4.22.0
   */
  sealed interface Payload permits ClickEvent.Payload.Custom, ClickEvent.Payload.Dialog, ClickEvent.Payload.Int, ClickEvent.Payload.Text {
    /**
     * Creates a text payload.
     *
     * @param value the payload value
     * @return the payload
     * @since 4.22.0
     */
    static ClickEvent.Payload.Text string(final String value) {
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
    static ClickEvent.Payload.Int integer(final int integer) {
      return new PayloadImpl.IntImpl(integer);
    }

    /**
     * Creates a dialog payload.
     *
     * @param dialog the payload value
     * @return the payload
     * @since 4.22.0
     */
    static ClickEvent.Payload.Dialog dialog(final DialogLike dialog) {
      requireNonNull(dialog, "dialog");
      return new PayloadImpl.DialogImpl(dialog);
    }

    /**
     * Creates a custom payload.
     *
     * <p>See {@link BinaryTagHolder#binaryTagHolder(String)} for a simple way to create NBT from SNBT.
     * For simple use cases, you can use plain strings directly as SNBT.</p>
     *
     * @param key the key identifying the payload
     * @param nbt the payload nbt data
     * @return the payload
     * @since 4.23.0
     */
    static ClickEvent.Payload.Custom custom(final Key key, final BinaryTagHolder nbt) {
      requireNonNull(key, "key");
      requireNonNull(nbt, "nbt");
      return new PayloadImpl.CustomImpl(key, nbt);
    }

    /**
     * A payload that holds a string.
     *
     * @since 4.22.0
     */
    sealed interface Text extends Payload permits PayloadImpl.TextImpl {
      /**
       * The string value for this payload.
       *
       * @return the string
       * @since 4.22.0
       */
      String value();
    }

    /**
     * A payload that holds an integer.
     *
     * @since 4.22.0
     */
    sealed interface Int extends Payload permits PayloadImpl.IntImpl {
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
    sealed interface Dialog extends Payload permits PayloadImpl.DialogImpl {
      /**
       * The dialog.
       *
       * @return the dialog
       * @since 4.22.0
       */
      DialogLike dialog();
    }

    /**
     * A payload that holds custom data.
     *
     * @see Action#CUSTOM
     * @since 4.22.0
     */
    sealed interface Custom extends Payload, Keyed permits PayloadImpl.CustomImpl {
      /**
       * The custom data.
       *
       * <p>See {@link BinaryTagHolder#string()} for a simple way to return SNBT from NBT data.</p>
       *
       * @return the data
       * @since 4.23.0
       */
      BinaryTagHolder nbt();
    }
  }
}
