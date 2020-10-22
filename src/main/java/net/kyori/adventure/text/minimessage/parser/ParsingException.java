package net.kyori.adventure.text.minimessage.parser;

public class ParsingException extends RuntimeException {

    private final int column;
    private String msg;

    public ParsingException(String msg, int column) {
        this.msg = msg;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
