plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.jmh)
}

configurations {
  testCompileOnly {
    extendsFrom(compileOnlyApi.get())
  }
}

dependencies {
  api(projects.adventureKey)
  compileOnlyApi(libs.jetbrainsAnnotations)
  testImplementation(libs.guava)
  annotationProcessor(projects.adventureAnnotationProcessors)
  testCompileOnly(libs.autoService.annotations)
  testAnnotationProcessor(libs.autoService)
}

applyJarMetadata("net.kyori.adventure")
