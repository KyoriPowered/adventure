/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import org.jspecify.annotations.NullMarked;

/**
 * Serialization operations for binary tags.
 *
 * @since 4.0.0
 */
@NullMarked
public final class BinaryTagIO {
  private BinaryTagIO() {
  }

  static {
    BinaryTagTypes.COMPOUND.id(); // initialize tag types
  }

  /**
   * Returns {@link Reader}, used to read binary tags.
   *
   * <p>There is a maximum depth of {@code 512} nested tags allowed, but no limit for the amount of contained data.</p>
   *
   * @return binary tag reader
   * @since 4.4.0
   */
  public static Reader unlimitedReader() {
    return BinaryTagReaderImpl.UNLIMITED;
  }

  /**
   * Returns {@link Reader}, used to read binary tags.
   *
   * <p>This reader has a size limit for the estimated number of data bytes for a tag.</p>
   *
   * @return binary tag reader
   * @since 4.4.0
   */
  public static Reader reader() {
    return BinaryTagReaderImpl.DEFAULT_LIMIT;
  }

  /**
   * Returns {@link Reader}, used to read binary tags.
   *
   * <p>This reader will limit the number of bytes read to the approximate size limit indicated.</p>
   *
   * @param sizeLimitBytes the maximum (approximate) size of uncompressed data, must be greater than {@code 0}
   * @return binary tag reader
   * @since 4.4.0
   */
  public static Reader reader(final long sizeLimitBytes) {
    if (sizeLimitBytes <= 0) {
      throw new IllegalArgumentException("The size limit must be greater than zero");
    }
    return new BinaryTagReaderImpl(sizeLimitBytes);
  }

  /**
   * Returns {@link Writer}, used to write binary tags.
   *
   * @return binary tag writer
   * @since 4.4.0
   */
  public static Writer writer() {
    return BinaryTagWriterImpl.INSTANCE;
  }

  /**
   * Reads a compound tag from {@code path}.
   *
   * @param path the path
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #reader()}
   */
  @Deprecated
  public static CompoundBinaryTag readPath(final Path path) throws IOException {
    return reader().read(path);
  }

  /**
   * Reads a compound tag from an input stream. The stream is not closed afterwards.
   *
   * @param input the input stream
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #reader()}
   */
  @Deprecated
  public static CompoundBinaryTag readInputStream(final InputStream input) throws IOException {
    return reader().read(input);
  }

  /**
   * Reads a compound tag from {@code path} using GZIP decompression.
   *
   * @param path the path
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #reader()}
   */
  @Deprecated
  public static CompoundBinaryTag readCompressedPath(final Path path) throws IOException {
    return reader().read(path, Compression.GZIP);
  }

  /**
   * Reads a compound tag from an input stream using GZIP decompression. The stream is not closed afterwards.
   *
   * @param input the input stream
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #reader()}
   */
  @Deprecated
  public static CompoundBinaryTag readCompressedInputStream(final InputStream input) throws IOException {
    return reader().read(input, Compression.GZIP);
  }

  /**
   * Reads a compound tag from {@code input}.
   *
   * @param input the input
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #reader()}
   */
  @Deprecated
  public static CompoundBinaryTag readDataInput(final DataInput input) throws IOException {
    return reader().read(input);
  }

  /**
   * Writes a compound tag to {@code path}.
   *
   * @param tag the compound tag
   * @param path the path
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #writer()}
   */
  @Deprecated
  public static void writePath(final CompoundBinaryTag tag, final Path path) throws IOException {
    writer().write(tag, path);
  }

  /**
   * Writes a compound tag to an output stream. The output stream will not be closed.
   *
   * @param tag the compound tag
   * @param output the output stream
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #writer()}
   */
  @Deprecated
  public static void writeOutputStream(final CompoundBinaryTag tag, final OutputStream output) throws IOException {
    writer().write(tag, output);
  }

  /**
   * Writes a compound tag to {@code path} using GZIP compression.
   *
   * @param tag the compound tag
   * @param path the path
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #writer()}
   */
  @Deprecated
  public static void writeCompressedPath(final CompoundBinaryTag tag, final Path path) throws IOException {
    writer().write(tag, path, Compression.GZIP);
  }

  /**
   * Writes a compound tag to an output stream using GZIP compression. The output stream is not closed afterwards.
   *
   * @param tag the compound tag
   * @param output the output stream
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #writer()}
   */
  @Deprecated
  public static void writeCompressedOutputStream(final CompoundBinaryTag tag, final OutputStream output) throws IOException {
    writer().write(tag, output, Compression.GZIP);
  }

