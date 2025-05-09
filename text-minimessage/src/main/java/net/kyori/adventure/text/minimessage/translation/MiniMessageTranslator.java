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
package net.kyori.adventure.text.minimessage.translation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.TranslationArgumentLike;
import net.kyori.adventure.text.VirtualComponent;
import net.kyori.adventure.text.VirtualComponentRenderer;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Translator} implementation that translates strings using MiniMessage.
 *
 * <p>To use this feature, you should extend this class, implementing the
 * {@link #getMiniMessageString(String, Locale)} method to return the MiniMessage string
 * for a given key and locale.
 * After that, you can use the translator as-is using
 * {@link #translate(TranslatableComponent, Locale)}, or automatically (depending on the
 * implementing platform) using the {@link GlobalTranslator}.</p>
 *
 * <p>This system supports arguments using {@code <arg:0>} tags (or {@code argument},
 * where {@code 0} is the index of the argument to use).
 * Alternatively, you can use named arguments by creating the translatable component
 * using returned {@code ComponentLike} instances provided by the methods available
 * in the {@link Argument} utility class.
 * The provided name will be available for use in a tag as {@code <name>}, in addition
 * to the index-based {@code arg} tag.
 * These tags will use {@link Tag#selfClosingInserting(Component)} to create self-closing
 * tags that insert a component representation of the argument.
 * This can also be used to add tag instances using {@link Argument#tag(String, Tag)}.</p>
 *
 * <p>By default, the {@link Context#target() target} will be a {@link Pointered}
 * instance that solely contains the {@link Locale} of the translation.
 * The locale can be obtained from the target using the {@link net.kyori.adventure.identity.Identity#LOCALE} pointer.
 * You can override the target by using {@link Argument#target(Pointered)}.</p>
 *
 * <p>You can also make arbitrary {@link TagResolver tag resolvers} available to the
 * deserialization process by using the {@code tagResolver} methods on {@link Argument}.
 * Note that these tag resolvers will not be available using the {@code <arg:0>}
 * index-based standard tag and will not cause the index to be incremented.
 * It is therefore recommended that you put these last in the translatable component
 * arguments to avoid potential confusion.</p>
 *
 * <p>An example of how you might construct a translatable component would be:</p>
 * <pre>{@code
 * Component.translatable(
 *   "my.translation.key", // the translation key, you'd return the MiniMessage string by implementing getMiniMessageString
 *   Component.text("hello"), // available as <arg:0> or <argument:0>
 *   Argument.string("today", "monday"), // available as <arg:1>, <argument:1> or <today>
 *   Argument.tag("danger", Tag.styling(NamedTextColor.RED)), // available as <arg:1>, <argument:1> or <red>, can be closed if needed
 *   Argument.tagResolver(StandardTags.pride()) // you can even add arbitrary tag resolvers!
 * );
 * }</pre>
 *
 * <p>For an easier way to create a MiniMessage translator, see {@link MiniMessageTranslationStore}.</p>
 *
 * @see Argument
 * @see Tag
 * @see TagResolver
 * @see Translator
 * @see GlobalTranslator
 * @since 4.20.0
 */
public abstract class MiniMessageTranslator implements Translator {
  private final MiniMessage miniMessage;

  /**
   * Constructor for a MiniMessageTranslator using the default MiniMessage instance.
   *
   * @see MiniMessage#miniMessage()
   * @since 4.20.0
   */
  public MiniMessageTranslator() {
    this(MiniMessage.miniMessage());
  }

  /**
   * Constructor for a MiniMessageTranslator using a specific MiniMessage instance.
   *
   * @param miniMessage the MiniMessage instance
   * @see MiniMessage#miniMessage()
   * @since 4.20.0
   */
  public MiniMessageTranslator(final @NotNull MiniMessage miniMessage) {
    this.miniMessage = Objects.requireNonNull(miniMessage, "miniMessage");
  }

  /**
   * Returns a raw MiniMessage string for the given key.
   *
   * <p>If no string is found for the given key, returning {@code null} will use the
   * {@link TranslatableComponent#fallback() translatable component's fallback} (or the
   * key itself).</p>
   *
   * @param key the key
   * @param locale the locale
   * @return the resulting MiniMessage string
   * @since 4.20.0
   */
  @SuppressWarnings("checkstyle:MethodName")
  protected abstract @Nullable String getMiniMessageString(final @NotNull String key, final @NotNull Locale locale);

  @Override
  public final @Nullable MessageFormat translate(final @NotNull String key, final @NotNull Locale locale) {
    return null;
  }

  @Override
  public final @Nullable Component translate(final @NotNull TranslatableComponent component, final @NotNull Locale locale) {
    final String miniMessageString = this.getMiniMessageString(component.key(), locale);

    if (miniMessageString == null) {
      return null;
    }

    Component resultingComponent;
    Pointered target = new LocalePointered(locale);

    final List<TranslationArgument> translationArguments = component.arguments();

    if (translationArguments.isEmpty()) {
      resultingComponent = this.miniMessage.deserialize(miniMessageString, target);
    } else {
      final TagResolver.Builder tagResolverBuilder = TagResolver.builder();
      final List<Tag> indexedArguments = new ArrayList<>(translationArguments.size());
      boolean targetAlreadyOverridden = false;

      for (final TranslationArgument argument : translationArguments) {
        final Object value = argument.value();

        if (value instanceof VirtualComponent) {
          final VirtualComponentRenderer<?> renderer = ((VirtualComponent) value).renderer();

          if (renderer instanceof MiniMessageTranslatorTarget) {
            if (targetAlreadyOverridden) {
              throw new IllegalArgumentException("Multiple Argument.target() translation arguments have been set!");
            }

            target = ((MiniMessageTranslatorTarget) renderer).pointered();
            targetAlreadyOverridden = true;
            continue;
          } else if (renderer instanceof MiniMessageTranslatorArgument<?>) {
            final MiniMessageTranslatorArgument<?> translatorArgument = (MiniMessageTranslatorArgument<?>) renderer;
            final Object data = translatorArgument.data();

            if (data instanceof TranslationArgumentLike) {
              final Tag tag = Tag.selfClosingInserting((TranslationArgumentLike) data);
              tagResolverBuilder.tag(translatorArgument.name(), tag);
              indexedArguments.add(tag);
              continue;
            } else if (data instanceof Tag) {
              final Tag tag = (Tag) data;
              tagResolverBuilder.tag(translatorArgument.name(), tag);
              indexedArguments.add(tag);
              continue;
            } else if (data instanceof TagResolver) {
              tagResolverBuilder.resolvers((TagResolver) data);
            } else {
              throw new IllegalArgumentException("Unknown translator argument type: " + data.getClass());
            }
          }
        }

        indexedArguments.add(Tag.selfClosingInserting(argument));
      }

      resultingComponent = this.miniMessage.deserialize(miniMessageString, target, new ArgumentTag(indexedArguments, tagResolverBuilder.build()));
    }

    final Style style = component.style();
    if (!style.isEmpty()) {
      resultingComponent = resultingComponent.applyFallbackStyle(style);
    }

    return resultingComponent.append(component.children());
  }
}
