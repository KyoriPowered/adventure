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
package net.kyori.adventure.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * A {@link java.util.ResourceBundle.Control} that enforces UTF-8 string encoding.
 *
 * <p>See https://stackoverflow.com/a/4660195 for more details.</p>
 */
public final class UTF8 extends ResourceBundle.Control {
  public ResourceBundle newBundle(final @NonNull String baseName, final @NonNull Locale locale, final @NonNull String format, final @NonNull ClassLoader loader, final boolean reload) throws IOException {
    // The below is a copy of the default implementation.
    final String bundleName = toBundleName(baseName, locale);
    final String resourceName = toResourceName(bundleName, "properties");
    ResourceBundle bundle = null;
    InputStream stream = null;
    if(reload) {
      final URL url = loader.getResource(resourceName);
      if(url != null) {
        final URLConnection connection = url.openConnection();
        if(connection != null) {
          connection.setUseCaches(false);
          stream = connection.getInputStream();
        }
      }
    } else {
      stream = loader.getResourceAsStream(resourceName);
    }
    if(stream != null) {
      try {
        // Only this line is changed to make it to read properties files as UTF-8.
        bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
      } finally {
        stream.close();
      }
    }
    return bundle;
  }
}
