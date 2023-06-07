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
package net.kyori.adventure.text.serializer.json;

import org.jetbrains.annotations.ApiStatus;

/**
 * Constants to aid in the creation and testing of JSON component serializers.
 *
 * @since 4.14.0
 */
@ApiStatus.Internal
public final class JSONComponentConstants {
  public static final String TEXT = "text";
  public static final String TRANSLATE = "translate";
  public static final String TRANSLATE_FALLBACK = "fallback";
  public static final String TRANSLATE_WITH = "with";
  public static final String SCORE = "score";
  public static final String SCORE_NAME = "name";
  public static final String SCORE_OBJECTIVE = "objective";
  public static final @Deprecated String SCORE_VALUE = "value";
  public static final String SELECTOR = "selector";
  public static final String KEYBIND = "keybind";
  public static final String EXTRA = "extra";
  public static final String NBT = "nbt";
  public static final String NBT_INTERPRET = "interpret";
  public static final String NBT_BLOCK = "block";
  public static final String NBT_ENTITY = "entity";
  public static final String NBT_STORAGE = "storage";
  public static final String SEPARATOR = "separator";
  public static final String FONT = "font";
  public static final String COLOR = "color";
  public static final String INSERTION = "insertion";
  public static final String CLICK_EVENT = "clickEvent";
  public static final String CLICK_EVENT_ACTION = "action";
  public static final String CLICK_EVENT_VALUE = "value";
  public static final String HOVER_EVENT = "hoverEvent";
  public static final String HOVER_EVENT_ACTION = "action";
  public static final String HOVER_EVENT_CONTENTS = "contents";
  public static final @Deprecated String HOVER_EVENT_VALUE = "value";
  public static final String SHOW_ENTITY_TYPE = "type";
  public static final String SHOW_ENTITY_ID = "id";
  public static final String SHOW_ENTITY_NAME = "name";
  public static final String SHOW_ITEM_ID = "id";
  public static final String SHOW_ITEM_COUNT = "count";
  public static final String SHOW_ITEM_TAG = "tag";

  private JSONComponentConstants() {
    throw new IllegalStateException("Cannot instantiate");
  }
}
