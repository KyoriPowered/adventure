# adventure [![License](https://img.shields.io/github/license/KyoriPowered/adventure.svg)](https://github.com/KyoriPowered/adventure/blob/master/license.txt) [![Build Status](https://travis-ci.org/KyoriPowered/adventure.svg?branch=master)](https://travis-ci.org/KyoriPowered/adventure)

A serverside user interface library for Minecraft.

#### Artifacts

There are various artifacts:

* `adventure-api` is the core project - you will always want to import this.
* `adventure-text-serializer-gson` is a GSON-based JSON serializer.
* `adventure-text-serializer-legacy` is a legacy character text serializer.
* `adventure-text-serializer-plain` is a plain text serializer.

#### Importing text into your project

* Maven
```xml
<dependency>
  <groupId>net.kyori</groupId>
  <artifactId>adventure-api</artifactId>
  <version>4.0.0-SNAPSHOT</version>
</dependency>
```
* Gradle
```gradle
repositories {
  mavenCentral()
}

dependencies {
  compile 'net.kyori:adventure-api:4.0.0-SNAPSHOT'
}
```

### Example usage

#### Creating components

```java
// Creates a line of text saying "You're a Bunny! Press <key> to jump!", with some colouring and styling.
final TextComponent textComponent = TextComponent.of("You're a ")
  .color(NamedTextColor.GRAY)
  .append(TextComponent.of("Bunny").color(NamedTextColor.LIGHT_PURPLE))
  .append(TextComponent.of("! Press "))
  .append(
    KeybindComponent.of("key.jump")
      .color(NamedTextColor.LIGHT_PURPLE)
      .decoration(TextDecoration.BOLD, true)
  )
  .append(TextComponent.of(" to jump!"));
// now you can send `textComponent` to something, such as a client
```

You can also use a builder, which is mutable, and creates one final component with the children.
```java
// Creates a line of text saying "You're a Bunny! Press <key> to jump!", with some colouring and styling.
final TextComponent textComponent2 = TextComponent.builder().content("You're a ")
  .color(NamedTextColor.GRAY)
  .append(TextComponent.builder("Bunny").color(NamedTextColor.LIGHT_PURPLE).build())
  .append(TextComponent.of("! Press "))
  .append(
    KeybindComponent.builder("key.jump")
      .color(NamedTextColor.LIGHT_PURPLE)
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
  .color(NamedTextColor.GOLD)
  .append(
    TextComponent.of("world")
      .color(NamedTextColor.AQUA).
      decoration(TextDecoration.BOLD, true)
  )
  .append(TextComponent.of("!").color(NamedTextColor.RED));

// Converts textComponent to the JSON form used for serialization by Minecraft.
String json = GsonComponentSerializer.INSTANCE.serialize(textComponent);

// Converts textComponent to a legacy string - "&6Hello &b&lworld&c!"
String legacy = LegacyComponentSerializer.legacy().serialize(textComponent, '&');

// Converts textComponent to a plain string - "Hello world!"
String plain = PlainComponentSerializer.INSTANCE.serialize(textComponent);
```

The same is of course also possible in reverse for deserialization.

```java
// Converts JSON in the form used for serialization by Minecraft to a Component
Component component = GsonComponentSerializer.INSTANCE.deserialize(json);

// Converts a legacy string (using formatting codes) to a TextComponent
TextComponent component = LegacyComponentSerializer.legacy().deserialize("&6Hello &b&lworld&c!", '&');

// Converts a plain string to a TextComponent
TextComponent component = PlainComponentSerializer.INSTANCE.deserialize("Hello world!");
```

#### Using components within your application

The way you use components within your application will of course vary depending on what you're aiming to achieve.

However, the most common task is likely to be sending a component to some sort of Minecraft client. The method for doing this will depend on the platform your program is running on, however it is likely to involve serializing the component to Minecraft's JSON format, and then sending the JSON through another method provided by the platform.

The text library is platform agnostic and therefore doesn't provide any way to send components to clients. However, some platform adapters (which make this easy!) can be found in the [adventure-platform](https://github.com/KyoriPowered/adventure-platform) project.
