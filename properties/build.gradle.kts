plugins {
  id("adventure.common-conventions")
}

dependencies {
  api(libs.examination.api)
  api(libs.examination.string)
}

applyJarMetadata("net.kyori.adventure.internal.properties")
