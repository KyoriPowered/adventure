/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translatable;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A component that can display translated text.
 *
 * <p>This component consists of:</p>
 * <dl>
 *   <dt>key</dt>
 *   <dd>a translation key used together with the viewer locale to fetch a translated string.</dd>
 *   <dt>args(optional)</dt>
 *   <dd>components that can be used as arguments in the translated string.
 *   <p>(e.g "You picked up <b>{0}</b>." -&#62; "You picked up <b>Carrot</b>.")</p></dd>
 * </dl>
 *
 * <p>Displaying this component through an {@link Audience} will run it through the {@link GlobalTranslator} by default,
 * rendering the key as translated text if a translation with a key matching this components key is found in the viewers locale,
 * optionally switching arguments with any placeholders in the discovered translation. If no translation is registered for the viewers locale
 * adventure will first try to find similar locales that has a valid translation, and then find a translation in the default language({@link TranslationRegistry#defaultLocale(Locale) relevant method}).</p>
 *
 * <p>In addition to the initial attempts, if no translation is found in the serverside registry,
 * the translation key and arguments will be passed through to the client which will perform translation using any
 * keys defined in an active resource pack. (Hint: vanilla Minecraft is also considered a resource pack)</p>
 *
 * @see GlobalTranslator
 * @see TranslationRegistry
 * @since 4.0.0
 */
public interface TranslatableComponent extends BuildableComponent<TranslatableComponent, TranslatableComponent.Builder>, ScopedComponent<TranslatableComponent> {
  /**
   * Gets the translation key.
   *
   * @return the translation key
   * @since 4.0.0
   */
  @NotNull String key();

  /**
   * Sets the translation key.
   *
   * @param translatable the translatable object to get the key from
   * @return a translatable component
   * @since 4.8.0
   */
  @Contract(pure = true)
  default @NotNull TranslatableComponent key(final @NotNull Translatable translatable) {
    return this.key(Objects.requireNonNull(translatable, "translatable").translationKey());
  }

  /**
   * Sets the translation key.
   *
   * @param key the translation key
   * @return a translatable component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NotNull TranslatableComponent key(final @NotNull String key);

  /**
   * Gets the unmodifiable list of translation arguments.
   *
   * @return the unmodifiable list of translation arguments
   * @since 4.0.0
   */
  @NotNull List<Component> args();

  /**
   * Sets the translation arguments for this component.
   *
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NotNull TranslatableComponent args(final @NotNull ComponentLike@NotNull... args);

  /**
   * Sets the translation arguments for this component.
   *
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NotNull TranslatableComponent args(final @NotNull List<? extends ComponentLike> args);

  /**
   * Gets the translation fallback text for this component.
   * The fallback text will be shown when the client doesn't know the
   * translation key used in the translatable component.
   *
   * @return the fallback string
   * @since 4.13.0
   * @sinceMinecraft 1.19.4
   */
  @Nullable String fallback();

  /**
   * Sets the translation fallback text for this component.
   * The fallback text will be shown when the client doesn't know the
   * translation key used in the translatable component.
   *
   * @param fallback the fallback string
   * @return a translatable component
   * @since 4.13.0
   * @sinceMinecraft 1.19.4
   */
  @Contract(pure = true)
  @NotNull TranslatableComponent fallback(final @Nullable String fallback);

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("key", this.key()),
        ExaminableProperty.of("args", this.args()),
        ExaminableProperty.of("fallback", this.fallback())
      ),
      BuildableComponent.super.examinableProperties()
    );
  }

  /**
   * A text component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends ComponentBuilder<TranslatableComponent, Builder> {
    /**
     * Sets the translation key.
     *
     * @param translatable the translatable object to get the key from
     * @return this builder
     * @since 4.8.0
     */
    @Contract(pure = true)
    default @NotNull Builder key(final @NotNull Translatable translatable) {
      return this.key(Objects.requireNonNull(translatable, "translatable").translationKey());
    }

    /**
     * Sets the translation key.
     *
     * @param key the translation key
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder key(final @NotNull String key);

    /**
     * Sets the translation args.
     *
     * @param arg the translation arg
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder args(final @NotNull ComponentBuilder<?, ?> arg);

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @SuppressWarnings("checkstyle:GenericWhitespace")
    @NotNull Builder args(final @NotNull ComponentBuilder<?, ?>@NotNull... args);

    /**
     * Sets the translation args.
     *
     * @param arg the translation arg
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder args(final @NotNull Component arg);

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder args(final @NotNull ComponentLike@NotNull... args);

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder args(final @NotNull List<? extends ComponentLike> args);

    /**
     * Sets the translation fallback text.
     * The fallback text will be shown when the client doesn't know the
     * translation key used in the translatable component.
     *
     * @param fallback the fallback string
     * @return this builder
     * @since 4.13.0
     * @sinceMinecraft 1.19.4
     */
    @Contract("_ -> this")
    @NotNull Builder fallback(final @Nullable String fallback);
  }
}
