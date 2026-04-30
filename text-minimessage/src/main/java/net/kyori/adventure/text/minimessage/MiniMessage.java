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
package net.kyori.adventure.text.minimessage;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.VirtualComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.minimessage.tree.Node;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Index;
import net.kyori.adventure.util.PlatformAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * MiniMessage is a textual representation of components.
 *
 * <p>This class allows you to serialize and deserialize them, strip
 * or escape them.</p>
 *
 * @since 4.10.0
 */
public interface MiniMessage extends ComponentSerializer<Component, Component, String> {
  /**
   * Gets a simple instance with default settings.
   *
   * @return a simple instance
   * @since 4.10.0
   */
  static MiniMessage miniMessage() {
    return MiniMessageImpl.Instances.INSTANCE;
  }

  /**
   * Gets an instance based on a specific preset.
   *
   * @param preset the preset to use
   * @return an instance
   * @since 5.0.0
   */
  static MiniMessage miniMessage(final Preset preset) {
    Objects.requireNonNull(preset, "preset");
    return MiniMessageImpl.Instances.PRESETS.get(preset);
  }

  /**
   * A variety of presets for configuring MiniMessage.
   *
   * <p>
   *   These presets can be used to get a default confuguration for MiniMessage by
   *   using the {@link #miniMessage(Preset)} method.
   * </p>
   *
   * <p>
   *   Alternatively, you can get a pre-configured builder based on a preset.
   *   This can be used to make your own custom MiniMessage instance whilst still keeping
   *   any configuration provided by a preset, such as post-processing.
   *   An example of how to do this is provided below.
   * </p>
   * <pre>
   * {@code
   * // Obtain the pre-configured builder.
   * final MiniMessage.Builder builder = MiniMessage.builder(MiniMessage.Preset.NON_INTERACTABLE);
   *
   * // Add your own tags or override other settings if needed.
   * builder.editTags(tags -> {
   *   tags
   *     .resolver(new MyCustomTagResolver())
   *     .resolver(fetchExternalTags());
   * });
   *
   * // Then build your MiniMessage instance!
   * final MiniMessage myMiniMessageInstance = builder.build();
   * }
   * </pre>
   *
   * @see #miniMessage(Preset)
   * @see #builder(Preset)
   * @since 5.0.0
   */
  enum Preset implements Consumer<Builder> {
    /**
     * The default preset, containing all features of MiniMessage.
     *
     * @since 5.0.0
     */
    DEFAULT {
      @Override
      public void accept(final Builder builder) {
        // no op, we keep default settings
      }
    },

    /**
     * A preset that disables all interactive features, such as hover and click events.
     *
     * <p>This preset also removes hover and click events from the returned component
     * using a custom post-processor.</p>
     *
     * @since 5.0.0
     */
    NON_INTERACTABLE {
      @Override
      public void accept(final Builder builder) {
        Objects.requireNonNull(builder, "builder");
        builder.tags(StandardTags.forPreset(this));
        builder.postProcessor(component ->
          component
            .toBuilder()
            .applyDeep(child -> child.clickEvent(null).hoverEvent(null).insertion(null))
            .build()
            .compact()
        );
      }
    },

    /**
     * A preset that disables all non-text component types and only allows formatting
     * text components (i.e., color, shadow, decorations, fonts).
     *
     * <p>This preset also performs the following modifications to the resulting component
     * using a custom post-processor:</p>
     * <ul>
     *   <li>Removes click events, hover events, and insertions from all text components.</li>
     *   <li>Filters out any non-text components.</li>
     * </ul>
     *
     * @since 5.0.0
     */
    FORMATTED_TEXT {
      @Override
      public void accept(final Builder builder) {
        Objects.requireNonNull(builder, "builder");
        builder.tags(StandardTags.forPreset(this));
        builder.postProcessor(component ->
          Component
            .textOfChildren(component) // Wrap it in the empty component so we don't care about the root when mapping.
            .toBuilder()
            .mapChildrenDeep(child -> {
              if (child instanceof TextComponent && !(child instanceof VirtualComponent)) {
                return child.toBuilder().clickEvent(null).hoverEvent(null).insertion(null).build();
              } else {
                return Component.empty();
              }
            })
            .build()
            .compact()
        );
      }
    };

    /**
     * The name map.
     *
     * <p>This can be used to easily make configurable presets.</p>
     *
     * @since 5.0.0
     */
    public static final Index<String, Preset> NAMES = Index.create(Preset.class, Preset::name);
  }

  /**
   * Escapes all known tags in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * <p>Only globally known tags will be escaped. Use the overload that takes a {@link TagResolver} if any custom tags should be handled.</p>
   *
   * @param input the input message, with potential tags
   * @return the output, with escaped tags
   * @since 4.10.0
   */
  String escapeTags(final String input);

  /**
   * Escapes all known tags in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with potential tags
   * @param tagResolver the tag resolver for any additional tags to handle
   * @return the output, with escaped tags
   * @since 4.10.0
   */
  String escapeTags(final String input, final TagResolver tagResolver);

