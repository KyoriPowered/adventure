package net.kyori.text;

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.serializer.ComponentSerializer;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void testDecorations() {
        final Component component = new TextComponent("Kittens!");

        // The bold decoration should not be set at this point.
        assertFalse(component.hasDecoration(TextDecoration.BOLD));
        assertFalse(component.isBold());
        assertNull(component.getBold());

        component.setDecoration(TextDecoration.BOLD, true);

        final Set<TextDecoration> decorations = component.getDecorations();

        // The bold decoration should be set and true at this point.
        assertTrue(component.hasDecoration(TextDecoration.BOLD));
        assertTrue(component.isBold());
        assertNotNull(component.getBold());
        assertEquals(component.isBold(), decorations.contains(TextDecoration.BOLD));
        assertTrue(decorations.contains(TextDecoration.BOLD));
        assertFalse(decorations.contains(TextDecoration.OBFUSCATED));
    }

    @Test
    public void testStyleReset() {
        final Component component = new TextComponent("kittens");
        assertFalse(component.hasStyling());
        component.setBold(true);
        assertTrue(component.hasStyling());
        component.resetStyle();
        assertFalse(component.hasStyling());
    }

    @Test
    public void testContains() {
        final Component component = new TextComponent("cat");
        final Component child = new TextComponent("kittens");
        component.append(child);
        assertTrue(component.contains(child));
    }

    @Test(expected = IllegalStateException.class)
    public void testCycleSelf() {
        final Component component = new TextComponent("cat");
        component.append(component);
        fail("A component was added to itself");
    }

    @Test(expected = IllegalStateException.class)
    public void testCycleHoverRoot() {
        final Component component = new TextComponent("cat");
        final Component hoverComponent = new TextComponent("hover");
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        // component's hover event value is hoverComponent, we should not be able to add it
        hoverComponent.append(component);
        fail("A component was added to itself");
    }

    @Test(expected = IllegalStateException.class)
    public void testCycleHoverChild() {
        final Component component = new TextComponent("cat");
        final Component hoverComponent = new TextComponent("hover child");
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("hover").append(hoverComponent)));
        // component's hover event value contains hoverComponent, we should not be able to add it
        hoverComponent.append(component);
        fail("A component was added to itself");
    }

    @Test
    public void testSerializeDeserialize() {
        final TextComponent expected = new TextComponent("Hello!");
        expected.setColor(TextColor.DARK_PURPLE).setBold(true);
        expected.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://google.com/"));
        expected.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(":o").setColor(TextColor.DARK_AQUA)));
        final String json = ComponentSerializer.serialize(expected);
        assertEquals(expected, ComponentSerializer.deserialize(json));
    }

    @Test
    public void assertOpenFileNotReadable() {
        final ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_FILE, "fake");
        assertFalse(event.getAction().isReadable());
    }

    @Test
    public void testCopyHover() {
        final HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("Kittens!"));
        final HoverEvent copy = event.copy();
        assertEquals(event, copy);
        copy.getValue().setBold(true);
        assertNotEquals(event, copy);
    }
}
