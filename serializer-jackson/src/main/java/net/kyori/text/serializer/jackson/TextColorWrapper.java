package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;

class TextColorWrapper {
    static final Deserializer DESERIALIZER = new Deserializer();

    final @Nullable TextColor color;
    final @Nullable TextDecoration decoration;
    final boolean reset;

    TextColorWrapper(final @Nullable TextColor color, final @Nullable TextDecoration decoration, final boolean reset) {
        this.color = color;
        this.decoration = decoration;
        this.reset = reset;
    }

    static class Deserializer extends JsonDeserializer<TextColorWrapper> {
        @Override
        public TextColorWrapper deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            final TextColor color = deserializeColor(p, ctxt);
            final TextDecoration decoration = color == null ? deserializeDecoration(p, ctxt) : null;
            final boolean reset = decoration == null && "reset".equals(p.getValueAsString());
            if(color == null && decoration == null && !reset) {
                return ctxt.reportInputMismatch(TextColorWrapper.class, "Don't know how to parse TextColorWrapper");
            }
            return new TextColorWrapper(color, decoration, reset);
        }
    }

    private static TextColor deserializeColor(JsonParser p, DeserializationContext ctxt) {
        try {
            return p.readValueAs(TextColor.class);
        } catch(final IOException e) {
            return null;
        }
    }

    private static TextDecoration deserializeDecoration(JsonParser p, DeserializationContext ctxt) {
        try {
            return p.readValueAs(TextDecoration.class);
        } catch(final IOException e) {
            return null;
        }
    }
}
