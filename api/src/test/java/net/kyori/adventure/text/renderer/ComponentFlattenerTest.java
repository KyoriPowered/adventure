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
package net.kyori.adventure.text.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentFlattenerTest {

  static class TrackingFlattener implements FlattenerListener {
    int pushCount;
    int popCount;
    final List<Style> pushedStyles = new ArrayList<>();
    final List<String> strings = new ArrayList<>();

    @Override
    public void pushStyle(final @NonNull Style style) {
      this.pushCount++;
      this.pushedStyles.add(style);
    }

    @Override
    public void component(final @NonNull String text) {
      this.strings.add(text);
    }

    @Override
    public void popStyle(final @NonNull Style style) {
      this.popCount++;
    }

    public TrackingFlattener assertBalanced() {
      assertEquals(this.pushCount, this.popCount, "Expected pushes and pops to be balanced at end of visit");
      return this;
    }

    public TrackingFlattener assertPushesAndPops(final int pushAndPopCount) {
      assertEquals(pushAndPopCount, this.pushCount, "Push count mismatch");
      assertEquals(pushAndPopCount, this.popCount, "Pop count mismatch");
      return this;
    }

    public TrackingFlattener assertContents(final String... lines) {
      assertIterableEquals(Arrays.asList(lines), this.strings);
      return this;
    }

    public TrackingFlattener assertStyles(final Style... styles) {
      assertIterableEquals(Arrays.asList(styles), this.pushedStyles);
      return this;
    }
  }

  private TrackingFlattener testFlatten(final ComponentFlattener flattener, final Component toFlatten) {
    final TrackingFlattener listener = new TrackingFlattener();
    flattener.flatten(toFlatten, listener);
    return listener;
  }

  @Test
  void testEmpty() {
    ComponentFlattener.basic().flatten(Component.empty(), input -> Assertions.fail("Should not be called"));
  }

  @Test
  void testSingleUnstyled() {
    this.testFlatten(ComponentFlattener.basic(), Component.text("Hello"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents("Hello");
  }

  @Test
  void testSingleStyled() {
    this.testFlatten(ComponentFlattener.basic(), Component.text("Hello", NamedTextColor.RED, TextDecoration.BOLD))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.style(NamedTextColor.RED, TextDecoration.BOLD))
      .assertContents("Hello");
  }

  @Test
  void testComplex() {
    final Component input = Component.text()
      .content("Hi there my")
      .append(Component.text(" blue ", NamedTextColor.BLUE))
      .append(Component.text("friend"))
      .build();

    this.testFlatten(ComponentFlattener.basic(), input).assertBalanced()
      .assertPushesAndPops(3)
      .assertStyles(Style.empty(), Style.style(NamedTextColor.BLUE), Style.empty())
      .assertContents("Hi there my", " blue ", "friend");
  }

  @Test
  void testTranslatable() {
    this.testFlatten(ComponentFlattener.basic(), Component.translatable("adventure.test.key"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents("adventure.test.key");

    this.testFlatten(ComponentFlattener.textOnly(), Component.translatable("adventure.test.key"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents();
  }

  @Test
  void testSelector() {
    this.testFlatten(ComponentFlattener.basic(), Component.selector("@p"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents("@p");

    this.testFlatten(ComponentFlattener.textOnly(), Component.selector("@p"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents();
  }

  @Test
  void testScore() {
    this.testFlatten(ComponentFlattener.basic(), Component.score("kashike", "dirtMined", "legacy support only"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents("legacy support only");

    this.testFlatten(ComponentFlattener.textOnly(), Component.score("kashike", "dirtMined", "legacy support only"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents();
  }

  @Test
  void testKeybind() {
    this.testFlatten(ComponentFlattener.basic(), Component.keybind("key.fullscreen"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents("key.fullscreen");

    this.testFlatten(ComponentFlattener.textOnly(), Component.keybind("key.fullscreen"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents();
  }

  @Test
  void testCustomHandler() {
    final ComponentFlattener customized = ComponentFlattener.basic()
      .toBuilder()
      .type(TranslatableComponent.class, component -> component.key().toUpperCase(Locale.ROOT))
      .build();

    this.testFlatten(customized, Component.translatable("adventure.test.key"))
      .assertBalanced()
      .assertPushesAndPops(1)
      .assertStyles(Style.empty())
      .assertContents("ADVENTURE.TEST.KEY");
  }

  @Test
  void testEmptyFlattenerProducesNoContent() {
    final ComponentFlattener empty = ComponentFlattener.builder().build();
    final Component test = Component.text()
      .content("Hello, press")
      .append(Component.keybind("key.chat", NamedTextColor.GREEN))
      .append(Component.space())
      .append(Component.translatable("entity.minecraft.bat"))
      .build();

    this.testFlatten(empty, test)
      .assertContents();
  }

  @Test
  void testComplexHandler() {
    final ComponentFlattener flattener = ComponentFlattener.basic().toBuilder()
      .complexType(TranslatableComponent.class, (component, accepter) -> accepter.accept(Component.text(component.key(), NamedTextColor.RED)))
      .build();

    this.testFlatten(flattener, Component.translatable("my.key"))
      .assertBalanced()
      .assertPushesAndPops(2)
      .assertStyles(Style.empty(), Style.style(NamedTextColor.RED))
      .assertContents("my.key");
  }

  @Test
  void testFailsWhenInSameHierarchy() {
    final ComponentFlattener.Builder builder = ComponentFlattener.builder();

    builder.type(NBTComponent.class, x -> "");

    // simple subtype
    assertThrows(IllegalArgumentException.class, () -> builder.type(BlockNBTComponent.class, $ -> ""));
    // complex subtype
    assertThrows(IllegalArgumentException.class, () -> builder.complexType(BlockNBTComponent.class, ($, $$) -> {}));

    // simple supertype
    assertThrows(IllegalArgumentException.class, () -> builder.type(Component.class, $ -> ""));
    // complex supertype
    assertThrows(IllegalArgumentException.class, () -> builder.complexType(Component.class, ($, $$) -> {}));
  }
}
