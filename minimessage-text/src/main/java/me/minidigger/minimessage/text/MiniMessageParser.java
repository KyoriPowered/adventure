package me.minidigger.minimessage.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import me.minidigger.minimessage.text.fancy.Fancy;
import me.minidigger.minimessage.text.fancy.Gradient;
import me.minidigger.minimessage.text.fancy.Rainbow;

import static me.minidigger.minimessage.text.Constants.CLICK;
import static me.minidigger.minimessage.text.Constants.CLOSE_TAG;
import static me.minidigger.minimessage.text.Constants.COLOR;
import static me.minidigger.minimessage.text.Constants.GRADIENT;
import static me.minidigger.minimessage.text.Constants.HOVER;
import static me.minidigger.minimessage.text.Constants.INSERTION;
import static me.minidigger.minimessage.text.Constants.KEYBIND;
import static me.minidigger.minimessage.text.Constants.PRE;
import static me.minidigger.minimessage.text.Constants.RAINBOW;
import static me.minidigger.minimessage.text.Constants.RESET;
import static me.minidigger.minimessage.text.Constants.SEPARATOR;
import static me.minidigger.minimessage.text.Constants.TAG_END;
import static me.minidigger.minimessage.text.Constants.TAG_START;
import static me.minidigger.minimessage.text.Constants.TRANSLATABLE;

public class MiniMessageParser {

    // regex group names
    private static final String START = "start";
    private static final String TOKEN = "token";
    private static final String INNER = "inner";
    private static final String END = "end";
    // https://regex101.com/r/8VZ7uA/10
    private static final Pattern pattern = Pattern.compile("((?<start><)(?<token>[^<>]+(:(?<inner>['\"]?([^'\"](\\\\['\"])?)+['\"]?))*)(?<end>>))+?");

    private static final Pattern dumSplitPattern = Pattern.compile("['\"]:['\"]");

    private static final Map<Class<? extends Fancy>, Fancy> empty = new HashMap<>();

    @Nonnull
    public static String escapeTokens(@Nonnull String richMessage) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern.matcher(richMessage);
        int lastEnd = 0;
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            if (startIndex > lastEnd) {
                sb.append(richMessage, lastEnd, startIndex);
            }
            lastEnd = endIndex;

            String start = matcher.group(START);
            String token = matcher.group(TOKEN);
            String inner = matcher.group(INNER);
            String end = matcher.group(END);

            // also escape inner
            if (inner != null) {
                token = token.replace(inner, escapeTokens(inner));
            }

