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

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class JoinConfigurationImpl implements JoinConfiguration {
  static final Function<ComponentLike, Component> DEFAULT_CONVERTOR = ComponentLike::asComponent;
  static final Predicate<ComponentLike> DEFAULT_PREDICATE = componentLike -> true;
  static final JoinConfigurationImpl NULL = new JoinConfigurationImpl();

  static final JoinConfiguration STANDARD_NEW_LINES = JoinConfiguration.separator(Component.newline());
  static final JoinConfiguration STANDARD_SPACES = JoinConfiguration.separator(Component.space());
  static final JoinConfiguration STANDARD_COMMA_SEPARATED = JoinConfiguration.separator(Component.text(","));
  static final JoinConfiguration STANDARD_COMMA_SPACE_SEPARATED = JoinConfiguration.separator(Component.text(", "));
  static final JoinConfiguration STANDARD_ARRAY_LIKE = JoinConfiguration.builder()
    .separator(Component.text(", "))
    .prefix(Component.text("["))
    .suffix(Component.text("]"))
    .build();

  private final Component prefix;
  private final Component suffix;
  private final Component separator;
  private final Component lastSeparator;
  private final Component lastSeparatorIfSerial;
  private final Function<ComponentLike, Component> convertor;
  private final Predicate<ComponentLike> predicate;
  private final Style rootStyle;

  private JoinConfigurationImpl() {
    this.prefix = null;
    this.suffix = null;
    this.separator = null;
    this.lastSeparator = null;
    this.lastSeparatorIfSerial = null;
    this.convertor = DEFAULT_CONVERTOR;
    this.predicate = DEFAULT_PREDICATE;
    this.rootStyle = Style.empty();
  }

  private JoinConfigurationImpl(final @NotNull BuilderImpl builder) {
    this.prefix = ComponentLike.unbox(builder.prefix);
    this.suffix = ComponentLike.unbox(builder.suffix);
    this.separator = ComponentLike.unbox(builder.separator);
    this.lastSeparator = ComponentLike.unbox(builder.lastSeparator);
    this.lastSeparatorIfSerial = ComponentLike.unbox(builder.lastSeparatorIfSerial);
    this.convertor = builder.convertor;
    this.predicate = builder.predicate;
    this.rootStyle = builder.rootStyle;
  }

  @Override
  public @Nullable Component prefix() {
    return this.prefix;
  }

  @Override
  public @Nullable Component suffix() {
    return this.suffix;
  }

  @Override
  public @Nullable Component separator() {
    return this.separator;
  }

  @Override
  public @Nullable Component lastSeparator() {
    return this.lastSeparator;
  }

  @Override
  public @Nullable Component lastSeparatorIfSerial() {
    return this.lastSeparatorIfSerial;
  }

  @Override
  public @NotNull Function<ComponentLike, Component> convertor() {
    return this.convertor;
  }

  @Override
  public @NotNull Predicate<ComponentLike> predicate() {
    return this.predicate;
  }

  @Override
  public @NotNull Style parentStyle() {
    return this.rootStyle;
  }

  @Override
  public JoinConfiguration.@NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("prefix", this.prefix),
      ExaminableProperty.of("suffix", this.suffix),
      ExaminableProperty.of("separator", this.separator),
      ExaminableProperty.of("lastSeparator", this.lastSeparator),
      ExaminableProperty.of("lastSeparatorIfSerial", this.lastSeparatorIfSerial),
      ExaminableProperty.of("convertor", this.convertor),
      ExaminableProperty.of("predicate", this.predicate),
      ExaminableProperty.of("rootStyle", this.rootStyle)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Contract(pure = true)
  static @NotNull Component join(final @NotNull JoinConfiguration config, final @NotNull Iterable<? extends ComponentLike> components) {
    Objects.requireNonNull(config, "config");
    Objects.requireNonNull(components, "components");

    final Iterator<? extends ComponentLike> it = components.iterator();

    if (!it.hasNext()) {
      return singleElementJoin(config, null);
    }

    ComponentLike component = Objects.requireNonNull(it.next(), "Null elements in \"components\" are not allowed");
    int componentsSeen = 0;

    if (!it.hasNext()) {
      return singleElementJoin(
        config,
        component
      );
    }

    final Component prefix = config.prefix();
    final Component suffix = config.suffix();
    final Function<ComponentLike, Component> convertor = config.convertor();
    final Predicate<ComponentLike> predicate = config.predicate();
    final Style rootStyle = config.parentStyle();
    final boolean hasRootStyle = rootStyle != Style.empty();

    final Component separator = config.separator();
    final boolean hasSeparator = separator != null;

    final TextComponent.Builder builder =
      hasRootStyle ?
        Component.text().style(rootStyle) :
        Component.text();
    if (prefix != null) builder.append(prefix);

    while (component != null) {
      if (!predicate.test(component)) {
        if (it.hasNext()) {
          component = it.next();
          continue;
        } else {
          break;
        }
      }

      builder.append(Objects.requireNonNull(convertor.apply(component), "Null output from \"convertor\" is not allowed"));
      componentsSeen++;

      if (!it.hasNext()) {
        component = null;
      } else {
        component = Objects.requireNonNull(it.next(), "Null elements in \"components\" are not allowed");

        if (it.hasNext()) {
          if (hasSeparator) builder.append(separator);
        } else {
          Component lastSeparator = null;

          if (componentsSeen > 1) lastSeparator = config.lastSeparatorIfSerial();
          if (lastSeparator == null) lastSeparator = config.lastSeparator();
          if (lastSeparator == null) lastSeparator = config.separator();

          if (lastSeparator != null) builder.append(lastSeparator);
        }
      }
    }

    if (suffix != null) builder.append(suffix);
    return builder.build();
  }

  static @NotNull Component singleElementJoin(final @NotNull JoinConfiguration config, final @Nullable ComponentLike component) {
    final Component prefix = config.prefix();
    final Component suffix = config.suffix();
    final Function<ComponentLike, Component> convertor = config.convertor();
    final Predicate<ComponentLike> predicate = config.predicate();
    final Style rootStyle = config.parentStyle();
    final boolean hasRootStyle = rootStyle != Style.empty();

    if (prefix == null && suffix == null) {
      final Component result;
      if (component == null || !predicate.test(component)) {
        result = Component.empty();
      } else {
        result = convertor.apply(component);
      }
      return hasRootStyle ? Component.text().style(rootStyle).append(result).build() : result;
    }

    final TextComponent.Builder builder = Component.text();
    if (prefix != null) builder.append(prefix);
    if (component != null && predicate.test(component)) builder.append(convertor.apply(component));
    if (suffix != null) builder.append(suffix);
    return hasRootStyle ? Component.text().style(rootStyle).append(builder).build() : builder.build();
  }

  static final class BuilderImpl implements JoinConfiguration.Builder {
    private ComponentLike prefix;
    private ComponentLike suffix;
    private ComponentLike separator;
    private ComponentLike lastSeparator;
    private ComponentLike lastSeparatorIfSerial;
    private Function<ComponentLike, Component> convertor;
    private Predicate<ComponentLike> predicate;
    private Style rootStyle;

    BuilderImpl() {
      this(JoinConfigurationImpl.NULL);
    }

    private BuilderImpl(final @NotNull JoinConfigurationImpl joinConfig) {
      this.separator = joinConfig.separator;
      this.lastSeparator = joinConfig.lastSeparator;
      this.prefix = joinConfig.prefix;
      this.suffix = joinConfig.suffix;
      this.convertor = joinConfig.convertor;
      this.lastSeparatorIfSerial = joinConfig.lastSeparatorIfSerial;
      this.predicate = joinConfig.predicate;
      this.rootStyle = joinConfig.rootStyle;
    }

    @Override
    public @NotNull Builder prefix(final @Nullable ComponentLike prefix) {
      this.prefix = prefix;
      return this;
    }

    @Override
    public @NotNull Builder suffix(final @Nullable ComponentLike suffix) {
      this.suffix = suffix;
      return this;
    }

    @Override
    public @NotNull Builder separator(final @Nullable ComponentLike separator) {
      this.separator = separator;
      return this;
    }

    @Override
    public @NotNull Builder lastSeparator(final @Nullable ComponentLike lastSeparator) {
      this.lastSeparator = lastSeparator;
      return this;
    }

    @Override
    public @NotNull Builder lastSeparatorIfSerial(final @Nullable ComponentLike lastSeparatorIfSerial) {
      this.lastSeparatorIfSerial = lastSeparatorIfSerial;
      return this;
    }

    @Override
    public @NotNull Builder convertor(final @NotNull Function<ComponentLike, Component> convertor) {
      this.convertor = Objects.requireNonNull(convertor, "convertor");
      return this;
    }

    @Override
    public @NotNull Builder predicate(final @NotNull Predicate<ComponentLike> predicate) {
      this.predicate = Objects.requireNonNull(predicate, "predicate");
      return this;
    }

    @Override
    public @NotNull Builder parentStyle(final @NotNull Style parentStyle) {
      this.rootStyle = Objects.requireNonNull(parentStyle, "rootStyle");
      return this;
    }

    @Override
    public @NotNull JoinConfiguration build() {
      return new JoinConfigurationImpl(this);
    }
  }
}
