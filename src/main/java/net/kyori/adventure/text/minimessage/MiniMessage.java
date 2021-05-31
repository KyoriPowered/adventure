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

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.markdown.MarkdownFlavor;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * MiniMessage is a textual representation of components.
 *
 * <p>This class allows you to serialize and deserialize them, strip
 * or escape them, and even supports a markdown like format.</p>
 *
 * @since 4.0.0
 */
public interface MiniMessage extends ComponentSerializer<Component, Component, String>, Buildable<MiniMessage, MiniMessage.Builder> {

  /**
   * Gets a simple instance without markdown support.
   *
   * @return a simple instance
   * @since 4.0.0
   */
  static @NonNull MiniMessage get() {
    return MiniMessageImpl.INSTANCE;
  }

  /**
   * Gets an instance with markdown support.
   *
   * <p>Uses {@link net.kyori.adventure.text.minimessage.markdown.GithubFlavor}.<br>
   * For other flavors, see {@link #withMarkdownFlavor(MarkdownFlavor)} or the builder.</p>
   *
   * @return a instance of markdown support
   * @since 4.0.0
   */
  static @NonNull MiniMessage markdown() {
    return MiniMessageImpl.MARKDOWN;
  }

  /**
   * Creates an custom instances with markdown supported by the given markdown flavor.
   *
   * @param markdownFlavor the markdown flavor
   * @return your very own custom MiniMessage instance
   * @since 4.0.0
   */
  static @NonNull MiniMessage withMarkdownFlavor(final MarkdownFlavor markdownFlavor) {
    return new MiniMessageImpl(true, markdownFlavor, new TransformationRegistry(), MiniMessageImpl.DEFAULT_PLACEHOLDER_RESOLVER, false, null, MiniMessageImpl.DEFAULT_ERROR_CONSUMER);
  }

  /**
   * Creates an custom instances without markdown support and the given transformations.
   *
   * @param types the transformations
   * @return your very own custom MiniMessage instance
   * @since 4.1.0
   */
  @SafeVarargs
  static @NonNull MiniMessage withTransformations(final TransformationType<? extends Transformation>... types) {
    return new MiniMessageImpl(false, MarkdownFlavor.defaultFlavor(), new TransformationRegistry(types), MiniMessageImpl.DEFAULT_PLACEHOLDER_RESOLVER, false, null, MiniMessageImpl.DEFAULT_ERROR_CONSUMER);
  }

  /**
   * Creates an custom instances with markdown support and the given transformations.
   *
   * @param types the transformations
   * @return your very own custom MiniMessage instance
   * @since 4.1.0
   */
  @SafeVarargs
  static @NonNull MiniMessage markdownWithTransformations(final TransformationType<? extends Transformation>... types) {
    return new MiniMessageImpl(true, MarkdownFlavor.defaultFlavor(), new TransformationRegistry(types), MiniMessageImpl.DEFAULT_PLACEHOLDER_RESOLVER, false, null, MiniMessageImpl.DEFAULT_ERROR_CONSUMER);
  }

  /**
   * Creates an custom instances with markdown support (with the given flavor) and the given transformations.
   *
   * @param markdownFlavor the markdown flavor to use
   * @param types the transformations
   * @return your very own custom MiniMessage instance
   * @since 4.1.0
   */
  @SafeVarargs
  static @NonNull MiniMessage markdownWithTransformations(final MarkdownFlavor markdownFlavor, final TransformationType<? extends Transformation>... types) {
    return new MiniMessageImpl(true, markdownFlavor, new TransformationRegistry(types), MiniMessageImpl.DEFAULT_PLACEHOLDER_RESOLVER, false, null, MiniMessageImpl.DEFAULT_ERROR_CONSUMER);
  }

  /**
   * Escapes all tokens in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with tokens
   * @return the output, with escaped tokens
   * @since 4.0.0
   */
  @NonNull String escapeTokens(final @NonNull String input);

  /**
   * Removes all tokens in the input message.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with tokens
   * @return the output, without tokens
   * @since 4.0.0
   */
  @NonNull String stripTokens(final @NonNull String input);

