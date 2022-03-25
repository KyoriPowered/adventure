package net.kyori.adventure.text;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.Style.style;

@Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ComponentEqualityBenchmark {

  Component component1;
  Component componentAlmost;
  Component component2;

  Component component3;

  @Setup
  public void setup() {
    component1 = create1();
    componentAlmost = createAlmost1();
    component2 = create1();

    component3 = create2();
  }

  @Benchmark
  public void constantEquals(final Blackhole blackhole) {
    blackhole.consume(component1.equals(component2));
  }

  @Benchmark
  public void constantAlmostEquals(final Blackhole blackhole) {
    blackhole.consume(component1.equals(componentAlmost));
  }

  @Benchmark
  public void constantNotEquals(final Blackhole blackhole) {
    blackhole.consume(component1.equals(component3));
  }

  @Benchmark
  public void singleConstantEquals(final Blackhole blackhole) {
    blackhole.consume(component1.equals(create1()));
  }

  @Benchmark
  public void singleConstantNotEqual(final Blackhole blackhole) {
    blackhole.consume(component1.equals(create2()));
  }

  Component create1() {
    return text().content("Hello ")
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

  Component createAlmost1() {
    return text().content("Hello ")
      .style(style(NamedTextColor.RED, TextDecoration.BOLD, TextDecoration.OBFUSCATED))
      .append(text("World! "))
      .append(text("What a ", NamedTextColor.RED))
      .append(text(c -> c.content("beautiful day ")
        .color(NamedTextColor.BLUE)
        .append(text("to create ", style(TextDecoration.ITALIC)))
        .append(text("a PR ", style(TextDecoration.BOLD)))
        .append(text("on GitHub!"))
      ))
      .build();
  }

  Component create2() {
    Style simpleScenarioStyle = style()
      .color(NamedTextColor.AQUA)
      .font(key("uniform"))
      .decorate(TextDecoration.BOLD, TextDecoration.ITALIC)
      .build();
    return text()
      .content("Hello ")
      .style(simpleScenarioStyle)
      .append(text("World!", style(TextDecoration.BOLD).font(key("uniform"))))
      .build();
  }
}
