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

import java.io.DataInput;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TrackingDataInput implements DataInput, BinaryTagScope {
  private static final int MAX_DEPTH = 512;
  private final DataInput input;
  private final long maxLength;
  private long counter;
  private int depth;

  TrackingDataInput(final DataInput input, final long maxLength) {
    this.input = input;
    this.maxLength = maxLength;
  }

  public static BinaryTagScope enter(final DataInput input) throws IOException {
    if (input instanceof TrackingDataInput) {
      return ((TrackingDataInput) input).enter();
    } else {
      return NoOp.INSTANCE;
    }
  }

  public static BinaryTagScope enter(final DataInput input, final long expectedSize) throws IOException {
    if (input instanceof TrackingDataInput) {
      return ((TrackingDataInput) input).enter(expectedSize);
    } else {
      return NoOp.INSTANCE;
    }
  }

  public DataInput input() {
    return this.input;
  }

  // enter a nesting level that pre-allocates storage
  public TrackingDataInput enter(final long expectedSize) throws IOException {
    if (this.depth++ > MAX_DEPTH) {
      throw new IOException("NBT read exceeded maximum depth of " + MAX_DEPTH);
    }

    this.ensureMaxLength(expectedSize);
    return this;
  }

  public TrackingDataInput enter() throws IOException {
    return this.enter(0);
  }

  public void exit() throws IOException {
    this.depth--;
    this.ensureMaxLength(0);
  }

  private void ensureMaxLength(final long expected) throws IOException {
    if (this.maxLength > 0 && this.counter + expected > this.maxLength) {
      throw new IOException("The read NBT was longer than the maximum allowed size of " + this.maxLength + " bytes!");
    }
  }

  @Override
  public void readFully(final byte@NotNull[] array) throws IOException {
    this.counter += array.length;
    this.input.readFully(array);
  }

  @Override
  public void readFully(final byte@NotNull[] array, final int off, final int len) throws IOException {
    this.counter += len;
    this.input.readFully(array, off, len);
  }

  @Override
  public int skipBytes(final int n) throws IOException {
    return this.input.skipBytes(n);
  }

  @Override
  public boolean readBoolean() throws IOException {
    this.counter++;
    return this.input.readBoolean();
  }

  @Override
  public byte readByte() throws IOException {
    this.counter++;
    return this.input.readByte();
  }

  @Override
  public int readUnsignedByte() throws IOException {
    this.counter++;
    return this.input.readUnsignedByte();
  }

  @Override
  public short readShort() throws IOException {
    this.counter += (Short.SIZE / Byte.SIZE);
    return this.input.readShort();
  }

  @Override
  public int readUnsignedShort() throws IOException {
    this.counter += (Short.SIZE / Byte.SIZE);
    return this.input.readUnsignedShort();
  }

  @Override
  public char readChar() throws IOException {
    this.counter += (Character.SIZE / Byte.SIZE);
    return this.input.readChar();
  }

  @Override
  public int readInt() throws IOException {
    this.counter += (Integer.SIZE / Byte.SIZE);
    return this.input.readInt();
  }

  @Override
  public long readLong() throws IOException {
    this.counter += (Long.SIZE / Byte.SIZE);
    return this.input.readLong();
  }

  @Override
  public float readFloat() throws IOException {
    this.counter += (Float.SIZE / Byte.SIZE);
    return this.input.readFloat();
  }

  @Override
  public double readDouble() throws IOException {
    this.counter += (Double.SIZE / Byte.SIZE);
    return this.input.readDouble();
  }

  @Override
  public @Nullable String readLine() throws IOException {
    final @Nullable String result = this.input.readLine();
    if (result != null) {
      this.counter += result.length() + 1;
    }
    return result;
  }

  @Override
  public @NotNull String readUTF() throws IOException {
    final String result = this.input.readUTF();
    this.counter += (result.length() * 2L) + 2; // not entirely accurate, but the closest we can get without doing implementation details
    return result;
  }

  @Override
  public void close() throws IOException {
    this.exit();
  }

}
