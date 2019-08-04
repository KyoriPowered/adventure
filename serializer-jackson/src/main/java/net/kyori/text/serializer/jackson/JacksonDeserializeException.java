package net.kyori.text.serializer.jackson;

public class JacksonDeserializeException extends RuntimeException {
    public JacksonDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
