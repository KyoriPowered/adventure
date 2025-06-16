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
package net.kyori.adventure.text.serializer.commons;

/**
 * Constants to aid in the creation and testing of tree-based component serializers.
 *
 * @since 4.20.0
 */
public final class ComponentTreeConstants {
  public static final String TEXT = "text";
  public static final String TRANSLATE = "translate";
  public static final String TRANSLATE_FALLBACK = "fallback";
  public static final String TRANSLATE_WITH = "with";
  public static final String SCORE = "score";
  public static final String SCORE_NAME = "name";
  public static final String SCORE_OBJECTIVE = "objective";
  @Deprecated
  public static final String SCORE_VALUE = "value";
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
  public static final String SHADOW_COLOR = "shadow_color";
  public static final String INSERTION = "insertion";
  @Deprecated
  public static final String CLICK_EVENT_CAMEL = "clickEvent";
  public static final String CLICK_EVENT_SNAKE = "click_event";
  public static final String CLICK_EVENT_ACTION = "action";
  public static final String CLICK_EVENT_VALUE = "value";
  public static final String CLICK_EVENT_URL = "url";
  public static final String CLICK_EVENT_PATH = "path";
  public static final String CLICK_EVENT_COMMAND = "command";
  public static final String CLICK_EVENT_PAGE = "page";
  public static final String CLICK_EVENT_ID = "id";
  public static final String CLICK_EVENT_PAYLOAD = "payload";
  @Deprecated
  public static final String HOVER_EVENT_CAMEL = "hoverEvent";
  public static final String HOVER_EVENT_SNAKE = "hover_event";
  public static final String HOVER_EVENT_ACTION = "action";
  @Deprecated
  public static final String HOVER_EVENT_CONTENTS = "contents";
  @Deprecated
  public static final String HOVER_EVENT_VALUE = "value";
  @Deprecated
  public static final String SHOW_ENTITY_TYPE = "type";
  public static final String SHOW_ENTITY_ID = "id";
  public static final String SHOW_ENTITY_UUID = "uuid";
  public static final String SHOW_ENTITY_NAME = "name";
  public static final String SHOW_ITEM_ID = "id";
  public static final String SHOW_ITEM_COUNT = "count";
  @Deprecated
  public static final String SHOW_ITEM_TAG = "tag";
  public static final String SHOW_ITEM_COMPONENTS = "components";

  private ComponentTreeConstants() {
    throw new IllegalStateException("Cannot instantiate");
  }
}
