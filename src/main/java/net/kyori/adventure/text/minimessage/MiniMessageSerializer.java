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
package net.kyori.adventure.text.minimessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static net.kyori.adventure.text.minimessage.Tokens.BOLD;
import static net.kyori.adventure.text.minimessage.Tokens.CLICK;
import static net.kyori.adventure.text.minimessage.Tokens.CLOSE_TAG;
import static net.kyori.adventure.text.minimessage.Tokens.COLOR;
import static net.kyori.adventure.text.minimessage.Tokens.FONT;
import static net.kyori.adventure.text.minimessage.Tokens.HOVER;
import static net.kyori.adventure.text.minimessage.Tokens.INSERTION;
import static net.kyori.adventure.text.minimessage.Tokens.ITALIC;
import static net.kyori.adventure.text.minimessage.Tokens.KEYBIND;
import static net.kyori.adventure.text.minimessage.Tokens.OBFUSCATED;
import static net.kyori.adventure.text.minimessage.Tokens.SEPARATOR;
import static net.kyori.adventure.text.minimessage.Tokens.STRIKETHROUGH;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_END;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_START;
import static net.kyori.adventure.text.minimessage.Tokens.TRANSLATABLE;
import static net.kyori.adventure.text.minimessage.Tokens.UNDERLINED;

final class MiniMessageSerializer {

  private MiniMessageSerializer() {
  }

  static @NonNull String serialize(final @NonNull Component component) {
    final List<ComponentNode> nodes = traverseNode(new ComponentNode(component));
    final StringBuilder sb = new StringBuilder();

    for(int i = 0; i < nodes.size(); i++) {
      // The previous node, null if it doesn't exist.
      final Style previous = ((i - 1) >= 0) ? nodes.get(i - 1).style() : null;

      // The next node, null if it doesn't exist.
      final Style next = ((i + 1) < nodes.size()) ? nodes.get(i + 1).style() : null;

      // The current node.
      final ComponentNode node = nodes.get(i);

      // Serialized string for the node.
      sb.append(serializeNode(node, previous, next));
    }
    return sb.toString();
  }

  // Sorts a ComponentNode's tree in a LinkedList using Pre Order Traversal.
  private static List<ComponentNode> traverseNode(@NonNull final ComponentNode root) {
    final List<ComponentNode> nodes = new LinkedList<>();
    nodes.add(root);

    // Only continue if children found.
    if(!root.component().children().isEmpty()) {
      for(final Component child : root.component().children()) {
        nodes.addAll(traverseNode(new ComponentNode(child, root.style())));
      }
    }

    return nodes;
  }

