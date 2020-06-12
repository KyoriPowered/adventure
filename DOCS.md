# Documentation

> Note: this page mainly covers minimessage-text, although the principles should also apply to the legacy bungee module, just with way fewer features.

## General

First let me start with a few core principles.

This library uses tags. Everything you do will be defined with tags. Tags have a start tag and an end tag (the `<reset>` tag is an exception here)
Start tags are mandatory (obviously), end tags aren't.
`<yellow>Hello <blue>World<yellow>!` and `<yellow>Hello <blue>World</blue>!` and even `<yellow>Hello </yellow><blue>World</blue><yellow>!</yellow>` all do the same.

Some tags have inner tags. Those look like this: `<tag:inner>stuff</tag>`. For example: `<hover:show_text:"<red>test:TEST">TEST` or `<click:run_command:test>TEST`  
As you can see, those sometimes contain components, sometimes just strings. Refer to the details docs below.


## Components

### Color

Color the next parts

Tag: `<_colorname_>`  
Arguments: `_colorname_`, all minecraft color constants (check [here](https://github.com/KyoriPowered/adventure/blob/master/api/src/main/java/net/kyori/text/format/TextColor.java))  
Examples:
* `<yellow>Hello <blue>World</blue>!` ![https://i.imgur.com/wB32YpZ.png](https://i.imgur.com/wB32YpZ.png)
* `<red>This is a <green>test!` ![https://i.imgur.com/vsN3OHa.png](https://i.imgur.com/vsN3OHa.png)

### Color (2)

A different, more flexible way (supports hex colors!) for colors looks like this

Tag: `<color:_colorNameOrHex_>`  
Arguments: `_colorNameOrHex_`, can be all the values from above, or hex colors (in 1.16)  
Examples:
* `<color:yellow>Hello <color:blue>World</color:blue>!` ![https://i.imgur.com/wB32YpZ.png](https://i.imgur.com/wB32YpZ.png)
* `<color:#FF5555>This is a <color:#55FF55>test!` ![https://i.imgur.com/vsN3OHa.png](https://i.imgur.com/vsN3OHa.png)

### Decoration

Decorate the next parts

Tag: `<_decorationname_>`  
Arguments:  `_decorationname_` , all minecraft decorations (check [here](https://github.com/KyoriPowered/adventure/blob/master/api/src/main/java/net/kyori/text/format/TextDecoration.java))  
Examples:
* `<underlined>This is <bold>important</bold>!` ![https://i.imgur.com/hREGXQy.png](https://i.imgur.com/hREGXQy.png)

### Reset

Reset all colors, decorations, hovers etc. Doesn't have a close tag

Tag: `<reset>`  
Arguments: non  
Examples: 
* `<yellow><bold>Hello <reset>world!` ![https://i.imgur.com/bjInUhj.png](https://i.imgur.com/bjInUhj.png)

### Click

TODO

### Hover

TODO

### Keybind

Allows displaying the configured key for actions

Tag: `<key:_key_>`  
Arguments: `_key_`, the minecraft key of the action  
Examples:
* `Press <red><key:key.jump> to jump!` ![https://i.imgur.com/iQmNDF6.png](https://i.imgur.com/iQmNDF6.png)

### Translatable

Allows displaying minecraft messages using the player locale

Tag: `<lang:_key_>`  
Arguments: `_key_`, the translation key   
Examples:
* `You should get a <lang:block.minecraft.diamond_block>!` ![https://i.imgur.com/mpdDMF6.png](https://i.imgur.com/mpdDMF6.png)

### Insertion

Allow insertion of text into chat via shift click

Tag: `<insertion:_text_>`  
Arguments: `_text_`, the text to insert
Examples:
* `Click <insert:test>this</insert> to insert!` ![https://i.imgur.com/Imhom84.png](https://i.imgur.com/Imhom84.png)

### Pre

Tags within this tag will not be parsed, useful for player input for example

Tag: `<pre>`  
Arguments: non  
Examples: 
* `<gray><<yellow><player><gray>> <reset><pre><message></pre>` ![https://i.imgur.com/pQqaJnD.png](https://i.imgur.com/pQqaJnD.png)
