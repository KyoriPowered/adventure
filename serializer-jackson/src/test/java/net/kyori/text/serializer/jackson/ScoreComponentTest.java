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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import net.kyori.text.Component;
import net.kyori.text.ScoreComponent;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreComponentTest extends AbstractComponentTest<ScoreComponent> {
  private static final String NAME = "abc";
  private static final String OBJECTIVE = "def";
  private static final String VALUE = "ghi";

  @Override
  Stream<Map.Entry<ScoreComponent, JsonNode>> tests() {
    return Stream.of(
      entry(ScoreComponent.of(NAME, OBJECTIVE), json -> json.set(ComponentSerializer.SCORE, object(score -> {
        score.put(ComponentSerializer.SCORE_NAME, NAME);
        score.put(ComponentSerializer.SCORE_OBJECTIVE, OBJECTIVE);
      }))),
      entry(ScoreComponent.of(NAME, OBJECTIVE, VALUE), json -> json.set(ComponentSerializer.SCORE, object(score -> {
        score.put(ComponentSerializer.SCORE_NAME, NAME);
        score.put(ComponentSerializer.SCORE_OBJECTIVE, OBJECTIVE);
        score.put(ComponentSerializer.SCORE_VALUE, VALUE);
      })))
    );
  }

  @Test
  void testDeserialize_withoutObjective() {
    // should be readValue, because convertValue wraps actual exception as IllegalArgumentException
    assertThrows(MismatchedInputException.class, () -> JacksonComponentSerializer.MAPPER.readValue(object(json -> json.set(ComponentSerializer.SCORE, object(score -> score.put(ComponentSerializer.SCORE_NAME, NAME)))).traverse(JacksonComponentSerializer.MAPPER.getFactory().getCodec()), Component.class));
  }
}
