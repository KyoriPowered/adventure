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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ComponentTest {

    @Test
    public void testCopy() {
        final Component component = new TextComponent("").color(TextColor.GRAY);
        component.append(new TextComponent("This is a test").color(TextColor.DARK_PURPLE));
        component.append(new TextComponent(" "));
        component.append(new TextComponent("A what?").color(TextColor.DARK_AQUA).clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/what")));
        assertEquals(component, component.copy());
    }

    @Test
    public void testDecorations() {
        final Component component = new TextComponent("Kittens!");

        // The bold decoration should not be set at this point.
        assertFalse(component.hasDecoration(TextDecoration.BOLD));
        assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.BOLD));

        component.decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);

        final Set<TextDecoration> decorations = component.decorations();

        // The bold decoration should be set and true at this point.
        assertTrue(component.hasDecoration(TextDecoration.BOLD));
        assertEquals(TextDecoration.State.TRUE, component.decoration(TextDecoration.BOLD));
        assertEquals(component.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE, decorations.contains(TextDecoration.BOLD));
        assertTrue(decorations.contains(TextDecoration.BOLD));
        assertFalse(decorations.contains(TextDecoration.OBFUSCATED));
    }

    @Test
    public void testStyleReset() {
        final Component component = new TextComponent("kittens");
        assertFalse(component.hasStyling());
        component.decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);
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
        component.hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        // component's hover event value is hoverComponent, we should not be able to add it
        hoverComponent.append(component);
        fail("A component was added to itself");
    }

    @Test(expected = IllegalStateException.class)
    public void testCycleHoverChild() {
        final Component component = new TextComponent("cat");
        final Component hoverComponent = new TextComponent("hover child");
        component.hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("hover").append(hoverComponent)));
        // component's hover event value contains hoverComponent, we should not be able to add it
        hoverComponent.append(component);
        fail("A component was added to itself");
    }

    @Test
    public void testSerializeDeserialize() {
        final TextComponent expected = new TextComponent("Hello!");
        expected.color(TextColor.DARK_PURPLE).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);
        expected.clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://google.com/"));
        expected.hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(":o").color(TextColor.DARK_AQUA)));
        final String json = ComponentSerializer.serialize(expected);
        assertEquals(expected, ComponentSerializer.deserialize(json));
    }

    @Test
    public void assertOpenFileNotReadable() {
        final ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_FILE, "fake");
        assertFalse(event.action().isReadable());
    }

    @Test
    public void testCopyHover() {
        final HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("Kittens!"));
        final HoverEvent copy = event.copy();
        assertEquals(event, copy);
        copy.value().decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);
        assertNotEquals(event, copy);
    }
}
