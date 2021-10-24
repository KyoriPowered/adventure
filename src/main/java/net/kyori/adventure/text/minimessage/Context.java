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

import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Carries needed context for minimessage around, ranging from debug info to the configured minimessage instance.
 *
 * @since 4.1.0
 */
public class Context {
  private final boolean strict;
  private final Appendable debugOutput;
  private ElementNode root;
  private final String originalMessage;
  private String replacedMessage;
  private final MiniMessage miniMessage;
  private final TemplateResolver templateResolver;
  private final UnaryOperator<Component> postProcessor;

  Context(final boolean strict, final Appendable debugOutput, final ElementNode root, final String originalMessage, final String replacedMessage, final MiniMessage miniMessage, final @NotNull TemplateResolver templateResolver, final UnaryOperator<Component> postProcessor) {
    this.strict = strict;
    this.debugOutput = debugOutput;
    this.root = root;
    this.originalMessage = originalMessage;
    this.replacedMessage = replacedMessage;
    this.miniMessage = miniMessage;
    this.templateResolver = templateResolver;
    this.postProcessor = postProcessor == null ? UnaryOperator.identity() : postProcessor;
  }

  /**
   * Init.
   *
   * @param strict if strict mode is enabled
   * @param input the input message
   * @param miniMessage the minimessage instance
   * @return the debug context
   * @since 4.1.0
   */
  public static Context of(final boolean strict, final String input, final MiniMessage miniMessage) {
    return new Context(strict, null, null, input, null, miniMessage, TemplateResolver.empty(), null);
  }

  /**
   * Init.
   *
   * @param strict if strict mode is enabled
   * @param debugOutput where to print debug output
   * @param input the input message
   * @param miniMessage the minimessage instance
   * @return the debug context
   * @since 4.1.0
   */
  public static Context of(final boolean strict, final Appendable debugOutput, final String input, final MiniMessage miniMessage) {
    return new Context(strict, debugOutput, null, input, null, miniMessage, TemplateResolver.empty(), null);
  }

  /**
   * Init.
   *
   * @param strict if strict mode is enabled
   * @param input the input message
   * @param miniMessage the minimessage instance
   * @param templates the templates passed to minimessage
   * @return the debug context
   * @since 4.1.0
   */
  public static Context of(final boolean strict, final String input, final MiniMessageImpl miniMessage, @NotNull final Template @Nullable [] templates) {
    return new Context(strict, null, null, input, null, miniMessage, templates == null ? TemplateResolver.empty() : TemplateResolver.templates(templates), null);
  }

  /**
   * Init.
   *
   * @param strict if strict mode is enabled
   * @param debugOutput where to print debug output
   * @param input the input message
   * @param miniMessage the minimessage instance
   * @param templates the templates passed to minimessage
   * @return the debug context
   * @since 4.2.0
   */
  public static Context of(final boolean strict, final Appendable debugOutput, final String input, final MiniMessageImpl miniMessage, @NotNull final Template @Nullable [] templates) {
    return new Context(strict, debugOutput, null, input, null, miniMessage, templates == null ? TemplateResolver.empty() : TemplateResolver.templates(templates), null);
  }

  /**
   * Init.
   *
   * @param strict if strict mode is enabled
   * @param debugOutput where to print debug output
   * @param input the input message
   * @param miniMessage the minimessage instance
   * @param templateResolver the template resolver passed to minimessage
   * @return the debug context
   * @since 4.2.0
   */
  public static Context of(final boolean strict, final Appendable debugOutput, final String input, final MiniMessageImpl miniMessage, final TemplateResolver templateResolver) {
    return new Context(strict, debugOutput, null, input, null, miniMessage, templateResolver, null);
  }

  /**
   * Init.
   *
   * @param strict if strict mode is enabled
   * @param debugOutput where to print debug output
   * @param input the input message
   * @param miniMessage the minimessage instance
   * @param templateResolver the template resolver passed to minimessage
   * @param postProcessor callback ran at the end of parsing which could be used to compact the output
   * @return the debug context
   * @since 4.2.0
   */
  public static Context of(final boolean strict, final Appendable debugOutput, final String input, final MiniMessageImpl miniMessage, final TemplateResolver templateResolver, final UnaryOperator<Component> postProcessor) {
    return new Context(strict, debugOutput, null, input, null, miniMessage, templateResolver, postProcessor);
  }

  /**
   * Sets the root element.
   *
   * @param root the root element.
   * @since 4.1.0
   */
  public void root(final ElementNode root) {
    this.root = root;
  }

  /**
   * sets the replaced message.
   *
   * @param replacedMessage the replaced message
   * @since 4.1.0
   */
  public void replacedMessage(final String replacedMessage) {
    this.replacedMessage = replacedMessage;
  }

  /**
   * Returns strict mode.
   *
   * @return if strict mode is enabled
   * @since 4.1.0
   */
  public boolean strict() {
    return this.strict;
  }

  /**
   * Returns the appendable to print debug output to.
   *
   * @return the debug output to print to
   * @since 4.2.0
   */
  public Appendable debugOutput() {
    return this.debugOutput;
  }

  /**
   * Returns the root element.
   *
   * @return root
   * @since 4.1.0
   */
  public ElementNode tokens() {
    return this.root;
  }

  /**
   * Returns og message.
   *
   * @return ogMessage
   * @since 4.1.0
   * @deprecated for removal, use {@link #originalMessage()} instead
   */
  @Deprecated
  public String ogMessage() {
    return this.originalMessage();
  }

  /**
   * Returns original message.
   *
   * @return ogMessage
   * @since 4.2.0
   */
  public String originalMessage() {
    return this.originalMessage;
  }

  /**
   * Returns replaced message.
   *
   * @return replacedMessage
   * @since 4.1.0
   */
  public String replacedMessage() {
    return this.replacedMessage;
  }

  /**
   * Returns minimessage.
   *
   * @return minimessage
   * @since 4.1.0
   */
  public MiniMessage miniMessage() {
    return this.miniMessage;
  }

  /**
   * Returns the template resolver.
   *
   * @return the template resolver
   * @since 4.2.0
   */
  public @NotNull TemplateResolver templateResolver() {
    return this.templateResolver;
  }

  /**
   * Returns callback ran at the end of parsing which could be used to compact the output.
   *
   * @return Post-processing function
   * @since 4.2.0
   */
  public UnaryOperator<Component> postProcessor() {
    return this.postProcessor;
  }

  /**
   * Parses a MiniMessage using all the settings of this context, including templates.
   *
   * @param message the message to parse
   * @return the parsed message
   * @since 4.1.0
   */
  public Component parse(final String message) {
    return this.miniMessage.deserialize(message, this.templateResolver);
  }
}
