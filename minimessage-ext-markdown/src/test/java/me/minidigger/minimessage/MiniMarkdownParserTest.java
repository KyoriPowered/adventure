package me.minidigger.minimessage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiniMarkdownParserTest {

    @Test
    public void testBold() {
        String input = "**bold**";
        String expected = "<bold>bold</bold>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testBold2() {
        String input = "__bold__";
        String expected = "<bold>bold</bold>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testItalic() {
        String input = "*italic*";
        String expected = "<italic>italic</italic>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testItalic2() {
        String input = "_italic_";
        String expected = "<italic>italic</italic>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testUnderline() {
        String input = "~~underline~~";
        String expected = "<underline>underline</underline>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testBoldWithSpaces() {
        String input = "AaA** bold **AaA";
        String expected = "AaA<bold> bold </bold>AaA";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testMixed() {
        String input = "*italic*~~underline~~**bold**";
        String expected = "<italic>italic</italic><underline>underline</underline><bold>bold</bold>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testMushedTogether() {
        String input = "*a***a**";
        String expected = "<italic>a</italic><bold>a</bold>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testWithFake() {
        String input = "*a~*a~";
        String expected = "<italic>a~</italic>a~";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testCrossed() {
        String input = "*a~~*a~~";
        String expected = "<italic>a</italic><underline>a</underline>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testCrossedWithSpace() {
        String input = "*a~~ *a~~";
        String expected = "<italic>a<underline> </italic>a</underline>";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testNoEnd() {
        String input = "*a";
        String expected = "*a";

        String output = MiniMarkdownParser.parse(input);

        assertEquals(expected, output);
    }

    @Test
    public void testStripMixed() {
        String input = "*italic*~~underline~~**bold**";
        String expected = "italicunderlinebold";

        String output = MiniMarkdownParser.stripMarkdown(input);

        assertEquals(expected, output);
    }

    @Test
    public void testStripMushedTogether() {
        String input = "*a***a**";
        String expected = "aa";

        String output = MiniMarkdownParser.stripMarkdown(input);

        assertEquals(expected, output);
    }

    @Test
    public void testStripWithFake() {
        String input = "*a~*a~";
        String expected = "a~a~";

        String output = MiniMarkdownParser.stripMarkdown(input);

        assertEquals(expected, output);
    }

    @Test
    public void testStripCrossed() {
        String input = "*a~~*a~~";
        String expected = "aa";

        String output = MiniMarkdownParser.stripMarkdown(input);

        assertEquals(expected, output);
    }

    @Test
    public void testStripCrossedWithSpace() {
        String input = "*a~~ *a~~";
        String expected = "a a";

        String output = MiniMarkdownParser.stripMarkdown(input);

        assertEquals(expected, output);
    }

    @Test
    public void testStripNoEnd() {
        String input = "*a";
        String expected = "*a";

        String output = MiniMarkdownParser.stripMarkdown(input);

        assertEquals(expected, output);
    }
}
