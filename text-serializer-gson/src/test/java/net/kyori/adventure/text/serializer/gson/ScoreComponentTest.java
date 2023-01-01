/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonParseException;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreComponentTest extends ComponentTest {
  private static final String NAME = "abc";
  private static final String OBJECTIVE = "def";
  private static final String VALUE = "ghi";

  @Test
  void test() {
    this.test(
      Component.score(NAME, OBJECTIVE),
      object(json -> json.add(ComponentSerializerImpl.SCORE, object(score -> {
        score.addProperty(ComponentSerializerImpl.SCORE_NAME, NAME);
        score.addProperty(ComponentSerializerImpl.SCORE_OBJECTIVE, OBJECTIVE);
      })))
    );
  }

  @Test
  void testWithValue() {
    this.test(
      Component.score(NAME, OBJECTIVE, VALUE),
      object(json -> json.add(ComponentSerializerImpl.SCORE, object(score -> {
        score.addProperty(ComponentSerializerImpl.SCORE_NAME, NAME);
        score.addProperty(ComponentSerializerImpl.SCORE_OBJECTIVE, OBJECTIVE);
        score.addProperty(ComponentSerializerImpl.SCORE_VALUE, VALUE);
      })))
    );
  }

  @Test
  void testWithoutObjective() {
    assertThrows(JsonParseException.class, () -> this.deserialize(object(json -> json.add(ComponentSerializerImpl.SCORE, object(score -> score.addProperty(ComponentSerializerImpl.SCORE_NAME, NAME))))));
  }
}
