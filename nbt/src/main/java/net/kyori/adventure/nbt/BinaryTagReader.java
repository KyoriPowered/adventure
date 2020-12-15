package net.kyori.adventure.nbt;

import java.io.DataInput;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A binary tag reader.
 *
 * @param <T> the tag type
 */
interface BinaryTagReader<T extends BinaryTag> {
  @NonNull T read(final @NonNull DataInput input) throws IOException;
}
