package net.kyori.adventure.chat;

import java.security.SecureRandom;
import java.time.Instant;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

final class UnsignedSignedMessageImpl implements SignedMessage {
  static final SecureRandom RANDOM = new SecureRandom();

  private final Instant instant;
  private final long salt;
  private final Component message;
  private final String plain;
  private final Identity identity;

  UnsignedSignedMessageImpl(final Component message, final String plain, final Identity identity) {
    this.instant = Instant.now();
    this.salt = RANDOM.nextLong();
    this.message = message;
    this.plain = plain;
    this.identity = identity;
  }

  @Override
  public @NotNull Instant timeStamp() {
    return this.instant;
  }

  @Override
  public long salt() {
    return this.salt;
  }

  @Override
  public Signature signature() {
    return null;
  }

  @Override
  public @NotNull Component message() {
    return this.message;
  }

  @Override
  public @NotNull String plain() {
    return this.plain;
  }

  @Override
  public @NotNull Identity identity() {
    return this.identity;
  }
}
