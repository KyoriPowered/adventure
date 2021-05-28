/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.parser.node.RootNode;
import net.kyori.adventure.text.minimessage.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.parser.node.TemplateNode;
import net.kyori.adventure.text.minimessage.parser.node.TextNode;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Handles parsing a string into a tree of tokens and then into a tree of nodes.
 *
 * @since 4.2.0
 */
public final class TokenParser {

  private TokenParser() {
  }

  // TODO remove - easy test harness

  /**
   * Remove me.
   *
   * @param args blah
   * @since 4.2.0
   */
  public static void main(final String[] args) {
    final List<String> list = Arrays.asList(
      "<gray><<yellow>TEST<gray>> Woooo << double <3",
      "<red>ONE<two><blue>THREE<four><five>",
      "<hover:show_item:'minecraft:stone':5>test",
      "<yellow>Woo: <gradient:red:blue:green:yellow:red>||||||||||||||||||||||||||||||||||||||||||||||||||||||</gradient>!",
      "<yellow>Woo: <rainbow>||||||||||||||||||||||||</rainbow>!",
      "Click <yellow><pre><insert:test>this</pre> to <red>insert!",
      "Click <yellow><insert:test>this<rainbow> wooo<reset> to insert!",
      "<dark_gray>»<gray> To download it from the internet, <click:open_url:https://www.google.com><hover:show_text:'<green>/!\\\\ install it from \\'Options/ResourcePacks\\' in your game'><green><bold>CLICK HERE</bold></hover></click>",
      "<yellow><test> random <gradient:red:blue:green><bold>stranger</gradient></bold><click:run_command:test command><underlined><red>click here</click><blue> to <rainbow><b>FEEL</rainbow></underlined> it",
      "<gray>\\<<yellow><player><gray>> <reset><pre><message></pre>",
      "<gray><<yellow><player><gray>> <reset><pre><message></pre>",
      "<gray><<yellow><player><gray>> <reset><pre><message>",
      "<hover:show_text:'<blue>Hello</blue>'<red>TEST</red></hover><click:suggest_command:'/msg <user>'><user></click> <reset>: <hover:show_text:'<date>'><message></hover>",
      "<red is already created! Try different name! :)",
      "<lang:test:'\"\"'>",
      "<lang:test:'\\'\\''>",
      "<lang:test:\"''\">",
      "<lang:test:\"\\\"\\\"\">",
      "<gray><arg1></gray><red><arg2></red><blue><arg3></blue> <green><arg4>",
      "<<<<>>><><><><><>>>><<<>>>>><red><><><><><><><><<<<<reset>>>>>>><<<<><<<<<>>>>>><>>>",
      "<<'\\''\\<'>'><3'<>< '>",
      "<pre><<'\\''\\<'>'><3'<>< '></pre</pre ></ pre></pre>",
      "<<'\\''\\<<reset>'>'><3'<>< '>"
    );

    final TransformationRegistry registry = new TransformationRegistry();
    for(final String s : list) {
      System.out.println();
      System.out.println();
      System.out.println(s);
      System.out.println();
      final ElementNode el = parse(registry, new HashMap<>(), s);
      System.out.println(el);
    }
  }

  /**
   * Parse a minimessage string into a tree of nodes.
   *
   * @param message the minimessage string to parse
   * @return the root of the resulting tree
   * @since 4.2.0
   */
  public static ElementNode parse(final @NonNull TransformationRegistry registry, final @NonNull Map<String, Template.ComponentTemplate> templates, final @NonNull String message) {
    final List<Token> tokens = parseFirstPass(message);
    parseSecondPass(message, tokens);

    return buildTree(registry, templates, tokens, message);
  }

