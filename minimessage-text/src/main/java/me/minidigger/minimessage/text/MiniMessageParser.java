package me.minidigger.minimessage.text;

import net.kyori.text.Component;
import net.kyori.text.KeybindComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TextComponent.Builder;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import static me.minidigger.minimessage.text.Constants.CLICK;
import static me.minidigger.minimessage.text.Constants.CLOSE_TAG;
import static me.minidigger.minimessage.text.Constants.HOVER;
import static me.minidigger.minimessage.text.Constants.INSERTION;
import static me.minidigger.minimessage.text.Constants.KEYBIND;
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
    // https://regex101.com/r/8VZ7uA/6
    private static final Pattern pattern = Pattern.compile("((?<start><)(?<token>([^<>]+)|([^<>]+[\"'](?<inner>[^\"']+)[\"']))(?<end>>))+?");

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
        Builder parent = TextComponent.builder("");

        ArrayDeque<ClickEvent> clickEvents = new ArrayDeque<>();
        ArrayDeque<HoverEvent> hoverEvents = new ArrayDeque<>();
        ArrayDeque<TextColor> colors = new ArrayDeque<>();
        ArrayDeque<String> insertions = new ArrayDeque<>();
        EnumSet<HelperTextDecoration> decorations = EnumSet.noneOf(HelperTextDecoration.class);

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
                current = applyFormatting(clickEvents, hoverEvents, colors, insertions,decorations, current);

            }

            String token = matcher.group(TOKEN);
            String inner = matcher.group(INNER);

            Optional<HelperTextDecoration> deco;
            Optional<TextColor> color;

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
            // keybind
            else if (token.startsWith(KEYBIND + SEPARATOR)) {
                if (current != null) {
                    parent.append(current);
                }
                current = handleKeybind(token);
                current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current);
            }
            // translatable
            else if (token.startsWith(TRANSLATABLE + SEPARATOR)) {
                if (current != null) {
                    parent.append(current);
                }
                current = handleTranslatable(token);
                current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current);
            }
            // insertion
            else if (token.startsWith(INSERTION + SEPARATOR)) {
                insertions.push(handleInsertion(token));
            } else if (token.startsWith(CLOSE_TAG + INSERTION)) {
                insertions.pop();
            }
            // invalid tag
            else {
                if (current != null) {
                    parent.append(current);
                }
                current = TextComponent.of(TAG_START + token + TAG_END);
                current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current);
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
            current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current);

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
                                             @Nonnull Deque<HoverEvent> hoverEvents,
                                             @Nonnull Deque<TextColor> colors,
                                             @Nonnull Deque<String> insertions,
                                             @Nonnull EnumSet<HelperTextDecoration> decorations,
                                             @Nonnull Component current) {
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
        return current;
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
    private static Component handleTranslatable(@Nonnull String token) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse translatable (too few args) " + token);
        }
        return TranslatableComponent.of(args[1]);
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
        ClickEvent.Action action = ClickEvent.Action.NAMES.value(args[1].toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ParseException("Can't parse click action (invalid action) " + token));
        return ClickEvent.of(action, token.replace(CLICK + SEPARATOR + args[1] + SEPARATOR, ""));
    }

    @Nonnull
    private static HoverEvent handleHover(@Nonnull String token, @Nonnull String inner) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse hover action (too few args) " + token);
        }
        HoverEvent.Action action = HoverEvent.Action.NAMES.value(args[1].toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ParseException("Can't parse hover action (invalid action) " + token));
        return HoverEvent.of(action, parseFormat(inner));
    }

    @Nonnull
    private static Optional<TextColor> resolveColor(@Nonnull String token) {
        return TextColor.NAMES.value(token.toLowerCase(Locale.ROOT));
    }

    @Nonnull
    private static Optional<HelperTextDecoration> resolveDecoration(@Nonnull String token) {
        try {
            return Optional.of(HelperTextDecoration.valueOf(token.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    enum HelperTextDecoration {
        BOLD(b -> b.decoration(TextDecoration.BOLD, true)),
        ITALIC(b -> b.decoration(TextDecoration.ITALIC, true)),
        UNDERLINED(b -> b.decoration(TextDecoration.UNDERLINED, true)),
        STRIKETHROUGH(b -> b.decoration(TextDecoration.STRIKETHROUGH, true)),
        OBFUSCATED(b -> b.decoration(TextDecoration.OBFUSCATED, true));

        private final UnaryOperator<Component> builder;

        HelperTextDecoration(@Nonnull UnaryOperator<Component> builder) {
            this.builder = builder;
        }

        @Nonnull
        public Component apply(@Nonnull Component comp) {
            return builder.apply(comp);
        }
    }

    static class ParseException extends RuntimeException {
        public ParseException(@Nonnull String message) {
            super(message);
        }
    }
}
