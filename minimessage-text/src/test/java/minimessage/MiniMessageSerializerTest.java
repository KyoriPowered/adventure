package minimessage;

import net.kyori.text.KeybindComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TextComponent.Builder;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import org.junit.Test;

import javax.annotation.Nonnull;

import me.minidigger.minimessage.MiniMessageSerializer;

import static org.junit.Assert.assertEquals;

public class MiniMessageSerializerTest {

    @Test
    public void testColor() {
        String expected = "<red>This is a test";

        Builder builder = TextComponent.builder().content("This is a test").color(TextColor.RED);

        test(builder, expected);
    }

    @Test
    public void testColorClosing() {
        String expected = "<red>This is a </red>test";

        Builder builder = TextComponent.builder()
                .content("This is a ").color(TextColor.RED)
                .append("test");

        test(builder, expected);
    }

    @Test
    public void testNestedColor() {
        String expected = "<red>This is a <blue>blue <red>test";

        Builder builder = TextComponent.builder()
                .content("This is a ").color(TextColor.RED)
                .append("blue ", TextColor.BLUE)
                .append("test", TextColor.RED);

        test(builder, expected);
    }

    @Test
    public void testDecoration() {
        String expected = "<underlined>This is <bold>underlined</underlined>, this</bold> isn't";

        Builder builder = TextComponent.builder()
                .content("This is ").decoration(TextDecoration.UNDERLINED, true)
                .append("underlined", b -> b.decoration(TextDecoration.UNDERLINED, true).decoration(TextDecoration.BOLD, true))
                .append(", this", b -> b.decoration(TextDecoration.BOLD, true))
                .append(" isn't");

        test(builder, expected);
    }

    @Test
    public void testHover() {
        String expected = "<hover:show_text:\"---\">Some hover</hover> that ends here";

        Builder builder = TextComponent.builder()
                .content("Some hover").hoverEvent(HoverEvent.showText(TextComponent.of("---")))
                .append(" that ends here");

        test(builder, expected);
    }

    @Test
    public void testHoverWithNested() {
        String expected = "<hover:show_text:\"<red>---<blue><bold>-\">Some hover</hover> that ends here";

        Builder builder = TextComponent.builder()
                .content("Some hover").hoverEvent(HoverEvent.showText(TextComponent.builder()
                        .content("---").color(TextColor.RED)
                        .append("-", TextColor.BLUE, TextDecoration.BOLD)
                        .build()))
                .append(" that ends here");

        test(builder, expected);
    }

    @Test
    public void testClick() {
        String expected = "<click:run_command:\"test\">Some click</click> that ends here";

        Builder builder = TextComponent.builder()
                .content("Some click").clickEvent(ClickEvent.runCommand("test"))
                .append(" that ends here");

        test(builder, expected);
    }

    @Test
    public void testContinuedClick() {
        String expected = "<click:run_command:\"test\">Some click<red> that doesn't end here";

        Builder builder = TextComponent.builder()
                .content("Some click").clickEvent(ClickEvent.runCommand("test"))
                // TODO figure out how to avoid repeating the click event here
                .append(" that doesn't end here", b -> b.color(TextColor.RED).clickEvent(ClickEvent.runCommand("test")));

        test(builder, expected);
    }

    @Test
    public void testContinuedClick2() {
        String expected = "<click:run_command:\"test\">Some click<red> that doesn't end here";

        Builder builder = TextComponent.builder()
                .content("Some click").clickEvent(ClickEvent.runCommand("test"))
                .append(" that doesn't end here", b -> b.color(TextColor.RED).clickEvent(ClickEvent.runCommand("test")));

        test(builder, expected);
    }

    @Test
    public void testKeyBind() {
        String expected = "Press <key:key.jump> to jump!";

        Builder builder = TextComponent.builder()
                .content("Press ")
                .append(KeybindComponent.of("key.jump"))
                .append(" to jump!");

        test(builder, expected);
    }

    @Test
    public void testKeyBindWithColor() {
        String expected = "Press <red><key:key.jump> to jump!";

        Builder builder = TextComponent.builder()
                .content("Press ")
                .append(KeybindComponent.of("key.jump").color(TextColor.RED))
                .append(" to jump!", TextColor.RED);

        test(builder, expected);
    }

    private void test(@Nonnull Builder builder, @Nonnull String expected) {
        String string = MiniMessageSerializer.serialize(builder.build());
        assertEquals(expected, string);
    }
}
