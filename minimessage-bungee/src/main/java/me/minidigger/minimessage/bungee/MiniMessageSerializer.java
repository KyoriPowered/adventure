package me.minidigger.minimessage.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Locale;

import static me.minidigger.minimessage.bungee.Constants.BOLD;
import static me.minidigger.minimessage.bungee.Constants.CLICK;
import static me.minidigger.minimessage.bungee.Constants.CLOSE_TAG;
import static me.minidigger.minimessage.bungee.Constants.HOVER;
import static me.minidigger.minimessage.bungee.Constants.ITALIC;
import static me.minidigger.minimessage.bungee.Constants.OBFUSCATED;
import static me.minidigger.minimessage.bungee.Constants.SEPARATOR;
import static me.minidigger.minimessage.bungee.Constants.STRIKETHROUGH;
import static me.minidigger.minimessage.bungee.Constants.TAG_END;
import static me.minidigger.minimessage.bungee.Constants.TAG_START;
import static me.minidigger.minimessage.bungee.Constants.UNDERLINED;

public final class MiniMessageSerializer {

    private MiniMessageSerializer(){
    }

    @Nonnull
    public static String serialize(@Nonnull BaseComponent... components) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < components.length; i++) {
            BaseComponent comp = components[i];

            // # start tags

            // ## get prev comp
            BaseComponent prevComp = null;
            if (i > 0) {
                prevComp = components[i - 1];
            }

            // ## color
            // ### white is not important
            if (!ChatColor.WHITE.equals(comp.getColor())) {
                sb.append(startColor(comp.getColor()));
            }

            // ## decoration
            // ### only start if prevComp didn't start
            if (comp.isBold() && (prevComp == null || !prevComp.isBold())) {
                sb.append(startTag(BOLD));
            }
            if (comp.isItalic() && (prevComp == null || !prevComp.isItalic())) {
                sb.append(startTag(ITALIC));
            }
            if (comp.isObfuscated() && (prevComp == null || !prevComp.isObfuscated())) {
                sb.append(startTag(OBFUSCATED));
            }
            if (comp.isStrikethrough() && (prevComp == null || !prevComp.isStrikethrough())) {
                sb.append(startTag(STRIKETHROUGH));
            }
            if (comp.isUnderlined() && (prevComp == null || !prevComp.isUnderlined())) {
                sb.append(startTag(UNDERLINED));
            }

            // ## hover
            // ### only start if prevComp didn't start the same one
            HoverEvent hov = comp.getHoverEvent();
            if (hov != null && (prevComp == null || areDifferent(hov, prevComp.getHoverEvent()))) {
                sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", HOVER, hov.getAction().name().toLowerCase(Locale.ROOT), serialize(hov.getValue()))));
            }

            // ## click
            // ### only start if prevComp didn't start the same one
            ClickEvent click = comp.getClickEvent();
            if (click != null && (prevComp == null || areDifferent(click, prevComp.getClickEvent()))) {
                sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", CLICK, click.getAction().name().toLowerCase(Locale.ROOT), click.getValue())));
            }

            // # append text
            sb.append(comp.toPlainText());

            // # end tags

            // ## get next comp
            BaseComponent nextComp = null;
            if (i + 1 < components.length) {
                nextComp = components[i + 1];
            }

            // ## color
            // ### only end color if next comp is white and current isn't
            if (nextComp != null && comp.getColor() != ChatColor.WHITE) {
                if (nextComp.getColor() == ChatColor.WHITE || nextComp.getColor() == null) {
                    sb.append(endColor(comp.getColor()));
                }
            }

            // ## decoration
            // ### only end decoration if next tag is different
            if (nextComp != null) {
                if (comp.isBold() && !nextComp.isBold()) {
                    sb.append(endTag(BOLD));
                }
                if (comp.isItalic() && !nextComp.isItalic()) {
                    sb.append(endTag(ITALIC));
                }
                if (comp.isObfuscated() && !nextComp.isObfuscated()) {
                    sb.append(endTag(OBFUSCATED));
                }
                if (comp.isStrikethrough() && !nextComp.isStrikethrough()) {
                    sb.append(endTag(STRIKETHROUGH));
                }
                if (comp.isUnderlined() && !nextComp.isUnderlined()) {
                    sb.append(endTag(UNDERLINED));
                }
            }

            // ## hover
            // ### only end hover if next tag is different
            if (nextComp != null && comp.getHoverEvent() != null) {
                if (areDifferent(comp.getHoverEvent(), nextComp.getHoverEvent())) {
                    sb.append(endTag(HOVER));
                }
            }

            // ## click
            // ### only end click if next tag is different
            if (nextComp != null && comp.getClickEvent() != null) {
                if (areDifferent(comp.getClickEvent(), nextComp.getClickEvent())) {
                    sb.append(endTag(CLICK));
                }
            }
        }

        return sb.toString();
    }

    private static boolean areDifferent(@Nonnull ClickEvent c1, @Nullable ClickEvent c2) {
        if (c2 == null) return true;
        return !c1.equals(c2) && (!c1.getAction().equals(c2.getAction()) || !c1.getValue().equals(c2.getValue()));
    }

    private static boolean areDifferent(@Nonnull HoverEvent h1, @Nullable HoverEvent h2) {
        if (h2 == null) return true;
        return !h1.equals(h2) && (!h1.getAction().equals(h2.getAction()));// TODO also compare value
    }

    @Nonnull
    private static String startColor(@Nonnull ChatColor color) {
        return startTag(color.getName());
    }

    @Nonnull
    private static String endColor(@Nonnull ChatColor color) {
        return endTag(color.getName());
    }

    @Nonnull
    private static String startTag(@Nonnull String content) {
        return TAG_START + content + TAG_END;
    }

    @Nonnull
    private static String endTag(@Nonnull String content) {
        return TAG_START + CLOSE_TAG + content + TAG_END;
    }
}
