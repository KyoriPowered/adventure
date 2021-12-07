plugins {
  id("adventure.common-conventions")
  id("me.champeau.jmh")
}

description = "A string-based, user-friendly format for representing Minecraft: Java Edition chat components."

dependencies {
  api(project(":adventure-api"))
  testImplementation(project(":adventure-text-serializer-plain"))
}

tasks.checkstyleJmh {
  exclude("**")
}

applyJarMetadata("net.kyori.adventure.text.minimessage")
