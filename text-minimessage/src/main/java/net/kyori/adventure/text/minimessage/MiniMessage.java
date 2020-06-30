package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * MiniMessage is a textual representation of components. This class allows you to serialize and deserialize them, strip
 * or escape them, and even supports a markdown like format.
 */
public interface MiniMessage extends ComponentSerializer<Component, Component, String>, Buildable<MiniMessage, MiniMessage.Builder> {

  /**
   * Gets a simple instance without markdown support
   *
   * @return a simple instance
   */
  static @NonNull MiniMessage instance() {
    return MiniMessageImpl.INSTANCE;
  }

  /**
   * Gets an instance with markdown support
   *
   * @return a instance of markdown support
   */
  static @NonNull MiniMessage withMarkDown() {
    return MiniMessageImpl.MARKDOWN;
  }

  /**
   * Escapes all tokens in the input message, so that they are ignored in deserialization. Useful for untrusted input.
   *
   * @param input the input message, with tokens
   * @return the output, with escaped tokens
   */
  @NonNull String escapeTokens(@NonNull String input);

  /**
   * Removes all tokens in the input message. Useful for untrusted input.
   *
   * @param input the input message, with tokens
   * @return the output, without tokens
   */
  @NonNull String stripTokens(@NonNull String input);

  /**
   * Parses a string into an component.
   *
   * @param input the input string
   * @return the output component
   */
  default Component parse(@NonNull String input) {
    return deserialize(input);
  }

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   */
  @NonNull Component parse(@NonNull String input, final @NonNull String... placeholders);

  /**
   * Parses a string into an component, allows passing placeholders in key value pairs
   *
   * @param input the input string
   * @param placeholders the placeholders
   * @return the output component
   */
  @NonNull Component parse(@NonNull String input, final @NonNull Map<String, String> placeholders);

  /**
   * Creates a new {@link MiniMessage.Builder}.
   *
   * @return a builder
   */
  static Builder builder() {
    return new MiniMessageImpl.BuilderImpl();
  }

  /**
   * A builder for {@link MiniMessage}.
   */
  interface Builder extends Buildable.AbstractBuilder<MiniMessage> {

    /**
     * Adds markdown support
     *
     * @return this builder
     */
    @NonNull Builder markdown();

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     */
    @Override
    @NonNull MiniMessage build();
  }
}
