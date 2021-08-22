plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(project(":adventure-api"))
  api(project(":adventure-text-serializer-json"))
  api(project(":adventure-nbt"))
}

applyJarMetadata("net.kyori.adventure.text.serializer.json.legacyimpl")