  /**
   * Escapes all known tags in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with potential tags
   * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
   * @return the output, with escaped tags
   * @since 4.10.0
   */
  default String escapeTags(final String input, final TagResolver... tagResolvers) {
    return this.escapeTags(input, TagResolver.resolver(tagResolvers));
  }

  /**
   * Removes all supported tags in the input message.
   *
   * <p>Useful for untrusted input.</p>
   *
   * <p>Only globally known tags will be stripped. Use the overload that takes a {@link TagResolver} if any custom tags should be handled.</p>
   *
   * @param input the input message, with potential tags
   * @return the output, without tags
   * @since 4.10.0
   */
  String stripTags(final String input);

  /**
   * Removes all known tags in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with tags
   * @param tagResolver the tag resolver for any additional tags to handle
   * @return the output, without tags
   * @since 4.10.0
   */
  String stripTags(final String input, final TagResolver tagResolver);

  /**
   * Removes all known tags in the input message, so that they are ignored in deserialization.
   *
   * <p>Useful for untrusted input.</p>
   *
   * @param input the input message, with tags
   * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
   * @return the output, without tags
   * @since 4.10.0
   */
  default String stripTags(final String input, final TagResolver... tagResolvers) {
    return this.stripTags(input, TagResolver.resolver(tagResolvers));
  }

  /**
   * Deserializes a string into a component, with a target.
   *
   * @param input the input string
   * @param target the target of the deserialization
   * @return the output component
   * @since 4.17.0
   */
  Component deserialize(final String input, final Pointered target);

  /**
   * Deserializes a string into a component, with a tag resolver to parse tags of the form {@code <key>}.
   *
   * <p>Tags will be resolved from the resolver parameter before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param tagResolver the tag resolver for any additional tags to handle
   * @return the output component
   * @since 4.10.0
   */
  Component deserialize(final String input, final TagResolver tagResolver);

  /**
   * Deserializes a string into a component, with a tag resolver to parse tags of the form {@code <key>} and a target.
   *
   * <p>Tags will be resolved from the resolver parameter before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param target the target of the deserialization
   * @param tagResolver the tag resolver for any additional tags to handle
   * @return the output component
   * @since 4.17.0
   */
  Component deserialize(final String input, final Pointered target, final TagResolver tagResolver);

  /**
   * Deserializes a string into a component, with tag resolvers to parse tags of the form {@code <key>}.
   *
   * <p>Tags will be resolved from the resolver parameters before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
   * @return the output component
   * @since 4.10.0
   */
  default Component deserialize(final String input, final TagResolver... tagResolvers) {
    return this.deserialize(input, TagResolver.resolver(tagResolvers));
  }

  /**
   * Deserializes a string into a component, with tag resolvers to parse tags of the form {@code <key>} and a target.
   *
   * <p>Tags will be resolved from the resolver parameters before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param target the target of the deserialization
   * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
   * @return the output component
   * @since 4.17.0
   */
  default Component deserialize(final String input, final Pointered target, final TagResolver... tagResolvers) {
    return this.deserialize(input, target, TagResolver.resolver(tagResolvers));
  }

  /**
   * Deserializes a string into a tree of parsed elements.
   * This is intended for inspecting the output of the parser for debugging purposes.
   *
   * @param input the input string
   * @return the root of the resulting tree
   * @since 4.10.0
   */
  Node.Root deserializeToTree(final String input);

  /**
   * Deserializes a string into a tree of parsed elements, with a target.
   * This is intended for inspecting the output of the parser for debugging purposes.
   *
   * @param input the input string
   * @param target the target of the deserialization
   * @return the root of the resulting tree
   * @since 4.17.0
   */
  Node.Root deserializeToTree(final String input, final Pointered target);

  /**
   * Deserializes a string into a tree of parsed elements, with a tag resolver to parse tags of the form {@code <key>}.
   * This is intended for inspecting the output of the parser for debugging purposes.
   *
   * <p>Tags will be resolved from the resolver parameter before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param tagResolver the tag resolver for any additional tags to handle
   * @return the root of the resulting tree
   * @since 4.10.0
   */
  Node.Root deserializeToTree(final String input, final TagResolver tagResolver);

  /**
   * Deserializes a string into a tree of parsed elements, with a tag resolver to parse tags of the form {@code <key>} and a target.
   * This is intended for inspecting the output of the parser for debugging purposes.
   *
   * <p>Tags will be resolved from the resolver parameter before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param target the target of the deserialization
   * @param tagResolver the tag resolver for any additional tags to handle
   * @return the root of the resulting tree
   * @since 4.17.0
   */
  Node.Root deserializeToTree(final String input, final Pointered target, final TagResolver tagResolver);

  /**
   * Deserializes a string into a tree of parsed elements, with a tag resolver to parse tags of the form {@code <key>}.
   * This is intended for inspecting the output of the parser for debugging purposes.
   *
   * <p>Tags will be resolved from the resolver parameter before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
   * @return the root of the resulting tree
   * @since 4.10.0
   */
  default Node.Root deserializeToTree(final String input, final TagResolver... tagResolvers) {
    return this.deserializeToTree(input, TagResolver.resolver(tagResolvers));
  }

