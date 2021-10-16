/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A resolver for user-defined templates.
 *
 * @since 4.2.0
 */
public interface TemplateResolver {
  /**
   * Constructs a template resolver from key-value pairs or {@link Template} instances.
   *
   * <p>The {@code pairs} arguments must be a string key followed by a string or {@link ComponentLike} value or a {@link Template}.</p>
   *
   * @param objects the objects
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver resolving(final @NotNull Object @NotNull ... objects) {
    final int size = Objects.requireNonNull(objects, "pairs").length;

    if (size == 0) return empty();

    String key = null;

    final Map<String, Template> templateMap = new HashMap<>(size);
    for (int i = 0; i < size; i++) {
      final Object obj = objects[i];

      if (key == null) {
        // we are looking for a key or a template
        if (obj instanceof Template) {
          final Template template = (Template) obj;
          templateMap.put(template.key(), template);
        } else if (obj instanceof String) {
          key = (String) obj;
        } else {
          throw new IllegalArgumentException("Argument " + i + " in pairs must be a String key or a Template, was " + obj.getClass().getName());
        }
      } else {
        // we are looking for a value
        if (obj instanceof String) {
          templateMap.put(key, Template.template(key, (String) obj));
        } else if (obj instanceof ComponentLike) {
          templateMap.put(key, Template.template(key, (ComponentLike) obj));
        } else {
          throw new IllegalArgumentException("Argument " + i + " in pairs must be a String or ComponentLike value, was " + obj.getClass().getName());
        }

        key = null;
      }
    }

    if (key != null) {
      throw new IllegalArgumentException("Found key \"" + key + "\" in objects that wasn't followed by a value.");
    }

    if (templateMap.isEmpty()) return empty();

    return new MapTemplateResolver(templateMap);
  }

  /**
   * Constructs a template resolver from key-value pairs.
   *
   * <p>The values must be instances of String, {@link ComponentLike} or {@link Template}.</p>
   *
   * @param pairs the key-value pairs
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver pairs(final @NotNull Map<String, ?> pairs) {
    final int size = Objects.requireNonNull(pairs, "pairs").size();

    if (size == 0) return empty();

    final Map<String, Template> templateMap = new HashMap<>(size);

    for (final Map.Entry<String, ?> entry : pairs.entrySet()) {
      final String key = Objects.requireNonNull(entry.getKey(), "pairs cannot contain null keys");
      final Object value = entry.getValue();

      if (value instanceof String) templateMap.put(key, Template.template(key, (String) value));
      else if (value instanceof ComponentLike) templateMap.put(key, Template.template(key, (ComponentLike) value));
      else if (value instanceof Template) templateMap.put(key, (Template) value);
      else
        throw new IllegalArgumentException("Values must be either ComponentLike or String but " + value + " was not.");
    }

    return new MapTemplateResolver(templateMap);
  }

  /**
   * Constructs a template resolver from some templates.
   *
   * @param templates the templates
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver templates(final @NotNull Template @NotNull ... templates) {
    if (Objects.requireNonNull(templates, "templates").length == 0) return empty();
    return templates(Arrays.asList(templates));
  }

  /**
   * Constructs a template resolver from some templates.
   *
   * @param templates the templates
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver templates(final @NotNull Iterable<? extends Template> templates) {
    final Map<String, Template> templateMap = new HashMap<>();

    for (final Template template : Objects.requireNonNull(templates, "templates")) {
      Objects.requireNonNull(template, "templates must not contain null elements");
      templateMap.put(template.key(), template);
    }

    if (templateMap.isEmpty()) return empty();

    return new MapTemplateResolver(templateMap);
  }

  /**
   * Constructs a template resolver capable of resolving from multiple sources.
   *
   * @param templateResolvers the template resolvers
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver combining(final @NotNull TemplateResolver @NotNull ... templateResolvers) {
    if (Objects.requireNonNull(templateResolvers, "templateResolvers").length == 1)
      return Objects.requireNonNull(templateResolvers[0], "templateResolvers must not contain null elements");
    return new GroupedTemplateResolver(Arrays.asList(templateResolvers));
  }

  /**
   * Constructs a template resolver capable of resolving from multiple sources, in iteration order.
   *
   * <p>The provided iterable is copied. This means changes to the iterable will not reflect in the returned resolver.</p>
   *
   * @param templateResolvers the template resolvers
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver combining(final @NotNull Iterable<? extends TemplateResolver> templateResolvers) {
    final List<TemplateResolver> templateResolverList = new ArrayList<>();

    for (final TemplateResolver templateResolver : Objects.requireNonNull(templateResolvers, "templateResolvers")) {
      templateResolverList.add(Objects.requireNonNull(templateResolver, "templateResolvers cannot contain null elements"));
    }

    final int size = templateResolverList.size();
    if (size == 0) return empty();
    if (size == 1) return templateResolverList.get(0);
    return new GroupedTemplateResolver(templateResolvers);
  }

  /**
   * Constructs a template resolver capable of dynamically resolving templates.
   *
   * <p>The {@code resolver} function must return instances of String, {@link ComponentLike} or {@link Template}.
   * The resolver can return {@code null} to indicate it cannot resolve a template.</p>
   *
   * @param resolver the resolver
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver dynamic(final @NotNull Function<String, ?> resolver) {
    return new DynamicTemplateResolver(Objects.requireNonNull(resolver, "resolver"));
  }

  /**
   * Constructs a template resolver that uses the provided filter to prevent the resolving of templates that match the filter.
   *
   * @param templateResolver the template resolver
   * @param filter the filter
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver filtering(final @NotNull TemplateResolver templateResolver, final @NotNull Predicate<Template> filter) {
    return new FilteringTemplateResolver(Objects.requireNonNull(templateResolver, "templateResolver"), Objects.requireNonNull(filter, "filter"));
  }

  /**
   * An empty template resolver that will return {@code null} for all resolve attempts.
   *
   * @return the template resolver
   * @since 4.2.0
   */
  static @NotNull TemplateResolver empty() {
    return EmptyTemplateResolver.INSTANCE;
  }

  /**
   * Checks if this template resolver can resolve a template from a key.
   *
   * @param key the key
   * @return if a template can be resolved from this key
   * @since 4.2.0
   */
  boolean canResolve(final @NotNull String key);

  /**
   * Returns a template from a given key, if any exist.
   *
   * @param key the key
   * @return the template
   * @since 4.2.0
   */
  @Nullable Template resolve(final @NotNull String key);
}
