package net.kyori.text;

import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.serializer.ComponentSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ComponentTest {

    @Test
    public void testCopy() {
        final Component component = new TextComponent("").setColor(TextColor.GRAY);
        component.append(new TextComponent("This is a test").setColor(TextColor.DARK_PURPLE));
        component.append(new TextComponent(" "));
        component.append(new TextComponent("A what?").setColor(TextColor.DARK_AQUA).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/what")));
        assertEquals(component, component.copy());
    }

    @Test
    public void testAddDecoration() {
        final Component component = new TextComponent("Kittens!");

        // The bold decoration should not be set at this point.
        assertFalse(component.hasDecoration(TextDecoration.BOLD));
        assertFalse(component.isBold());
        assertNull(component.getBold());

        component.setDecoration(TextDecoration.BOLD, true);

        // The bold decoration should be set and true at this point.
        assertTrue(component.hasDecoration(TextDecoration.BOLD));
        assertTrue(component.isBold());
        assertNotNull(component.getBold());
    }

    @Test
    public void testSerializeDeserialize() {
        final TextComponent expected = new TextComponent("Hello!");
        expected.setColor(TextColor.DARK_PURPLE).setBold(true);
        final String json = ComponentSerializer.serialize(expected);
        assertEquals(expected, ComponentSerializer.deserialize(json));
    }
}
