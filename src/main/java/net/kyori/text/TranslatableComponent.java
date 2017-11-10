/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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
package net.kyori.text;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import net.kyori.blizzard.NonNull;
import net.kyori.blizzard.Nullable;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A translatable text component.
 */
public class TranslatableComponent extends AbstractBuildableComponent<TranslatableComponent, TranslatableComponent.Builder> {

  /**
   * The translation key.
   */
  @NonNull private final String key;
  /**
   * The list of translation arguments.
   */
  @NonNull private final List<Component> args;

  /**
   * Creates a translatable component builder.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a translatable component builder with a translation key.
   *
   * @param key the translation key
   * @return a builder
   */
  public static Builder builder(@NonNull final String key) {
    return new Builder().key(key);
  }

  /**
   * Creates a translatable component with a translation key.
   *
   * @param key the translation key
   * @return the text component
   */
  public static TranslatableComponent of(@NonNull final String key) {
    return builder(key).build();
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param args the translation arguments
   * @return the text component
   */
  public static TranslatableComponent of(@NonNull final String key, @NonNull final Component... args) {
    return of(key, Arrays.asList(args));
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param args the translation arguments
   * @return the text component
   */
  public static TranslatableComponent of(@NonNull final String key, @NonNull final List<Component> args) {
    return builder()
      .key(key)
      .args(args)
      .build();
  }

  protected TranslatableComponent(@NonNull final Builder builder) {
    super(builder);
    this.key = builder.key;
    this.args = ImmutableList.copyOf(builder.args);
  }

  protected TranslatableComponent(@NonNull final List<Component> children, @Nullable final TextColor color, @NonNull final TextDecoration.State obfuscated, @NonNull final TextDecoration.State bold, @NonNull final TextDecoration.State strikethrough, @NonNull final TextDecoration.State underlined, @NonNull final TextDecoration.State italic, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent hoverEvent, @Nullable final String insertion, @NonNull final String key, @NonNull final List<Component> args) {
    super(children, color, obfuscated, bold, strikethrough, underlined, italic, clickEvent, hoverEvent, insertion);
    this.key = key;
    this.args = ImmutableList.copyOf(args);
  }

  /**
   * Gets the translation key.
   *
   * @return the translation key
   */
  @NonNull
  public String key() {
    return this.key;
  }

  /**
   * Sets the translation key.
   *
   * @param key the translation key
   * @return a copy of this component
   */
  @NonNull
  public TranslatableComponent key(@NonNull final String key) {
    return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, checkNotNull(key, "key"), this.args);
  }

  /**
   * Gets the unmodifiable list of translation arguments.
   *
   * @return the unmodifiable list of translation arguments
   */
  @NonNull
  public List<Component> args() {
    return Collections.unmodifiableList(this.args);
  }

  /**
   * Sets the translation arguments for this component.
   *
   * @param args the translation arguments
   * @return this component
   */
  @NonNull
  public TranslatableComponent args(@NonNull final List<Component> args) {
    return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, args);
  }

  @NonNull
  @Override
  public TranslatableComponent append(@NonNull final Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> children = new ArrayList<>(this.children.size() + 1);
    children.addAll(this.children);
    children.add(component);
    return new TranslatableComponent(children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent color(@Nullable final TextColor color) {
    return new TranslatableComponent(this.children, color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent decoration(@NonNull final TextDecoration decoration, final boolean flag) {
    return (TranslatableComponent) super.decoration(decoration, flag);
  }

  @NonNull
  @Override
  public TranslatableComponent decoration(@NonNull final TextDecoration decoration, @NonNull final TextDecoration.State state) {
    switch(decoration) {
      case BOLD: return new TranslatableComponent(this.children, this.color, this.obfuscated, checkNotNull(state, "flag"), this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
      case ITALIC: return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, checkNotNull(state, "flag"), this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
      case UNDERLINE: return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, checkNotNull(state, "flag"), this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
      case STRIKETHROUGH: return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, checkNotNull(state, "flag"), this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
      case OBFUSCATED: return new TranslatableComponent(this.children, this.color, checkNotNull(state, "flag"), this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
      default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
  }

  @NonNull
  @Override
  public TranslatableComponent clickEvent(@Nullable final ClickEvent event) {
    return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent hoverEvent(@Nullable final HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, event, this.insertion, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent insertion(@Nullable final String insertion) {
    return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent mergeStyle(@NonNull final Component that) {
    return new TranslatableComponent(this.children, that.color(), that.decoration(TextDecoration.OBFUSCATED), that.decoration(TextDecoration.BOLD), that.decoration(TextDecoration.STRIKETHROUGH), that.decoration(TextDecoration.UNDERLINE), that.decoration(TextDecoration.ITALIC), that.clickEvent(), that.hoverEvent(), that.insertion(), this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent mergeColor(@NonNull final Component that) {
    return new TranslatableComponent(this.children, that.color(), this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent mergeDecorations(@NonNull final Component that) {
    final TextDecoration.State obfuscated = that.decoration(TextDecoration.OBFUSCATED) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.OBFUSCATED) : this.obfuscated;
    final TextDecoration.State bold = that.decoration(TextDecoration.BOLD) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.BOLD) : this.bold;
    final TextDecoration.State strikethrough = that.decoration(TextDecoration.STRIKETHROUGH) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.STRIKETHROUGH) : this.strikethrough;
    final TextDecoration.State underlined = that.decoration(TextDecoration.UNDERLINE) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.UNDERLINE) : this.underlined;
    final TextDecoration.State italic = that.decoration(TextDecoration.ITALIC) != TextDecoration.State.NOT_SET ? that.decoration(TextDecoration.ITALIC) : this.italic;
    return new TranslatableComponent(this.children, this.color, obfuscated, bold, strikethrough, underlined, italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent mergeEvents(@NonNull final Component that) {
    return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, that.clickEvent(), that.hoverEvent(), this.insertion, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent resetStyle() {
    return new TranslatableComponent(this.children, null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null, this.key, this.args);
  }

  @NonNull
  @Override
  public TranslatableComponent copy() {
    return new TranslatableComponent(this.children, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion, this.key, this.args);
  }

  @Override
  public boolean equals(@Nullable final Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof TranslatableComponent)) return false;
    if(!super.equals(other)) return false;
    final TranslatableComponent that = (TranslatableComponent) other;
    return Objects.equals(this.key, that.key) && Objects.equals(this.args, that.args);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.key, this.args);
  }

  @Override
  protected void populateToString(@NonNull final MoreObjects.ToStringHelper builder) {
    builder
      .add("key", this.key)
      .add("args", this.args);
  }

  @NonNull
  @Override
  public Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * A text component builder.
   */
  public static class Builder extends AbstractBuildableComponent.AbstractBuilder<TranslatableComponent, Builder> {

    @Nullable private String key;
    @NonNull private List<Component> args = Component.EMPTY_COMPONENT_LIST;

    Builder() {
    }

    Builder(@NonNull final TranslatableComponent component) {
      super(component);
      this.key = component.key();
      this.args = component.args();
    }

    /**
     * Sets the translation key.
     *
     * @param key the translation key
     * @return this builder
     */
    @NonNull
    public Builder key(@NonNull final String key) {
      this.key = key;
      return this;
    }

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     */
    @NonNull
    public Builder args(@NonNull final Component... args) {
      return this.args(Arrays.asList(args));
    }

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     */
    @NonNull
    public Builder args(@NonNull final List<Component> args) {
      this.args = args;
      return this;
    }

    @NonNull
    @Override
    public TranslatableComponent build() {
      checkState(this.key != null, "key must be set");
      return new TranslatableComponent(this);
    }
  }
}
