/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.Collections;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.VirtualComponent;
import net.kyori.adventure.text.VirtualComponentRenderer;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import net.kyori.adventure.text.minimessage.tree.Node;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

/**
 * A transformation that applies a colour change.
 *
 * <p>The lifecycle is:</p>
 *
 * <ol>
 * <li>init()</li>
 * <li>(color()? advanceColor())*</li>
 * </ol>
 *
 * @since 4.10.0
 */
abstract class AbstractColorChangingTag implements Modifying, Examinable {

  private static final ComponentFlattener LENGTH_CALCULATOR = ComponentFlattener.builder()
    .mapper(TextComponent.class, TextComponent::content)
    .unknownMapper(x -> "_") // every unknown component gets a single colour
    .build();

  private boolean visited;
  private int size = 0;
  private int disableApplyingColorDepth = -1;

  protected final int size() {
    return this.size;
  }

  @Override
  public final void visit(final @NotNull Node current, final int depth) {
    if (this.visited) {
      throw new IllegalStateException("Color changing tag instances cannot be re-used, return a new one for each resolve");
    }

    if (current instanceof ValueNode) {
      final String value = ((ValueNode) current).value();
      this.size += value.codePointCount(0, value.length());
    } else if (current instanceof TagNode) {
      final TagNode tag = (TagNode) current;
      if (tag.tag() instanceof Inserting) {
        // ComponentTransformation.apply() returns the value of the component placeholder
        LENGTH_CALCULATOR.flatten(((Inserting) tag.tag()).value(), s -> this.size += s.codePointCount(0, s.length()));
      }
    }
  }

  @Override
  public final void postVisit() {
    // init
    this.visited = true;
    this.init();
  }

  @Override
  public final Component apply(final @NotNull Component current, final int depth) {
    if (depth == 0) {
      // capture state into a virtual component, no other logic is needed in normal MM handling
      return Component.virtual(Void.class, new TagInfoHolder(this.preserveData(), current));
    }

    if ((this.disableApplyingColorDepth != -1 && depth > this.disableApplyingColorDepth) || current.style().color() != null) {
      if (this.disableApplyingColorDepth == -1 || depth < this.disableApplyingColorDepth) {
        this.disableApplyingColorDepth = depth;
      }
      // This component has its own color applied, which overrides ours
      // We still want to keep track of where we are though if this is text
      if (current instanceof TextComponent) {
        this.skipColorForLengthOf(((TextComponent) current).content());
      }
      return current.children(Collections.emptyList());
    }

    this.disableApplyingColorDepth = -1;
    if (current instanceof VirtualComponent) {
      // this component has its own information, so we can't rainbowify direct content -- we can process children tho
      // basically treat as if it's a non-text component
      this.skipColorForLengthOf(((VirtualComponent) current).content());

      return current.children(Collections.emptyList());
    } else if (current instanceof TextComponent && ((TextComponent) current).content().length() > 0) {
      final TextComponent textComponent = (TextComponent) current;
      final String content = textComponent.content();

      final TextComponent.Builder parent = Component.text();

      // apply
      final int[] holder = new int[1];
      for (final PrimitiveIterator.OfInt it = content.codePoints().iterator(); it.hasNext();) {
        holder[0] = it.nextInt();
        final Component comp = Component.text(new String(holder, 0, 1), current.style().color(this.color()));
        this.advanceColor();
        parent.append(comp);
      }

      return parent.build();
    } else if (!(current instanceof TextComponent)) {
      final Component ret = current.children(Collections.emptyList()).colorIfAbsent(this.color());
      this.advanceColor();
      return ret;
    }

    return Component.empty().mergeStyle(current);
  }

  private void skipColorForLengthOf(final String content) {
    final int len = content.codePointCount(0, content.length());
    for (int i = 0; i < len; i++) {
      // increment our color index
      this.advanceColor();
    }
  }

  // The lifecycle

  protected abstract void init();

  /**
   * Advance the active color.
   */
  protected abstract void advanceColor();

  /**
   * Get the current color, without side-effects.
   *
   * @return the current color
   * @since 4.10.0
   */
  protected abstract TextColor color();

  /**
   * Return an emitable that will accurately reserialize the provided input data.
   *
   * @return the emitable for this tag
   * @since 4.13.0
   */
  protected abstract @NotNull Consumer<TokenEmitter> preserveData();

  // misc

  @Override
  public abstract @NotNull Stream<? extends ExaminableProperty> examinableProperties();

  @Override
  public final @NotNull String toString() {
    return Internals.toString(this);
  }

  @Override
  public abstract boolean equals(final @Nullable Object other);

  @Override
  public abstract int hashCode();

  static final class TagInfoHolder implements VirtualComponentRenderer<Void>, Emitable {
    private final Consumer<TokenEmitter> output;
    private final Component originalComp;

    TagInfoHolder(final Consumer<TokenEmitter> output, final Component originalComp) {
      this.output = output;
      this.originalComp = originalComp;
    }

    @Override
    public @UnknownNullability ComponentLike apply(final @NotNull Void context) {
      return this.originalComp;
    }

    @Override
    public @NotNull String fallbackString() {
      return ""; // only holds data for reserialization, not for display
    }

    @Override
    public void emit(final @NotNull TokenEmitter emitter) {
      this.output.accept(emitter);
    }

    @Override
    public @Nullable Component substitute() {
      return this.originalComp;
    }
  }

  static @Nullable Emitable claimComponent(final Component comp) {
    if (!(comp instanceof VirtualComponent)) {
      return null;
    }

    final VirtualComponentRenderer<?> holder = ((VirtualComponent) comp).renderer();
    if (!(holder instanceof TagInfoHolder)) {
      return null;
    }

    return (TagInfoHolder) holder;
  }
}
