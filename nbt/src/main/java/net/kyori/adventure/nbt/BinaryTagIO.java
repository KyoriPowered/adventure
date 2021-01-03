/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Serialization operations for binary tags.
 *
 * @since 4.0.0
 */
public final class BinaryTagIO {
  private BinaryTagIO() {
  }

  static {
    BinaryTagTypes.COMPOUND.id(); // initialize tag types
  }

  /**
   * Returns {@link Writer}, used to read binary tags.
   *
   * @return binary tag reader
   * @since 4.4.0
   */
  public static @NonNull Reader reader() {
    return BinaryTagReaderImpl.INSTANCE;
  }

  /**
   * Returns {@link Writer}, used to write binary tags.
   *
   * @return binary tag writer
   * @since 4.4.0
   */
  public static @NonNull Writer writer() {
    return BinaryTagWriterImpl.INSTANCE;
  }

  /**
   * Reads a compound tag from {@code path}.
   *
   * @param path the path
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @deprecated since 4.4.0, use {@link #reader()}
   * @since 4.0.0
   */
  @Deprecated
  public static @NonNull CompoundBinaryTag readPath(final @NonNull Path path) throws IOException {
    return reader().read(path);
  }

  /**
   * Reads a compound tag from an input stream. The stream is not closed afterwards.
   *
   * @param input the input stream
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @deprecated since 4.4.0, use {@link #reader()}
   * @since 4.0.0
   */
  @Deprecated
  public static @NonNull CompoundBinaryTag readInputStream(final @NonNull InputStream input) throws IOException {
    return reader().read(input);
  }

  /**
   * Reads a compound tag from {@code path} using GZIP decompression.
   *
   * @param path the path
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @deprecated since 4.4.0, use {@link #reader()}
   * @since 4.0.0
   */
  @Deprecated
  public static @NonNull CompoundBinaryTag readCompressedPath(final @NonNull Path path) throws IOException {
    return reader().read(path, Compression.GZIP);
  }

  /**
   * Reads a compound tag from an input stream using GZIP decompression. The stream is not closed afterwards.
   *
   * @param input the input stream
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @deprecated since 4.4.0, use {@link #reader()}
   * @since 4.0.0
   */
  @Deprecated
  public static @NonNull CompoundBinaryTag readCompressedInputStream(final @NonNull InputStream input) throws IOException {
    return reader().read(input, Compression.GZIP);
  }

  /**
   * Reads a compound tag from {@code input}.
   *
   * @param input the input
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @deprecated since 4.4.0, use {@link #reader()}
   * @since 4.0.0
   */
  @Deprecated
  public static @NonNull CompoundBinaryTag readDataInput(final @NonNull DataInput input) throws IOException {
    return reader().read(input);
  }

  /**
   * Writes a compound tag to {@code path}.
   *
   * @param tag the compound tag
   * @param path the path
   * @throws IOException if an exception was encountered while writing the compound tag
   * @deprecated since 4.4.0, use {@link #writer()}
   * @since 4.0.0
   */
  @Deprecated
  public static void writePath(final @NonNull CompoundBinaryTag tag, final @NonNull Path path) throws IOException {
    writer().write(tag, path);
  }

  /**
   * Writes a compound tag to an output stream. The output stream will not be closed.
   *
   * @param tag the compound tag
   * @param output the output stream
   * @throws IOException if an exception was encountered while writing the compound tag
   * @deprecated since 4.4.0, use {@link #writer()}
   * @since 4.0.0
   */
  @Deprecated
  public static void writeOutputStream(final @NonNull CompoundBinaryTag tag, final @NonNull OutputStream output) throws IOException {
    writer().write(tag, output);
  }

  /**
   * Writes a compound tag to {@code path} using GZIP compression.
   *
   * @param tag the compound tag
   * @param path the path
   * @throws IOException if an exception was encountered while writing the compound tag
   * @deprecated since 4.4.0, use {@link #writer()}
   * @since 4.0.0
   */
  @Deprecated
  public static void writeCompressedPath(final @NonNull CompoundBinaryTag tag, final @NonNull Path path) throws IOException {
    writer().write(tag, path, Compression.GZIP);
  }

