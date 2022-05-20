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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class ComponentLoggerTest {
  @Mock(name = "net.kyori.adventure.test.ComponentLoggerTest")
  Logger backingLogger;

  private final ComponentLoggerProvider.LoggerHelper helper = new Handler.LoggerHelperImpl();

  ComponentLogger makeLogger() {
    return new WrappingComponentLoggerImpl(this.backingLogger, this.helper.plainSerializer());
  }

  @BeforeEach
  void enableLevels() {
    Mockito.when(this.backingLogger.isInfoEnabled()).thenReturn(true);
  }

  @Test
  void testLogSimple() {
    final Component toLog = Component.text().content("Hello ").color(NamedTextColor.RED).append(Component.translatable("location.world")).build();

    final ComponentLogger logger = this.makeLogger();

    logger.info(toLog);

    Mockito.verify(this.backingLogger, Mockito.times(2)).isInfoEnabled();
    Mockito.verify(this.backingLogger).info("Hello location.world");
    Mockito.verifyNoMoreInteractions(this.backingLogger);
  }
}
