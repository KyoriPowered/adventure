package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
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

public class CustomDeserializers
        implements Deserializers, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    protected HashMap<CustomClassKey,JsonDeserializer<?>> _classMappings = null;

    /**
     * Flag to help find "generic" enum deserializer, if one has been registered.
     *
     * @since 2.3
     */
    protected boolean _hasEnumDeserializer = false;

    /*
    /**********************************************************
    /* Life-cycle, construction and configuring
    /**********************************************************
     */

    public CustomDeserializers() { }

    /**
     * @since 2.1
     */
    public CustomDeserializers(Map<Class<?>,JsonDeserializer<?>> desers) {
        addDeserializers(desers);
    }

    public <T> void addDeserializer(Class<T> forClass, JsonDeserializer<? extends T> deser)
    {
        CustomClassKey key = new CustomClassKey(forClass);
        if (_classMappings == null) {
            _classMappings = new HashMap<>();
        }
        _classMappings.put(key, deser);
        // [Issue#227]: generic Enum deserializer?
        if (forClass == Enum.class) {
            _hasEnumDeserializer = true;
        }
    }

    /**
     * @since 2.1
     */
    @SuppressWarnings("unchecked")
    public void addDeserializers(Map<Class<?>,JsonDeserializer<?>> desers)
    {
        for (Map.Entry<Class<?>,JsonDeserializer<?>> entry : desers.entrySet()) {
            Class<?> cls = entry.getKey();
            // what a mess... nominal generics safety...
            JsonDeserializer<Object> deser = (JsonDeserializer<Object>) entry.getValue();
            addDeserializer((Class<Object>) cls, deser);
        }
    }

    /*
    /**********************************************************
    /* Serializers implementation
    /**********************************************************
     */

    @Override
    public JsonDeserializer<?> findArrayDeserializer(ArrayType type,
                                                     DeserializationConfig config, BeanDescription beanDesc,
                                                     TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException
    {
        return _find(type);
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type,
                                                    DeserializationConfig config, BeanDescription beanDesc)
            throws JsonMappingException
    {
        return _find(type);
    }

    @Override
    public JsonDeserializer<?> findCollectionDeserializer(CollectionType type,
                                                          DeserializationConfig config, BeanDescription beanDesc,
                                                          TypeDeserializer elementTypeDeserializer,
                                                          JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException
    {
        return _find(type);
    }

    @Override
    public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type,
                                                              DeserializationConfig config, BeanDescription beanDesc,
                                                              TypeDeserializer elementTypeDeserializer,
                                                              JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException
    {
        return _find(type);
    }

    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type,
                                                    DeserializationConfig config, BeanDescription beanDesc)
            throws JsonMappingException
    {
        if (_classMappings == null) {
            return null;
        }
        JsonDeserializer<?> deser = _classMappings.get(new CustomClassKey(type));
        if (deser == null) {
            if (_hasEnumDeserializer && type.isEnum()) {
                deser = _classMappings.get(new CustomClassKey(Enum.class));
            }
        }
        return deser;
    }

    @Override
    public JsonDeserializer<?> findTreeNodeDeserializer(Class<? extends JsonNode> nodeType,
                                                        DeserializationConfig config, BeanDescription beanDesc)
            throws JsonMappingException
    {
        if (_classMappings == null) {
            return null;
        }
        return _classMappings.get(new CustomClassKey(nodeType));
    }

    @Override
    public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType,
                                                         DeserializationConfig config, BeanDescription beanDesc,
                                                         TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer)
            throws JsonMappingException {
        // 21-Oct-2015, tatu: Unlikely this will really get used (reference types need more
        //    work, simple registration probably not sufficient). But whatever.
        return _find(refType);
    }

    @Override
    public JsonDeserializer<?> findMapDeserializer(MapType type,
                                                   DeserializationConfig config, BeanDescription beanDesc,
                                                   KeyDeserializer keyDeserializer,
                                                   TypeDeserializer elementTypeDeserializer,
                                                   JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException
    {
        return _find(type);
    }

    @Override
    public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type,
                                                       DeserializationConfig config, BeanDescription beanDesc,
                                                       KeyDeserializer keyDeserializer,
                                                       TypeDeserializer elementTypeDeserializer,
                                                       JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException
    {
        return _find(type);
    }

    private final JsonDeserializer<?> _find(JavaType type) {
        if (_classMappings == null) {
            return null;
        }
        for (Map.Entry<CustomClassKey, JsonDeserializer<?>> entry : _classMappings.entrySet()) {
            if (entry.getKey().isAssignableFrom(type.getRawClass())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
