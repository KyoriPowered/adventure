package me.minidigger.minimessage.text.fancy;

import net.kyori.adventure.text.Component;

import java.util.function.Function;

public interface Fancy extends Function<Component, Component> {

    void init(int size);
}
