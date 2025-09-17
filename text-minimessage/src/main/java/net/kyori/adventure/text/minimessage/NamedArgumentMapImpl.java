package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.NamedArgumentMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

final class NamedArgumentMapImpl<T extends Tag.Argument> implements NamedArgumentMap {
  private final Context context;
  final Map<String, T> args;

  public NamedArgumentMapImpl(final Context context, final Map<String, T> args) {
    this.context = context;
    this.args = args;
  }

  @Override
  public boolean isPresent(final @NotNull String name) {
    requireNonNull(name, "name");
    return this.args.containsKey(name);
  }

  @Override
  public int size() {
    return this.args.size();
  }

  @Override
  public Tag.@Nullable Argument get(final @NotNull String name) {
    requireNonNull(name, "name");
    return this.args.get(name);
  }

  @Override
  public Tag.@NotNull Argument getOrThrow(@NotNull String name, final @NotNull String errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    final Tag.Argument arg = get(name);
    if (arg == null) {
      throw this.context.newException(errorMessage);
    }
    return arg;
  }

  @Override
  public Tag.@NotNull Argument getOrThrow(@NotNull String name, final @NotNull Supplier<String> errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    final Tag.Argument arg = get(name);
    if (arg == null) {
      throw this.context.newException(errorMessage.get());
    }
    return arg;
  }
}
