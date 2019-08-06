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

    private Map<ClassKey,JsonDeserializer<?>> mappings = null;

    private boolean enumDeserializer = false;

    KyoriDeserializers() {}

    <T> void addDeserializer(Class<T> forClass, JsonDeserializer<? extends T> deser) {
        ClassKey key = new ClassKey(forClass);
        if (mappings == null) {
            mappings = new HashMap<>();
        }
        mappings.put(key, deser);
        if (forClass == Enum.class) {
            enumDeserializer = true;
        }
    }

    @Override
    public JsonDeserializer<?> findArrayDeserializer(ArrayType type,
                                                     DeserializationConfig config,
                                                     BeanDescription beanDesc,
                                                     TypeDeserializer elementTypeDeserializer,
                                                     JsonDeserializer<?> elementDeserializer) {
        return find(type);
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type,
                                                    DeserializationConfig config,
                                                    BeanDescription beanDesc) {
        return find(type);
    }

    @Override
    public JsonDeserializer<?> findCollectionDeserializer(CollectionType type,
                                                          DeserializationConfig config,
                                                          BeanDescription beanDesc,
                                                          TypeDeserializer elementTypeDeserializer,
                                                          JsonDeserializer<?> elementDeserializer) {
        return find(type);
    }

    @Override
    public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type,
                                                              DeserializationConfig config,
                                                              BeanDescription beanDesc,
                                                              TypeDeserializer elementTypeDeserializer,
                                                              JsonDeserializer<?> elementDeserializer) {
        return find(type);
    }

    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type,
                                                    DeserializationConfig config,
                                                    BeanDescription beanDesc) {
        if (mappings == null) {
            return null;
        }
        JsonDeserializer<?> deser = mappings.get(new ClassKey(type));
        if (deser == null && enumDeserializer && type.isEnum()) {
            deser = mappings.get(new ClassKey(Enum.class));
        }
        return deser;
    }

    @Override
    public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType,
                                                        DeserializationConfig config,
                                                        BeanDescription beanDesc) {
        return find(nodeType);
    }

    @Override
    public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType,
                                                         DeserializationConfig config,
                                                         BeanDescription beanDesc,
                                                         TypeDeserializer contentTypeDeserializer,
                                                         JsonDeserializer<?> contentDeserializer) {
        return find(refType);
    }

    @Override
    public JsonDeserializer<?> findMapDeserializer(MapType type,
                                                   DeserializationConfig config,
                                                   BeanDescription beanDesc,
                                                   KeyDeserializer keyDeserializer,
                                                   TypeDeserializer elementTypeDeserializer,
                                                   JsonDeserializer<?> elementDeserializer) {
        return find(type);
    }

    @Override
    public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type,
                                                       DeserializationConfig config,
                                                       BeanDescription beanDesc,
                                                       KeyDeserializer keyDeserializer,
                                                       TypeDeserializer elementTypeDeserializer,
                                                       JsonDeserializer<?> elementDeserializer) {
        return find(type);
    }

    private JsonDeserializer<?> find(JavaType type) {
        return find(type.getRawClass());
    }

    private JsonDeserializer<?> find(Class<?> clazz) {
        if (mappings == null) {
            return null;
        }
        for (Map.Entry<ClassKey, JsonDeserializer<?>> entry : mappings.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
