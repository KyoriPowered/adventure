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

import net.kyori.adventure.text.minimessage.Context;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageLexerTest {

  @Test
  void test() throws Exception {
//    this.test("<red>This is a test</red><yellow>Wooo<#112233>hex!</#112233><color:blue>Named color</color>");
//    this.test("<hover:show_text:'<red>test'>TEST</hover>");
//    this.test("<rainbow><treerev> <click:open_url:'https://github.com'>https://github.com</click></rainbow>");
  }

  /*
  private void test(final String input) throws Exception {
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    System.out.println("Testing: " + input);
    final MiniMessageLexer lexer = new MiniMessageLexer(input, Context.of(false, null, null));
    lexer.scan();
    lexer.clean();
    final List<Token> tokens = lexer.getTokens();

    final StringBuilder result = new StringBuilder();
    final StringBuilder split = new StringBuilder();
    final StringBuilder types = new StringBuilder();
    for(final Token token : tokens) {
      result.append(token.value());

      final int length = Math.max(token.value().length(), token.type().name().length());

      split.append(token.value()).append(this.padding(length - token.value().length())).append(" ");
      types.append(token.type().name()).append(this.padding(length - token.type().name().length())).append(" ");
    }

    System.out.println("Result:  " + result.toString());
    System.out.println("Split:   " + split.toString());
    System.out.println("Types:   " + types.toString());
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    assertEquals(result.toString(), input);
  }

  private String padding(final int length) {
    if(length > 0) {
      final char[] array = new char[length];
      Arrays.fill(array, ' ');
      return new String(array);
    }
    return "";
  }
   */
}
