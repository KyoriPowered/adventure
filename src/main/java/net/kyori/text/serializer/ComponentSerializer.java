package net.kyori.text.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.text.Component;
import net.kyori.text.ScoreComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;

import java.lang.reflect.Type;

import javax.annotation.Nullable;

/**
 * A {@link Component} serializer and deserializer.
 */
public class ComponentSerializer implements JsonDeserializer<Component>, JsonSerializer<Component> {

    private static final Gson GSON = new GsonBuilder()
        .registerTypeHierarchyAdapter(Component.class, new ComponentSerializer())
        .create();

    /**
     * Serialize a component into a json string.
     *
     * @param component the component
     * @return the json string
     */
    public static String serialize(final Component component) {
        return GSON.toJson(component);
    }

    /**
     * Deserialize a json string into a component.
     *
     * @param string the json string
     * @return the component
     */
    public static Component deserialize(final String string) {
        return GSON.fromJson(string, Component.class);
    }

    @Override
    public Component deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if(element.isJsonPrimitive()) {
            return new TextComponent(element.getAsString());
        }
        if(!element.isJsonObject()) {
            if(element.isJsonArray()) {
                Component parent = null;
                for(final JsonElement childElement : element.getAsJsonArray()) {
                    final Component child = this.deserialize(childElement, childElement.getClass(), context);
                    if(parent == null) {
                        parent = child;
                    } else {
                        parent.append(child);
                    }
                }
                return parent;
            }

            throw new JsonParseException("Don't know how to turn " + element + " into a Component");
        }
        final JsonObject object = element.getAsJsonObject();
        final Component component;
        if(object.has("text")) {
            component = new TextComponent(object.get("text").getAsString());
        } else if(object.has("translate")) {
            final String key = object.get("translate").getAsString();
            if(!object.has("with")) {
                component = new TranslatableComponent(key);
            } else {
                final JsonArray with = object.getAsJsonArray("with");
                final Component[] args = new Component[with.size()];
                for(int i = 0; i < args.length; i++) {
                    final JsonElement argElement = with.get(i);
                    args[i] = this.deserialize(argElement, argElement.getClass(), context);
                }
                component = new TranslatableComponent(key, args);
            }
        } else if(object.has("score")) {
            final JsonObject score = object.getAsJsonObject("score");
            if(!score.has("name") && !score.has("objective")) {
                throw new JsonParseException("A score component requires a name and objective");
            }
            // score components can have a value sometimes, let's grab it
            if(score.has("value")) {
                component = new ScoreComponent(score.get("name").getAsString(), score.get("objective").getAsString(), score.get("value").getAsString());
            } else {
                component = new ScoreComponent(score.get("name").getAsString(), score.get("objective").getAsString());
            }
        } else if(object.has("selector")) {
            component = new SelectorComponent(object.get("selector").getAsString());
        } else {
            throw new JsonParseException("Don't know how to turn " + element + " into a Component");
        }

        if(object.has("extra")) {
            final JsonArray extra = object.getAsJsonArray("extra");
            for(int i = 0, size = extra.size(); i < size; i++) {
                final JsonElement extraElement = extra.get(i);
                component.append(this.deserialize(extraElement, extraElement.getClass(), context));
            }
        }

