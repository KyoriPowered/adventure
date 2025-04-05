plugins {
  id("adventure.common-conventions")
}

dependencies {
  compileOnlyApi(libs.jetbrainsAnnotations)
}

applyJarMetadata("net.kyori.adventure.text.serializer.constant")