  /**
   * Writes a compound tag to {@code output}.
   *
   * @param tag the compound tag
   * @param output the output
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   * @deprecated since 4.4.0, use {@link #writer()}
   */
  @Deprecated
  public static void writeDataOutput(final CompoundBinaryTag tag, final DataOutput output) throws IOException {
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
    default CompoundBinaryTag read(final Path path) throws IOException {
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
    CompoundBinaryTag read(final Path path, final Compression compression) throws IOException;

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
    default CompoundBinaryTag read(final InputStream input) throws IOException {
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
    CompoundBinaryTag read(final InputStream input, final Compression compression) throws IOException;

    /**
     * Reads a binary tag from {@code input}.
     *
     * @param input the input stream
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    CompoundBinaryTag read(final DataInput input) throws IOException;

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
    default Map.Entry<String, CompoundBinaryTag> readNamed(final Path path) throws IOException {
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
    Map.Entry<String, CompoundBinaryTag> readNamed(final Path path, final Compression compression) throws IOException;

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
    default Map.Entry<String, CompoundBinaryTag> readNamed(final InputStream input) throws IOException {
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
    Map.Entry<String, CompoundBinaryTag> readNamed(final InputStream input, final Compression compression) throws IOException;

    /**
     * Reads a binary tag, with a name, from {@code input}.
     *
     * @param input the input
     * @return a binary tag
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    Map.Entry<String, CompoundBinaryTag> readNamed(final DataInput input) throws IOException;
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
     * @param tag the tag to write
     * @param path the path
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default void write(final CompoundBinaryTag tag, final Path path) throws IOException {
      this.write(tag, path, Compression.NONE);
    }

    /**
     * Writes a binary tag to {@code path} with a {@code compression} type.
     *
     * @param tag the tag to write
     * @param path the path
     * @param compression the compression type
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void write(final CompoundBinaryTag tag, final Path path, final Compression compression) throws IOException;

    /**
     * Writes a binary tag to {@code output}.
     *
     * <p>This is the equivalent of passing {@link Compression#NONE} as the second parameter to {@link #write(CompoundBinaryTag, OutputStream, Compression)}.</p>
     *
     * @param tag the tag to write
     * @param output the output stream
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default void write(final CompoundBinaryTag tag, final OutputStream output) throws IOException {
      this.write(tag, output, Compression.NONE);
    }

    /**
     * Writes a binary tag to {@code output} with a {@code compression} type.
     *
     * @param tag the tag to write
     * @param output the output stream
     * @param compression the compression type
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void write(final CompoundBinaryTag tag, final OutputStream output, final Compression compression) throws IOException;

    /**
     * Writes a binary tag to {@code output}.
     *
     * @param tag the tag to write
     * @param output the output
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void write(final CompoundBinaryTag tag, final DataOutput output) throws IOException;

    /**
     * Writes a binary tag, with a name, to {@code path}.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #write(CompoundBinaryTag, Path, Compression)}.</p>
     *
     * @param tag the named tag entry to write
     * @param path the path
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, final Path path) throws IOException {
      this.writeNamed(tag, path, Compression.NONE);
    }

    /**
     * Writes a binary tag, with a name, to {@code path} with a {@code compression} type.
     *
     * @param tag the named tag entry to write
     * @param path the path
     * @param compression the compression type
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, final Path path, final Compression compression) throws IOException;

    /**
     * Writes a binary tag, with a name, to {@code output}.
     *
     * <p>This is the equivalent of passing {@code Compression#NONE} as the second parameter to {@link #write(CompoundBinaryTag, OutputStream, Compression)}.</p>
     *
     * @param tag the named tag entry to write
     * @param output the output stream
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    default void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, final OutputStream output) throws IOException {
      this.writeNamed(tag, output, Compression.NONE);
    }

    /**
     * Writes a binary tag, with a name, to {@code output} with a {@code compression} type.
     *
     * @param tag the named tag entry to write
     * @param output the output stream
     * @param compression the compression type
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, final OutputStream output, final Compression compression) throws IOException;

    /**
     * Writes a binary tag, with a name, to {@code output}.
     *
     * @param tag the named tag entry to write
     * @param output the output
     * @throws IOException if an exception was encountered while reading the tag
     * @since 4.4.0
     */
    void writeNamed(final Map.Entry<String, CompoundBinaryTag> tag, final DataOutput output) throws IOException;
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
      InputStream decompress(final InputStream is) {
        return is;
      }

      @Override
      OutputStream compress(final OutputStream os) {
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
      InputStream decompress(final InputStream is) throws IOException {
        return new GZIPInputStream(is);
      }

      @Override
      OutputStream compress(final OutputStream os) throws IOException {
        return new GZIPOutputStream(os);
      }

      @Override
      public String toString() {
        return "Compression.GZIP";
      }
    };
    /**
     * <a href="https://en.wikipedia.org/wiki/Zlib">ZLIB</a> compression.
     *
     * @since 4.6.0
     */
    public static final Compression ZLIB = new Compression() {
      @Override
      InputStream decompress(final InputStream is) {
        return new InflaterInputStream(is);
      }

      @Override
      OutputStream compress(final OutputStream os) {
        return new DeflaterOutputStream(os);
      }

      @Override
      public String toString() {
        return "Compression.ZLIB";
      }
    };

    abstract InputStream decompress(final InputStream is) throws IOException;

    abstract OutputStream compress(final OutputStream os) throws IOException;
  }
}
