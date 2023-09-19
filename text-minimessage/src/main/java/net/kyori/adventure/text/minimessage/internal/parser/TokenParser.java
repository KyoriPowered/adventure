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
package net.kyori.adventure.text.minimessage.internal.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.TagInternals;
import net.kyori.adventure.text.minimessage.internal.parser.match.MatchedTokenConsumer;
import net.kyori.adventure.text.minimessage.internal.parser.match.StringResolvingMatchedTokenConsumer;
import net.kyori.adventure.text.minimessage.internal.parser.match.TokenListProducingMatchedTokenConsumer;
import net.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.internal.parser.node.TextNode;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.ParserDirective;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles parsing a string into a list of tokens and then into a tree of nodes.
 *
 * @since 4.10.0
 */
@ApiStatus.Internal
public final class TokenParser {
  private static final int MAX_DEPTH = 16;
  // minimessage tags
  public static final char TAG_START = '<';
  public static final char TAG_END = '>';
  public static final char CLOSE_TAG = '/';
  public static final char SEPARATOR = ':';
  // misc
  public static final char ESCAPE = '\\';

  private TokenParser() {
  }

  /**
   * Parse a minimessage string into a tree of nodes.
   *
   * @param tagProvider provides tags based on the available information
   * @param tagNameChecker checker for tag names, performing necessary tag normalization
   * @param message the minimessage string to parse, after processing for preprocess tags
   * @param originalMessage the string to parse, before preprocess tags
   * @param strict whether parsing in strict mode
   * @return the root of the resulting tree
   * @throws ParsingException if invalid input is provided when in strict mode
   * @since 4.10.0
   */
  public static RootNode parse(
    final @NotNull TagProvider tagProvider,
    final @NotNull Predicate<String> tagNameChecker,
    final @NotNull String message,
    final @NotNull String originalMessage,
    final boolean strict
  ) throws ParsingException {
    // collect tokens...
    final List<Token> tokens = tokenize(message, false);

    // then build the tree!
    return buildTree(tagProvider, tagNameChecker, tokens, message, originalMessage, strict);
  }

  /**
   * Resolves all pre-process tags in a string.
   *
   * @param message the message
   * @param provider the tag resolver, to gather preprocess tags
   * @return the resulting string
   * @since 4.10.0
   */
  public static String resolvePreProcessTags(final String message, final TagProvider provider) {
    int passes = 0;
    String lastResult;
    String result = message;

    do {
      lastResult = result;
      final StringResolvingMatchedTokenConsumer stringTokenResolver = new StringResolvingMatchedTokenConsumer(lastResult, provider);

      parseString(lastResult, false, stringTokenResolver);
      result = stringTokenResolver.result();
      passes++;
    } while (passes < MAX_DEPTH && !lastResult.equals(result));

    return lastResult;
  }

  /**
   * Tokenize a minimessage string into a list of tokens.
   *
   * @param message the minimessage string to parse
   * @param lenient whether to allow section symbols (for escaping/stripping/non-actual-parse stuff only)
   * @return the root tokens
   * @since 4.10.0
   */
  public static List<Token> tokenize(final String message, final boolean lenient) {
    final TokenListProducingMatchedTokenConsumer listProducer = new TokenListProducingMatchedTokenConsumer(message);
    parseString(message, lenient, listProducer);
    final List<Token> tokens = listProducer.result();
    parseSecondPass(message, tokens);
    return tokens;
  }

  enum FirstPassState {
    NORMAL,
    TAG,
    STRING;
  }

