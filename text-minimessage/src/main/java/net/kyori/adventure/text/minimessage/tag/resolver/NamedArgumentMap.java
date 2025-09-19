package net.kyori.adventure.text.minimessage.tag.resolver;

import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A map of named {@link Tag} arguments.
 */
@ApiStatus.NonExtendable
public interface NamedArgumentMap {

  /**
   * {@return whether an argument by this name is present}
   */
  boolean isPresent(@NotNull String name);

  /**
   * {@return the number of arguments present}
   */
  int size();

  /**
   * Get an argument by its name, returning {@code null} if none was found.
   *
   * @return the argument
   */
  Tag.@Nullable Argument get(@NotNull String name);

  /**
   * Get an argument by its name, throwing an exception if no argument with that name was present.
   *
   * @param errorMessage the error to throw if an argument with that name is not present
   * @return the argument
   */
  Tag.@NotNull Argument getOrThrow(@NotNull String name, @NotNull String errorMessage);

  /**
   * Get an argument by its name, throwing an exception if no argument with that name was present.
   *
   * @param errorMessage the error to throw if an argument with that name is not present
   * @return the argument
   */
  Tag.@NotNull Argument getOrThrow(@NotNull String name, @NotNull Supplier<String> errorMessage);
}
