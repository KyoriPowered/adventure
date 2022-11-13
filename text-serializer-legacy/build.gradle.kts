plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
  annotationProcessor(projects.adventureAnnotationProcessors)
}

applyJarMetadata("net.kyori.adventure.text.serializer.legacy")
