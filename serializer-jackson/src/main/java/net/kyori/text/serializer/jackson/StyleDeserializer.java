package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.kyori.text.Component;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextDecoration;

import java.io.IOException;

public class StyleDeserializer extends JsonDeserializer<Style> {
    static final StyleDeserializer INSTANCE = new StyleDeserializer();

    private static final TextDecoration[] DECORATIONS = TextDecoration.values();
    static final String COLOR = "color";
    static final String INSERTION = "insertion";
    static final String CLICK_EVENT = "clickEvent";
    static final String CLICK_EVENT_ACTION = "action";
    static final String CLICK_EVENT_VALUE = "value";
    static final String HOVER_EVENT = "hoverEvent";
    static final String HOVER_EVENT_ACTION = "action";
    static final String HOVER_EVENT_VALUE = "value";

    @Override
    public Style deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final Style.Builder style = Style.builder();
        JsonNode json = p.readValueAsTree();

        if(json.has(COLOR)) {
            final TextColorWrapper color = json.get(COLOR).traverse(p.getCodec()).readValueAs(TextColorWrapper.class);
            if(color.color != null) {
                style.color(color.color);
            } else if(color.decoration != null) {
                style.decoration(color.decoration, true);
            }
        }

        for(final TextDecoration decoration : DECORATIONS) {
            final String name = TextDecoration.NAMES.name(decoration);
            if(json.has(name)) {
                style.decoration(decoration, json.get(name).asBoolean());
            }
        }

        if(json.has(INSERTION)) {
            style.insertion(json.get(INSERTION).asText());
        }

        if(json.has(CLICK_EVENT)) {
            final JsonNode clickEvent = json.get(CLICK_EVENT);
            if(clickEvent != null) {
                final /* @Nullable */ JsonNode rawAction = clickEvent.get(CLICK_EVENT_ACTION);
                final ClickEvent./*@Nullable*/ Action action = rawAction == null ? null : rawAction.traverse(p.getCodec()).readValueAs(ClickEvent.Action.class);
                if(action != null && action.readable()) {
                    final /* @Nullable */ JsonNode rawValue = clickEvent.get(CLICK_EVENT_VALUE);
                    final /* @Nullable */ String value = rawValue == null ? null : rawValue.asText();
                    if(value != null) {
                        style.clickEvent(ClickEvent.of(action, value));
                    }
                }
            }
        }

        if(json.has(HOVER_EVENT)) {
            final JsonNode hoverEvent = json.get(HOVER_EVENT);
            if(hoverEvent != null) {
                final /* @Nullable */ JsonNode rawAction = hoverEvent.get(HOVER_EVENT_ACTION);
                final HoverEvent./*@Nullable*/ Action action = rawAction == null ? null : rawAction.traverse(p.getCodec()).readValueAs(HoverEvent.Action.class);
                if(action != null && action.readable()) {
                    final /* @Nullable */ JsonNode rawValue = hoverEvent.get(HOVER_EVENT_VALUE);
                    final /* @Nullable */ Component value = rawValue == null ? null : rawValue.traverse(p.getCodec()).readValueAs(Component.class);
                    if(value != null) {
                        style.hoverEvent(HoverEvent.of(action, value));
                    }
                }
            }
        }

        return style.build();
    }
}
