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
package net.kyori.adventure.text.serializer.moshi;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ComponentSerializationBenchmark {

  @Benchmark
  public String simpleComponent() {
    return MoshiComponentSerializer.moshi().serialize(Component.text("Hello, World!", Style.style(TextDecoration.UNDERLINED)));
  }

  @Benchmark
  public String componentTreeWithStyle() {
    return MoshiComponentSerializer.moshi().serialize(Component.text()
      .decorate(TextDecoration.UNDERLINED, TextDecoration.ITALIC)
      .append(Component.text("Component ", TextColor.color(0x8cfbde)))
      .append(Component.text("with ", TextColor.color(0x0fff95), TextDecoration.BOLD))
      .append(Component.text("hex ", TextColor.color(0x06ba63)))
      .append(Component.text("colors", TextColor.color(0x103900)))
      .build());
  }

  @Benchmark
  public String componentTreeWithEvents() {
    return MoshiComponentSerializer.moshi().serialize(Component.text()
      .decorate(TextDecoration.UNDERLINED, TextDecoration.ITALIC)
      .append(Component.text("Component ", TextColor.color(0x8cfbde))
        .clickEvent(ClickEvent.openUrl("https://kyori.net/")))
      .append(Component.text("with ", TextColor.color(0x0fff95), TextDecoration.BOLD)
        .hoverEvent(HoverEvent.showItem(Key.key("iron_sword"), 1, BinaryTagHolder.of("{Damage: 30, RepairCost: 4, Enchantments: [{id: 'minecraft:sharpness', lvl: 3s}, {id: 'minecraft:unbreaking', lvl: 1s}]}"))))
      .append(Component.text("hex ", TextColor.color(0x06ba63))
        .hoverEvent(HoverEvent.showEntity(Key.key("pig"), UUID.randomUUID(), Component.text("Piggy", NamedTextColor.YELLOW))))
      .append(Component.text("colors", TextColor.color(0x103900))
        .hoverEvent(HoverEvent.showText(Component.text("Text hover!"))))
      .build());
  }
}
