package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import net.kyori.text.BlockNbtComponent;
import net.kyori.text.Component;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

public class KyoriTextModule extends Module implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private static final Version VERSION = VersionUtil.parseVersion("3.0.2", "net.kyori", "text-serializer-jackson");


    @Override
    public String getModuleName() {
        return getClass().getName();
    }

    @Override
    public Version version() {
        return VERSION;
    }

    @Override
    public void setupModule(SetupContext context) {
        CustomDeserializers deserializers = new CustomDeserializers();
        deserializers.addDeserializer(BlockNbtComponent.Pos.class, BlockNbtComponentPosDeserializer.INSTANCE);
        deserializers.addDeserializer(Component.class, ComponentDeserializer.INSTANCE);
        deserializers.addDeserializer(ClickEvent.Action.class, NameMapDeserializer.CLICK);
        deserializers.addDeserializer(HoverEvent.Action.class, NameMapDeserializer.HOVER);
        deserializers.addDeserializer(TextColor.class, NameMapDeserializer.COLOR);
        deserializers.addDeserializer(TextDecoration.class, NameMapDeserializer.DECORATION);
        deserializers.addDeserializer(Style.class, StyleDeserializer.INSTANCE);
        deserializers.addDeserializer(TextColorWrapper.class, TextColorWrapper.DESERIALIZER);
        context.addDeserializers(deserializers);

        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(BlockNbtComponent.Pos.class, BlockNbtComponentPosSerializer.INSTANCE);
        serializers.addSerializer(Component.class, ComponentSerializer.INSTANCE);
        serializers.addSerializer(ClickEvent.Action.class, NameMapSerializer.CLICK);
        serializers.addSerializer(HoverEvent.Action.class, NameMapSerializer.HOVER);
        serializers.addSerializer(TextColor.class, NameMapSerializer.COLOR);
        serializers.addSerializer(TextDecoration.class, NameMapSerializer.DECORATION);
        serializers.addSerializer(Style.class, StyleSerializer.INSTANCE);
        context.addSerializers(serializers);
    }
}
