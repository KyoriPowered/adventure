plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.jmh)
}

dependencies {
  api(projects.adventureApi)
  api(libs.gson)
  testImplementation(projects.adventureNbt)
}

applyJarMetadata("net.kyori.adventure.text.serializer.gson")
