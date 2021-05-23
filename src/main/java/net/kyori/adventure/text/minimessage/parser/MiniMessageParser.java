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

import java.io.StringReader;
import java.util.List;
import net.kyori.adventure.text.minimessage.parser.gen.MiniParser;
import net.kyori.adventure.text.minimessage.parser.gen.ParseException;

public final class MiniMessageParser {

  private final MiniParser parser;

  public MiniMessageParser(final String message) {
    this.parser = new MiniParser(new StringReader(message));
  }

  public ElementNode parse() throws ParseException {
    return this.buildTree(this.parser.message());
  }

  private ElementNode buildTree(final List<Element> elements) {
    final ElementNode root = new ElementNode(null);
    ElementNode node = root;

    for(final Element element : elements) {
      if(element instanceof Element.RawTextElement) {
        node.getChildren().add(new ElementNode.RawTextNode(node, ((Element.RawTextElement) element).getValue()));
      } else if(element instanceof Element.OpenTagElement) {
        final ElementNode.TagNode tagNode = new ElementNode.TagNode(node, ((Element.OpenTagElement) element).getParts());
        node.getChildren().add(tagNode);
        node = tagNode;
      } else if(element instanceof Element.CloseTagElement) {
        final List<String> parts = ((Element.CloseTagElement) element).getParts();
        if(parts.isEmpty()) {
          // TODO error or not depends on strict parsing
          continue;
        }
        final String value = parts.get(0);

        ElementNode parentNode = node;
        while(parentNode instanceof ElementNode.TagNode) {
          final String nodeValue = ((ElementNode.TagNode) parentNode).getParts().get(0);
          if(value.equals(nodeValue)) {
            node = parentNode.getParent();
            break;
          }

          // TODO closing tag isn't closing the immediate tag, is an error if closing tags are required
          parentNode = parentNode.getParent();

          if(parentNode == null) {
            // TODO dangling closing tag, is an error or not depending on strict parsing
            break;
          }
        }
      }
    }

    // TODO if root != node then this is an error if closing tags are required

    return root;
  }

  public static void main(final String[] args) throws ParseException {
    final String text = "<yellow>Woo: <hover:show_text:'This is a test'><gradient>||||||||||||||||||||||||</gradient>!";
    System.out.println(new MiniMessageParser(text).parse());
  }
}
