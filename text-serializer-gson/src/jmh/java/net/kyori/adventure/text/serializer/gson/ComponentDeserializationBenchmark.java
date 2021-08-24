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
package net.kyori.adventure.text.serializer.gson;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.HoverEvent.showEntity;
import static net.kyori.adventure.text.event.HoverEvent.showItem;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ComponentDeserializationBenchmark {
  private String simpleComponent;
  private String componentTreeWithStyle;
  private String componentTreeWithEvents;

  @Setup(Level.Trial)
  public void prepare() {
    this.simpleComponent = gson().serialize(text("Hello, World!", style(TextDecoration.UNDERLINED)));
    this.componentTreeWithStyle = gson().serialize(text()
                                                     .decorate(TextDecoration.UNDERLINED, TextDecoration.ITALIC)
                                                     .append(text("Component ", color(0x8cfbde)))
                                                     .append(text("with ", color(0x0fff95), TextDecoration.BOLD))
                                                     .append(text("hex ", color(0x06ba63)))
                                                     .append(text("colors", color(0x103900)))
                                                     .build());
    this.componentTreeWithEvents = gson().serialize(text()
                                                      .decorate(TextDecoration.UNDERLINED, TextDecoration.ITALIC)
                                                      .append(text("Component ", style(color(0x8cfbde), openUrl("https://kyori.net/"))))
                                                      .append(text("with ", style(color(0x0fff95), TextDecoration.BOLD, showItem(Key.key("iron_sword"), 1, BinaryTagHolder.of("{Damage: 30, RepairCost: 4, Enchantments: [{id: 'minecraft:sharpness', lvl: 3s}, {id: 'minecraft:unbreaking', lvl: 1s}]}")))))
                                                      .append(text("hex ", style(color(0x06ba63), showEntity(Key.key("pig"), UUID.randomUUID(), text("Piggy", NamedTextColor.YELLOW)))))
                                                      .append(text("colors", style(color(0x103900), showText(text("Text hover!")))))
                                                      .build());
  }

  @Benchmark
  public Component simpleComponent() {
    return gson().deserialize(this.simpleComponent);
  }

  @Benchmark
  public Component componentTreeWithStyle() {
    return gson().deserialize(this.componentTreeWithStyle);
  }

  @Benchmark
  public Component componentTreeWithEvents() {
    return gson().deserialize(this.componentTreeWithEvents);
  }
}
