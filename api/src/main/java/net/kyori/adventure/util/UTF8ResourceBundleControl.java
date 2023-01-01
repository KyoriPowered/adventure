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
package net.kyori.adventure.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ResourceBundle.Control} that enforces UTF-8 string encoding.
 *
 * <p>See https://stackoverflow.com/a/4660195 for more details.</p>
 *
 * @since 4.0.0
 */
public final class UTF8ResourceBundleControl extends ResourceBundle.Control {
  private static final UTF8ResourceBundleControl INSTANCE = new UTF8ResourceBundleControl();

  /**
   * Gets the shared instance.
   *
   * @return a resource bundle control
   * @since 4.0.0
   */
  public static ResourceBundle.@NotNull Control get() {
    return INSTANCE;
  }

  @Override
  public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IllegalAccessException, InstantiationException, IOException {
    if (format.equals("java.properties")) {
      final String bundle = this.toBundleName(baseName, locale);
      final String resource = this.toResourceName(bundle, "properties");
      InputStream is = null;
      if (reload) {
        final URL url = loader.getResource(resource);
        if (url != null) {
          final URLConnection connection = url.openConnection();
          if (connection != null) {
            connection.setUseCaches(false);
            is = connection.getInputStream();
          }
        }
      } else {
        is = loader.getResourceAsStream(resource);
      }

      if (is != null) {
        try (final InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
          return new PropertyResourceBundle(isr);
        }
      } else {
        return null;
      }
    } else {
      return super.newBundle(baseName, locale, format, loader, reload);
    }
  }
}
