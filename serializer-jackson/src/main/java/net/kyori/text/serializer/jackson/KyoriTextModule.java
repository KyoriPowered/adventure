/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
    return this.getClass().getName();
  }

  @Override
  public Version version() {
    return VERSION;
  }

  @Override
  public void setupModule(final SetupContext context) {
    final KyoriDeserializers deserializers = new KyoriDeserializers();
    deserializers.addDeserializer(BlockNbtComponent.Pos.class, BlockNbtComponentPosDeserializer.INSTANCE);
    deserializers.addDeserializer(Component.class, ComponentDeserializer.INSTANCE);
    deserializers.addDeserializer(ClickEvent.Action.class, NameMapDeserializer.CLICK);
    deserializers.addDeserializer(HoverEvent.Action.class, NameMapDeserializer.HOVER);
    deserializers.addDeserializer(TextColor.class, NameMapDeserializer.COLOR);
    deserializers.addDeserializer(TextDecoration.class, NameMapDeserializer.DECORATION);
    deserializers.addDeserializer(Style.class, StyleDeserializer.INSTANCE);
    deserializers.addDeserializer(TextColorWrapper.class, TextColorWrapper.DESERIALIZER);
    context.addDeserializers(deserializers);

    final SimpleSerializers serializers = new SimpleSerializers();
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
