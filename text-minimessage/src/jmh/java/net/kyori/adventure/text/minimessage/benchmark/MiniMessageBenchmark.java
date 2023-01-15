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
package net.kyori.adventure.text.minimessage.benchmark;

import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class MiniMessageBenchmark {

  @Benchmark
  public Component testNiceMix() {
    final String input = "<yellow><test> random <gradient:red:blue:green><bold>stranger</gradient></bold><click:run_command:test command><underlined><red>click here</click><blue> to <rainbow><b>FEEL</rainbow></underlined> it";
    return MiniMessage.miniMessage().deserialize(input);
  }

  @Benchmark
  public Component testSimple() {
    final String input = "<yellow><test><bold>stranger";
    return MiniMessage.miniMessage().deserialize(
      input,
      Placeholder.component("test", Component.text("test2"))
    );
  }

  @Benchmark
  public Component testGradient() {
    final String input = "<gradient:red:blue:green>COLORS ARE COOL";
    return MiniMessage.miniMessage().deserialize(input);
  }

  @Benchmark
  public Component testRainbow() {
    final String input = "<rainbow>COLORS ARE COOL";
    return MiniMessage.miniMessage().deserialize(input);
  }
}
