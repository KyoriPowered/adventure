package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;

public class JacksonComponentSerializer implements ComponentSerializer<Component, Component, String> {
    public static final JacksonComponentSerializer INSTANCE = new JacksonComponentSerializer();
    static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new KyoriTextModule());

    @NonNull
    @Override
    public Component deserialize(@NonNull String input) throws JacksonDeserializeException {
        try {
            return MAPPER.readValue(input, Component.class);
        } catch (IOException e) {
            throw new JacksonDeserializeException("Can't deserialize component", e);
        }
    }

    @NonNull
    @Override
    public String serialize(@NonNull Component component) {
        try {
            return MAPPER.writeValueAsString(component);
        } catch (JsonProcessingException e) {
            throw new JacksonSerializeException("Can't serialize component", e);
        }
    }
}
