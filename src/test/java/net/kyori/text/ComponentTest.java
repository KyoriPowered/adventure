package net.kyori.text;

import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.ComponentSerializer;
import org.junit.Assert;
import org.junit.Test;

public class ComponentTest {

    @Test
    public void testSerializeDeserialize() {
        TextComponent component = new TextComponent("Hello!");
        component.setColor(TextColor.DARK_PURPLE).setBold(true);
        String json = ComponentSerializer.serialize(component);
        Assert.assertEquals(component, ComponentSerializer.deserialize(json));
    }
}
