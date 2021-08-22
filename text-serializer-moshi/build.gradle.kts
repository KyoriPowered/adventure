plugins {
  id("adventure.common-conventions")
  id("me.champeau.jmh")
}

dependencies {
  api(project(":adventure-api"))
  api(project(":adventure-text-serializer-json"))
  api("com.squareup.moshi:moshi:1.12.0")
  testImplementation(project(":adventure-nbt"))
}

applyJarMetadata("net.kyori.adventure.text.serializer.moshi")
