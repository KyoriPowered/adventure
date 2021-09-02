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
package net.kyori.adventure.text.minimessage;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;

/**
 * MiniMessage is a textual representation of components.
 *
 * <p>This class allows you to serialize and deserialize them, strip
 * or escape them.</p>
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
  static @NotNull MiniMessage get() {
    return MiniMessageImpl.INSTANCE;
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
  @NotNull String escapeTokens(final @NotNull String input);

  /**
   * Removes all tokens in the input message.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with tokens
   * @return the output, without tokens
   * @since 4.0.0
   */
  @NotNull String stripTokens(final @NotNull String input);

  /**
   * Parses a string into an component.
   *
   * @param input the input string
   * @return the output component
   * @since 4.0.0
   */
  default Component parse(final @NotNull String input) {
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
  @NotNull Component parse(final @NotNull String input, final @NotNull String... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs.
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.1.0
   */
  @NotNull Component parse(final @NotNull String input, final @NotNull Map<String, String> placeholders);

  /**
   * Parses a string into an component, allows passing placeholders using key component pairs.
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.1.0
   */
  @NotNull Component parse(@NotNull String input, @NotNull Object... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders using templates (which support components).
   * MiniMessage parses placeholders from following syntax: {@code <placeholder_name>} where placeholder_name is {@link Template#key()}
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.0.0
   */
  @NotNull Component parse(final @NotNull String input, final @NotNull Template... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders using templates (which support components).
   * MiniMessage parses placeholders from following syntax: {@code <placeholder_name>} where placeholder_name is {@link Template#key()}
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   * @since 4.0.0
   */
  @NotNull Component parse(final @NotNull String input, final @NotNull List<Template> placeholders);

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
     * Uses the supplied transformation registry.
     *
     * @param transformationRegistry the transformation registry to use
     * @return this builder
     * @since 4.1.0
     */
    @NotNull Builder transformations(final TransformationRegistry transformationRegistry);

    /**
     * Sets the placeholder resolve that should handle all (unresolved) placeholders.
     * <br>
     * It needs to return a component
     *
     * @param placeholderResolver the placeholder resolver to use
     * @return this builder
     * @since 4.1.0
     */
    @NotNull Builder placeholderResolver(final Function<String, ComponentLike> placeholderResolver);

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
    @NotNull Builder strict(boolean strict);

    /**
     * Print debug information to the given output (disabled by default)
     * <br>
     * Debug output includes detailed information about the parsing process to help debug parser behavior.
     *
     * @param debugOutput if debug mode should be enabled
     * @return this builder
     * @since 4.2.0
     */
    @NotNull Builder debug(Appendable debugOutput);

    /**
     * If in lenient mode, MiniMessage will output helpful messages. This method allows you to change how they should be printed. By default, they will be printed to standard out.
     *
     * @param consumer the error message consumer
     * @return this builder
     * @since 4.1.0
     */
    @NotNull Builder parsingErrorMessageConsumer(final Consumer<List<String>> consumer);

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     * @since 4.0.0
     */
    @Override
    @NotNull MiniMessage build();
  }
}
