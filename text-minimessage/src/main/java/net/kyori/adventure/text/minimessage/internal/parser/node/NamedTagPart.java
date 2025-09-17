package net.kyori.adventure.text.minimessage.internal.parser.node;

import net.kyori.adventure.text.minimessage.internal.parser.Token;
import net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;

public final class NamedTagPart extends TagPart implements Tag.NamedArgument {
  private final String name;

  public NamedTagPart(
    final @NotNull String sourceMessage,
    final @NotNull Token token,
    final TokenParser.@NotNull TagProvider tagResolver,
    final String name
  ) {
    super(sourceMessage, token, tagResolver);
    this.name = name;
  }

  @Override
  public @NotNull String name() {
    return this.name;
  }
}
