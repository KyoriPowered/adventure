plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
  api(libs.configurate.v4)
  testImplementation(projects.adventureTextSerializerGson)
}

applyJarMetadata("net.kyori.adventure.serializer.configurate4")
