/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MiniMessage is a textual representation of components.
 *
 * <p>This class allows you to serialize and deserialize them, strip
 * or escape them.</p>
 *
 * @since 4.10.0
 */
public interface MiniMessage extends ComponentSerializer<Component, Component, String>, Buildable<MiniMessage, MiniMessage.Builder> {

  /**
   * Gets a simple instance without markdown support.
   *
   * @return a simple instance
   * @since 4.10.0
   */
  static @NotNull MiniMessage miniMessage() {
    return MiniMessageImpl.INSTANCE;
  }

  /**
   * Escapes all known tokens in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * <p>Only globally known tokens will be escaped. Use the overload that takes a {@link TagResolver} if any custom tags should be handled.</p>
   *
   * @param input the input message, with tokens
   * @return the output, with escaped tokens
   * @since 4.10.0
   */
  @NotNull String escapeTokens(final @NotNull String input);

  /**
   * Escapes all known tokens in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with tokens
   * @param tags a tag resolver to provide any additional tags that can be understood, combined with the overall tag resolver
   * @return the output, with escaped tokens
   * @since 4.10.0
   */
  @NotNull String escapeTokens(final @NotNull String input, final @NotNull TagResolver tags);

  /**
   * Removes all supported tokens in the input message.
   *
   * <p>Useful for untrusted input.</p>
   *
   * <p>Only globally known tokens will be stripped. Use the overload that takes a {@link TagResolver} if any custom tags should be handled.</p>
   *
   * @param input the input message, with tokens
   * @return the output, without tokens
   * @since 4.10.0
   */
  @NotNull String stripTokens(final @NotNull String input);

  /**
   * Removes all known tokens in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with tokens
   * @param tags a tag resolver to provide any additional tags that can be understood, combined with the overall tag resolver
   * @return the output, without tokens
   * @since 4.10.0
   */
  @NotNull String stripTokens(final @NotNull String input, final @NotNull TagResolver tags);

  /**
   * Deserializes a string into a component, with a tag resolver to parse tags of the form {@code <key>}.
   *
   * <p>Tags will be resolved from the resolver parameter before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param tagResolver the tag resolver for any additional tags to handle.
   * @return the output component
   * @since 4.10.0
   */
  @NotNull Component deserialize(final @NotNull String input, final @NotNull TagResolver tagResolver);

  /**
   * Creates a new {@link MiniMessage.Builder}.
   *
   * @return a builder
   * @since 4.10.0
   */
  static Builder builder() {
    return new MiniMessageImpl.BuilderImpl();
  }

  /**
   * A builder for {@link MiniMessage}.
   *
   * @since 4.10.0
   */
  interface Builder extends Buildable.Builder<MiniMessage> {

    /**
     * Set the known tags to the provided tag resolver.
     *
     * @param tags the tag resolver to use
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder tags(final @NotNull TagResolver tags);

    /**
     * Add to the set of known tags this MiniMessage instance can use.
     *
     * @param adder a function operating on a builder containing currently known tags
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder tags(final @NotNull Consumer<TagResolver.Builder> adder);

    /**
     * Allows to enable strict mode (disabled by default).
     *
     * <p>By default, MiniMessage will allow {@link net.kyori.adventure.text.minimessage.tag.Inserting#allowsChildren() child-allowing} tags to be implicitly closed. When strict mode
     * is enabled, all child-allowing tags which are {@code <opened>} must be explicitly {@code </closed>} as well.</p>
     *
     * @param strict if strict mode should be enabled
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder strict(final boolean strict);

    /**
     * Print debug information to the given output (disabled by default).
     *
     * <p>Debug output includes detailed information about the parsing process to help debug parser behavior.</p>
     *
     * <p>The consumer will receive line fragments terminated by {@code LF}, not complete lines.
     * This avoids string concatenation within debug output generation. If the consumer is {@code null}, no debug information will be generated.</p>
     *
     * @param debugOutput if debug mode should be enabled
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder debug(final @Nullable Consumer<String> debugOutput);

    /**
     * If in lenient mode, MiniMessage will output helpful messages.
     *
     * <p>This method allows you to change how they should be printed. By default, they will be printed to standard out.</p>
     *
     * @param consumer the error message consumer
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder parsingErrorMessageConsumer(final @NotNull Consumer<List<String>> consumer);

    /**
     * Specify a function that takes the component at the end of the parser process.
     * <p>By default, this compacts the resulting component with {@link Component#compact()}.</p>
     *
     * @param postProcessor method run at the end of parsing
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder postProcessor(final @NotNull UnaryOperator<Component> postProcessor);

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     * @since 4.10.0
     */
    @Override
    @NotNull MiniMessage build();
  }
}