  /*
   * First pass over the text identifies valid tags and text blocks.
   */
  @SuppressWarnings("DuplicatedCode")
  private static List<Token> parseFirstPass(final String message) {
    final ArrayList<Token> elements = new ArrayList<>();

    FirstPassState state = FirstPassState.NORMAL;
    // If the current state is escaped then the next character is skipped
    boolean escaped = false;

    int currentTokenEnd = 0;
    // Marker is the starting index for the current token
    int marker = -1;
    char currentStringChar = 0;

    final int length = message.length();
    for(int i = 0; i < length; i++) {
      final int codePoint = message.codePointAt(i);
      if(!Character.isBmpCodePoint(codePoint)) {
        i++;
      }

      if(!escaped) {
        if(codePoint == '\\') {
          escaped = true;
          continue;
        }
      } else {
        escaped = false;
        continue;
      }

      switch(state) {
        case NORMAL:
          if(codePoint == '<') {
            // Possibly a tag
            marker = i;
            state = FirstPassState.TAG;
          }
          break;
        case TAG:
          switch(codePoint) {
            case '>':
              if(i == marker + 1) {
                // This is empty, <>, so it's not a tag
                state = FirstPassState.NORMAL;
                break;
              }

              // We found a tag
              if(currentTokenEnd != marker) {
                // anything not matched up to this point is normal text
                elements.add(new Token(currentTokenEnd, marker, TokenType.TEXT));
              }
              currentTokenEnd = i + 1;

              // closing tags start with </
              TokenType thisType = TokenType.OPEN_TAG;
              if(boundsCheck(message, marker, 1) && message.charAt(marker + 1) == '/') {
                thisType = TokenType.CLOSE_TAG;
              }
              elements.add(new Token(marker, currentTokenEnd, thisType));

              // <pre> tags put us into a state where we don't parse anything
              if(message.regionMatches(marker, "<pre>", 0, 5)) {
                state = FirstPassState.PRE;
              } else {
                state = FirstPassState.NORMAL;
              }
              break;
            case '<':
              // This isn't a tag, but we can re-start looking here
              marker = i;
              break;
            case '\'':
            case '"':
              state = FirstPassState.STRING;
              currentStringChar = (char) codePoint;
              break;
          }
          break;
        case PRE:
          if(codePoint == '<') {
            if(message.regionMatches(i, "</pre>", 0, 6)) {
              // Anything inside the <pre>...</pre> is text
              elements.add(new Token(currentTokenEnd, i, TokenType.TEXT));
              // the </pre> is still a closing tag though
              elements.add(new Token(i, i + 6, TokenType.CLOSE_TAG));
              i += 6;
              currentTokenEnd = i;
              state = FirstPassState.NORMAL;
            }
          }
          break;
        case STRING:
          if(codePoint == currentStringChar) {
            state = FirstPassState.TAG;
          }
          break;
      }
    }

    // anything left over is plain text
    if(elements.isEmpty()) {
      elements.add(new Token(0, message.length(), TokenType.TEXT));
    } else {
      final int end = elements.get(elements.size() - 1).endIndex();
      if(end != message.length()) {
        elements.add(new Token(end, message.length(), TokenType.TEXT));
      }
    }

    return elements;
  }

  enum FirstPassState {
    NORMAL,
    TAG,
    PRE,
    STRING
  }

  /*
   * Second pass over the tag tokens identifies tag parts
   */
  @SuppressWarnings("DuplicatedCode")
  private static void parseSecondPass(final String message, final List<Token> tokens) {
    for(final Token token : tokens) {
      final TokenType type = token.type();
      if(type != TokenType.OPEN_TAG && type != TokenType.CLOSE_TAG) {
        continue;
      }

      // Only look inside the tag <[/] and >
      final int startIndex = type == TokenType.OPEN_TAG ? token.startIndex() + 1 : token.startIndex() + 2;
      final int endIndex = token.endIndex() - 1;

      SecondPassState state = SecondPassState.NORMAL;
      boolean escaped = false;
      char currentStringChar = 0;

      // Marker is the starting index for the current token
      int marker = startIndex;

      for(int i = startIndex; i < endIndex; i++) {
        final int codePoint = message.codePointAt(i);
        if(!Character.isBmpCodePoint(i)) {
          i++;
        }

        if(!escaped) {
          if(codePoint == '\\') {
            escaped = true;
            continue;
          }
        } else {
          escaped = false;
          continue;
        }

        switch(state) {
          case NORMAL:
            // Values are split by : unless it's in a URL
            if(codePoint == ':') {
              if(boundsCheck(message, i, 2) && message.charAt(i + 1) == '/' && message.charAt(i + 2) == '/') {
                break;
              }
              if(marker == i) {
                // 2 colons side-by-side like <::> or <:text> or <text::text> would lead to this happening
                insert(token, new Token(i, i, TokenType.TAG_VALUE));
                marker++;
              } else {
                insert(token, new Token(marker, i, TokenType.TAG_VALUE));
                marker = i + 1;
              }
            } else if(codePoint == '\'' || codePoint == '"') {
              state = SecondPassState.STRING;
              currentStringChar = (char) codePoint;
            }
            break;
          case STRING:
            if(codePoint == currentStringChar) {
              state = SecondPassState.NORMAL;
            }
            break;
        }
      }

      // anything not matched is the final part
      if(token.childTokens() == null || token.childTokens().isEmpty()) {
        insert(token, new Token(startIndex, endIndex, TokenType.TAG_VALUE));
      } else {
        final int end = token.childTokens().get(token.childTokens().size() - 1).endIndex();
        if(end != endIndex) {
          insert(token, new Token(end + 1, endIndex, TokenType.TAG_VALUE));
        }
      }
    }
  }

