package me.minidigger.minimessage;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

import org.junit.Test;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;

public class MiniMessageSerializerTest {

    @Test
    public void testColor() {
        String expected = "<red>This is a test";

        ComponentBuilder builder = new ComponentBuilder("This is a test");
        builder.color(ChatColor.RED);

        test(builder, expected);
    }

    @Test
    public void testColorClosing() {
        String expected = "<red>This is a </red>test";

        ComponentBuilder builder = new ComponentBuilder("This is a ");
        builder.color(ChatColor.RED);
        builder.append("test", FormatRetention.NONE);

        test(builder, expected);
    }

    @Test
    public void testNestedColor() {
        String expected = "<red>This is a <blue>blue <red>test";

        ComponentBuilder builder = new ComponentBuilder("This is a ").color(ChatColor.RED)//
            .append("blue ", FormatRetention.NONE).color(ChatColor.BLUE)//
            .append("test", FormatRetention.NONE).color(ChatColor.RED);

        test(builder, expected);
    }

    @Test
    public void testDecoration() {
        String expected = "<underlined>This is <bold>underlined</underlined>, this</bold> isn't";

        ComponentBuilder builder = new ComponentBuilder("This is ").underlined(true)//
            .append("underlined", FormatRetention.NONE).bold(true).underlined(true)//
            .append(", this", FormatRetention.NONE).bold(true)//
            .append(" isn't", FormatRetention.NONE);

        test(builder, expected);
    }

    @Test
    public void testHover() {
        String expected = "<hover:show_text:\"---\">Some hover</hover> that ends here";

        ComponentBuilder builder = new ComponentBuilder("Some hover")
            .event(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("---").create()))//
            .append(" that ends here", FormatRetention.NONE);

        test(builder, expected);
    }

    @Test
    public void testHoverWithNested() {
        String expected = "<hover:show_text:\"<red>---<blue><bold>-\">Some hover</hover> that ends here";

        ComponentBuilder builder = new ComponentBuilder("Some hover").event(new HoverEvent(Action.SHOW_TEXT, //
            new ComponentBuilder("---").color(ChatColor.RED)//
                .append("-").color(ChatColor.BLUE).bold(true).create()))//
            .append(" that ends here", FormatRetention.NONE);

        test(builder, expected);
    }

    @Test
    public void testClick() {
        String expected = "<click:run_command:\"test\">Some click</click> that ends here";

        ComponentBuilder builder = new ComponentBuilder("Some click")
            .event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "test"))//
            .append(" that ends here", FormatRetention.NONE);

        test(builder, expected);
    }

    @Test
    public void testContinuedClick() {
        String expected = "<click:run_command:\"test\">Some click<red> that doesn't ends here";

        ComponentBuilder builder = new ComponentBuilder("Some click")
            .event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "test"))//
            .append(" that doesn't ends here", FormatRetention.EVENTS).color(ChatColor.RED);

        test(builder, expected);
    }

    @Test
    public void testContinuedClick2() {
        String expected = "<click:run_command:\"test\">Some click<red> that doesn't ends here";

        ComponentBuilder builder = new ComponentBuilder("Some click")
            .event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "test"))//
            .append(" that doesn't ends here", FormatRetention.NONE)
            .event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "test"))
            .color(ChatColor.RED);

        test(builder, expected);
    }

    private void test(@Nonnull ComponentBuilder builder, @Nonnull String expected) {
        String string = MiniMessageSerializer.serialize(builder.create());
        assertEquals(expected, string);
    }
}
