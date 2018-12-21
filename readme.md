# text [![License](https://img.shields.io/github/license/KyoriPowered/text.svg)](https://github.com/KyoriPowered/text/blob/master/license.txt) [![Build Status](https://travis-ci.org/KyoriPowered/text.svg?branch=master)](https://travis-ci.org/KyoriPowered/text) [![codecov](https://codecov.io/gh/KyoriPowered/text/branch/master/graph/badge.svg)](https://codecov.io/gh/KyoriPowered/text)

A text library for Minecraft.

#### Versions

There are currently two release branches for text.

* The `1.12` version targets Minecraft 1.12 and above.
    * Can be found under the `master` Git branch.
    * The version string is prefixed with `1.12-`.
    * Uses Guava `21.0` and GSON `2.8.0`.
    * Includes the `KeybindComponent` type.
* The `1.11` version targets Minecraft 1.11 and below.
    * Can be found under the `1.11` Git branch.
    * The version string is prefixed with `1.11-`.
    * Uses Guava `17.0` and GSON `2.2.0`.

The full diff between the 1.12 and 1.11 branches can be viewed [here](https://github.com/KyoriPowered/text/compare/1.11..master).

#### Importing text into your project

* Maven
```xml
<dependency>
    <groupId>net.kyori</groupId>
    <artifactId>text</artifactId>
    <version>1.12-1.6.4</version>
</dependency>
```
* Gradle
```gradle
repositories {
    mavenCentral()
}
dependencies {
    compile 'net.kyori:text:1.12-1.6.4'
}
```

`1.12` can be substituted with `1.11` for the other branch.

### Example usage

#### Creating components

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
// now you can send `textComponent` to something, such as a client
```

You can also use a builder, which is mutable, and creates one final component with the children.
```java
// Creates a line of text saying "You're a Bunny! Press <key> to jump!", with some colouring and styling.
final TextComponent textComponent2 = TextComponent.builder().content("You're a ")
  .color(TextColor.GRAY)
  .append(TextComponent.builder("Bunny").color(TextColor.LIGHT_PURPLE).build())
  .append(TextComponent.of("! Press "))
  .append(
    KeybindComponent.builder("key.jump")
      .color(TextColor.LIGHT_PURPLE)
      .decoration(TextDecoration.BOLD, true)
      .build()
  )
  .append(TextComponent.of(" to jump!"))
  .build();
// now you can send `textComponent2` to something, such as a client
```

#### Serializing and deserializing components

Serialization to JSON, legacy and plain representations is also supported.

```java
// Creates a text component
final TextComponent textComponent = TextComponent.of("Hello ")
  .color(TextColor.GOLD)
  .append(
    TextComponent.of("world")
      .color(TextColor.AQUA).
      decoration(TextDecoration.BOLD, true)
  )
  .append(TextComponent.of("!").color(TextColor.RED));

// Converts textComponent to the JSON form used for serialization by Minecraft.
String json = ComponentSerializers.JSON.serialize(textComponent);

// Converts textComponent to a legacy string - "&6Hello &b&lworld&c!"
String legacy = ComponentSerializers.LEGACY.serialize(textComponent, '&');

// Converts textComponent to a plain string - "Hello world!"
String plain = ComponentSerializers.PLAIN.serialize(textComponent);
```

The same is of course also possible in reverse for deserialization.

```java
// Converts JSON in the form used for serialization by Minecraft to a Component
Component component = ComponentSerializers.JSON.deserialize(json);

// Converts a legacy string (using formatting codes) to a TextComponent
TextComponent component = ComponentSerializers.LEGACY.deserialize("&6Hello &b&lworld&c!", '&');

// Converts a plain string to a TextComponent
TextComponent component = ComponentSerializers.PLAIN.deserialize("Hello world!");
```

#### Using components within your application

The way you use components within your application will of course vary depending on what you're aiming to achieve.

However, the most common task is likely to be sending a component to some sort of Minecraft client. The method for doing this will depend on the platform your program is running on, however it is likely to involve serializing the component to Minecraft's JSON format, and then sending the JSON through another method provided by the platform.

The text library is platform agnostic and therefore doesn't provide any way to send components to clients. However, some platform adapters (which make this easy!) can be found in the [text-extras](https://github.com/KyoriPowered/text-extras) project.
