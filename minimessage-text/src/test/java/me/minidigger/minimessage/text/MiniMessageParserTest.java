package me.minidigger.minimessage.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Nonnull;

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
    public void testNewColor() {
        String input1 = "<color:yellow>TEST<color:green> nested</color:green>Test";
        String input2 = "<color:yellow>TEST<color:green> nested<color:yellow>Test";
        String out1 = GsonComponentSerializer.INSTANCE.serialize(MiniMessageParser.parseFormat(input1));
        String out2 = GsonComponentSerializer.INSTANCE.serialize(MiniMessageParser.parseFormat(input2));

        assertEquals(out1, out2);
    }

    @Test
    public void testHexColor() {
        String input1 = "<color:#ff00ff>TEST<color:#00ff00> nested</color:#00ff00>Test";
        String input2 = "<color:#ff00ff>TEST<color:#00ff00> nested<color:#ff00ff>Test";
        String out1 = GsonComponentSerializer.INSTANCE.serialize(MiniMessageParser.parseFormat(input1));
        String out2 = GsonComponentSerializer.INSTANCE.serialize(MiniMessageParser.parseFormat(input2));

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
        String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"test\",\"color\":\"red\"}}}";

        test(input, expected);
    }

    @Test
    public void testHover2() {
        String input = "<hover:show_text:'<red>test'>TEST";
        String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"test\",\"color\":\"red\"}}}";

        test(input, expected);
    }

    @Test
    public void testHoverWithColon() {
        String input = "<hover:show_text:\"<red>test:TEST\">TEST";
        String expected = "{\"text\":\"TEST\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"test:TEST\",\"color\":\"red\"}}}";

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
        String input = "<click:run_command:/test command>TEST";
        String expected = "{\"text\":\"TEST\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/test command\"}}";

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
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"\\u003ctest\\u003e\",\"color\":\"yellow\"},{\"text\":\" random \",\"color\":\"yellow\"},{\"text\":\"stranger\",\"color\":\"yellow\",\"bold\":true},{\"text\":\"\\u003coof\\u003e\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\"\\u003c/oof\\u003e\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\"click here\",\"color\":\"red\",\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"test command\"}},{\"text\":\" to \",\"color\":\"blue\",\"underlined\":true},{\"text\":\"FEEL\",\"color\":\"blue\",\"bold\":true,\"underlined\":true},{\"text\":\" it\",\"color\":\"blue\",\"bold\":true}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testKeyBind() {
        String input = "Press <key:key.jump> to jump!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Press \"},{\"keybind\":\"key.jump\"},{\"text\":\" to jump!\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testKeyBindWithColor() {
        String input = "Press <red><key:key.jump> to jump!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Press \"},{\"keybind\":\"key.jump\",\"color\":\"red\"},{\"text\":\" to jump!\",\"color\":\"red\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testTranslatable() {
        String input = "You should get a <lang:block.minecraft.diamond_block>!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"You should get a \"},{\"translate\":\"block.minecraft.diamond_block\"},{\"text\":\"!\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testTranslatableWith() {
        String input = "Test: <lang:commands.drop.success.single:'<red>1':'<blue>Stone'>!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Test: \"},{\"translate\":\"commands.drop.success.single\",\"with\":[{\"text\":\"1\",\"color\":\"red\"},{\"text\":\"Stone\",\"color\":\"blue\"}]},{\"text\":\"!\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    @Ignore  // TODO implement inner with ' or "
    public void testTranslatableWithHover() {
        String input = "Test: <lang:commands.drop.success.single:'<red>1<hover:show_text:'<red>dum'>':'<blue>Stone'>!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Test: \"},{\"translate\":\"commands.drop.success.single\",\"with\":[{\"text\":\"1\",\"color\":\"red\"},{\"text\":\"Stone\",\"color\":\"blue\"}]},{\"text\":\"!\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testKingAlter() {
        String input = "Ahoy <lang:offset.-40:'<red>mates!'>";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Ahoy \"},{\"translate\":\"offset.-40\",\"with\":[{\"text\":\"mates!\",\"color\":\"red\"}]}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testInsertion() {
        String input = "Click <insert:test>this</insert> to insert!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Click \"},{\"text\":\"this\",\"insertion\":\"test\"},{\"text\":\" to insert!\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testGH5() {
        String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:<pack_url>><hover:show_text:\"<green>/!\\ install it from Options/ResourcePacks in your game\"><green><bold>CLICK HERE</bold></hover></click>";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"»\",\"color\":\"dark_gray\"},{\"text\":\" To download it from the internet, \",\"color\":\"gray\"},{\"text\":\"CLICK HERE\",\"color\":\"green\",\"bold\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"/!\\\\ install it from Options/ResourcePacks in your game\",\"color\":\"green\"}}}]}";

        // should work
        Component comp1 = MiniMessageParser.parseFormat(input, "pack_url", "https://www.google.com");
        test(comp1, expected);

        // shouldnt throw an error
        MiniMessageParser.parseFormat(input, "url", "https://www.google.com");
    }

    @Test
    @Ignore // TODO implement inner with ' or "
    public void testGH5Modified() {
        String input = "<dark_gray>»<gray> To download it from the internet, <click:open_url:<pack_url>><hover:show_text:\"<green>/!\\ install it from 'Options/ResourcePacks' in your game\"><green><bold>CLICK HERE</bold></hover></click>";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"»\",\"color\":\"dark_gray\"},{\"text\":\" To download it from the internet, \",\"color\":\"gray\"},{\"text\":\"CLICK HERE\",\"color\":\"green\",\"bold\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"/!\\\\ install it from 'Options/ResourcePacks' in your game\",\"color\":\"green\"}}}]}";

        // should work
        Component comp1 = MiniMessageParser.parseFormat(input, "pack_url", "https://www.google.com");
        test(comp1, expected);

        // shouldnt throw an error
        MiniMessageParser.parseFormat(input, "url", "https://www.google.com");
    }

    @Test
    public void testReset() {
        String input = "Click <yellow><insert:test>this<reset> to insert!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Click \"},{\"text\":\"this\",\"color\":\"yellow\",\"insertion\":\"test\"},{\"text\":\" to insert!\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testPre() {
        String input = "Click <yellow><pre><insert:test>this</pre> to <red>insert!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Click \"},{\"text\":\"\\u003cinsert:test\\u003e\",\"color\":\"yellow\"},{\"text\":\"this\",\"color\":\"yellow\"},{\"text\":\" to \",\"color\":\"yellow\"},{\"text\":\"insert!\",\"color\":\"red\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testRainbow() {
        String input = "<yellow>Woo: <rainbow>||||||||||||||||||||||||</rainbow>!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"extra\":[{\"text\":\"|\",\"color\":\"#e1a00d\"},{\"text\":\"|\",\"color\":\"#c9bf03\"},{\"text\":\"|\",\"color\":\"#acd901\"},{\"text\":\"|\",\"color\":\"#8bed08\"},{\"text\":\"|\",\"color\":\"#6afa16\"},{\"text\":\"|\",\"color\":\"#4bff2c\"},{\"text\":\"|\",\"color\":\"#2ffa48\"},{\"text\":\"|\",\"color\":\"#18ed68\"},{\"text\":\"|\",\"color\":\"#08d989\"},{\"text\":\"|\",\"color\":\"#01bfa9\"},{\"text\":\"|\",\"color\":\"#02a0c7\"},{\"text\":\"|\",\"color\":\"#0c80e0\"},{\"text\":\"|\",\"color\":\"#1e5ff2\"},{\"text\":\"|\",\"color\":\"#3640fc\"},{\"text\":\"|\",\"color\":\"#5326fe\"},{\"text\":\"|\",\"color\":\"#7412f7\"},{\"text\":\"|\",\"color\":\"#9505e9\"},{\"text\":\"|\",\"color\":\"#b401d3\"},{\"text\":\"|\",\"color\":\"#d005b7\"},{\"text\":\"|\",\"color\":\"#e71297\"},{\"text\":\"|\",\"color\":\"#f72676\"},{\"text\":\"|\",\"color\":\"#fe4056\"},{\"text\":\"|\",\"color\":\"#fd5f38\"}],\"color\":\"#f3801f\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testRainbowPhase() {
        String input = "<yellow>Woo: <rainbow:2>||||||||||||||||||||||||</rainbow>!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"extra\":[{\"text\":\"|\",\"color\":\"#0de17d\"},{\"text\":\"|\",\"color\":\"#03c99e\"},{\"text\":\"|\",\"color\":\"#01acbd\"},{\"text\":\"|\",\"color\":\"#088bd7\"},{\"text\":\"|\",\"color\":\"#166aec\"},{\"text\":\"|\",\"color\":\"#2c4bf9\"},{\"text\":\"|\",\"color\":\"#482ffe\"},{\"text\":\"|\",\"color\":\"#6818fb\"},{\"text\":\"|\",\"color\":\"#8908ef\"},{\"text\":\"|\",\"color\":\"#a901db\"},{\"text\":\"|\",\"color\":\"#c702c1\"},{\"text\":\"|\",\"color\":\"#e00ca3\"},{\"text\":\"|\",\"color\":\"#f21e82\"},{\"text\":\"|\",\"color\":\"#fc3661\"},{\"text\":\"|\",\"color\":\"#fe5342\"},{\"text\":\"|\",\"color\":\"#f77428\"},{\"text\":\"|\",\"color\":\"#e99513\"},{\"text\":\"|\",\"color\":\"#d3b406\"},{\"text\":\"|\",\"color\":\"#b7d001\"},{\"text\":\"|\",\"color\":\"#97e704\"},{\"text\":\"|\",\"color\":\"#76f710\"},{\"text\":\"|\",\"color\":\"#56fe24\"},{\"text\":\"|\",\"color\":\"#38fd3e\"}],\"color\":\"#1ff35c\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testGradient() {
        String input = "<yellow>Woo: <gradient>||||||||||||||||||||||||</gradient>!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"extra\":[{\"text\":\"|\",\"color\":\"#f4f4f4\"},{\"text\":\"|\",\"color\":\"#e9e9e9\"},{\"text\":\"|\",\"color\":\"#dedede\"},{\"text\":\"|\",\"color\":\"#d3d3d3\"},{\"text\":\"|\",\"color\":\"#c8c8c8\"},{\"text\":\"|\",\"color\":\"#bcbcbc\"},{\"text\":\"|\",\"color\":\"#b1b1b1\"},{\"text\":\"|\",\"color\":\"#a6a6a6\"},{\"text\":\"|\",\"color\":\"#9b9b9b\"},{\"text\":\"|\",\"color\":\"#909090\"},{\"text\":\"|\",\"color\":\"#858585\"},{\"text\":\"|\",\"color\":\"#7a7a7a\"},{\"text\":\"|\",\"color\":\"#6f6f6f\"},{\"text\":\"|\",\"color\":\"#646464\"},{\"text\":\"|\",\"color\":\"#595959\"},{\"text\":\"|\",\"color\":\"#4e4e4e\"},{\"text\":\"|\",\"color\":\"#434343\"},{\"text\":\"|\",\"color\":\"#373737\"},{\"text\":\"|\",\"color\":\"#2c2c2c\"},{\"text\":\"|\",\"color\":\"#212121\"},{\"text\":\"|\",\"color\":\"#161616\"},{\"text\":\"|\",\"color\":\"#0b0b0b\"},{\"text\":\"|\",\"color\":\"black\"}],\"color\":\"white\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testGradient2() {
        String input = "<yellow>Woo: <gradient:#5e4fa2:#f79459>||||||||||||||||||||||||</gradient>!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"extra\":[{\"text\":\"|\",\"color\":\"#65529f\"},{\"text\":\"|\",\"color\":\"#6b559c\"},{\"text\":\"|\",\"color\":\"#725898\"},{\"text\":\"|\",\"color\":\"#795b95\"},{\"text\":\"|\",\"color\":\"#7f5e92\"},{\"text\":\"|\",\"color\":\"#86618f\"},{\"text\":\"|\",\"color\":\"#8d648c\"},{\"text\":\"|\",\"color\":\"#936789\"},{\"text\":\"|\",\"color\":\"#9a6a85\"},{\"text\":\"|\",\"color\":\"#a16d82\"},{\"text\":\"|\",\"color\":\"#a7707f\"},{\"text\":\"|\",\"color\":\"#ae737c\"},{\"text\":\"|\",\"color\":\"#b47679\"},{\"text\":\"|\",\"color\":\"#bb7976\"},{\"text\":\"|\",\"color\":\"#c27c72\"},{\"text\":\"|\",\"color\":\"#c87f6f\"},{\"text\":\"|\",\"color\":\"#cf826c\"},{\"text\":\"|\",\"color\":\"#d68569\"},{\"text\":\"|\",\"color\":\"#dc8866\"},{\"text\":\"|\",\"color\":\"#e38b63\"},{\"text\":\"|\",\"color\":\"#ea8e5f\"},{\"text\":\"|\",\"color\":\"#f0915c\"},{\"text\":\"|\",\"color\":\"#f79459\"}],\"color\":\"#5e4fa2\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
        Component comp = MiniMessageParser.parseFormat(input);

        test(comp, expected);
    }

    @Test
    public void testGradient3() {
        String input = "<yellow>Woo: <gradient:green:blue>||||||||||||||||||||||||</gradient>!";
        String expected = "{\"text\":\"\",\"extra\":[{\"text\":\"Woo: \",\"color\":\"yellow\"},{\"text\":\"|\",\"extra\":[{\"text\":\"|\",\"color\":\"#55f85c\"},{\"text\":\"|\",\"color\":\"#55f064\"},{\"text\":\"|\",\"color\":\"#55e96b\"},{\"text\":\"|\",\"color\":\"#55e173\"},{\"text\":\"|\",\"color\":\"#55da7a\"},{\"text\":\"|\",\"color\":\"#55d381\"},{\"text\":\"|\",\"color\":\"#55cb89\"},{\"text\":\"|\",\"color\":\"#55c490\"},{\"text\":\"|\",\"color\":\"#55bc98\"},{\"text\":\"|\",\"color\":\"#55b59f\"},{\"text\":\"|\",\"color\":\"#55aea6\"},{\"text\":\"|\",\"color\":\"#55a6ae\"},{\"text\":\"|\",\"color\":\"#559fb5\"},{\"text\":\"|\",\"color\":\"#5598bc\"},{\"text\":\"|\",\"color\":\"#5590c4\"},{\"text\":\"|\",\"color\":\"#5589cb\"},{\"text\":\"|\",\"color\":\"#5581d3\"},{\"text\":\"|\",\"color\":\"#557ada\"},{\"text\":\"|\",\"color\":\"#5573e1\"},{\"text\":\"|\",\"color\":\"#556be9\"},{\"text\":\"|\",\"color\":\"#5564f0\"},{\"text\":\"|\",\"color\":\"#555cf8\"},{\"text\":\"|\",\"color\":\"blue\"}],\"color\":\"green\"},{\"text\":\"!\",\"color\":\"yellow\"}]}";
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
