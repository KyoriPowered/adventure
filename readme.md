# text

A text library for Minecraft.

[![Build Status](https://travis-ci.org/KyoriPowered/text.svg?branch=master)](https://travis-ci.org/KyoriPowered/text) [![codecov](https://codecov.io/gh/KyoriPowered/text/branch/master/graph/badge.svg)](https://codecov.io/gh/KyoriPowered/text)

#### Example usage:
```java
// Creates a line of text saying "You're a Bunny! Press <key> to jump!", with some colouring and styling.
final Component component = new TextComponent("You're a ")
    .color(TextColor.GRAY)
    .append(new TextComponent("Bunny").color(TextColor.LIGHT_PURPLE))
    .append(new TextComponent("! Press "))
    .append(
        new KeybindComponent("key.jump")
            .color(TextColor.LIGHT_PURPLE)
            .decoration(TextDecoration.BOLD, true)
    )
    .append(new TextComponent(" to jump!"));
// now you can send `component` to something, such as a client
```
