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
package net.kyori.adventure.text.minimessage.transformation.inbuild;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * A transformation that applies a {@link HoverEvent}.
 *
 * @since 4.10.0
 */
public final class HoverTransformation extends Transformation {
  public static final String HOVER = "hover";

  private final HoverEvent.Action<Object> action;
  private final Object value;

  /**
   * Create a new hover transformation from a tag.
   *
   * @param ctx the active parse context
   * @param name the tag name
   * @param args the arguments provided
   * @return a new transformation
   * @throws ParsingException if an error occurs
   * @since 4.10.0
   */
  @SuppressWarnings("unchecked")
  public static HoverTransformation create(final Context ctx, final String name, final List<TagPart> args) {
    if (args.size() < 2) {
      throw new ParsingException("Doesn't know how to turn " + args + " into a hover event", args);
    }

    final List<TagPart> newArgs = args.subList(1, args.size());

    final HoverEvent.Action<Object> action = (HoverEvent.Action<Object>) HoverEvent.Action.NAMES.value(args.get(0).value());
    final Object value;
    if (action == (Object) HoverEvent.Action.SHOW_TEXT) {
      value = ctx.parse(newArgs.get(0).value());
    } else if (action == (Object) HoverEvent.Action.SHOW_ITEM) {
      value = parseShowItem(newArgs);
    } else if (action == (Object) HoverEvent.Action.SHOW_ENTITY) {
      value = parseShowEntity(newArgs, ctx);
    } else {
      throw new ParsingException("Don't know how to turn '" + args + "' into a hover event", args);
    }

    return new HoverTransformation(action, value);
  }

  private static HoverEvent.@NotNull ShowItem parseShowItem(final @NotNull List<TagPart> args) {
    try {
      if (args.isEmpty()) {
        throw new ParsingException("Show item hover needs at least item id!");
      }
      final Key key = Key.key(args.get(0).value());
      final int count;
      if (args.size() >= 2) {
        count = Integer.parseInt(args.get(1).value());
      } else {
        count = 1;
      }
      if (args.size() == 3) {
        return HoverEvent.ShowItem.of(key, count, BinaryTagHolder.of(args.get(2).value()));
      }
      return HoverEvent.ShowItem.of(key, count);
    } catch (final InvalidKeyException | NumberFormatException ex) {
      throw new ParsingException("Exception parsing show_item hover", ex, args);
    }
  }

  private static HoverEvent.@NotNull ShowEntity parseShowEntity(final @NotNull List<TagPart> args, final Context context) {
    try {
      if (args.size() < 2) {
        throw new ParsingException("Show entity hover needs at least type and uuid!");
      }
      final Key key = Key.key(args.get(0).value());
      final UUID id = UUID.fromString(args.get(1).value());
      if (args.size() == 3) {
        final Component name = context.parse(args.get(2).value());
        return HoverEvent.ShowEntity.of(key, id, name);
      }
      return HoverEvent.ShowEntity.of(key, id);
    } catch (final IllegalArgumentException | InvalidKeyException ex) {
      throw new ParsingException("Exception parsing show_entity hover", ex, args);
    }
  }

  private HoverTransformation(final HoverEvent.Action<Object> action, final Object value) {
    this.action = action;
    this.value = value;
  }

  @Override
  public Component apply() {
    return Component.empty().hoverEvent(HoverEvent.hoverEvent(this.action, this.value));
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("action", this.action),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final HoverTransformation that = (HoverTransformation) other;
    return Objects.equals(this.action, that.action)
      && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.action, this.value);
  }
}
