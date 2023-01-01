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
package net.kyori.adventure.internal.properties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

final class AdventurePropertiesImpl {
  private static final String FILESYSTEM_DIRECTORY_NAME = "config";
  private static final String FILESYSTEM_FILE_NAME = "adventure.properties";
  private static final Properties PROPERTIES = new Properties();

  static {
    final Path path = Optional.ofNullable(System.getProperty(systemPropertyName("config")))
      .map(Paths::get)
      .orElseGet(() -> Paths.get(FILESYSTEM_DIRECTORY_NAME, FILESYSTEM_FILE_NAME));
    if (Files.isRegularFile(path)) {
      try (final InputStream is = Files.newInputStream(path)) {
        PROPERTIES.load(is);
      } catch (final IOException e) {
        // Well, that's awkward.
        print(e);
      }
    }
  }

  @SuppressWarnings("CatchAndPrintStacktrace") // we don't have any better options on Java 8
  private static void print(final Throwable ex) {
    ex.printStackTrace();
  }

  private AdventurePropertiesImpl() {
  }

  @VisibleForTesting
  static @NotNull String systemPropertyName(final String name) {
    return String.join(".", "net", "kyori", "adventure", name);
  }

  static <T> AdventureProperties.@NotNull Property<T> property(final @NotNull String name, final @NotNull Function<String, T> parser, final @Nullable T defaultValue) {
    return new PropertyImpl<>(name, parser, defaultValue);
  }

  private static final class PropertyImpl<T> implements AdventureProperties.Property<T> {
    private final String name;
    private final Function<String, T> parser;
    private final @Nullable T defaultValue;
    private boolean valueCalculated;
    private @Nullable T value;

    PropertyImpl(final @NotNull String name, final @NotNull Function<String, T> parser, final @Nullable T defaultValue) {
      this.name = name;
      this.parser = parser;
      this.defaultValue = defaultValue;
    }

    @Override
    public @Nullable T value() {
      if (!this.valueCalculated) {
        final String property = systemPropertyName(this.name);
        final String value = System.getProperty(property, PROPERTIES.getProperty(this.name));
        if (value != null) {
          this.value = this.parser.apply(value);
        }
        if (this.value == null) {
          this.value = this.defaultValue;
        }
        this.valueCalculated = true;
      }
      return this.value;
    }

    @Override
    public boolean equals(final @Nullable Object that) {
      return this == that;
    }

    @Override
    public int hashCode() {
      return this.name.hashCode();
    }
  }
}
