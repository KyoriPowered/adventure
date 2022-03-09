plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
}

applyJarMetadata("net.kyori.adventure.text.serializer.plain")
