plugins {
  id("adventure.common-conventions")
}

configurations {
  testCompileOnly {
    extendsFrom(compileOnlyApi.get())
  }
}

dependencies {
  api(libs.examination.api)
  api(libs.examination.string)
  compileOnlyApi(libs.jetbrainsAnnotations)
  implementation(projects.adventureProperties)
  testImplementation(libs.guava)
}

applyJarMetadata("net.kyori.adventure.key")
