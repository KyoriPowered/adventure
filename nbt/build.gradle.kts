plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.jspecify)
  compileOnlyApi(libs.jetbrainsAnnotations)
}

applyJarMetadata("net.kyori.adventure.nbt")
