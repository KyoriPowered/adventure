plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(projects.adventureApi)
  api(libs.ansi)
}

applyJarMetadata("net.kyori.adventure.text.serializer.ansi")
