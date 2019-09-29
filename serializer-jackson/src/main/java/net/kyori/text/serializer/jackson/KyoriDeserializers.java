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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import java.util.HashMap;
import java.util.Map;

public class KyoriDeserializers
  implements Deserializers, java.io.Serializable {
  private static final long serialVersionUID = 1L;

  private Map<ClassKey, JsonDeserializer<?>> mappings = null;

  private boolean enumDeserializer = false;

  KyoriDeserializers() {
  }

  <T> void addDeserializer(final Class<T> forClass, final JsonDeserializer<? extends T> deser) {
    final ClassKey key = new ClassKey(forClass);
    if(this.mappings == null) {
      this.mappings = new HashMap<>();
    }
    this.mappings.put(key, deser);
    if(forClass == Enum.class) {
      this.enumDeserializer = true;
    }
  }

  @Override
  public JsonDeserializer<?> findArrayDeserializer(final ArrayType type,
                                                   final DeserializationConfig config,
                                                   final BeanDescription beanDesc,
                                                   final TypeDeserializer elementTypeDeserializer,
                                                   final JsonDeserializer<?> elementDeserializer) {
    return this.find(type);
  }

  @Override
  public JsonDeserializer<?> findBeanDeserializer(final JavaType type,
                                                  final DeserializationConfig config,
                                                  final BeanDescription beanDesc) {
    return this.find(type);
  }

  @Override
  public JsonDeserializer<?> findCollectionDeserializer(final CollectionType type,
                                                        final DeserializationConfig config,
                                                        final BeanDescription beanDesc,
                                                        final TypeDeserializer elementTypeDeserializer,
                                                        final JsonDeserializer<?> elementDeserializer) {
    return this.find(type);
  }

  @Override
  public JsonDeserializer<?> findCollectionLikeDeserializer(final CollectionLikeType type,
                                                            final DeserializationConfig config,
                                                            final BeanDescription beanDesc,
                                                            final TypeDeserializer elementTypeDeserializer,
                                                            final JsonDeserializer<?> elementDeserializer) {
    return this.find(type);
  }

  @Override
  public JsonDeserializer<?> findEnumDeserializer(final Class<?> type,
                                                  final DeserializationConfig config,
                                                  final BeanDescription beanDesc) {
    if(this.mappings == null) {
      return null;
    }
    JsonDeserializer<?> deser = this.mappings.get(new ClassKey(type));
    if(deser == null && this.enumDeserializer && type.isEnum()) {
      deser = this.mappings.get(new ClassKey(Enum.class));
    }
    return deser;
  }

  @Override
  public JsonDeserializer<?> findTreeNodeDeserializer(final Class<? extends JsonNode> nodeType,
                                                      final DeserializationConfig config,
                                                      final BeanDescription beanDesc) {
    return this.find(nodeType);
  }

  @Override
  public JsonDeserializer<?> findReferenceDeserializer(final ReferenceType refType,
                                                       final DeserializationConfig config,
                                                       final BeanDescription beanDesc,
                                                       final TypeDeserializer contentTypeDeserializer,
                                                       final JsonDeserializer<?> contentDeserializer) {
    return this.find(refType);
  }

  @Override
  public JsonDeserializer<?> findMapDeserializer(final MapType type,
                                                 final DeserializationConfig config,
                                                 final BeanDescription beanDesc,
                                                 final KeyDeserializer keyDeserializer,
                                                 final TypeDeserializer elementTypeDeserializer,
                                                 final JsonDeserializer<?> elementDeserializer) {
    return this.find(type);
  }

  @Override
  public JsonDeserializer<?> findMapLikeDeserializer(final MapLikeType type,
                                                     final DeserializationConfig config,
                                                     final BeanDescription beanDesc,
                                                     final KeyDeserializer keyDeserializer,
                                                     final TypeDeserializer elementTypeDeserializer,
                                                     final JsonDeserializer<?> elementDeserializer) {
    return this.find(type);
  }

  private JsonDeserializer<?> find(final JavaType type) {
    return this.find(type.getRawClass());
  }

  private JsonDeserializer<?> find(final Class<?> clazz) {
    if(this.mappings == null) {
      return null;
    }
    for(final Map.Entry<ClassKey, JsonDeserializer<?>> entry : this.mappings.entrySet()) {
      if(entry.getKey().isAssignableFrom(clazz)) {
        return entry.getValue();
      }
    }
    return null;
  }
}
