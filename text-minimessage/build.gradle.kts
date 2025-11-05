plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.jmh)
}

description = "A string-based, user-friendly format for representing Minecraft: Java Edition chat components."

dependencies {
  api(projects.adventureApi)
  testImplementation(projects.adventureTextSerializerPlain)
  testImplementation(projects.adventureTextSerializerAnsi)
  annotationProcessor(projects.adventureAnnotationProcessors)
}

tasks.checkstyleJmh {
  exclude("**")
}

tasks.javadoc {
  // TODO: I assume this is not welcome, but otherwise the javadoc task fails, so...
  // exclude("net/kyori/adventure/text/minimessage/internal/**")
}

applyJarMetadata("net.kyori.adventure.text.minimessage")
