plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.jmh)
}

dependencies {
  api(projects.adventureApi)
  api(libs.gson)
  testImplementation(projects.adventureNbt)
  annotationProcessor(projects.adventureAnnotationProcessors)
}

applyJarMetadata("net.kyori.adventure.text.serializer.gson")
