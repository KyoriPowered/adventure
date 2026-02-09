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
package net.kyori.adventure.text.logger.slf4j;

import com.github.valfirst.slf4jtest.LoggingEvent;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import com.github.valfirst.slf4jtest.TestLoggerFactoryExtension;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.ComponentMessageThrowable;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestLoggerFactoryExtension.class)
public class ComponentLoggerTest {
  private static final TestLogger LOGGER = TestLoggerFactory.getTestLogger(ComponentLoggerTest.class);
  private static final Function<Component, String> PLAIN_SERIALIZER = Handler.LoggerHelperImpl.INSTANCE.plainSerializer();

  private static final Marker MARKED = MarkerFactory.getMarker("MARKED");

  ComponentLogger makeLogger() {
    return ComponentLogger.logger();
  }

  ComponentLogger makeLogger(final @Nullable ComponentLogContextInjector injector) {
    return new WrappingComponentLoggerImpl(LOGGER, PLAIN_SERIALIZER, injector);
  }

  @Test
  void testCallerLogger() {
    final ComponentLogger logger = ComponentLogger.logger();
    assertEquals(this.getClass().getName(), logger.getName());
  }

  @Test
  void testLogSimple() {
    final Component toLog = Component.text()
      .content("Hello ")
      .color(NamedTextColor.RED)
      .append(Component.translatable("location.world"))
      .build();

    this.makeLogger().info(toLog);

    assertEquals(LOGGER.getLoggingEvents(), ImmutableList.of(LoggingEvent.info("Hello location.world")));
  }

  @Test
  void testComponentArg() {
    final Component message = Component.text("Hello ").append(Component.text("{}", NamedTextColor.BLUE));
    final Component arg = Component.selector("@s");

    this.makeLogger().warn(message, arg);

    assertEquals(LOGGER.getLoggingEvents(), ImmutableList.of(LoggingEvent.warn("Hello {}", "@s")));
  }

  @Test
  void testStringArg() {
    final Component message = Component.text("Hello ").append(Component.text("{}", NamedTextColor.BLUE));
    final String arg = "world";

    this.makeLogger().debug(message, arg);

    assertEquals(LOGGER.getLoggingEvents(), ImmutableList.of(LoggingEvent.debug("Hello {}", arg)));
  }

  @Test
  void testMultiArgs() {
    final Component message = Component.text("Good morning! The time is {} and you have {} cats!");
    final Component arg0 = Component.text("14:28", NamedTextColor.BLUE);
    final String arg1 = "11";

    this.makeLogger().error(message, arg0, arg1);
    assertEquals(
      LOGGER.getLoggingEvents(),
      ImmutableList.of(LoggingEvent.error("Good morning! The time is {} and you have {} cats!", "14:28", "11"))
    );
  }

  @Test
  void testUnwrapThrowable() {
    final Component message = Component.text("Hello world");
    final Exception error = new RichTestException(Component.translatable("test.failed", NamedTextColor.DARK_PURPLE));

    this.makeLogger().warn(message, error);

    final List<LoggingEvent> events = LOGGER.getLoggingEvents();
    assertEquals(1, events.size());
    final Throwable thrownException = events.get(0).getThrowable().orElse(null);
    assertNotNull(thrownException);

    assertEquals("test.failed", thrownException.getMessage());
    assertArrayEquals(error.getStackTrace(), thrownException.getStackTrace());
    assertTrue(thrownException.toString().startsWith("net.kyori.adventure.text.logger.slf4j.ComponentLoggerTest$RichTestException"));
  }

  static class RichTestException extends Exception implements ComponentMessageThrowable {
    private static final long serialVersionUID = -1l;

    private final Component richMessage;

    RichTestException(final Component richMessage) {
      super("no");
      this.richMessage = richMessage;
    }

    @Override
    public @Nullable Component componentMessage() {
      return this.richMessage;
    }
  }

  @Test
  void testWithMarker() {
    final Component message = Component.text("meow :3");
    this.makeLogger().info(MARKED, message);
    assertEquals(
      LOGGER.getLoggingEvents(),
      ImmutableList.of(LoggingEvent.info(MARKED, "meow :3"))
    );
  }

  @Test
  void testComponentAsArgToPlainLog() {
    this.makeLogger().info("Hello {}", Component.text("friend"));
    assertEquals(
      LOGGER.getLoggingEvents(),
      ImmutableList.of(LoggingEvent.info("Hello {}", "friend"))
    );
  }

