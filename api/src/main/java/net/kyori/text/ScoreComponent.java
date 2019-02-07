/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A scoreboard score component.
 */
public class ScoreComponent extends AbstractBuildableComponent<ScoreComponent, ScoreComponent.Builder> {
  /**
   * The score name.
   */
  private final String name;
  /**
   * The score objective.
   */
  private final String objective;
  /**
   * The value.
   */
  private final @Nullable String value;

  /**
   * Creates a score component builder.
   *
   * @return a builder
   */
  public static @NonNull Builder builder() {
    return new Builder();
  }

  /**
   * Creates a score component builder with a name and objective.
   *
   * @param name the score name
   * @param objective the score objective
   * @return a builder
   */
  public static @NonNull Builder builder(final @NonNull String name, final @NonNull String objective) {
    return new Builder().name(name).objective(objective);
  }

  /**
   * Creates a score component with a name and objective.
   *
   * @param name the score name
   * @param objective the score objective
   * @return the score component
   */
  public static @NonNull ScoreComponent of(final @NonNull String name, final @NonNull String objective) {
    return of(name, objective, null);
  }

  /**
   * Creates a score component with a name, objective, and optional value.
   *
   * @param name the score name
   * @param objective the score objective
   * @param value the value
   * @return the score component
   */
  public static @NonNull ScoreComponent of(final @NonNull String name, final @NonNull String objective, final @Nullable String value) {
    return builder()
      .name(name)
      .objective(objective)
      .value(value)
      .build();
  }

  protected ScoreComponent(final @NonNull Builder builder) {
    super(builder);
    this.name = requireNonNull(builder.name, "name");
    this.objective = requireNonNull(builder.objective, "objective");
    this.value = builder.value;
  }

  protected ScoreComponent(final @NonNull List<Component> children, final @NonNull Style style, final @NonNull String name, final @NonNull String objective, final @Nullable String value) {
    super(children, style);
    this.name = name;
    this.objective = objective;
    this.value = value;
  }

  /**
   * Gets the score name.
   *
   * @return the score name
   */
  public @NonNull String name() {
    return this.name;
  }

  /**
   * Sets the score name.
   *
   * @param name the score name
   * @return a copy of this component
   */
  public @NonNull ScoreComponent name(final @NonNull String name) {
    return new ScoreComponent(this.children, this.style, requireNonNull(name, "name"), this.objective, this.value);
  }

  /**
   * Gets the objective name.
   *
   * @return the objective name
   */
  public @NonNull String objective() {
    return this.objective;
  }

  /**
   * Sets the score objective.
   *
   * @param objective the score objective
   * @return a copy of this component
   */
  public @NonNull ScoreComponent objective(final @NonNull String objective) {
    return new ScoreComponent(this.children, this.style, this.name, requireNonNull(objective, "objective"), this.value);
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public @Nullable String value() {
    return this.value;
  }

  /**
   * Sets the value.
   *
   * @param value the value
   * @return a copy of this component
   */
  public @NonNull ScoreComponent content(final @NonNull String value) {
    return new ScoreComponent(this.children, this.style, this.name, this.objective, value);
  }

  @Override
  public @NonNull ScoreComponent append(final @NonNull Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> children = new ArrayList<>(this.children.size() + 1);
    children.addAll(this.children);
    children.add(component);
    return new ScoreComponent(children, this.style, this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent color(final @Nullable TextColor color) {
    return new ScoreComponent(this.children, this.style.color(color), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent decoration(final @NonNull TextDecoration decoration, final boolean flag) {
    return (ScoreComponent) super.decoration(decoration, flag);
  }

  @Override
  public @NonNull ScoreComponent decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    return new ScoreComponent(this.children, this.style.decoration(decoration, state), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent clickEvent(final @Nullable ClickEvent event) {
    return new ScoreComponent(this.children, this.style.clickEvent(event), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent hoverEvent(final @Nullable HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    return new ScoreComponent(this.children, this.style.hoverEvent(event), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent insertion(final @Nullable String insertion) {
    return new ScoreComponent(this.children, this.style.insertion(insertion), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent mergeStyle(final @NonNull Component that) {
    return new ScoreComponent(this.children, this.style.mergeStyle(that.style()), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent mergeColor(final @NonNull Component that) {
    return new ScoreComponent(this.children, this.style.mergeColor(that.style()), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent mergeDecorations(final @NonNull Component that) {
    return new ScoreComponent(this.children, this.style.mergeDecorations(that.style()), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent mergeEvents(final @NonNull Component that) {
    return new ScoreComponent(this.children, this.style.mergeEvents(that.style()), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent resetStyle() {
    return new ScoreComponent(this.children, this.style.resetStyle(), this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent copy() {
    return new ScoreComponent(this.children, this.style, this.name, this.objective, this.value);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || !(other instanceof ScoreComponent)) return false;
    if(!super.equals(other)) return false;
    final ScoreComponent that = (ScoreComponent) other;
    return Objects.equals(this.name, that.name) && Objects.equals(this.objective, that.objective) && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.name, this.objective, this.value);
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("name", this.name);
    builder.put("objective", this.objective);
    builder.put("value", this.value);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new Builder(this);
  }

  /**
   * A score component builder.
   */
  public static class Builder extends AbstractBuildableComponent.AbstractBuilder<ScoreComponent, Builder> {
    private @Nullable String name;
    private @Nullable String objective;
    private @Nullable String value;

    Builder() {
    }

    Builder(final @NonNull ScoreComponent component) {
      super(component);
      this.name = component.name();
      this.objective = component.objective();
      this.value = component.value();
    }

    /**
     * Sets the score name.
     *
     * @param name the score name
     * @return this builder
     */
    public @NonNull Builder name(final @NonNull String name) {
      this.name = name;
      return this;
    }

    /**
     * Sets the score objective.
     *
     * @param objective the score objective
     * @return this builder
     */
    public @NonNull Builder objective(final @NonNull String objective) {
      this.objective = objective;
      return this;
    }

    /**
     * Sets the value.
     *
     * @param value the value
     * @return this builder
     */
    public @NonNull Builder value(final @Nullable String value) {
      this.value = value;
      return this;
    }

    @Override
    public @NonNull ScoreComponent build() {
      if(this.name == null) throw new IllegalStateException("name must be set");
      if(this.objective == null) throw new IllegalStateException("objective must be set");
      return new ScoreComponent(this);
    }
  }
}