  /**
   * Deserializes a string into a tree of parsed elements, with a tag resolver to parse tags of the form {@code <key>}.
   * This is intended for inspecting the output of the parser for debugging purposes.
   *
   * <p>Tags will be resolved from the resolver parameter before the resolver provided in the builder is used.</p>
   *
   * @param input the input string
   * @param target the target of the deserialization
   * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
   * @return the root of the resulting tree
   * @since 4.17.0
   */
  default Node.Root deserializeToTree(final String input, final Pointered target, final TagResolver... tagResolvers) {
    return this.deserializeToTree(input, target, TagResolver.resolver(tagResolvers));
  }

  /**
   * Returns if this MiniMessage instance is in strict mode.
   *
   * @return if the instance is in strict mode
   * @see Builder#strict(boolean)
   * @since 4.15.0
   */
  boolean strict();

  /**
   * Returns the base tag resolver of this MiniMessage instance.
   *
   * @return the base tag resolver
   * @since 4.15.0
   */
  TagResolver tags();

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
   * Creates a new {@link MiniMessage.Builder} pre-configured for a specific preset.
   *
   * @param preset the preset
   * @return the builder
   * @since 5.0.0
   */
  static Builder builder(final Preset preset) {
    Objects.requireNonNull(preset, "preset");
    final Builder builder = MiniMessage.builder();
    preset.accept(builder);
    return builder;
  }

  /**
   * A builder for {@link MiniMessage}.
   *
   * @since 4.10.0
   */
  interface Builder extends AbstractBuilder<MiniMessage> {

    /**
     * Set the known tags to the provided tag resolver.
     *
     * <p>This resolver determines the base set of tags known to this {@link MiniMessage} instance.
     * Any resolvers passed to the {@link MiniMessage#deserialize(String, TagResolver)} method will override this resolver.</p>
     *
     * @param tags the tag resolver to use
     * @return this builder
     * @since 4.10.0
     */
    Builder tags(final TagResolver tags);

    /**
     * Add to the set of known tags this MiniMessage instance can use.
     *
     * @param adder a function operating on a builder containing currently known tags
     * @return this builder
     * @since 4.10.0
     */
    Builder editTags(final Consumer<TagResolver.Builder> adder);

    /**
     * Enables strict mode (disabled by default).
     *
     * <p>By default, MiniMessage will allow {@link net.kyori.adventure.text.minimessage.tag.Inserting#allowsChildren() child-allowing} tags to be implicitly closed. When strict mode
     * is enabled, all child-allowing tags which are {@code <opened>} must be explicitly {@code </closed>} as well.</p>
     *
     * <p>Additionally, the {@link net.kyori.adventure.text.minimessage.tag.ParserDirective#RESET reset tag} is disabled in this mode.
     * Any usage of this tag will throw a parser exception if strict mode is enabled.</p>
     *
     * @param strict if strict mode should be enabled
     * @return this builder
     * @since 4.10.0
     */
    Builder strict(final boolean strict);

    /**
     * Configures if MiniMessage should emit virtual components (enabled by default).
     *
     * <p>
     * Emitting virtual components may enable MiniMessage to more accurately reconstruct
     * the source string representation when serializing a component by inserting virtual components
     * during deserialization.
     * Emitting virtual components will, however, break equality to components deserialized from
     * MiniMessage instances that do not emit virtual components.
     * </p>
     *
     * @param emitVirtuals if virtual components should be emitted.
     * @return this builder.
     * @since 4.19.0
     */
    Builder emitVirtuals(final boolean emitVirtuals);

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
    Builder debug(final @Nullable Consumer<String> debugOutput);

    /**
     * Specify a function that takes the component at the end of the parser process.
     *
     * <p>By default, this compacts the resulting component with {@link Component#compact()}.</p>
     *
     * @param postProcessor method run at the end of parsing
     * @return this builder
     * @since 4.10.0
     */
    Builder postProcessor(final UnaryOperator<Component> postProcessor);

    /**
     * Specify a function that takes the string at the start of the parser process.
     *
     * <p>By default, this does absolutely nothing.</p>
     *
     * @param preProcessor method run at the start of parsing
     * @return this builder
     * @since 4.11.0
     */
    Builder preProcessor(final UnaryOperator<String> preProcessor);

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     * @since 4.10.0
     */
    @Override
    MiniMessage build();
  }

  /**
   * A {@link MiniMessage} service provider.
   *
   * @since 4.10.0
   * @hidden
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a standard {@link MiniMessage} instance.
     *
     * @return a {@link MiniMessage} instance
     * @since 4.10.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    MiniMessage miniMessage();

    /**
     * Initialize a {@link Builder} before it is returned to the API caller.
     *
     * @return a {@link Consumer} modifying a {@link Builder}
     * @since 4.10.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    Consumer<Builder> builder();
  }
}
