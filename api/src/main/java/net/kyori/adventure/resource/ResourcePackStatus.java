/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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

/**
 * Resource pack application state.
 *
 * <p>Each status is a phase in the status state machine.
 * The client provides this information back to servers as it attempts to download and apply resource packs.</p>
 *
 * <p>Initial states are {@link #ACCEPTED}, {@link #DECLINED}, or {@link #INVALID_URL}.</p>
 *
 * @since 4.15.0
 */
public enum ResourcePackStatus {
  /**
   * Indicates that the user has accepted download.
   *
   * <p>Next states: {@link #FAILED_DOWNLOAD}, {@link #DOWNLOADED}</p>
   *
   * @since 4.15.0
   */
  ACCEPTED(true),
  /**
   * Indicates that the user has declined a pack.
   *
   * <p>Terminal state.</p>
   *
   * @since 4.15.0
   */
  DECLINED(false),
  /**
   * Indicates that the provided pack URL could not be parsed.
   *
   * <p>Terminal state.</p>
   *
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  INVALID_URL(false),
  /**
   * Indicates that the download failed for some other reason.
   *
   * <p>Terminal state.</p>
   *
   * @since 4.15.0
   */
  FAILED_DOWNLOAD(false),
  /**
   * Indicates that the resource pack has been successfully downloaded.
   *
   * <p>Next states: {@link #FAILED_RELOAD}, {@link #DISCARDED}, or {@link #SUCCESSFULLY_LOADED}</p>
   *
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  DOWNLOADED(true),
  /**
   * Indicates that the client's resource manager reload failed.
   *
   * <p>Terminal state.</p>
   *
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  FAILED_RELOAD(false),
  /**
   * Indicates that this resource pack did not have issues, but was not applied due to a failure in another server resource pack.
   *
   * <p>Terminal state.</p>
   *
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  DISCARDED(false),

  /**
   * Indicates that the pack has successfully loaded and resource reloading is complete.
   *
   * <p>Terminal state.</p>
   *
   * @since 4.15.0
   */
  SUCCESSFULLY_LOADED(false);

  private final boolean intermediate;

  ResourcePackStatus(final boolean intermediate) {
    this.intermediate = intermediate;
  }

  /**
   * Whether, after receiving this status, further status events might occur.
   *
   * @return the intermediate status
   * @since 4.15.0
   */
  public boolean intermediate() {
    return this.intermediate;
  }
}