  /**
   * Writes a compound tag to an output stream using GZIP compression. The output stream is not closed afterwards.
   *
   * @param tag the compound tag
   * @param output the output stream
   * @throws IOException if an exception was encountered while writing the compound tag
   * @deprecated since 4.4.0, use {@link #writer()}
   * @since 4.0.0
   */
  @Deprecated
  public static void writeCompressedOutputStream(final @NonNull CompoundBinaryTag tag, final @NonNull OutputStream output) throws IOException {
    writer().write(tag, output, Compression.GZIP);
  }

  /**
   * Writes a compound tag to {@code output}.
   *
   * @param tag the compound tag
   * @param output the output
   * @throws IOException if an exception was encountered while writing the compound tag
   * @deprecated since 4.4.0, use {@link #writer()}
   * @since 4.0.0
   */
  @Deprecated
  public static void writeDataOutput(final @NonNull CompoundBinaryTag tag, final @NonNull DataOutput output) throws IOException {
    writer().write(tag, output);
  }

  /**
   * {@link CompoundBinaryTag} reader.
   *
   * @since 4.4.0
   */
  public interface Reader {
    /**
     * Reads a binary tag from {@code path}.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #read(Path, Compression)}.</p>
     *
     * @param path the path
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default @NonNull CompoundBinaryTag read(final @NonNull Path path) throws IOException {
      return this.read(path, Compression.NONE);
    }

    /**
     * Reads a binary tag from {@code path} with a {@code compression} type.
     *
     * @param path the path
     * @param compression the compression type
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    @NonNull CompoundBinaryTag read(final @NonNull Path path, final @NonNull Compression compression) throws IOException;

    /**
     * Reads a binary tag from {@code input}.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #read(InputStream, Compression)}.</p>
     *
     * @param input the input stream
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default @NonNull CompoundBinaryTag read(final @NonNull InputStream input) throws IOException {
      return this.read(input, Compression.NONE);
    }

    /**
     * Reads a binary tag from {@code input} with a {@code compression} type.
     *
     * @param input the input stream
     * @param compression the compression type
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    @NonNull CompoundBinaryTag read(final @NonNull InputStream input, final @NonNull Compression compression) throws IOException;

    /**
     * Reads a binary tag from {@code input}.
     *
     * @param input the input stream
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    @NonNull CompoundBinaryTag read(final @NonNull DataInput input) throws IOException;

    /**
     * Reads a binary tag, with a name, from {@code path}.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #readNamed(Path, Compression)}.</p>
     *
     * @param path the path
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default Map.@NonNull Entry<String, CompoundBinaryTag> readNamed(final @NonNull Path path) throws IOException {
      return this.readNamed(path, Compression.NONE);
    }

    /**
     * Reads a binary tag, with a name, from {@code path}.
     *
     * @param path the path
     * @param compression the compression type
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    Map.@NonNull Entry<String, CompoundBinaryTag> readNamed(final @NonNull Path path, final @NonNull Compression compression) throws IOException;

    /**
     * Reads a binary tag, with a name, from {@code input}.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #readNamed(InputStream, Compression)}.</p>
     *
     * @param input the input stream
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default Map.@NonNull Entry<String, CompoundBinaryTag> readNamed(final @NonNull InputStream input) throws IOException {
      return this.readNamed(input, Compression.NONE);
    }

    /**
     * Reads a binary tag, with a name, from {@code input}.
     *
     * @param input the input stream
     * @param compression the compression type
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    Map.@NonNull Entry<String, CompoundBinaryTag> readNamed(final @NonNull InputStream input, final @NonNull Compression compression) throws IOException;

    /**
     * Reads a binary tag, with a name, from {@code input}.
     *
     * @param input the input
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    Map.@NonNull Entry<String, CompoundBinaryTag> readNamed(final @NonNull DataInput input) throws IOException;
  }

  /**
   * {@link CompoundBinaryTag} writer.
   *
   * @since 4.4.0
   */
  public interface Writer {
    /**
     * Writes a binary tag to {@code path} with a {@code compression} type.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #write(CompoundBinaryTag, Path, Compression)}.</p>
     *
     * @param path the path
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default void write(final @NonNull CompoundBinaryTag tag, final @NonNull Path path) throws IOException {
      this.write(tag, path, Compression.NONE);
    }

    /**
     * Writes a binary tag to {@code path} with a {@code compression} type.
     *
     * @param path the path
     * @param compression the compression type
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void write(final @NonNull CompoundBinaryTag tag, final @NonNull Path path, final @NonNull Compression compression) throws IOException;

    /**
     * Writes a binary tag to {@code output}.
     *
     * <p>This is the equivalent of passing {@link Compression#NONE} as the second parameter to {@link #write(CompoundBinaryTag, OutputStream, Compression)}.</p>
     *
     * @param output the output stream
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default void write(final @NonNull CompoundBinaryTag tag, final @NonNull OutputStream output) throws IOException {
      this.write(tag, output, Compression.NONE);
    }

    /**
     * Writes a binary tag to {@code output} with a {@code compression} type.
     *
     * @param output the output stream
     * @param compression the compression type
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void write(final @NonNull CompoundBinaryTag tag, final @NonNull OutputStream output, final @NonNull Compression compression) throws IOException;

    /**
     * Writes a binary tag to {@code output}.
     *
     * @param output the output
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void write(final @NonNull CompoundBinaryTag tag, final @NonNull DataOutput output) throws IOException;

    /**
     * Writes a binary tag, with a name, to {@code path}.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #write(CompoundBinaryTag, Path, Compression)}.</p>
     *
     * @param path the path
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default void writeNamed(final Map.@NonNull Entry<String, CompoundBinaryTag> tag, final @NonNull Path path) throws IOException {
      this.writeNamed(tag, path, Compression.NONE);
    }

    /**
     * Writes a binary tag, with a name, to {@code path} with a {@code compression} type.
     *
     * @param path the path
     * @param compression the compression type
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void writeNamed(final Map.@NonNull Entry<String, CompoundBinaryTag> tag, final @NonNull Path path, final @NonNull Compression compression) throws IOException;

    /**
     * Writes a binary tag, with a name, to {@code output}.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #write(CompoundBinaryTag, OutputStream, Compression)}.</p>
     *
     * @param output the output stream
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default void writeNamed(final Map.@NonNull Entry<String, CompoundBinaryTag> tag, final @NonNull OutputStream output) throws IOException {
      this.writeNamed(tag, output, Compression.NONE);
    }

    /**
     * Writes a binary tag, with a name, to {@code output} with a {@code compression} type.
     *
     * @param output the output stream
     * @param compression the compression type
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void writeNamed(final Map.@NonNull Entry<String, CompoundBinaryTag> tag, final @NonNull OutputStream output, final @NonNull Compression compression) throws IOException;

    /**
     * Writes a binary tag, with a name, to {@code output}.
     *
     * @param output the output
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void writeNamed(final Map.@NonNull Entry<String, CompoundBinaryTag> tag, final @NonNull DataOutput output) throws IOException;
  }

  /**
   * Compression types.
   *
   * @since 4.4.0
   */
  public static abstract class Compression {
    /**
     * No compression.
     *
     * @since 4.4.0
     */
    public static final Compression NONE = new Compression() {
      @Override
      @NonNull InputStream decompress(final @NonNull InputStream is) {
        return is;
      }

      @Override
      @NonNull OutputStream compress(final @NonNull OutputStream os) {
        return os;
      }

      @Override
      public String toString() {
        return "Compression.NONE";
      }
    };
    /**
     * <a href="https://en.wikipedia.org/wiki/Gzip">GZIP</a> compression.
     *
     * @since 4.4.0
     */
    public static final Compression GZIP = new Compression() {
      @Override
      @NonNull InputStream decompress(final @NonNull InputStream is) throws IOException {
        return new GZIPInputStream(is);
      }

      @Override
      @NonNull OutputStream compress(final @NonNull OutputStream os) throws IOException {
        return new GZIPOutputStream(os);
      }

      @Override
      public String toString() {
        return "Compression.GZIP";
      }
    };

    abstract @NonNull InputStream decompress(final @NonNull InputStream is) throws IOException;

    abstract @NonNull OutputStream compress(final @NonNull OutputStream os) throws IOException;
  }
}
