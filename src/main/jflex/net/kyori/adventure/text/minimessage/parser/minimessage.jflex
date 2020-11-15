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

import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
%%

%class MiniMessageLexer
%function zzScan
%type Token
%public
%final
%unicode
%ignorecase
%debug
%line
%column

%state TAG
%state PARAM
%state QUOTED
%state STRING

%{
    private final StringBuffer string = new StringBuffer();
    private final List<Token> tokens = new ArrayList<>();
    private String input = "error";

    public MiniMessageLexer(String input) {
      this(new StringReader(input));
      this.input = input;
    }

    public void clean() {
        tokens.removeIf(t -> t.value().length() == 0);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public String getString() {
        String s = string.toString();
        string.setLength(0);
        return s;
    }

    public List<Token> scan() throws IOException {
        try {
            while (zzScan() != null);
        } catch (ParsingException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append(ex.getMessage()).append("\n");
            msg.append(input).append("\n");
            msg.append(padding(ex.getColumn())).append("^--- HERE");
            ex.setMessage(msg.toString());
            throw ex;
        }
        tokens.removeIf(t -> t.value().length() == 0);
        return tokens;
    }

    private String padding(int length) {
        if (length > 0) {
            char[] array = new char[length];
            Arrays.fill(array, ' ');
            return new String(array);
        }
        return "";
    }
%}

openTagStart = <
closeTagStart = <\/
tagEnd = >

escapedOpenTagStart = \\<
escapedCloseTagStart = \\<\/
escapedTagEnd = \\>

identifier = [a-zA-Z0-9_\-#\./ ]

paramSeperator = :

quote = '|\"
escapedQuote = \\'|\\\"

%%

<YYINITIAL> {
  {escapedOpenTagStart}   { string.setLength(0); string.append("<"); yybegin(STRING); }
  {escapedCloseTagStart}  { string.setLength(0); string.append("</"); yybegin(STRING); }
  {openTagStart}          { yybegin(TAG); string.setLength(0); tokens.add(new Token(TokenType.OPEN_TAG_START)); }
  {closeTagStart}         { yybegin(TAG); string.setLength(0); tokens.add(new Token(TokenType.CLOSE_TAG_START)); }
  [^]                     { string.setLength(0); string.append(yytext()); yybegin(STRING); }
}

<TAG> {
  {paramSeperator}        { yybegin(PARAM); tokens.add(new Token(TokenType.NAME, getString())); tokens.add(new Token(TokenType.PARAM_SEPARATOR)); }
  {tagEnd}                { yybegin(YYINITIAL); tokens.add(new Token(TokenType.NAME, getString())); tokens.add(new Token(TokenType.TAG_END)); }
  {identifier}            { string.append(yytext()); }
  [^]                     { throw new ParsingException("Illegal character '" + yytext() + "'. Only alphanumeric + ._-#/ are allowed as token names", yycolumn); }
}

<PARAM> {
  {paramSeperator}        { tokens.add(new Token(getString())); tokens.add(new Token(TokenType.PARAM_SEPARATOR)); }
  {tagEnd}                { yybegin(YYINITIAL); tokens.add(new Token(getString())); tokens.add(new Token(TokenType.TAG_END)); }
  {quote}                 { yybegin(QUOTED); tokens.add(new Token(TokenType.QUOTE_START)); }
  {identifier}            { string.append(yytext()); }
  [^]                     { throw new ParsingException("Illegal character '" + yytext() + "'. Only alphanumeric + ._-#/ and spaces are allowed as params", yycolumn); }
}

<QUOTED> {
  {escapedQuote}          { string.append(yytext().substring(1)); }
  {quote}                 { yybegin(PARAM); tokens.add(new Token(getString())); tokens.add(new Token(TokenType.QUOTE_END)); }
  [^]                     { string.append(yytext()); }
}

<STRING> {
  {escapedOpenTagStart}   { string.append("<"); }
  {escapedCloseTagStart}  { string.append("</"); }
  {escapedTagEnd}         { string.append(">"); }
  {tagEnd}                { yybegin(YYINITIAL); tokens.add(new Token(getString())); tokens.add(new Token(TokenType.TAG_END)); }
  {closeTagStart}         { yybegin(TAG); tokens.add(new Token(getString())); tokens.add(new Token(TokenType.CLOSE_TAG_START)); }
  {openTagStart}          { yybegin(TAG); tokens.add(new Token(getString())); tokens.add(new Token(TokenType.OPEN_TAG_START)); }
  \\n                     { string.append('\n'); }
  [^]                     { string.append(yytext()); }
  <<EOF>>                 { yybegin(YYINITIAL); tokens.add(new Token(getString())); }
}

/* error fallback */
[^]                       { throw new ParsingException("Illegal character '" + yytext() + "'", yycolumn); }
