# Changelog

# 4.1.0 (xx.11.2020) - the bye bye regex release

* Rewrote the whole parser to use less regex and more formal grammar!  
This means, that the whole parser is much more robust now.
* Added a transformation registry, to disable certain tags and add your own ones!
* Added placeholder resolvers, to better integrate placeholder apis!
* Added markdown flavors, to allow supporting multiple ones. DiscordFlavor for example supports ||sploilers||!
* Added a bunch of convenience aliases, `<strikethrough>` becomes `<st>`. For the full list, see the docs.
* Added ability to handle malformed input lenient or strict  
* Slightly better escape handling (more work to come...)
* More unit tests!
* Updated adventure to 4.2

This doesn't even look that much, now that I wrote this all down, but this release has over three thousand new lines and almost one thousand deleted lines!
This small lib contains now almost five thousand lines of code and one hundred unit tests!

# 4.0.0 (xx.07.2020) - the lost release

* MiniMessage is now part of the Kyori organization!
* Add support for hex colors, rainbows and gradients
* Add templates, a new type of placeholder that works with components
* Dropped bungee impl
* Moved markdown ext into the main project
* Refactored the project, MiniMessage is now the main api
* Moved docs to the adventure docs page https://docs.adventure.kyori.net/minimessage.html
* Moved the project to gradle to align with other kyori projects

# 2.1.0 (12.06.2020) - the rgb release

* Move packages around to avoid confusion between bungee and text impls
* Allow the usage of single quotes to define inner components
* Use localized toLower/Uppercase methods (@mikroskeem)
* Use map lookup over valueOf when resolving stuff (@mikroskeem)
* Add new color tag syntax (in preparation for 1.16 hex colors) (@mikroskeem)
* Support reset tags
* Support pre tags
* Support placeholders in lang tags
* Add [docs](/DOCS.md)
* Add changelog (this, lol)