  // Serializes a single node into minimessage format.
  private static String serializeNode(@NonNull final ComponentNode node, @Nullable final Style previous, @Nullable final Style next) {
    final StringBuilder sb = new StringBuilder();
    final Style style = node.style();

    // # start tags

    // ## color
    if(style.color() != null && (previous == null || previous.color() != style.color())) {
      sb.append(startColor(Objects.requireNonNull(style.color())));
    }

    // ## decoration
    // ### only start if previous didn't start
    if(style.hasDecoration(TextDecoration.BOLD) && (previous == null || !previous.hasDecoration(TextDecoration.BOLD))) {
      sb.append(startTag(BOLD));
    }
    if(style.hasDecoration(TextDecoration.ITALIC) && (previous == null || !previous.hasDecoration(TextDecoration.ITALIC))) {
      sb.append(startTag(ITALIC));
    }
    if(style.hasDecoration(TextDecoration.OBFUSCATED) && (previous == null || !previous.hasDecoration(TextDecoration.OBFUSCATED))) {
      sb.append(startTag(OBFUSCATED));
    }
    if(style.hasDecoration(TextDecoration.STRIKETHROUGH) && (previous == null || !previous.hasDecoration(TextDecoration.STRIKETHROUGH))) {
      sb.append(startTag(STRIKETHROUGH));
    }
    if(style.hasDecoration(TextDecoration.UNDERLINED) && (previous == null || !previous.hasDecoration(TextDecoration.UNDERLINED))) {
      sb.append(startTag(UNDERLINED));
    }

    // ## hover
    // ### only start if prevComp didn't start the same one
    final HoverEvent<?> hov = style.hoverEvent();
    if(hov != null && (previous == null || areDifferent(hov, previous.hoverEvent()))) {
      serializeHoverEvent(sb, hov);
    }

    // ## click
    // ### only start if previous didn't start the same one
    final ClickEvent click = style.clickEvent();
    if(click != null && (previous == null || areDifferent(click, previous.clickEvent()))) {
      sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", CLICK, ClickEvent.Action.NAMES.key(click.action()), click.value())));
    }

    // ## insertion
    // ### only start if previous didn't start the same one
    final String insert = style.insertion();
    if(insert != null && (previous == null || !insert.equals(previous.insertion()))) {
      sb.append(startTag(INSERTION + SEPARATOR + insert));
    }

    // ## font
    final Key font = style.font();
    if(font != null && (previous == null || !font.equals(previous.font()))) {
      sb.append(startTag(FONT + SEPARATOR + font.asString()));
    }

    // # append text
    if(node.component() instanceof TextComponent) {
      sb.append(((TextComponent) node.component()).content());
    } else {
      handleDifferentComponent(node.component(), sb);
    }

    // # end tags

    // ## color
    if(next != null && style.color() != null && next.color() != style.color()) {
      sb.append(endColor(Objects.requireNonNull(style.color())));
    }

    // ## decoration
    // ### only end decoration if next tag is different
    if(next != null) {
      if(style.hasDecoration(TextDecoration.BOLD) && !next.hasDecoration(TextDecoration.BOLD)) {
        sb.append(endTag(BOLD));
      }
      if(style.hasDecoration(TextDecoration.ITALIC) && !next.hasDecoration(TextDecoration.ITALIC)) {
        sb.append(endTag(ITALIC));
      }
      if(style.hasDecoration(TextDecoration.OBFUSCATED) && !next.hasDecoration(TextDecoration.OBFUSCATED)) {
        sb.append(endTag(OBFUSCATED));
      }
      if(style.hasDecoration(TextDecoration.STRIKETHROUGH) && !next.hasDecoration(TextDecoration.STRIKETHROUGH)) {
        sb.append(endTag(STRIKETHROUGH));
      }
      if(style.hasDecoration(TextDecoration.UNDERLINED) && !next.hasDecoration(TextDecoration.UNDERLINED)) {
        sb.append(endTag(UNDERLINED));
      }
    }

    // ## hover
    // ### only end hover if next tag is different
    if(next != null && style.hoverEvent() != null) {
      if(areDifferent(Objects.requireNonNull(style.hoverEvent()), next.hoverEvent())) {
        sb.append(endTag(HOVER));
      }
    }

    // ## click
    // ### only end click if next tag is different
    if(next != null && style.clickEvent() != null) {
      if(areDifferent(Objects.requireNonNull(style.clickEvent()), next.clickEvent())) {
        sb.append(endTag(CLICK));
      }
    }

    // ## insertion
    // ### only end insertion if next tag is different
    if(next != null && style.insertion() != null) {
      if(!Objects.equals(style.insertion(), next.insertion())) {
        sb.append(endTag(INSERTION));
      }
    }

    // ## font
    // ### only end insertion if next tag is different
    if(next != null && style.font() != null) {
      if(!Objects.equals(style.font(), next.font())) {
        sb.append(endTag(FONT));
      }
    }
    return sb.toString();
  }

