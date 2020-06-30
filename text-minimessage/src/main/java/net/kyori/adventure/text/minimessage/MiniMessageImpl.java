package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/* package */ class MiniMessageImpl implements MiniMessage {

  /* package */ static final MiniMessage INSTANCE = new MiniMessageImpl(false);
  /* package */ static final MiniMessage MARKDOWN = new MiniMessageImpl(true);

  private final boolean markdown;

  MiniMessageImpl(boolean markdown) {
    this.markdown = markdown;
  }

  @Override
  public @NonNull Component deserialize(@NonNull String input) {
    if (markdown) {
      input = MiniMarkdownParser.parse(input);
    }
    return MiniMessageParser.parseFormat(input);
  }


  @Override
  public @NonNull String serialize(@NonNull Component component) {
    return MiniMessageSerializer.serialize(component);
  }

  @Override
  public @NonNull Component parse(@NonNull String input, @NonNull String... placeholders) {
    if (markdown) {
      input = MiniMarkdownParser.parse(input);
    }
    return MiniMessageParser.parseFormat(input, placeholders);
  }

  @Override
  public @NonNull Component parse(@NonNull String input, @NonNull Map<String, String> placeholders) {
    if (markdown) {
      input = MiniMarkdownParser.parse(input);
    }
    return MiniMessageParser.parseFormat(input, placeholders);
  }

  @Override
  public @NonNull String escapeTokens(@NonNull String input) {
    return MiniMessageParser.escapeTokens(input);
  }

  @Override
  public @NonNull String stripTokens(@NonNull String input) {
    if (markdown) {
      input = MiniMarkdownParser.stripMarkdown(input);
    }
    return MiniMessageParser.stripTokens(input);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  /* package */ static final class BuilderImpl implements Builder {
    private boolean markdown = false;

    BuilderImpl() {
    }

    BuilderImpl(final MiniMessageImpl serializer) {
      this.markdown = serializer.markdown;
    }


    @Override
    public @NonNull Builder markdown() {
      this.markdown = true;
      return this;
    }

    @Override
    public @NonNull MiniMessage build() {
      return this.markdown ? MARKDOWN : INSTANCE;
    }
  }
}
