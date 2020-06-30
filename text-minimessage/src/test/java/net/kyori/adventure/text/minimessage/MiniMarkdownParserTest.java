/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage;

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
    String expected = "<underlined>underline</underlined>";

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
    String expected = "<italic>italic</italic><underlined>underline</underlined><bold>bold</bold>";

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
    String expected = "<italic>a</italic><underlined>a</underlined>";

    String output = MiniMarkdownParser.parse(input);

    assertEquals(expected, output);
  }

  @Test
  public void testCrossedWithSpace() {
    String input = "*a~~ *a~~";
    String expected = "<italic>a<underlined> </italic>a</underlined>";

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
