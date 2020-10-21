package net.kyori.adventure.text.minimessage.tokens;

import net.kyori.adventure.text.minimessage.parser.MiniMessageLexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Parser {

    private Map<String, TokenHandler> handlers;

    public void parse(String msg) throws IOException {
        MiniMessageLexer lexer = new MiniMessageLexer(msg);
        lexer.scan();
        lexer.clean();
        List<Token> tokens = lexer.getTokens();
        parse(tokens);
    }

    public void parse(List<Token> tokens) {

        Stack<TokenHandler> handlers = new Stack<>();
        // we need some kind of data structure to handle components
        // one single stack/(de)queue/whatever should be enough
        // it needs to:
        // * be able to handle priorities (so later added color override earlier added color)
        // * be able to pop by name (pop yellow even tho it was not the last one?)
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            switch (token.getType()) {
                case OPEN_TAG_START:
                    // next has to be name
                    Token name = tokens.get(++i);
                    if (name.getType() != TokenType.NAME) {
                        throw new ParsingException("Expected name after open tag, but got " + name, -1);
                    }
                    // after that, we get a param seperator or the end
                    Token paramOrEnd = tokens.get(++i);
                    if (paramOrEnd.getType() == TokenType.PARAM_SEPARATOR) {
                        // we need to handle params, so read till end of tag
                        List<Token> inners = new ArrayList<>();
                        Token next;
                        while ((next = tokens.get(++i)).getType() != TokenType.TAG_END) {
                            inners.add(next);
                        }
                        System.out.println("got start of " + name.getValue() + " with params " + inners);
                    } else if (paramOrEnd.getType() == TokenType.TAG_END) {
                        // we finished

                        System.out.println("got start of " + name.getValue());
                    } else {
                        throw new ParsingException("Expected tag end or param seperator after tag name, but got " + paramOrEnd, -1);
                    }
                    break;
                case CLOSE_TAG_START:
                    // next has to be name
                    name = tokens.get(++i);
                    if (name.getType() != TokenType.NAME) {
                        throw new ParsingException("Expected name after close tag start, but got " + name, -1);
                    }
                    // after that, we just want end
                    Token end = tokens.get(++i);
                    if (end.getType() == TokenType.TAG_END) {
                        // we finished, gotta remove name out of the stack
                        System.out.println("got end of " + name.getValue());
                    } else {
                        throw new ParsingException("Expected tag end after tag name, but got " + end, -1);
                    }
                    break;
                case STRING:
                    System.out.println("got: " + token.getValue());
                    break;
                case TAG_END:
                case PARAM_SEPARATOR:
                case QUOTE_START:
                case QUOTE_END:
                case NAME:
                    throw new ParsingException("Unexpected token " + token, -1);
            }
        }
    }
}
