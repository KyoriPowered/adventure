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
package net.kyori.test;

import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

import static org.junit.jupiter.api.Assertions.fail;

public final class Overrides {
  private Overrides() {
  }

  private static Stream<MethodInfo> methodStream(final Method[] methods, final @Nullable Class<? extends Annotation> exclusionAnno) {
    return Arrays.stream(methods)
      .filter(method -> exclusionAnno == null || !method.isAnnotationPresent(exclusionAnno)) // there are some that truly are default methods
      .filter(method -> !Modifier.isStatic(method.getModifiers())) // unlikely to exist, but best we exclude them just in case
      .map(MethodInfo::new);
  }

  public static Set<MethodInfo> methods(final Class<?> in, final Class<? extends Annotation> exclusionAnno) {
    return methodStream(in.getDeclaredMethods(), exclusionAnno)
      .collect(Collectors.toSet());
  }

  public static Stream<MethodInfo> inheritedMethodStream(final Class<?> in, final Class<? extends Annotation> exclusionAnno) {
    return methodStream(in.getMethods(), exclusionAnno);
  }

  public static void failIfMissing(final Class<?> parent, final Set<MethodInfo> parentMethods, final Class<?> child, final Set<MethodInfo> childMethods) {
    final Set<Overrides.MethodInfo> missing = Sets.difference(parentMethods, childMethods);
    if (!missing.isEmpty()) {
      final StringBuilder error = new StringBuilder();
      error.append(child.getSimpleName()).append(" is missing override for ").append(parent.getSimpleName()).append(" methods:");
      for (final Overrides.MethodInfo method : missing) {
        error.append('\n').append("- ").append(method);
      }
      error.append('\n');
      fail(error.toString());
    }
  }

  // todo(kashike): this can be a record once we have a Java 16 source-set
  public static final class MethodInfo {
    public final String name;
    public final Class<?> returnType;
    public final Class<?>[] paramTypes;
    public final boolean isDeprecated;

    MethodInfo(final Method method) {
      this.name = method.getName();
      this.returnType = method.getReturnType();
      this.paramTypes = method.getParameterTypes();
      this.isDeprecated = method.isAnnotationPresent(Deprecated.class);
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
