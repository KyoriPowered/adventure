package net.kyori.adventure.text.minimessage;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ParseException extends RuntimeException {

  private static final long serialVersionUID = 42L;

  public ParseException(@NonNull String message) {
    super(message);
  }
}
