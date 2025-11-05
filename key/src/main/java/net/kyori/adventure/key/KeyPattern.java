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
package net.kyori.adventure.key;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.RegExp;

/**
 * An annotation used to annotate elements that must match against a valid {@link Key} pattern.
 *
 * @since 4.14.0
 */
@Documented
@Pattern("(?:(" + KeyPattern.NAMESPACE_PATTERN + ":)?|:)" + KeyPattern.VALUE_PATTERN)
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
public @interface KeyPattern {
  /**
   * The pattern for namespaces.
   *
   * @since 5.0.0
   */
  @RegExp String NAMESPACE_PATTERN = "[a-z0-9_\\-.]+";
  /**
   * The pattern for values.
   *
   * @since 5.0.0
   */
  @RegExp String VALUE_PATTERN = "[a-z0-9_\\-./]+";

  /**
   * An annotation used to annotate elements that must match against a valid {@link Key} {@link Key#namespace() namespace} pattern.
   *
   * @since 4.14.0
   */
  @Documented
  @Pattern(KeyPattern.NAMESPACE_PATTERN)
  @Retention(RetentionPolicy.CLASS)
  @Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
  @interface Namespace {
  }

  /**
   * An annotation used to annotate elements that must match against a valid {@link Key} {@link Key#value() value} pattern.
   *
   * @since 4.14.0
   */
  @Documented
  @Pattern(KeyPattern.VALUE_PATTERN)
  @Retention(RetentionPolicy.CLASS)
  @Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
  @interface Value {
  }
}
