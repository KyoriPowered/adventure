package net.kyori.text.serializer.jackson;

public class JacksonSerializeException extends RuntimeException {
    public JacksonSerializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
