/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.logger.slf4j;

import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * A service interface for platforms to provide their own component logger implementations.
 *
 * @since 4.11.0
 */
public interface ComponentLoggerProvider {
  /**
   * Create a component logger for the provided logger name.
   *
   * @param helper a source for common helper implementations when building a logger
   * @param name the logger name
   * @return a component logger with the provided name
   * @since 4.11.0
   */
  @NotNull ComponentLogger logger(final @NotNull LoggerHelper helper, final @NotNull String name);

  /**
   * A factory for default implementations of component loggers.
   *
   * @since 4.11.0
   */
  @ApiStatus.NonExtendable
  interface LoggerHelper {

    /**
     * Create a serializer function that will translate logged output into the system default locale, and then serialize it to plain text.
     *
     * @return a plain serializer
     * @since 4.11.0
     */
    @NotNull Function<Component, String> plainSerializer();

    /**
     * Create a component logger based on one which delegates to an underlying plain {@link Logger} implementation.
     *
     * <p>This sort of logger requires Components to be serialized to some sort of formatted {@link String} to match the SLF4J contract.</p>
     *
     * @param base the base logger
     * @param serializer the serializer to translate and format a component in a log message.
     * @return a new logger
     * @since 4.11.0
     */
    @NotNull ComponentLogger delegating(final @NotNull Logger base, final @NotNull Function<Component, String> serializer);
  }
}
