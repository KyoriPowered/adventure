package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.util.NameMap;

import java.io.IOException;

public class NameMapSerializer<T> extends JsonSerializer<T> {
    static final NameMapSerializer<ClickEvent.Action> CLICK = new NameMapSerializer<>("click action", ClickEvent.Action.NAMES, ClickEvent.Action.class);
    static final NameMapSerializer<HoverEvent.Action> HOVER = new NameMapSerializer<>("hover action", HoverEvent.Action.NAMES, HoverEvent.Action.class);
    static final NameMapSerializer<TextColor> COLOR = new NameMapSerializer<>("text color", TextColor.NAMES, TextColor.class);
    static final NameMapSerializer<TextDecoration> DECORATION = new NameMapSerializer<>("text decoration", TextDecoration.NAMES, TextDecoration.class);


    private final String name;
    private final NameMap<T> map;
    private final Class<T> type;

    public NameMapSerializer(String name, NameMap<T> map, Class<T> type) {
        this.name = name;
        this.map = map;
        this.type = type;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(map.name(value));
    }
}