  /*
   * Build a tree from the OPEN_TAG and CLOSE_TAG tokens
   */
  private static ElementNode buildTree(final @NonNull TransformationRegistry registry, final @NonNull Map<String, Template.ComponentTemplate> templates, final @NonNull List<Token> tokens, final @NonNull String message) {
    final RootNode root = new RootNode(message);
    ElementNode node = root;

    for(final Token token : tokens) {
      final TokenType type = token.type();
      switch(type) {
        case TEXT:
          node.addChild(new TextNode(node, token, message));
          break;

        case OPEN_TAG:
          final TagNode tagNode = new TagNode(node, token, message);
          if(tagNode.name().equals("reset")) {
            // <reset> tags get special treatment and don't appear in the tree
            // instead, they close all currently open tags

            // TODO <reset> tags are invalid if all closing tags are required
            node = root;
          } else if(tagNode.name().equals("pre")) {
            // <pre> tags also get special treatment and don't appear in the tree
            // anything inside <pre> is raw text, so just skip

            continue;
          } else {
            if(registry.exists(tagNode.name())) {
              node.addChild(tagNode);
              node = tagNode;
            } else if(templates.containsKey(tagNode.name())) {
              // TODO What to do if a template has multiple parts?
              node.addChild(new TemplateNode(node, token, message));
            } else {
              // tag does not exist, so treat it as text
              node.addChild(new TextNode(node, token, message));
            }
          }
          break; // OPEN_TAG

        case CLOSE_TAG:
          final List<Token> childTokens = token.childTokens();
          if(childTokens.isEmpty()) {
            throw new IllegalStateException("CLOSE_TAG token somehow has no children - " +
              "the parser should not allow this. Original text: " + message);
          }

          final ArrayList<String> closeValues = new ArrayList<>(childTokens.size());
          for(final Token childToken : childTokens) {
            closeValues.add(TagPart.unquoteAndEscape(message, childToken.startIndex(), childToken.endIndex()));
          }

          final String closeTagName = closeValues.get(0);
          if(closeTagName.equals("reset") || closeTagName.equals("pre")) {
            // These are synthetic nodes, closing them means nothing in the context of building a tree
            continue;
          }

          if(!registry.exists(closeTagName)) {
            // tag does not exist, so treat it as text
            node.addChild(new TextNode(node, token, message));
            continue;
          }

          ElementNode parentNode = node;
          while(parentNode instanceof TagNode) {
            final List<TagPart> openParts = ((TagNode) parentNode).parts();

            if(tagCloses(closeValues, openParts)) {
              final ElementNode par = parentNode.parent();
              if(par != null) {
                node = par;
              } else {
                throw new IllegalStateException("Root node matched with close tag value, this should not be possible. " +
                  "Original text: " + message);
              }
              break;
            }

            // TODO closing tag isn't closing the immediate tag, is an error if closing tags are required
            parentNode = parentNode.parent();

            if(parentNode == null) {
              // TODO dangling closing tag, is an error or not depending on strict parsing
              break;
            }
          }
          break; // CLOSE_TAG
      }
    }

    // TODO if root != node then this is an error if closing tags are required

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
    if(closeParts.size() > openParts.size()) {
      return false;
    }
    for(int i = 0; i < closeParts.size(); i++) {
      if(!closeParts.get(i).equals(openParts.get(i).value())) {
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
    if(token.childTokens() == null) {
      token.childTokens(Collections.singletonList(value));
      return;
    }
    if(token.childTokens().size() == 1) {
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
    STRING
  }

  /**
   * Removes escaping {@code '\`} characters from a substring. In general, this removes all {@code '\`} characters,
   * though the pattern {@code '\\'} will be replaced with {@code '\'}.
   *
   * @param text the input text
   * @param startIndex the starting index of the substring
   * @param endIndex the ending index of the substring
   * @return the output escaped substring
   * @since 4.2.0
   */
  public static String unescape(final String text, final int startIndex, final int endIndex) {
    int from = startIndex;

    int i = text.indexOf('\\', from);
    if(i == -1 || i >= endIndex) {
      return text.substring(from, endIndex);
    }

    final StringBuilder sb = new StringBuilder(endIndex - startIndex);
    while(i != -1 && i < endIndex) {
      sb.append(text, from, i);
      i++;
      final int codePoint = text.codePointAt(i);
      sb.appendCodePoint(codePoint);

      if(Character.isBmpCodePoint(codePoint)) {
        i += 1;
      } else {
        i += 2;
      }

      from = i;
      i = text.indexOf('\\', from);
    }

    sb.append(text, from, endIndex);

    return sb.toString();
  }
}
