plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.option)
  api(projects.adventureApi)
  api(projects.adventureNbt)
  implementation(projects.adventureTextSerializerCommons)
}

applyJarMetadata("net.kyori.adventure.text.serializer.nbt")
