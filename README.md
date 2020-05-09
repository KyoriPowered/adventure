# MiniMessage
[![CodeFactor](https://www.codefactor.io/repository/github/minidigger/minimessage/badge)](https://www.codefactor.io/repository/github/minidigger/minimessage)
[![GitHub license](https://img.shields.io/github/license/MiniDigger/MiniMessage?style=flat-square)](https://github.com/MiniDigger/MiniMessage/blob/master/LICENSE)
![Maven Central](https://img.shields.io/maven-central/v/me.minidigger/MiniMessage?style=flat-square)

Simple library that implements an easy to use textual format to send rich json messages. 

## Examples

MiniMessage:  
`<yellow>TEST<green>nested</green>Test`  
Json:  
```json
{
  "extra": [
    {
      "color": "yellow",
      "text": "TEST"
    },
    {
      "color": "green",
      "text": "nested"
    },
    {
      "color": "yellow",
      "text": "Test"
    }
  ],
  "text": ""
}
```

MiniMessage:  
`<hover:show_text:"<red>test:TEST">TEST`  
Json:  
```json
{
  "hoverEvent": {
    "action": "show_text",
    "value": [
      {
        "color": "red",
        "text": "test:TEST"
      }
    ]
  },
  "text": "TEST"
}
```

MiniMessage:  
`<click:run_command:/test command>TEST`  
Json:  
```json
{
  "clickEvent": {
    "action": "run_command",
    "value": "/test command"
  },
  "text": "TEST"
}
```

## Usage

These examples use `minimessage-bungee`, but the api and format is the same between the two, `minimessage-text` just returns a Component instead

```java
// simple parsing
String input = "<red>Hai";
BaseComponent[] comp = MiniMessageParser.parseFormat(input);
// parsing with placeholder
String placeholderInput = "<red>Hello <green><guy>"
BaseComponent[] comp2 = MiniMessageParser.parseFormat(placeholderInput, "guy", "You!"); // replaces <guy> with You!
// parsing with placeholder map
Map<String, String> placeholders = new HashMap<>();
placeholders.put("guy", "You!")
BaseComponent[] comp3 = MiniMessageParser.parseFormat(placeholderInput, placeholders);

// serialization
String minimessage = MiniMessageSerializer.serialize(comp3); // use any BaseComponent, or array, and convert it into a nice string
// mini message is '<red>Hello <green>You!'

// escaping (useful for escaping user input, dont want users to send click events ;))
String escaped = MiniMessageParser.escapeTokens(minimessage);
// escaped is '\<red\>Hello \<green\>You!'
// stripping (useful for sending to stuff that doesnt support color)
String stripped = MiniMessageParser.stripTokens(minimessage);
// stripped is 'Hello You!'
```

You should look at the parser tests for way more usage examples. The text version supports stuff like keybinds and translatable components for example!

## Maven Artifact

Artifact:  
```xml
<dependency>
    <groupId>me.minidigger</groupId>
    <artifactId>MiniMessage</artifactId>
    <version>2.0.3</version> <!-- see badge on top of the repo for current version -->
    <scope>compile</scope>
</dependency>
```
Repo (for snapshots):  
```xml
<repository>
    <id>sonatype</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```
Releases get deployed to maven central.

## Upgrading to 2.0

2.0 seperated MiniMessage into two versions, `minimessage-bungee` and `minimessage-text`.  
The bungee version continues to use the Bungee Chat Components, like 1.x did, but the new Version uses the kyori text lib, which has support for way more features.  
If you don't want to change your code, use the old dependency, but you should really consider upgrading to the new one!

## Licence
MIT
