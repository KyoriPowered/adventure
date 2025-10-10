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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class CompletionResultImpl implements CompletionResult {
  final int start;
  final Collection<Completion> completions;

  private CompletionResultImpl(final int start, final @NotNull Collection<Completion> completions) {
    this.start = start;
    this.completions = completions;
  }

  @Override
  public int start() {
    return this.start;
  }

  @Override
  public @NotNull Collection<Completion> completions() {
    return this.completions;
  }

  final static class CompletionImpl implements Completion {
    private final String completion;
    private final Component completionComponent;

    private CompletionImpl(final @NotNull String value, final @Nullable Component completionComponent) {
      this.completion = value;
      this.completionComponent = completionComponent;
    }

    @Override
    public @NotNull String completion() {
      return this.completion;
    }

    @Override
    public @Nullable Component completionComponent() {
      return this.completionComponent;
    }
  }

  static class BuilderImpl implements CompletionResult.Builder {
    List<BuilderCompletion> completions = new ArrayList<>();

    @Override
    public void add(final @NotNull String completion, final @Nullable Component completionComponent, final int start) {
      this.completions.add(new BuilderCompletion(completion, completionComponent, start));
    }

    CompletionResultImpl build(final CompletionContextImpl context) {
      final int offset = context.offset();

      int minStart = -1;
      for (final BuilderCompletion completion : this.completions) {
        if (minStart == -1 || completion.start < minStart) {
          minStart = completion.start;
        }
      }

      final Collection<Completion> compls = new ArrayList<>(this.completions.size());
      for (final BuilderCompletion completion : this.completions) {
        if (completion.start > minStart) {
          compls.add(new CompletionImpl(context.partial().substring(minStart, completion.start), completion.completionComponent));
        } else {
          compls.add(new CompletionImpl(completion.completion, completion.completionComponent));
        }
      }

      return new CompletionResultImpl(offset + minStart, compls);
    }

    private static class BuilderCompletion {
      String completion;
      Component completionComponent;
      int start;

      BuilderCompletion(final String completion, final @Nullable Component completionComponent, final int start) {
        this.completion = completion;
        this.completionComponent = completionComponent;
        this.start = start;
      }
    }
  }
}
