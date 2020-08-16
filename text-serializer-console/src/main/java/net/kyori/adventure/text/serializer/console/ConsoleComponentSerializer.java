package net.kyori.adventure.text.serializer.console;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Created by Narimm on 16/08/2020.
 */
public interface ConsoleComponentSerializer extends ComponentSerializer<Component, Component, String>, Buildable<ConsoleComponentSerializer, ConsoleComponentSerializer.Builder> {

  char ESC_CHAR = '\u001B';

  static @NonNull ConsoleComponentSerializer fullColour() {
    return ConsoleComponentSerializerImpl.trueColor;
  }

  static @NonNull Builder builder() {
    return new ConsoleComponentSerializerImpl.BuilderImpl();
  }

  interface Builder extends Buildable.AbstractBuilder<ConsoleComponentSerializer> {

    @NonNull Builder downSample(final boolean downSample);

    @NonNull
    @Override
    ConsoleComponentSerializer build();
  }


}
