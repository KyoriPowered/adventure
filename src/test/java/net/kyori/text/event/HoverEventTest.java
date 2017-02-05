package net.kyori.text.event;

import net.kyori.text.TextComponent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HoverEventTest {

    @Test
    public void testCopy() {
        final HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("Kittens!"));
        assertEquals(event, event.copy());
    }
}
