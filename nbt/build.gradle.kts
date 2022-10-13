plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.examination.api)
  api(libs.examination.string)
  compileOnlyApi(libs.jetbrainsAnnotations)
}

applyJarMetadata("net.kyori.adventure.nbt")