  @Test
  void testInjectorReceivesRawComponentRecord() {
    final Component format = Component.text("Hello ").append(Component.text("{}", NamedTextColor.BLUE));
    final Component arg = Component.text("friend", NamedTextColor.RED);
    final AtomicReference<ComponentLogRecord> seen = new AtomicReference<>();

    this.makeLogger(record -> {
      seen.set(record);
      return null;
    }).info(format, arg);

    final ComponentLogRecord captured = seen.get();
    assertNotNull(captured);
    assertEquals(org.slf4j.event.Level.INFO, captured.level());
    assertSame(format, captured.componentFormatOrMessage());
    assertNull(captured.stringFormatOrMessage());
    assertNotNull(captured.arguments());
    assertEquals(1, captured.arguments().length);
    assertSame(arg, captured.arguments()[0]);
    assertNull(captured.throwable());
    assertEquals(LOGGER.getLoggingEvents(), ImmutableList.of(LoggingEvent.info("Hello {}", "friend")));
  }

  @Test
  void testInjectorReceivesRawThrowableForStringRecord() {
    final RichTestException throwable = new RichTestException(Component.text("rich"));
    final AtomicReference<ComponentLogRecord> seen = new AtomicReference<>();

    this.makeLogger(record -> {
      seen.set(record);
      return null;
    }).warn("warn text", throwable);

    final ComponentLogRecord captured = seen.get();
    assertNotNull(captured);
    assertEquals(org.slf4j.event.Level.WARN, captured.level());
    assertEquals("warn text", captured.stringFormatOrMessage());
    assertNull(captured.componentFormatOrMessage());
    assertNull(captured.arguments());
    assertSame(throwable, captured.throwable());
  }

  @Test
  void testInjectorScopeClosedOnSuccessAndExceptionsSwallowed() {
    final AtomicInteger beginCalls = new AtomicInteger();
    final AtomicInteger closeCalls = new AtomicInteger();

    this.makeLogger(record -> {
      beginCalls.incrementAndGet();
      return () -> {
        closeCalls.incrementAndGet();
        throw new IllegalStateException("close fail");
      };
    }).info("Hello world");

    assertEquals(1, beginCalls.get());
    assertEquals(1, closeCalls.get());
    assertEquals(LOGGER.getLoggingEvents(), ImmutableList.of(LoggingEvent.info("Hello world")));
  }

  @Test
  void testInjectorBeginFailureIsSwallowed() {
    this.makeLogger(record -> {
      throw new IllegalStateException("begin fail");
    }).info("still logs");

    assertEquals(LOGGER.getLoggingEvents(), ImmutableList.of(LoggingEvent.info("still logs")));
  }

  @Test
  void testInjectorScopeClosedWhenUnderlyingLoggerThrows() {
    final AtomicInteger closeCalls = new AtomicInteger();
    final Logger throwingLogger = (Logger) Proxy.newProxyInstance(
      Logger.class.getClassLoader(),
      new Class<?>[] {Logger.class},
      (proxy, method, args) -> {
        if (method.getName().equals("isInfoEnabled")) {
          return true;
        } else if (method.getName().equals("info") && method.getParameterCount() == 1) {
          throw new IllegalStateException("logger fail");
        } else if (method.getName().equals("getName")) {
          return "throwing";
        }

        if (method.getReturnType() == boolean.class) return false;
        return null;
      }
    );
    final ComponentLogger logger = new WrappingComponentLoggerImpl(
      throwingLogger,
      PLAIN_SERIALIZER,
      record -> () -> closeCalls.incrementAndGet()
    );

    assertThrows(IllegalStateException.class, () -> logger.info("hello"));
    assertEquals(1, closeCalls.get());
  }

  @Test
  @Disabled("We cannot implement this without forcing a binary dep at the moment")
  void testFluentApi() {
    final Component message = Component.text("Hello ").append(Component.text("{}", NamedTextColor.BLUE));
    final String arg = "world";

    this.makeLogger().atDebug()
        // .setMessage(message)
        .addArgument(arg)
        .log();

    assertEquals(LOGGER.getLoggingEvents(), ImmutableList.of(LoggingEvent.debug("Hello {}", arg)));
  }
}
