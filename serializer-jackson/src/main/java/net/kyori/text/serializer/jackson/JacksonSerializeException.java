package net.kyori.text.serializer.jackson;

public class JacksonSerializeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JacksonSerializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
