package minimessage;

import net.kyori.text.Component;
import net.kyori.text.serializer.gson.GsonComponentSerializer;

import org.junit.Test;

import javax.annotation.Nonnull;

import me.minidigger.minimessage.MiniMessageParser;

import static org.junit.Assert.assertEquals;

public class MiniMessageParserTest {

    @Test
    public void test() {
        String input1 = "<yellow>TEST<green> nested</green>Test";
        String input2 = "<yellow>TEST<green> nested<yellow>Test";
        String out1 = GsonComponentSerializer.INSTANCE.serialize(MiniMessageParser.parseFormat(input1));
        String out2 = GsonComponentSerializer.INSTANCE.serialize(MiniMessageParser.parseFormat(input2));
        System.out.println(out1);
        System.out.println(out2);
        assertEquals(out1, out2);
    }

    @Test
    public void testStripSimple() {
        String input = "<yellow>TEST<green> nested</green>Test";
        String expected = "TEST nestedTest";
        assertEquals(expected, MiniMessageParser.stripTokens(input));
    }

    @Test
    public void testStripComplex() {
        String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
        String expected = " random strangerclick here to FEEL it";
        assertEquals(expected, MiniMessageParser.stripTokens(input));
    }

    @Test
    public void testStripInner() {
        String input = "<hover:show_text:\"<red>test:TEST\">TEST";
        String expected = "TEST";
        assertEquals(expected, MiniMessageParser.stripTokens(input));
    }

    @Test
    public void testEscapeSimple() {
        String input = "<yellow>TEST<green> nested</green>Test";
        String expected = "\\<yellow\\>TEST\\<green\\> nested\\</green\\>Test";
        assertEquals(expected, MiniMessageParser.escapeTokens(input));
    }

    @Test
    public void testEscapeComplex() {
        String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
        String expected = "\\<yellow\\>\\<test\\> random \\<bold\\>stranger\\</bold\\>\\<click:run_command:test command\\>\\<underlined\\>\\<red\\>click here\\</click\\>\\<blue\\> to \\<bold\\>FEEL\\</underlined\\> it";
        assertEquals(expected, MiniMessageParser.escapeTokens(input));
    }

    @Test
    public void testEscapeInner() {
        String input = "<hover:show_text:\"<red>test:TEST\">TEST";
        String expected = "\\<hover:show_text:\"\\<red\\>test:TEST\"\\>TEST";
        assertEquals(expected, MiniMessageParser.escapeTokens(input));
    }


    @Test
    public void checkPlaceholder() {
        String input = "<test>";
        String expected = "{\"text\":\"Hello!\"}";
        Component comp = MiniMessageParser.parseFormat(input, "test", "Hello!");

        test(comp, expected);
    }

    @Test
    public void testNiceMix() {
        String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Hello! random \",\"color\":\"yellow\"},{\"text\":\"stranger\",\"color\":\"yellow\",\"bold\":true},{\"text\":\"click here\",\"color\":\"red\",\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\" to \",\"color\":\"blue\",\"underlined\":true},{\"text\":\"FEEL\",\"color\":\"blue\",\"bold\":true,\"underlined\":true},{\"text\":\" it\",\"color\":\"blue\",\"bold\":true}]}";
        Component comp = MiniMessageParser.parseFormat(input, "test", "Hello!");

        test(comp, expected);
    }

    @Test
    public void testColorSimple() {
        String input = "<yellow>TEST";
        String expected = "{\"text\":\"TEST\",\"color\":\"yellow\"}";

        test(input, expected);
    }

    @Test
    public void testColorNested() {
        String input = "<yellow>TEST<green>nested</green>Test";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"TEST\",\"color\":\"yellow\"},{\"text\":\"nested\",\"color\":\"green\"},{\"text\":\"Test\",\"color\":\"yellow\"}]}";

        test(input, expected);
    }

    @Test
    public void testColorNotNested() {
        String input = "<yellow>TEST</yellow><green>nested</green>Test";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"TEST\",\"color\":\"yellow\"},{\"text\":\"nested\",\"color\":\"green\"},{\"text\":\"Test\"}]}";

        test(input, expected);
    }

    @Test
    public void testHover() {
        String input = "<hover:show_text:\"<red>test\">TEST";
        String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"test\",\"color\":\"red\"}}}";

        test(input, expected);
    }

    @Test
    public void testHoverWithColon() {
        String input = "<hover:show_text:\"<red>test:TEST\">TEST";
        String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"test:TEST\",\"color\":\"red\"}}}";

        test(input, expected);
    }

    @Test
    public void testClick() {
        String input = "<click:run_command:test>TEST";
        String expected = "{\"text\":\"TEST\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test\"}}";

        test(input, expected);
    }

    @Test
    public void testClickExtendedCommand() {
        String input = "<click:run_command:test command>TEST";
        String expected = "{\"text\":\"TEST\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}}";

        test(input, expected);
    }

    @Test
    public void testInvalidTag() {
        String input = "<test>";
        String expected = "{\"text\":\"\\u003ctest\\u003e\"}"; // gson makes it html save
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);

        // TODO am not totally happy about this yet, invalid tags arent getting colored for example, but good enough for now
    }

    @Test
    public void testInvalidTagComplex() {
        String input = "<yellow><test> random <bold>stranger</bold><click:run_command:test command><oof></oof><underlined><red>click here</click><blue> to <bold>FEEL</underlined> it";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"\\u003ctest\\u003e\"},{\"text\":\" random \",\"color\":\"yellow\"},{\"text\":\"stranger\",\"color\":\"yellow\",\"bold\":true},{\"text\":\"\\u003coof\\u003e\"},{\"text\":\"\\u003c/oof\\u003e\"},{\"text\":\"click here\",\"color\":\"red\",\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\" to \",\"color\":\"blue\",\"underlined\":true},{\"text\":\"FEEL\",\"color\":\"blue\",\"bold\":true,\"underlined\":true},{\"text\":\" it\",\"color\":\"blue\",\"bold\":true}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    private void test(@Nonnull String input, @Nonnull String expected) {
        test(MiniMessageParser.parseFormat(input), expected);
    }

    private void test(@Nonnull Component comp, @Nonnull String expected) {
        assertEquals(expected, GsonComponentSerializer.INSTANCE.serialize(comp));
    }
}