  /**
   * Parses a string, providing information on matched tokens to the matched token consumer.
   *
   * @param message the message
   * @param lenient whether to allow section symbols
   * @param consumer the consumer
   * @since 4.10.0
   */
  public static void parseString(final String message, final boolean lenient, final MatchedTokenConsumer<?> consumer) {
    FirstPassState state = FirstPassState.NORMAL;
    // If the current state is escaped then the next character is skipped
    boolean escaped = false;

    int currentTokenEnd = 0;
    // Marker is the starting index for the current token
    int marker = -1;
    char currentStringChar = 0;

    final int length = message.length();
    for (int i = 0; i < length; i++) {
      final int codePoint = message.codePointAt(i);
      // DiamondFire start
      /*
      if (!lenient && codePoint == '§' && i + 1 < length) {
        final int nextChar = Character.toLowerCase(message.codePointAt(i + 1));
        // Only throw an exception if the next character is actually going to make a legacy color code
        if ((nextChar >= '0' && nextChar <= '9')
          || (nextChar >= 'a' && nextChar <= 'f')
          || nextChar == 'r'
          || (nextChar >= 'k' && nextChar <= 'o')) {
          throw new ParsingExceptionImpl(
            "Legacy formatting codes have been detected in a MiniMessage string - this is unsupported behaviour. Please refer to the Adventure documentation (https://docs.advntr.dev) for more information.",
            message,
            null,
            true,
            new Token(i, i + 2, TokenType.TEXT)
          );
        }
      }
      */
      // DiamondFire end

      if (!Character.isBmpCodePoint(codePoint)) {
        i++;
      }
      if (!escaped) {
        // if we're trying to escape and the next character exists
        if (codePoint == ESCAPE && i + 1 < message.length()) {
          final int nextCodePoint = message.codePointAt(i + 1);

          switch (state) {
            case NORMAL:
              // allow escaping open tokens
              escaped = nextCodePoint == TAG_START || nextCodePoint == ESCAPE;
              break;
            case STRING:
              // allow escaping closing string chars
              escaped = currentStringChar == nextCodePoint || nextCodePoint == ESCAPE;
              break;
            case TAG:
              // Escape characters are not valid in tag names, so we aren't a tag token
              if (nextCodePoint == TAG_START) {
                escaped = true;
                state = FirstPassState.NORMAL;
              }
              break;
          }

          // only escape if we need to
          if (escaped) {
            continue;
          }
        }
      } else {
        escaped = false;
        continue;
      }

      switch (state) {
        case NORMAL:
          if (codePoint == TAG_START) {
            // Possibly a tag
            marker = i;
            state = FirstPassState.TAG;
          }
          break;
        case TAG:
          switch (codePoint) {
            case TAG_END:
              if (i == marker + 1) {
                // This is empty, <>, so it's not a tag
                state = FirstPassState.NORMAL;
                break;
              }

              // We found a tag
              if (currentTokenEnd != marker) {
                // anything not matched up to this point is normal text
                consumer.accept(currentTokenEnd, marker, TokenType.TEXT);
              }
              currentTokenEnd = i + 1;

              // closing tags start with </
              TokenType thisType = TokenType.OPEN_TAG;
              if (boundsCheck(message, marker, 1) && message.charAt(marker + 1) == CLOSE_TAG) { // </content>
                thisType = TokenType.CLOSE_TAG;
              } else if (boundsCheck(message, marker, 2) && message.charAt(i - 1) == CLOSE_TAG) { // <content/>
                thisType = TokenType.OPEN_CLOSE_TAG;
              }
              consumer.accept(marker, currentTokenEnd, thisType);
              state = FirstPassState.NORMAL;
              break;
            case TAG_START:
              // This isn't a tag, but we can re-start looking here
              marker = i;
              break;
            case '\'':
            case '"':
              currentStringChar = (char) codePoint;
              // Look ahead if the quote being opened is ever closed
              if (message.indexOf(codePoint, i + 1) != -1) {
                state = FirstPassState.STRING;
              }
              break;
          }
          break;
        case STRING:
          if (codePoint == currentStringChar) {
            state = FirstPassState.TAG;
          }
          break;
      }

      if (i == (length - 1) && state == FirstPassState.TAG) {
        // We've reached the end of the input with an open `<`, but it was never matched to a closing `>`.
        // Anything which was inside of quotes needs to be parsed again, as it may contain additional tags.
        // Rewind back to directly after the `<`, but in the NORMAL state, instead of TAG.
        i = marker;
        state = FirstPassState.NORMAL;
      }
    }

    // anything left over is plain text
    final int end = consumer.lastEndIndex();
    if (end == -1) {
      consumer.accept(0, message.length(), TokenType.TEXT);
    } else if (end != message.length()) {
      consumer.accept(end, message.length(), TokenType.TEXT);
    }
  }

