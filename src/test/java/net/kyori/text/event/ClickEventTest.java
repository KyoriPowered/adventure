package net.kyori.text.event;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ClickEventTest {

    @Test
    public void assertOpenFileNotReadable() {
        final ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_FILE, "fake");
        assertFalse(event.getAction().isReadable());
    }
}
