/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.serializer.configurate3;

import com.google.common.reflect.TypeToken;
import java.util.List;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("UnstableApiUsage") // TypeToken
final class BookTypeSerializer implements TypeSerializer<Book> {
  static final TypeToken<Book> TYPE = TypeToken.of(Book.class);
  static final BookTypeSerializer INSTANCE = new BookTypeSerializer();
  static final String TITLE = "title";
  static final String AUTHOR = "author";
  static final String PAGES = "pages";

  private BookTypeSerializer() {
  }

  @Override
  public Book deserialize(final @NonNull TypeToken<?> type, final @NonNull ConfigurationNode value) throws ObjectMappingException {
    final Component title = value.getNode(TITLE).getValue(ComponentTypeSerializer.TYPE);
    final Component author = value.getNode(AUTHOR).getValue(ComponentTypeSerializer.TYPE);
    final List<Component> pages = value.getNode(PAGES).getValue(ComponentTypeSerializer.LIST_TYPE);
    if(title == null || author == null || pages == null) {
      throw new ObjectMappingException("title, author, and pages fields are all required to deserialize a Book");
    }
    return Book.book(title, author, pages);
  }

  @Override
  public void serialize(final @NonNull TypeToken<?> type, final @Nullable Book obj, final @NonNull ConfigurationNode value) throws ObjectMappingException {
    if(obj == null) {
      value.setValue(null);
      return;
    }
    value.getNode(TITLE).setValue(ComponentTypeSerializer.TYPE, obj.title());
    value.getNode(AUTHOR).setValue(ComponentTypeSerializer.TYPE, obj.author());
    value.getNode(PAGES).setValue(ComponentTypeSerializer.LIST_TYPE, obj.pages());
  }
}
