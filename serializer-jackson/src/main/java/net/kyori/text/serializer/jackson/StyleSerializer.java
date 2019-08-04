package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.io.IOException;

public class StyleSerializer extends JsonSerializer<Style> {
    static StyleSerializer INSTANCE = new StyleSerializer();

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
    public void serialize(Style value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        for(final TextDecoration decoration : DECORATIONS) {
            final TextDecoration.State state = value.decoration(decoration);
            if(state != TextDecoration.State.NOT_SET) {
                final String name = TextDecoration.NAMES.name(decoration);
                gen.writeBooleanField(name, state == TextDecoration.State.TRUE);
            }
        }

        final /* @Nullable */ TextColor color = value.color();
        if(color != null) {
            gen.writeObjectField(COLOR, color);
        }

        final /* @Nullable */ String insertion = value.insertion();
        if(insertion != null) {
            gen.writeObjectField(INSERTION, insertion);
        }

        final /* @Nullable */ ClickEvent clickEvent = value.clickEvent();
        if(clickEvent != null) {
            gen.writeObjectFieldStart(CLICK_EVENT);
            gen.writeObjectField(CLICK_EVENT_ACTION, clickEvent.action());
            gen.writeObjectField(CLICK_EVENT_VALUE, clickEvent.value());
            gen.writeEndObject();
        }

        final /* @Nullable */ HoverEvent hoverEvent = value.hoverEvent();
        if(hoverEvent != null) {
            gen.writeObjectFieldStart(HOVER_EVENT);
            gen.writeObjectField(HOVER_EVENT_ACTION, hoverEvent.action());
            gen.writeObjectField(HOVER_EVENT_VALUE, hoverEvent.value());
            gen.writeEndObject();
        }
    }
}
