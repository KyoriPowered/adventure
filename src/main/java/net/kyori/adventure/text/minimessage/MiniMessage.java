/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

/**
 * MiniMessage is a textual representation of components. This class allows you to serialize and deserialize them, strip
 * or escape them, and even supports a markdown like format.
 */
public interface MiniMessage extends ComponentSerializer<Component, Component, String>, Buildable<MiniMessage, MiniMessage.Builder> {

  /**
   * Gets a simple instance without markdown support
   *
   * @return a simple instance
   */
  static @NonNull MiniMessage get() {
    return MiniMessageImpl.INSTANCE;
  }

  /**
   * Gets an instance with markdown support
   *
   * @return a instance of markdown support
   */
  static @NonNull MiniMessage markdown() {
    return MiniMessageImpl.MARKDOWN;
  }

  /**
   * Creates an custom instances without markdown support and the given transformations
   * @param types the transformations
   * @return your very own custom MiniMessage instance
   */
  @SafeVarargs
  static @NonNull MiniMessage withTransformations(TransformationType<? extends Transformation>... types) {
    return new MiniMessageImpl(false, new TransformationRegistry(types));
  }

  /**
   * Creates an custom instances with markdown support and the given transformations
   * @param types the transformations
   * @return your very own custom MiniMessage instance
   */
  @SafeVarargs
  static @NonNull MiniMessage markdownWithTransformations(TransformationType<? extends Transformation>... types) {
    return new MiniMessageImpl(false, new TransformationRegistry(types));
  }

  /**
   * Escapes all tokens in the input message, so that they are ignored in deserialization. Useful for untrusted input.
   *
   * @param input the input message, with tokens
   * @return the output, with escaped tokens
   */
  @NonNull String escapeTokens(final @NonNull String input);

  /**
   * Removes all tokens in the input message. Useful for untrusted input.
   *
   * @param input the input message, with tokens
   * @return the output, without tokens
   */
  @NonNull String stripTokens(final @NonNull String input);

  /**
   * Parses a string into an component.
   *
   * @param input the input string
   * @return the output component
   */
  default Component parse(final @NonNull String input) {
    return deserialize(input);
  }

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   */
  @NonNull Component parse(final @NonNull String input, final @NonNull String... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   */
  @NonNull Component parse(final @NonNull String input, final @NonNull Map<String, String> placeholders);

  /**
   * Parses a string into an component, allows passing placeholders using templates (which support components)
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   */
  @NonNull Component parse(final @NonNull String input, final @NonNull Template... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders using templates (which support components)
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   */
  @NonNull Component parse(final @NonNull String input, final @NonNull List<Template> placeholders);

  /**
   * Creates a new {@link MiniMessage.Builder}.
   *
   * @return a builder
   */
  static Builder builder() {
    return new MiniMessageImpl.BuilderImpl();
  }

  /**
   * A builder for {@link MiniMessage}.
   */
  interface Builder extends Buildable.Builder<MiniMessage> {

    /**
     * Adds markdown support
     *
     * @return this builder
     */
    @NonNull Builder markdown();

    /**
     * Removes all default transformations, allowing you to create a customized set of transformations
     *
     * @return this builder
     */
    @NonNull Builder removeDefaultTransformations();

    /**
     * Adds the given transformation
     *
     * @param type the type of transformation to add
     *
     * @return this builder
     */
    @NonNull Builder transformation(TransformationType<? extends Transformation> type);

    /**
     * Adds the given transformations
     *
     * @param types the types of transformations to add
     *
     * @return this builder
     */
    @NonNull Builder transformations(TransformationType<? extends Transformation>... types);

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     */
    @Override
    @NonNull MiniMessage build();
  }
}