  /*
   * Second pass over the tag tokens identifies tag parts
   */
  @SuppressWarnings("DuplicatedCode")
  private static void parseSecondPass(final String message, final List<Token> tokens) {
    for (final Token token : tokens) {
      final TokenType type = token.type();
      if (type != TokenType.OPEN_TAG && type != TokenType.OPEN_CLOSE_TAG && type != TokenType.CLOSE_TAG) {
        continue;
      }

      // Only look inside the tag <[/] and >
      final int startIndex = type == TokenType.CLOSE_TAG ? token.startIndex() + 2 : token.startIndex() + 1;
      final int endIndex = type == TokenType.OPEN_CLOSE_TAG ? token.endIndex() - 2 : token.endIndex() - 1;

      SecondPassState state = SecondPassState.NORMAL;
      boolean escaped = false;
      char currentStringChar = 0;

      // Marker is the starting index for the current token
      int marker = startIndex;

      for (int i = startIndex; i < endIndex; i++) {
        final int codePoint = message.codePointAt(i);
        if (!Character.isBmpCodePoint(i)) {
          i++;
        }

        if (!escaped) {
          // if we're trying to escape and the next character exists
          if (codePoint == ESCAPE && i + 1 < message.length()) {
            final int nextCodePoint = message.codePointAt(i + 1);

            switch (state) {
              case NORMAL:
                // allow escaping open tokens
                escaped = nextCodePoint == TAG_START || nextCodePoint == ESCAPE;
                break;
              case STRING:
                // allow escaping closing string chars
                escaped = currentStringChar == nextCodePoint || nextCodePoint == ESCAPE;
                break;
            }

            // only escape if we need to
            if (escaped) {
              continue;
            }
          }
        } else {
          escaped = false;
          continue;
        }

        switch (state) {
          case NORMAL:
            // Values are split by : unless it's in a URL
            if (codePoint == SEPARATOR) {
              if (boundsCheck(message, i, 2) && message.charAt(i + 1) == '/' && message.charAt(i + 2) == '/') {
                break;
              }
              if (marker == i) {
                // 2 colons side-by-side like <::> or <:text> or <text::text> would lead to this happening
                insert(token, new Token(i, i, TokenType.TAG_VALUE));
                marker++;
              } else {
                insert(token, new Token(marker, i, TokenType.TAG_VALUE));
                marker = i + 1;
              }
            } else if (codePoint == '\'' || codePoint == '"') {
              state = SecondPassState.STRING;
              currentStringChar = (char) codePoint;
            }
            break;
          case STRING:
            if (codePoint == currentStringChar) {
              state = SecondPassState.NORMAL;
            }
            break;
        }
      }

      // anything not matched is the final part
      if (token.childTokens() == null || token.childTokens().isEmpty()) {
        insert(token, new Token(startIndex, endIndex, TokenType.TAG_VALUE));
      } else {
        final int end = token.childTokens().get(token.childTokens().size() - 1).endIndex();
        if (end != endIndex) {
          insert(token, new Token(end + 1, endIndex, TokenType.TAG_VALUE));
        }
      }
    }
  }

