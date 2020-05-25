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
package net.kyori.adventure.spi;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

final class AdventureProvider0 {
  private static AdventureProvider provider;

  /**
   * Get the highest-supported provider available.
   *
   * @return a loaded provider
   */
  public static AdventureProvider provide() {
    AdventureProvider result = provider;
    if (result != null) {
      return result;
    }
      final ServiceLoader<AdventureProvider> loader  = ServiceLoader.load(AdventureProvider.class);
      final Iterator<AdventureProvider> it = loader.iterator();
      while (it.hasNext()) {
        try {
          AdventureProvider prov = it.next();
          if (result == null || prov.supportLevel().ordinal() >= result.supportLevel().ordinal()) {
            result = prov;
          }
        } catch(ServiceConfigurationError ex) {
          continue;
        }
      }
      if (result == null) {
        throw new IllegalArgumentException("Could not find Adventure provider");
      }
      return provider = result;
  }
}
