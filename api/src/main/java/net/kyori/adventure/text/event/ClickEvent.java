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
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A click event.
 *
 * <p>A click event processes an {@link Action} when clicked on.</p>
 *
 * @param <T> the payload type
 * @since 4.0.0
 */
@SuppressWarnings("ClassCanBeRecord") // We need private constructors.
public final class ClickEvent<T extends ClickEvent.Payload> implements StyleBuilderApplicable {
  /**
   * Creates a click event that opens a url.
   *
   * <p>Since <em>Minecraft: Java Edition</em> 1.21.5 the url will fail to parse if not a {@code http://} or {@code https://} scheme.</p>
   *
   * @param url the url to open
   * @return a click event
   * @since 4.0.0
   */
  public static ClickEvent<Payload.Text> openUrl(final String url) {
    return clickEvent(Action.OPEN_URL, Payload.string(url));
  }

  /**
   * Creates a click event that opens a url.
   *
   * @param url the url to open
   * @return a click event
   * @since 4.0.0
   */
  public static ClickEvent<Payload.Text> openUrl(final URL url) {
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
  public static ClickEvent<Payload.Text> openFile(final String file) {
    return clickEvent(Action.OPEN_FILE, Payload.string(file));
  }

  /**
   * Creates a click event that runs a command.
   *
   * @param command the command to run
   * @return a click event
   * @since 4.0.0
   */
  public static ClickEvent<Payload.Text> runCommand(final String command) {
    return clickEvent(Action.RUN_COMMAND, Payload.string(command));
  }

  /**
   * Creates a click event that suggests a command.
   *
   * @param command the command to suggest
   * @return a click event
   * @since 4.0.0
   */
  public static ClickEvent<Payload.Text> suggestCommand(final String command) {
    return clickEvent(Action.SUGGEST_COMMAND, Payload.string(command));
  }

  /**
   * Creates a click event that changes to a page.
   *
   * @param page the page to change to
   * @return a click event
   * @since 4.0.0
   */
  public static ClickEvent<Payload.Int> changePage(final int page) {
    return clickEvent(Action.CHANGE_PAGE, Payload.integer(page));
  }

  /**
   * Creates a click event that copies text to the clipboard.
   *
   * @param text the text to copy to the clipboard
   * @return a click event
   * @since 4.0.0
   * @sinceMinecraft 1.15
   */
  public static ClickEvent<Payload.Text> copyToClipboard(final String text) {
    return clickEvent(Action.COPY_TO_CLIPBOARD, Payload.string(text));
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
  public static ClickEvent<?> callback(final ClickCallback<Audience> function) {
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
  public static ClickEvent<?> callback(final ClickCallback<Audience> function, final ClickCallback.Options options) {
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
  public static ClickEvent<?> callback(final ClickCallback<Audience> function, final Consumer<ClickCallback.Options.Builder> optionsBuilder) {
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
  public static ClickEvent<Payload.Dialog> showDialog(final DialogLike dialog) {
    return clickEvent(Action.SHOW_DIALOG, Payload.dialog(dialog));
  }

  /**
   * Creates a click event that sends a custom event to the server.
   *
   * @param key the key identifying the payload
   * @return the click event
   * @since 5.0.0
   */
  public static ClickEvent<Payload.Custom> custom(final Key key) {
    return ClickEvent.custom(key, null);
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
  public static ClickEvent<Payload.Custom> custom(final Key key, final @Nullable BinaryTagHolder nbt) {
    return clickEvent(Action.CUSTOM, Payload.custom(key, nbt));
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
  public static <T extends ClickEvent.Payload> ClickEvent<T> clickEvent(final Action<T> action, final T payload) {
    return new ClickEvent<>(requireNonNull(action, "action"), requireNonNull(payload, "payload"));
  }

  private final Action<T> action;
  private final Payload payload;

  private ClickEvent(final Action<T> action, final Payload payload) {
    this.action = action;
    this.payload = payload;
  }

  /**
   * Gets the click event action.
   *
   * @return the click event action
   * @since 4.0.0
   */
  public Action<T> action() {
    return this.action;
  }

  /**
   * Gets the payload associated with this click event.
   *
   * @return the payload
   * @since 4.22.0
   */
  public Payload payload() {
    return this.payload;
  }

  @Override
  public void styleApply(final Style.Builder style) {
    style.clickEvent(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof ClickEvent<?> that)) return false;
    return this.action.equals(that.action) && this.payload.equals(that.payload);
  }

  @Override
  public int hashCode() {
    int result = this.action.hashCode();
    result = 31 * result + this.payload.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "ClickEvent{" +
      "action=" + this.action +
      ", payload=" + this.payload +
      '}';
  }

  /**
   * An enumeration of click event actions.
   *
   * <p><b>Note:</b> although this interface is sealed, new implementations
   * may be added at any time as and when needed.</p>
   *
   * @param <T> the payload type
   * @since 4.0.0
   */
  @SuppressWarnings("StaticInitializerReferencesSubClass") // We have private subclasses and private constructors for them, so this is fine.
  public static sealed abstract class Action<T extends Payload> permits Action.ChangePage, Action.Custom, Action.ShowDialog, Action.TextCarrier {
    /**
     * Opens a url when clicked.
     *
     * @since 4.0.0
     */
    public static final OpenUrl OPEN_URL = new OpenUrl();

    /**
     * Opens a file when clicked.
     *
     * <p>This action is not readable, and may only be used locally on the client.</p>
     *
     * @since 4.0.0
     */
    public static final OpenFile OPEN_FILE = new OpenFile();

    /**
     * Runs a command when clicked.
     *
     * @since 4.0.0
     */
    public static final RunCommand RUN_COMMAND = new RunCommand();

    /**
     * Suggests a command into the chat box.
     *
     * @since 4.0.0
     */
    public static final SuggestCommand SUGGEST_COMMAND = new SuggestCommand();

    /**
     * Changes the page of a book.
     *
     * @since 4.0.0
     */
    public static final ChangePage CHANGE_PAGE = new ChangePage();

    /**
     * Copies text to the clipboard.
     *
     * @since 4.0.0
     * @sinceMinecraft 1.15
     */
    public static final CopyToClipboard COPY_TO_CLIPBOARD = new CopyToClipboard();

    /**
     * Shows a dialog.
     *
     * <p>This action is not readable at this time until Adventure has a full Dialog API.</p>
     *
     * @since 4.22.0
     * @sinceMinecraft 1.21.6
     */
    public static final ShowDialog SHOW_DIALOG = new ShowDialog();

    /**
     * Sends a custom event to the server.
     *
     * @since 4.22.0
     * @sinceMinecraft 1.21.6
     */
    public static final Custom CUSTOM = new Custom();

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    public static final Index<String, ClickEvent.Action<?>> NAMES = Index.create(Action::toString, OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE, COPY_TO_CLIPBOARD, SHOW_DIALOG, CUSTOM);

    private final String name;
    private final boolean readable;
    private final Class<? extends ClickEvent.Payload> payloadType;

    Action(final String name, final boolean readable, final Class<? extends ClickEvent.Payload> payloadType) {
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
    public boolean supports(final Payload payload) {
      return this.payloadType.isAssignableFrom(payload.getClass());
    }

    /**
     * Returns the name of this action.
     *
     * @return the name of the action
     * @since 4.0.0
     */
    public String name() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }

    @Override
    public int hashCode() {
      return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
      return obj == this;
    }

    /**
     * An action with a text payload.
     *
     * @since 5.0.0
     */
    public sealed abstract static class TextCarrier extends Action<ClickEvent.Payload.Text> permits OpenUrl, OpenFile, RunCommand, SuggestCommand, CopyToClipboard {
      TextCarrier(final String name, final boolean readable) {
        super(name, readable, Payload.Text.class);
      }
    }

    /**
     * Opens a url when clicked.
     *
     * @see #OPEN_URL
     * @since 5.0.0
     */
    public static final class OpenUrl extends TextCarrier {
      private OpenUrl() {
        super("open_url", true);
      }
    }

    /**
     * Opens a file when clicked.
     *
     * <p>This action is not readable, and may only be used locally on the client.</p>
     *
     * @see #OPEN_FILE
     * @since 5.0.0
     */
    public static final class OpenFile extends TextCarrier {
      private OpenFile() {
        super("open_file", false);
      }
    }

    /**
     * Runs a command when clicked.
     *
     * @see #RUN_COMMAND
     * @since 5.0.0
     */
    public static final class RunCommand extends TextCarrier {
      private RunCommand() {
        super("run_command", true);
      }
    }

    /**
     * Suggests a command.
     *
     * @see #SUGGEST_COMMAND
     * @since 5.0.0
     */
    public static final class SuggestCommand extends TextCarrier {
      private SuggestCommand() {
        super("suggest_command", true);
      }
    }

    /**
     * Changes the page of a book.
     *
     * @see #CHANGE_PAGE
     * @since 5.0.0
     */
    public static final class ChangePage extends ClickEvent.Action<Payload.Int> {
      private ChangePage() {
        super("change_page", true, Payload.Int.class);
      }
    }

    /**
     * Copies text to the clipboard.
     *
     * @see #COPY_TO_CLIPBOARD
     * @since 4.0.0
     * @sinceMinecraft 1.15
     */
    public static final class CopyToClipboard extends TextCarrier {
      private CopyToClipboard() {
        super("copy_to_clipboard", true);
      }
    }

    /**
     * Shows a dialog.
     *
     * <p>This action is not readable at this time until Adventure has a full Dialog API.</p>
     *
     * @see #SHOW_DIALOG
     * @since 5.0.0
     * @sinceMinecraft 1.21.6
     */
    public static final class ShowDialog extends ClickEvent.Action<Payload.Dialog> {
      private ShowDialog() {
        super("show_dialog", false, Payload.Dialog.class);
      }
    }

    /**
     * Sends a custom event to the server.
     *
     * @see #CUSTOM
     * @since 5.0.0
     * @sinceMinecraft 1.21.6
     */
    public static final class Custom extends ClickEvent.Action<Payload.Custom> {
      private Custom() {
        super("custom", true, Payload.Custom.class);
      }
    }
  }

  /**
   * A payload for a click event.
   *
   * <p><b>Note:</b> although this interface is sealed, new implementations
   * may be added at any time as and when needed.</p>
   *
   * @since 4.22.0
   */
  public sealed interface Payload permits ClickEvent.Payload.Custom, ClickEvent.Payload.Dialog, ClickEvent.Payload.Int, ClickEvent.Payload.Text {
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
     * @param key the key identifying the payload
     * @return the payload
     * @since 5.0.0
     */
    static ClickEvent.Payload.Custom custom(final Key key) {
      return ClickEvent.Payload.custom(key, null);
    }

    /**
     * Creates a custom payload.
     *
     * <p>See {@link BinaryTagHolder#binaryTagHolder(String)} for a simple way to create NBT from SNBT.
     * For simple use cases, you can use plain strings directly as SNBT.</p>
     *
     * @param key the key identifying the payload
     * @param nbt the payload nbt data, optional
     * @return the payload
     * @since 4.23.0
     */
    static ClickEvent.Payload.Custom custom(final Key key, final @Nullable BinaryTagHolder nbt) {
      requireNonNull(key, "key");
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
     * A payload with a key and optional custom NBT data.
     *
     * @see Action#CUSTOM
     * @since 4.22.0
     */
    sealed interface Custom extends Payload, Keyed permits PayloadImpl.CustomImpl {
      /**
       * The optional custom data.
       *
       * <p>See {@link BinaryTagHolder#string()} for a simple way to return SNBT from NBT data.</p>
       *
       * @return the data
       * @since 4.23.0
       */
      @Nullable BinaryTagHolder nbt();
    }
  }
}
