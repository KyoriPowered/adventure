plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.dfu8)
  api(projects.adventureNbtDfu)
  api(projects.adventureApi)
  api(projects.adventureTextSerializerCommons)
}

indra {
  javaVersions().target(17) // minimum supported by DFU
}

applyJarMetadata("net.kyori.adventure.dfu")
