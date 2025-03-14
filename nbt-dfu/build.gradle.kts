plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.dfu8)
  api(projects.adventureNbt)

  testImplementation(projects.adventureDfu)
  testImplementation(projects.adventureKey)
}

indra {
  javaVersions().target(17) // minimum supported by DFU
}

applyJarMetadata("net.kyori.adventure.nbt.dfu")
