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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageLexerTest {

    @Test
    public void test() throws Exception {
        test("<red>This is a test</red><yellow>Wooo<#112233>hex!</#112233><color:blue>Named color</color>");
        test("<hover:show_text:'<red>test'>TEST</hover>");
        test("<rainbow><treerev> <click:open_url:'https://github.com'>https://github.com</click></rainbow>");
    }

    private void test(String input) throws Exception {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Testing: " + input);
        MiniMessageLexer lexer = new MiniMessageLexer(input);
        lexer.scan();
        lexer.clean();
        List<Token> tokens = lexer.getTokens();

        StringBuilder result = new StringBuilder();
        StringBuilder split = new StringBuilder();
        StringBuilder types = new StringBuilder();
        for (Token token : tokens) {
            result.append(token.getValue());

            int length = Math.max(token.getValue().length(), token.getType().name().length());

            split.append(token.getValue()).append(padding(length - token.getValue().length())).append(" ");
            types.append(token.getType().name()).append(padding(length - token.getType().name().length())).append(" ");
        }

        System.out.println("Result:  " + result.toString());
        System.out.println("Split:   " + split.toString());
        System.out.println("Types:   " + types.toString());
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        assertEquals(result.toString(), input);
    }

    private String padding(int length) {
        if (length > 0) {
            char[] array = new char[length];
            Arrays.fill(array, ' ');
            return new String(array);
        }
        return "";
    }
}
