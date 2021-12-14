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
import net.kyori.adventure.text.minimessage.transformation.inbuild.ClickTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.ColorTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.DecorationTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.FontTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.HoverTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.InsertionTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.KeybindTransformation;
import net.kyori.adventure.text.minimessage.transformation.inbuild.TranslatableTransformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.minimessage.Tokens.CLOSE_TAG;
import static net.kyori.adventure.text.minimessage.Tokens.SEPARATOR;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_END;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_START;

final class MiniMessageSerializer {
  private MiniMessageSerializer() {
  }

  static @NotNull String serialize(final @NotNull Component component) {
    final List<ComponentNode> nodes = traverseNode(new ComponentNode(component));
    final StringBuilder sb = new StringBuilder();

    for (int i = 0; i < nodes.size(); i++) {
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
  private static List<ComponentNode> traverseNode(final @NotNull ComponentNode root) {
    final List<ComponentNode> nodes = new LinkedList<>();
    nodes.add(root);

    // Only continue if children found.
    if (!root.component().children().isEmpty()) {
      for (final Component child : root.component().children()) {
        nodes.addAll(traverseNode(new ComponentNode(child, root.style())));
      }
    }

    return nodes;
  }

  // Serializes a single node into minimessage format.
  private static String serializeNode(final @NotNull ComponentNode node, final @Nullable Style previous, final @Nullable Style next) {
    final StringBuilder sb = new StringBuilder();
    final Style style = node.style();

    // # start tags

    // ## color
    if (style.color() != null && (previous == null || previous.color() != style.color())) {
      sb.append(startColor(Objects.requireNonNull(style.color())));
    }

    // ## decoration
    // ### only start if previous didn't start
    if (style.hasDecoration(TextDecoration.BOLD) && (previous == null || !previous.hasDecoration(TextDecoration.BOLD))) {
      sb.append(startTag(DecorationTransformation.BOLD));
    }
    if (style.hasDecoration(TextDecoration.ITALIC) && (previous == null || !previous.hasDecoration(TextDecoration.ITALIC))) {
      sb.append(startTag(DecorationTransformation.ITALIC));
    }
    if (style.hasDecoration(TextDecoration.OBFUSCATED) && (previous == null || !previous.hasDecoration(TextDecoration.OBFUSCATED))) {
      sb.append(startTag(DecorationTransformation.OBFUSCATED));
    }
    if (style.hasDecoration(TextDecoration.STRIKETHROUGH) && (previous == null || !previous.hasDecoration(TextDecoration.STRIKETHROUGH))) {
      sb.append(startTag(DecorationTransformation.STRIKETHROUGH));
    }
    if (style.hasDecoration(TextDecoration.UNDERLINED) && (previous == null || !previous.hasDecoration(TextDecoration.UNDERLINED))) {
      sb.append(startTag(DecorationTransformation.UNDERLINED));
    }

    // ## disabled decorations
    // ### only start if previous didn't start
    if (style.decoration(TextDecoration.BOLD) == TextDecoration.State.FALSE && (previous == null || previous.decoration(TextDecoration.BOLD) == TextDecoration.State.NOT_SET)) {
      sb.append(startTag("!" + DecorationTransformation.BOLD));
    }
    if (style.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE && (previous == null || previous.decoration(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET)) {
      sb.append(startTag("!" + DecorationTransformation.ITALIC));
    }
    if (style.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.FALSE && (previous == null || previous.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.NOT_SET)) {
      sb.append(startTag("!" + DecorationTransformation.OBFUSCATED));
    }
    if (style.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.FALSE && (previous == null || previous.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.NOT_SET)) {
      sb.append(startTag("!" + DecorationTransformation.STRIKETHROUGH));
    }
    if (style.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.FALSE && (previous == null || previous.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.NOT_SET)) {
      sb.append(startTag("!" + DecorationTransformation.UNDERLINED));
    }

    // ## hover
    // ### only start if prevComp didn't start the same one
    final HoverEvent<?> hov = style.hoverEvent();
    if (hov != null && (previous == null || areDifferent(hov, previous.hoverEvent()))) {
      serializeHoverEvent(sb, hov);
    }

    // ## click
    // ### only start if previous didn't start the same one
    final ClickEvent click = style.clickEvent();
    if (click != null && (previous == null || areDifferent(click, previous.clickEvent()))) {
      sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", ClickTransformation.CLICK, ClickEvent.Action.NAMES.key(click.action()), click.value())));
    }

    // ## insertion
    // ### only start if previous didn't start the same one
    final String insert = style.insertion();
    if (insert != null && (previous == null || !insert.equals(previous.insertion()))) {
      sb.append(startTag(InsertionTransformation.INSERTION + SEPARATOR + insert));
    }

    // ## font
    final Key font = style.font();
    if (font != null && (previous == null || !font.equals(previous.font()))) {
      sb.append(startTag(FontTransformation.FONT + SEPARATOR + font.asString()));
    }

    // # append text
    if (node.component() instanceof TextComponent) {
      sb.append(((TextComponent) node.component()).content());
    } else {
      handleDifferentComponent(node.component(), sb);
    }

    // # end tags
    // ### these must be in reverse order to avoid https://github.com/KyoriPowered/adventure-text-minimessage/issues/151

    // ## font
    // ### only end insertion if next tag is different
    if (next != null && style.font() != null) {
      if (!Objects.equals(style.font(), next.font())) {
        sb.append(endTag(FontTransformation.FONT));
      }
    }

    // ## insertion
    // ### only end insertion if next tag is different
    if (next != null && style.insertion() != null) {
      if (!Objects.equals(style.insertion(), next.insertion())) {
        sb.append(endTag(InsertionTransformation.INSERTION));
      }
    }

    // ## click
    // ### only end click if next tag is different
    if (next != null && style.clickEvent() != null) {
      if (areDifferent(Objects.requireNonNull(style.clickEvent()), next.clickEvent())) {
        sb.append(endTag(ClickTransformation.CLICK));
      }
    }

    // ## hover
    // ### only end hover if next tag is different
    if (next != null && style.hoverEvent() != null) {
      if (areDifferent(Objects.requireNonNull(style.hoverEvent()), next.hoverEvent())) {
        sb.append(endTag(HoverTransformation.HOVER));
      }
    }

    // ## decoration
    // ### only end decoration if next tag is different
    if (next != null) {
      if (style.hasDecoration(TextDecoration.UNDERLINED) && !next.hasDecoration(TextDecoration.UNDERLINED)) {
        sb.append(endTag(DecorationTransformation.UNDERLINED));
      }
      if (style.hasDecoration(TextDecoration.STRIKETHROUGH) && !next.hasDecoration(TextDecoration.STRIKETHROUGH)) {
        sb.append(endTag(DecorationTransformation.STRIKETHROUGH));
      }
      if (style.hasDecoration(TextDecoration.OBFUSCATED) && !next.hasDecoration(TextDecoration.OBFUSCATED)) {
        sb.append(endTag(DecorationTransformation.OBFUSCATED));
      }
      if (style.hasDecoration(TextDecoration.ITALIC) && !next.hasDecoration(TextDecoration.ITALIC)) {
        sb.append(endTag(DecorationTransformation.ITALIC));
      }
      if (style.hasDecoration(TextDecoration.BOLD) && !next.hasDecoration(TextDecoration.BOLD)) {
        sb.append(endTag(DecorationTransformation.BOLD));
      }
    }

    // ## disabled decorations
    // ### only end decoration if next tag is different
    if (next != null) {
      if (style.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.FALSE && next.decoration(TextDecoration.UNDERLINED) == TextDecoration.State.NOT_SET) {
        sb.append(endTag("!" + DecorationTransformation.UNDERLINED));
      }
      if (style.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.FALSE && next.decoration(TextDecoration.STRIKETHROUGH) == TextDecoration.State.NOT_SET) {
        sb.append(endTag("!" + DecorationTransformation.STRIKETHROUGH));
      }
      if (style.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.FALSE && next.decoration(TextDecoration.OBFUSCATED) == TextDecoration.State.NOT_SET) {
        sb.append(endTag("!" + DecorationTransformation.OBFUSCATED));
      }
      if (style.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE && next.decoration(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET) {
        sb.append(endTag("!" + DecorationTransformation.ITALIC));
      }
      if (style.decoration(TextDecoration.BOLD) == TextDecoration.State.FALSE && next.decoration(TextDecoration.BOLD) == TextDecoration.State.NOT_SET) {
        sb.append(endTag("!" + DecorationTransformation.BOLD));
      }
    }

    // ## color
    if (next != null && style.color() != null && next.color() != style.color()) {
      sb.append(endColor(Objects.requireNonNull(style.color())));
    }

    return sb.toString();
  }

  private static void serializeHoverEvent(final @NotNull StringBuilder sb, final @NotNull HoverEvent<?> hov) {
    if (hov.action() == HoverEvent.Action.SHOW_TEXT) {
      sb.append(startTag(HoverTransformation.HOVER + SEPARATOR + HoverEvent.Action.NAMES.key(hov.action()) + SEPARATOR + "\"" + serialize((Component) hov.value()).replace("\"", "\\\"") + "\""));
    } else if (hov.action() == HoverEvent.Action.SHOW_ITEM) {
      final HoverEvent.ShowItem showItem = (HoverEvent.ShowItem) hov.value();
      final String nbt;
      if (showItem.nbt() != null) {
        nbt = SEPARATOR + "\"" + showItem.nbt().string().replace("\"", "\\\"") + "\"";
      } else {
        nbt = "";
      }
      sb.append(startTag(HoverTransformation.HOVER + SEPARATOR + HoverEvent.Action.NAMES.key(hov.action()) + SEPARATOR + "'" + showItem.item().asString() + "'" + SEPARATOR + showItem.count() + nbt));
    } else if (hov.action() == HoverEvent.Action.SHOW_ENTITY) {
      final HoverEvent.ShowEntity showEntity = (HoverEvent.ShowEntity) hov.value();
      final String displayName;
      if (showEntity.name() != null) {
        displayName = SEPARATOR + "\"" + serialize(showEntity.name()).replace("\"", "\\\"") + "\"";
      } else {
        displayName = "";
      }
      sb.append(startTag(HoverTransformation.HOVER + SEPARATOR + HoverEvent.Action.NAMES.key(hov.action()) + SEPARATOR + "'" + showEntity.type().asString() + "'" + SEPARATOR + showEntity.id() + displayName));
    } else {
      throw new RuntimeException("Don't know how to serialize '" + hov + "'!");
    }
  }

  private static boolean areDifferent(final @NotNull ClickEvent c1, final @Nullable ClickEvent c2) {
    if (c2 == null) return true;
    return !c1.equals(c2) && (!c1.action().equals(c2.action()) || !c1.value().equals(c2.value()));
  }

  private static boolean areDifferent(final @NotNull HoverEvent<?> h1, final @Nullable HoverEvent<?> h2) {
    if (h2 == null) return true;
    return !h1.equals(h2) && (!h1.action().equals(h2.action())); // TODO also compare value
  }

  private static @NotNull String startColor(final @NotNull TextColor color) {
    if (color instanceof NamedTextColor) {
      return startTag(Objects.requireNonNull(NamedTextColor.NAMES.key((NamedTextColor) color)));
    } else {
      return startTag(ColorTransformation.COLOR + SEPARATOR + color.asHexString());
    }
  }

  private static @NotNull String endColor(final @NotNull TextColor color) {
    if (color instanceof NamedTextColor) {
      return endTag(Objects.requireNonNull(NamedTextColor.NAMES.key((NamedTextColor) color)));
    } else {
      return endTag(ColorTransformation.COLOR + SEPARATOR + color.asHexString());
    }
  }

  private static @NotNull String startTag(final @NotNull String content) {
    return "" + TAG_START + content + TAG_END;
  }

  private static @NotNull String endTag(final @NotNull String content) {
    return ("" + TAG_START) + CLOSE_TAG + content + TAG_END;
  }

  private static void handleDifferentComponent(final @NotNull Component component, final @NotNull StringBuilder sb) {
    if (component instanceof KeybindComponent) {
      sb.append(startTag(KeybindTransformation.KEYBIND + SEPARATOR + ((KeybindComponent) component).keybind()));
    } else if (component instanceof TranslatableComponent) {
      final StringBuilder args = new StringBuilder();
      for (final Component arg : ((TranslatableComponent) component).args()) {
        args.append(SEPARATOR)
          .append("\"")
          .append(serialize(arg).replace("\"", "\\\""))
          .append("\"");
      }
      sb.append(startTag(TranslatableTransformation.TRANSLATABLE + SEPARATOR + ((TranslatableComponent) component).key() + args));
    }
  }

  private static class ComponentNode {
    private final Component component;
    private final Style style;

    ComponentNode(final @NotNull Component component) {
      this(component, null);
    }

    ComponentNode(final @NotNull Component component, final @Nullable Style parent) {
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
