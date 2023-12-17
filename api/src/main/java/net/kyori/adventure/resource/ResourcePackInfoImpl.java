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
package net.kyori.adventure.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class ResourcePackInfoImpl implements ResourcePackInfo {
  private final UUID id;
  private final URI uri;
  private final String hash;

  ResourcePackInfoImpl(final @NotNull UUID id, final @NotNull URI uri, final @NotNull String hash) {
    this.id = requireNonNull(id, "id");
    this.uri = requireNonNull(uri, "uri");
    this.hash = requireNonNull(hash, "hash");
  }

  @Override
  public @NotNull UUID id() {
    return this.id;
  }

  @Override
  public @NotNull URI uri() {
    return this.uri;
  }

  @Override
  public @NotNull String hash() {
    return this.hash;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("id", this.id),
      ExaminableProperty.of("uri", this.uri),
      ExaminableProperty.of("hash", this.hash)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof ResourcePackInfoImpl)) return false;
    final ResourcePackInfoImpl that = (ResourcePackInfoImpl) other;
    return this.id.equals(that.id) &&
           this.uri.equals(that.uri) &&
           this.hash.equals(that.hash);
  }

  @Override
  public int hashCode() {
    int result = this.id.hashCode();
    result = 31 * result + this.uri.hashCode();
    result = 31 * result + this.hash.hashCode();
    return result;
  }

  static final class BuilderImpl implements Builder {
    private UUID id;
    private URI uri;
    private String hash;

    BuilderImpl() {
    }

    @Override
    public @NotNull Builder id(final @NotNull UUID id) {
      this.id = requireNonNull(id, "id");
      return this;
    }

    @Override
    public @NotNull Builder uri(final @NotNull URI uri) {
      this.uri = requireNonNull(uri, "uri");
      if (this.id == null) {
        this.id = UUID.nameUUIDFromBytes(uri.toString().getBytes(StandardCharsets.UTF_8));
      }
      return this;
    }

    @Override
    public @NotNull Builder hash(final @NotNull String hash) {
      this.hash = requireNonNull(hash, "hash");
      return this;
    }

    @Override
    public @NotNull ResourcePackInfo build() {
      return new ResourcePackInfoImpl(this.id, this.uri, this.hash);
    }

    @Override
    public @NotNull CompletableFuture<ResourcePackInfo> computeHashAndBuild(final @NotNull Executor executor) {
      return computeHash(requireNonNull(this.uri, "uri"), executor)
        .thenApply(hash -> {
          this.hash(hash);
          return this.build();
        });
    }
  }

  static CompletableFuture<String> computeHash(final URI uri, final Executor exec) {
    final CompletableFuture<String> result = new CompletableFuture<>();

    exec.execute(() -> {
      try {
        final URL url = uri.toURL();
        final URLConnection conn = url.openConnection();
        conn.addRequestProperty("User-Agent", "adventure/" + ResourcePackInfoImpl.class.getPackage().getSpecificationVersion() + " (pack-fetcher)");
        try (final InputStream is = conn.getInputStream()) {
          final MessageDigest digest = MessageDigest.getInstance("SHA-1");
          final byte[] buf = new byte[8192];
          int read;
          while ((read = is.read(buf)) != -1) {
            digest.update(buf, 0, read);
          }
          result.complete(bytesToString(digest.digest()));
        }
      } catch (final IOException | NoSuchAlgorithmException ex) {
        result.completeExceptionally(ex);
      }
    });

    return result;
  }

  static String bytesToString(final byte[] arr) {
    final StringBuilder builder = new StringBuilder(arr.length * 2);
    final Formatter fmt = new Formatter(builder, Locale.ROOT);
    for (int i = 0; i < arr.length; i++) {
      fmt.format("%02x", arr[i] & 0xff);
    }
    return builder.toString();
  }
}
