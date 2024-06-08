plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.option)
  api(projects.adventureApi)
  api(projects.adventureNbt)
}

applyJarMetadata("net.kyori.adventure.text.serializer.nbt")
