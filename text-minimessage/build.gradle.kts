plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.jmh)
}

description = "A string-based, user-friendly format for representing Minecraft: Java Edition chat components."

dependencies {
  api(projects.adventureApi)
  testImplementation(project(":adventure-text-serializer-plain"))
}

tasks.checkstyleJmh {
  exclude("**")
}

tasks.javadoc {
  exclude("net/kyori/adventure/text/minimessage/internal/**")
}

applyJarMetadata("net.kyori.adventure.text.minimessage")
