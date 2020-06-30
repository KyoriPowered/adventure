package net.kyori.adventure.text.minimessage.fancy;

import net.kyori.adventure.text.Component;

import java.util.function.Function;

public interface Fancy extends Function<Component, Component> {

  void init(int size);
}
