package me.minidigger.minimessage.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import static me.minidigger.minimessage.bungee.Constants.CLICK;
import static me.minidigger.minimessage.bungee.Constants.CLOSE_TAG;
import static me.minidigger.minimessage.bungee.Constants.HOVER;
import static me.minidigger.minimessage.bungee.Constants.SEPARATOR;
import static me.minidigger.minimessage.bungee.Constants.TAG_END;
import static me.minidigger.minimessage.bungee.Constants.TAG_START;

public class MiniMessageParser {

    // regex group names
    private static final String START = "start";
    private static final String TOKEN = "token";
    private static final String INNER = "inner";
    private static final String END = "end";
    // https://regex101.com/r/8VZ7uA/5
    private static final Pattern pattern = Pattern.compile("((?<start><)(?<token>([^<>]+)|([^<>]+\"(?<inner>[^\"]+)\"))(?<end>>))+?");

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
    public static BaseComponent[] parseFormat(@Nonnull String richMessage, @Nonnull String... placeholders) {
        return parseFormat(handlePlaceholders(richMessage, placeholders));
    }

    @Nonnull
    public static BaseComponent[] parseFormat(@Nonnull String richMessage, @Nonnull Map<String, String> placeholders) {
        return parseFormat(handlePlaceholders(richMessage, placeholders));
    }

    @Nonnull
    public static BaseComponent[] parseFormat(@Nonnull String richMessage) {
        ComponentBuilder builder = null;

        Deque<ClickEvent> clickEvents = new ArrayDeque<>();
        Deque<HoverEvent> hoverEvents = new ArrayDeque<>();
        Deque<ChatColor> colors = new ArrayDeque<>();
        EnumSet<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);

        Matcher matcher = pattern.matcher(richMessage);
        int lastEnd = 0;
        while (matcher.find()) {
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
                if (builder == null) {
                    builder = new ComponentBuilder(msg);
                } else {
                    builder.append(msg, ComponentBuilder.FormatRetention.NONE);
                }

                // set everything that is not closed yet
                if (!clickEvents.isEmpty()) {
                    builder.event(clickEvents.peek());
                }
                if (!hoverEvents.isEmpty()) {
                    builder.event(hoverEvents.peek());
                }
                if (!colors.isEmpty()) {
                    builder.color(colors.peek());
                }
                if (!decorations.isEmpty()) {
                    // no lambda because builder isn't effective final :/
                    for (TextDecoration decor : decorations) {
                        decor.apply(builder);
                    }
                }
            }

            String token = matcher.group(TOKEN);
            String inner = matcher.group(INNER);

            Optional<TextDecoration> deco;
            Optional<ChatColor> color;

            // click
            if (token.startsWith(CLICK + SEPARATOR)) {
                clickEvents.push(handleClick(token, inner));
            } else if (token.equals(CLOSE_TAG + CLICK)) {
                clickEvents.pop();
            }
            // hover
            else if (token.startsWith(HOVER + SEPARATOR)) {
                hoverEvents.push(handleHover(token, inner));
            } else if (token.equals(CLOSE_TAG + HOVER)) {
                hoverEvents.pop();
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
                colors.pop();
            } else {
                // invalid tag
                if (builder == null) {
                    builder = new ComponentBuilder(TAG_START + token + TAG_END);
                } else {
                    builder.append(TAG_START + token + TAG_END, ComponentBuilder.FormatRetention.NONE);
                }
            }
        }

        // handle last message part
        if (richMessage.length() > lastEnd) {
            String msg = richMessage.substring(lastEnd);
            // append message
            if (builder == null) {
                builder = new ComponentBuilder(msg);
            } else {
                builder.append(msg, ComponentBuilder.FormatRetention.NONE);
            }

            // set everything that is not closed yet
            if (!clickEvents.isEmpty()) {
                builder.event(clickEvents.peek());
            }
            if (!hoverEvents.isEmpty()) {
                builder.event(hoverEvents.peek());
            }
            if (!colors.isEmpty()) {
                builder.color(colors.peek());
            }
            if (!decorations.isEmpty()) {
                // no lambda because builder isn't effective final :/
                for (TextDecoration decor : decorations) {
                    decor.apply(builder);
                }
            }
        }

        if (builder == null) {
            // lets just return an empty component
            builder = new ComponentBuilder("");
        }

        return builder.create();
    }

    @Nonnull
    private static ClickEvent handleClick(@Nonnull String token, @Nonnull String inner) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse click action (too few args) " + token);
        }
        ClickEvent.Action action = ClickEvent.Action.valueOf(args[1].toUpperCase(Locale.ROOT));
        return new ClickEvent(action, token.replace(CLICK + SEPARATOR + args[1] + SEPARATOR, ""));
    }

    @Nonnull
    private static HoverEvent handleHover(@Nonnull String token, @Nonnull String inner) {
        String[] args = token.split(SEPARATOR);
        if (args.length < 2) {
            throw new ParseException("Can't parse hover action (too few args) " + token);
        }
        HoverEvent.Action action = HoverEvent.Action.valueOf(args[1].toUpperCase(Locale.ROOT));
        return new HoverEvent(action, parseFormat(inner));
    }

    @Nonnull
    private static Optional<ChatColor> resolveColor(@Nonnull String token) {
        try {
            return Optional.of(ChatColor.valueOf(token.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    @Nonnull
    private static Optional<TextDecoration> resolveDecoration(@Nonnull String token) {
        try {
            return Optional.of(TextDecoration.valueOf(token.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    enum TextDecoration {
        BOLD(b -> b.bold(true)),
        ITALIC(b -> b.italic(true)),
        UNDERLINED(b -> b.underlined(true)),
        STRIKETHROUGH(b -> b.strikethrough(true)),
        OBFUSCATED(b -> b.obfuscated(true));

        private final Consumer<ComponentBuilder> builder;

        TextDecoration(@Nonnull Consumer<ComponentBuilder> builder) {
            this.builder = builder;
        }

        public void apply(@Nonnull ComponentBuilder comp) {
            builder.accept(comp);
        }
    }

    static class ParseException extends RuntimeException {
        public ParseException(@Nonnull String message) {
            super(message);
        }
    }
}
