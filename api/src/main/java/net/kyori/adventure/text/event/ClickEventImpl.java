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

import net.kyori.adventure.text.format.Style;

import static java.util.Objects.requireNonNull;

record ClickEventImpl<T extends ClickEvent.Payload>(Action<T> action, Payload payload) implements ClickEvent<T> {

  static <T extends ClickEvent.Payload> ClickEvent<T> create(final Action<T> action, final T payload) {
    return new ClickEventImpl<>(requireNonNull(action, "action"), requireNonNull(payload, "payload"));
  }

  @Override
  public void styleApply(final Style.Builder style) {
    style.clickEvent(this);
  }

  static ClickEvent.Action.OpenUrl OPEN_URL = new AbstractAction.OpenUrlImpl();
  static ClickEvent.Action.OpenFile OPEN_FILE = new AbstractAction.OpenFileImpl();
  static ClickEvent.Action.RunCommand RUN_COMMAND = new AbstractAction.RunCommandImpl();
  static ClickEvent.Action.SuggestCommand SUGGEST_COMMAND = new AbstractAction.SuggestCommandImpl();
  static ClickEvent.Action.ChangePage CHANGE_PAGE = new AbstractAction.ChangePageImpl();
  static ClickEvent.Action.CopyToClipboard COPY_TO_CLIPBOARD = new AbstractAction.CopyToClipboardImpl();
  static ClickEvent.Action.ShowDialog SHOW_DIALOG = new AbstractAction.ShowDialogImpl();
  static ClickEvent.Action.Custom CUSTOM = new AbstractAction.CustomImpl();

  abstract static class AbstractAction {
    private final String name;
    private final boolean readable;
    private final Class<? extends Payload> payloadType;

    private AbstractAction(final String name, final boolean readable, final Class<? extends Payload> payloadType) {
      this.name = name;
      this.readable = readable;
      this.payloadType = payloadType;
    }

    public boolean readable() {
      return this.readable;
    }

    public boolean supports(final Payload payload) {
      return this.payloadType.isInstance(payload);
    }

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

    static final class OpenUrlImpl extends AbstractAction implements Action.OpenUrl {
      private OpenUrlImpl() {
        super("open_url", true, Payload.Text.class);
      }
    }

    static final class OpenFileImpl extends AbstractAction implements Action.OpenFile {
      private OpenFileImpl() {
        super("open_file", false, Payload.Text.class);
      }
    }

    static final class RunCommandImpl extends AbstractAction implements Action.RunCommand {
      private RunCommandImpl() {
        super("run_command", true, Payload.Text.class);
      }
    }

    static final class SuggestCommandImpl extends AbstractAction implements Action.SuggestCommand {
      private SuggestCommandImpl() {
        super("suggest_command", true, Payload.Text.class);
      }
    }

    static final class ChangePageImpl extends AbstractAction implements Action.ChangePage {
      private ChangePageImpl() {
        super("change_page", true, Payload.Int.class);
      }
    }

    static final class CopyToClipboardImpl extends AbstractAction implements Action.CopyToClipboard {
      private CopyToClipboardImpl() {
        super("copy_to_clipboard", true, Payload.Text.class);
      }
    }

    static final class ShowDialogImpl extends AbstractAction implements Action.ShowDialog {
      private ShowDialogImpl() {
        super("show_dialog", false, Payload.Dialog.class);
      }
    }

    static final class CustomImpl extends AbstractAction implements Action.Custom {
      private CustomImpl() {
        super("custom", true, Payload.Custom.class);
      }
    }
  }
}
