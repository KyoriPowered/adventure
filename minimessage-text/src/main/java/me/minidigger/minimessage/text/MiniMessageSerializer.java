package me.minidigger.minimessage.text;

import net.kyori.text.Component;
import net.kyori.text.KeybindComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static me.minidigger.minimessage.text.Constants.BOLD;
import static me.minidigger.minimessage.text.Constants.CLICK;
import static me.minidigger.minimessage.text.Constants.CLOSE_TAG;
import static me.minidigger.minimessage.text.Constants.HOVER;
import static me.minidigger.minimessage.text.Constants.INSERTION;
import static me.minidigger.minimessage.text.Constants.ITALIC;
import static me.minidigger.minimessage.text.Constants.OBFUSCATED;
import static me.minidigger.minimessage.text.Constants.SEPARATOR;
import static me.minidigger.minimessage.text.Constants.STRIKETHROUGH;
import static me.minidigger.minimessage.text.Constants.TAG_END;
import static me.minidigger.minimessage.text.Constants.TAG_START;
import static me.minidigger.minimessage.text.Constants.UNDERLINED;

public final class MiniMessageSerializer {

    private MiniMessageSerializer() {
    }

    @Nonnull
    public static String serialize(@Nonnull Component component) {
        StringBuilder sb = new StringBuilder();

        List<Component> components = new ArrayList<>();
        components.add(component);

        for (int i = 0; i < components.size(); i++) {
            Component comp = components.get(i);

            // add childs
            components.addAll(comp.children());

            // # start tags

            // ## get prev comp
            Component prevComp = null;
            if (i > 0) {
                prevComp = components.get(i - 1);
            }

            // ## color
            // ### white is not important
            if (!TextColor.WHITE.equals(comp.color()) && comp.color() != null && (prevComp == null || prevComp.color() != comp.color())) {
                sb.append(startColor(Objects.requireNonNull(comp.color())));
            }

            // ## decoration
            // ### only start if prevComp didn't start
            if (comp.hasDecoration(TextDecoration.BOLD) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.BOLD))) {
                sb.append(startTag(BOLD));
            }
            if (comp.hasDecoration(TextDecoration.ITALIC) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.ITALIC))) {
                sb.append(startTag(ITALIC));
            }
            if (comp.hasDecoration(TextDecoration.OBFUSCATED) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.OBFUSCATED))) {
                sb.append(startTag(OBFUSCATED));
            }
            if (comp.hasDecoration(TextDecoration.STRIKETHROUGH) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.STRIKETHROUGH))) {
                sb.append(startTag(STRIKETHROUGH));
            }
            if (comp.hasDecoration(TextDecoration.UNDERLINED) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.UNDERLINED))) {
                sb.append(startTag(UNDERLINED));
            }

            // ## hover
            // ### only start if prevComp didn't start the same one
            HoverEvent hov = comp.hoverEvent();
            if (hov != null && (prevComp == null || areDifferent(hov, prevComp.hoverEvent()))) {
                sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", HOVER, HoverEvent.Action.NAMES.name(hov.action()), serialize(hov.value()))));
            }

            // ## click
            // ### only start if prevComp didn't start the same one
            ClickEvent click = comp.clickEvent();
            if (click != null && (prevComp == null || areDifferent(click, prevComp.clickEvent()))) {
                sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", CLICK, ClickEvent.Action.NAMES.name(click.action()), click.value())));
            }

            // ## insertion
            // ### only start if prevComp didn't start the same one
            String insert = comp.insertion();
            if (insert != null && (prevComp == null ||  !insert.equals(prevComp.insertion()))) {
                sb.append(startTag(INSERTION + SEPARATOR + insert));
            }

            // # append text
            if (comp instanceof TextComponent) {
                sb.append(((TextComponent) comp).content());
            } else {
                handleDifferentComponent(comp, sb);
            }

            // # end tags

            // ## get next comp
            Component nextComp = null;
            if (i + 1 < components.size()) {
                nextComp = components.get(i + 1);
            }

            // ## color
            // ### only end color if next comp is white and current isn't
            if (nextComp != null && comp.color() != TextColor.WHITE && comp.color() != null) {
                if (nextComp.color() == TextColor.WHITE || nextComp.color() == null) {
                    sb.append(endColor(Objects.requireNonNull(comp.color())));
                }
            }

            // ## decoration
            // ### only end decoration if next tag is different
            if (nextComp != null) {
                if (comp.hasDecoration(TextDecoration.BOLD) && !nextComp.hasDecoration(TextDecoration.BOLD)) {
                    sb.append(endTag(BOLD));
                }
                if (comp.hasDecoration(TextDecoration.ITALIC) && !nextComp.hasDecoration(TextDecoration.ITALIC)) {
                    sb.append(endTag(ITALIC));
                }
                if (comp.hasDecoration(TextDecoration.OBFUSCATED) && !nextComp.hasDecoration(TextDecoration.OBFUSCATED)) {
                    sb.append(endTag(OBFUSCATED));
                }
                if (comp.hasDecoration(TextDecoration.STRIKETHROUGH) && !nextComp.hasDecoration(TextDecoration.STRIKETHROUGH)) {
                    sb.append(endTag(STRIKETHROUGH));
                }
                if (comp.hasDecoration(TextDecoration.UNDERLINED) && !nextComp.hasDecoration(TextDecoration.UNDERLINED)) {
                    sb.append(endTag(UNDERLINED));
                }
            }

            // ## hover
            // ### only end hover if next tag is different
            if (nextComp != null && comp.hoverEvent() != null) {
                if (areDifferent(Objects.requireNonNull(comp.hoverEvent()), nextComp.hoverEvent())) {
                    sb.append(endTag(HOVER));
                }
            }

            // ## click
            // ### only end click if next tag is different
            if (nextComp != null && comp.clickEvent() != null) {
                if (areDifferent(Objects.requireNonNull(comp.clickEvent()), nextComp.clickEvent())) {
                    sb.append(endTag(CLICK));
                }
            }

            // ## insertion
            // ### only end insertion if next tag is different
            if (nextComp != null && comp.insertion() != null) {
                if (!Objects.equals(comp.insertion(), nextComp.insertion())) {
                    sb.append(endTag(INSERTION));
                }
            }
        }

        return sb.toString();
    }

    private static boolean areDifferent(@Nonnull ClickEvent c1, @Nullable ClickEvent c2) {
        if (c2 == null) return true;
        return !c1.equals(c2) && (!c1.action().equals(c2.action()) || !c1.value().equals(c2.value()));
    }

    private static boolean areDifferent(@Nonnull HoverEvent h1, @Nullable HoverEvent h2) {
        if (h2 == null) return true;
        return !h1.equals(h2) && (!h1.action().equals(h2.action()));// TODO also compare value
    }

    @Nonnull
    private static String startColor(@Nonnull TextColor color) {
        return startTag(TextColor.NAMES.name(color));
    }

    @Nonnull
    private static String endColor(@Nonnull TextColor color) {
        return endTag(TextColor.NAMES.name(color));
    }

    @Nonnull
    private static String startTag(@Nonnull String content) {
        return TAG_START + content + TAG_END;
    }

    @Nonnull
    private static String endTag(@Nonnull String content) {
        return TAG_START + CLOSE_TAG + content + TAG_END;
    }

    private static void handleDifferentComponent(@Nonnull Component component, @Nonnull StringBuilder sb) {
        if (component instanceof KeybindComponent) {
            sb.append(startTag("key" + SEPARATOR + ((KeybindComponent) component).keybind()));
        } else if (component instanceof TranslatableComponent) {
            sb.append(startTag("lang" + SEPARATOR + ((TranslatableComponent) component).key()));
        }
    }
}