            sb.append("\\").append(start).append(token).append("\\").append(end);
        }

        if (richMessage.length() > lastEnd) {
            sb.append(richMessage.substring(lastEnd));
        }

        return sb.toString();
    }

    @Nonnull
    public static String stripTokens(@Nonnull String richMessage) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern.matcher(richMessage);
        int lastEnd = 0;
        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            if (startIndex > lastEnd) {
                sb.append(richMessage, lastEnd, startIndex);
            }
            lastEnd = endIndex;
        }

        if (richMessage.length() > lastEnd) {
            sb.append(richMessage.substring(lastEnd));
        }

        return sb.toString();
    }

    @Nonnull
    public static String handlePlaceholders(@Nonnull String richMessage, @Nonnull String... placeholders) {
        if (placeholders.length % 2 != 0) {
            throw new ParseException(
                    "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)");
        }
        for (int i = 0; i < placeholders.length; i += 2) {
            richMessage = richMessage.replace(TAG_START + placeholders[i] + TAG_END, placeholders[i + 1]);
        }
        return richMessage;
    }

    @Nonnull
    public static String handlePlaceholders(@Nonnull String richMessage, @Nonnull Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            richMessage = richMessage.replace(TAG_START + entry.getKey() + TAG_END, entry.getValue());
        }
        return richMessage;
    }

    @Nonnull
    public static Component parseFormat(@Nonnull String richMessage, @Nonnull String... placeholders) {
        return parseFormat(handlePlaceholders(richMessage, placeholders));
    }

    @Nonnull
    public static Component parseFormat(@Nonnull String richMessage, @Nonnull Map<String, String> placeholders) {
        return parseFormat(handlePlaceholders(richMessage, placeholders));
    }

    @Nonnull
    public static Component parseFormat(@Nonnull String richMessage) {
        TextComponent.Builder parent = TextComponent.builder("");

        ArrayDeque<ClickEvent> clickEvents = new ArrayDeque<>();
        ArrayDeque<HoverEvent<?>> hoverEvents = new ArrayDeque<>();
        ArrayDeque<TextColor> colors = new ArrayDeque<>();
        ArrayDeque<String> insertions = new ArrayDeque<>();
        EnumSet<HelperTextDecoration> decorations = EnumSet.noneOf(HelperTextDecoration.class);
        boolean isPreformatted = false;
        Map<Class<? extends Fancy>, Fancy> fancy = new LinkedHashMap<>();

        Matcher matcher = pattern.matcher(richMessage);
        int lastEnd = 0;
        while (matcher.find()) {
            Component current = null;
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            String msg = null;
            if (startIndex > lastEnd) {
                msg = richMessage.substring(lastEnd, startIndex);
            }
            lastEnd = endIndex;

            // handle message
            if (msg != null && msg.length() != 0) {
                // append message
                current = TextComponent.of(msg);
                current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy);

            }

            String token = matcher.group(TOKEN);
            String inner = matcher.group(INNER);

            Optional<HelperTextDecoration> deco;
            Optional<TextColor> color;

            // handle pre
            if (isPreformatted) {
                if (token.startsWith(CLOSE_TAG + PRE)) {
                    isPreformatted = false;
                    if (current != null) {
                        parent.append(current);
                    }
                } else {
                    if (current != null) {
                        parent.append(current);
                    }
                    current = TextComponent.of(TAG_START + token + TAG_END);
                    current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy);
                    parent.append(current);
                }
                continue;
            }

            // click
            if (token.startsWith(CLICK + SEPARATOR)) {
                clickEvents.push(handleClick(token, inner));
            } else if (token.equals(CLOSE_TAG + CLICK)) {
                clickEvents.pollFirst();
            }
            // hover
            else if (token.startsWith(HOVER + SEPARATOR)) {
                hoverEvents.push(handleHover(token, inner));
            } else if (token.equals(CLOSE_TAG + HOVER)) {
                hoverEvents.pollFirst();
            }
            // decoration
            else if ((deco = resolveDecoration(token)).isPresent()) {
                decorations.add(deco.get());
            } else if (token.startsWith(CLOSE_TAG) && (deco = resolveDecoration(token.replace(CLOSE_TAG, ""))).isPresent()) {
                decorations.remove(deco.get());
            }
            // color
            else if ((color = resolveColor(token)).isPresent()) {
                colors.push(color.get());
            } else if (token.startsWith(CLOSE_TAG) && resolveColor(token.replace(CLOSE_TAG, "")).isPresent()) {
                colors.pollFirst();
            }
            // color; hex or named syntax
            else if (token.startsWith(COLOR + SEPARATOR) && (color = resolveColorNew(token)).isPresent()) {
                colors.push(color.get());
            } else if (token.startsWith(CLOSE_TAG + COLOR) && resolveColorNew(token.replace(CLOSE_TAG, "")).isPresent()) {
                colors.pollFirst();
            }
            // keybind
            else if (token.startsWith(KEYBIND + SEPARATOR)) {
                if (current != null) {
                    parent.append(current);
                }
                current = handleKeybind(token);
                current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy);
            }
            // translatable
            else if (token.startsWith(TRANSLATABLE + SEPARATOR)) {
                if (current != null) {
                    parent.append(current);
                }
                current = handleTranslatable(token, inner);
                current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy);
            }
            // insertion
            else if (token.startsWith(INSERTION + SEPARATOR)) {
                insertions.push(handleInsertion(token));
            } else if (token.startsWith(CLOSE_TAG + INSERTION)) {
                insertions.pop();
            }
            // reset
            else if (token.startsWith(RESET)) {
                clickEvents.clear();
                hoverEvents.clear();
                colors.clear();
                insertions.clear();
                decorations.clear();
            }
            // pre
            else if (token.startsWith(PRE)) {
                isPreformatted = true;
            }
            // rainbow
            else if (token.startsWith(RAINBOW)) {
                fancy.put(Rainbow.class, handleRainbow(token));
            } else if (token.startsWith(CLOSE_TAG + RAINBOW)) {
                fancy.remove(Rainbow.class);
            }
            // gradient
            else if (token.startsWith(GRADIENT)) {
                fancy.put(Gradient.class, handleGradient(token));
            } else if (token.startsWith(CLOSE_TAG + GRADIENT)) {
                fancy.remove(Gradient.class);
            }
            // invalid tag
            else {
                if (current != null) {
                    parent.append(current);
                }
                current = TextComponent.of(TAG_START + token + TAG_END);
                current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy);
            }

            if (current != null) {
                parent.append(current);
            }
        }

        // handle last message part
        if (richMessage.length() > lastEnd) {
            String msg = richMessage.substring(lastEnd);
            // append message
            Component current = TextComponent.of(msg);

            // set everything that is not closed yet
            current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy);

            parent.append(current);
        }

        // optimization, ignore empty parent
        TextComponent comp = parent.build();
        if (comp.content().equals("") && comp.children().size() == 1) {
            return comp.children().get(0);
        } else {
            return comp;
        }
    }

    @Nonnull
    private static Component applyFormatting(@Nonnull Deque<ClickEvent> clickEvents,
                                             @Nonnull Deque<HoverEvent<?>> hoverEvents,
                                             @Nonnull Deque<TextColor> colors,
                                             @Nonnull Deque<String> insertions,
                                             @Nonnull EnumSet<HelperTextDecoration> decorations,
                                             @Nonnull Component current,
                                             @Nonnull Map<Class<? extends Fancy>, Fancy> fancy) {
        // set everything that is not closed yet
        if (!clickEvents.isEmpty()) {
            current = current.clickEvent(clickEvents.peek());
        }
        if (!hoverEvents.isEmpty()) {
            current = current.hoverEvent(hoverEvents.peek());
        }
        if (!colors.isEmpty()) {
            current = current.color(colors.peek());
        }
        if (!decorations.isEmpty()) {
            // no lambda because builder isn't effective final :/
            for (HelperTextDecoration decor : decorations) {
                current = decor.apply(current);
            }
        }
        if (!insertions.isEmpty()) {
            current = current.insertion(insertions.peek());
        }

        if (current instanceof TextComponent && fancy.size() != 0) {
            Component parent = null;
            TextComponent bigComponent = (TextComponent) current;

            Fancy next = fancy.entrySet().iterator().next().getValue();
            next.init(bigComponent.content().length());
            // split into multiple components
            for (int i = 0; i < bigComponent.content().length(); i++) {
                Component smallComponent = TextComponent.of(bigComponent.content().charAt(i));
                // apply formatting
                smallComponent = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, smallComponent, empty);
                smallComponent = next.apply(smallComponent);
                // append
                if (parent == null) {
                    parent = smallComponent;
                } else {
                    parent = parent.append(smallComponent);
                }
            }
            if (parent != null) {
                current = parent;
            }
        }

        return current;
    }

    @Nonnull
    private static Rainbow handleRainbow(String token) {
        if (token.contains(SEPARATOR)) {
            String phase = token.substring(token.indexOf(SEPARATOR) + 1);
            try {
                return new Rainbow(Integer.parseInt(phase));
            } catch (NumberFormatException ex) {
                throw new ParseException("Can't parse rainbow phase (not a int) " + token);
            }
        }
        return new Rainbow();
    }

    @Nonnull
    private static Gradient handleGradient(String token) {
        if (token.contains(SEPARATOR)) {
            String[] split = token.split(":");
            if (split.length == 3) {
                TextColor c1 = parseColor(split[1]).orElseThrow(() -> new ParseException("Can't parse gradient phase (not a color 1) " + token));
                TextColor c2 = parseColor(split[2]).orElseThrow(() -> new ParseException("Can't parse gradient phase (not a color 2) " + token));
                return new Gradient(c1, c2);
            } else {
                throw new ParseException("Can't parse gradient (wrong args) " + token);
            }
        }
        return new Gradient();
    }

    @Nonnull
    private static String handleInsertion(@Nonnull String token) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse insertion (too few args) " + token);
        }
        return token.replace(args[0] + SEPARATOR, "");
    }

    @Nonnull
    private static Component handleTranslatable(@Nonnull String token, String inner) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse translatable (too few args) " + token);
        }
        if (inner == null) {
            return TranslatableComponent.of(args[1]);
        } else {
            List<Component> inners = new ArrayList<>();
            String toSplit = token.replace(args[0] + ":" + args[1] + ":", "");
            String[] split = dumSplitPattern.split(cleanInner(toSplit));
            for (String someInner : split) {
                inners.add(parseFormat(someInner));
            }
            return TranslatableComponent.of(args[1], inners);
        }
    }

    @Nonnull
    private static KeybindComponent handleKeybind(@Nonnull String token) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse keybind (too few args) " + token);
        }
        return KeybindComponent.of(args[1]);
    }

    @Nonnull
    private static ClickEvent handleClick(@Nonnull String token, @Nonnull String inner) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse click action (too few args) " + token);
        }
        ClickEvent.Action action = Optional.ofNullable(ClickEvent.Action.NAMES.value(args[1].toLowerCase(Locale.ROOT)))
                .orElseThrow(() -> new ParseException("Can't parse click action (invalid action) " + token));
        return ClickEvent.of(action, token.replace(CLICK + SEPARATOR + args[1] + SEPARATOR, ""));
    }

    @Nonnull
    private static HoverEvent<?> handleHover(@Nonnull String token, @Nonnull String inner) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse hover action (too few args) " + token);
        }
        inner = cleanInner(inner);
        HoverEvent.Action action = Optional.ofNullable(HoverEvent.Action.NAMES.value(args[1].toLowerCase(Locale.ROOT)))
                .orElseThrow(() -> new ParseException("Can't parse hover action (invalid action) " + token));
        return HoverEvent.of(action, parseFormat(inner));
    }

    @Nonnull
    private static Optional<TextColor> resolveColor(@Nonnull String token) {
        return Optional.ofNullable(NamedTextColor.NAMES.value(token.toLowerCase(Locale.ROOT)));
    }

    @Nonnull
    private static Optional<TextColor> resolveColorNew(@Nonnull String token) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse color (too few args) " + token);
        }

        return parseColor(args[1]);
    }

    @Nonnull
    private static Optional<TextColor> parseColor(String color) {
        if (color.charAt(0) == '#') {
            return Optional.ofNullable(TextColor.fromHexString(color));
        } else {
            return Optional.ofNullable(NamedTextColor.NAMES.value(color.toLowerCase(Locale.ROOT)));
        }
    }

    @Nonnull
    private static Optional<HelperTextDecoration> resolveDecoration(@Nonnull String token) {
        try {
            return Optional.of(HelperTextDecoration.valueOf(token.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    private static String cleanInner(String inner) {
        return inner.substring(1).substring(0, inner.length() - 2); // cut off first and last "/'
    }

}
