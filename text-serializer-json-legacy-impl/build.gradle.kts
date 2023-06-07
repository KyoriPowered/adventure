plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
  api(projects.adventureTextSerializerJson)
  api(projects.adventureNbt)
}

applyJarMetadata("net.kyori.adventure.text.serializer.json.legacyimpl")
