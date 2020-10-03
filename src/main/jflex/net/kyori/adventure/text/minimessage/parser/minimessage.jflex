package net.kyori.adventure.text.minimessage.parser;

import net.kyori.adventure.text.minimessage.tokens.MiniMessageToken;import net.kyori.adventure.text.minimessage.tokens.Token;
%%

%class MiniMessageLexer
%function scan
%type Token
%public
%final
%unicode
%ignorecase
//%debug
//%line
//%column

%state STRING
%state TOKEN
%state TOKEN_CLOSE
%state EXPECT_END

%{
  private StringBuffer string = new StringBuffer();

  public MiniMessageLexer() {
    this(null);
  }
%}

tagStart = <
tagEnd = >
tagClose = \/
tagSeperator = :

color = (BLACK)|(DARK_BLUE)|(DARK_GREEN)|(DARK_AQUA)|(DARK_RED)|(DARK_PURPLE)|(GOLD)|(GRAY)|(DARK_GRAY)|(BLUE)|(GREEN)|(AQUA)|(RED)|(LIGHT_PURPLE)|(YELLOW)|(WHITE)

%%

<YYINITIAL> {
    {tagStart}                     { yybegin(TOKEN); }
    [^]                            { string.setLength(0); string.append(yytext()); yybegin(STRING); }
}

<TOKEN> {
    {color}                        { yybegin(EXPECT_END); return new MiniMessageToken.Color(yytext(), false); }
    {tagClose}                     { yybegin(TOKEN_CLOSE); }
}

<TOKEN_CLOSE> {
    {color}                        { yybegin(EXPECT_END); return new MiniMessageToken.Color(yytext(), true); }
}

<EXPECT_END> {
    {tagEnd}                       { yybegin(YYINITIAL); }
    [^]                            { throw new Error("Illegal character '" + yytext() + "'. Expected tagEnd"); }
}

<STRING> {
    {tagStart}                     { yybegin(TOKEN); return new MiniMessageToken.Text(string.toString()); }
    \\n                            { string.append('\n'); }

    [^\\n<]+                       { string.append(yytext()); }
    <<EOF>>                        { yybegin(YYINITIAL); return new MiniMessageToken.Text(string.toString()); }
}

/* error fallback */
[^]   { throw new Error("Illegal character '" + yytext() + "'"); }
