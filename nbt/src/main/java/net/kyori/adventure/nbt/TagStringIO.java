/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.nbt;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

/**
 * A holder for string tag format options.
 *
 * @since 4.0.0
 */
public final class TagStringIO {
  private static final TagStringIO INSTANCE = new TagStringIO(new Builder());

  /**
   * Get an instance of {@link TagStringIO} that creates reads and writes using standard options.
   *
   * @return the basic instance
   * @since 4.0.0
   */
  public static @NotNull TagStringIO get() {
    return INSTANCE;
  }

  /**
   * Create an new builder to configure IO.
   *
   * @return a builder
   * @since 4.0.0
   */
  public static @NotNull Builder builder() {
    return new Builder();
  }

  private final boolean acceptLegacy;
  private final boolean emitLegacy;
  private final String indent;

  private TagStringIO(final @NotNull Builder builder) {
    this.acceptLegacy = builder.acceptLegacy;
    this.emitLegacy = builder.emitLegacy;
    this.indent = builder.indent;
  }

  /**
   * Read the string into a compound tag structure.
   *
   * <p>When working with untrusted input (such as from the network), users should be careful
   * to validate that the {@code input} string is of a reasonable size.</p>
   *
   * @param input Input data
   * @return this
   * @throws IOException on any syntax errors
   * @since 4.0.0
   */
  public CompoundBinaryTag asCompound(final String input) throws IOException {
    try {
      final CharBuffer buffer = new CharBuffer(input);
      final TagStringReader parser = new TagStringReader(buffer);
      parser.legacy(this.acceptLegacy);
      final CompoundBinaryTag tag = parser.compound();
      if (buffer.skipWhitespace().hasMore()) {
        throw new IOException("Document had trailing content after first CompoundTag");
      }
      return tag;
    } catch (final StringTagParseException ex) {
      throw new IOException(ex);
    }
  }

  /**
   * Get a string representation of the provided tag.
   *
   * @param input tag to serialize
   * @return serialized form
   * @throws IOException if any errors occur writing to string
   * @since 4.0.0
   */
  public String asString(final CompoundBinaryTag input) throws IOException {
    final StringBuilder sb = new StringBuilder();
    try (final TagStringWriter emit = new TagStringWriter(sb, this.indent)) {
      emit.legacy(this.emitLegacy);
      emit.writeTag(input);
    }
    return sb.toString();
  }

  /**
   * Writes a tag to in string format.
   *
   * <p>The provided {@link Writer} will remain open after reading a tag.</p>
   *
   * @param input Tag to write
   * @param dest Writer to write to
   * @throws IOException if any IO or syntax errors occur while parsing
   * @since 4.0.0
   */
  public void toWriter(final CompoundBinaryTag input, final Writer dest) throws IOException {
    try (final TagStringWriter emit = new TagStringWriter(dest, this.indent)) {
      emit.legacy(this.emitLegacy);
      emit.writeTag(input);
    }
  }

  /**
   * Builder for a SNBT I/O handler.
   *
   * @since 4.0.0
   */
  public static class Builder {
    private boolean acceptLegacy = true;
    private boolean emitLegacy = false;
    private String indent = "";

    Builder() {
    }

    /**
     * Set the indent for new levels of pretty-printing.
     *
     * <p>Providing a level of {@code 0} will disable pretty-printing</p>
     *
     * @param spaces Spaces to indent each level with
     * @return this builder
     * @since 4.0.0
     */
    public @NotNull Builder indent(final int spaces) {
      if (spaces == 0) {
        this.indent = "";
      } else if ((this.indent.length() > 0 && this.indent.charAt(0) != ' ') || spaces != this.indent.length()) {
        final char[] indent = new char[spaces];
        Arrays.fill(indent, ' ');
        this.indent = String.copyValueOf(indent);
      }
      return this;
    }

    /**
     * Set the indent for new levels of pretty-printing.
     *
     * <p>Providing a level of {@code 0} will disable pretty-printing</p>
     *
     * @param tabs Tabs to indent each level with
     * @return this builder
     * @since 4.0.0
     */
    public @NotNull Builder indentTab(final int tabs) {
      if (tabs == 0) {
        this.indent = "";
      } else if ((this.indent.length() > 0 && this.indent.charAt(0) != '\t') || tabs != this.indent.length()) {
        final char[] indent = new char[tabs];
        Arrays.fill(indent, '\t');
        this.indent = String.copyValueOf(indent);
      }
      return this;
    }

    /**
     * Configure whether or not the resulting IO configuration will accept legacy-formatted data.
     *
     * <p>The legacy format was used in versions of Minecraft prior to 1.13</p>
     *
     * <p>Be aware that because the legacy int array and modern list are ambiguous with each other,
     * the deserializer will always return legacy-format integer arrays as a list tag.</p>
     *
     * @param legacy whether to accept legacy formatting
     * @return this builder
     * @since 4.0.0
     */
    public @NotNull Builder acceptLegacy(final boolean legacy) {
      this.acceptLegacy = legacy;
      return this;
    }

    /**
     * Configure whether or not the resulting IO configuration will emit legacy-formatted data.
     *
     * @param legacy whether to emit legacy formatting
     * @return this builder
     * @since 4.0.0
     */
    public @NotNull Builder emitLegacy(final boolean legacy) {
      this.emitLegacy = legacy;
      return this;
    }

    /**
     * Create a new IO configuration from this builder.
     *
     * @return new IO configuration
     * @since 4.0.0
     */
    public @NotNull TagStringIO build() {
      return new TagStringIO(this);
    }
  }
}