  /*
   * Build a tree from the OPEN_TAG and CLOSE_TAG tokens
   */
  private static RootNode buildTree(
    final @NotNull TagProvider tagProvider,
    final @NotNull Predicate<String> tagNameChecker,
    final @NotNull List<Token> tokens,
    final @NotNull String message,
    final @NotNull String originalMessage,
    final boolean strict
  ) throws ParsingException {
    final RootNode root = new RootNode(message, originalMessage);
    ElementNode node = root;

    for (final Token token : tokens) {
      final TokenType type = token.type();
      switch (type) {
        case TEXT:
          node.addChild(new TextNode(node, token, message));
          break;

        case OPEN_TAG:
        case OPEN_CLOSE_TAG:
          // Check if this even is a valid tag
          final Token tagNamePart = token.childTokens().get(0);
          final String tagName = message.substring(tagNamePart.startIndex(), tagNamePart.endIndex());
          if (!TagInternals.sanitizeAndCheckValidTagName(tagName)) {
            // This wouldn't be a valid tag, just parse it as text instead!
            node.addChild(new TextNode(node, token, message));
            break;
          }

          final TagNode tagNode = new TagNode(node, token, message, tagProvider);
          if (tagNameChecker.test(tagNode.name())) {
            final Tag tag = tagProvider.resolve(tagNode);
            if (tag == null) {
              // something went wrong, ignore it
              // if strict mode is enabled this will throw an exception for us
              node.addChild(new TextNode(node, token, message));
            } else if (tag == ParserDirective.RESET) {
              // <reset> tags get special treatment and don't appear in the tree
              // instead, they close all currently open tags
              if (strict) {
                throw new ParsingExceptionImpl("<reset> tags are not allowed when strict mode is enabled", message, token);
              }
              node = root;
            } else {
              // This is a recognized tag, goes in the tree
              tagNode.tag(tag);
              node.addChild(tagNode);
              if (type != TokenType.OPEN_CLOSE_TAG && (!(tag instanceof Inserting) || ((Inserting) tag).allowsChildren())) {
                node = tagNode;
              }
            }
          } else {
            // not recognized, plain text
            node.addChild(new TextNode(node, token, message));
          }
          break; // OPEN_TAG

        case CLOSE_TAG:
          final List<Token> childTokens = token.childTokens();
          if (childTokens.isEmpty()) {
            throw new IllegalStateException("CLOSE_TAG token somehow has no children - " +
              "the parser should not allow this. Original text: " + message);
          }

          final ArrayList<String> closeValues = new ArrayList<>(childTokens.size());
          for (final Token childToken : childTokens) {
            closeValues.add(TagPart.unquoteAndEscape(message, childToken.startIndex(), childToken.endIndex()));
          }

          final String closeTagName = closeValues.get(0);

          if (tagNameChecker.test(closeTagName)) {
            final Tag tag = tagProvider.resolve(closeTagName);

            if (tag == ParserDirective.RESET) {
              // This is a synthetic node, closing it means nothing in the context of building a tree
              continue;
            }
          } else {
            // tag does not exist, so treat it as text
            node.addChild(new TextNode(node, token, message));
            continue;
          }

          ElementNode parentNode = node;
          while (parentNode instanceof TagNode) {
            final List<TagPart> openParts = ((TagNode) parentNode).parts();

            if (tagCloses(closeValues, openParts)) {
              if (parentNode != node && strict) {
                final String msg = "Unclosed tag encountered; " + ((TagNode) node).name() + " is not closed, because " +
                  closeValues.get(0) + " was closed first.";
                throw new ParsingExceptionImpl(msg, message, parentNode.token(), node.token(), token);
              }

              final ElementNode par = parentNode.parent();
              if (par != null) {
                node = par;
              } else {
                throw new IllegalStateException("Root node matched with close tag value, " +
                  "this should not be possible. Original text: " + message);
              }
              break;
            }

            parentNode = parentNode.parent();
          }
          if (parentNode == null || parentNode instanceof RootNode) {
            // This means the closing tag didn't match to anything
            // Since open tags which don't match to anything is never an error, neither is this
            node.addChild(new TextNode(node, token, message));
            break;
          }
          break; // CLOSE_TAG
        default: // ignore other tags
          break;
      }
    }

    if (strict && root != node) {
      final ArrayList<TagNode> openTags = new ArrayList<>();
      {
        ElementNode n = node;
        while (n != null) {
          if (n instanceof TagNode) {
            openTags.add((TagNode) n);
          } else {
            break;
          }
          n = n.parent();
        }
      }

      final Token[] errorTokens = new Token[openTags.size()];

      final StringBuilder sb = new StringBuilder("All tags must be explicitly closed while in strict mode. " +
        "End of string found with open tags: ");

      int i = 0;
      final ListIterator<TagNode> iter = openTags.listIterator(openTags.size());
      while (iter.hasPrevious()) {
        final TagNode n = iter.previous();
        errorTokens[i++] = n.token();

        sb.append(n.name());
        if (iter.hasPrevious()) {
          sb.append(", ");
        }
      }

      throw new ParsingExceptionImpl(sb.toString(), message, errorTokens);
    }

    return root;
  }

