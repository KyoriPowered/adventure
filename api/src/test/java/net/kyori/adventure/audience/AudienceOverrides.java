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
package net.kyori.adventure.audience;

import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.fail;

class AudienceOverrides {
  @ParameterizedTest
  @ValueSource(classes = {
    ForwardingAudience.class,
    ForwardingAudience.Single.class
  })
  void ensureForwardingAudiencesOverrideRequiredMethods(final Class<?> audience) {
    final Set<MethodInfo> missing = Sets.difference(methods(Audience.class), methods(audience));
    if (!missing.isEmpty()) {
      final StringBuilder error = new StringBuilder();
      error.append(audience.getSimpleName()).append(" is missing override for ").append(Audience.class.getSimpleName()).append(" methods:");
      for (final MethodInfo method : missing) {
        error.append('\n').append("- ").append(method);
      }
      error.append('\n');
      fail(error.toString());
    }
  }

  private static Set<MethodInfo> methods(final Class<?> in) {
    return Arrays.stream(in.getDeclaredMethods())
      .filter(method -> !method.isAnnotationPresent(ForwardingAudienceOverrideNotRequired.class)) // there are some that truly are default methods
      .filter(method -> !Modifier.isStatic(method.getModifiers())) // unlikely to exist, but best we exclude them just in case
      .map(MethodInfo::new)
      .collect(Collectors.toSet());
  }

  // todo(kashike): this can be a record once we have a Java 16 source-set
  static final class MethodInfo {
    final String name;
    final Class<?> returnType;
    final Class<?>[] paramTypes;

    MethodInfo(final Method method) {
      this.name = method.getName();
      this.returnType = method.getReturnType();
      this.paramTypes = method.getParameterTypes();
    }

    @Override
    public String toString() {
      return this.returnType + " " + this.name + "(" + Arrays.stream(this.paramTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public boolean equals(final Object other) {
      if (this == other) return true;
      if (other == null || this.getClass() != other.getClass()) return false;
      final MethodInfo that = (MethodInfo) other;
      return this.name.equals(that.name)
        && Objects.equals(this.returnType, that.returnType)
        && Arrays.equals(this.paramTypes, that.paramTypes);
    }

    @Override
    public int hashCode() {
      int result = this.name.hashCode();
      result = (31 * result) + this.returnType.hashCode();
      result = (31 * result) + Arrays.hashCode(this.paramTypes);
      return result;
    }
  }
}
