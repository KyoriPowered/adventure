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

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.minimessage.parser.gen.Token;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class Element {

  private final Token token;

  private Element(final @NonNull Token token) {
    this.token = token;
  }

  public @NonNull Token getToken() {
    return this.token;
  }

  public static final class RawTextElement extends Element {

    private final String value;

    public RawTextElement(final @NonNull Token token, final @NonNull String value) {
      super(token);
      this.value = value;
    }

    public @NonNull String getValue() {
      return this.value;
    }

    @Override
    public String toString() {
      return "RawTextComponent{" +
          "value='" + this.value + '\'' +
          '}';
    }
  }

  public static abstract class TagElement extends Element {

    private final List<TagPart> parts = new ArrayList<>();

    private TagElement(final @NonNull Token token) {
      super(token);
    }

    public @NonNull List<TagPart> getParts() {
      return this.parts;
    }
  }

  public static final class TagPart {

    private final Token token;
    private final String value;

    public TagPart(final @NonNull Token token, final @NonNull String value) {
      this.token = token;
      this.value = value;
    }

    public @NonNull Token getToken() {
      return this.token;
    }

    public @NonNull String getValue() {
      return this.value;
    }
  }

  public static final class OpenTagElement extends TagElement {

    public OpenTagElement(final @NonNull Token token) {
      super(token);
    }

    @Override
    public String toString() {
      return "OpenTagComponent{" +
          "parts=" + this.getParts() +
          '}';
    }
  }

  public static final class CloseTagElement extends TagElement {

    public CloseTagElement(final @NonNull Token token) {
      super(token);
    }

    @Override
    public String toString() {
      return "CloseTagComponent{" +
          "parts=" + this.getParts() +
          '}';
    }
  }
}
