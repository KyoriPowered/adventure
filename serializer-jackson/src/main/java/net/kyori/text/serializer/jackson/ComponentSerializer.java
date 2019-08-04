package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import net.kyori.text.BlockNbtComponent;
import net.kyori.text.Component;
import net.kyori.text.EntityNbtComponent;
import net.kyori.text.KeybindComponent;
import net.kyori.text.NbtComponent;
import net.kyori.text.ScoreComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.format.Style;

import java.io.IOException;
import java.util.Map;

public class ComponentSerializer extends JsonSerializer<Component> {
    static final ComponentSerializer INSTANCE = new ComponentSerializer();

    static final String TEXT = "text";
    static final String TRANSLATE = "translate";
    static final String TRANSLATE_WITH = "with";
    static final String SCORE = "score";
    static final String SCORE_NAME = "name";
    static final String SCORE_OBJECTIVE = "objective";
    static final String SCORE_VALUE = "value";
    static final String SELECTOR = "selector";
    static final String KEYBIND = "keybind";
    static final String EXTRA = "extra";
    static final String NBT = "nbt";
    static final String NBT_INTERPRET = "interpret";
    static final String NBT_BLOCK = "block";
    static final String NBT_ENTITY = "entity";

    @Override
    public void serialize(Component value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value instanceof TextComponent) {
            gen.writeStringField(TEXT, ((TextComponent) value).content());
        } else if(value instanceof TranslatableComponent) {
            final TranslatableComponent tc = (TranslatableComponent) value;
            gen.writeStringField(TRANSLATE, tc.key());
            if(!tc.args().isEmpty()) {
                gen.writeArrayFieldStart(TRANSLATE_WITH);
                for(final Component arg : tc.args()) {
                    gen.writeObject(arg);
                }
                gen.writeEndArray();
            }
        } else if(value instanceof ScoreComponent) {
            final ScoreComponent sc = (ScoreComponent) value;
            gen.writeObjectFieldStart(SCORE);
            gen.writeStringField(SCORE_NAME, sc.name());
            gen.writeStringField(SCORE_OBJECTIVE, sc.objective());
            // score component value is optional
            if(sc.value() != null) gen.writeStringField(SCORE_VALUE, sc.value());
            gen.writeEndObject();
        } else if(value instanceof SelectorComponent) {
            gen.writeStringField(SELECTOR, ((SelectorComponent) value).pattern());
        } else if(value instanceof KeybindComponent) {
            gen.writeStringField(KEYBIND, ((KeybindComponent) value).keybind());
        } else if(value instanceof NbtComponent) {
            final NbtComponent<?, ?> nc = (NbtComponent<?, ?>) value;
            gen.writeStringField(NBT, nc.nbtPath());
            gen.writeBooleanField(NBT_INTERPRET, nc.interpret());
            if(value instanceof BlockNbtComponent) {
                gen.writeObjectField(NBT_BLOCK, ((BlockNbtComponent) nc).pos());
            } else if(value instanceof EntityNbtComponent) {
                gen.writeObjectField(NBT_ENTITY, ((EntityNbtComponent) nc).selector());
            } else {
                serializers.reportMappingProblem("Don't know how to serialize " + value + " as a Component");
            }
        } else {
            serializers.reportMappingProblem("Don't know how to serialize " + value + " as a Component");
        }

        if(!value.children().isEmpty()) {
            gen.writeArrayFieldStart(EXTRA);
            for(final Component child : value.children()) {
                gen.writeObject(child);
            }
            gen.writeEndArray();
        }

        if(value.hasStyling()) {
            gen.writeObject(value.style());
        }
    }
}
