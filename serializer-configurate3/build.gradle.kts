plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
  api(libs.configurate.v3)
  testImplementation(projects.adventureTextSerializerGson)
}

applyJarMetadata("net.kyori.adventure.serializer.configurate3")
