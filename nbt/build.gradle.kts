plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.examination.api)
  api(libs.examination.string)
  compileOnlyApi(libs.jetbrainsAnnotations)
  compileOnlyApi(libs.jspecify)
}

applyJarMetadata("net.kyori.adventure.nbt")