  /**
   * Determine if a set of close string parts closes the given list of open tag parts. If the open parts starts with
   * the set of close parts, then this method returns {@code true}.
   *
   * @param closeParts The parts of the close tag
   * @param openParts The parts of the open tag
   * @return {@code true} if the given close parts closes the open tag parts.
   */
  private static boolean tagCloses(final List<String> closeParts, final List<TagPart> openParts) {
    if (closeParts.size() > openParts.size()) {
      return false;
    }
    // The tag name is case-insensitive, but the tag values are not
    if (!closeParts.get(0).equalsIgnoreCase(openParts.get(0).value())) {
      return false;
    }
    for (int i = 1; i < closeParts.size(); i++) {
      if (!closeParts.get(i).equals(openParts.get(i).value())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns {@code true} if it's okay to check for characters up to the given length. Returns {@code false} if the
   * string is too short.
   *
   * @param text The string to check.
   * @param index The index to start from.
   * @param length The length to check.
   * @return {@code true} if the string's length is at least as long as {@code index + length}.
   */
  private static boolean boundsCheck(final String text, final int index, final int length) {
    return index + length < text.length();
  }

  /**
   * Optimized insert method for adding child tokens to the given {@code token}.
   *
   * @param token The token to add {@code value} as a child.
   * @param value The token to add to {@code token}.
   */
  private static void insert(final Token token, final Token value) {
    if (token.childTokens() == null) {
      token.childTokens(Collections.singletonList(value));
      return;
    }
    if (token.childTokens().size() == 1) {
      final ArrayList<Token> list = new ArrayList<>(3);
      list.add(token.childTokens().get(0));
      list.add(value);
      token.childTokens(list);
    } else {
      token.childTokens().add(value);
    }
  }

  enum SecondPassState {
    NORMAL,
    STRING;
  }

  /**
   * Removes escaping {@code '\`} characters from a substring where the subsequent character matches a given predicate.
   *
   * @param text the input text
   * @param startIndex the starting index of the substring
   * @param endIndex the ending index of the substring
   * @param escapes the predicate to determine if an escape happened
   * @return the output escaped substring
   * @since 4.10.0
   */
  public static String unescape(final String text, final int startIndex, final int endIndex, final IntPredicate escapes) {
    int from = startIndex;

    int i = text.indexOf('\\', from);
    if (i == -1 || i >= endIndex) {
      return text.substring(from, endIndex);
    }

    final StringBuilder sb = new StringBuilder(endIndex - startIndex);
    while (i != -1 && i + 1 < endIndex) {
      if (escapes.test(text.codePointAt(i + 1))) {
        sb.append(text, from, i);
        i++;

        if (i >= endIndex) {
          from = endIndex;
          break;
        }

        final int codePoint = text.codePointAt(i);
        sb.appendCodePoint(codePoint);

        if (Character.isBmpCodePoint(codePoint)) {
          i += 1;
        } else {
          i += 2;
        }

        if (i >= endIndex) {
          from = endIndex;
          break;
        }
      } else {
        i++;
        sb.append(text, from, i);
      }

      from = i;
      i = text.indexOf('\\', from);
    }

    sb.append(text, from, endIndex);

    return sb.toString();
  }

  /**
   * Normalizing provider for tag information.
   *
   * @since 4.10.0
   */
  @ApiStatus.Internal
  public interface TagProvider {
    /**
     * Look up a tag.
     *
     * <p>Parsing exceptions must be caught and handled within this method.</p>
     *
     * @param name the tag name, pre-sanitized
     * @param trimmedArgs arguments, with the tag name trimmed off
     * @param token the token, if this tag is from a parse stream
     * @return a tag
     * @since 4.10.0
     */
    @Nullable Tag resolve(final @NotNull String name, final @NotNull List<? extends Tag.Argument> trimmedArgs, final @Nullable Token token);

    /**
     * Resolve by sanitized name.
     *
     * @param name sanitized name
     * @return a tag, if any is available
     * @since 4.10.0
     */
    default @Nullable Tag resolve(final @NotNull String name) {
      return this.resolve(name, Collections.emptyList(), null);
    }

    /**
     * Resolve by node.
     *
     * @param node tag node
     * @return a tag, if any is available
     * @since 4.10.0
     */
    default @Nullable Tag resolve(final @NotNull TagNode node) {
      return this.resolve(
        sanitizePlaceholderName(node.name()),
        node.parts().subList(1, node.parts().size()),
        node.token()
      );
    }

    /**
     * Sanitize placeholder names.
     *
     * <p>This makes all placeholder names lower-case.</p>
     *
     * @param name the raw name
     * @return a sanitized name
     * @since 4.10.0
     */
    static @NotNull String sanitizePlaceholderName(final @NotNull String name) {
      return name.toLowerCase(Locale.ROOT);
    }
  }
}
