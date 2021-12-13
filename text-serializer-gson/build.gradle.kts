plugins {
  id("adventure.common-conventions")
  id("me.champeau.jmh")
}

dependencies {
  api(project(":adventure-api"))
  api(project(":adventure-text-serializer-json"))
  api("com.google.code.gson:gson:2.8.0")
  testImplementation(project(":adventure-nbt"))
}

applyJarMetadata("net.kyori.adventure.text.serializer.gson")
