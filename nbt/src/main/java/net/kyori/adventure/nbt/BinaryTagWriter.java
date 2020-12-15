package net.kyori.adventure.nbt;

import java.io.DataOutput;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A binary tag writer.
 *
 * @param <T> the tag type
 */
interface BinaryTagWriter<T extends BinaryTag> {
  void write(final @NonNull T tag, final @NonNull DataOutput output) throws IOException;
}
