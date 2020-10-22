package net.kyori.adventure.text.minimessage.parser;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Parser {

    private Map<String, Transformation> handlers;
    private TransformationRegistry registry = new TransformationRegistry();

    public void parse(String msg) throws IOException {
        MiniMessageLexer lexer = new MiniMessageLexer(msg);
        lexer.scan();
        lexer.clean();
        List<Token> tokens = lexer.getTokens();
        parse(tokens);
    }

    public Component parse(List<Token> tokens) {
        final TextComponent.Builder parent = Component.text();
        ArrayDeque<Transformation> transformations = new ArrayDeque<>();

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

                        Transformation transformation = registry.get(name.getValue(), inners);
                        System.out.println("got start of " + name.getValue() + " with params " + inners + " -> " + transformation);
                        if (transformation == null) {
                            // TODO unknown tag -> turn into string
                            System.out.println("no transformation found");
                        } else {
                            transformations.addLast(transformation);
                        }
                    } else if (paramOrEnd.getType() == TokenType.TAG_END) {
                        // we finished
                        Transformation transformation = registry.get(name.getValue(), Collections.emptyList());
                        System.out.println("got start of " + name.getValue() + " -> " + transformation);
                        if (transformation == null) {
                            // TODO unknown tag -> turn into string
                            System.out.println("no transformation found");
                        } else {
                            transformations.addLast(transformation);
                        }
                    } else {
                        throw new ParsingException("Expected tag end or param separator after tag name, but got " + paramOrEnd, -1);
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
                        transformations.removeIf(t -> t.name().equals(name.getValue()));
                    } else {
                        throw new ParsingException("Expected tag end after tag name, but got " + end, -1);
                    }
                    break;
                case STRING:
                    System.out.println("got: " + token.getValue() + " with transformations " + transformations);
                    Component current = Component.text(token.getValue());

                    for (Transformation transformation : transformations) {
                        System.out.println("applying " + transformation);
                        current = transformation.apply(current);
                    }

                    parent.append(current);
                    break;
                case TAG_END:
                case PARAM_SEPARATOR:
                case QUOTE_START:
                case QUOTE_END:
                case NAME:
                    throw new ParsingException("Unexpected token " + token, -1);
            }
        }

        // optimization, ignore empty parent
        final TextComponent comp = parent.build();
        if (comp.content().equals("") && comp.children().size() == 1) {
            return comp.children().get(0);
        } else {
            return comp;
        }
    }
}
