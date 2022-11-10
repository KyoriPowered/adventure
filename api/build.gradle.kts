plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.jmh)
}

configurations {
  testCompileOnly {
    extendsFrom(compileOnlyApi.forUseAtConfigurationTime().get())
  }
}

tasks.withType<JavaCompile> {
  doFirst {
    println( "AnnotationProcessorPath for $name is ${options.annotationProcessorPath?.files}")
  }
}

dependencies {
  api(projects.adventureKey)
  api(libs.examination.api)
  api(libs.examination.string)
  compileOnlyApi(libs.jetbrainsAnnotations)
  testImplementation(libs.guava)
  annotationProcessor(projects.adventureAnnotationProcessors)
}

applyJarMetadata("net.kyori.adventure")
