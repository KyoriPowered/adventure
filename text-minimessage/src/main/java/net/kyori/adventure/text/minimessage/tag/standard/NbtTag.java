/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.minimessage.tag.standard;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

/**
 * One tag for all NBT components.
 *
 * @since 4.13.0
 */
final class NbtTag {
  private static final String NBT = "nbt";
  private static final String DATA = "data";

  private static final String BLOCK = "block";
  private static final String ENTITY = "entity";
  private static final String STORAGE = "storage";
  private static final String INTERPRET = "interpret";

  static final TagResolver RESOLVER = SerializableResolver.claimingComponent(
    StandardTags.names(NBT, DATA),
    NbtTag::resolve,
    NbtTag::emit
  );

  private NbtTag() {
  }

  // syntax intended to match the /data command in vanilla MC
  static Tag resolve(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final String type = args.popOr("a type of block, entity, or storage is required").lowerValue();
    final NBTComponentBuilder<?, ?> builder;
    if (BLOCK.equals(type)) {
      final String pos = args.popOr("A position is required").value();
      try {
        builder = Component.blockNBT()
          .pos(BlockNBTComponent.Pos.fromString(pos));
      } catch (final IllegalArgumentException ex) {
        throw ctx.newException(ex.getMessage(), args);
      }
    } else if (ENTITY.equals(type)) {
      builder = Component.entityNBT()
        .selector(args.popOr("A selector is required").value());
    } else if (STORAGE.equals(type)) {
      builder = Component.storageNBT()
        .storage(Key.key(args.popOr("A storage key is required").value()));
    } else {
      throw ctx.newException("Unknown nbt tag type '" + type + "'", args);
    }

    builder.nbtPath(args.popOr("An NBT path is required").value());

    if (args.hasNext()) {
      final String popped = args.pop().value();

      if (INTERPRET.equalsIgnoreCase(popped)) {
        builder.interpret(true);
      } else {
        builder.separator(ctx.deserialize(popped));

        if (args.hasNext() && args.pop().value().equalsIgnoreCase(INTERPRET)) {
          builder.interpret(true);
        }
      }
    }

    return Tag.inserting(builder.build());
  }

  static @Nullable Emitable emit(final Component comp) {
    final String type;
    final String id;
    if (comp instanceof BlockNBTComponent) {
      type = BLOCK;
      id = ((BlockNBTComponent) comp).pos().asString();
    } else if (comp instanceof EntityNBTComponent) {
      type = ENTITY;
      id = ((EntityNBTComponent) comp).selector();
    } else if (comp instanceof StorageNBTComponent) {
      type = STORAGE;
      id = ((StorageNBTComponent) comp).storage().asString();
    } else {
      return null;
    }

    return out -> {
      final NBTComponent<?, ?> nbt = (NBTComponent<?, ?>) comp;
      out.tag(NBT)
        .argument(type)
        .argument(id)
        .argument(nbt.nbtPath());

      if (nbt.separator() != null) {
        out.argument(nbt.separator());
      }

      if (nbt.interpret()) {
        out.argument(INTERPRET);
      }
    };
  }
}
