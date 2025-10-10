plugins {
  id("adventure.common-conventions")
}

dependencies {
  compileOnlyApi(libs.jetbrainsAnnotations)
  compileOnlyApi(libs.jspecify)
}

applyJarMetadata("net.kyori.adventure.nbt")
