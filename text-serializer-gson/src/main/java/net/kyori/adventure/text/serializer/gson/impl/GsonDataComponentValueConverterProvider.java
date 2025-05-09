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
package net.kyori.adventure.text.serializer.gson.impl;

import com.google.auto.service.AutoService;
import com.google.gson.JsonNull;
import java.util.Collections;
import net.kyori.adventure.Adventure;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import net.kyori.adventure.text.serializer.gson.GsonDataComponentValue;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A provider for Gson's implementations of data component value converters.
 *
 * <p>This is public SPI, not API.</p>
 *
 * @since 4.17.0
 */
@AutoService(DataComponentValueConverterRegistry.Provider.class)
@ApiStatus.Internal
public final class GsonDataComponentValueConverterProvider implements DataComponentValueConverterRegistry.Provider {
  private static final Key ID = Key.key(Adventure.NAMESPACE, "serializer/gson");

  @Override
  public @NotNull Key id() {
    return ID;
  }

  @Override
  public @NotNull Iterable<DataComponentValueConverterRegistry.Conversion<?, ?>> conversions() {
    return Collections.singletonList(
      DataComponentValueConverterRegistry.Conversion.convert(
        DataComponentValue.Removed.class,
        GsonDataComponentValue.class,
        (k, removed) -> GsonDataComponentValue.gsonDataComponentValue(JsonNull.INSTANCE)
      )
    );
  }
}
