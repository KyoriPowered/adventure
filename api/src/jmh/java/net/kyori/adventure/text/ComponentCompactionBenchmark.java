/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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

import java.util.concurrent.TimeUnit;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.Style.style;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ComponentCompactionBenchmark {

  private Component alreadyCompactedInput;
  private Component simpleScenarioInput;
  private Component moreComplexInput;

  @Setup(Level.Trial)
  public void prepare() {
    this.alreadyCompactedInput = Component.text()
      .content("Hello World!")
      .color(NamedTextColor.RED)
      .append(Component.text(" aaaa", style(b -> b.color(NamedTextColor.BLUE).font(Key.key("uniform")))))
      .build();

    final Style simpleScenarioStyle = style()
      .color(NamedTextColor.AQUA)
      .font(key("uniform"))
      .decorate(TextDecoration.BOLD, TextDecoration.ITALIC)
      .build();
    this.simpleScenarioInput = text()
      .content("Hello ")
      .style(simpleScenarioStyle)
      .append(text("World!", style(TextDecoration.BOLD).font(key("uniform"))))
      .build();

    this.moreComplexInput =
      text().content("Hello ")
        .style(style(NamedTextColor.RED, TextDecoration.BOLD, TextDecoration.OBFUSCATED))
        .append(text("World! "))
        .append(text("What a ", NamedTextColor.RED))
        .append(text(c -> c.content("beautiful day ")
          .color(NamedTextColor.BLUE)
          .append(text("to create ", style(TextDecoration.ITALIC)))
          .append(text("a PR ", style(TextDecoration.BOLD)))
          .append(text("on Adventure!"))
        ))
        .build();
  }

  @Benchmark
  public Component alreadyCompacted() {
    return this.alreadyCompactedInput.compact();
  }

  @Benchmark
  public Component simpleScenario() {
    return this.simpleScenarioInput.compact();
  }

  @Benchmark
  public Component moreComplex() {
    return this.moreComplexInput.compact();
  }

  public static void main(final String[] args) throws RunnerException {
    final Options opt = new OptionsBuilder()
      .include(ComponentCompactionBenchmark.class.getSimpleName())
      .forks(1)
      .build();

    new Runner(opt).run();
  }

}
