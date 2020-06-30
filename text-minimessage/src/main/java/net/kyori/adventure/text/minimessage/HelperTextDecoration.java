package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.UnaryOperator;

enum HelperTextDecoration {
  BOLD(b -> b.decoration(TextDecoration.BOLD, true)),
  ITALIC(b -> b.decoration(TextDecoration.ITALIC, true)),
  UNDERLINED(b -> b.decoration(TextDecoration.UNDERLINED, true)),
  STRIKETHROUGH(b -> b.decoration(TextDecoration.STRIKETHROUGH, true)),
  OBFUSCATED(b -> b.decoration(TextDecoration.OBFUSCATED, true));

  private final UnaryOperator<Component> builder;

  HelperTextDecoration(@NonNull UnaryOperator<Component> builder) {
    this.builder = builder;
  }

  @NonNull
  public Component apply(@NonNull Component comp) {
    return builder.apply(comp);
  }
}
