pluginManagement {
  includeBuild("build-logic")
}

rootProject.name = "adventure-parent"

// Make sure to update bom/build.gradle.kts when making changes to modules.

sequenceOf(
  "api",
  "bom",
  "extra-kotlin",
  "key",
  "nbt",
  "serializer-configurate3",
  "serializer-configurate4",
  "text-minimessage",
  "text-serializer-gson",
  "text-serializer-gson-legacy-impl",
  "text-serializer-legacy",
  "text-serializer-plain"
).forEach {
  include("adventure-$it")
  project(":adventure-$it").projectDir = file(it)
}
