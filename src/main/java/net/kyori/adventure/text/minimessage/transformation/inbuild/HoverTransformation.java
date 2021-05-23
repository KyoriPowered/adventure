/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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

import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A transformation that applies a {@link HoverEvent}.
 *
 * @since 4.1.0
 */
public final class HoverTransformation extends Transformation {
  // https://regex101.com/r/wC2xT6/1 splits on ':', except when in single or double quotes, respecting escaped quotes.
  private static final Pattern REALLY_DUM_SPLIT_PATTERN = Pattern.compile("(?s):(?=(?:((?<!\\\\)[\"'])(?:(?!(?<!\\\\)\\1).)*(?<!\\\\)\\1|\\\\.|[^\"'])*$)");

  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.1.0
   */
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.HOVER);
  }

  private HoverEvent.Action<Object> action;
  private Object value;

  private HoverTransformation() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public void load(final String name, final List<String> args) {
    super.load(name, args);

    if(args.size() < 2) {
      throw new ParsingException("Doesn't know how to turn " + args + " into a hover event", -1);
    }

    final String string = String.join("", args.subList(1, args.size()));

    this.action = (HoverEvent.Action<Object>) HoverEvent.Action.NAMES.value(args.get(0));
    if(this.action == (Object) HoverEvent.Action.SHOW_TEXT) {
      this.value = context.parse(string);
    } else if(this.action == (Object) HoverEvent.Action.SHOW_ITEM) {
      this.value = this.parseShowItem(string);
    } else if(this.action == (Object) HoverEvent.Action.SHOW_ENTITY) {
      this.value = this.parseShowEntity(string);
    } else {
      throw new ParsingException("Don't know how to turn '" + args + "' into a hover event", -1);
    }
  }

  private HoverEvent.@NonNull ShowItem parseShowItem(final @NonNull String value) {
    try {
      final String[] args = REALLY_DUM_SPLIT_PATTERN.split(value);
      if(args.length == 0) {
        throw new RuntimeException("Show item hover needs at least item id!");
      }
      final Key key = Key.key(args[0]);
      final int count;
      if(args.length >= 2) {
        count = Integer.parseInt(args[1]);
      } else {
        count = 1;
      }
      if(args.length == 3) {
        return HoverEvent.ShowItem.of(key, count, BinaryTagHolder.of(args[2]));
      }
      return HoverEvent.ShowItem.of(key, count);
    } catch(final InvalidKeyException | NumberFormatException ex) {
      throw new RuntimeException(String.format("Exception parsing show_item hover '%s'.", value), ex);
    }
  }

  private HoverEvent.@NonNull ShowEntity parseShowEntity(final @NonNull String value) {
    try {
      final String[] args = REALLY_DUM_SPLIT_PATTERN.split(value);
      if(args.length <= 1) {
        throw new RuntimeException("Show entity hover needs at least type and uuid!");
      }
      final Key key = Key.key(args[0]);
      final UUID id = UUID.fromString(args[1]);
      if(args.length == 3) {
        final Component name = context.parse(args[2]);
        return HoverEvent.ShowEntity.of(key, id, name);
      }
      return HoverEvent.ShowEntity.of(key, id);
    } catch(final IllegalArgumentException | InvalidKeyException ex) {
      throw new RuntimeException(String.format("Exception parsing show_entity hover '%s'.", value), ex);
    }
  }

  @Override
  public Component apply() {
    return Component.empty().hoverEvent(HoverEvent.hoverEvent(this.action, this.value));
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
            ExaminableProperty.of("action", this.action),
            ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final HoverTransformation that = (HoverTransformation) other;
    return Objects.equals(this.action, that.action)
      && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.action, this.value);
  }

  /**
   * Factory for {@link HoverTransformation} instances.
   *
   * @since 4.1.0
   */
  public static class Parser implements TransformationParser<HoverTransformation> {
    @Override
    public HoverTransformation parse() {
      return new HoverTransformation();
    }
  }
}
