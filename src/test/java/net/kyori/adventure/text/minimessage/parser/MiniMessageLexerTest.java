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

import net.kyori.adventure.text.minimessage.tokens.Token;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class MiniMessageLexerTest {

    @Test
    public void test() throws Exception {
        test("<RED>This is a test</red><yellow>Wooo<#112233>hex!</#112233>");
        test("<hover:show_text:'<red>test'>TEST</hover>");
    }

    private void test(String input) throws Exception {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Testing: " + input);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        MiniMessageLexer lexer = new MiniMessageLexer(new StringReader(input));
        Token token;
        while((token = lexer.scan()) != null) {
            System.out.println("token: " + token);
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
