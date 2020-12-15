package net.kyori.adventure.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

class BinaryTagTypeImpl<T extends BinaryTag> extends BinaryTagType<T> {
  final Class<T> type;
  final byte id;
  private final BinaryTagReader<T> reader;
  private final @Nullable BinaryTagWriter<T> writer;

  BinaryTagTypeImpl(final Class<T> type, final byte id, final BinaryTagReader<T> reader, final @Nullable BinaryTagWriter<T> writer) {
    this.type = type;
    this.id = id;
    this.reader = reader;
    this.writer = writer;
  }

  @Override
  public final @NonNull T read(final @NonNull DataInput input) throws IOException {
    return this.reader.read(input);
  }

  @Override
  public final void write(final @NonNull T tag, final @NonNull DataOutput output) throws IOException {
    if(this.writer != null) this.writer.write(tag, output);
  }

  @Override
  public final byte id() {
    return this.id;
  }

  @Override
  boolean numeric() {
    return false;
  }

  @Override
  public String toString() {
    return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + "]";
  }

  static class Numeric<T extends BinaryTag> extends BinaryTagTypeImpl<T> {
    Numeric(final Class<T> type, final byte id, final BinaryTagReader<T> reader, final @Nullable BinaryTagWriter<T> writer) {
      super(type, id, reader, writer);
    }

    @Override
    boolean numeric() {
      return true;
    }

    @Override
    public String toString() {
      return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + " (numeric)]";
    }
  }
}