  private static void serializeHoverEvent(final @NonNull StringBuilder sb, final @NonNull HoverEvent<?> hov) {
    if(hov.action() == HoverEvent.Action.SHOW_TEXT) {
      sb.append(startTag(HOVER + SEPARATOR + HoverEvent.Action.NAMES.key(hov.action()) + SEPARATOR + "\"" + serialize((Component) hov.value()).replace("\"", "\\\"") + "\""));
    } else if(hov.action() == HoverEvent.Action.SHOW_ITEM) {
      final HoverEvent.ShowItem showItem = (HoverEvent.ShowItem) hov.value();
      final String nbt;
      if(showItem.nbt() != null) {
        nbt = SEPARATOR + "\"" + showItem.nbt().string().replace("\"", "\\\"") + "\"";
      } else {
        nbt = "";
      }
      sb.append(startTag(HOVER + SEPARATOR + HoverEvent.Action.NAMES.key(hov.action()) + SEPARATOR + "'" + showItem.item().asString() + "'" + SEPARATOR + showItem.count() + nbt));
    } else if(hov.action() == HoverEvent.Action.SHOW_ENTITY) {
      final HoverEvent.ShowEntity showEntity = (HoverEvent.ShowEntity) hov.value();
      final String displayName;
      if(showEntity.name() != null) {
        displayName = SEPARATOR + "\"" + serialize(showEntity.name()).replace("\"", "\\\"") + "\"";
      } else {
        displayName = "";
      }
      sb.append(startTag(HOVER + SEPARATOR + HoverEvent.Action.NAMES.key(hov.action()) + SEPARATOR + "'" + showEntity.type().asString() + "'" + SEPARATOR + showEntity.id() + displayName));
    } else {
      throw new RuntimeException("Don't know how to serialize '" + hov + "'!");
    }
  }

  private static boolean areDifferent(final @NonNull ClickEvent c1, final @Nullable ClickEvent c2) {
    if(c2 == null) return true;
    return !c1.equals(c2) && (!c1.action().equals(c2.action()) || !c1.value().equals(c2.value()));
  }

  private static boolean areDifferent(final @NonNull HoverEvent<?> h1, final @Nullable HoverEvent<?> h2) {
    if(h2 == null) return true;
    return !h1.equals(h2) && (!h1.action().equals(h2.action())); // TODO also compare value
  }

  private static @NonNull String startColor(final @NonNull TextColor color) {
    if(color instanceof NamedTextColor) {
      return startTag(Objects.requireNonNull(NamedTextColor.NAMES.key((NamedTextColor) color)));
    } else {
      return startTag(COLOR + SEPARATOR + color.asHexString());
    }
  }

  private static @NonNull String endColor(final @NonNull TextColor color) {
    if(color instanceof NamedTextColor) {
      return endTag(Objects.requireNonNull(NamedTextColor.NAMES.key((NamedTextColor) color)));
    } else {
      return endTag(COLOR + SEPARATOR + color.asHexString());
    }
  }

  private static @NonNull String startTag(final @NonNull String content) {
    return TAG_START + content + TAG_END;
  }

  private static @NonNull String endTag(final @NonNull String content) {
    return TAG_START + CLOSE_TAG + content + TAG_END;
  }

  private static void handleDifferentComponent(final @NonNull Component component, final @NonNull StringBuilder sb) {
    if(component instanceof KeybindComponent) {
      sb.append(startTag(KEYBIND + SEPARATOR + ((KeybindComponent) component).keybind()));
    } else if(component instanceof TranslatableComponent) {
      final StringBuilder args = new StringBuilder();
      for(final Component arg : ((TranslatableComponent) component).args()) {
        args.append(SEPARATOR)
          .append("\"")
          .append(serialize(arg).replace("\"", "\\\""))
          .append("\"");
      }
      sb.append(startTag(TRANSLATABLE + SEPARATOR + ((TranslatableComponent) component).key() + args));
    }
  }

  private static class ComponentNode {
    private final Component component;
    private final Style style;

    ComponentNode(@NonNull final Component component) {
      this(component, null);
    }

    ComponentNode(@NonNull final Component component, @Nullable final Style parent) {
      this.component = component;
      this.style = (parent == null) ? component.style() : component.style().merge(parent, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
    }

    public Component component() {
      return this.component;
    }

    public Style style() {
      return this.style;
    }
  }
}
