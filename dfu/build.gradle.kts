plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.dfu8)
  api(projects.adventureTextSerializerGson)
}

indra {
  javaVersions().target(17) // minimum supported by DFU
}

applyJarMetadata("net.kyori.adventure.dfu")