        if(object.has("bold")) component.setBold(object.get("bold").getAsBoolean());
        if(object.has("italic")) component.setItalic(object.get("italic").getAsBoolean());
        if(object.has("underlined")) component.setUnderlined(object.get("underlined").getAsBoolean());
        if(object.has("strikethrough")) component.setStrikethrough(object.get("strikethrough").getAsBoolean());
        if(object.has("obfuscated")) component.setObfuscated(object.get("obfuscated").getAsBoolean());
        if(object.has("color")) component.setColor(context.deserialize(object.get("color"), TextColor.class));
        if(object.has("insertion")) component.setInsertion(object.get("insertion").getAsString());
        if(object.has("clickEvent")) {
            final JsonObject clickEvent = object.getAsJsonObject("clickEvent");
            if(clickEvent != null) {
                @Nullable final JsonPrimitive rawAction = clickEvent.getAsJsonPrimitive("action");
                @Nullable final ClickEvent.Action action = rawAction == null ? null : ClickEvent.Action.getById(rawAction.getAsString());
                @Nullable final JsonPrimitive rawValue = clickEvent.getAsJsonPrimitive("value");
                @Nullable final String value = rawValue == null ? null : rawValue.getAsString();
                if(action != null && value != null && action.isReadable()) {
                    component.setClickEvent(new ClickEvent(action, value));
                }
            }
        }
        if(object.has("hoverEvent")) {
            final JsonObject hoverEvent = object.getAsJsonObject("hoverEvent");
            if(hoverEvent != null) {
                @Nullable final JsonPrimitive rawAction = hoverEvent.getAsJsonPrimitive("action");
                @Nullable final HoverEvent.Action action = rawAction == null ? null : HoverEvent.Action.getById(rawAction.getAsString());
                if(action != null && action.isReadable()) {
                    @Nullable final JsonElement rawValue = hoverEvent.get("value");
                    @Nullable final Component value = rawValue == null ? null : this.deserialize(rawValue, rawValue.getClass(), context);
                    if(value != null) component.setHoverEvent(new HoverEvent(action, value));
                }
            }
        }

        return component;
    }

    @Override
    public JsonElement serialize(final Component component, final Type type, final JsonSerializationContext context) {
        final JsonObject object = new JsonObject();
        if(component instanceof TextComponent) {
            object.addProperty("text", ((TextComponent) component).getContent());
        } else if(component instanceof TranslatableComponent) {
            final TranslatableComponent tc = (TranslatableComponent) component;
            object.addProperty("translate", tc.getKey());
            if(!tc.getArgs().isEmpty()) {
                final JsonArray with = new JsonArray();
                for(final Component arg : tc.getArgs()) {
                    with.add(this.serialize(arg, arg.getClass(), context));
                }
                object.add("with", with);
            }
        } else if(component instanceof ScoreComponent) {
            final ScoreComponent sc = (ScoreComponent) component;
            final JsonObject score = new JsonObject();
            score.addProperty("name", sc.getName());
            score.addProperty("objective", sc.getObjective());
            // score component value is optional
            if(sc.getValue() != null) score.addProperty("value", sc.getValue());
            object.add("score", score);
        } else if(component instanceof SelectorComponent) {
            object.addProperty("selector", ((SelectorComponent) component).getPattern());
        } else {
            throw new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
        }

        if(!component.getChildren().isEmpty()) {
            final JsonArray extra = new JsonArray();
            for(Component child : component.getChildren()) {
                extra.add(this.serialize(child, child.getClass(), context));
            }
            object.add("extra", extra);
        }

        if(component.hasStyling()) {
            if(component.getBold() != null) object.addProperty("bold", component.getBold());
            if(component.getItalic() != null) object.addProperty("italic", component.getItalic());
            if(component.getUnderlined() != null) object.addProperty("underlined", component.getUnderlined());
            if(component.getStrikethrough() != null) object.addProperty("strikethrough", component.getStrikethrough());
            if(component.getObfuscated() != null) object.addProperty("obfuscated", component.getObfuscated());
            if(component.getColor() != null) object.add("color", context.serialize(component.getColor()));
            if(component.getInsertion() != null) object.add("insertion", context.serialize(component.getInsertion()));
            if(component.getClickEvent() != null) {
                final JsonObject clickEvent = new JsonObject();
                clickEvent.addProperty("action", component.getClickEvent().getAction().getId());
                clickEvent.addProperty("value", component.getClickEvent().getValue());
                object.add("clickEvent", clickEvent);
            }
            if(component.getHoverEvent() != null) {
                final JsonObject hoverEvent = new JsonObject();
                hoverEvent.addProperty("action", component.getHoverEvent().getAction().getId());
                hoverEvent.add("value", this.serialize(component.getHoverEvent().getValue(), type, context));
                object.add("hoverEvent", hoverEvent);
            }
        }

        return object;
    }
}
