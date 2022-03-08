plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
  api(projects.adventureTextSerializerGson)
  api(projects.adventureNbt)
}

applyJarMetadata("net.kyori.adventure.text.serializer.gson.legacyimpl")
