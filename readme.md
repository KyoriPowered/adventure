# text

A text library for Minecraft.

[![Build Status](https://travis-ci.org/KyoriPowered/text.svg?branch=master)](https://travis-ci.org/KyoriPowered/text) [![codecov](https://codecov.io/gh/KyoriPowered/text/branch/master/graph/badge.svg)](https://codecov.io/gh/KyoriPowered/text)

#### Example usage:
```java
// Creates a line of text saying "You're a Bunny! Press <key> to jump!", with some colouring and styling.
final TextComponent textComponent = TextComponent.of("You're a ")
    .color(TextColor.GRAY)
    .append(TextComponent.of("Bunny").color(TextColor.LIGHT_PURPLE))
    .append(TextComponent.of("! Press "))
    .append(
        KeybindComponent.of("key.jump")
            .color(TextColor.LIGHT_PURPLE)
            .decoration(TextDecoration.BOLD, true)
    )
    .append(TextComponent.of(" to jump!"));
// now you can send `component` to something, such as a client
```

You can also use a builder, which is mutable, and creates one final component with the children.
```java
// Creates a line of text saying "You're a Bunny! Press <key> to jump!", with some colouring and styling.
final TextComponent textComponent = TextComponent.builder().content("You're a ")
    .color(TextColor.GRAY)
    .append(TextComponent.of("Bunny").color(TextColor.LIGHT_PURPLE))
    .append(TextComponent.of("! Press "))
    .append(
        KeybindComponent.of("key.jump")
            .color(TextColor.LIGHT_PURPLE)
            .decoration(TextDecoration.BOLD, true)
    )
    .append(TextComponent.of(" to jump!"))
	.build();
// now you can send `textComponent` to something, such as a client
```
