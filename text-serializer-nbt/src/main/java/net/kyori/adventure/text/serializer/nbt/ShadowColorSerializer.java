package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.text.format.ShadowColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ShadowColorSerializer {

  private ShadowColorSerializer() {
  }

  static @Nullable BinaryTag serialize(@NotNull ShadowColor color, @NotNull NBTComponentSerializerImpl serializer) {
    NBTSerializerOptions.ShadowColorEmitMode emitMode = serializer.flags().value(NBTSerializerOptions.SHADOW_COLOR_MODE);
    switch (emitMode) {
      case NONE:
        return null;
      case EMIT_INTEGER:
        return IntBinaryTag.intBinaryTag(color.value());
      case EMIT_ARRAY:
        ListBinaryTag.Builder<FloatBinaryTag> shadowColorTagBuilder = ListBinaryTag.builder(BinaryTagTypes.FLOAT);
        addShadowColorComponent(shadowColorTagBuilder, color.red());
        addShadowColorComponent(shadowColorTagBuilder, color.green());
        addShadowColorComponent(shadowColorTagBuilder, color.blue());
        addShadowColorComponent(shadowColorTagBuilder, color.alpha());
        return shadowColorTagBuilder.build();
      default:
        // Never called, but needed for proper compilation
        throw new IllegalArgumentException("Unknown shadow color emit mode: " + emitMode);
    }
  }

  static @NotNull ShadowColor deserialize(@NotNull BinaryTag tag) {
    if (tag instanceof IntBinaryTag) {
      IntBinaryTag castShadowColorTag = (IntBinaryTag) tag;
      return ShadowColor.shadowColor(castShadowColorTag.value());
    } else if (tag instanceof ListBinaryTag) {
      ListBinaryTag castShadowColorTag = (ListBinaryTag) tag;
      return ShadowColor.shadowColor(
        getShadowColorComponent(castShadowColorTag, 0),
        getShadowColorComponent(castShadowColorTag, 1),
        getShadowColorComponent(castShadowColorTag, 2),
        getShadowColorComponent(castShadowColorTag, 3)
      );
    } else {
      throw new IllegalArgumentException("The binary tag representing the shadow color is of an invalid type");
    }
  }

  private static int getShadowColorComponent(@NotNull ListBinaryTag tag, int index) {
    return (int) (tag.getFloat(index) * 0xff);
  }

  private static void addShadowColorComponent(@NotNull ListBinaryTag.Builder<FloatBinaryTag> builder, int element) {
    builder.add(FloatBinaryTag.floatBinaryTag((float) element / 0xff));
  }
}
