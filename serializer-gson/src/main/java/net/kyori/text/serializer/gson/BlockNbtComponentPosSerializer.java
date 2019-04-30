/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.serializer.gson;

import com.google.gson.*;
import net.kyori.text.BlockNbtComponent;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockNbtComponentPosSerializer implements JsonDeserializer<BlockNbtComponent.Pos>, JsonSerializer<BlockNbtComponent.Pos> {
  public static final BlockNbtComponentPosSerializer INSTANCE = new BlockNbtComponentPosSerializer();
  private static final Pattern LOCAL_PATTERN = Pattern.compile("^\\^(\\d+(\\.\\d+)?) \\^(\\d+(\\.\\d+)?) \\^(\\d+(\\.\\d+)?)$");
  private static final Pattern WORLD_PATTERN = Pattern.compile("^(~?)(\\d+) (~?)(\\d+) (~?)(\\d+)$");

  @Override
  public BlockNbtComponent.Pos deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    final String string = json.getAsString();

    final Matcher localMatch = LOCAL_PATTERN.matcher(string);
    if(localMatch.matches()) {
      return BlockNbtComponent.LocalPos.of(
        Double.parseDouble(localMatch.group(1)),
        Double.parseDouble(localMatch.group(3)),
        Double.parseDouble(localMatch.group(5))
      );
    }

    final Matcher worldMatch = WORLD_PATTERN.matcher(string);
    if(worldMatch.matches()) {
      return BlockNbtComponent.WorldPos.of(
        deserializeCoordinate(worldMatch.group(1), worldMatch.group(2)),
        deserializeCoordinate(worldMatch.group(3), worldMatch.group(4)),
        deserializeCoordinate(worldMatch.group(5), worldMatch.group(6))
      );
    }

    throw new JsonParseException("Don't know how to turn " + string + " into a Position");
  }

  @Override
  public JsonElement serialize(final BlockNbtComponent.Pos src, final Type typeOfSrc, final JsonSerializationContext context) {
    if(src instanceof BlockNbtComponent.LocalPos) {
      final BlockNbtComponent.LocalPos local = (BlockNbtComponent.LocalPos) src;
      return new JsonPrimitive("^" + local.left() + ' ' + '^' + local.up() + ' ' + '^' + local.forwards());
    } else if(src instanceof BlockNbtComponent.WorldPos) {
      final BlockNbtComponent.WorldPos world = (BlockNbtComponent.WorldPos) src;
      return new JsonPrimitive(serializeCoordinate(world.x()) + ' ' + serializeCoordinate(world.y()) + ' ' + serializeCoordinate(world.z()));
    } else {
      throw new IllegalArgumentException("Don't know how to serialize " + src + " as a Position");
    }
  }

  private static BlockNbtComponent.WorldPos.Coordinate deserializeCoordinate(final String prefix, final String value) {
    final int i = Integer.parseInt(value);
    if(prefix.isEmpty()) {
      return BlockNbtComponent.WorldPos.Coordinate.absolute(i);
    } else if(prefix.equals("~")) {
      return BlockNbtComponent.WorldPos.Coordinate.relative(i);
    } else {
      throw new AssertionError(); // regex does not allow any other value for prefix.
    }
  }

  private static String serializeCoordinate(final BlockNbtComponent.WorldPos.Coordinate coordinate) {
    return (coordinate.type() == BlockNbtComponent.WorldPos.Coordinate.Type.RELATIVE ? "~" : "") + coordinate.value();
  }
}
