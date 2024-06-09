plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.option)
  api(projects.adventureApi)
  api(projects.adventureNbt)
  api(projects.adventureTextSerializerGson)
}

applyJarMetadata("net.kyori.adventure.text.serializer.nbt")
