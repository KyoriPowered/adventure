/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/* package */ final class ScoreComponentImpl extends AbstractComponent implements ScoreComponent {
  private final String name;
  private final String objective;
  private final @Nullable String value;

  ScoreComponentImpl(final @NonNull List<? extends ComponentLike> children, final @NonNull Style style, final @NonNull String name, final @NonNull String objective, final @Nullable String value) {
    super(children, style);
    this.name = name;
    this.objective = objective;
    this.value = value;
  }

  @Override
  public @NonNull String name() {
    return this.name;
  }

  @Override
  public @NonNull ScoreComponent name(final @NonNull String name) {
    if(Objects.equals(this.name, name)) return this;
    return new ScoreComponentImpl(this.children, this.style, requireNonNull(name, "name"), this.objective, this.value);
  }

  @Override
  public @NonNull String objective() {
    return this.objective;
  }

  @Override
  public @NonNull ScoreComponent objective(final @NonNull String objective) {
    if(Objects.equals(this.objective, objective)) return this;
    return new ScoreComponentImpl(this.children, this.style, this.name, requireNonNull(objective, "objective"), this.value);
  }

  @Override
  public @Nullable String value() {
    return this.value;
  }

  @Override
  public @NonNull ScoreComponent value(final @Nullable String value) {
    if(Objects.equals(this.value, value)) return this;
    return new ScoreComponentImpl(this.children, this.style, this.name, this.objective, value);
  }

  @Override
  public @NonNull ScoreComponent children(final @NonNull List<? extends ComponentLike> children) {
    return new ScoreComponentImpl(children, this.style, this.name, this.objective, this.value);
  }

  @Override
  public @NonNull ScoreComponent style(final @NonNull Style style) {
    return new ScoreComponentImpl(this.children, style, this.name, this.objective, this.value);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof ScoreComponent)) return false;
    if(!super.equals(other)) return false;
    final ScoreComponent that = (ScoreComponent) other;
    return Objects.equals(this.name, that.name())
      && Objects.equals(this.objective, that.objective())
      && Objects.equals(this.value, that.value());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.name.hashCode();
    result = (31 * result) + this.objective.hashCode();
    result = (31 * result) + Objects.hashCode(this.value);
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("name", this.name),
        ExaminableProperty.of("objective", this.objective),
        ExaminableProperty.of("value", this.value)
      ),
      super.examinableProperties()
    );
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  /* package */ static final class BuilderImpl extends AbstractComponentBuilder<ScoreComponent, Builder> implements ScoreComponent.Builder {
    private @Nullable String name;
    private @Nullable String objective;
    private @Nullable String value;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull ScoreComponent component) {
      super(component);
      this.name = component.name();
      this.objective = component.objective();
      this.value = component.value();
    }

    @Override
    public @NonNull Builder name(final @NonNull String name) {
      this.name = name;
      return this;
    }

    @Override
    public @NonNull Builder objective(final @NonNull String objective) {
      this.objective = objective;
      return this;
    }

    @Override
    public @NonNull Builder value(final @Nullable String value) {
      this.value = value;
      return this;
    }

    @Override
    public @NonNull ScoreComponent build() {
      if(this.name == null) throw new IllegalStateException("name must be set");
      if(this.objective == null) throw new IllegalStateException("objective must be set");
      return new ScoreComponentImpl(this.children, this.buildStyle(), this.name, this.objective, this.value);
    }
  }
}
