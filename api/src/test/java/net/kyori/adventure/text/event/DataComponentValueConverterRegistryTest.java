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
package net.kyori.adventure.text.event;

import com.google.auto.service.AutoService;
import java.util.Arrays;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.key.Key.key;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataComponentValueConverterRegistryTest {
  @Test
  void testKnownSourceToUnknownDest() {
    final IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> {
      DataComponentValueConverterRegistry.convert(Unregistered.class, key("test"), new DirectValue(3));
    });

    assertTrue(iae.getMessage().contains("There is no data holder converter registered"));
  }

  @Test
  void testUnknownSourceToKnownDest() {
    final IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> {
      DataComponentValueConverterRegistry.convert(DirectValue.class, key("test"), new Unregistered());
    });

    assertTrue(iae.getMessage().contains("There is no data holder converter registered"));

  }

  @Test
  void testFailedConversionBlamesProvider() {
    final IllegalStateException ise = assertThrows(IllegalStateException.class, () -> {
      DataComponentValueConverterRegistry.convert(Failing.class, key("test"), BinaryTagHolder.binaryTagHolder("{}"));
    });

    assertTrue(ise.getMessage().contains(TestConverterProvider.ID.asString()));
  }

  @Test
  void testExactToExact() {
    final DirectValue input = new DirectValue(new Object());
    final ItfValueImpl result = DataComponentValueConverterRegistry.convert(ItfValueImpl.class, key("test"), input);

    assertEquals(input.value, result.value);
  }

  @Test
  void testSubtypeToExact() {
    final ItfValue input = new ItfValueImpl(new Object());
    final DirectValue result = DataComponentValueConverterRegistry.convert(DirectValue.class, key("test"), input);

    assertEquals(input.value(), result.value);
  }

  @Test
  void testSubtypeToSupertype() {
    final ItfValue input = new ItfValueImpl(new Object());
    final DataComponentValue result = DataComponentValueConverterRegistry.convert(Intermediate.class, key("test"), input);

    assertInstanceOf(DirectValue.class, result);
    assertEquals(input.value(), ((DirectValue) result).value);
  }

  @Test
  void testExactToSupertype() {
    final DirectValue input = new DirectValue(new Object());
    final ItfValue result = DataComponentValueConverterRegistry.convert(ItfValue.class, key("test"), input);

    assertEquals(input.value, result.value());
  }

  @AutoService(DataComponentValueConverterRegistry.Provider.class)
  public static final class TestConverterProvider implements DataComponentValueConverterRegistry.Provider {
    static final Key ID = key("adventure", "test/converter_registry");

    @Override
    public @NotNull Key id() {
      return ID;
    }

    @Override
    public @NotNull Iterable<DataComponentValueConverterRegistry.Conversion<?, ?>> conversions() {
      // gah j8
      return Arrays.asList(
        DataComponentValueConverterRegistry.Conversion.convert(DirectValue.class, ItfValueImpl.class, (key, dir) -> new ItfValueImpl(dir.value)),
        DataComponentValueConverterRegistry.Conversion.convert(ItfValue.class, DirectValue.class, (key, itf) -> new DirectValue(itf.value())),
        DataComponentValueConverterRegistry.Conversion.convert(BinaryTagHolder.class, Failing.class, (key, itf) -> {
          throw new RuntimeException("hah!");
        })
      );
    }
  }

  static final class Unregistered implements DataComponentValue {
    // i shall not be converted
  }

  static final class Failing implements DataComponentValue {
    // this is a marker interface to trigger a failure
  }

  interface Intermediate extends DataComponentValue {

  }

  static final class DirectValue implements Intermediate {
    final Object value;

    DirectValue(final Object value) {
      this.value = value;
    }
  }

  interface ItfValue extends DataComponentValue {
    Object value();
  }

  static final class ItfValueImpl implements ItfValue {
    final Object value;

    ItfValueImpl(final Object value) {
      this.value = value;
    }

    @Override
    public Object value() {
      return this.value;
    }
  }
}
