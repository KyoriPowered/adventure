package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.util.NameMap;

import java.io.IOException;
import java.util.Optional;

public class NameMapDeserializer<T> extends JsonDeserializer<T> {
    static final NameMapDeserializer<ClickEvent.Action> CLICK = new NameMapDeserializer<>("click action", ClickEvent.Action.NAMES, ClickEvent.Action.class);
    static final NameMapDeserializer<HoverEvent.Action> HOVER = new NameMapDeserializer<>("hover action", HoverEvent.Action.NAMES, HoverEvent.Action.class);
    static final NameMapDeserializer<TextColor> COLOR = new NameMapDeserializer<>("text color", TextColor.NAMES, TextColor.class);
    static final NameMapDeserializer<TextDecoration> DECORATION = new NameMapDeserializer<>("text decoration", TextDecoration.NAMES, TextDecoration.class);

    private final String name;
    private final NameMap<T> map;
    private final Class<T> type;

    public NameMapDeserializer(String name, NameMap<T> map, Class<T> type) {
        this.name = name;
        this.map = map;
        this.type = type;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String name = p.getValueAsString();
        Optional<T> ret = map.get(name);
        if (ret.isPresent()) {
            return ret.get();
        }

        return ctxt.reportInputMismatch(type, "invalid " + this.name + ":  " + name);
    }
}
