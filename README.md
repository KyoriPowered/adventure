# adventure-text-minimessage [![License](https://img.shields.io/github/license/KyoriPowered/adventure-text-minimessage.svg)](https://github.com/KyoriPowered/adventure-text-minimessage/blob/master/license.txt) [![Build Status](https://travis-ci.org/KyoriPowered/adventure-text-minimessage.svg?branch=master)](https://travis-ci.org/KyoriPowered/adventure-text-minimessage)

Simple library that implements an easy to use textual format on top of components, with optional markdown like formatting

### Importing MiniMessage into your project

* Maven
```xml
<dependency>
  <groupId>net.kyori</groupId>
  <artifactId>adventure-text-minimessage</artifactId>
  <version>3.0.0-SNAPSHOT</version>
</dependency>
```
* Gradle
```gradle
repositories {
  mavenCentral()
}

dependencies {
  compile 'net.kyori:adventure-text-minimessage:3.0.0-SNAPSHOT'
}
```

MiniMessage depends on `adventure-text`, so you don't need to provide that, but you might want to use one of the platforms in https://github.com/KyoriPowered/adventure-platform to send components to players.

### Example usage

#### Intro

MiniMessage uses a really simple tag system. For a full breakdown, please refer to [the documentation](https://adventure.docs.kyori.net/), but you can find a few examples below:

`<yellow>Hello <blue>World</blue>!"` -> ![https://i.imgur.com/wB32YpZ.png](https://i.imgur.com/wB32YpZ.png)  
`<red>This is a <green>test!"` ->  ![https://i.imgur.com/vsN3OHa.png](https://i.imgur.com/vsN3OHa.png)  
`<hover:show_text:'<red>test'>TEST"` ->  ![https://i.imgur.com/VsHDPTI.png](https://i.imgur.com/VsHDPTI.png)  
`<yellow>Woo: <rainbow>||||||||||||||||||||||||</rainbow>!"` ->  ![https://i.imgur.com/uNbyoYk.png](https://i.imgur.com/uNbyoYk.png)  

#### MiniMessage -> Components

To parse a MiniMessage into a Component, use the MiniMessage#parse methods. You can send a component via [the adventure adapters](https://github.com/KyoriPowered/adventure-platform)

```java
Component result = MiniMessage.instance().parse("<red>Test");
BukkitPlatform.of(plugin).player(player).sendMessage(result)
```

There is also placeholder support available, either using simple strings, or other components:

```java
Component result = MiniMessage.instance().parse("<red>Hello<bold><reader></bold>", "reader", "You!");
Component result = MiniMessage.instance().parse("<red>Hello<bold><reader></bold>", Template.of("reader", TextComponent.of("You!").color(NamedTextColor.BLUE)));
```

Additionally, there is simple markdown suport available
```java
Component result = MiniMessage.withMarkDown().parse("**<red>BOLD**");
```

#### Components -> MiniMessage

MiniMessage also allows you to convert any component into a string representation, so that you can modify it using string replacement.

```java
Component component = TextComponent.of("Hello!").color(NamedTextColor.RED);
String result = MiniMessage.instance().serialize(component);
result = result.replace("Hello", "Hi");
component = MiniMessage.instance().parse(result);
BukkitPlatform.of(plugin).player(player).sendMessage(result);
```

For more examples and a break down of all supported components, please refer to [the documentation](https://adventure.docs.kyori.net/)

### Changelog

You can find the changelog [here](CHANGELOG.md).
