/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.minimessage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import net.kyori.adventure.text.minimessage.internal.serializer.QuotingOverride;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class MiniMessageSerializer {
  private MiniMessageSerializer() {
  }

  // TODO: serialization customization:
  // - preferred quoting style
  // - abbreviated vs long tag names (tag-specific option)
  //

  static @NotNull String serialize(final @NotNull Component component, final @NotNull SerializableResolver resolver, final boolean strict) {
    final StringBuilder sb = new StringBuilder();
    final Collector emitter = new Collector(resolver, strict, sb);

    emitter.mark();
    visit(component, emitter, resolver, true);
    if (strict) {
      // If we are in strict mode, we need to close all tags at the end of our serialization journey
      emitter.popAll();
    } else {
      emitter.completeTag();
    }

    return sb.toString();
  }

  private static void visit(final @NotNull Component component, final Collector emitter, final SerializableResolver resolver, final boolean lastChild) {
    // visit self
    resolver.handle(component, emitter);
    Component childSource = emitter.flushClaims(component);
    if (childSource == null) {
      childSource = component;
    }

    // then children
    for (final Iterator<Component> it = childSource.children().iterator(); it.hasNext();) {
      emitter.mark();
      visit(it.next(), emitter, resolver, lastChild && !it.hasNext());
    }

    if (!lastChild) {
      emitter.popToMark();
    }
  }

  static final class Collector implements TokenEmitter, ClaimConsumer {
    enum TagState {
      TEXT(false),
      MID(true),
      MID_SELF_CLOSING(true);

      final boolean isTag;

      TagState(final boolean isTag) {
        this.isTag = isTag;
      }
    }

    /**
     * mark tag boundaries within the stack, without needing to mess with typing too much.
     */
    private static final String MARK = "__<'\"\\MARK__";
    private static final char[] TEXT_ESCAPES = {TokenParser.ESCAPE, TokenParser.TAG_START};
    private static final char[] TAG_TOKENS = {TokenParser.TAG_END, TokenParser.SEPARATOR};
    private static final char[] SINGLE_QUOTED_ESCAPES = {TokenParser.ESCAPE, '\''};
    private static final char[] DOUBLE_QUOTED_ESCAPES = {TokenParser.ESCAPE, '"'};

    private final SerializableResolver resolver;
    private final boolean strict;
    private final StringBuilder consumer;
    private String[] activeTags = new String[4];
    private int tagLevel = 0;
    private TagState tagState = TagState.TEXT;

    Collector(final SerializableResolver resolver, final boolean strict, final StringBuilder consumer) {
      this.resolver = resolver;
      this.strict = strict;
      this.consumer = consumer;
    }

    // state tracking
    private void pushActiveTag(final String tag) {
      if (this.tagLevel >= this.activeTags.length) {
        this.activeTags = Arrays.copyOf(this.activeTags, this.activeTags.length * 2);
      }
      this.activeTags[this.tagLevel++] = tag;
    }

    private String popTag(final boolean allowMarks) {
      if (this.tagLevel-- <= 0) {
        throw new IllegalStateException("Unbalanced tags, tried to pop below depth");
      }
      final String tag = this.activeTags[this.tagLevel];
      if (!allowMarks && tag == MARK) {
        throw new IllegalStateException("Tried to pop past mark, tag stack: " + Arrays.toString(this.activeTags) + " @ " + this.tagLevel);
      }
      return tag;
    }

    void mark() {
      this.pushActiveTag(MARK);
    }

    void popToMark() {
      if (this.tagLevel == 0) {
        return;
      }
      String tag;
      while ((tag = this.popTag(true)) != MARK) {
        this.emitClose(tag);
      }
    }

    void popAll() {
      while (this.tagLevel > 0) {
        final String tag = this.activeTags[--this.tagLevel];
        if (tag != MARK) {
          this.emitClose(tag);
        }
      }
    }

    void completeTag() {
      if (this.tagState.isTag) {
        this.consumer.append(TokenParser.TAG_END);
        this.tagState = TagState.TEXT;
      }
    }

    // TokenEmitter

    @Override
    public @NotNull Collector tag(final @NotNull String token) {
      this.completeTag();
      this.consumer.append(TokenParser.TAG_START);
      this.escapeTagContent(token, QuotingOverride.UNQUOTED);
      this.tagState = TagState.MID;
      this.pushActiveTag(token);
      return this;
    }

    @Override
    public @NotNull TokenEmitter selfClosingTag(final @NotNull String token) {
      this.completeTag();
      this.consumer.append(TokenParser.TAG_START);
      this.escapeTagContent(token, QuotingOverride.UNQUOTED);
      this.tagState = TagState.MID_SELF_CLOSING;
      return this;
    }

    @Override
    public @NotNull TokenEmitter argument(final @NotNull String arg) {
      if (!this.tagState.isTag) {
        throw new IllegalStateException("Not within a tag!");
      }
      this.consumer.append(TokenParser.SEPARATOR);
      this.escapeTagContent(arg, null);
      return this;
    }

    @Override
    public @NotNull TokenEmitter argument(final @NotNull String arg, final @NotNull QuotingOverride quotingPreference) {
      if (!this.tagState.isTag) {
        throw new IllegalStateException("Not within a tag!");
      }
      this.consumer.append(TokenParser.SEPARATOR);
      this.escapeTagContent(arg, requireNonNull(quotingPreference, "quotingPreference"));
      return this;
    }

    @Override
    public @NotNull TokenEmitter argument(final @NotNull Component arg) {
      final String serialized = MiniMessageSerializer.serialize(arg, this.resolver, this.strict);
      return this.argument(serialized, QuotingOverride.QUOTED); // always quote tokens
    }

    @Override
    public @NotNull Collector text(final @NotNull String text) {
      this.completeTag();
      // escape '\' and '<'
      appendEscaping(this.consumer, text, TEXT_ESCAPES, true);
      return this;
    }

    private void escapeTagContent(final String content, final @Nullable QuotingOverride preference) {
      boolean mustBeQuoted = preference == QuotingOverride.QUOTED;
      boolean hasSingleQuote = false;
      boolean hasDoubleQuote = false;

      for (int i = 0; i < content.length(); i++) {
        final char active = content.charAt(i);
        if (active == TokenParser.TAG_END || active == TokenParser.SEPARATOR || active == ' ') { // space is not technically required here, but is preferred
          mustBeQuoted = true;
          if (hasSingleQuote && hasDoubleQuote) break;
        } else if (active == '\'') {
          hasSingleQuote = true;
          break; // we know our quoting style
        } else if (active == '"') {
          hasDoubleQuote = true;
          if (mustBeQuoted && hasSingleQuote) break;
        }
      }

      if (hasSingleQuote) { // double-quoted
        this.consumer.append('"');
        appendEscaping(this.consumer, content, DOUBLE_QUOTED_ESCAPES, true);
        this.consumer.append('"');
      } else if (hasDoubleQuote || mustBeQuoted) {
        // single-quoted
        this.consumer.append('\'');
        appendEscaping(this.consumer, content, SINGLE_QUOTED_ESCAPES, true);
        this.consumer.append('\'');
      } else { // unquoted
        appendEscaping(this.consumer, content, TAG_TOKENS, false);
      }
    }

    static void appendEscaping(final StringBuilder builder, final String text, final char[] escapeChars, final boolean allowEscapes) {
      int startIdx = 0;
      boolean unescapedFound = false;

      for (int i = 0; i < text.length(); i++) {
        final char test = text.charAt(i);
        boolean escaped = false;
        for (final char c : escapeChars) {
          if (test == c) {
            if (!allowEscapes) {
              throw new IllegalArgumentException("Invalid escapable character '" + test + "' found at index " + i + " in string '" + text + "'");
            }
            escaped = true;
            break;
          }
        }

        if (escaped) {
          if (unescapedFound) builder.append(text, startIdx, i);
          startIdx = i + 1;
          builder.append(TokenParser.ESCAPE).append(test);
        } else {
          unescapedFound = true;
        }
      }

      if (startIdx < text.length() && unescapedFound) {
        builder.append(text, startIdx, text.length());
      }
    }

    @Override
    public @NotNull Collector pop() {
      this.emitClose(this.popTag(false));
      return this;
    }

    private void emitClose(final @NotNull String tag) {
      // currently: we don't keep any arguments, does it ever make sense to?
      if (this.tagState.isTag) {
        if (this.tagState == TagState.MID) { // not _SELF_CLOSING
          this.consumer.append(TokenParser.CLOSE_TAG);
        }
        this.consumer.append(TokenParser.TAG_END);
        this.tagState = TagState.TEXT;
      } else {
        this.consumer.append(TokenParser.TAG_START)
          .append(TokenParser.CLOSE_TAG);
        this.escapeTagContent(tag, QuotingOverride.UNQUOTED);
        this.consumer.append(TokenParser.TAG_END);
      }
    }

    // ClaimCollector

    @Nullable Emitable componentClaim;
    final Set<String> claimedStyleElements = new HashSet<>();

    @Override
    public void style(final @NotNull String claimKey, final @NotNull Emitable styleClaim) {
      if (this.claimedStyleElements.add(requireNonNull(claimKey, "claimKey"))) {
        styleClaim.emit(this);
      }
    }

    @Override
    public boolean component(final @NotNull Emitable componentClaim) {
      if (this.componentClaim != null) return false;

      this.componentClaim = requireNonNull(componentClaim, "componentClaim");
      return true;
    }

    @Override
    public boolean componentClaimed() {
      return this.componentClaim != null;
    }

    @Override
    public boolean styleClaimed(final @NotNull String claimId) {
      return this.claimedStyleElements.contains(claimId);
    }

    @Nullable Component flushClaims(final Component component) { // return: a substitute to provide children
      Component ret = null;
      if (this.componentClaim != null) {
        this.componentClaim.emit(this);
        ret = this.componentClaim.substitute();
        this.componentClaim = null;
      } else if (component instanceof TextComponent) {
        this.text(((TextComponent) component).content());
      } else {
        // todo: best choice?
        throw new IllegalStateException("Unclaimed component " + component);
      }
      this.claimedStyleElements.clear();
      return ret;
    }
  }

}
