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
package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.kyori.text.BlockNbtComponent;

import java.io.IOException;

public class BlockNbtComponentPosSerializer extends JsonSerializer<BlockNbtComponent.Pos> {
    static final BlockNbtComponentPosSerializer INSTANCE = new BlockNbtComponentPosSerializer();

    private static String serializeCoordinate(final BlockNbtComponent.WorldPos.Coordinate coordinate) {
        return (coordinate.type() == BlockNbtComponent.WorldPos.Coordinate.Type.RELATIVE ? "~" : "") + coordinate.value();
    }

    @Override
    public void serialize(final BlockNbtComponent.Pos value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        if(value instanceof BlockNbtComponent.LocalPos) {
            final BlockNbtComponent.LocalPos local = (BlockNbtComponent.LocalPos) value;
            gen.writeString("^" + local.left() + ' ' + '^' + local.up() + ' ' + '^' + local.forwards());
        } else if(value instanceof BlockNbtComponent.WorldPos) {
            final BlockNbtComponent.WorldPos world = (BlockNbtComponent.WorldPos) value;
            gen.writeString(serializeCoordinate(world.x()) + ' ' + serializeCoordinate(world.y()) + ' ' + serializeCoordinate(world.z()));
        } else {
            serializers.reportMappingProblem("Don't know how to serialize " + value + " as a Position");
        }
    }
}
