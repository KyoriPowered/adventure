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
package net.kyori.adventure.text.serializer.json;

import net.kyori.adventure.text.serializer.constant.ComponentTreeConstants;
import org.jetbrains.annotations.ApiStatus;

/**
 * Constants to aid in the creation and testing of JSON component serializers.
 *
 * @since 4.14.0
 * @deprecated for removal since 4.20.0, use {@link ComponentTreeConstants} instead.
 */
@Deprecated
@ApiStatus.Internal
@SuppressWarnings("deprecation")
public final class JSONComponentConstants {
  public static final String TEXT = ComponentTreeConstants.TEXT;
  public static final String TRANSLATE = ComponentTreeConstants.TRANSLATE;
  public static final String TRANSLATE_FALLBACK = ComponentTreeConstants.TRANSLATE_FALLBACK;
  public static final String TRANSLATE_WITH = ComponentTreeConstants.TRANSLATE_WITH;
  public static final String SCORE = ComponentTreeConstants.SCORE;
  public static final String SCORE_NAME = ComponentTreeConstants.SCORE_NAME;
  public static final String SCORE_OBJECTIVE = ComponentTreeConstants.SCORE_OBJECTIVE;
  public static final @Deprecated String SCORE_VALUE = ComponentTreeConstants.SCORE_VALUE;
  public static final String SELECTOR = ComponentTreeConstants.SELECTOR;
  public static final String KEYBIND = ComponentTreeConstants.KEYBIND;
  public static final String EXTRA = ComponentTreeConstants.EXTRA;
  public static final String NBT = ComponentTreeConstants.NBT;
  public static final String NBT_INTERPRET = ComponentTreeConstants.NBT_INTERPRET;
  public static final String NBT_BLOCK = ComponentTreeConstants.NBT_BLOCK;
  public static final String NBT_ENTITY = ComponentTreeConstants.NBT_ENTITY;
  public static final String NBT_STORAGE = ComponentTreeConstants.NBT_STORAGE;
  public static final String SEPARATOR = ComponentTreeConstants.SEPARATOR;
  public static final String FONT = ComponentTreeConstants.FONT;
  public static final String COLOR = ComponentTreeConstants.COLOR;
  public static final String SHADOW_COLOR = ComponentTreeConstants.SHADOW_COLOR;
  public static final String INSERTION = ComponentTreeConstants.INSERTION;
  public static final String CLICK_EVENT = ComponentTreeConstants.CLICK_EVENT;
  public static final String CLICK_EVENT_ACTION = ComponentTreeConstants.CLICK_EVENT_ACTION;
  public static final String CLICK_EVENT_VALUE = ComponentTreeConstants.CLICK_EVENT_VALUE;
  public static final String HOVER_EVENT = ComponentTreeConstants.HOVER_EVENT;
  public static final String HOVER_EVENT_ACTION = ComponentTreeConstants.HOVER_EVENT_ACTION;
  public static final String HOVER_EVENT_CONTENTS = ComponentTreeConstants.HOVER_EVENT_CONTENTS;
  public static final @Deprecated String HOVER_EVENT_VALUE = ComponentTreeConstants.HOVER_EVENT_VALUE;
  public static final String SHOW_ENTITY_TYPE = ComponentTreeConstants.SHOW_ENTITY_TYPE;
  public static final String SHOW_ENTITY_ID = ComponentTreeConstants.SHOW_ENTITY_ID;
  public static final String SHOW_ENTITY_NAME = ComponentTreeConstants.SHOW_ENTITY_NAME;
  public static final String SHOW_ITEM_ID = ComponentTreeConstants.SHOW_ITEM_ID;
  public static final String SHOW_ITEM_COUNT = ComponentTreeConstants.SHOW_ITEM_COUNT;
  public static final @Deprecated String SHOW_ITEM_TAG = ComponentTreeConstants.SHOW_ITEM_TAG;
  public static final String SHOW_ITEM_COMPONENTS = ComponentTreeConstants.SHOW_ITEM_COMPONENTS;

  private JSONComponentConstants() {
    throw new IllegalStateException("Cannot instantiate");
  }
}