  /**
   * Parses a string into an component.
   *
   * @param input the input string
   * @return the output component
   * @since 4.0.0
   */
  default Component parse(final @NonNull String input) {
    return this.deserialize(input);
  }

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs.
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.1.0
   */
  @NonNull Component parse(final @NonNull String input, final @NonNull String... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs.
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.1.0
   */
  @NonNull Component parse(final @NonNull String input, final @NonNull Map<String, String> placeholders);

  /**
   * Parses a string into an component, allows passing placeholders using key component pairs.
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.1.0
   */
  @NonNull Component parse(@NonNull String input, @NonNull Object... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders using templates (which support components).
   * MiniMessage parses placeholders from following syntax: {@code <placeholder_name>} where placeholder_name is {@link Template#key()}
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.0.0
   */
  @NonNull Component parse(final @NonNull String input, final @NonNull Template... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders using templates (which support components).
   * MiniMessage parses placeholders from following syntax: {@code <placeholder_name>} where placeholder_name is {@link Template#key()}
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.0.0
   */
  @NonNull Component parse(final @NonNull String input, final @NonNull List<Template> placeholders);

  /**
   * Creates a new {@link MiniMessage.Builder}.
   *
   * @return a builder
   * @since 4.0.0
   */
  static Builder builder() {
    return new MiniMessageImpl.BuilderImpl();
  }

  /**
   * A builder for {@link MiniMessage}.
   *
   * @since 4.0.0
   */
  interface Builder extends Buildable.Builder<MiniMessage> {

    /**
     * Adds markdown support.
     *
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder markdown();

    /**
     * Removes all default transformations, allowing you to create a customized set of transformations.
     *
     * @return this builder
     * @since 4.1.0
     */
    @NonNull Builder removeDefaultTransformations();

    /**
     * Adds the given transformation.
     *
     * @param type the type of transformation to add
     * @return this builder
     * @since 4.1.0
     */
    @NonNull Builder transformation(final TransformationType<? extends Transformation> type);

    /**
     * Adds the given transformations.
     *
     * @param types the types of transformations to add
     * @return this builder
     * @since 4.1.0
     */
    @SuppressWarnings("unchecked")
    @NonNull Builder transformations(final TransformationType<? extends Transformation>... types);

    /**
     * Sets the markdown flavor that should be used to parse markdown.
     *
     * @param markdownFlavor the markdown flavor to use
     * @return this builder
     * @since 4.1.0
     */
    @NonNull Builder markdownFlavor(final MarkdownFlavor markdownFlavor);

    /**
     * Sets the placeholder resolve that should handle all (unresolved) placeholders.
     * <br>
     * It needs to return a component
     *
     * @param placeholderResolver the markdown flavor to use
     * @return this builder
     * @since 4.1.0
     */
    @NonNull Builder placeholderResolver(final Function<String, ComponentLike> placeholderResolver);

    /**
     * Allows to enable strict mode (disabled by default)
     * <br>
     * By default, MiniMessage will allow non-{@link net.kyori.adventure.text.minimessage.transformation.Inserting Inserting} tags to be implicitly closed. When strict mode
     * is enabled, all non-inserting tags which are {@code <opened>} must be explicitly {@code </closed>} as well.
     *
     * @param strict if strict mode should be enabled
     * @return this builder
     * @since 4.1.0
     */
    @NonNull Builder strict(boolean strict);

    /**
     * Print debug information to the given output (disabled by default)
     * <br>
     * Debug output includes detailed information about the parsing process to help debug parser behavior.
     *
     * @param debugOutput if debug mode should be enabled
     * @return this builder
     * @since 4.2.0
     */
    @NonNull Builder debug(Appendable debugOutput);

    /**
     * If in lenient mode, MiniMessage will output helpful messages. This method allows you to change how they should be printed. By default, they will be printed to standard out.
     *
     * @param consumer the error message consumer
     * @return this builder
     * @since 4.1.0
     */
    @NonNull Builder parsingErrorMessageConsumer(final Consumer<List<String>> consumer);

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     * @since 4.0.0
     */
    @Override
    @NonNull MiniMessage build();
  }
}
