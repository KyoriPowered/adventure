package net.kyori.text.serializer.jackson;

public class JacksonDeserializeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JacksonDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
