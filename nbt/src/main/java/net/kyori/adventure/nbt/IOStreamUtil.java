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
import java.io.InputStream;
import java.io.OutputStream;

final class IOStreamUtil {
  private IOStreamUtil() {
  }

  static InputStream closeShield(final InputStream stream) {
    return new InputStream() {
      @Override
      public int read() throws IOException {
        return stream.read();
      }

      @Override
      public int read(final byte[] b) throws IOException {
        return stream.read(b);
      }

      @Override
      public int read(final byte[] b, final int off, final int len) throws IOException {
        return stream.read(b, off, len);
      }
    };
  }

  static OutputStream closeShield(final OutputStream stream) {
    return new OutputStream() {
      @Override
      public void write(final int b) throws IOException {
        stream.write(b);
      }

      @Override
      public void write(final byte[] b) throws IOException {
        stream.write(b);
      }

      @Override
      public void write(final byte[] b, final int off, final int len) throws IOException {
        stream.write(b, off, len);
      }
    };
  }
}
